package main;

import java.util.ArrayList;
import java.util.Random;

public class User {
    private int id;
    private String name;
    private String type;
    private ArrayList<Dataset> assignedDatasets;
    private ArrayList<LabelAssignment> labelAssignments;

    public User(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.assignedDatasets = new ArrayList<Dataset>();
        this.labelAssignments = new ArrayList<LabelAssignment>();
    }

    public void addAssignedDataset(Dataset dataset) {
        this.assignedDatasets.add(dataset);
    }

    public ArrayList<Dataset> getAssignedDatasets() {
        return this.assignedDatasets;
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

    public String getCompletenessPercentage() {
        String completenessPercentage = "";
        for (Dataset dataset : this.assignedDatasets) {
            completenessPercentage += String.format("Dataset%d %s, ", dataset.getId(), dataset.getCompleteness());
        }
        return completenessPercentage;
    }

    public int getTotalLabelings() {
        return this.labelAssignments.size();
    }

    public int getUniqueLabelings() {
        ArrayList<Integer> instanceIds = new ArrayList<Integer>();
        for (LabelAssignment la : this.labelAssignments) {
            if (!instanceIds.contains(la.getInstance().getId())) {
                instanceIds.add(la.getInstance().getId());
            }
        }
        return instanceIds.size();
    }

    public double getConsistencyPercentage() {
        ArrayList<LabelAssignment> reccurrentLabelings = new ArrayList<LabelAssignment>();

        for (LabelAssignment labelAssignment : this.labelAssignments) {

        }

        return 0;
    }

    public double getAverageTime() {
        double totalTimeSpent = 0;
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            totalTimeSpent += labelAssignment.getTimeSpent();
        }

        return totalTimeSpent / this.labelAssignments.size();
    }

    public double getStdDevTime() {
        double standardDeviation = 0;

        for (LabelAssignment labelAssignment : this.labelAssignments) {
            standardDeviation += Math.pow(labelAssignment.getTimeSpent() - this.getAverageTime(), 2);
        }

        return Math.sqrt(standardDeviation / this.getTotalLabelings());
    }
}
