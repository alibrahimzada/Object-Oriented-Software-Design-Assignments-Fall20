package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;

public class Instance {

    // attributes of the Instance class
    private int id;
    private String text;
	private Dataset dataset;
	private Label finalLabel;
    private List<LabelAssignment> labelAssignments;
    
    // constructor of the instance class
    public Instance(int id, String text) {
        this.id = id;
        this.text = text;
        this.labelAssignments = new ArrayList<LabelAssignment>();
    }

    // adds a label assignment object to the list of label assignments
    public void addLabelAssignment(LabelAssignment labelAssignment) {
        this.labelAssignments.add(labelAssignment);
    }

    // returns the id of this instance
    public int getId() {
    	return this.id;
    }

    // returns the text of this instance
    public String getText() {
    	return this.text;
    }

    // returns the label assignments from this instance
    public List<LabelAssignment> getLabelAssignments() {
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
    public int getTotalLabelAssignments() {
        return this.labelAssignments.size();
    }

    // returns the total number of unique label assignments from this instance
    public int getUniqueLabelAssignments() {
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
    public int getUniqueUsers() {
        List<Integer> userIds = new ArrayList<Integer>();

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
    public String getFrequentLabelPercentage() {
        Map<String, Double> classLabelDistribution = this.getClassLabelDistribution();

        // find the max key-value pair
        Map.Entry<String, Double> maxEntry = Map.entry("X", 0.0);   // default value
        for (Map.Entry<String, Double> entry : classLabelDistribution.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }

        return String.format("%s - %.2f", maxEntry.getKey(), maxEntry.getValue()) + "%";
    }

    // returns the final class label distribution
    public Map<String, Double> getClassLabelDistribution() {
        Map<String, Double> labelFreq = new LinkedHashMap<String, Double>();
        int totalCount = 0;

        // calculate frequency of each label
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            // first put 0 to all labels
            if (totalCount == 0) {
                for (Label label : labelAssignment.getLabels()) {
                    labelFreq.put(label.getText(), 0.0);
                }    
            }

            for (Label label : labelAssignment.getAssignedLabels()) {
				double currentCount = (double) labelFreq.get(label.getText());
				labelFreq.put(label.getText(), currentCount + 1.0);
                totalCount++;
            }
        }

        // normalize and calculate percentages
        for (Map.Entry<String, Double> entry : labelFreq.entrySet()) {
            labelFreq.put(entry.getKey(), entry.getValue() * 100 / totalCount);
        }

		// return class label distribution
        return labelFreq;
    }

    // returns the entropy of this instance
    public double getEntropy() {
		// retrieve the class label distribution
        Map<String, Double> classLabelDistribution = this.getClassLabelDistribution();

        // calculate entropy
        double entropy = 0;
        for (Map.Entry<String, Double> entry : classLabelDistribution.entrySet()) {
			if (entry.getValue() == 0) {
				continue;
			}
			entropy += -(entry.getValue() / 100) * (Math.log(entry.getValue() / 100) / Math.log(classLabelDistribution.size()));
        }
		
		// return entropy
        return entropy;
	}
	
	// sets the final label of this instance
	public Label getFinalLabel() {
		// first get the text of the most frequent label
		String frequentLabelText = (this.getFrequentLabelPercentage().split(" - "))[0];
		Label frequentLabel = null;

		// find the label whose text is same as frequent label
		for (Label label : this.getDataset().getLabels()) {
			if (label.getText().equals(frequentLabelText)) {
				frequentLabel = label;
			}
		}
		this.finalLabel = frequentLabel;
		return frequentLabel;
	}
}
