package project.controller.BD;

import java.sql.*;

public class RegisterOrderController {

    public String registerOrder(int orderId, int clientId, String deliveryDate, String orderDate, String clientState) {
        Connection conn = null;
        CallableStatement stmt = null;
        String result = "";

        try {
            conn = BDController.getConnection();

            java.sql.Date deliverySqlDate = java.sql.Date.valueOf(deliveryDate);
            java.sql.Date orderSqlDate = java.sql.Date.valueOf(orderDate);

            String sql = "{ ? = call register_order(?, ?, ?, ?, ?) }";
            stmt = conn.prepareCall(sql);

            stmt.setInt(2, orderId);
            stmt.setInt(3, clientId);
            stmt.setDate(4, deliverySqlDate);
            stmt.setDate(5, orderSqlDate);
            stmt.setString(6, clientState);

            stmt.registerOutParameter(1, Types.VARCHAR);

            stmt.execute();

            result = stmt.getString(1);

        } catch (SQLException e) {
            e.printStackTrace();
            result = "ERROR: " + e.getMessage();
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
