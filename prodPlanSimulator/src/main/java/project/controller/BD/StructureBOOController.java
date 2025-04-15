package project.controller.BD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StructureBOOController {

    private static final String STRUCTURE_BOO_DOT = "documentation\\structure\\BOO.dot";
    private static final String STRUCTURE_BOO_PNG = "documentation\\structure\\BOO.png";
    private static final String BOO_FILE = "files\\booBD.csv";

    public void execute() throws IOException {

        generate();
        renderDotFile();

    }

    private void generate() throws IOException {
        try (FileWriter writer = new FileWriter(STRUCTURE_BOO_DOT)) {
            writer.write("digraph BOO {\n");

            writer.write("    node [style=filled, fontname=Arial];\n");

            String colorFirstItemMain = "yellow";
            String colorOtherItemMain = "lightgreen";
            String colorOperation = "lightblue";
            String colorSubItem = "orange";

            try (BufferedReader br = new BufferedReader(new FileReader(BOO_FILE))) {
                String line = br.readLine();

                boolean first = true;

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(";");

                    try {
                        String idOperationMain = values[0];
                        String idItemMain = values[1];
                        int quantOpIt = Integer.parseInt(values[2].replace(",", "."));

                        if (first) {
                            writer.write(String.format("    \"%s\" [shape=circle, color=%s];\n", idItemMain, colorFirstItemMain));
                            first = false;
                        } else {
                            writer.write(String.format("    \"%s\" [shape=circle, color=%s];\n", idItemMain, colorOtherItemMain));
                        }

                        writer.write(String.format("    \"%s\" [shape=box, color=%s];\n", idOperationMain, colorOperation));

                        writer.write(String.format("    \"%s\" -> \"%s\" [label=\"%d\"];\n", idOperationMain, idItemMain, quantOpIt));

                        int index = 4;

                        while (!values[index].equals(")")) {
                            if (!values[index].isEmpty()) {
                                String operation = values[index];
                                double quantityOp = Double.parseDouble(values[index + 1].replace(",", "."));

                                writer.write(String.format("    \"%s\" [shape=box, color=%s];\n", operation, colorOperation));
                                writer.write(String.format("    \"%s\" -> \"%s\" [label=\"%.2f\"];\n", operation, idOperationMain, quantityOp));
                            }
                            index += 2;
                        }

                        index += 2;

                        while (!values[index].equals(")")) {
                            if (!values[index].isEmpty()) {
                                String subItemId = values[index];
                                double quantityIt = Double.parseDouble(values[index + 1].replace(",", "."));

                                writer.write(String.format("    \"%s\" [shape=diamond, color=%s];\n", subItemId, colorSubItem));
                                writer.write(String.format("    \"%s\" -> \"%s\" [label=\"%.2f\"];\n", subItemId, idOperationMain, quantityIt));
                            }
                            index += 2;
                        }
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                        System.err.println("Error: " + line);
                        e.printStackTrace();
                    }
                }
            }

            writer.write("}\n");
        }
    }



    private void renderDotFile() throws IOException {
        String[] cmd = {"dot", "-Tpng", STRUCTURE_BOO_DOT, "-o", STRUCTURE_BOO_PNG};
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
