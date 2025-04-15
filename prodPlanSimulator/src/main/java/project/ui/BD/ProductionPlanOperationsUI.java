package project.ui.BD;

import project.controller.BD.ProductionPlanOperationsController;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductionPlanOperationsUI implements Runnable {

    private final ProductionPlanOperationsController controller;

    public ProductionPlanOperationsUI() {
        this.controller = new ProductionPlanOperationsController();
    }

    @Override
    public void run() {
        System.out.print("Digite o ID do produto: ");
        String idProduto = new java.util.Scanner(System.in).nextLine();

        ResultSet rs = controller.listOperationsProduct(idProduto);

        try {
            if (rs != null) {
                int rowCount = 0;
                while (rs.next()) {
                    System.out.println("Level Depth: " + rs.getInt("Level_Depth"));
                    System.out.println("Product ID: " + rs.getString("ProductID"));
                    System.out.println("Operation ID: " + rs.getInt("OperationID"));
                    System.out.println("Operation Number: " + rs.getInt("OperationNumber"));
                    System.out.println("Operation Type Name: " + rs.getString("OperationTypeName"));
                    System.out.println("Workstation Type Name: " + rs.getString("WorkstationTypeName"));
                    System.out.println("Execution Time: " + rs.getString("ExpectedExecutionTime") + " minutes");
                    System.out.println("Input Part: " + rs.getString("InputPartID") + " - Quantity: " + rs.getInt("InputQuantity"));
                    System.out.println("Output Part: " + rs.getString("OutputPartID") + " - Quantity: " + rs.getInt("OutputQuantity"));
                    System.out.println("-----------------------------------");
                    rowCount++;
                }
                if (rowCount == 0) {
                    System.out.println("The product does not exist: " + idProduto);
                }
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
