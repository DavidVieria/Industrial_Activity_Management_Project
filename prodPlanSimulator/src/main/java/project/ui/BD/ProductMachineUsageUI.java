package project.ui.BD;

import project.controller.BD.ProductMachineUsageController;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMachineUsageUI implements Runnable {

    private final ProductMachineUsageController controller;

    public ProductMachineUsageUI() {
        this.controller = new ProductMachineUsageController();
    }

    public void run() {
        ResultSet rs = controller.findProductUsingAllMachineTypes();

        try {
            if (rs == null) {
                System.out.println("There is no product that can use all types of workstations");
                return;
            }

            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                String productId = rs.getString("ProductID");
                String productName = rs.getString("Name");

                System.out.println("Product - ID: " + productId + " | Name: " + productName);
            }

            if (!hasResults) {
                System.out.println("There is no product that can use all types of workstations");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
