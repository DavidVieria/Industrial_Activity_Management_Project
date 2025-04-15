package project.controller.BD;

import java.sql.*;

public class TriggerOperationController {

    public String insertOperation(int operationID, int operationTypeID, double expectedExecutionTime) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String result = "";

        try {
            conn = BDController.getConnection();

String sql = "MERGE INTO Operation o " +
             "USING (SELECT ? AS OperationID, ? AS OperationTypeID, ? AS ExpectedExecutionTime FROM dual) src " +
             "ON (o.OperationID = src.OperationID) " +
             "WHEN MATCHED THEN " +
             "UPDATE SET o.OperationTypeID = src.OperationTypeID, o.ExpectedExecutionTime = src.ExpectedExecutionTime " +
             "WHEN NOT MATCHED THEN " +
             "INSERT (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (src.OperationID, src.OperationTypeID, src.ExpectedExecutionTime)";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, operationID);
            stmt.setInt(2, operationTypeID);
            stmt.setDouble(3, expectedExecutionTime);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                result = "Operation Inserted Successfully";
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 20001) {
                result = "Error: The expected execution time (" + expectedExecutionTime + ") exceeds the maximum allowed time for the workstations.";
            } else {
                result = "Error: " + e.getMessage();
            }
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
