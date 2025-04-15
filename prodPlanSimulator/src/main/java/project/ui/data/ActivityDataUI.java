package project.ui.data;

import project.controller.DataLoaderController;

import java.util.Scanner;

public class ActivityDataUI implements Runnable {

    private final DataLoaderController controller;

    public ActivityDataUI(DataLoaderController controller) {
        this.controller = controller;
    }

    public void run() {
        System.out.println();

        controller.setActivitiesCsvPath(loadActivities());
    }

    private String loadActivities() {
        Scanner scanner = new Scanner(System.in);
        String basePath = "files\\";
        String activitiesPath = null;

        while (activitiesPath == null || activitiesPath.trim().isEmpty()) {
            System.out.print("Enter the file name of the activities (e.g. activities.csv): ");
            activitiesPath = scanner.nextLine().trim();
        }

        activitiesPath = basePath + activitiesPath;
        return activitiesPath;
    }
}
