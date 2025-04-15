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




-- Complexity Analysis:
-- O(k * n)
-- Where:
-- n - number of operations
-- k - number of subproducts (i.e., BOO depth)
-- This complexity arises because, in addition to including all operations of a product,
-- if these are associated with subproducts, their respective operations must also be included.
-- Therefore, recursion is necessary, leading to the indicated complexity.
-- There are also other factors that, although minor, impact the complexity of this function,
-- such as sorting (ORDER BY Level_Depth, OperationNumber), which we considered necessary to include,
-- as it ensures the BOO is ordered in the desired way.
