package project.ui.data;

import project.controller.DataLoaderController;

import java.util.Scanner;

public class WorkstationDataUI implements Runnable {
    private final DataLoaderController controller;

    public WorkstationDataUI(DataLoaderController controller) {
        this.controller = controller;
    }

    public void run() {
        System.out.println();

        controller.setWorkstationsCsvPath(loadWorkStations());
    }

    private String loadWorkStations() {
        Scanner scanner = new Scanner(System.in);
        String basePath = "files\\";
        String workstationsPath = null;

        while (workstationsPath == null || workstationsPath.trim().isEmpty()) {
            System.out.print("Enter the file name of the workstations (e.g. workstations.csv): ");
            workstationsPath = scanner.nextLine().trim();
        }

        workstationsPath = basePath + workstationsPath;
        return workstationsPath;
    }
}