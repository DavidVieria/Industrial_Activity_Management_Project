CREATE OR REPLACE TRIGGER prevent_circular_reference
    BEFORE INSERT OR UPDATE ON OperationInput
    FOR EACH ROW
DECLARE
    -- Custom exception declaration for circular reference
    EX_CIRCULAR_REFERENCE EXCEPTION;

    -- Variable to store PartID
    v_part_id OperationInput.PartID%TYPE;

BEGIN
    -- Store the current PartID value in a variable
    v_part_id := :NEW.PartID;

    -- Check if a circular reference exists
    BEGIN
        SELECT 1
        INTO :NEW.PartID
        FROM BOO b
        JOIN ProductBOOOperation pbo
                ON pbo.ProductID = b.ProductID
        JOIN Operation o
                ON pbo.OperationID = o.OperationID
        WHERE :NEW.OperationID = o.OperationID
                AND :NEW.PartID = b.ProductID;

        -- If the query returns a value, a circular reference is found
        RAISE EX_CIRCULAR_REFERENCE;

    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            -- If no match is found, allow the operation to proceed without issues
            NULL;
    END;

-- Handle exceptions outside the inner block
EXCEPTION
    WHEN EX_CIRCULAR_REFERENCE THEN
        RAISE_APPLICATION_ERROR(-20002, 'It is not allowed to use a product as input in its own BOO (Bill of Operations). The product causing the error is: ' || v_part_id);
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20003, 'An unexpected error occurred: ' || SQLERRM);
END;
/