package project.controller.BD;

import java.sql.*;

public class ListReservedPartsController {

    public void listReservedParts() {
        Connection conn = null;
        CallableStatement stmtProcedure = null;
        CallableStatement stmtDbmsOutput = null;
        Statement stmtEnableDbmsOutput = null;

        try {
            conn = BDController.getConnection();

            // Enable DBMS_OUTPUT
            stmtEnableDbmsOutput = conn.createStatement();
            stmtEnableDbmsOutput.executeUpdate("BEGIN DBMS_OUTPUT.ENABLE(NULL); END;");

            // Call the procedure
            String sqlProcedure = "{ call list_reserved_parts }";
            stmtProcedure = conn.prepareCall(sqlProcedure);
            stmtProcedure.execute();

            // Retrieve the DBMS_OUTPUT
            stmtDbmsOutput = conn.prepareCall(
                "DECLARE " +
                "    l_line VARCHAR2(255); " +
                "    l_done NUMBER; " +
                "    l_buffer LONG; " +
                "BEGIN " +
                "    LOOP " +
                "        EXIT WHEN LENGTH(l_buffer) + 255 > 32000 OR l_done = 1; " +
                "        DBMS_OUTPUT.GET_LINE(l_line, l_done); " +
                "        l_buffer := l_buffer || l_line || chr(10); " +
                "    END LOOP; " +
                "    :buffer := l_buffer; " +
                "END;"
            );
            stmtDbmsOutput.registerOutParameter(1, Types.VARCHAR);
            stmtDbmsOutput.execute();

            // Print the output
            String dbmsOutput = stmtDbmsOutput.getString(1);
            System.out.println(dbmsOutput);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmtProcedure != null) stmtProcedure.close();
                if (stmtDbmsOutput != null) stmtDbmsOutput.close();
                if (stmtEnableDbmsOutput != null) stmtEnableDbmsOutput.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}