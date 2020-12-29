package main;

import java.util.ArrayList;
import java.util.List;

public class RuleBasedLabelingMechanism extends LabelingMechanism {

	public RuleBasedLabelingMechanism() {
        this.assignedLabels = new ArrayList<Label>();
	}

	public List<Label> labelInstance(Instance instance, List<Label> labels, int maxLabel) {
        return this.assignedLabels;
	}
	
}
