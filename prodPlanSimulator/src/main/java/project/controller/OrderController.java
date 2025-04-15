package project.controller;

import project.domain.genericDataStructures.AVL;
import project.domain.genericDataStructures.NaryTree;
import project.domain.model.Event;
import project.domain.model.Order;
import project.domain.model.ProductionElement;
import project.domain.service.MergeTrees;
import project.domain.service.OperationHierarchy;
import project.domain.service.ProcessOrders;
import project.repository.*;

import java.util.List;
import java.util.Map;

public class OrderController {

    private OrderRepository orderRepository;
    private ProductionTreeRepository productionTreeRepository;
    private WorkStationRepository workStationRepository;
    private ProcessOrders processOrders;

    public OrderController() {
        getOrderRepository();
        getProductionTreeRepository();
        getWorkstationRepository();
        processOrders = new ProcessOrders();
    }

    public void processOrders () {
        MergeTrees.merge(productionTreeRepository.getAllTrees());
        processOrders.processOrders(orderRepository, productionTreeRepository, workStationRepository);
    }

    public Map<Order, NaryTree<ProductionElement>> getOrderTreeMap() {
        return processOrders.getOrderTreeMap();
    }

    public Map<Order, AVL<OperationHierarchy>> getOrderAVLMap() {
        return processOrders.getOrderAVLMap();
    }

    public List<Event> getEvents() {
        return processOrders.getEventList();
    }

    public OrderRepository getOrderRepository() {
        if (orderRepository == null) {
            Repositories repositories = Repositories.getInstance();
            orderRepository = repositories.getOrderRepository();
        }
        return orderRepository;
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
