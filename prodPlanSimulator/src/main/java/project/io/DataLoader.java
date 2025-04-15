package project.io;

import project.domain.enums.Priority;
import project.domain.model.*;
import project.domain.genericDataStructures.NaryTree;
import project.repository.ItemRepository;
import project.repository.OperationRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DataLoader {

    private static List<Article> articles = new ArrayList<>();
    private static List<WorkStation> workStations = new ArrayList<>();
    private static List<Item> items = new ArrayList<>();
    private static List<Operation> operations = new ArrayList<>();
    private static List<ProductionElement> productionElements = new ArrayList<>();
    private static List<NaryTree<ProductionElement>> trees = new ArrayList<>();
    private static List<Activity> activities =  new ArrayList<>();
    private static Map<Activity, List<String>> predecessorsActivityMap = new LinkedHashMap<>();
    private static List<Order> orders = new ArrayList<>();

    public static List<Article> loadArticles(String articlesFile) {
        articles = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(articlesFile))) {
            String line;
            br.readLine();

            int index = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");

                if (values.length < 3) {
                    System.err.println("Skipping row with insufficient data");
                    continue;
                }

                String idItem = values[0];
                Priority priority;

                try {
                    priority = Priority.valueOf(values[1].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid priority: " + values[1] + ". Skipping item.");
                    continue;
                }

                List<String> operations = new ArrayList<>();
                for (int i = 2; i < values.length; i++) {
                    operations.add(values[i]);
                }

                Article article = new Article(idItem, priority, operations, index);
                articles.add(article);
                index++;
            }

        } catch (IOException e) {
            System.err.println("Error loading items: " + e.getMessage());
        }
        return articles;
    }

    public static List<WorkStation> loadWorkStations(String workstationsFile) {
        workStations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(workstationsFile))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");

                if (values.length < 3) {
                    System.err.println("Skipping row with insufficient data");
                    continue;
                }

                try {
                String workStationID = values[0];
                String operation = values[1];
                int time = Integer.parseInt(values[2]);
                WorkStation workStation = new WorkStation(workStationID, operation, time);
                workStations.add(workStation);
                } catch (NumberFormatException e) {
                    System.err.println("Skipping row with invalid time number");
                    continue;
                }
            }

        } catch (IOException e) {
            System.err.println("Error loading WorkStations: " + e.getMessage());
        }
        return workStations;
    }

    public static List<Item> loadItems(String itemsFile) {
        items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(itemsFile))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");

                if (values.length < 2) {
                    System.err.println("Skipping row with insufficient data");
                    continue;
                }

                String id = values[0];
                String name = values[1];
                Item item = new Item(id, name);
                items.add(item);
            }

        } catch (IOException e) {
            System.err.println("Error loading Items: " + e.getMessage());
        }
        return items;
    }

    public static List<Operation> loadOperations(String operationsFile) {
        operations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(operationsFile))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");

                if (values.length < 2) {
                    System.err.println("Skipping row with insufficient data");
                    continue;
                }

                String id = values[0];
                String name = values[1];
                Operation operation = new Operation(id, name);
                operations.add(operation);
            }

        } catch (IOException e) {
            System.err.println("Error loading Operations: " + e.getMessage());
        }
        return operations;
    }

    public static List<ProductionElement> loadProductionElement(String productionNodesFile, ItemRepository itemRepository, OperationRepository operationRepository) {
        productionElements = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(productionNodesFile))) {
            List<String> lines = new ArrayList<>();
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            for (int i = lines.size() - 1; i >= 0; i--) {
                String[] values = lines.get(i).split(";");

                if (values.length < 4) {
                    System.err.println("Skipping row with insufficient data");
                    continue;
                }

                String idOperation = values[0];
                String idItemMain = values[1];
                int quantity = Integer.parseInt(values[2].replace(",", "."));
                Item itemMain = itemRepository.getItemById(idItemMain);
                Operation operation = operationRepository.getOperationById(idOperation);
                ProductionElement element = new ProductionElement(itemMain, operation, quantity);
                productionElements.add(element);
            }

        } catch (IOException e) {
            System.err.println("Error loading BOM: " + e.getMessage());
        }

        return productionElements;
    }

    public static List<NaryTree<ProductionElement>> loadProductionTree(String productionNodesFile, ItemRepository itemRepository, OperationRepository operationRepository) {
        trees = new ArrayList<>();
        List<ProductionElement> productionElements = loadProductionElement(productionNodesFile, itemRepository, operationRepository);

        try (BufferedReader br = new BufferedReader(new FileReader(productionNodesFile))) {
            List<String> lines = new ArrayList<>();
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            int j = 0;

            for (int i = lines.size() - 1; i >= 0; i--) {
                line = lines.get(i);
                ProductionElement element = productionElements.get(j);
                NaryTree<ProductionElement> tree = new NaryTree<>(element);
                element.setProductionTree(tree);

                String[] values = line.split(";");
                int index = 4;

                while (!values[index].equals(")")) {
                    if (!values[index].isEmpty()) {
                        for (ProductionElement productElement : productionElements) {
                            if (values[index].equals(productElement.getOperation().getId())) {
                                double quantity = Double.parseDouble(values[index + 1].replace(",", "."));
                                productElement.setQuantity(quantity);
                                tree.getRoot().addChild(productElement);
                                break;
                            }
                        }
                    }
                    index += 2;
                }

                index += 2;
                while (!values[index].equals(")")) {
                    if (!values[index].isEmpty()) {
                        String subItemId = values[index];
                        double quantity = Double.parseDouble(values[index + 1].replace(",", "."));
                        Item subItem = itemRepository.getItemById(subItemId);
                        ProductionElement material = new ProductionElement(subItem, quantity);
                        tree.getRoot().addChild(material);
                    }
                    index += 2;
                }

                trees.add(tree);
                j++;
            }

        } catch (IOException e) {
            System.err.println("Error loading BOM: " + e.getMessage());
        }

        return trees;
    }

    public static void loadActivities(String activitiesFile) {
        activities.clear();
        predecessorsActivityMap.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(activitiesFile))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                if (values.length < 5) {
                    System.err.println("Skipping row with insufficient data");
                    continue;
                }

                String id = values[0];
                String description = values[1];
                double duration = Double.parseDouble(values[2]);
                String durationUnit = values[3];
                double cost = Double.parseDouble(values[4]);

                List<String> predecessors = new ArrayList<>();
                if (values.length > 5 && !values[5].isEmpty()) {
                    String[] predecessorArray = values[5].replace("\"", "").split(",");
                    for (String predecessor : predecessorArray) {
                        predecessors.add(predecessor.trim());
                    }
                }

                Activity activity = new Activity(id, description, duration, durationUnit, cost, "USD");
                activities.add(activity);
                predecessorsActivityMap.put(activity, predecessors);
            }

        } catch (IOException e) {
            System.err.println("Error loading activities: " + e.getMessage());
        }
    }

    public static List<Activity> getActivityList() {
        return activities;
    }

    public static Map<Activity, List<String>> getPredecessorsActivityMap() {
        return predecessorsActivityMap;
    }

    public static List<Order> loadOrders(String orderFile, ItemRepository itemRepository) {
        orders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(orderFile))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");

                if (values.length < 3) {
                    System.err.println("Skipping row with insufficient data");
                    continue;
                }

                String orderID = values[0];

                String itemID = values[1];
                Item item = itemRepository.getItemById(itemID);
                if (item == null) {
                    System.err.println("Invalid item id: " + values[1] + ". Skipping order.");
                    continue;
                }

                Priority priority;
                try {
                    priority = Priority.valueOf(values[2].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid priority: " + values[2] + ". Skipping item.");
                    continue;
                }

                double quantity = Double.parseDouble(values[3]);

                Order order = new Order(orderID, item, priority, quantity);
                orders.add(order);
            }

        } catch (IOException e) {
            System.err.println("Error loading Orders: " + e.getMessage());
        }

        return orders;
    }
}
