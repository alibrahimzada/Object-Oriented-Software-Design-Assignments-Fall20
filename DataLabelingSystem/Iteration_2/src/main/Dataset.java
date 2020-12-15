package main;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONArray;

public class Dataset {
    private int id;
    private String name;
    private int maxLabel;
    private ArrayList<Instance> instances;
    private ArrayList<Label> labels;

    public Dataset(int id, String name, int maxLabel) {
        this.id = id;
        this.name = name;
        this.maxLabel = maxLabel;
        this.instances = new ArrayList<Instance>();
        this.labels = new ArrayList<Label>();
    }

    public void addInstances(JSONArray instances) {
        /*
        Takes a JsonArray containing Instances of the dataset and hashes it. For each dataset,
        it gets the info of it and creates with with an object of the Instance class. Then, stores
        the object in the arrayList instances. 
        */
        for (int i = 0; i < instances.size(); i++) {
            HashMap<String, Object> currentInstance = (HashMap<String, Object>) instances.get(i);
            long instanceId = (long) currentInstance.get("id");
            String instanceText = (String) currentInstance.get("instance");
            Instance instanceObject = new Instance((int) instanceId, instanceText);
            this.instances.add(instanceObject);
        }
    }

    public void addLabels(JSONArray labels) {
        /*
        Takes a JsonArray containing labels of the dataset and hashes it. For each label,
        it gets the info of it and creates with with an object of the Label class. Then, stores
        the object in the arrayList labels. 
        */
        for (int i = 0; i < labels.size(); i++) {
            HashMap<String, Object> currentLabel = (HashMap<String, Object>) labels.get(i);
            long labelId = (long) currentLabel.get("label id");
            String labelText = (String) currentLabel.get("label text");
            Label labelObject = new Label((int) labelId, labelText);
            this.labels.add(labelObject);
        }
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getMaxLabel() {
        return this.maxLabel;
    }

    public ArrayList<Instance> getInstances() {
        return this.instances;
    }

    public ArrayList<Label> getLabels() {
        return this.labels;
    }
}
