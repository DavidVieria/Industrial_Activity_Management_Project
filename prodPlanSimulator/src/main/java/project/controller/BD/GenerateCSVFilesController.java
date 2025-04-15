package project.controller.BD;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateCSVFilesController {

    private static final String BOO_CSV = "files\\booBD.csv";
    private static final String OPERATIONS_CSV = "files\\operationsBD.csv";
    private static final String ITEMS_CSV = "files\\itemsBD.csv";
    private static final String WORKSTATIONS_CSV = "files\\workstationsBD.csv";

    public void executeBoo(String productID) {
        try (Connection connection = BDController.getConnection()) {
            generateBooCsv(connection, productID);
        } catch (SQLException | IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void generateBooCsv(Connection connection, String productID) throws SQLException, IOException {
        String query = """
            SELECT PBO.OperationID, OI.PartID, OI.Quantity
            FROM ProductBOOOperation PBO
            LEFT JOIN OperationInput OI ON PBO.OperationID = OI.OperationID
            WHERE PBO.ProductID = ?
            ORDER BY PBO.OperationNumber
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query);
             FileWriter writer = new FileWriter(BOO_CSV)) {

            stmt.setString(1, productID);
            ResultSet rs = stmt.executeQuery();

            writer.write("op_id;item_id;item_qtd;(;op1;op_qtd1;op2;op_qtd2;opN;op_qtdN;);(;item_id1;item_qtd1;item_id1;item_qtd1;item_id1;item_qtd1;)\n");
            writer.flush();

            Map<Integer, Integer> operationAndQuantity = new HashMap<>();
            Map<String, Integer> materialAndQuantity = new HashMap<>();
            List<String> products = new ArrayList<>();
            int operationID = 0;
            boolean first = true;

            while (rs.next()) {
                operationID = rs.getInt("OperationID");
                String materialID = rs.getString("PartID");
                int quantity = rs.getInt("Quantity");

                if (first) {
                    writer.write(operationID + ";" + productID + ";1;");
                    writer.flush();
                    first = false;
                }

                if (checkIsProduct(connection, materialID)) {
                    operationAndQuantity.put(operation_isProduct(connection, materialID), quantity);
                    products.add(materialID);
                } else {
                    materialAndQuantity.put(materialID, quantity);
                }
            }

            writer.write("(");
            for (Map.Entry<Integer, Integer> entry : operationAndQuantity.entrySet()) {
                writer.write(";" + entry.getKey() + ";" + entry.getValue());
            }
            writer.write(";);(");
            for (Map.Entry<String, Integer> entry : materialAndQuantity.entrySet()) {
                writer.write(";" + entry.getKey() + ";" + entry.getValue());
            }
            writer.write(";)\n");
            writer.flush();


            for (String product : products) {
                loopBOO(connection, product, writer);
            }

        }
    }

    private boolean checkIsProduct(Connection connection, String materialID) throws SQLException {
        String query = "SELECT COUNT(*) FROM Product WHERE ProductID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, materialID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    private int operation_isProduct(Connection connection, String materialID) throws SQLException {
        String query = """
            SELECT PBO.OperationID, OI.PartID, OI.Quantity
            FROM ProductBOOOperation PBO
            LEFT JOIN OperationInput OI ON PBO.OperationID = OI.OperationID
            WHERE PBO.ProductID = ?
            ORDER BY PBO.OperationNumber
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, materialID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("OperationID");
            }
            throw new SQLException("No operation found for material ID: " + materialID);
        }
    }

    private void loopBOO (Connection connection, String productID, FileWriter writer) throws SQLException, IOException {
        String query = """
            SELECT PBO.OperationID, OI.PartID, OI.Quantity
            FROM ProductBOOOperation PBO
            LEFT JOIN OperationInput OI ON PBO.OperationID = OI.OperationID
            WHERE PBO.ProductID = ?
            ORDER BY PBO.OperationNumber
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, productID);
            ResultSet rs = stmt.executeQuery();

            Map<Integer, Integer> operationAndQuantity = new HashMap<>();
            Map<String, Integer> materialAndQuantity = new HashMap<>();
            List<String> products = new ArrayList<>();
            int operationID = 0;
            boolean first = true;

            while (rs.next()) {
                operationID = rs.getInt("OperationID");
                String materialID = rs.getString("PartID");
                int quantity = rs.getInt("Quantity");

                if (first) {
                    writer.write(operationID + ";" + productID + ";1;");
                    first = false;
                }

                if (checkIsProduct(connection, materialID)) {
                    operationAndQuantity.put(operation_isProduct(connection, materialID), quantity);
                    products.add(materialID);
                } else {
                    materialAndQuantity.put(materialID, quantity);
                }
            }

            writer.write("(");
            for (Map.Entry<Integer, Integer> entry : operationAndQuantity.entrySet()) {
                writer.write(";" + entry.getKey() + ";" + entry.getValue());
            }
            writer.write(";);(");
            for (Map.Entry<String, Integer> entry : materialAndQuantity.entrySet()) {
                writer.write(";" + entry.getKey() + ";" + entry.getValue());
            }
            writer.write(";)\n");
            writer.flush();
        }
    }

    public void executeOperation() {
        try (Connection connection = BDController.getConnection()) {
            generateOperationsCsv(connection);
        } catch (SQLException | IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void generateOperationsCsv(Connection connection) throws SQLException, IOException {
        String query = """
            SELECT o.OperationID, ot.OperationTypeName
            FROM Operation o
            INNER JOIN OperationType ot ON o.OperationTypeID = ot.OperationTypeID
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query);
             FileWriter writer = new FileWriter(OPERATIONS_CSV)) {

            ResultSet rs = stmt.executeQuery();

            writer.write("op_id;op_name\n");
            writer.flush();

            while (rs.next()) {
                String opID = rs.getString("OperationID");
                String opName = rs.getString("OperationTypeName");
                writer.write(opID + ";" + opName + "\n");
                writer.flush();
            }
        }
    }

    public void executeItems() {
        try (Connection connection = BDController.getConnection()) {
            generateItemsCsv(connection);
        } catch (SQLException | IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void generateItemsCsv(Connection connection) throws SQLException, IOException {
        String query = """
            SELECT p.PartID, p.Description
            FROM Part p
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query);
             FileWriter writer = new FileWriter(ITEMS_CSV)) {

            ResultSet rs = stmt.executeQuery();

            writer.write("id_item;item_name\n");
            writer.flush();

            while (rs.next()) {
                String idItem = rs.getString("PartID");
                String itemName = rs.getString("Description");
                writer.write(idItem + ";" + itemName + "\n");
                writer.flush();
            }
        }
    }

    public boolean checkProductExists(String productID) {
        String query = "SELECT COUNT(*) FROM Product WHERE ProductID = ?";
        try (Connection connection = BDController.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, productID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    public void executeWorkstation() {
        try (Connection connection = BDController.getConnection()) {
            generateWorkstationsCsv(connection);
        } catch (SQLException | IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void generateWorkstationsCsv(Connection connection) throws SQLException, IOException {
        String query = """
        SELECT 
            wt.WorkstationTypeID AS workstation_id, 
            op.OperationID AS operation_id, 
            (op.ExpectedExecutionTime + cbda.SetupTime) AS total_time
        FROM 
            WorkstationType wt
        JOIN 
            CanBeDoneAt cbda ON wt.WorkstationTypeID = cbda.WorkstationTypeID
        JOIN 
            OperationType ot ON cbda.OperationTypeID = ot.OperationTypeID
        JOIN 
            Operation op ON ot.OperationTypeID = op.OperationTypeID
    """;

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery();
             FileWriter writer = new FileWriter(WORKSTATIONS_CSV)) {

            writer.write("workstation;operation_name;total_time\n");

            while (rs.next()) {
                String workstationId = rs.getString("workstation_id");
                String operationName = rs.getString("operation_id");
                double totalTime = rs.getDouble("total_time");

                int totalTimeInt = (int) Math.round(totalTime);

                writer.write(String.format("%s;%s;%d\n", workstationId, operationName, totalTimeInt));
            }
        }
    }

    public void executeMultiplesBoo(List<String> productsID) {
        try (Connection connection = BDController.getConnection();
             FileWriter writer = new FileWriter(BOO_CSV)) {
            writer.write("op_id;item_id;item_qtd;(;op1;op_qtd1;op2;op_qtd2;opN;op_qtdN;);(;item_id1;item_qtd1;item_id2;item_qtd2;item_idN;item_qtdN;)");
            writer.write("\n");
            writer.flush();

            for (String productID : productsID) {
                generateBooCsv2(connection, productID, writer);
            }

        } catch (SQLException | IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void generateBooCsv2(Connection connection, String productID, FileWriter writer) throws SQLException, IOException {
        String query = """
            SELECT PBO.OperationID, OI.PartID, OI.Quantity
            FROM ProductBOOOperation PBO
            LEFT JOIN OperationInput OI ON PBO.OperationID = OI.OperationID
            WHERE PBO.ProductID = ?
            ORDER BY PBO.OperationNumber
        """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, productID);

            try (ResultSet rs = stmt.executeQuery()) {

                Map<Integer, Integer> operationAndQuantity = new HashMap<>();
                Map<String, Integer> materialAndQuantity = new HashMap<>();
                List<String> products = new ArrayList<>();

                boolean first = true;

                while (rs.next()) {
                    int operationID = rs.getInt("OperationID");
                    String materialID = rs.getString("PartID");
                    int quantity = rs.getInt("Quantity");

                    if (first) {
                        writer.write(operationID + ";" + productID + ";1;");
                        writer.flush();
                        first = false;
                    }

                    if (checkIsProduct(connection, materialID)) {
                        operationAndQuantity.put(operation_isProduct(connection, materialID), quantity);
                        products.add(materialID);
                    } else {
                        materialAndQuantity.put(materialID, quantity);
                    }
                }

                // Write operations to the CSV file
                writer.write("(");
                for (Map.Entry<Integer, Integer> entry : operationAndQuantity.entrySet()) {
                    writer.write(";" + entry.getKey() + ";" + entry.getValue());
                }
                writer.write(";);");

                // Write materials to the CSV file
                writer.write("(");
                for (Map.Entry<String, Integer> entry : materialAndQuantity.entrySet()) {
                    writer.write(";" + entry.getKey() + ";" + entry.getValue());
                }
                writer.write(";)\n");
                writer.flush();

                // Recursively process products
                for (String product : products) {
                    loopBOO(connection, product, writer);
                }
            }
        }
    }

}
