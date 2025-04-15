package project.controller;

import project.domain.genericDataStructures.Graph;
import project.domain.service.ActivityInfo;
import project.domain.service.ActivityTimeCalculator;

public class CalculateTimesController {

    private final ActivityTimeCalculator calculator;

    public CalculateTimesController(Graph<ActivityInfo, Double> graph) {
        this.calculator = new ActivityTimeCalculator(graph);
    }

    public Graph<ActivityInfo, Double> calculateTimes() {
        return calculator.calculateTimes();
    }
}
