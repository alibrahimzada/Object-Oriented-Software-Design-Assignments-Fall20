package main;

import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;

public class Dataset {
    // attributes of the Dataset class
    private Integer id;
    private String name;
    private Integer maxLabel;
    private ArrayList<Instance> instances;
    private ArrayList<Label> labels;
    private ArrayList<User> assignedUsers;

    // constructor of the Dataset class
    public Dataset(Integer id, String name, Integer maxLabel) {
        this.id = id;
        this.name = name;
        this.maxLabel = maxLabel;
        this.instances = new ArrayList<Instance>();
        this.labels = new ArrayList<Label>();
        this.assignedUsers = new ArrayList<User>();
    }

    // creates instance objects and add them to the corresponding data structures
    public void addInstances(JSONArray instances) {
        for (int i = 0; i < instances.size(); i++) {
            HashMap<String, Object> currentInstance = (HashMap<String, Object>) instances.get(i);
            long instanceId = (long) currentInstance.get("id");
            String instanceText = (String) currentInstance.get("instance");
            Instance instanceObject = new Instance((int) instanceId, instanceText);
            instanceObject.setDataset(this);
            this.instances.add(instanceObject);
        }
    }

    // creates label objects and add them to the corresponding data structures
    public void addLabels(JSONArray labels) {
        for (int i = 0; i < labels.size(); i++) {
            HashMap<String, Object> currentLabel = (HashMap<String, Object>) labels.get(i);
            long labelId = (long) currentLabel.get("label id");
            String labelText = (String) currentLabel.get("label text");
            Label labelObject = new Label((int) labelId, labelText);
            labelObject.setDataset(this);
            this.labels.add(labelObject);
        }
    }

    // add the given user to the assigned users list
    public void addAssignedUser(User user) {
        this.assignedUsers.add(user);
    }

    // returns the id of this dataset
    public Integer getId() {
        return this.id;
    }

    // returns the name of this dataset
    public String getName() {
        return this.name;
    }

    // returns the max label of this dataset
    public Integer getMaxLabel() {
        return this.maxLabel;
    }

    // returns the instance list of this dataset
    public ArrayList<Instance> getInstances() {
        return this.instances;
    }

    // returns an Instance object given the instance id
    public Instance getInstance(Integer instanceId) {
        Instance instance = null;
        for (Instance currentInstance : this.instances) {
            if (currentInstance.getId() == instanceId) {
                instance = currentInstance;
            }
        }
        return instance;
    }

    // returns the label list list of this dataset
    public ArrayList<Label> getLabels() {
        return this.labels;
    }

    // returns the assigned user list of this dataset
    public ArrayList<User> getAssignedUsers() {
        return this.assignedUsers;
    }

    // returns the completeness percentage of this dataset
    public String getCompleteness() {
        int instanceLabeledCount = 0;
        for (Instance instance : this.instances) {
            if (instance.getTotalLabelAssignments() >= 1) {
                instanceLabeledCount++;
            }
        }
        return String.format("%.2f", instanceLabeledCount * 100.0 / instances.size())+ "%";
    }

    // returns the final distribution of labels in this dataset
    public HashMap<String, Double> getDistribution(ArrayList<LabelAssignment> labelAssignments) {
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

    // returns the unique instances labeled from this dataset
    public HashMap<String, Integer> getUniqueInstances(ArrayList<LabelAssignment> labelAssignments) {
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

    // returns the total number of assigned users to this dataset
    public int getTotalAssignedUsers() {
        return this.assignedUsers.size();
    }

    // returns the completeness percentage of each user from this dataset
    public HashMap<String, String> getUserCompletenessPercentange() {
        int totalLabelings = 0;
        // first find out the total number of labelings done by assigned users in this dataset
        HashMap<String, Double> userCompleteness = new HashMap<String, Double>();
        for (User user : this.assignedUsers) {
            for (LabelAssignment labelAssignment : user.getLabelAssignments()) {
                if (labelAssignment.getInstance().getDataset() == this) {
                    Double currentCount = userCompleteness.get(user.getName());
                    userCompleteness.put(user.getName(), (currentCount == null) ? 1.0 : currentCount + 1);
                    totalLabelings++;
                }
            }
        }

        // then normalize the total counts
        for (String key: userCompleteness.keySet()) {
            userCompleteness.put(key, userCompleteness.get(key) / totalLabelings * 100.0);
        }

        // convert the normalized scores into percentage string
        HashMap<String, String> userCompletenessPercentage = new HashMap<String, String>();
        for (Map.Entry<String, Double> entry : userCompleteness.entrySet()) {
            userCompletenessPercentage.put(entry.getKey(), String.format("%.2f", entry.getValue()) + "%");
        }

        return userCompletenessPercentage;
    }

    // returns the consistency percentage of each user from this dataset
    public HashMap<String, Object> getUserConsistencyPercentage() {
        HashMap<String, Object> labelingsFreq = new HashMap<String, Object>();

        // first start by counting the number of times a unique instance was labeled assigned users of this dataset
        for (User user : this.assignedUsers) {
            for (LabelAssignment labelAssignment : user.getLabelAssignments()) {
                if (labelAssignment.getInstance().getDataset() == this) {
                    HashMap<String, Object> userLabelingsFreq = new HashMap<String, Object>();
                    String currentInstanceText = labelAssignment.getInstance().getText();
                    if (userLabelingsFreq.containsKey(currentInstanceText)) {
                        HashMap<String, Object> value = (HashMap) userLabelingsFreq.get(currentInstanceText);
                        int currentCount = (int) value.get("currentCount");
                        int totalReccurent = (int) value.get("totalRecurrent");
                        value.put("currentCount", currentCount + 1);
                        value.put("totalRecurrent", totalReccurent + 1);
                        labelingsFreq.put(currentInstanceText, value);
                    } else {
                        HashMap<String, Object> value = new HashMap<String, Object>() {{
                            put("currentCount", 1);
                            put("totalRecurrent", 0);
                            put("object", labelAssignment);
                        }};
                        userLabelingsFreq.put(currentInstanceText, value);
                    }
                    labelingsFreq.put("user" + user.getId(), userLabelingsFreq);
                }
            }
        }

        // count all of the reccurrent instances which were labeled the same by assigned users in this dataset
        HashMap<String, Object> recurrentLabelings = new HashMap<String, Object>();
        for (User user : this.assignedUsers) {
            for (LabelAssignment labelAssignment : user.getLabelAssignments()) {
                if (labelAssignment.getInstance().getDataset() == this) {
                    String currentInstanceText = labelAssignment.getInstance().getText();
                    HashMap<String, Object> value = (HashMap) ((HashMap) labelingsFreq.get("user" + user.getId())).get(currentInstanceText);
                    if ((int) value.get("currentCount") > 1 && ((LabelAssignment) value.get("object")).getAssignedLabels() == labelAssignment.getAssignedLabels()) {
                        Double currentCount = (Double) recurrentLabelings.get("user" + user.getId());
                        recurrentLabelings.put("user" + user.getId(), (currentCount == null) ? 1.0 : currentCount + 1);
                    }    
                }
            }

            int totalRecurrents = 0;
            HashMap<String, Object> userLabelingFreq = (HashMap) labelingsFreq.get("user" + user.getId());
            for (Map.Entry<String, Object> entry : userLabelingFreq.entrySet()) {
                totalRecurrents += (int) ((HashMap) entry.getValue()).get("totalRecurrent");
            }

            if (recurrentLabelings.containsKey("user" + user.getId())) {
                double sameLabelings = (double) recurrentLabelings.get("user" + user.getId());
                if (totalRecurrents == 0) {
                    recurrentLabelings.put("user" + user.getId(), "0.00%");
                }
                recurrentLabelings.put("user" + user.getId(), String.format("%.2f", sameLabelings * 100 / totalRecurrents) + "%");
            }
        }

        return recurrentLabelings;
    }
}
