SELECT  "M O".OrderID  AS "Order Number",
        M.Description AS "Material Description",
        SUM("M O".Quantity)  AS "Quantity",
        COALESCE("P B M".MaterialClassification, 'Not defined') AS "Material Classification"   --If it is a raw material or component
FROM "MANUFACTURINGORDER" "M O"

JOIN Product P
        ON "M O".ProductID = P.ProductID
JOIN BOM B
        ON "M O".ProductID = B.ProductID
JOIN ProductBOMMaterial "P B M"
        ON B.ProductID = "P B M".ProductID
JOIN Material M
        ON "P B M".MaterialNumber = M.MaterialNumber
WHERE "M O".OrderID = 6   --Change to the desired US number
GROUP BY "M O".OrderID, M.Description, "P B M".MaterialClassification
ORDER BY "P B M".MaterialClassification, M.Description;