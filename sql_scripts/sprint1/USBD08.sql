SELECT Op.Description AS "Operation Description",
       WT.Name AS "Workstation Type"
FROM Operation Op
         JOIN OperationWorkstationType OWT
              ON Op.OperationID = OWT.OperationID
         JOIN WorkstationType WT
              ON OWT.WorkstationTypeID = WT.WorkstationTypeID
ORDER BY Op.Description, WT.Name;
