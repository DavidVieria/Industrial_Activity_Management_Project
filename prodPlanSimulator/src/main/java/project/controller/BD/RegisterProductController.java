package project.controller.BD;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class RegisterProductController {

    public String registerProduct(String productID, int productFamilyID, String productName, String productDescription) {
        Connection conn = null;
        CallableStatement stmt = null;
        String result = "";

        try {
            conn = BDController.getConnection();

            String sql = "{ ? = call register_product(?, ?, ?, ?) }";
            stmt = conn.prepareCall(sql);

            stmt.registerOutParameter(1, Types.VARCHAR);

            stmt.setString(2, productID);
            stmt.setInt(3, productFamilyID);
            stmt.setString(4, productName);
            stmt.setString(5, productDescription);

            stmt.execute();

            result = stmt.getString(1);
        } catch (SQLException e) {
            result = "Error: " + e.getMessage();
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
