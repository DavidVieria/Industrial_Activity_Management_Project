CREATE OR REPLACE FUNCTION productionPlanOperations(p_ProductID IN VARCHAR2)
    RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        WITH ProductOperationsCTE (
            ProductID,
            OperationID,
            OperationNumber,
            OperationTypeName,
            WorkstationTypeName,
            ExpectedExecutionTime,
            InputPartID,
            InputQuantity,
            OutputPartID,
            OutputQuantity,
            Level_Depth
        ) AS (
            -- Base case: direct operations of the product
            SELECT
                pbo.ProductID,
                pbo.OperationID,
                pbo.OperationNumber,
                ot.OperationTypeName,
                wst.WorkstationTypeName,
                op.ExpectedExecutionTime,
                oi.PartID,
                oi.Quantity,
                oo.ManufacturedPartID,
                oo.Quantity,
                1 as Level_Depth
            FROM ProductBOOOperation pbo
            JOIN Operation op ON pbo.OperationID = op.OperationID
            JOIN OperationType ot ON op.OperationTypeID = ot.OperationTypeID
            JOIN CanBeDoneAt cbda ON ot.OperationTypeID = cbda.OperationTypeID
            JOIN WorkstationType wst ON cbda.WorkstationTypeID = wst.WorkstationTypeID
            JOIN OperationInput oi ON pbo.OperationID = oi.OperationID
            JOIN OperationOutput oo ON pbo.OperationID = oo.OperationID
            WHERE pbo.ProductID = p_ProductID

            UNION ALL

            -- Recursive case: operations of subproducts
            SELECT
                pbo.ProductID,
                pbo.OperationID,
                pbo.OperationNumber,
                ot.OperationTypeName,
                wst.WorkstationTypeName,
                op.ExpectedExecutionTime,
                oi.PartID,
                oi.Quantity,
                oo.ManufacturedPartID,
                oo.Quantity,
                cte.Level_Depth + 1
            FROM ProductBOOOperation pbo
            JOIN Operation op ON pbo.OperationID = op.OperationID
            JOIN OperationType ot ON op.OperationTypeID = ot.OperationTypeID
            JOIN CanBeDoneAt cbda ON ot.OperationTypeID = cbda.OperationTypeID
            JOIN WorkstationType wst ON cbda.WorkstationTypeID = wst.WorkstationTypeID
            JOIN OperationInput oi ON pbo.OperationID = oi.OperationID
            JOIN OperationOutput oo ON pbo.OperationID = oo.OperationID
            JOIN ProductOperationsCTE cte ON pbo.ProductID = cte.InputPartID
            WHERE cte.Level_Depth < 10  -- Prevent infinite recursion
        )

        SELECT DISTINCT
            ProductID,
            OperationID,
            OperationNumber,
            OperationTypeName,
            WorkstationTypeName,
            ExpectedExecutionTime,
            InputPartID,
            InputQuantity,
            OutputPartID,
            OutputQuantity,
            Level_Depth
        FROM ProductOperationsCTE
        ORDER BY Level_Depth, OperationNumber;

    RETURN v_cursor;
END;
/



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



CREATE OR REPLACE FUNCTION check_materials_in_stock_for_order (
    p_OrderID "Order".OrderID%TYPE
) RETURN NUMBER IS
    v_required_parts_cursor SYS_REFCURSOR;
    v_part_id StockPart.StockPartID%TYPE;
    v_required_quantity NUMBER;
    v_current_stock NUMBER;
    v_is_stock_sufficient NUMBER := 1; -- Assume stock is sufficient by default (1 for true, 0 for false)
BEGIN
    -- Retrieve required parts for the order
    v_required_parts_cursor := get_required_parts_for_order(p_OrderID);

    -- Log the check
    DBMS_OUTPUT.PUT_LINE('Checking stock for Order ID ' || p_OrderID || ':');

    -- Verify stock for each part
    LOOP
        FETCH v_required_parts_cursor INTO v_part_id, v_required_quantity;
        EXIT WHEN v_required_parts_cursor%NOTFOUND;

        -- Get current stock
        SELECT CurrentStock INTO v_current_stock
        FROM StockPart
        WHERE StockPartID = v_part_id;

        -- Check if stock is sufficient
        IF v_current_stock < v_required_quantity THEN
            v_is_stock_sufficient := 0;
            DBMS_OUTPUT.PUT_LINE('- Insufficient stock for Part ID ' || v_part_id ||
                                 '. Required: ' || v_required_quantity || ', Available: ' || v_current_stock || '.');
            EXIT; -- Exit loop if stock is insufficient
        ELSE
            DBMS_OUTPUT.PUT_LINE('- Part ID ' || v_part_id || ': Stock is sufficient.');
        END IF;
    END LOOP;

    -- Close cursor
    CLOSE v_required_parts_cursor;

    -- Return result
    RETURN v_is_stock_sufficient;

EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);

        -- Ensure cursor is closed on error
        IF v_required_parts_cursor%ISOPEN THEN
            CLOSE v_required_parts_cursor;
        END IF;

        RETURN 0; -- Return 0 in case of error
END;
/



CREATE OR REPLACE FUNCTION reserve_required_parts_for_order (
    p_OrderID "Order".OrderID%TYPE
) RETURN VARCHAR2 IS
    v_result VARCHAR2(255);
    v_required_parts_list SYS_REFCURSOR;
    v_part_id StockPart.StockPartID%TYPE;
    v_required_quantity NUMBER;
    v_exists NUMBER;
    v_reservation_success NUMBER := 1; -- Flag to indicate reservation success (1 for true, 0 for false)

BEGIN
    -- Begin transaction
    BEGIN
        -- Fetch the required parts for the order
        v_required_parts_list := get_required_parts_for_order(p_OrderID);

        -- Check stock availability
        IF check_materials_in_stock_for_order(p_OrderID) = 0 THEN
            v_result := 'Reservation failed: Insufficient stock for one or more required parts.';
            ROLLBACK; -- Rollback transaction if stock is insufficient
        ELSE
            -- Loop through required parts and insert reservations
            LOOP
                FETCH v_required_parts_list INTO v_part_id, v_required_quantity;
                EXIT WHEN v_required_parts_list%NOTFOUND;

                BEGIN
                    -- Check if reservation already exists
                    SELECT COUNT(*) INTO v_exists
                    FROM PartReservation
                    WHERE OrderID = p_OrderID
                      AND StockPartID = v_part_id
                      AND ReservedQuantity = v_required_quantity
                      AND ReservationDate = TRUNC(SYSDATE);

                    IF v_exists > 0 THEN
                        v_result := 'Reservation already exists for order ID: ' || p_OrderID;
                        v_reservation_success := 0;
                        EXIT; -- Exit loop if reservation exists
                    ELSE
                        -- Insert new reservation
                        INSERT INTO PartReservation (ReservationID, OrderID, StockPartID, ReservedQuantity, ReservationDate)
                        VALUES (SEQ_PARTRESERVATION.NEXTVAL, p_OrderID, v_part_id, v_required_quantity, TRUNC(SYSDATE));
                    END IF;
                END;
            END LOOP;

            -- Commit or rollback based on success
            IF v_reservation_success = 1 THEN
                v_result := 'Reservation completed successfully: All required parts have been reserved.';
                COMMIT;
            ELSE
                ROLLBACK;
            END IF;

            -- Close cursor
            CLOSE v_required_parts_list;
        END IF;
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
            IF v_required_parts_list%ISOPEN THEN
                CLOSE v_required_parts_list;
            END IF;
            ROLLBACK;
            v_result := 'Error: Unable to reserve the required parts due to an unexpected issue.';
            RETURN v_result;
    END;

    RETURN v_result;
END;
/

