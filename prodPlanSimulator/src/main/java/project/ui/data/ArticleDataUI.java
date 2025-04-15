package project.ui.data;

import project.controller.DataLoaderController;

import java.util.Scanner;

public class ArticleDataUI implements Runnable {
    private final DataLoaderController controller;

    public ArticleDataUI(DataLoaderController controller) {
        this.controller = controller;
    }

    public void run() {
        System.out.println();

        controller.setArticlesCsvPath(loadArticles());
    }

    private String loadArticles() {
        Scanner scanner = new Scanner(System.in);
        String basePath = "files\\";
        String itemsPath = null;

        while (itemsPath == null || itemsPath.trim().isEmpty()) {
            System.out.print("Enter the file name of the articles (e.g. articles.csv): ");
            itemsPath = scanner.nextLine().trim();
        }

        itemsPath = basePath + itemsPath;
        return itemsPath;
    }
}