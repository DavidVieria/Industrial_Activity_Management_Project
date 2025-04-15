package project.ui.data;

import project.controller.DataLoaderController;

import java.util.Scanner;

public class OperationDataUI implements Runnable {
    private final DataLoaderController controller;

    public OperationDataUI(DataLoaderController controller) {
        this.controller = controller;
    }

    public void run() {
        System.out.println();

        controller.setOperationsCsvPath(loadOperations());
    }

    private String loadOperations() {
        Scanner scanner = new Scanner(System.in);
        String basePath = "files\\";
        String itemsPath = null;

        while (itemsPath == null || itemsPath.trim().isEmpty()) {
            System.out.print("Enter the file name of the operations (e.g. operations.csv): ");
            itemsPath = scanner.nextLine().trim();
        }

        itemsPath = basePath + itemsPath;
        return itemsPath;
    }
}