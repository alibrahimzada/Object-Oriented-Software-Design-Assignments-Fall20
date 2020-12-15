package main;

import java.util.ArrayList;

public abstract class LabelingMechanism {
    protected ArrayList<Label> assignedLabels;

    public abstract ArrayList<Label> labelInstance(Instance instance, ArrayList<Label> labels, int maxLabel);
}
