CREATE OR REPLACE FUNCTION reserve_required_parts_for_order (
    p_OrderID "Order".OrderID%TYPE
) RETURN VARCHAR2 IS
    v_result VARCHAR2(255);
    v_required_parts_list SYS_REFCURSOR;
    v_part_id StockPart.StockPartID%TYPE;
    v_required_quantity NUMBER;
    v_exists NUMBER;
    v_reservation_success BOOLEAN := TRUE; -- Flag to indicate reservation success

BEGIN
    -- Begin transaction
    BEGIN
        -- Fetch the required parts for the order
        v_required_parts_list := get_required_parts_for_order(p_OrderID);

        -- Check stock availability
        IF check_materials_in_stock_for_order(p_OrderID) = 0 THEN
            v_result := 'Reservation failed: Insufficient stock for one or more required parts.';
            ROLLBACK; -- Rollback transaction if stock is insufficient
        ELSE
            -- Loop through required parts and insert reservations
            LOOP
                FETCH v_required_parts_list INTO v_part_id, v_required_quantity;
                EXIT WHEN v_required_parts_list%NOTFOUND;

                BEGIN
                    -- Check if reservation already exists
                    SELECT COUNT(*) INTO v_exists
                    FROM PartReservation
                    WHERE OrderID = p_OrderID
                      AND StockPartID = v_part_id
                      AND ReservedQuantity = v_required_quantity
                      AND ReservationDate = TRUNC(SYSDATE);

                    IF v_exists > 0 THEN
                        v_result := 'Reservation already exists for order ID: ' || p_OrderID;
                        v_reservation_success := FALSE;
                        EXIT; -- Exit loop if reservation exists
                    ELSE
                        -- Insert new reservation
                        INSERT INTO PartReservation (ReservationID, OrderID, StockPartID, ReservedQuantity, ReservationDate)
                        VALUES (SEQ_PARTRESERVATION.NEXTVAL, p_OrderID, v_part_id, v_required_quantity, TRUNC(SYSDATE));
                    END IF;
                END;
            END LOOP;

            -- Commit or rollback based on success
            IF v_reservation_success THEN
                v_result := 'Reservation completed successfully: All required parts have been reserved.';
                COMMIT;
            ELSE
                ROLLBACK;
            END IF;

            -- Close cursor
            CLOSE v_required_parts_list;
        END IF;
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
            IF v_required_parts_list%ISOPEN THEN
                CLOSE v_required_parts_list;
            END IF;
            ROLLBACK;
            v_result := 'Error: Unable to reserve the required parts due to an unexpected issue.';
            RETURN v_result;
    END;

    RETURN v_result;
END;
/

