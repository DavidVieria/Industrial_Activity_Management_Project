package project.repository;

import project.domain.model.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityRepository {

    private List<Activity> activities;

    public ActivityRepository(List<Activity> activities) {
        this.activities = activities;
    }

    public ActivityRepository() {
        this.activities = new ArrayList<>();
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void addActivities(List<Activity> activities) {
        this.activities.addAll(activities);
    }

    public List<Activity> getAllActivities() {
        return new ArrayList<>(activities);
    }

    public void clearActivities() {
        activities.clear();
    }

    public boolean isEmpty() {
        return activities.isEmpty();
    }

    public int getActivityCount() {
        return activities.size();
    }

    public Activity getActivityByID(String id) {
        for (Activity activity : activities) {
            if (activity.getActID().equals(id)) {
                return activity;
            }
        }
        return null;
    }
}
