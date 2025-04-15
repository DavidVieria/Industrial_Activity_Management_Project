DECLARE
v_cursor SYS_REFCURSOR;
    v_ProductID VARCHAR2(10) := 'AS12945S22';
    v_ProductID_out VARCHAR2(10);
    v_OperationID NUMBER;
    v_OperationNumber NUMBER;
    v_OperationTypeName VARCHAR2(255);
    v_WorkstationTypeName VARCHAR2(255);
    v_ExecutionTime NUMBER;
    v_InputPartID VARCHAR2(10);
    v_InputQuantity NUMBER;
    v_OutputPartID VARCHAR2(10);
    v_OutputQuantity NUMBER;
    v_Level NUMBER;
    v_row_count NUMBER := 0;
BEGIN
    v_cursor := productionPlanOperations(v_ProductID);

    DBMS_OUTPUT.PUT_LINE('Operations for product: ' || v_ProductID);
    DBMS_OUTPUT.PUT_LINE('----------------------------------------');

    LOOP
        FETCH v_cursor INTO
            v_ProductID_out,
            v_OperationID,
            v_OperationNumber,
            v_OperationTypeName,
            v_WorkstationTypeName,
            v_ExecutionTime,
            v_InputPartID,
            v_InputQuantity,
            v_OutputPartID,
            v_OutputQuantity,
            v_Level;

        EXIT WHEN v_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Level: ' || v_Level);
        DBMS_OUTPUT.PUT_LINE('ProductID: ' || v_ProductID_out);
        DBMS_OUTPUT.PUT_LINE('OperationID: ' || v_OperationID);
        DBMS_OUTPUT.PUT_LINE('OperationNumber: ' || v_OperationNumber);
        DBMS_OUTPUT.PUT_LINE('Operation Type: ' || v_OperationTypeName);
        DBMS_OUTPUT.PUT_LINE('Workstation Type: ' || v_WorkstationTypeName);
        DBMS_OUTPUT.PUT_LINE('Execution Time: ' || v_ExecutionTime || ' minutes');
        DBMS_OUTPUT.PUT_LINE('Input Part: ' || v_InputPartID || ' (Qty: ' || v_InputQuantity || ')');
        DBMS_OUTPUT.PUT_LINE('Output Part: ' || v_OutputPartID || ' (Qty: ' || v_OutputQuantity || ')');
        DBMS_OUTPUT.PUT_LINE('----------------------------------------');

        v_row_count := v_row_count + 1;
    END LOOP;

    IF v_row_count = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No operations found for product ' || v_ProductID);
    END IF;

    CLOSE v_cursor;
END;
/

