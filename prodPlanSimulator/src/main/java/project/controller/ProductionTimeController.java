package project.controller;

import project.domain.model.Event;
import project.domain.service.AverageProductionTime;
import project.domain.service.MergeTrees;
import project.repository.ProductionTreeRepository;
import project.repository.Repositories;
import project.repository.WorkStationRepository;

import java.util.List;

public class ProductionTimeController {

    private ProductionTreeRepository productionTreeRepository;
    private WorkStationRepository workStationRepository;
    private AverageProductionTime averageProductionTime;

    public ProductionTimeController() {
        getProductionTreeRepository();
        getWorkstationRepository();
    }

    public void function (String itemID) {
        averageProductionTime = new AverageProductionTime(productionTreeRepository, itemID);
        MergeTrees.merge(productionTreeRepository.getAllTrees());
        averageProductionTime.function(itemID, productionTreeRepository, workStationRepository);
    }

    public int getTotalProductionTime() {
        return averageProductionTime.getTotalProductionTime();
    }

    public List<Event> getEvents() {
        return averageProductionTime.getEventList();
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
