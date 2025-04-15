CREATE OR REPLACE PROCEDURE list_reserved_parts IS
BEGIN
    DBMS_OUTPUT.PUT_LINE('--------------------------------------');
    DBMS_OUTPUT.PUT_LINE('------List of All Reserved Parts------');
    DBMS_OUTPUT.PUT_LINE('--------------------------------------');

    FOR rec IN (
        SELECT
            pr.ReservationID,
            pr.OrderID,
            pr.StockPartID,
            pr.ReservedQuantity,
            TO_CHAR(pr.ReservationDate, 'DD/MM/YYYY') AS ReservationDate, -- Format the date
            s.SupplierID,
            s.Name
        FROM
            PartReservation pr
                JOIN StockPart sp ON pr.StockPartID = sp.StockPartID
                LEFT JOIN SupplyOffer so ON sp.StockPartID = so.StockPartID
                LEFT JOIN Supplier s ON so.SupplierID = s.SupplierID
        ORDER BY pr.RESERVATIONDATE, pr.RESERVATIONID
        ) LOOP
            DBMS_OUTPUT.PUT_LINE('Reservation ID : ' || rec.ReservationID);
            DBMS_OUTPUT.PUT_LINE('  - Order ID          : ' || rec.OrderID);
            DBMS_OUTPUT.PUT_LINE('  - Stock Part ID     : ' || rec.StockPartID);
            DBMS_OUTPUT.PUT_LINE('  - Reserved Quantity : ' || rec.ReservedQuantity);
            DBMS_OUTPUT.PUT_LINE('  - Reservation Date  : ' || rec.ReservationDate);
            IF rec.SupplierID IS NULL THEN
                DBMS_OUTPUT.PUT_LINE('  - Supplier          : Not Available');
            ELSE
                DBMS_OUTPUT.PUT_LINE('  - Supplier ID       : ' || rec.SupplierID);
                DBMS_OUTPUT.PUT_LINE('  - Supplier Name     : ' || rec.Name);
            END IF;
            DBMS_OUTPUT.PUT_LINE('--------------------------------------');
        END LOOP;
END;
/

