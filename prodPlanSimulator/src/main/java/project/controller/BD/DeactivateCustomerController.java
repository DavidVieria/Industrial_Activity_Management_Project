package project.controller.BD;

import java.sql.*;

public class DeactivateCustomerController {

    public ResultSet listCustomerActive() {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;

        try {

            conn = BDController.getConnection();

            String sql = "{ ? = call list_customers_active() }";
            stmt = conn.prepareCall(sql);

            stmt.registerOutParameter(1, Types.REF_CURSOR);

            stmt.execute();

            rs = (ResultSet) stmt.getObject(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    public String deactivateCustomer(int clientId) {
        Connection conn = null;
        CallableStatement stmt = null;
        String result = null;

        try {

            conn = BDController.getConnection();

            String sql = "{ ? = call deactivate_customer(?) }";
            stmt = conn.prepareCall(sql);

            stmt.setInt(2, clientId);

            stmt.registerOutParameter(1, Types.VARCHAR);

            stmt.execute();

            result = stmt.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

}
