package project.controller;

import project.domain.genericDataStructures.Graph;
import project.domain.service.ActivityInfo;
import project.domain.service.ExportSchedule;

import java.io.IOException;

public class ExportScheduleController {

    private Graph<ActivityInfo, Double> graph;

    public ExportScheduleController(Graph<ActivityInfo, Double> graph) {
        this.graph = graph;
    }

    public boolean exportScheduleCSV(String fileName) throws IOException {
        try {
            ExportSchedule.exportProjectSchedule(graph, fileName);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}