CREATE OR REPLACE FUNCTION register_workstation(
    w_workstationId Workstation.WorkstationID%TYPE,
    w_workstationTypeId Workstation.WorkstationTypeID%TYPE,
    w_name Workstation.Name%TYPE,
    w_description Workstation.Description%TYPE
)

RETURN VARCHAR2
IS
v_result VARCHAR2(255);

BEGIN
    BEGIN

    -- Data Validation
    IF w_workstationId IS NULL OR w_workstationId = '' THEN
        RETURN 'ERROR: WorkstationID cannot be empty!';
    END IF;

    IF w_workstationTypeId IS NULL OR w_workstationTypeId = '' THEN
        RETURN 'ERROR: WorkstationTypeID cannot be empty!';
    END IF;

    IF w_name IS NULL OR w_name = '' THEN
        RETURN 'ERROR: Workstation Name cannot be empty!';
    END IF;

    IF w_description IS NULL OR w_description = '' THEN
        RETURN 'ERROR: Workstation Description cannot be empty!';
    END IF;

    -- Verify if the name already exists
        BEGIN
            SELECT 1
            INTO v_result
            FROM Workstation
            WHERE Name = w_name;

            -- If it does, cast error
            RETURN 'ERROR: Workstation Name already exists.';
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                ROLLBACK;
                -- If not, continue
                NULL;
        END;

    -- Insertion
    INSERT INTO Workstation (WorkstationID, WorkstationTypeID, Name, Description)
    VALUES (w_workstationId, w_workstationTypeId, w_name, w_description);

    COMMIT;

    -- Success
    v_result := 'SUCCESS: Workstation was registered successfully!';
    RETURN v_result;

    -- Exceptions
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX THEN
            ROLLBACK;
            v_result := 'ERROR: WorkstationID already exists.';
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
    v_workstationId Workstation.WorkstationID%TYPE := 8700;
    v_workstationTypeId Workstation.WorkstationTypeID%TYPE := 'A4578';
    v_name Workstation.Name%TYPE := 'Packaging 21';
    v_description Workstation.Description%TYPE := 'Packaging station';
BEGIN
    v_result := register_workstation(v_workstationId, v_workstationTypeId, v_name, v_description);
    DBMS_OUTPUT.PUT_LINE(v_result);
END;
/