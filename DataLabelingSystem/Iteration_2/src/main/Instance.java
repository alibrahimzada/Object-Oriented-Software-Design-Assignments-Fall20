package main;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Instance {

    // attributes of the Instance class
    private Integer id;
    private String text;
    private Dataset dataset;
    private ArrayList<LabelAssignment> labelAssignments;
    
    // constructor of the instance class
    public Instance(Integer id, String text) {
        this.id = id;
        this.text = text;
        this.labelAssignments = new ArrayList<LabelAssignment>();
    }

    // adds a label assignment object to the list of label assignments
    public void addLabelAssignment(LabelAssignment labelAssignment) {
        this.labelAssignments.add(labelAssignment);
    }

    // returns the id of this instance
    public Integer getId() {
    	return this.id;
    }

    // returns the text of this instance
    public String getText() {
    	return this.text;
    }

    // returns the label assignments from this instance
    public ArrayList<LabelAssignment> getLabelAssignments() {
        return this.labelAssignments;
    }

    // sets the dataset field of this instance to the given dataset object
    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }
    
    // returns the dataset of this instance
    public Dataset getDataset() {
        return this.dataset;
    }
    
    // returns the total number of label assignments from this instance
    public Integer getTotalLabelAssignments() {
        return this.labelAssignments.size();
    }

    // returns the total number of unique label assignments from this instance
    public Integer getUniqueLabelAssignments() {
        ArrayList<String> labelNames = new ArrayList<String>();

        // loop over all label assignments
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            for (Label label : labelAssignment.getAssignedLabels()) {

                // check if label name is already in the list
                if (!labelNames.contains(label.getText())) {
                    labelNames.add(label.getText());
                }
            }
        }

        // return the length of the list
        return labelNames.size();
    }

    // returns the total number of unique users involved in assigning labels to this instance
    public Integer getUniqueUsers() {
        ArrayList<Integer> userIds = new ArrayList<Integer>();

        // loop over all label assignments
        for (LabelAssignment labelAssignment : this.labelAssignments) {

            // check if the user is already added to the list
            if (!userIds.contains(labelAssignment.getUser().getId())) {
                userIds.add(labelAssignment.getUser().getId());
            }
        }

        // return the length of the list
        return userIds.size();
    }

    // returns the most frequent label from label assignments
    public String getFrequentLabel() {
        Map<String, Integer> labelFreq = new LinkedHashMap<String, Integer>();
        int totalCount = 0;

        // calculate frequency of each label
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            for (Label label : labelAssignment.getAssignedLabels()) {
                if (labelFreq.containsKey(label.getText())) {
                    int currentCount = (int) labelFreq.get(label.getText());
                    labelFreq.put(label.getText(), currentCount + 1);
                } else {
                    labelFreq.put(label.getText(), 1);
                }
                totalCount++;
            }
        }
        // find the max key-value pair
        Map.Entry<String, Integer> maxEntry = Map.entry("X", 0);   // default value
        for (Map.Entry<String, Integer> entry : labelFreq.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        // return the frequent label name and percentage
        double frequency = maxEntry.getValue() * 100.0 / totalCount;
        if (Double.isNaN(frequency)) {
            return String.format("%s - %.2f", maxEntry.getKey(), frequency);
        }
        return String.format("%s - %.2f", maxEntry.getKey(), frequency) + "%";
    }

    // returns the final class label distribution
    public Map<String, Object> getClassLabelDistribution() {
        Map<String, Object> labelFreq = new LinkedHashMap<String, Object>();
        int totalCount = 0;

        // calculate frequency of each label
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            // first put 0 to all labels
            if (totalCount == 0) {
                for (Label label : labelAssignment.getLabels()) {
                    labelFreq.put(label.getText(), 0);
                }    
            }

            for (Label label : labelAssignment.getAssignedLabels()) {
                if (labelFreq.containsKey(label.getText())) {
                    int currentCount = (int) labelFreq.get(label.getText());
                    labelFreq.put(label.getText(), currentCount + 1);
                } else {
                    labelFreq.put(label.getText(), 1);
                }
                totalCount++;
            }
        }

        // normalize and calculate percentages
        for (Map.Entry<String, Object> entry : labelFreq.entrySet()) {
            labelFreq.put(entry.getKey(), String.format("%.2f", (int) entry.getValue() * 100.0 / totalCount) + "%");
        }

        return labelFreq;
    }

    // returns the entropy of this instance
    public String getEntropy() {
        Map<String, Integer> labelFreq = new HashMap<String, Integer>();

        // parse the all of the assigned labels and calculate their frequencies
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            for (Label label : labelAssignment.getAssignedLabels()) {
                if (labelFreq.containsKey(label.getText())) {
                    int currentCount = labelFreq.get(label.getText());
                    labelFreq.put(label.getText(), currentCount + 1);
                } else {
                    labelFreq.put(label.getText(), 1);
                }
            }
        }

        // calculate entropy
        double entropy = 0;
        for (Map.Entry<String, Integer> entry : labelFreq.entrySet()) {
            double normalizedValue = entry.getValue() / labelFreq.size();
            entropy += -normalizedValue * (Math.log(normalizedValue) / Math.log(2));
        }
        
        if (Double.isNaN(entropy)) {
            return "NaN";
        }
        return String.format("%.3f", entropy);
    }
}
