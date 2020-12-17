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
    private ArrayList<User> assignedUsers;

    public Dataset(int id, String name, int maxLabel) {
        this.id = id;
        this.name = name;
        this.maxLabel = maxLabel;
        this.instances = new ArrayList<Instance>();
        this.labels = new ArrayList<Label>();
        this.assignedUsers = new ArrayList<User>();
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
            instanceObject.setDataset(this);
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
            labelObject.setDataset(this);
            this.labels.add(labelObject);
        }
    }

    public void addAssignedUser(User user) {
        this.assignedUsers.add(user);
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

    public ArrayList<User> getAssignedUsers() {
        return this.assignedUsers;
    }

    public String getCompleteness() {
        //C- Dataset Performance Metrics: 1
        int instance_labeled_count = 0;
        for (Instance instance : instances){
            if (instance.getUniqueAssignments() >= 1) 
                instance_labeled_count++;
        }
        return String.format("%.0f", instance_labeled_count/instances.size()*100)+ "%";
    }

    public HashMap<String,Double> getDistribution() {
        //C- Dataset Performance Metrics: 2
        HashMap<String,Double> labelFreqs = new HashMap<String,Double>();
        for (Instance instance : instances){
            labelFreqs.merge(instance.getFrequentLabel(), 1.0, Double::sum);
        }
        for (String key: labelFreqs.keySet()){
            labelFreqs.put(key, labelFreqs.get(key)/instances.size() * 100);
        }
        return labelFreqs;
    }

    public HashMap<String,Integer> getUniqueInstances() {
        //C- Dataset Performance Metrics: 3
        HashMap<String,Integer> labelFreqsUniqueInstances = new HashMap<String,Integer>();
        for (Instance instance : instances){
            labelFreqsUniqueInstances.merge(instance.getFrequentLabel(), 1, Integer::sum);
        }
        return labelFreqsUniqueInstances;
    }

    public int getTotalAssignedUsers() {
         //C- Dataset Performance Metrics: 4
        return this.assignedUsers.size();
    }

    public HashMap<String,Double> getUserCompletenessPercentange() {
        //C- Dataset Performance Metrics: 5
        HashMap<String,Double> userCompletenessPercentage = new HashMap<String,Double>();
        for (User user : assignedUsers){
            userCompletenessPercentage.put(user.getName(), (double)user.getUniqueLabelings());
        }
        for (String key: userCompletenessPercentage.keySet()){
            userCompletenessPercentage.put(key, userCompletenessPercentage.get(key)/instances.size() * 100);
        }
        return userCompletenessPercentage;
    }

    public HashMap<String,Double> getUserConsistencyPercentage() {
        //C- Dataset Performance Metrics: 6
        HashMap<String,Double> userConsistencyPercentage = new HashMap<String,Double>();
        for (User user : assignedUsers)
           userConsistencyPercentage.put(user.getName(), user.getConsistencyPercentage());

        return userConsistencyPercentage;
    }
}
