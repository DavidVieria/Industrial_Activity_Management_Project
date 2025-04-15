package project.repository;

import project.domain.model.WorkStation;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a repository for storing and managing workstations.
 * This class provides methods to add, retrieve, and manage workstations in a list.
 */
public class WorkStationRepository {

    private List<WorkStation> workStations;

    public WorkStationRepository(List<WorkStation> workStations) {
        this.workStations = workStations;
    }

    /**
     * Constructs a WorkStationRepository and initializes the list of workstations.
     */
    public WorkStationRepository() {
        this.workStations = new ArrayList<>();
    }

    /**
     * Adds a new workstation to the repository.
     *
     * @param workStation the workstation to be added
     */
    public void addWorkStation(WorkStation workStation) {
        workStations.add(workStation);
    }

    public void addWorkStations(List<WorkStation> workStations) {
        this.workStations.addAll(workStations);
    }

    /**
     * Retrieves a workstation by its ID.
     *
     * @param workStationID the ID of the workstation to retrieve
     * @return the workstation with the specified ID, or null if not found
     */
    public WorkStation getWorkStationById(String workStationID) {
        for (WorkStation workStation : workStations) {
            if (workStation.getWorkStationID().equals(workStationID)) {
                return workStation;
            }
        }
        return null;
    }

    /**
     * Retrieves all workstations in the repository.
     *
     * @return a list of all workstations
     */
    public List<WorkStation> getAllWorkStations() {
        return new ArrayList<>(workStations);
    }

    /**
     * Removes a specified workstation from the repository.
     *
     * @param workStation the workstation to be removed
     */
    public void removeWorkStation(WorkStation workStation) {
        workStations.remove(workStation);
    }

    /**
     * Clears all workstations from the repository.
     */
    public void clearWorkStations() {
        workStations.clear();
    }

    /**
     * Gets the count of workstations in the repository.
     *
     * @return the number of workstations in the repository
     */
    public int getWorkStationCount() {
        return workStations.size();
    }
}