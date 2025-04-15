package project.controller.BD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StructureBOMController {

    private static final String STRUCTURE_BOM_DOT = "documentation\\structure\\BOM.dot";
    private static final String STRUCTURE_BOM_PNG = "documentation\\structure\\BOM.png";
    private static final String BOO_FILE = "files\\booBD.csv";

    public void execute() throws IOException {

        generate();
        renderDotFile();

    }

    private void generate() throws IOException {
        try (FileWriter writer = new FileWriter(STRUCTURE_BOM_DOT)) {
            writer.write("digraph BOO {\n");

            writer.write("    node [style=filled, fontname=Arial];\n");

            String colorFirstItemMain = "yellow";
            String colorOtherItemMain = "lightgreen";
            String colorSubItem = "orange";

            try (BufferedReader br = new BufferedReader(new FileReader(BOO_FILE))) {
                String line = br.readLine();

                boolean first = true;

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(";");

                    try {
                        String idItemMain = values[1];

                        if (first) {
                            writer.write(String.format("    \"%s\" [shape=circle, color=%s];\n", idItemMain, colorFirstItemMain));
                            first = false;
                        } else {
                            writer.write(String.format("    \"%s\" [shape=circle, color=%s];\n", idItemMain, colorOtherItemMain));
                        }

                        int index = 4;

                        while (!values[index].equals(")")) {
                            if (!values[index].isEmpty()) {

                                String operation = values[index];
                                double quantityOp = Double.parseDouble(values[index + 1].replace(",", "."));

                                try (BufferedReader br2 = new BufferedReader(new FileReader(BOO_FILE))) {
                                    String line2 = br2.readLine();
                                    while ((line2 = br2.readLine()) != null) {
                                        String[] values2 = line2.split(";");
                                        if (values2[0].equals(operation)) {
                                            writer.write(String.format("    \"%s\" [shape=box, color=%s];\n", values2[1], colorSubItem));
                                            writer.write(String.format("    \"%s\" -> \"%s\" [label=\"%.2f\"];\n", values2[1], idItemMain, quantityOp));
                                        }
                                    }
                                    }
                            }
                            index += 2;
                        }

                        index += 2;

                        while (!values[index].equals(")")) {
                            if (!values[index].isEmpty()) {
                                String subItemId = values[index];
                                double quantityIt = Double.parseDouble(values[index + 1].replace(",", "."));

                                writer.write(String.format("    \"%s\" [shape=diamond, color=%s];\n", subItemId, colorSubItem));
                                writer.write(String.format("    \"%s\" -> \"%s\" [label=\"%.2f\"];\n", subItemId, idItemMain, quantityIt));
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
        String[] cmd = {"dot", "-Tpng", STRUCTURE_BOM_DOT, "-o", STRUCTURE_BOM_PNG};
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
