-- This script is an anonymous block that checks if the stock is sufficient for an order.

-- To run this user story, it is recommended to first run the additional inserts
-- indicated in the file `test_pl_sql_code.sql` for user stories 26, 27, and 28.


DECLARE
    v_OrderID "Order".OrderID%TYPE := 9;
    v_is_stock_sufficient NUMBER;
BEGIN
    -- Check if the stock is sufficient for the order
    v_is_stock_sufficient := check_materials_in_stock_for_order(v_OrderID);
    DBMS_OUTPUT.PUT_LINE('The order ' || CASE WHEN v_is_stock_sufficient = 1 THEN 'can be fulfilled with the available stock.' ELSE 'CANNOT be fulfilled with the available stock.' END);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('An error occurred: ' || SQLERRM);
END;
/

