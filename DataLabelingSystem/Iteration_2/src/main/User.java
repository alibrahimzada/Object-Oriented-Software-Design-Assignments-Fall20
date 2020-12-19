package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class User {

    // attributes of User class
    private Integer id;
    private String name;
    private String type;
    private ArrayList<Dataset> assignedDatasets;
    private ArrayList<LabelAssignment> labelAssignments;

    // constructor of User class
    public User(Integer id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.assignedDatasets = new ArrayList<Dataset>();
        this.labelAssignments = new ArrayList<LabelAssignment>();
    }

    // adds a given dataset to the list of assigned datasets
    public void addAssignedDataset(Dataset dataset) {
        this.assignedDatasets.add(dataset);
    }

    // returns a list of assigned dataset objects assigned to this user
    public ArrayList<Dataset> getAssignedDatasets() {
        return this.assignedDatasets;
    }

    // adds a given label assignment to the list of label assignments
    public void addLabelAssignment(LabelAssignment labelAssignment) {
        this.labelAssignments.add(labelAssignment);
    }

    // returns a list of label assignment objects labeled by this user
    public ArrayList<LabelAssignment> getLabelAssignments() {
        return this.labelAssignments;
    }

    // returns a random instance object from the label assignment list
    public Instance getRandomInstance() {
        Random random = new Random();
        int randomIndex = random.nextInt(this.getTotalLabelings());
        return this.labelAssignments.get(randomIndex).getInstance();
    }

    // returns id of this user
    public Integer getId() {
        return this.id;
    }

    // returns name of this user
    public String getName() {
        return this.name;
    }

    // returns type of this user
    public String getType() {
        return this.type;
    }

    // returns total number of datasets assigned to this user
    public Integer getTotalDatasets() {
        return this.assignedDatasets.size();
    }

    // returns completeness percentage of this user from a given dataset
    public String getCompletenessPercentage(Dataset dataset) {
        ArrayList<String> uniqueInstances = new ArrayList<String>();

        // loop over all label assignments done by this user
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            String currentInstanceText = labelAssignment.getInstance().getText();

            // check if the instance is from the given dataset, and its not already in the list
            if (!uniqueInstances.contains(currentInstanceText) && labelAssignment.getInstance().getDataset() == dataset) {
                uniqueInstances.add(currentInstanceText);
            }
        }

        // calculate the completeness percentage of this user from the given dataset
        double completenessPercentage = (uniqueInstances.size() * 100.0) / dataset.getInstances().size();
        return String.format("%.2f", completenessPercentage) + "%";
    }

    // returns total number of labelings done by this user so far
    public Integer getTotalLabelings() {
        return this.labelAssignments.size();
    }

    // returns total number of unique labelings done by this user so far
    public Integer getTotalUniqueLabelings() {
        ArrayList<String> instances = new ArrayList<String>();

        // loop over each label assignment from this user
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            String currentInstanceText = labelAssignment.getInstance().getText();
            // add it to the list if its not already in the list
            if (!instances.contains(currentInstanceText)) {
                instances.add(currentInstanceText);
            }
        }

        // return the length of the list
        return instances.size();
    }

    // returns the consistency percentage of this user from all its labelings so far
    public String getConsistencyPercentage() {
        HashMap<String, Object> labelingsFreq = new HashMap<String, Object>();
        int totalReccurent = 0;

        // first start by counting the number of times a unique instance was labeled by this user
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            String currentInstanceText = labelAssignment.getInstance().getText();
            if (labelingsFreq.containsKey(currentInstanceText)) {
                HashMap<String, Object> value = (HashMap) labelingsFreq.get(currentInstanceText);
                int currentCount = (int) value.get("currentCount");
                value.put("currentCount", currentCount + 1);
                labelingsFreq.put(currentInstanceText, value);
                totalReccurent++;
            } else {
                HashMap<String, Object> value = new HashMap<String, Object>() {{
                    put("currentCount", 1);
                    put("object", labelAssignment);
                }};
                labelingsFreq.put(currentInstanceText , value);
            }
        }

        // count all of the reccurrent instances which were labeled the same 
        int sameReccurrents = 0;
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            String currentInstanceText = labelAssignment.getInstance().getText();
            HashMap<String, Object> value = (HashMap) labelingsFreq.get(currentInstanceText);
            if ((int) value.get("currentCount") > 1 && ((LabelAssignment) value.get("object")).getAssignedLabels() == labelAssignment.getAssignedLabels()) {
                sameReccurrents++;
            }
        }

        if (totalReccurent == 0) {
            return "0.00%";
        }
        return String.format("%.2f", (sameReccurrents / totalReccurent) * 100.0) + "%";
    }

    // returns the average time spent by this user labeling all its instances
    public Double getAverageTime() {
        // this variable will be accumulated with the time spent for all label assignments
        double totalTimeSpent = 0;

        // loop over each label assignment instance, and get their time spent in seconds
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            totalTimeSpent += labelAssignment.getTimeSpent();
        }

        // calculate the average time spent by this user
        if (totalTimeSpent == 0) {
            return 0.0;
        }
        return totalTimeSpent / this.getTotalLabelings();
    }

    // returns the standard deviation of time spent by this user labeling all its instances
    public Double getStdDevTime() {
        // this variable will be accumulated with the squared difference of time spent and average time
        double squaredDifference = 0;
        double averageTime = this.getAverageTime();

        // loop over each label assignment instance, get their spent time, and calculate their squared difference with average
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            squaredDifference += Math.pow(labelAssignment.getTimeSpent() - averageTime, 2);
        }

        // calculate the standard deviation of time spent by this user
        if (squaredDifference == 0) {
            return 0.0;
        }
        return Math.sqrt(squaredDifference / this.getTotalLabelings());
    }
}
