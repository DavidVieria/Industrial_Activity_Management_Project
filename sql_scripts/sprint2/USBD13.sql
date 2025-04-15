CREATE OR REPLACE FUNCTION list_operations_product(p_ProductID IN VARCHAR2)
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
            InputPart,
            InputQuantity,
            OutputPart,
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
                oi.PartID,
                oi.Quantity,
                op.ManufacturedPartID,
                op.Quantity,
                1 as Level_Depth
            FROM ProductBOOOperation pbo
            JOIN Operation op ON pbo.OperationID = op.OperationID
            JOIN OperationType ot ON op.OperationTypeID = ot.OperationTypeID
            JOIN CanBeDoneAt cbda ON ot.OperationTypeID = cbda.OperationTypeID
            JOIN WorkstationType wst ON cbda.WorkstationTypeID = wst.WorkstationTypeID
            JOIN OperationInput oi ON pbo.OperationID = oi.OperationID
            JOIN OperationOutput op ON pbo.OperationID = op.OperationID
            WHERE pbo.ProductID = p_ProductID

            UNION ALL

            -- Recursive case: operations of subproducts
            SELECT
                pbo.ProductID,
                pbo.OperationID,
                pbo.OperationNumber,
                ot.OperationTypeName,
                wst.WorkstationTypeName,
                oi.PartID,
                oi.Quantity,
                op.ManufacturedPartID,
                op.Quantity,
                cte.Level_Depth + 1
            FROM ProductBOOOperation pbo
            JOIN Operation op ON pbo.OperationID = op.OperationID
            JOIN OperationType ot ON op.OperationTypeID = ot.OperationTypeID
            JOIN CanBeDoneAt cbda ON ot.OperationTypeID = cbda.OperationTypeID
            JOIN WorkstationType wst ON cbda.WorkstationTypeID = wst.WorkstationTypeID
            JOIN OperationInput oi ON pbo.OperationID = oi.OperationID
            JOIN OperationOutput op ON pbo.OperationID = op.OperationID
            JOIN ProductOperationsCTE cte ON pbo.ProductID = cte.InputPart
            WHERE cte.Level_Depth < 10
        )
        SELECT DISTINCT
            ProductID,
            OperationID,
            OperationNumber,
            OperationTypeName,
            WorkstationTypeName,
            InputPart,
            InputQuantity,
            OutputPart,
            OutputQuantity,
            Level_Depth
        FROM ProductOperationsCTE
        ORDER BY Level_Depth, OperationNumber;

    RETURN v_cursor;
END;
/

DECLARE
v_cursor SYS_REFCURSOR;
    v_ProductID VARCHAR2(10) := 'AS12945S22';
    v_ProductID_out VARCHAR2(10);
    v_OperationID NUMBER;
    v_OperationNumber NUMBER;
    v_OperationTypeName VARCHAR2(255);
    v_WorkstationTypeName VARCHAR2(255);
    v_InputPart VARCHAR2(10);
    v_InputQuantity NUMBER;
    v_OutputPart VARCHAR2(10);
    v_OutputQuantity NUMBER;
    v_Level NUMBER;
    v_row_count NUMBER := 0;
BEGIN
    v_cursor := list_operations_product(v_ProductID);

    DBMS_OUTPUT.PUT_LINE('Operations for product: ' || v_ProductID);
    DBMS_OUTPUT.PUT_LINE('----------------------------------------');

    LOOP
        FETCH v_cursor INTO
            v_ProductID_out,
            v_OperationID,
            v_OperationNumber,
            v_OperationTypeName,
            v_WorkstationTypeName,
            v_InputPart,
            v_InputQuantity,
            v_OutputPart,
            v_OutputQuantity,
            v_Level;

        EXIT WHEN v_cursor%NOTFOUND;

        DBMS_OUTPUT.PUT_LINE('Level: ' || v_Level);
        DBMS_OUTPUT.PUT_LINE('ProductID: ' || v_ProductID_out);
        DBMS_OUTPUT.PUT_LINE('OperationID: ' || v_OperationID);
        DBMS_OUTPUT.PUT_LINE('OperationNumber: ' || v_OperationNumber);
        DBMS_OUTPUT.PUT_LINE('Operation Type: ' || v_OperationTypeName);
        DBMS_OUTPUT.PUT_LINE('Workstation Type: ' || v_WorkstationTypeName);
        DBMS_OUTPUT.PUT_LINE('Input Part: ' || v_InputPart || ' (Qty: ' || v_InputQuantity || ')');
        DBMS_OUTPUT.PUT_LINE('Output Part: ' || v_OutputPart || ' (Qty: ' || v_OutputQuantity || ')');
        DBMS_OUTPUT.PUT_LINE('----------------------------------------');

        v_row_count := v_row_count + 1;
    END LOOP;

    IF v_row_count = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No operations found for product ' || v_ProductID);
    END IF;

    CLOSE v_cursor;
END;
/