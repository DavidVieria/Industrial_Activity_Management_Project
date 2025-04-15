package project.controller.BD;

import java.sql.*;

public class CheckMaterialsInStockController {

    public boolean checkMaterialsInStockForOrder(int orderID) {
        Connection conn = null;
        CallableStatement stmt = null;
        boolean isStockSufficient = false;

        try {
            conn = BDController.getConnection();

            // Enable DBMS_OUTPUT
            stmt = conn.prepareCall("{call dbms_output.enable()}");
            stmt.execute();
            stmt.close();

            // Call the function
            String sql = "{ ? = call check_materials_in_stock_for_order(?) }";
            stmt = conn.prepareCall(sql);

            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, orderID);

            stmt.execute();

            int resultInt = stmt.getInt(1);
            isStockSufficient = (resultInt == 1);

            // Print the specific output message
            System.out.println("The order " + (isStockSufficient ? "can be fulfilled with the available stock." : "CANNOT be fulfilled with the available stock."));

            // Retrieve DBMS_OUTPUT
            stmt = conn.prepareCall("{call dbms_output.get_line(?, ?)}");
            String line;
            int status;
            do {
                stmt.registerOutParameter(1, Types.VARCHAR);
                stmt.registerOutParameter(2, Types.INTEGER);
                stmt.execute();
                line = stmt.getString(1);
                status = stmt.getInt(2);
                if (line != null) {
                    System.out.println(line);
                }
            } while (status == 0);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isStockSufficient;
    }
}