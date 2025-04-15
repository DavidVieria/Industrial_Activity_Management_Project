package project.controller.BD;

import java.sql.*;

public class ProductMachineUsageController {

    public ResultSet findProductUsingAllMachineTypes() {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;

        try {

            conn = BDController.getConnection();

            String sql = "{ ? = call find_product_All_Machine_Types() }";
            stmt = conn.prepareCall(sql);

            stmt.registerOutParameter(1, Types.REF_CURSOR);

            stmt.execute();

            rs = (ResultSet) stmt.getObject(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }
}
