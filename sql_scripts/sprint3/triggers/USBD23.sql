CREATE OR REPLACE TRIGGER check_operation_execution_time
    BEFORE INSERT OR UPDATE ON Operation
    FOR EACH ROW

    DECLARE
        max_execution_time NUMBER;
        EXCEPTION_TIME_EXCEEDED EXCEPTION;
    BEGIN

    SELECT MIN(MaximumExecutionTime)
    INTO max_execution_time
    FROM CanBeDoneAt
    WHERE OperationTypeID = :NEW.OperationTypeID;

    IF :NEW.ExpectedExecutionTime > max_execution_time THEN
        RAISE EXCEPTION_TIME_EXCEEDED;
    END IF;

EXCEPTION
    WHEN EXCEPTION_TIME_EXCEEDED THEN
        RAISE_APPLICATION_ERROR(-20001,
            'Expected execution time (' || :NEW.ExpectedExecutionTime ||
            ') exceeds the maximum allowed time (' || max_execution_time || ') ');
END;
/
