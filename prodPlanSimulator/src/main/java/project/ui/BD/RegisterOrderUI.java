package project.ui.BD;

import project.controller.BD.RegisterOrderController;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class RegisterOrderUI implements Runnable {
    private final RegisterOrderController controller;
    private final Scanner scanner;

    public RegisterOrderUI() {
        this.controller = new RegisterOrderController();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        int orderId = getIntInput("Enter Order ID: ");
        int clientId = getIntInput("Enter Client ID: ");

        String deliveryDate = getDateInput("Enter Delivery Date (dd/MM/yyyy): ");
        String orderDate = getDateInput("Enter Order Date (dd/MM/yyyy): ");

        String clientState = getClientStateInput("Enter Client State (Active/Inactive): ");

        System.out.println();
        String result = controller.registerOrder(orderId, clientId, deliveryDate, orderDate, clientState);

        System.out.println(result);
    }

    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }

    private String getDateInput(String prompt) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        while (true) {
            System.out.print(prompt);
            String dateStr = scanner.nextLine();
            try {
                Date date = sdf.parse(dateStr);
                SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                return targetFormat.format(date);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Please enter the date in dd/MM/yyyy format.");
            }
        }
    }

    private String getClientStateInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String state = scanner.nextLine().trim();
            if (state.equalsIgnoreCase("Active") || state.equalsIgnoreCase("Inactive")) {
                return state;
            } else {
                System.out.println("Invalid input. Please enter 'Active' or 'Inactive'.");
            }
        }
    }
}
