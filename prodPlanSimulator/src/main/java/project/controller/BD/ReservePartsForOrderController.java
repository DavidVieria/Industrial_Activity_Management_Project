package project.controller.BD;

import java.sql.*;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

public class ReservePartsForOrderController {

    public String reserveRequiredPartsForOrder(int orderID) {
        Connection conn = null;
        OracleCallableStatement stmt = null;
        String result = "";

        try {
            conn = BDController.getConnection();

            // Enable DBMS_OUTPUT
            stmt = (OracleCallableStatement) conn.prepareCall("{call dbms_output.enable(1000000)}");
            stmt.execute();

            // Call the function
            String sql = "{ ? = call reserve_required_parts_for_order(?) }";
            stmt = (OracleCallableStatement) conn.prepareCall(sql);

            stmt.registerOutParameter(1, Types.VARCHAR);
            stmt.setInt(2, orderID);

            stmt.execute();

            result = stmt.getString(1);

            // Capture DBMS_OUTPUT
            stmt = (OracleCallableStatement) conn.prepareCall(
                "declare " +
                "    num integer := 1000; " +
                "begin " +
                "    dbms_output.get_lines(?, num); " +
                "end;"
            );
            stmt.registerOutParameter(1, OracleTypes.ARRAY, "SYS.DBMSOUTPUT_LINESARRAY");
            stmt.execute();

            Array array = stmt.getArray(1);
            if (array != null) {
                String[] lines = (String[]) array.getArray();
                for (String line : lines) {
                    if (line != null) {
                        System.out.println(line);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
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