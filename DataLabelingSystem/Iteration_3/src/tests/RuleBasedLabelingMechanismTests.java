package tests;

import main.*;
import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;


@Testable
public class RuleBasedLabelingMechanismTests {

    private RuleBasedLabelingMechanism labelingMechanism1, labelingMechanism2;

    public RuleBasedLabelingMechanismTests(){
        this.labelingMechanism1 = new RuleBasedLabelingMechanism();
        this.labelingMechanism2 = new RuleBasedLabelingMechanism();
    }

    @Test
    public void labelInstanceTest(){
        List<Label> labels = createLabels();
        Instance instance = new Instance(1, "This is instance");
        int maxLabel1 = 1, maxLabel2 = 4;
        List<Label> assignedLabels1 = this.labelingMechanism1.labelInstance(instance, labels, maxLabel1);
        List<Label> assignedLabels2 = this.labelingMechanism2.labelInstance(instance, labels, maxLabel2);
        assertEquals(1, assignedLabels1.size());// to test when maxLabel = 1
        assertTrue(assignedLabels2.size() >= 1 && assignedLabels2.size() <= maxLabel2);//to test when maxLabel>1
        assertTrue(assignedLabels1.get(0).getText() == "label");//expected label with highest sim is "label"
    }


    @Test
    public void sortLabelsBySimilaritiesTest(){
        List<Label> labels = createLabels(); // create labels
        Instance instance = new Instance(1, "This is an instance");
        List<Label> sortedLabels = this.labelingMechanism1.sortLabelsBySimilarities(instance, labels);
        String expectedLabel = "label"; // the label with the highest similarity
        assertTrue(sortedLabels.get(0).getText() == expectedLabel);
    }

    @Test
    public void calculateSimilarityTest(){
        Instance instance = new Instance(1, "This is an instance");
        Label label = new Label(1, "label");
        double actualSimilarity = this.labelingMechanism1.calculateSimilarity(instance, label);
        double expectedSimilarity = (double)(1 + 2)/label.getText().length(); // {e: 1, a: 2}
        assertTrue(actualSimilarity == expectedSimilarity);

    }

    
    private List<Label> createLabels(){
        List<Label> labels = new ArrayList<Label>();
        labels.add(new Label(1, "label")); // expectedSim = .6
        labels.add(new Label(2, "xyza")); // expectedSim = .5
        labels.add(new Label(2, "xyze")); // expectedSim = .25
        labels.add(new Label(2, "xyzc")); // expectedSim = .25
        return labels;
    }
    
}