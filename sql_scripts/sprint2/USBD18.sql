CREATE OR REPLACE FUNCTION list_customers_active
RETURN SYS_REFCURSOR
IS
    v_Cursor SYS_REFCURSOR;
BEGIN
    -- Open a cursor to retrieve active customers with only ID and Name
OPEN v_Cursor FOR
SELECT ClientID, Name
FROM Client
WHERE ClientState = 'Active'
ORDER BY ClientID;

RETURN v_Cursor;
EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20001, 'Error: Unexpected error occurred while listing active customers.');
END list_customers_active;
/


CREATE OR REPLACE FUNCTION deactivate_customer(p_ClientID IN Client.ClientID%TYPE)
RETURN VARCHAR2
IS
    v_UndeliveredOrders NUMBER;
    v_ClientExists      NUMBER;
    v_ClientState       Client.ClientState%TYPE;
BEGIN
    -- Check if the client exists and retrieve their state
SELECT COUNT(*), ClientState
INTO v_ClientExists, v_ClientState
FROM Client
WHERE ClientID = p_ClientID
GROUP BY ClientState;

-- If the client is not found
IF v_ClientExists = 0 THEN
        RETURN 'Error: Client not found.';
END IF;

    -- Check if the client is already deactivated
    IF v_ClientState = 'Inactive' THEN
        RETURN 'Error: Client is already deactivated.';
END IF;

    -- Check if the client has undelivered orders
SELECT COUNT(*)
INTO v_UndeliveredOrders
FROM "Order"
WHERE ClientID = p_ClientID
  AND DeliveryDate > SYSDATE; -- Compare with current date

IF v_UndeliveredOrders > 0 THEN
        RETURN 'Error: Client has undelivered orders.';
END IF;

    -- Update client status to deactivated
UPDATE Client
SET ClientState = 'Inactive'
WHERE ClientID = p_ClientID;

RETURN 'Success: Client deactivated.';
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 'Error: Client not found.';
WHEN OTHERS THEN
        RETURN 'Error: Unexpected error occurred.';
END deactivate_customer;
/


-- Test
DECLARE
v_Cursor SYS_REFCURSOR;
    v_ClientID Client.ClientID%TYPE;
    v_Name Client.Name%TYPE;
BEGIN
    -- Call the function to get the cursor
    v_Cursor := list_customers_active;

    -- Loop to process the cursor
    LOOP
FETCH v_Cursor INTO v_ClientID, v_Name;
        EXIT WHEN v_Cursor%NOTFOUND;

        -- Display active clients (only ID and Name)
        DBMS_OUTPUT.PUT_LINE('ID: ' || v_ClientID || ' | Name: ' || v_Name);
END LOOP;

    -- Close the cursor
CLOSE v_Cursor;
END;
/


-- Test
BEGIN
    DBMS_OUTPUT.PUT_LINE(deactivate_customer(456)); -- Existing ID
    DBMS_OUTPUT.PUT_LINE(deactivate_customer(999)); -- Non-existent ID
    DBMS_OUTPUT.PUT_LINE(deactivate_customer(456)); -- Already deactivated ID
END;
/