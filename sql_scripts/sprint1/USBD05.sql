SELECT  C.Name AS "Client Name",
        P.Name AS "Product Name",
        "M O".Quantity AS "Quantity",
        O.DeliveryDate AS "Delivery Date",
        O.OrderID AS "Order Number"
FROM "Order" O

JOIN "MANUFACTURINGORDER" "M O"
        ON O.OrderID = "M O".OrderID
JOIN CLIENT C
        ON O.ClientID = C.ClientID
JOIN Product P
        ON "M O".ProductID = P.ProductID
WHERE O.DeliveryDate BETWEEN TO_DATE('01-01-2024', 'DD-MM-YYYY')
        AND TO_DATE('31-12-2024', 'DD-MM-YYYY')
ORDER BY O.DeliveryDate, P.ProductID, O.OrderDate    -- Prioritizes the delivery date
;