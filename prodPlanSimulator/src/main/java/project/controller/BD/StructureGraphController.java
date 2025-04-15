package project.controller.BD;

import project.domain.genericDataStructures.Graph;
import project.domain.service.ActivityInfo;

import java.io.*;

public class StructureGraphController {

    private static final String STRUCTURE_GRAPH_DOT = "documentation\\structure\\graph.dot";
    private static final String STRUCTURE_GRAPH_PNG = "documentation\\structure\\graph.png";

    public boolean execute(Graph<ActivityInfo, Double> graph) throws IOException {
        generate(graph);
        return renderDotFile();
    }

    private void generate(Graph<ActivityInfo, Double> graph) throws IOException {
        try (FileWriter writer = new FileWriter(STRUCTURE_GRAPH_DOT)) {

            writer.write("digraph {\n");
            writer.write("    rankdir=LR;\n");
            writer.write("    size=\"10,10\";\n");
            writer.write("    dpi=300;\n");
            writer.write("    node [style=filled, fontname=\"Arial\"];\n");

            for (ActivityInfo activityInfo : graph.vertices()) {
                String actID = activityInfo.getActivity().getActID();
                String description = activityInfo.getActivity().getDescription();
                double duration = activityInfo.getActivity().getDuration();
                String durationUnit = activityInfo.getActivity().getDurationUnit();
                double cost = activityInfo.getActivity().getCost();
                String costUnit = activityInfo.getActivity().getCostUnit();

                if (duration == 0 || cost == 0) {
                    writer.write(String.format("    \"%s\" [label=\"%s\", shape=box];\n", actID, description));
                } else {
                    writer.write(String.format("    \"%s\" [label=\"%s\n(%s)\n%.0f %s\n%.2f %s\", shape=box];\n",
                            actID, description, actID, duration, durationUnit, cost, costUnit));
                }

                for (ActivityInfo predecessor : graph.incomingVertices(activityInfo)) {
                    writer.write(String.format("    \"%s\" -> \"%s\";\n",
                            predecessor.getActivity().getActID(), actID));
                }
            }

            writer.write("}\n");
        }
    }


    private boolean renderDotFile() throws IOException {
        String[] cmd = {"dot", "-Tpng", STRUCTURE_GRAPH_DOT, "-o", STRUCTURE_GRAPH_PNG};
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        boolean success = true;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                success = false;
                throw new IOException("Graphviz command failed with exit code " + exitCode);
            }
        } catch (InterruptedException e) {
            success = false;
            Thread.currentThread().interrupt();
            throw new IOException("Graphviz command interrupted", e);
        }

        return success;
    }
}
