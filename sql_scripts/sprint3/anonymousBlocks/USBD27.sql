-- This script is an anonymous block that reserves the required parts for an order.

-- To run this user story, it is recommended to first run the additional inserts
-- indicated in the file `test_pl_sql_code.sql` for user stories 26, 27, and 28.

DECLARE
    v_result VARCHAR2(255);
    v_OrderID "Order".OrderID%TYPE := 8;  -- Change to the order ID
BEGIN
    -- Reserve the required parts for the order
    v_result := reserve_required_parts_for_order(v_OrderID);
    DBMS_OUTPUT.PUT_LINE(v_result);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('An error occurred: ' || SQLERRM);
END;
/

