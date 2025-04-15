CREATE OR REPLACE FUNCTION register_order(
    o_orderId "Order".OrderID%TYPE,
    o_clientId "Order".ClientID%TYPE,
    o_deliveryDate "Order".DeliveryDate%TYPE,
    o_orderDate "Order".OrderDate%TYPE,
    o_clientState Client.ClientState%TYPE
)

    RETURN VARCHAR2
    IS
    v_result VARCHAR2(255);

BEGIN
    BEGIN

        -- Data Validation
        IF o_orderId IS NULL OR o_orderId = '' THEN
            RETURN 'ERROR: OrderID cannot be empty!';
        END IF;
        IF o_clientId IS NULL OR o_clientId = '' THEN
            RETURN 'ERROR: ClientID cannot be empty!';
        END IF;

        IF o_deliveryDate IS NULL OR o_deliveryDate = '' THEN
            RETURN 'ERROR: Delivery Date cannot be empty!';
        END IF;

        IF SYSTIMESTAMP > o_deliveryDate THEN
            RETURN 'ERROR: Product is not in the current line-up!';
        END IF;

        IF o_orderDate IS NULL OR o_orderDate = '' THEN
            RETURN 'ERROR: Order Date cannot be empty!';
        END IF;

        IF o_clientState = 'Inactive' THEN
            RETURN 'ERROR: Client is not Active!';
        END IF;

    -- Insertion
        INSERT INTO "Order" (OrderID, ClientID, DeliveryDate, OrderDate)
        VALUES (o_orderId, o_clientId, o_deliveryDate, o_orderDate);

        COMMIT;

    -- Success
        v_result := 'SUCCESS: OrderId: ' || o_orderId || ', was registered successfully.';
        RETURN v_result;


    -- Check if OrderID already exists or any other error
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            ROLLBACK;
            v_result := 'ERROR: OrderID already exists.';
        RETURN v_result;
        WHEN OTHERS THEN
            ROLLBACK;
            v_result := 'ERROR: ' || SQLERRM;
        RETURN v_result;
    END;
END;
/

DECLARE
    v_result VARCHAR2(255);
    v_orderId "Order".OrderID%TYPE := 15;
    v_clientId "Order".ClientID%TYPE := 348;
    v_deliveryDate "Order".DeliveryDate%TYPE := TO_DATE('25/11/2024', 'DD/MM/YYYY');
    v_orderDate "Order".OrderDate%TYPE := TO_DATE('25/09/2024', 'DD/MM/YYYY');
    v_clientState Client.ClientState%TYPE := 'Active';
BEGIN
    v_result := register_order(v_orderId, v_clientId, v_deliveryDate, v_orderDate, v_clientState);
    DBMS_OUTPUT.PUT_LINE(v_result);
END;
/
