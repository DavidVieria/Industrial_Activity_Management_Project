package project.domain.service;

import project.domain.model.Activity;

import java.util.Objects;

/**
 * Represents information about an activity, including scheduling details
 * such as early start, early finish, late start, late finish, slack, and dependency level.
 */
public class ActivityInfo {

    private Activity activity;
    private double es; // Early start
    private double ef; // Early finish
    private double ls; // Late start
    private double lf; // Late finish
    private double slack; // Slack time
    private int dependencyLevel; // Level of dependency for the activity

    /**
     * Constructs an ActivityInfo instance with the specified activity.
     * Initializes scheduling attributes to 0.
     *
     * @param activity the activity associated with this information
     */
    public ActivityInfo(Activity activity) {
        this.activity = activity;
        this.es = 0;
        this.ef = 0;
        this.ls = 0;
        this.lf = 0;
        this.slack = 0;
        this.dependencyLevel = 0;
    }

    /**
     * Gets the activity associated with this ActivityInfo.
     *
     * @return the activity
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * Sets the activity associated with this ActivityInfo.
     *
     * @param activity the activity to set
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * Gets the early start time of the activity.
     *
     * @return the early start time
     */
    public double getEs() {
        return es;
    }

    /**
     * Sets the early start time of the activity.
     *
     * @param es the early start time to set
     */
    public void setEs(double es) {
        this.es = es;
    }

    /**
     * Gets the early finish time of the activity.
     *
     * @return the early finish time
     */
    public double getEf() {
        return ef;
    }

    /**
     * Sets the early finish time of the activity.
     *
     * @param ef the early finish time to set
     */
    public void setEf(double ef) {
        this.ef = ef;
    }

    /**
     * Gets the late start time of the activity.
     *
     * @return the late start time
     */
    public double getLs() {
        return ls;
    }

    /**
     * Sets the late start time of the activity.
     *
     * @param ls the late start time to set
     */
    public void setLs(double ls) {
        this.ls = ls;
    }

    /**
     * Gets the late finish time of the activity.
     *
     * @return the late finish time
     */
    public double getLf() {
        return lf;
    }

    /**
     * Sets the late finish time of the activity.
     *
     * @param lf the late finish time to set
     */
    public void setLf(double lf) {
        this.lf = lf;
    }

    /**
     * Gets the slack time of the activity.
     *
     * @return the slack time
     */
    public double getSlack() {
        return slack;
    }

    /**
     * Sets the slack time of the activity.
     *
     * @param slack the slack time to set
     */
    public void setSlack(double slack) {
        this.slack = slack;
    }

    /**
     * Gets the dependency level of the activity.
     *
     * @return the dependency level
     */
    public int getDependencyLevel() {
        return dependencyLevel;
    }

    /**
     * Sets the dependency level of the activity.
     *
     * @param dependencyLevel the dependency level to set
     */
    public void setDependencyLevel(int dependencyLevel) {
        this.dependencyLevel = dependencyLevel;
    }

    /**
     * Checks if this ActivityInfo is equal to another object.
     * Two ActivityInfo objects are equal if their associated activities are equal.
     *
     * @param o the object to compare
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityInfo that = (ActivityInfo) o;
        return Objects.equals(activity, that.activity);
    }

    /**
     * Computes the hash code of this ActivityInfo based on its associated activity.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(activity);
    }

    /**
     * Returns a string representation of this ActivityInfo.
     *
     * @return a string containing the activity and scheduling details
     */
    @Override
    public String toString() {
        return "ActivityInfo{" +
                "activity=" + activity +
                ", es=" + es +
                ", ef=" + ef +
                ", ls=" + ls +
                ", lf=" + lf +
                ", slack=" + slack +
                '}';
    }
}
