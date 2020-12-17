package main;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Instance {
    private int id;
    private String text;
    private Dataset dataset;
    private ArrayList<LabelAssignment> labelAssignments;
    
    public Instance(int id, String text) {
        this.id = id;
        this.text = text;
        this.labelAssignments = new ArrayList<LabelAssignment>();
    }

    public void addLabelAssignment(LabelAssignment labelAssignment) {
        this.labelAssignments.add(labelAssignment);
    }

    public int getId() {
    	return this.id;
    }
    
    public String getText() {
    	return this.text;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }
    
    public Dataset getDataset() {
        return this.dataset;
    }
    
    public int getTotalAssignments() {
        return this.labelAssignments.size();
    }

    public int getUniqueAssignments() {
        ArrayList<String> labelNames = new ArrayList<String>();
        for (LabelAssignment la : this.labelAssignments) {
            for (Label label : la.getLabels()){
                if (!labelNames.contains(label.getText())) {
                    labelNames.add(label.getText());
                }
            }
        }
        return labelNames.size();
    }

    public int getUniqueUsers() {
        ArrayList<Integer> userIds = new ArrayList<Integer>();
        for (LabelAssignment la : this.labelAssignments) {
            if (!userIds.contains(la.getUser().getId())) {
                userIds.add(la.getUser().getId());
            }
        }
        return userIds.size();
    }

    public String getFrequentLabel() {
        Map<String, Integer> labelFrequencies = new HashMap<String, Integer>();
        ArrayList<String> labelNames = new ArrayList<String>();

        //Parse the labels
        for(LabelAssignment la : this.labelAssignments){
            for (Label label : la.getLabels()){
                labelNames.add(label.getText());
            }
        }
        //Check for frequencies
        for (String i : labelNames) {
            Integer j = labelFrequencies.get(i);
            labelFrequencies.put(i, (j == null) ? 1 : j + 1);
        }


        //Sorting the HashMap to get the most frequent
        ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(labelFrequencies.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<String, Integer> sortedFrequencies = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedFrequencies.put(entry.getKey(), entry.getValue());
        }

        return sortedFrequencies.entrySet().iterator().next().getKey();
    }

    public double getEntropy() {
        double entropy = 0;
        Map<String, Integer> labels = new HashMap<String, Integer>();
        ArrayList<String> labelNames = new ArrayList<>();

        //Parse the labels
        for (LabelAssignment la : this.labelAssignments) {
            for (Label label : la.getLabels()) {
                labelNames.add(label.getText());
            }
        }
        //Check for frequencies
        for (String i : labelNames) {
            Integer j = labels.get(i);
            labels.put(i, (j == null) ? 1 : j + 1);
        }

        double i = 0;
        for (Map.Entry<String, Integer> entry : labels.entrySet()) {
            i = entry.getValue();
            entropy += (-i / 10 * (Math.log(i) / Math.log(2)));
        }

        return entropy;
    }
}
