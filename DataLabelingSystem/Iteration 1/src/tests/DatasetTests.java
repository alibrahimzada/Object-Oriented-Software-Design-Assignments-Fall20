package tests;

import main.*;
import java.util.ArrayList;
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

    @Test
    public void DatasetCreationTest(){
        try {
            this.dataset =  new Dataset(1, "dataset1", 1);
          } catch (Exception e) {
            Assert.fail(e.getMessage());
          }


    }

    @Test 
    public void addInstanceTest(){
        JSONArray jsonArray = createJsonArray("instance");
        this.dataset.addInstances(jsonArray);
        ArrayList<Instance> acutal_instances = this.dataset.getInstances();
        ArrayList<Instance> expected_instances = new ArrayList<Instance>();
        expected_instances.add(new Instance(1, "This is instance 1"));
        expected_instances.add(new Instance(2, "This is instance 2"));
        for (int i = 0; i < expected_instances.size(); i++) {
            Assert.assertEquals(expected_instances.get(i).getId(), acutal_instances.get(i).getId());
            Assert.assertEquals(expected_instances.get(i).getText(), acutal_instances.get(i).getText());
        }
    }

    @Test 
    public void addLabelTest(){
        JSONArray jsonArray = createJsonArray("label");
        Dataset dataset1 = new Dataset(1, "dataset1", 1);
        dataset1.addLabels(jsonArray);
        ArrayList<Label> acutal_labels = dataset1.getLabels();
        ArrayList<Label> expected_labels = new ArrayList<Label>();
        expected_labels.add(new Label(1, "This is label 1"));
        expected_labels.add(new Label(2, "This is label 2"));
        for (int i = 0; i < expected_labels.size(); i++) {
            Assert.assertEquals(expected_labels.get(i).getId(), acutal_labels.get(i).getId());
            Assert.assertEquals(expected_labels.get(i).getText(), acutal_labels.get(i).getText());
        }

        
    }

    private JSONArray createJsonArray(String type){
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
