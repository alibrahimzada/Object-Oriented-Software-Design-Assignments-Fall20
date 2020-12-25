package main;

import java.util.ArrayList;
import java.util.List;

public class ManualLabelingMechanism extends LabelingMechanism {

	private String[] userSelections;

    public ManualLabelingMechanism() {
        this.assignedLabels = new ArrayList<Label>();
    }

    public List<Label> labelInstance(Instance instance, List<Label> labels, int maxLabel) {
		for (String labelId : this.userSelections) {
			for (Label label : labels) {
				if (label.getId() == Integer.parseInt(labelId)) {
					this.assignedLabels.add(label);
				}
			}
			
			if (maxLabel == 1) {
				break;
			}
		}
        return this.assignedLabels;
	}
	
	public void setUserSelections(String[] userSelections) {
		this.userSelections = userSelections;
	}
}
