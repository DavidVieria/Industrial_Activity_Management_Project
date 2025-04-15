SELECT DISTINCT O.OrderID  AS "Order Number",
                "W T".Name AS "Workstation Type"
FROM "Order" O

JOIN "MANUFACTURINGORDER" "M O"
        ON O.OrderID = "M O".OrderID
JOIN Product P
        ON "M O".ProductID = P.ProductID
JOIN ProductFamily "P F"
        ON P.ProductFamilyID = "P F".ProductFamilyID
JOIN BOO B
        ON "P F".ProductFamilyID = B.ProductFamilyID
JOIN ProductBOOOperation "P B O"
        ON B.ProductFamilyID = "P B O".ProductFamilyID
JOIN Operation Op
        ON "P B O".OperationID = Op.OperationID
JOIN OperationWorkstationType "O W T"
        ON Op.OperationID = "O W T".OperationID
JOIN WorkstationType "W T"
        ON "O W T".WorkstationTypeID = "W T".WorkstationTypeID
WHERE O.OrderID = 4   --Change to the desired US number
;