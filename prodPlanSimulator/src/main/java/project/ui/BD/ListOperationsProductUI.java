package project.ui.BD;

import project.controller.BD.ListOperationsProductController;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListOperationsProductUI implements Runnable {
    private final ListOperationsProductController controller;

    public ListOperationsProductUI() {
        this.controller = new ListOperationsProductController();
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
                    System.out.println("Level_Depth: " + rs.getInt("Level_Depth"));
                    System.out.println("ProductID: " + rs.getString("ProductID"));
                    System.out.println("OperationID: " + rs.getInt("OperationID"));
                    System.out.println("OperationNumber: " + rs.getInt("OperationNumber"));
                    System.out.println("OperationTypeDescription: " + rs.getString("OperationTypeName"));
                    System.out.println("WorkstationTypeName: " + rs.getString("WorkstationTypeName"));
                    System.out.println("InputMaterial: " + rs.getString("InputPart"));
                    System.out.println("InputQuantity: " + rs.getInt("InputQuantity"));
                    System.out.println("OutputMaterial: " + rs.getString("OutputPart"));
                    System.out.println("OutputQuantity: " + rs.getInt("OutputQuantity"));
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
