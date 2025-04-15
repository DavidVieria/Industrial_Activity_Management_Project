package project.ui.BD;

import project.controller.BD.ReservePartsForOrderController;

import java.util.Scanner;

public class ReservePartsForOrderUI implements Runnable {

    private final ReservePartsForOrderController controller;

    public ReservePartsForOrderUI() {
        this.controller = new ReservePartsForOrderController();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the order ID: ");
        int orderID = scanner.nextInt();

        String result = controller.reserveRequiredPartsForOrder(orderID);
        System.out.println("\n" + result);
    }
}