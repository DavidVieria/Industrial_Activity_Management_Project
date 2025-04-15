package project.controller;

import project.domain.model.*;
import project.domain.genericDataStructures.NaryTree;
import project.io.DataLoader;
import project.repository.*;

import java.util.ArrayList;
import java.util.List;

public class DataLoaderController {

    private ArticleRepository articleRepository;
    private WorkStationRepository workStationRepository;
    private String articlesCsvPath;
    private String workstationsCsvPath;
    private boolean simulatorDataLoadedSuccessfully;
    private ItemRepository itemRepository;
    private OperationRepository operationRepository;
    private ProductionTreeRepository productionTreeRepository;
    private String itemsCsvPath;
    private String operationsCsvPath;
    private String bomCsvPath;
    private boolean productionTreeDataLoadedSuccessfully;
    private ActivityRepository activityRepository;
    private String activitiesCsvPath;
    private boolean activitiesDataLoadedSuccessfully;
    private OrderRepository orderRepository;
    private String ordersCsvPath;
    private boolean ordersDataLoadedSuccessfully;

    public DataLoaderController() {
        getArticleRepository();
        getWorkStationRepository();
        this.articlesCsvPath = null;
        this.workstationsCsvPath = null;
        this.simulatorDataLoadedSuccessfully = false;
        getItemRepository();
        getOperationRepository();
        getProductionTreeRepository();
        this.itemsCsvPath = null;
        this.operationsCsvPath = null;
        this.bomCsvPath = null;
        this.productionTreeDataLoadedSuccessfully = false;
        getActivityRepository();
        this.activitiesCsvPath = null;
        this.activitiesDataLoadedSuccessfully = false;
        getOrderRepository();
        this.ordersCsvPath = null;
        this.ordersDataLoadedSuccessfully = false;
    }

    public ArticleRepository getArticleRepository() {
        if (articleRepository == null) {
            Repositories repositories = Repositories.getInstance();
            articleRepository = repositories.getArticleRepository();
        }
        return articleRepository;
    }

    public WorkStationRepository getWorkStationRepository() {
        if (workStationRepository == null) {
            Repositories repositories = Repositories.getInstance();
            workStationRepository = repositories.getWorkStationRepository();
        }
        return workStationRepository;
    }

    public ItemRepository getItemRepository() {
        if (itemRepository == null) {
            Repositories repositories = Repositories.getInstance();
            itemRepository = repositories.getItemRepository();
        }
        return itemRepository;
    }

    public OperationRepository getOperationRepository() {
        if (operationRepository == null) {
            Repositories repositories = Repositories.getInstance();
            operationRepository = repositories.getOperationRepository();
        }
        return operationRepository;
    }

    public ProductionTreeRepository getProductionTreeRepository() {
        if (productionTreeRepository == null) {
            Repositories repositories = Repositories.getInstance();
            productionTreeRepository = repositories.getProductionTreeRepository();
        }
        return productionTreeRepository;
    }

    public ActivityRepository getActivityRepository() {
        if (activityRepository == null) {
            Repositories repositories = Repositories.getInstance();
            activityRepository = repositories.getActivityRepository();
        }
        return activityRepository;
    }

    public OrderRepository getOrderRepository() {
        if (orderRepository == null) {
            Repositories repositories = Repositories.getInstance();
            orderRepository = repositories.getOrderRepository();
        }
        return orderRepository;
    }

    public void readSimulatorFiles() {
        getArticleRepository().clearArticles();
        getWorkStationRepository().clearWorkStations();
        try {
            List<Article> articles = DataLoader.loadArticles(articlesCsvPath);
            List<WorkStation> workStations = DataLoader.loadWorkStations(workstationsCsvPath);

            if (articles.isEmpty() || workStations.isEmpty()) {
                simulatorDataLoadedSuccessfully = false;
            } else {
                getArticleRepository().addArticles(articles);
                getWorkStationRepository().addWorkStations(workStations);
                simulatorDataLoadedSuccessfully = true;
            }
        } catch (Exception e) {
            simulatorDataLoadedSuccessfully = false;
        }
    }

    public void readProductionTreeFiles() {
        getItemRepository().clearItems();
        getOperationRepository().clearOperations();
        getProductionTreeRepository().clearTrees();

        try {
            List<Item> items = DataLoader.loadItems(itemsCsvPath);
            List<Operation> operations = DataLoader.loadOperations(operationsCsvPath);

            if (items.isEmpty() || operations.isEmpty()) {
                productionTreeDataLoadedSuccessfully = false;
            } else {
                getItemRepository().addItems(items);
                getOperationRepository().addOperations(operations);
                List<NaryTree<ProductionElement>> trees = DataLoader.loadProductionTree(bomCsvPath, getItemRepository(), getOperationRepository());
                getProductionTreeRepository().addProductionTrees(trees);
                if (!getProductionTreeRepository().isEmpty()) {
                    productionTreeDataLoadedSuccessfully = true;
                }
            }
        } catch (Exception e) {
            productionTreeDataLoadedSuccessfully = false;
        }
    }

    public void readActivitiesFiles() {
        getActivityRepository().clearActivities();
        DataLoader.getPredecessorsActivityMap().clear();

        try {
            DataLoader.loadActivities(activitiesCsvPath);
            List<Activity> activities = DataLoader.getActivityList();

            if (activities.isEmpty()) {
                activitiesDataLoadedSuccessfully = false;
            } else {
                getActivityRepository().addActivities(activities);
                if (!getActivityRepository().isEmpty()) {
                    activitiesDataLoadedSuccessfully = true;
                }
            }
        } catch (Exception e) {
            activitiesDataLoadedSuccessfully = false;
        }
    }

    public void readOrdersFiles() {
        getOrderRepository().clearOrders();

        try {
            List<Order> orders = DataLoader.loadOrders(ordersCsvPath, getItemRepository());

            if (orders.isEmpty()) {
                ordersDataLoadedSuccessfully = false;
            } else {
                getOrderRepository().addOrders(orders);
                ordersDataLoadedSuccessfully = true;
            }

        } catch (Exception e) {
            ordersDataLoadedSuccessfully = false;
        }
    }

    public String getArticlesCsvPath() {
        return articlesCsvPath;
    }

    public String getWorkstationsCsvPath() {
        return workstationsCsvPath;
    }

    public void setWorkstationsCsvPath(String workstationsCsvPath) {
        this.workstationsCsvPath = workstationsCsvPath;
    }

    public void setArticlesCsvPath(String articlesCsvPath) {
        this.articlesCsvPath = articlesCsvPath;
    }

    public boolean isSimulatorDataLoadedSuccessfully() {
        return simulatorDataLoadedSuccessfully;
    }

    public String getItemsCsvPath() {
        return itemsCsvPath;
    }

    public void setItemsCsvPath(String itemsCsvPath) {
        this.itemsCsvPath = itemsCsvPath;
    }

    public String getOperationsCsvPath() {
        return operationsCsvPath;
    }

    public void setOperationsCsvPath(String operationsCsvPath) {
        this.operationsCsvPath = operationsCsvPath;
    }

    public String getBomCsvPath() {
        return bomCsvPath;
    }

    public void setBomCsvPath(String bomCsvPath) {
        this.bomCsvPath = bomCsvPath;
    }

    public boolean isProductionTreeDataLoadedSuccessfully() {
        return productionTreeDataLoadedSuccessfully;
    }

    public String getActivitiesCsvPath() {
        return activitiesCsvPath;
    }

    public void setActivitiesCsvPath(String activitiesCsvPath) {
        this.activitiesCsvPath = activitiesCsvPath;
    }

    public boolean isActivitiesDataLoadedSuccessfully() {
        return activitiesDataLoadedSuccessfully;
    }

    public String getOrdersCsvPath() {
        return ordersCsvPath;
    }

    public void setOrdersCsvPath(String ordersCsvPath) {
        this.ordersCsvPath = ordersCsvPath;
    }

    public boolean isOrdersDataLoadedSuccessfully() {
        return ordersDataLoadedSuccessfully;
    }

}
