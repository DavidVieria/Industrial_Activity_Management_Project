-- With the data provided, there is no product that can use all types of workstations
-- So the answer isn't always 'There is no product that can use all types of workstations',
-- here are the inserts needed to have at least one:
INSERT INTO Material (MaterialID, Description) VALUES ('AS12345D12', 'bench');
INSERT INTO Product (ProductID, ProductFamilyID, Name) VALUES ('AS12345D12', 130, 'bench');
INSERT INTO BOO (ProductID) VALUES ('AS12345D12');
INSERT INTO Operation (OperationID, OperationTypeID) VALUES (200, 5665);
INSERT INTO Operation (OperationID, OperationTypeID) VALUES (201, 5671);
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12345D12', 100, 1, '103');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12345D12', 103, 2, '112');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12345D12', 112, 3, '114');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12345D12', 114, 4, '115');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12345D12', 115, 5, '200');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12345D12', 200, 6, '201');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12345D12', 201, 7, '130');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12345D12', 130, 8, '163');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12345D12', 163, 9, '164');
INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('AS12345D12', 164, 10, 'None');
INSERT INTO OperationTypeWorkstationType (OperationTypeID, WorkstationTypeID) VALUES (5647, 'K3676');
INSERT INTO OperationTypeWorkstationType (OperationTypeID, WorkstationTypeID) VALUES (5649, 'G9273');
INSERT INTO OperationTypeWorkstationType (OperationTypeID, WorkstationTypeID) VALUES (5651, 'G9274');


CREATE OR REPLACE FUNCTION find_product_All_Machine_Types
    RETURN SYS_REFCURSOR
IS
    result_cursor SYS_REFCURSOR;
BEGIN
    OPEN result_cursor FOR
        SELECT p.ProductID, p.Name
        FROM Product p
        JOIN ProductBOOOperation pbo ON p.ProductID = pbo.ProductID
        JOIN Operation o ON pbo.OperationID = o.OperationID
        JOIN OperationTypeWorkstationType otwt ON o.OperationTypeID = otwt.OperationTypeID
        JOIN WorkstationType wt ON otwt.WorkstationTypeID = wt.WorkstationTypeID
        GROUP BY p.ProductID, p.Name
        HAVING COUNT (DISTINCT wt.WorkstationTypeID) = (SELECT COUNT(*) FROM WorkstationType);

    RETURN result_cursor;
END;
/

DECLARE
    product_cursor SYS_REFCURSOR;
    product_id Product.ProductID%TYPE;
    product_name Product.Name%TYPE;
    v_count INTEGER;
BEGIN
    product_cursor := find_product_All_Machine_Types;

    FETCH product_cursor INTO product_id, product_name;
        IF product_cursor%NOTFOUND THEN
            DBMS_OUTPUT.PUT_LINE('There is no product that can use all types of workstations');
        ELSE
            LOOP
                DBMS_OUTPUT.PUT_LINE('ProductID: ' || product_id || ', Name: ' || product_name);
            FETCH product_cursor INTO product_id, product_name;
            EXIT WHEN product_cursor%NOTFOUND;
            END LOOP;
        END IF;

    CLOSE product_cursor;
END;
/