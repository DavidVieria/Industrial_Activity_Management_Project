package project.ui.BD;

import project.controller.BD.DeactivateCustomerController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DeactivateCustomerUI implements Runnable {
    private final DeactivateCustomerController controller;

    public DeactivateCustomerUI() {
        this.controller = new DeactivateCustomerController();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        ResultSet rs1 = controller.listCustomerActive();

        try {
            System.out.println();

            if (rs1 == null) {
                System.out.println("There are no active clients");
            }

            System.out.println("List Active Clients (Name/ID):");
            while (rs1 != null && rs1.next()) {
                System.out.println("- " + rs1.getString("Name") + " (" + rs1.getInt("ClientID") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int clientID = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("\nEnter the ClientID of the client to deactivate: ");
            try {
                clientID = scanner.nextInt();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number for ClientID.");
                scanner.nextLine();
            }
        }

        String result = controller.deactivateCustomer(clientID);

        System.out.println(result);

    }

}