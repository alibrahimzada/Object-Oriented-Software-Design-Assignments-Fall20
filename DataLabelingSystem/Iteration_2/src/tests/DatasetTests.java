package tests;

import main.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;
import org.junit.Assert;



@Testable
public class DatasetTests {
    
    private Dataset dataset;
    
    public DatasetTests(){
        DatasetCreationTest(); 
    }

    public Dataset getDataset(){
        return dataset;
    }

    @Test
    public void DatasetCreationTest(){
        // test (the creation) and initialize a dataset object. 
        try {
            this.dataset =  new Dataset(1, "dataset1", 1);
          } catch (Exception e) {
            Assert.fail(e.getMessage());
          }
    }

    @Test 
    public void addInstanceTest(){
        JSONArray jsonArray = createJsonArray("instance"); // creating a json array with 2 instances.
        dataset.addInstances(jsonArray); // invoke the method to be tested. 
        List<Instance> acutal_instances = dataset.getInstances(); // getting the added instances
        List<Instance> expected_instances = new ArrayList<Instance>(); // an array to hold two datasets. 
        expected_instances.add(new Instance(1, "This is instance 1")); 
        expected_instances.add(new Instance(2, "This is instance 2"));
        for (int i = 0; i < expected_instances.size(); i++) {
            // testing the equality of the actual and the expected instances' ids and texts.
            Assert.assertEquals(expected_instances.get(i).getId(), acutal_instances.get(i).getId());
            Assert.assertEquals(expected_instances.get(i).getText(), acutal_instances.get(i).getText());
        }
    }

    @Test 
    public void addLabelTest(){
        JSONArray jsonArray = createJsonArray("label"); // creating a json array with 2 labels.
        dataset.addLabels(jsonArray);
        List<Label> acutal_labels = dataset.getLabels(); // getting the actual return of the method
        List<Label> expected_labels = new ArrayList<Label>();
        expected_labels.add(new Label(1, "This is label 1"));
        expected_labels.add(new Label(2, "This is label 2"));
        for (int i = 0; i < expected_labels.size(); i++) {
             // testing the equality of the actual and the expected labels' ids and texts.
            Assert.assertEquals(expected_labels.get(i).getId(), acutal_labels.get(i).getId());
            Assert.assertEquals(expected_labels.get(i).getText(), acutal_labels.get(i).getText());
        }
    }

    @Test
    public void addAssignedUserTest(){
        // testing adding assgined users.  
        User userTest = new User(1, "name", "type");
        dataset.addAssignedUser(userTest);
        List<User> users = dataset.getAssignedUsers();
        assertTrue(users.contains(userTest));
    }

    @Test
    public void getCompletenessTest(){
        JSONArray jsonArray = createJsonArray("instance"); // creating a json array with 2 instances.
        dataset.addInstances(jsonArray); 
        //user
        User userTest = new User(1, "name", "type");
        //labels 
        List<Label> labels = new ArrayList<Label>();
        //labeling mechanism
        LabelingMechanism labelingMechanism = new RandomLabelingMechanism();
        // adding a label assignment to one of the instances
        for (Instance instance: dataset.getInstances()){
            LabelAssignment labelAssignment = new LabelAssignment(userTest, instance, labels, labelingMechanism);
            instance.addLabelAssignment(labelAssignment);
            break; //add for the first instance only.  
        }
        assertTrue(dataset.getCompleteness() == 50.0 ); // testing if 1/2 of the instances are labeled as expected.
    }

    public JSONArray createJsonArray(String type){
        // creates and returns a json array with either 2 instances or 2 labels
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String id = "id", text = type;
        if (type.equals("label")){
            id = "label id";
            text = "label text";
        }
        jsonObject1.put(id, (long) 1);
        jsonObject1.put(text, "This is " + type   +" 1");
        jsonObject2.put(id, (long) 2);
        jsonObject2.put(text, "This is " + type   +" 2");
        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);

        return jsonArray;
    }

    
   
    
}
