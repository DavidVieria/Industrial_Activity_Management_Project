package project.controller.BD;

import java.sql.*;

public class RegisterWorkstationController {

    public String registerWorkstation(int workstationId, String workstationTypeId, String name, String description) {
        Connection conn = null;
        CallableStatement stmt = null;
        String result = "";

        try {
            conn = BDController.getConnection();

            String sql = "{ ? = call register_workstation(?, ?, ?, ?) }";
            stmt = conn.prepareCall(sql);

            stmt.registerOutParameter(1, Types.VARCHAR);

            stmt.setInt(2, workstationId);
            stmt.setString(3, workstationTypeId);
            stmt.setString(4, name);
            stmt.setString(5, description);

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
