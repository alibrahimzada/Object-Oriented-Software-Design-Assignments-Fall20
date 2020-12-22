package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomLabelingMechanism extends LabelingMechanism {

    public RandomLabelingMechanism() {
        this.assignedLabels = new ArrayList<Label>();
    }

    public List<Label> labelInstance(Instance instance, List<Label> labels, int maxLabel) {
        /*
            Randomly assigns labels(one or more-depends on maxLabel) to an instance.
        */

        if (maxLabel == 1) { // if maxLabel is 1, assign one label randomly 
            this.assignedLabels.add(this.getRandomElement(labels));
        } else { // if maxLabel is > 1
            Random rand = new Random(); 
            int selectionCount = rand.nextInt(maxLabel); // get a random number(#of labels to assign) from 0 to (maxLabel-1)
            do {
                Label selectedLabel = getRandomElement(labels); // get a random label
                if (!this.assignedLabels.contains(selectedLabel)) {
                    this.assignedLabels.add(selectedLabel); // add the selected label to the assignedLabels
                    selectionCount -= 1; //decrement the (#of labels to assign)
                }
            } while (selectionCount >= 0); 
        }
        return this.assignedLabels;
    }

    public Label getRandomElement(List<Label> labels) {
        /*
          Given an arrayList of labels, returns one of the labels randomly.  
        */
        Random rand = new Random();
        return labels.get(rand.nextInt(labels.size()));
    }
}
