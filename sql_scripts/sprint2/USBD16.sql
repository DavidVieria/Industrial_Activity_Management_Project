CREATE OR REPLACE FUNCTION register_product(
    p_ProductID Product.ProductID%TYPE,
    p_ProductFamilyID Product.ProductFamilyID%TYPE,
    p_Name Product.Name%TYPE,
    p_Description Material.Description%TYPE
    )
RETURN VARCHAR2 IS
    v_result VARCHAR2(255);
    v_count NUMBER;

BEGIN
    -- Start of the main transaction block
    BEGIN
        -- Input validation: Check if ProductID is null, empty, or only spaces
        IF p_ProductID IS NULL OR REGEXP_LIKE(p_ProductID, '^\s*$') THEN
            RETURN 'ERROR: Product ID cannot be empty.';
        END IF;

        -- Input validation: Check if Name is null, empty, or only spaces
        IF p_Name IS NULL OR REGEXP_LIKE(p_Name, '^\s*$') THEN
            RETURN 'ERROR: Product Name cannot be empty.';
        END IF;

        -- Input validation: Check if Description is null, empty, or only spaces
        IF p_Description IS NULL OR REGEXP_LIKE(p_Description, '^\s*$') THEN
            RETURN 'ERROR: Description cannot be empty.';
        END IF;

        -- Input validation: Check if ProductFamilyID is null or invalid
        IF p_ProductFamilyID IS NULL OR p_ProductFamilyID <= 0 THEN
            RETURN 'ERROR: Product Family cannot be empty or invalid.';
        END IF;

        -- Check if ProductID already exists in the database
        SELECT COUNT(*)
        INTO v_count
        FROM Product
        WHERE ProductID = p_ProductID;

        IF v_count > 0 THEN
            RETURN 'ERROR: Product ID already exists.';
        END IF;

        -- Validate ProductID format (must start with 'AS')
        IF SUBSTR(p_ProductID, 1, 2) != 'AS' THEN
            RETURN 'ERROR: ProductID must start with ''AS'' as required by the system. Please correct the format.';
        END IF;

        -- Check if the specified Product Family exists
        SELECT COUNT(*)
        INTO v_count
        FROM ProductFamily
        WHERE ProductFamilyID = p_ProductFamilyID;

        IF v_count = 0 THEN
            RETURN 'ERROR: The specified Product Family does not exist in the system. Please check the entered ID.';
        END IF;

        -- Check if the Description is unique
        SELECT COUNT(*)
        INTO v_count
        FROM Material
        WHERE Description = p_Description;

        IF v_count > 0 THEN
            RETURN 'ERROR: The description for each product must be unique, and there is already a product with this description in the system.';
        END IF;

        -- Attempt to insert into Material and Product tables
        INSERT INTO Material (MaterialID, Description, Unit)
        VALUES (p_ProductID, p_Description, 'unit');

        INSERT INTO Product (ProductID, ProductFamilyID, Name)
        VALUES (p_ProductID, p_ProductFamilyID, p_Name);

        -- Commit transaction on success
        COMMIT;

        -- Return success message
        v_result := 'SUCCESS: Product registered successfully.';
        RETURN v_result;

        -- Exception handling block
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            ROLLBACK;
            -- Handle specific UNIQUE constraint violations
            IF SQLERRM LIKE '%MATERIAL_UNIQUE_DESCRIPTION%' THEN
                v_result := 'ERROR: The description for each product must be unique, and there is already a product with this description in the system.';
            ELSE
                v_result := 'ERROR: Product ID already exists.';
            END IF;
            RETURN v_result;

        WHEN OTHERS THEN
            ROLLBACK;
            -- Handle all other exceptions
            v_result := 'ERROR: ' || SQLERRM;
            RETURN v_result;
    END;
END;
/

-- Anonymous block for using the function
DECLARE
v_result VARCHAR2(255);
    v_ProductID Product.ProductID%TYPE := 'AS32045S75'; --Change to the desired ID
    v_ProductFamilyID Product.ProductFamilyID%TYPE := 125; --Change to the desired Family ID
    v_Name Product.Name%TYPE := 'Pro 44 10l pot'; --Change to the desired Name
    v_Description Material.Description%TYPE := '10l 44 cm stainless steel pot'; --Change to the desired Description
BEGIN
    v_result := register_product(v_ProductID, v_ProductFamilyID, v_Name, v_Description);
    DBMS_OUTPUT.PUT_LINE(v_result);
END;
/

----------------------------------------------------------------------------------------------------------------------------

-- Tests for register_product function
BEGIN
    -- Test 1: Insert a valid product
    DBMS_OUTPUT.PUT_LINE('Test 1 - ' || register_product('AS32045S79', 125, 'Pro 36 8l pot', '8l 36 cm stainless steel pot')); -- Expected: SUCCESS

    -- Test 2: Attempt to insert a product with an already existing ProductID
    DBMS_OUTPUT.PUT_LINE('Test 2 - ' || register_product('AS12945G48', 125, 'Pro 36 8l pot', '8l 36 cm stainless steel pot')); -- Expected: ERROR: Product ID already exists.

    -- Test 3: Attempt to insert a product with a non-unique description
    DBMS_OUTPUT.PUT_LINE('Test 3 - ' || register_product('AS32047S11', 125, 'Pro 20 3l pot', '3l 20 cm stainless steel pot')); -- Expected: ERROR: The description for each product must be unique.

    -- Test 4: Attempt to insert a product with an invalid ProductID format
    DBMS_OUTPUT.PUT_LINE('Test 4 - ' || register_product('P12345', 125, 'Pro 34 3l pot', '3l 34 cm stainless steel pot')); -- Expected: ERROR: ProductID must start with 'AS'.

    -- Test 5: Attempt to insert a product with a non-existent Product Family ID
    DBMS_OUTPUT.PUT_LINE('Test 5 - ' || register_product('AS32047S71', 999, 'Pro 36 8l pot', '8l 36 cm stainless steel pot')); -- Expected: ERROR: The specified Product Family does not exist.

    -- Test 6: Attempt to insert a product with an empty Description
    DBMS_OUTPUT.PUT_LINE('Test 6 - ' || register_product('AS12947S91', 145, 'Pro 27 lid', '')); -- Expected: ERROR: Description cannot be empty.

    -- Test 7: Attempt to insert a product with a negative ProductFamilyID
    DBMS_OUTPUT.PUT_LINE('Test 7 - ' || register_product('AS12947S91', -1, 'Pro 27 lid', '27 cm stainless steel lid')); -- Expected: ERROR: Product Family cannot be empty or invalid.

END;
/
