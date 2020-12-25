package tests;

import main.*;
import org.junit.platform.commons.annotation.Testable;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import org.json.simple.JSONArray;
import org.junit.Assert;

@Testable
public class InstanceTests {

    private Instance instance;

    public InstanceTests(){
        instanceCreationTest(); 
    }
   
    @Test
    public void instanceCreationTest(){
        // test (the creation) and initialize an Instance object. 
        try {
            instance =  new Instance(1, "I am an instance");
          } catch (Exception e) {
            Assert.fail(e.getMessage());
          }
    }

    @Test
    public void getUniqueUsersTest(){
        Instance instance = createInstanceToTest();
        //invoke the method to be tested. Expecting 2 unique users.
        assertTrue(instance.getUniqueUsers() == 1);
    }
        
    

    @Test
    public void getEntropyTest(){
        Instance instance = createInstanceToTest();
        // involing the method to be tested: checking if the entropy is between 0 and 1. 
        double entropy = instance.getEntropy();
        assertTrue(entropy >= 0 && entropy <= 1);
    }

    private Instance createInstanceToTest(){
        /*
            Creating a instance of a dataset with label assignments to test.
        */
        // dataset
        DatasetTests datasetTests = new DatasetTests();
        Dataset dataset =  datasetTests.getDataset();
        //instances
        JSONArray jsonArray = datasetTests.createJsonArray("instance"); // creating a json array with 2 instances.
        dataset.addInstances(jsonArray);
        //labels
        JSONArray jsonArray1 = datasetTests.createJsonArray("label"); // creating a json array with 2 labels.
        dataset.addLabels(jsonArray1);
        //users 
        User user1 = new User(1, "name1", "type1", "password", 0.1);
        //labeling mechanism
        LabelingMechanism labelingMechanism = new RandomLabelingMechanism();
        // label assignments
        LabelAssignment labelAssignment1 = new LabelAssignment(user1, instance, dataset.getLabels(), labelingMechanism);
        LabelAssignment labelAssignment2 = new LabelAssignment(user1, instance, dataset.getLabels(), labelingMechanism);
        labelAssignment1.assignLabels(2);
        labelAssignment2.assignLabels(1);
        instance.addLabelAssignment(labelAssignment1);
        instance.addLabelAssignment(labelAssignment2);
        return instance;
}

}
