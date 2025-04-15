package project.ui;

import project.controller.ActivityGraphController;
import project.controller.TopologicalSortActivitiesController;
import project.domain.service.ActivityInfo;

import java.util.LinkedList;

public class TopologicalSortActivitiesUI implements Runnable {

    private final ActivityGraphController activityGraphController;
    private final TopologicalSortActivitiesController controller;

    public TopologicalSortActivitiesUI(ActivityGraphController activityGraphController) {
        this.activityGraphController = activityGraphController;
        this.controller = new TopologicalSortActivitiesController();
    }

    public void run() {
        if (activityGraphController.isDependencyCircularDetector()) {
            System.out.println("Functionality is not possible as circular dependencies were detected.");
            return;
        }

        LinkedList<ActivityInfo> activities = controller.topologicalSortActivities(activityGraphController.getGraph());
        printActivities(activities);
    }

    public void printActivities(LinkedList<ActivityInfo> activities) {
        System.out.println();
        for (int i = 0; i < activities.size(); i++) {
            ActivityInfo activityInfo = activities.get(i);

            System.out.print(activityInfo.getActivity().getActID());

            if (i < activities.size() - 1) {
                System.out.print(" --> ");
            }
        }
        System.out.println();
    }
}
