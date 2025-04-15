package project.controller.BD;

import java.sql.*;

public class TriggerPreventCyclesController {

    public String insertOrUpdateOperationInput(int operationID, String partID, int quantity) {
        Connection conn = null;
        PreparedStatement stmt = null;
        String result = "";

        try {
            conn = BDController.getConnection();

            String sql = "MERGE INTO OperationInput oi " +
                         "USING (SELECT ? AS OperationID, ? AS PartID, ? AS Quantity FROM dual) src " +
                         "ON (oi.OperationID = src.OperationID AND oi.PartID = src.PartID) " +
                         "WHEN MATCHED THEN " +
                         "UPDATE SET oi.Quantity = src.Quantity " +
                         "WHEN NOT MATCHED THEN " +
                         "INSERT (OperationID, PartID, Quantity) VALUES (src.OperationID, src.PartID, src.Quantity)";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, operationID);
            stmt.setString(2, partID);
            stmt.setInt(3, quantity);

            stmt.executeUpdate();
            result = "Operation Input Inserted/Updated Successfully";
        } catch (SQLException e) {
            if (e.getErrorCode() == 20002) {
                result = "Error: It is not allowed to use a product as input in its own BOO (Bill of Operations). The product causing the error is: " + partID;
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