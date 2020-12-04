package tests;

import main.*;
import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;


@Testable
public class RandomLabelingMechanismTests {

    private RandomLabelingMechanism labelingMechanism1, labelingMechanism2;

    public RandomLabelingMechanismTests(){

        this.labelingMechanism1 = new RandomLabelingMechanism();
        this.labelingMechanism2 = new RandomLabelingMechanism();

    }

    @Test
    public void labelInstanceTest(){
        ArrayList<Label> labels = createLabels();
        Instance instance = new Instance(1, "This is instance 1");
        int maxLabel1 = 1, maxLabel2 = 5;
        ArrayList<Label> assignedLabels1 = this.labelingMechanism1.labelInstance(instance, labels, maxLabel1);
        ArrayList<Label> assignedLabels2 = this.labelingMechanism2.labelInstance(instance, labels, maxLabel2);
        assertEquals(1, assignedLabels1.size());
        assertTrue(assignedLabels2.size() >= 1 && assignedLabels2.size() <= maxLabel2);

    }

    @Test
    public void getRandomElementTest(){
        ArrayList<Label> labels = createLabels();
        Label label = this.labelingMechanism1.getRandomElement(labels);
        assertTrue(labels.contains(label));

    }

    
    private ArrayList<Label> createLabels(){
        ArrayList<Label> labels = new ArrayList<Label>();
        labels.add(new Label(1, "This is label 1"));
        labels.add(new Label(2, "This is label 2"));
        labels.add(new Label(2, "This is label 3"));
        labels.add(new Label(2, "This is label 4"));
        return labels;
    }
    
}
