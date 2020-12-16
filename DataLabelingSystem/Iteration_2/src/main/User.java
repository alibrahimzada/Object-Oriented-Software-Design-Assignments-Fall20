package main;

import java.util.ArrayList;
import java.util.Random;

public class User {
    private int id;
    private String name;
    private String type;
    private ArrayList<Dataset> assignedDatasets;
    private ArrayList<LabelAssignment> labelAssignments;
    private double timeSpent;

    public User(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.assignedDatasets = new ArrayList<Dataset>();
        this.labelAssignments = new ArrayList<LabelAssignment>();
        this.timeSpent = 0;
    }

    public void addAssignedDataset(Dataset dataset) {
        this.assignedDatasets.add(dataset);
    }

    public void addLabelAssignment(LabelAssignment labelAssignment) {
        this.labelAssignments.add(labelAssignment);
    }

    public ArrayList<LabelAssignment> getLabelAssignments() {
        return this.labelAssignments;
    }

    public Instance getRandomInstance() {
        Random random = new Random();
        int randomIndex = random.nextInt(this.getTotalLabelings());
        return this.labelAssignments.get(randomIndex).getInstance();
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public int getTotalDatasets() {
        return this.assignedDatasets.size();
    }

    public String getCompletenessPercentage(Dataset dataset) {
        return "";
    }

    public int getTotalLabelings() {
        return this.labelAssignments.size();
    }

    public int getUniqueLabelings() {
        return 1;
    }

    public String getConsistencyPercentage() {
        return "";
    }

    public double getAverageTime() {
        return 1.0;
    }

    public double getStdDevTime() {
        return 1.0;
    }
}
