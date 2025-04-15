package project.controller;

import project.domain.model.Event;
import project.domain.service.ExportProductionSimulator;
import project.domain.service.MergeTrees;
import project.repository.ProductionTreeRepository;
import project.repository.Repositories;
import project.repository.WorkStationRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExportProductionSimulatorController {

    private ProductionTreeRepository productionTreeRepository;
    private WorkStationRepository workStationRepository;
    private ExportProductionSimulator exportProductionSimulator;

    public ExportProductionSimulatorController() {
        getProductionTreeRepository();
        getWorkstationRepository();
    }

    public void function (String itemID) throws IOException {
        exportProductionSimulator = new ExportProductionSimulator(productionTreeRepository, itemID);
        MergeTrees.merge(productionTreeRepository.getAllTrees());
        exportProductionSimulator.productionSimulator(itemID, productionTreeRepository, workStationRepository);
        exportProductionSimulator.export("files\\instructionsBD.txt");
    }

    public List<Event> getEvents() {
        return exportProductionSimulator.getEventList();
    }

    public Map<String, Integer> getWorkstationsNumbers () {
        return exportProductionSimulator.getWorkstations();
    }

    public Map<String, Integer> getOperationsNumbers () {
        return exportProductionSimulator.getOperations();
    }

    public ProductionTreeRepository getProductionTreeRepository() {
        if (productionTreeRepository == null) {
            Repositories repositories = Repositories.getInstance();
            productionTreeRepository = repositories.getProductionTreeRepository();
        }
        return productionTreeRepository;
    }

    public WorkStationRepository getWorkstationRepository() {
        if (workStationRepository == null) {
            Repositories repositories = Repositories.getInstance();
            workStationRepository = repositories.getWorkStationRepository();
        }
        return workStationRepository;
    }
}
