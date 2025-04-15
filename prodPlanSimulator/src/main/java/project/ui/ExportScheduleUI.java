package project.ui;

import project.controller.ActivityGraphController;
import project.controller.ExportScheduleController;

import java.io.IOException;

public class ExportScheduleUI implements Runnable {

    private final ActivityGraphController activityGraphController;
    private final ExportScheduleController exportScheduleController;

    public ExportScheduleUI(ActivityGraphController activityGraphController) {
        this.activityGraphController = activityGraphController;
        this.exportScheduleController = new ExportScheduleController(activityGraphController.getGraph());
    }

    public void run() {
        if (activityGraphController.isDependencyCircularDetector()) {
            System.out.println("Functionality is not possible as circular dependencies were detected.");
            return;
        }
        if (!activityGraphController.isCalculatedTimes()) {
            System.out.println("Times have not yet been calculated, perform functionality 3.");
            return;
        }

        String fileName = "files\\schedule.csv";
        try {
            boolean success = exportScheduleController.exportScheduleCSV(fileName);
            if (success) {
                System.out.println("Schedule exported successfully!");
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}