package project.ui.BD;

import project.controller.BD.ListPartsProductController;

import java.sql.*;

public class ListPartsProductUI implements Runnable {

    private final ListPartsProductController controller;

    public ListPartsProductUI() {
        this.controller = new ListPartsProductController();
    }

    @Override
    public void run() {

        System.out.println();
        System.out.print("Enter the product ID: ");
        String productId = new java.util.Scanner(System.in).nextLine();

        ResultSet rs = controller.listPartsProduct(productId);

        try {
            if (rs != null && rs.next()) {
                do {
                    String materialId = rs.getString("v_material_id");
                    String materialDescription = rs.getString("v_material_description");
                    double totalQuantity = rs.getDouble("v_total_quantity");
                    String unit = rs.getString("v_unit");

                    System.out.println("Material ID: " + materialId);
                    System.out.println("Description: " + materialDescription);
                    System.out.println("Total Quantity: " + totalQuantity);
                    System.out.println("Unit: " + unit);
                    System.out.println("-----------------------------------");
                } while (rs.next());
            } else {
                System.out.println("The product does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
