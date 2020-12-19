package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public Instance getInstance(Integer instanceId) {
        Instance instance = null;
        for (Instance currentInstance : this.instances) {
            if (currentInstance.getId() == instanceId) {
                instance = currentInstance;
            }
        }
        return instance;
    }

    public ArrayList<Label> getLabels() {
        return this.labels;
    }

    public ArrayList<User> getAssignedUsers() {
        return this.assignedUsers;
    }

    public String getCompleteness() {
        //C- Dataset Performance Metrics: 1
        int instanceLabeledCount = 0;
        for (Instance instance : this.instances) {
            if (instance.getTotalLabelAssignments() >= 1) {
                instanceLabeledCount++;
            }
        }
        return String.format("%.2f", instanceLabeledCount / instances.size() * 100.0)+ "%";
    }

    public HashMap<String, Double> getDistribution(ArrayList<LabelAssignment> labelAssignments) {
        //C- Dataset Performance Metrics: 2

        // first initialize counts to 0
        HashMap<String, Double> labelFreqs = new HashMap<String, Double>();
        for (Label label : this.labels) {
            labelFreqs.put(label.getText(), 0.0);
        }

        // count labels in label assignments
        for (LabelAssignment labelAssignment : labelAssignments) {
            for (Label label : labelAssignment.getAssignedLabels()) {
                double currentCount = labelFreqs.get(label.getText());
                labelFreqs.put(label.getText(), currentCount + 1);
            }
        }

        // normalize
        for (Map.Entry<String, Double> entry : labelFreqs.entrySet()) {
            labelFreqs.put(entry.getKey(), (entry.getValue() * 100.0) / labelAssignments.size());
        }
        return labelFreqs;
    }

    public HashMap<String, Integer> getUniqueInstances(ArrayList<LabelAssignment> labelAssignments) {
        //C- Dataset Performance Metrics: 3

        // first intialize counts to empty list
        HashMap<String, ArrayList<String>> uniqueInstances = new HashMap<String, ArrayList<String>>();
        for (Label label : this.labels) {
            uniqueInstances.put(label.getText(), new ArrayList<String>());
        }

        // add the unique instances to a list
        for (LabelAssignment labelAssignment : labelAssignments) {
            for (Label label : labelAssignment.getAssignedLabels()) {
                String instanceText = labelAssignment.getInstance().getText();
                if (!uniqueInstances.get(label.getText()).contains(instanceText)) {
                    uniqueInstances.get(label.getText()).add(instanceText);
                }
    
            }
        }

        // change the list to size of list
        HashMap<String, Integer> uniqueInstancesFreq = new HashMap<String, Integer>();
        for (Map.Entry<String, ArrayList<String>> entry : uniqueInstances.entrySet()) {
            uniqueInstancesFreq.put(entry.getKey(), entry.getValue().size());
        }
        return uniqueInstancesFreq;
    }

    public int getTotalAssignedUsers() {
         //C- Dataset Performance Metrics: 4
        return this.assignedUsers.size();
    }

    public HashMap<String, String> getUserCompletenessPercentange() {
        //C- Dataset Performance Metrics: 5
        HashMap<String, Double> userCompleteness = new HashMap<String, Double>();
        for (User user : assignedUsers) {
            userCompleteness.put(user.getName(), (double) user.getTotalUniqueLabelings());
        }

        for (String key: userCompleteness.keySet()){
            userCompleteness.put(key, userCompleteness.get(key) * 100.0 / this.instances.size());
        }

        HashMap<String, String> userCompletenessPercentage = new HashMap<String, String>();
        for (Map.Entry<String, Double> entry : userCompleteness.entrySet()) {
            userCompletenessPercentage.put(entry.getKey(), entry.getValue() + "%");
        }
        return userCompletenessPercentage;
    }

    public HashMap<String, String> getUserConsistencyPercentage() {
        //C- Dataset Performance Metrics: 6
        HashMap<String, String> userConsistencyPercentage = new HashMap<String, String>();
        for (User user : assignedUsers)
           userConsistencyPercentage.put(user.getName(), user.getConsistencyPercentage() + "%");

        return userConsistencyPercentage;
    }
}
