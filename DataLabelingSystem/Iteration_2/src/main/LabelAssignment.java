package main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;

public class LabelAssignment {
    private double timeSpent;
    private Date date;
    private Instance instance;
    private User user;
    private List<Label> labels;
    private List<Label> assignedLabels;
    private LabelingMechanism labelingMechanism;

    public LabelAssignment(User user, Instance instance, List<Label> labels, LabelingMechanism labelingMechanism) {
        this.user = user;
        this.instance = instance;
        this.labels = labels;
        this.labelingMechanism = labelingMechanism;
    }

    public void assignLabels(int maxLabel) {
        /*
            Uses the object labelingMechanism passed it to invoke its method labelInstance
            while passing to the method the instance, labels, and the max number of labels
            an instance might have.
        */
        long start = System.nanoTime();
        this.assignedLabels = this.labelingMechanism.labelInstance(this.instance, this.labels, maxLabel);
        this.user.addLabelAssignment(this);
        this.instance.addLabelAssignment(this);
        this.date = new Date();
        long end = System.nanoTime();
        this.timeSpent = (end - start) / 1000000D;
    }

    public double getTimeSpent() {
        return this.timeSpent;
    }

    public void setTimeSpent(Double timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(String dateString) {
        try {
            this.date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(dateString);
        } catch (Exception ex) {
            System.out.println("could not parse date from dateString!");
        }
    }

    public Instance getInstance() {
        return this.instance;
    }

    public User getUser() {
        return this.user;
    }

    public List<Label> getAssignedLabels() {
        return this.assignedLabels;
    }

    public void setAssignedLabels(JSONArray assignedLabelsIds) {
        List<Label> assignedLabels = new ArrayList<Label>();
        for (int i = 0; i < assignedLabelsIds.size(); i++) {
            for (Label label : this.labels) {
                if (label.getId() == ((Long) assignedLabelsIds.get(i)).intValue()) {
                    assignedLabels.add(label);
                }
            }
        }
        this.assignedLabels = assignedLabels;
    }

    public List<Label> getLabels() {
        return this.labels;
    }

    public LabelingMechanism getLabelingMechanism() {
        return this.labelingMechanism;
    }
}
