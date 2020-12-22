package tests;

import main.*;
import org.junit.platform.commons.annotation.Testable;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.json.simple.JSONArray;
import org.junit.Assert;

@Testable
public class UserTests {

    private User user;

    public UserTests(){
      userCreationTest(); // test (the creation) and initialize a user object. 
    }
    
    @Test
    public void userCreationTest(){
        try {
            user =  new User(1, "name", "type");
          } catch (Exception e) {
            Assert.fail(e.getMessage());
          }
    }

    @Test
    public void getUniqueInstancesTest(){
      Dataset dataset = initializePopulatedDataset();
      User user = initializePopulatedUser(dataset);
      //invoking the method to be tested. Expected unique instances 2.(the two instances with id 1 and 2)
      assertTrue(user.getUniqueInstances(dataset).size() == 1);
    }


    @Test 
    public void getCompletenessPercentageTest(){
      Dataset dataset = initializePopulatedDataset();
      User user = initializePopulatedUser(dataset);
      Map<String, Double> completeness = user.getCompletenessPercentage();
      for (Map.Entry<String, Double> entry: completeness.entrySet()){
          assertTrue(entry.getValue() == 50.0); // half of the instances were labeled.
      }
    }

    @Test 
    public void getTotalUniqueLabelingsTest(){
      Dataset dataset = initializePopulatedDataset();
      User user = initializePopulatedUser(dataset);
      // invoking the second method to be tested.
      assertTrue(user.getTotalUniqueLabelings() == 1);
    }


    private Dataset initializePopulatedDataset(){
        // dataset
        DatasetTests datasetTests = new DatasetTests();
        Dataset dataset =  datasetTests.getDataset();
        //instances
        JSONArray jsonArray = datasetTests.createJsonArray("instance"); // creating a json array with 2 instances.
        dataset.addInstances(jsonArray);
        return dataset;
    }
    private User initializePopulatedUser(Dataset dataset){        
        //labeling mechanism
        LabelingMechanism labelingMechanism = new RandomLabelingMechanism();
        //labelassignments
        LabelAssignment labelAssignment1 = new LabelAssignment(user, dataset.getInstance(1), dataset.getLabels(), labelingMechanism);
        LabelAssignment labelAssignment2 = new LabelAssignment(user, dataset.getInstance(1), dataset.getLabels(), labelingMechanism);
        LabelAssignment labelAssignment3 = new LabelAssignment(user, dataset.getInstance(1), dataset.getLabels(), labelingMechanism);
        user.addLabelAssignment(labelAssignment1);
        user.addLabelAssignment(labelAssignment2);
        user.addLabelAssignment(labelAssignment3);
        // adding the dataset to the assigned datasets of the user
        user.addAssignedDataset(dataset);
        return user;
    }





    
}
