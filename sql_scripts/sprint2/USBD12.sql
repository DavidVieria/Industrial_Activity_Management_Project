CREATE OR REPLACE FUNCTION get_all_materials(p_product_id Product.ProductID%TYPE)
    RETURN SYS_REFCURSOR AS
    v_materials_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_materials_cursor FOR
        WITH ProductMaterialsCTE (ProductID, MaterialID, MaterialDescription, Quantity, Unit, ParentMaterialID) AS (
            -- Initial query: direct materials of the product
            SELECT
                pbo.ProductID,
                i.MaterialID,
                m.Description AS MaterialDescription,
                i.Quantity,
                m.Unit,
                NULL AS ParentMaterialID
            FROM ProductBOOOperation pbo
            JOIN Input i
                    ON pbo.OperationID = i.OperationID
            JOIN Material m
                    ON i.MaterialID = m.MaterialID
            WHERE pbo.ProductID = p_product_id

            UNION ALL

            -- Recursive query: materials of subproducts
            SELECT
                pbo.ProductID,
                i.MaterialID,
                m.Description AS MaterialDescription,
                i.Quantity,
                m.Unit,
                cte.MaterialID AS ParentMaterialID
            FROM Input i
            JOIN ProductBOOOperation pbo
                    ON i.OperationID = pbo.OperationID
            JOIN Material m
                    ON i.MaterialID = m.MaterialID
            JOIN ProductMaterialsCTE cte
                    ON pbo.ProductID = cte.MaterialID
        )
        SELECT DISTINCT
            cte.MaterialID AS v_material_id,
            cte.MaterialDescription AS v_material_description,
            SUM(cte.Quantity) AS v_total_quantity,
            cte.Unit AS v_unit
        FROM ProductMaterialsCTE cte
        GROUP BY cte.MaterialID, cte.MaterialDescription, cte.Unit
        ORDER BY cte.MaterialID; -- Order by MaterialID
    RETURN v_materials_cursor;
END;
/

DECLARE
    -- Cursor to store the function results
    v_product_materials SYS_REFCURSOR;

    -- Variables to iterate over the results
    v_product_id Product.ProductID%TYPE := 'AS12945S20'; --Change to the desired Product ID
    v_material_id Material.MaterialID%TYPE;
    v_material_description Material.Description%TYPE;
    v_total_quantity Input.Quantity%TYPE;
    v_unit Material.Unit%TYPE;
BEGIN
    -- Call the function passing the product ID as a parameter
    v_product_materials := get_all_materials(v_product_id);

    DBMS_OUTPUT.PUT_LINE('Product: ' || v_product_id);
    DBMS_OUTPUT.PUT_LINE('--------------------------------------------------');
    DBMS_OUTPUT.PUT_LINE('------------------Materials List------------------');
    DBMS_OUTPUT.PUT_LINE('--------------------------------------------------');
    -- Loop to iterate over the results returned by the cursor
    LOOP
        FETCH v_product_materials INTO v_material_id, v_material_description, v_total_quantity, v_unit;
        EXIT WHEN v_product_materials%NOTFOUND;

        -- Display the results
        DBMS_OUTPUT.PUT_LINE('Material ID: ' || v_material_id);
        DBMS_OUTPUT.PUT_LINE('Description: ' || v_material_description);
        DBMS_OUTPUT.PUT_LINE('Total Quantity: ' || v_total_quantity);
        DBMS_OUTPUT.PUT_LINE('Unit: ' || v_unit);
        DBMS_OUTPUT.PUT_LINE('--------------------------------------------------');
    END LOOP;

    -- Close the cursor
    CLOSE v_product_materials;
END;
/
