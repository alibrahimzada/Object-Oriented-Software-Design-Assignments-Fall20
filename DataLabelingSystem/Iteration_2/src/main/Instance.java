package main;

import java.util.ArrayList;

public class Instance {
    private int id;
    private String text;
    private Dataset dataset;
    private ArrayList<LabelAssignment> labelAssignments;
    
    public Instance(int id, String text){
        this.id = id;
        this.text = text;
    }

    public int getId() {
    	return this.id;
    }
    
    public String getText() {
    	return this.text;
    }

    public Dataset getDataset() {
        return this.dataset;
    }
    
    public int getTotalAssignments() {
        return this.labelAssignments.size();
    }

    public int getUniqueAssignments() {
        return 1;
    }

    public int getUniqueUsers() {
        return 1;
    }

    public String getFrequentLabel() {
        return "";
    }

    public double getEntropy() {
        return 1.0;
    }
}
