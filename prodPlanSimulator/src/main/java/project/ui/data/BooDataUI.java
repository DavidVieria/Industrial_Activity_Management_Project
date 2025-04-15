package project.ui.data;

import project.controller.DataLoaderController;

import java.util.Scanner;

public class BooDataUI implements Runnable {
    private final DataLoaderController controller;

    public BooDataUI(DataLoaderController controller) {
        this.controller = controller;
    }

    public void run() {
        System.out.println();

        controller.setBomCsvPath(loadBom());
    }

    private String loadBom() {
        Scanner scanner = new Scanner(System.in);
        String basePath = "files\\";
        String itemsPath = null;

        while (itemsPath == null || itemsPath.trim().isEmpty()) {
            System.out.print("Enter the file name of the BOO (e.g. boo.csv): ");
            itemsPath = scanner.nextLine().trim();
        }

        itemsPath = basePath + itemsPath;
        return itemsPath;
    }
}