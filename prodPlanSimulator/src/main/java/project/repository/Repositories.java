package project.repository;


/**
 * This class manages singleton instances of repositories.
 */
public class Repositories {

    /** The singleton instance of Repositories. */
    private static Repositories instance;

    private ArticleRepository articleRepository;

    private WorkStationRepository workStationRepository;

    private EventRepository eventRepository;

    private ItemRepository itemRepository;

    private OperationRepository operationRepository;

    private ProductionTreeRepository productionTreeRepository;

    private ActivityRepository activityRepository;

    private OrderRepository orderRepository;

    /**
     * Constructs a new Repositories object, initializing all repositories.
     */
    private Repositories() {
        articleRepository = new ArticleRepository();
        workStationRepository = new WorkStationRepository();
        eventRepository = new EventRepository();
        itemRepository = new ItemRepository();
        operationRepository = new OperationRepository();
        productionTreeRepository = new ProductionTreeRepository();
        activityRepository = new ActivityRepository();
        orderRepository = new OrderRepository();
    }

    /**
     * Returns the singleton instance of Repositories, creating it if necessary.
     * @return The singleton instance of Repositories.
     */
    public static Repositories getInstance() {
        if (instance == null) {
            synchronized (Repositories.class) {
                if (instance == null) {
                    instance = new Repositories();
                }
            }
        }
        return instance;
    }

    public ArticleRepository getArticleRepository() {
        return articleRepository;
    }

    public WorkStationRepository getWorkStationRepository() {
        return workStationRepository;
    }

    public EventRepository getEventRepository() {
        return eventRepository;
    }

    public ItemRepository getItemRepository() { return itemRepository; }

    public OperationRepository getOperationRepository() { return operationRepository; }

    public ProductionTreeRepository getProductionTreeRepository() { return productionTreeRepository; }

    public ActivityRepository getActivityRepository() { return activityRepository; }

    public OrderRepository getOrderRepository() { return orderRepository; }
}

