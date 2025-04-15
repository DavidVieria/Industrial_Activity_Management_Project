package project.ui;

import project.controller.CalculateMaterialsQuantityController;
import project.controller.ProductionTreeController;
import project.domain.model.Item;

import java.util.Map;

public class CalculateMaterialsQuantityUI implements Runnable {
    private final CalculateMaterialsQuantityController calculateController;
    private final ProductionTreeController productionTreeController;

    public CalculateMaterialsQuantityUI (ProductionTreeController productionTreeController) {
        this.productionTreeController = productionTreeController;
        this.calculateController = new CalculateMaterialsQuantityController(productionTreeController.getTree());

    }

    public void run() {

        Map<Item, Double> materialsAndQuantities = calculateController.materialsAndQuantity();

        if (materialsAndQuantities.isEmpty()) {
            System.out.println("No material found!");
        } else {
            System.out.println();
            System.out.println("=== Calculation of Material Quantities ===");
            for (Map.Entry<Item, Double> entry : materialsAndQuantities.entrySet()) {
                Item item = entry.getKey();
                double quantity = entry.getValue();
                System.out.printf("- %s: %.2f%n", item.getName(), quantity);
            }
        }

        System.out.println("===========================================");
    }
}
