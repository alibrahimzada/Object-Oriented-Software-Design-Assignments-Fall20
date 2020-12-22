package main;

import java.util.List;

public abstract class LabelingMechanism {
    protected List<Label> assignedLabels;

    public abstract List<Label> labelInstance(Instance instance, List<Label> labels, int maxLabel);
}
