package project.ui.menu;

public class MenuItem {
    private final String description;
    private final Runnable ui;

    public MenuItem(String description, Runnable ui) {
        this.description = description;
        this.ui = ui;
    }

    public void run() {
        this.ui.run();
    }

    public boolean hasDescription(String description) {
        return this.description.equals(description);
    }

    public String toString() {
        return this.description;
    }
}
