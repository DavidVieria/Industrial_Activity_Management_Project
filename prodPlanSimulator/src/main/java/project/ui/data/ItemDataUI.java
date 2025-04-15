package project.ui.data;

import project.controller.DataLoaderController;

import java.util.Scanner;

public class ItemDataUI implements Runnable {
    private final DataLoaderController controller;

    public ItemDataUI(DataLoaderController controller) {
        this.controller = controller;
    }

    public void run() {
        System.out.println();

        controller.setItemsCsvPath(loadItems());
    }

    private String loadItems() {
        Scanner scanner = new Scanner(System.in);
        String basePath = "files\\";
        String itemsPath = null;

        while (itemsPath == null || itemsPath.trim().isEmpty()) {
            System.out.print("Enter the file name of the items (e.g. items.csv): ");
            itemsPath = scanner.nextLine().trim();
        }

        itemsPath = basePath + itemsPath;
        return itemsPath;
    }
}