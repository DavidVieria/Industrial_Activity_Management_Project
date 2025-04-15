package project.ui.BD;

import project.ui.menu.MenuItem;
import project.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseFeaturesUI implements Runnable {


    public DatabaseFeaturesUI() {
    }

    public void run() {

        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("List of parts used in a product", new ListPartsProductUI()));
        options.add(new MenuItem("List of operations involved in a product", new ListOperationsProductUI()));
        options.add(new MenuItem("Which product uses all types of machines available", new ProductMachineUsageUI()));
        options.add(new MenuItem("Register a Workstation", new RegisterWorkstationUI()));
        options.add(new MenuItem("Register a Product", new RegisterProductUI()));
        options.add(new MenuItem("Register an Order", new RegisterOrderUI()));
        options.add(new MenuItem("Deactivate a Customer", new DeactivateCustomerUI()));
        options.add(new MenuItem("Insert/Update an Operation", new TriggerOperationUI()));
        options.add(new MenuItem("Insert/Update an Operation Input", new TriggerPreventCyclesUI()));
        options.add(new MenuItem("List of operations necessary for the production plan module", new ProductionPlanOperationsUI()));
        options.add(new MenuItem("Check if materials are in stock for an order", new CheckMaterialsInStockUI()));
        options.add(new MenuItem("Reserve required parts for an order", new ReservePartsForOrderUI()));
        options.add(new MenuItem("List reserved parts", new ListReservedPartsUI()));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n=== Database Features Menu ===");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }


}
