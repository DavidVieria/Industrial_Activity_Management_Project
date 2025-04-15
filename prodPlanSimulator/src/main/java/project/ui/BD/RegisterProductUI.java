package project.ui.BD;

import project.controller.BD.RegisterProductController;

import java.util.InputMismatchException;
import java.util.Scanner;

public class RegisterProductUI implements Runnable {
    private final RegisterProductController controller;

    public RegisterProductUI() {
        this.controller = new RegisterProductController();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Product ID: ");
        String productID = scanner.nextLine();

        int productFamilyID = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter Product Family ID: ");
            try {
                productFamilyID = Integer.parseInt(scanner.nextLine());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for Product Family ID.");
            }
        }

        System.out.print("Enter Product Name: ");
        String productName = scanner.nextLine();

        System.out.print("Enter Product Description: ");
        String productDescription = scanner.nextLine();

        String result = controller.registerProduct(productID, productFamilyID, productName, productDescription);

        System.out.println();
        System.out.println(result);
    }
}
