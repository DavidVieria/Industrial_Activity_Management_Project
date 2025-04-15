CREATE OR REPLACE FUNCTION get_required_parts_for_order (
    p_OrderID IN "Order".OrderID%TYPE
)
    RETURN SYS_REFCURSOR
    IS
    parts_cursor SYS_REFCURSOR;
    v_part_count NUMBER;

    -- Defining custom exceptions correctly
    INVALID_ORDER_ID EXCEPTION;
    NO_PARTS_FOUND EXCEPTION;

BEGIN
    -- Input validation: Check if OrderID is null or invalid
    IF p_OrderID IS NULL OR p_OrderID <= 0 THEN
        RAISE INVALID_ORDER_ID;
    END IF;

    -- Check if there are parts for the order before opening the cursor
    SELECT COUNT(*)
    INTO v_part_count
    FROM "Order" o
             JOIN ProductOrder po ON o.OrderID = po.OrderID
             JOIN Product p ON po.ProductID = p.ProductID
             JOIN BOO b ON p.ProductID = b.ProductID
             JOIN ProductBOOOperation pbo ON b.ProductID = pbo.ProductID
             JOIN Operation op ON pbo.OperationID = op.OperationID
             JOIN OperationInput oi ON op.OperationID = oi.OperationID
             JOIN Part pa ON oi.PartID = pa.PartID
             JOIN StockPart sp ON pa.PartID = sp.StockPartID
    WHERE o.OrderID = p_OrderID;

    -- If there are no parts, raise an exception
    IF v_part_count = 0 THEN
        RAISE NO_PARTS_FOUND;
    END IF;

    -- Otherwise, open the cursor for the required parts
    OPEN parts_cursor FOR
        SELECT
            sp.StockPartID AS part_id,
            SUM(po.ProductQuantity * oi.Quantity) AS required_quantity
        FROM
            "Order" o
                JOIN ProductOrder po ON o.OrderID = po.OrderID
                JOIN Product p ON po.ProductID = p.ProductID
                JOIN BOO b ON p.ProductID = b.ProductID
                JOIN ProductBOOOperation pbo ON b.ProductID = pbo.ProductID
                JOIN Operation op ON pbo.OperationID = op.OperationID
                JOIN OperationInput oi ON op.OperationID = oi.OperationID
                JOIN Part pa ON oi.PartID = pa.PartID
                JOIN StockPart sp ON pa.PartID = sp.StockPartID
        WHERE
            o.OrderID = p_OrderID
        GROUP BY
            sp.StockPartID;

    -- Return the cursor with the required parts
    RETURN parts_cursor;

EXCEPTION
    WHEN INVALID_ORDER_ID THEN
        -- Raise a custom exception if the order is not found
        RAISE_APPLICATION_ERROR(-20001, 'ERROR: Order ID cannot be empty or invalid.');
        RETURN NULL;
    WHEN NO_PARTS_FOUND THEN
        -- Raise a custom exception if the cursor is empty
        RAISE_APPLICATION_ERROR(-20002, 'ERROR: Order ID ' || p_OrderID || ' does not require any stockable parts.');
        RETURN NULL;
    WHEN OTHERS THEN
        -- Raise a generic exception in case of an unexpected error
        RAISE_APPLICATION_ERROR(-20003, 'An error occurred: ' || SQLERRM);
        -- Return NULL in case of an error
        RETURN NULL;
END;
/

