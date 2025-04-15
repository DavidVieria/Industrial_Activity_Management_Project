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

