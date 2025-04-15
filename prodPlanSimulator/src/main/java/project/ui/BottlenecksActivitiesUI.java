package project.ui;

import project.controller.ActivityGraphController;
import project.controller.BottlenecksActivitiesController;
import project.domain.genericDataStructures.DependencyComparator;
import project.domain.genericDataStructures.Entry;
import project.domain.genericDataStructures.HeapPriorityQueue;
import project.domain.service.ActivityInfo;

import java.util.Iterator;

public class BottlenecksActivitiesUI implements Runnable {
    private final ActivityGraphController activityGraphController;
    private final BottlenecksActivitiesController bottlenecksActivitiesController;

    public BottlenecksActivitiesUI(ActivityGraphController activityGraphController) {
        this.activityGraphController = activityGraphController;
        this.bottlenecksActivitiesController = new BottlenecksActivitiesController(activityGraphController.getGraph());
    }

    @Override
    public void run() {
        if (activityGraphController.isDependencyCircularDetector()) {
            System.out.println("Functionality is not possible as circular dependencies were detected.");
            return;
        }
        if (!activityGraphController.isCalculatedTimes()) {
            System.out.println("Times have not yet been calculated, perform functionality 3.");
            return;
        }

        HeapPriorityQueue<ActivityInfo, Integer> dependencyQueue = bottlenecksActivitiesController.generateDependencyQueue();
        HeapPriorityQueue<ActivityInfo, Integer> copiedQueue = new HeapPriorityQueue<>(new DependencyComparator<>());

        for (int i = 0; i < dependencyQueue.size(); i++) {
            Entry<ActivityInfo, Integer> entry = dependencyQueue.heap.get(i); // Acesse diretamente a lista 'heap'
            copiedQueue.insert(entry.getKey(), entry.getValue());
        }



        int numActivities = dependencyQueue.size();

        System.out.println("Top Activities with the highest dependency levels:");
        int count = 0;
        int topCount = Math.min(numActivities, 5);

        for (int i = 0; i < topCount; i++) {
            Entry<ActivityInfo, Integer> entry = copiedQueue.removeMin();
            ActivityInfo activity = entry.getKey();
            System.out.printf("%d. Activity: %s - Dependency level: %d\n", count + 1, activity.getActivity().getActID(), activity.getDependencyLevel());
            count++;
        }

        if (numActivities < 5) {
            System.out.printf("\nThere are only %d activities in the list. This is the entire list!\n", numActivities);
        } else {

            while (true) {
                System.out.println("\nWould you like to see the full list of activities (yes/no)?");
                String userInput = System.console().readLine().toLowerCase();

                if (userInput.equals("yes")) {
                    System.out.println("\nFull list of activities:");
                    int count2 = 0;
                    while (!dependencyQueue.isEmpty()) {
                        Entry<ActivityInfo, Integer> entry = dependencyQueue.removeMin();
                        ActivityInfo activity = entry.getKey();
                        System.out.printf("%d. Activity: %s - Dependency level: %d\n", count2 + 1, activity.getActivity().getActID(), activity.getDependencyLevel());
                        count2++;
                    }
                    break;
                } else if (userInput.equals("no")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please type 'yes' or 'no'.");
                }
            }
        }
    }
}
