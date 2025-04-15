package project.ui.BD;

import project.controller.BD.ListReservedPartsController;

public class ListReservedPartsUI implements Runnable {

    private final ListReservedPartsController controller;

    public ListReservedPartsUI() {
        this.controller = new ListReservedPartsController();
    }

    @Override
    public void run() {
        controller.listReservedParts();
    }
}