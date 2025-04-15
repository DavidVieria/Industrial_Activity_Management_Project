package project.ui.data;

import project.controller.DataLoaderController;

import java.util.Scanner;

public class OrderDataUI implements Runnable {

    private final DataLoaderController controller;

    public OrderDataUI(DataLoaderController controller) {
        this.controller = controller;
    }

    public void run() {
        System.out.println();

        controller.setOrdersCsvPath(loadOrders());
    }

    private String loadOrders() {
        Scanner scanner = new Scanner(System.in);
        String basePath = "files\\";
        String ordersPath = null;

        while (ordersPath == null || ordersPath.trim().isEmpty()) {
            System.out.print("Enter the file name of the orders (e.g. orders.csv): ");
            ordersPath = scanner.nextLine().trim();
        }

        ordersPath = basePath + ordersPath;
        return ordersPath;
    }
}
