package project.ui.data;

import project.controller.DataLoaderController;
import project.ui.menu.MenuItem;
import project.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DataLoaderUI  implements Runnable {
    private final DataLoaderController controller;

    public DataLoaderUI(DataLoaderController controller) {
        this.controller = controller;
    }

    public void run() {
        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("Upload articles files", new ArticleDataUI(controller)));
        options.add(new MenuItem("Upload workstations files", new WorkstationDataUI(controller)));
        options.add(new MenuItem("Upload items files", new ItemDataUI(controller)));
        options.add(new MenuItem("Upload operations files", new OperationDataUI(controller)));
        options.add(new MenuItem("Upload BOO files", new BooDataUI(controller)));
        options.add(new MenuItem("Upload orders files", new OrderDataUI(controller)));
        options.add(new MenuItem("Upload activities files", new ActivityDataUI(controller)));

        int option = 0;
        do {
            option = Utils.showAndSelectIndex(options, "\n\n=== File Upload ===");

            if ((option >= 0) && (option < options.size())) {
                options.get(option).run();
            }
        } while (option != -1);
    }

}