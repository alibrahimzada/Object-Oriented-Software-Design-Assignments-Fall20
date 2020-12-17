package main;

import java.util.ArrayList;
import java.util.Date;

public class LabelAssignment {
    private double timeSpent;
    private Date date;
    private Instance instance;
    private User user;
    private ArrayList<Label> labels;
    private ArrayList<Label> assignedLabels;
    private LabelingMechanism labelingMechanism;

    public LabelAssignment(User user, Instance instance, ArrayList<Label> labels, LabelingMechanism labelingMechanism) {
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
        long start = System.currentTimeMillis();
        this.assignedLabels = this.labelingMechanism.labelInstance(this.instance, this.labels, maxLabel);
        this.user.addLabelAssignment(this);
        this.instance.addLabelAssignment(this);
        this.date = new Date();
        long end = System.currentTimeMillis();
        this.timeSpent = (end - start) / 1000D;
    }

    public double getTimeSpent() {
        return this.timeSpent;
    }

    public Date getDate() {
        return this.date;
    }

    public Instance getInstance() {
        return this.instance;
    }

    public User getUser() {
        return this.user;
    }

    public ArrayList<Label> getAssignedLabels() {
        return this.assignedLabels;
    }

    public ArrayList<Label> getLabels() {
        return this.labels;
    }

    public LabelingMechanism getLabelingMechanism() {
        return this.labelingMechanism;
    }
}
