package project.domain.model;

import java.util.Objects;

/**
 * Represents a workstation in the production system. A workstation is
 * responsible for performing specific operations and has a defined
 * processing time and availability status.
 */
public class WorkStation {

    private String workStationID;
    private String operation;
    private int time;
    private boolean available;

    /**
     * Constructs a WorkStation with the specified ID, operation, and time.
     *
     * @param workStationID the unique identifier of the workstation
     * @param operation     the operation performed by the workstation
     * @param time         the time required to perform the operation
     */
    public WorkStation(String workStationID, String operation, int time) {
        this.workStationID = workStationID;
        this.operation = operation;
        this.time = time;
        this.available = true; // By default, the workstation is available
    }

    /**
     * Gets the unique identifier of the workstation.
     *
     * @return the workstation ID
     */
    public String getWorkStationID() {
        return workStationID;
    }

    /**
     * Sets the unique identifier of the workstation.
     *
     * @param workStationID the new workstation ID
     */
    public void setWorkStationID(String workStationID) {
        this.workStationID = workStationID;
    }

    /**
     * Gets the operation performed by the workstation.
     *
     * @return the operation of the workstation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Sets the operation performed by the workstation.
     *
     * @param operation the new operation of the workstation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * Gets the time required to perform the operation at the workstation.
     *
     * @return the time for the operation
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets the time required to perform the operation at the workstation.
     *
     * @param time the new time for the operation
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Checks if the workstation is available.
     *
     * @return true if the workstation is available, false otherwise
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Sets the availability status of the workstation.
     *
     * @param available the new availability status
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkStation workStation = (WorkStation) o;
        return time == workStation.time &&
                available == workStation.available &&
                Objects.equals(workStationID, workStation.workStationID) &&
                Objects.equals(operation, workStation.operation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workStationID, operation, time, available);
    }

    @Override
    public String toString() {
        return "WorkStation{" +
                "ID='" + workStationID + '\'' +
                ", operation='" + operation + '\'' +
                ", time=" + time +
                ", available=" + available +
                '}';
    }
}