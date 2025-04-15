---------------------------------------------------------------------------------------------------------------------------------
------------------------------------------------- Test Data for PL/SQL Features -------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------

-- USBD23: Inserts to test the trigger for maximum execution time --------------------------------

-- Preconditions:
-- Operation Type 5649 on Workstation Type A4588 has a maximum execution time of 230.
-- Operation Type 5649 on Workstation Type A4598 has a maximum execution time of 235.

-- Invalid operation: exceeds the maximum execution time of all workstation types that can execute it
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime)
VALUES (201, 5649, 1000);

-- Invalid operation: exceeds the maximum execution time of one of the workstation types that can execute it
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime)
VALUES (202, 5649, 232);

-- Valid operation: within the maximum execution time limits
INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime)
VALUES (203, 5649, 100);

-- Invalid Update:
UPDATE Operation
SET ExpectedExecutionTime = 9999
WHERE OperationID = 100;



-- USBD24: Inserts to test the trigger prevent_circular_reference --------------------------------

-- Preconditions:
-- Operation ID 170 is used in the Bill of Operations for Product 'AS12945S20'.
-- Operation ID 120 is used in the Bill of Operations for Product 'AS12947S22'.
-- Operation ID 150 is used in the Bill of Operations for Part 'PN52384R50'.
-- Operation ID 100 is used in the Bill of Operations for Part 'PN52384R50'.

-- Test Case 1: Circular reference with Product 'AS12945S20'
-- Invalid insertion: circular reference detected
INSERT INTO OperationInput (OperationID, PartID, Quantity)
VALUES (170, 'AS12945S20', 5);

-- Valid insertion: no circular reference
INSERT INTO OperationInput (OperationID, PartID, Quantity)
VALUES (170, 'AS12946S22', 3);

-- Test Case 2: Circular reference with Product 'AS12947S22'
-- Invalid insertion: circular reference detected
INSERT INTO OperationInput (OperationID, PartID, Quantity)
VALUES (120, 'AS12947S22', 2);

-- Valid insertion: no circular reference
INSERT INTO OperationInput (OperationID, PartID, Quantity)
VALUES (120, 'AS12945S20', 1);

-- Test Case 3: Update PartID to introduce circular reference
-- Invalid update: circular reference detected
BEGIN
    UPDATE OperationInput
    SET PartID = 'AS12946S20'
    WHERE OperationID = 150 AND PartID = 'PN52384R50';
END;
/

-- Valid update: no circular reference
BEGIN
    UPDATE OperationInput
    SET PartID = 'AS12947S20'
    WHERE OperationID = 100 AND PartID = 'PN52384R50';
END;
/

-- Cleanup: Reverse Test Data (valid insertions only)

-- Data Test Case 1:
DELETE FROM OperationInput
WHERE OperationID = 170 AND PartID = 'AS12946S22';

-- Data Test Case 2:
DELETE FROM OperationInput
WHERE OperationID = 120 AND PartID = 'AS12945S20';

-- Data Test Case 3:
BEGIN
    UPDATE OperationInput
    SET PartID = 'PN52384R50'
    WHERE OperationID = 100 AND PartID = 'AS12947S20';
END;
/


-- Inserts added to test the functionalities of user stories 26, 27 and 28 --------------------------------

INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (8, 348, 4, TO_DATE('03/01/2025', 'DD/MM/YYYY'), TO_DATE('23/02/2025', 'DD/MM/YYYY'));
INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (9, 785, 2, TO_DATE('04/01/2025', 'DD/MM/YYYY'), TO_DATE('17/02/2025', 'DD/MM/YYYY'));
INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (10, 456, 1, TO_DATE('05/01/2025', 'DD/MM/YYYY'), TO_DATE('13/05/2025', 'DD/MM/YYYY'));
INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (11, 348, 4, TO_DATE('09/01/2025', 'DD/MM/YYYY'), TO_DATE('23/02/2025', 'DD/MM/YYYY'));
INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (12, 785, 2, TO_DATE('12/01/2025', 'DD/MM/YYYY'), TO_DATE('17/02/2025', 'DD/MM/YYYY'));
INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (13, 456, 1, TO_DATE('15/01/2025', 'DD/MM/YYYY'), TO_DATE('13/05/2025', 'DD/MM/YYYY'));
INSERT INTO "Order" (OrderID, ClientID, DeliveryAddressID, OrderDate, DeliveryDate) VALUES (14, 657, 3, TO_DATE('16/01/2025', 'DD/MM/YYYY'), TO_DATE('23/02/2025', 'DD/MM/YYYY'));

INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (8, 'AS12946S22', 25);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (8, 'AS12947S22', 10);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (9, 'AS12946S20', 7);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (9, 'AS12946S22', 5);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (10, 'AS12946S20', 108);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (10, 'AS12947S20', 87);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (11, 'AS12947S22', 14);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (11, 'AS12946S22', 4);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (12, 'AS12947S22', 6);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (13, 'AS12947S20', 3);
INSERT INTO ProductOrder (OrderID, ProductID, ProductQuantity) VALUES (14, 'AS12946S20', 8);


-- Test the function get_required_parts_for_order -----------------------------------------------------

-- Test case 1: order with required parts
-- The function should return the required parts and their total quantity
DECLARE
    v_result SYS_REFCURSOR;
    v_part_id StockPart.StockPartID%TYPE;
    v_total_quantity NUMBER;
BEGIN
    v_result := get_required_parts_for_order(8);

    -- Se o resultado for NULL, trata o caso
    IF v_result IS NULL THEN
        DBMS_OUTPUT.PUT_LINE('No required parts found or an error occurred.');
    ELSE
        LOOP
            FETCH v_result INTO v_part_id, v_total_quantity;
            EXIT WHEN v_result%NOTFOUND;
            DBMS_OUTPUT.PUT_LINE('Part ID: ' || v_part_id || ', Total Quantity: ' || v_total_quantity);
        END LOOP;
        CLOSE v_result;
    END IF;
END;
/

-- Test case 2: order with no required stockable parts
-- The function should return an error message
DECLARE
    v_result SYS_REFCURSOR;
    v_part_id StockPart.StockPartID%TYPE;
    v_total_quantity NUMBER;
BEGIN
    v_result := get_required_parts_for_order(4);

    -- Se o resultado for NULL, trata o caso
    IF v_result IS NULL THEN
        DBMS_OUTPUT.PUT_LINE('No required parts found or an error occurred.');
    ELSE
        LOOP
            FETCH v_result INTO v_part_id, v_total_quantity;
            EXIT WHEN v_result%NOTFOUND;
            DBMS_OUTPUT.PUT_LINE('Part ID: ' || v_part_id || ', Total Quantity: ' || v_total_quantity);
        END LOOP;
        CLOSE v_result;
    END IF;
END;
/

-- USBD26: Test the function check_materials_in_stock_for_order ---------------------------------------

-- Test case 1: stock is insufficient
-- The function should return FALSE
DECLARE
    v_is_stock_sufficient NUMBER;
BEGIN
    v_is_stock_sufficient := check_materials_in_stock_for_order(10);
    DBMS_OUTPUT.PUT_LINE('The order ' || CASE WHEN v_is_stock_sufficient = 1 THEN 'can be fulfilled with the available stock.' ELSE 'CANNOT be fulfilled with the available stock.' END);
END;
/

-- Test case 2: stock is sufficient
-- The function should return TRUE
Declare
    v_is_stock_sufficient NUMBER;
BEGIN
    v_is_stock_sufficient := check_materials_in_stock_for_order(8);
    DBMS_OUTPUT.PUT_LINE('The order ' || CASE WHEN v_is_stock_sufficient = 1 THEN 'can be fulfilled with the available stock.' ELSE 'CANNOT be fulfilled with the available stock.' END);
END;
/

-- USBD27: Test the function reserve_required_parts_for_order ---------------------------------------

-- Test case 1: stock is insufficient
-- The function should return a error message
Declare
    v_is_stock_sufficient BOOLEAN;
BEGIN
    DBMS_OUTPUT.PUT_LINE(reserve_required_parts_for_order(10));
END;
/

-- Test case 2: stock is sufficient
-- The function should return a success message
Declare
    v_is_stock_sufficient BOOLEAN;
BEGIN
    DBMS_OUTPUT.PUT_LINE(reserve_required_parts_for_order(8));
END;
/

-- Test case 3: there are no required stockable parts for the order
-- The function should return a error message
Declare
    v_is_stock_sufficient BOOLEAN;
BEGIN
    DBMS_OUTPUT.PUT_LINE(reserve_required_parts_for_order(6));
END;
/

-- Test case 4: the parts are already reserved (run test 2 first)
-- The function should return that the parts are already reserved
Declare
    v_is_stock_sufficient BOOLEAN;
BEGIN
    DBMS_OUTPUT.PUT_LINE(reserve_required_parts_for_order(8));
END;
/

-- USBD28: use the function reserve_required_parts_for_order to have reservations parts Data ----------------------------


BEGIN
    DBMS_OUTPUT.PUT_LINE(reserve_required_parts_for_order(9));
    DBMS_OUTPUT.PUT_LINE(reserve_required_parts_for_order(11));
    DBMS_OUTPUT.PUT_LINE(reserve_required_parts_for_order(12));
    DBMS_OUTPUT.PUT_LINE(reserve_required_parts_for_order(13));
    DBMS_OUTPUT.PUT_LINE(reserve_required_parts_for_order(14));
END;
/

-- You can also run test case 2 of USBD27 to have more reservations parts Data

