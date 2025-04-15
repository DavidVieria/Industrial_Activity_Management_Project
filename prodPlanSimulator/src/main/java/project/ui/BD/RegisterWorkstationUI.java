package project.ui.BD;

import project.controller.BD.RegisterWorkstationController;

import java.util.InputMismatchException;
import java.util.Scanner;

public class RegisterWorkstationUI implements Runnable {
    private final RegisterWorkstationController controller;

    public RegisterWorkstationUI() {
        this.controller = new RegisterWorkstationController();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int workstationId = -1;

        while (true) {
            try {
                System.out.print("Enter Workstation ID: ");
                workstationId = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer for Workstation ID.");
                scanner.nextLine();
            }
        }

        System.out.print("Enter Workstation Type ID: ");
        String workstationTypeId = scanner.nextLine();

        System.out.print("Enter Workstation Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Workstation Description: ");
        String description = scanner.nextLine();

        String result = controller.registerWorkstation(workstationId, workstationTypeId, name, description);

        System.out.println();
        System.out.println(result);
    }
}
