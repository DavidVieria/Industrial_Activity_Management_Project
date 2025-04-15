package project.ui.BD;

import project.controller.BD.GenerateInsertsXMLController;

public class GenerateInsertsXMLUI implements Runnable {

    private final GenerateInsertsXMLController controller;

    public GenerateInsertsXMLUI() {
        controller = new GenerateInsertsXMLController();
    }

    public void run() {

        System.out.println("\n\n=== XML ===\n");

        controller.partsXMl();
        controller.operationsXMl();
        controller.booXMl();
        controller.procurementXML();

        controller.getMaterialInserts().forEach(System.out::println);
        System.out.println();
        controller.getManufacturedPartInserts().forEach(System.out::println);
        System.out.println();
        controller.getIntermediateProductInserts().forEach(System.out::println);
        System.out.println();
        controller.getComponentInserts().forEach(System.out::println);
        System.out.println();
        controller.getRawMaterialInserts().forEach(System.out::println);
        System.out.println();
        controller.getProductInserts().forEach(System.out::println);
        System.out.println();
        controller.getBooInserts().forEach(System.out::println);
        System.out.println();
        controller.getOperationTypeInserts().forEach(System.out::println);
        System.out.println();
        controller.getOperationInserts().forEach(System.out::println);
        System.out.println();
        controller.getProductBOOOperationInserts().forEach(System.out::println);
        System.out.println();
//        controller.getOperationTypeWorkstationTypeInserts().forEach(System.out::println);
//        System.out.println();
        controller.getInputsInserts().forEach(System.out::println);
        System.out.println();
        controller.getOutputsInserts().forEach(System.out::println);
//        System.out.println();
//        controller.getSupplierInserts().forEach(System.out::println);
        System.out.println();
        controller.getSupplyOffer().forEach(System.out::println);
    }

}
