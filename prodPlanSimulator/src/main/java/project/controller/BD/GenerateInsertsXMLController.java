package project.controller.BD;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class GenerateInsertsXMLController {

    private List<String> materialInserts;
    private List<String> manufacturedPartInserts;
    private List<String> intermediateProductInserts;
    private List<String> componentInserts;
    private List<String> rawMaterialInserts;
    private List<String> productInserts;
    private List<String> operationTypeInserts;
    private List<String> operationTypeWorkstationTypeInserts;
    private List<String> booInserts;
    private List<String> productBOOOperationInserts;
    private List<String> operationInserts;
    private List<String> inputsInserts;
    private List<String> outputsInserts;
    private List<String> supplierInserts;
    private List<String> supplyOffer;

    public GenerateInsertsXMLController() {
        materialInserts = new ArrayList<>();
        manufacturedPartInserts = new ArrayList<>();
        intermediateProductInserts = new ArrayList<>();
        componentInserts = new ArrayList<>();
        rawMaterialInserts = new ArrayList<>();
        productInserts = new ArrayList<>();
        operationTypeInserts = new ArrayList<>();
        operationTypeWorkstationTypeInserts = new ArrayList<>();
        booInserts = new ArrayList<>();
        productBOOOperationInserts = new ArrayList<>();
        operationInserts = new ArrayList<>();
        inputsInserts = new ArrayList<>();
        outputsInserts = new ArrayList<>();
        supplierInserts = new ArrayList<>();
        supplyOffer = new ArrayList<>();
    }

    public void partsXMl() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("documentation/legacySystem/Dataset_S3_V04/Dataset_S3_parts_V01.xml");

            NodeList partList = document.getElementsByTagName("part");

            for (int i = 0; i < partList.getLength(); i++) {
                Node partNode = partList.item(i);
                if (partNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element partElement = (Element) partNode;

                    String partNumber = partElement.getElementsByTagName("part_number").item(0).getTextContent().replace("\"", "");
                    String description = partElement.getElementsByTagName("description").item(0).getTextContent().replace("\"", "");
                    String partType = partElement.getElementsByTagName("part_type").item(0).getTextContent().replace("\"", "");

                    String unit = "1";
                    if (partType.equals("Product")) {
                        String name = partElement.getElementsByTagName("name").item(0).getTextContent().replace("\"", "");
                        String family = partElement.getElementsByTagName("family").item(0).getTextContent();

                        materialInserts.add(String.format("INSERT INTO Part (PartID, Description) VALUES ('%s', '%s');", partNumber, description));
                        productInserts.add(String.format("INSERT INTO Product (ProductID, ProductFamilyID, ProductName) VALUES ('%s', %s, '%s');", partNumber, family, name));
                        manufacturedPartInserts.add(String.format("INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('%s');", partNumber));
                    } else {
                        materialInserts.add(String.format("INSERT INTO Part (PartID, Description) VALUES ('%s', '%s');", partNumber, description));

                        switch (partType) {
                            case "Intermediate Product":
                                intermediateProductInserts.add(String.format("INSERT INTO IntermediateProduct (IntermediateID) VALUES ('%s');", partNumber));
                                manufacturedPartInserts.add(String.format("INSERT INTO ManufacturedPart (ManufacturedPartID) VALUES ('%s');", partNumber));
                                break;
                            case "Component":
                                componentInserts.add(String.format("INSERT INTO Component (ComponentID) VALUES ('%s');", partNumber));
                                break;
                            case "Raw Material":
                                rawMaterialInserts.add(String.format("INSERT INTO RawMaterial (RawMaterialID, UnitID) VALUES ('%s', %s);", partNumber, unit));
                                break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void operationsXMl() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("documentation/legacySystem/Dataset_S3_V04/Dataset_S3_parts_V01.xml");

            NodeList operationTypeList = document.getElementsByTagName("operation_type");

            for (int i = 0; i < operationTypeList.getLength(); i++) {
                Node operationNode = operationTypeList.item(i);
                if (operationNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element operationElement = (Element) operationNode;

                    String operationID = operationElement.getAttribute("id");
                    String description = operationElement.getElementsByTagName("operation_desc").item(0).getTextContent().replace("\"", "");

                    operationTypeInserts.add(String.format("INSERT INTO OperationType (OperationTypeID, OperationTypeName) VALUES (%s, '%s');", operationID, description));

                    NodeList workstationList = operationElement.getElementsByTagName("workstation_type");
                    for (int j = 0; j < workstationList.getLength(); j++) {
                        String workstationID = workstationList.item(j).getTextContent().replace("\"", "");
                        operationTypeWorkstationTypeInserts.add(String.format("INSERT INTO OperationTypeWorkstationType (OperationTypeID, WorkstationTypeID) VALUES (%s, '%s');", operationID, workstationID));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void booXMl() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("documentation/legacySystem/Dataset_S3_V04/Dataset_S3_boo_V03.xml");

            NodeList booList = document.getElementsByTagName("boo");

            for (int i = 0; i < booList.getLength(); i++) {
                int operationOrder = 1;
                Node booNode = booList.item(i);
                if (booNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element booElement = (Element) booNode;

                    String productID = booElement.getAttribute("id");

                    booInserts.add(String.format("INSERT INTO BOO (ProductID) VALUES ('%s');", productID));

                    NodeList operationList = booElement.getElementsByTagName("operation");
                    for (int j = 0; j < operationList.getLength(); j++) {
                        Node operationNode = operationList.item(j);
                        if (operationNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element operationElement = (Element) operationNode;

                            String operationID = operationElement.getAttribute("id");
                            String operationTypeID = operationElement.getElementsByTagName("optype_id").item(0).getTextContent();
                            String eet = operationElement.getElementsByTagName("eet").item(0).getTextContent();
                            String nextOperation = operationElement.getElementsByTagName("next_op").item(0).getTextContent();

                            productBOOOperationInserts.add(String.format("INSERT INTO ProductBOOOperation (ProductID, OperationID, OperationNumber, NextOperation) VALUES ('%s', %s, %s, '%s');",
                                    productID, operationID, operationOrder, nextOperation));

                            operationInserts.add(String.format("INSERT INTO Operation (OperationID, OperationTypeID, ExpectedExecutionTime) VALUES (%s, %s, %s);", operationID, operationTypeID, eet));

                            NodeList inputList = operationElement.getElementsByTagName("input");
                            for (int k = 0; k < inputList.getLength(); k++) {
                                Node inputNode = inputList.item(k);
                                if (inputNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element inputElement = (Element) inputNode;

                                    String materialID = inputElement.getElementsByTagName("part").item(0).getTextContent();
                                    String quantity = inputElement.getElementsByTagName("quantity").item(0).getTextContent();

                                    inputsInserts.add(String.format("INSERT INTO OperationInput (OperationID, PartID, Quantity) VALUES (%s, '%s', %s);", operationID, materialID, quantity));
                                }
                            }

                            NodeList outputList = operationElement.getElementsByTagName("output");
                            for (int k = 0; k < outputList.getLength(); k++) {
                                Node outputNode = outputList.item(k);
                                if (outputNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element outputElement = (Element) outputNode;

                                    String materialID = outputElement.getElementsByTagName("part").item(0).getTextContent();
                                    String quantity = outputElement.getElementsByTagName("quantity").item(0).getTextContent();

                                    outputsInserts.add(String.format("INSERT INTO OperationOutput (OperationID, ManufacturedPartID, Quantity) VALUES (%s, '%s', %s);", operationID, materialID, quantity));
                                }
                            }
                        }
                        operationOrder++;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void procurementXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("documentation/legacySystem/Dataset_S3_V04/Dataset_S3_procurement_V02.xml");

            doc.getDocumentElement().normalize();

            NodeList supplierNodes = doc.getElementsByTagName("supplier");

            for (int i = 0; i < supplierNodes.getLength(); i++) {
                Node supplierNode = supplierNodes.item(i);
                if (supplierNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element supplierElement = (Element) supplierNode;

                    String supplierId = supplierElement.getAttribute("id");
                    supplierInserts.add(String.format("INSERT INTO Supplier (SupplierID) VALUES (%s);", supplierId));

                    NodeList partNodes = supplierElement.getElementsByTagName("part");

                    for (int j = 0; j < partNodes.getLength(); j++) {
                        Node partNode = partNodes.item(j);
                        if (partNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element partElement = (Element) partNode;

                            String partId = partElement.getAttribute("id");
                            NodeList offerNodes = partElement.getElementsByTagName("offer");

                            for (int k = 0; k < offerNodes.getLength(); k++) {
                                Node offerNode = offerNodes.item(k);
                                if (offerNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element offerElement = (Element) offerNode;

                                    String startDate = offerElement.getElementsByTagName("start_date").item(0).getTextContent();
                                    String endDate = offerElement.getElementsByTagName("end_date").item(0).getTextContent();
                                    String minQuantity = offerElement.getElementsByTagName("min_quantity").item(0).getTextContent();
                                    String price = offerElement.getElementsByTagName("price").item(0).getTextContent();

                                    String formattedStartDate = String.format("TO_DATE('%s', 'DD/MM/YYYY')", convertDateFormat(startDate));
                                    String formattedEndDate = endDate.equalsIgnoreCase("NULL")
                                            ? "NULL"
                                            : String.format("TO_DATE('%s', 'DD/MM/YYYY')", convertDateFormat(endDate));

                                    supplyOffer.add(String.format(
                                            "INSERT INTO SupplyOffer (SupplierID, StockPartID, StartDate, EndDate, MinimumQuantity, UnitCost) VALUES (%s, '%s', %s, %s, %s, %s);",
                                            supplierId, partId, formattedStartDate, formattedEndDate, minQuantity, price));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertDateFormat(String date) {
        // YYYY/MM/DD para DD/MM/YYYY
        String[] parts = date.split("/");
        return String.format("%s/%s/%s", parts[2], parts[1], parts[0]);
    }

    public List<String> getMaterialInserts() {
        return materialInserts;
    }

    public List<String> getIntermediateProductInserts() {
        return intermediateProductInserts;
    }

    public List<String> getComponentInserts() {
        return componentInserts;
    }

    public List<String> getRawMaterialInserts() {
        return rawMaterialInserts;
    }

    public List<String> getProductInserts() {
        return productInserts;
    }

    public List<String> getOperationTypeInserts() {
        return operationTypeInserts;
    }

    public List<String> getOperationTypeWorkstationTypeInserts() {
        return operationTypeWorkstationTypeInserts;
    }

    public List<String> getBooInserts() {
        return booInserts;
    }

    public List<String> getProductBOOOperationInserts() {
        return productBOOOperationInserts;
    }

    public List<String> getOperationInserts() {
        return operationInserts;
    }

    public List<String> getInputsInserts() {
        return inputsInserts;
    }

    public List<String> getOutputsInserts() {
        return outputsInserts;
    }

    public List<String> getSupplierInserts() {
        return supplierInserts;
    }

    public List<String> getSupplyOffer() {
        return supplyOffer;
    }

    public List<String> getManufacturedPartInserts() {
        return manufacturedPartInserts;
    }
}
