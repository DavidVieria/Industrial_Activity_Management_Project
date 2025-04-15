package project.ui.menu;

import project.controller.DataLoaderController;
import project.ui.*;
import project.ui.BD.*;
import project.ui.data.DataLoaderUI;
import project.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainMenuUI implements Runnable {
    private final DataLoaderController controller;

    public MainMenuUI() {
        controller = new DataLoaderController();
    }

    public void run() {
        List<MenuItem> options = new ArrayList<MenuItem>();
        options.add(new MenuItem("Upload CSV files", new DataLoaderUI(controller)));
        options.add(new MenuItem("Generate CSV files", new GenerateCSVFilesUI()));
        options.add(new MenuItem("Generate Inserts XML Files", new GenerateInsertsXMLUI()));
        options.add(new MenuItem("Database Features", new DatabaseFeaturesUI()));
        options.add(new MenuItem("Product Structure (BOM & BOO)", new StructureProductUI()));
        options.add(new MenuItem("Simulator without item priority", new SimulatorWithoutPriorityUI(controller)));
        options.add(new MenuItem("Simulator with item priority", new SimulatorWithPriorityUI(controller)));
        options.add(new MenuItem("Production Tree", new ProductionTreeUI(controller)));
        options.add(new MenuItem("Process Orders", new OrderUI(controller)));
        options.add(new MenuItem("Average Production Time", new ProductionTimeUI(controller)));
        options.add(new MenuItem("Export Production Planner Simulator", new ExportProductionSimulatorUI(controller)));
        options.add(new MenuItem("Project Activities Graph", new ActivityGraphUI(controller)));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n--- MAIN MENU --------------------------");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }

}
