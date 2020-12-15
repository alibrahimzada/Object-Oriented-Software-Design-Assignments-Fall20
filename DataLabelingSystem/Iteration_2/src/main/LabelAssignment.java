package main;

import java.util.ArrayList;
import java.util.Date;

public class LabelAssignment {
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
        this.assignedLabels = this.labelingMechanism.labelInstance(this.instance, this.labels, maxLabel);
        this.date = new Date();
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
