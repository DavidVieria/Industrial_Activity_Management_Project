package project.domain.model;

import java.util.Objects;

public class Activity {

    private String actID;
    private String description;
    private double duration;
    private String durationUnit;
    private double cost;
    private String costUnit;

    public Activity(String actID, String description, double duration, String durationUnit, double cost, String costUnit) {
        this.actID = actID;
        this.description = description;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.cost = cost;
        this.costUnit = costUnit;
    }

    public Activity(String actID, String description) {
        this.actID = actID;
        this.description = description;
        this.duration = 0;
        this.durationUnit = "";
        this.cost = 0;
        this.costUnit = "";
    }

    public String getActID() {
        return actID;
    }

    public void setActID(String actID) {
        this.actID = actID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getCostUnit() {
        return costUnit;
    }

    public void setCostUnit(String costUnit) {
        this.costUnit = costUnit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(actID, activity.actID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actID);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "actID='" + actID + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", durationUnit='" + durationUnit + '\'' +
                ", cost=" + cost +
                ", costUnit='" + costUnit + '\'' +
                '}';
    }
}
