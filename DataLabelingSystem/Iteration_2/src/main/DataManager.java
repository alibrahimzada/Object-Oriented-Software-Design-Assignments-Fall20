package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class DataManager {
    private DataLabelingSystem dataLabelingSystem;
    private ArrayList<User> users;
    private ArrayList<LabelAssignment> labelAssignments;
    private ArrayList<Dataset> datasets;
    
    public DataManager(DataLabelingSystem dataLabelingSystem) {
        this.dataLabelingSystem = dataLabelingSystem;
        this.users = new ArrayList<User>();
        this.labelAssignments = new ArrayList<LabelAssignment>();
        this.datasets = new ArrayList<Dataset>();
    }

    public void addUsers(JSONArray users) {
        /*
            Iterates over the users JSONArray, pulls out the values of all the keys, 
            then creates an object of the class User and adds it to the attribute users
            if the user isAvailable. 
        */
        for (int i = 0; i < users.size(); i++) {
            HashMap<String, Object> currentUser = (HashMap) users.get(i);
            long userId = (long) currentUser.get("userId");
            String userName = (String) currentUser.get("userName");
            String userType = (String) currentUser.get("userType");
            long isAvailable = (long) currentUser.get("isAvailable");
            if (isAvailable == 1) {
                User userObject = new User((int) userId, userName, userType);
                this.users.add(userObject);
                this.dataLabelingSystem.getSystemLog().getLogger().info(String.format("successfully added user %s with id %d", userName, userId));
            }
        }
    }

    private HashMap<String, Object> parseDatasetFile(HashMap<String, Object> currentDataset) {
        HashMap<String, Object> dataset = new HashMap<String, Object>();
        try {
            String datasetFilePath = (String) currentDataset.get("filePath");
            Object obj = new JSONParser().parse(new FileReader(datasetFilePath));
            JSONObject jsonObject = (JSONObject) obj;
            dataset = (HashMap) jsonObject;

        } catch (Exception ex) {
            ex.printStackTrace();
            this.dataLabelingSystem.getSystemLog().getLogger().info(String.format("The dataset is not in proper format"));
        }

        return dataset;
    }

    private void addAssignedUsers(JSONArray assignedUsers, Dataset dataset) {
        for (int i = 0; i < assignedUsers.size(); i++) {
            for (int j = 0; j < this.users.size(); j++) {
                if (((Long) assignedUsers.get(i)).intValue() == this.users.get(j).getId()) {
                    this.users.get(j).addAssignedDataset(dataset);
                    dataset.addAssignedUser(this.users.get(j));
                }
            }
        }
    }

    public void addDatasets(JSONArray datasets) {
        /*
            Parses each dataset file in the given directory. Casts the JSON object
            as a hashmap, then passes it to the method add datasets. 
        */
        for (int i = 0; i < datasets.size(); i++) {
            HashMap<String, Object> currentDataset = (HashMap) datasets.get(i);
            HashMap<String, Object> dataset = this.parseDatasetFile(currentDataset);

            long datasetId = (long) dataset.get("dataset id");
            String datasetName = (String) dataset.get("dataset name");
            long maxLabel = (long) dataset.get("maximum number of labels per instance");
            JSONArray labels = (JSONArray) dataset.get("class labels");
            JSONArray instances = (JSONArray) dataset.get("instances");
            JSONArray assignedUsers = (JSONArray) currentDataset.get("assignedUsers");
            
            Dataset datasetObject = new Dataset((int) datasetId, datasetName, (int) maxLabel);

            this.datasets.add(datasetObject);
            datasetObject.addLabels(labels);  
            datasetObject.addInstances(instances);
            this.addAssignedUsers(assignedUsers, datasetObject);
            this.dataLabelingSystem.getSystemLog().getLogger().info(String.format("successfully added a dataset %s with id=%d", datasetName, datasetId));
        }
    }

    public ArrayList<Dataset> getDatasets() {
        return this.datasets;
    }

    public Dataset getDataset(int id) {
        Dataset dataset = null;
        for (int i = 0; i < this.datasets.size(); i++) {
            if (this.datasets.get(i).getId() == id) {
                dataset = this.datasets.get(i);
            }
        }
        return dataset;
    }

    public void addLabelAssignment(LabelAssignment labelAssignment) {
        this.labelAssignments.add(labelAssignment);
    }

    public void updateReport() throws SecurityException, IOException {
        String reportDirectory = (String) this.dataLabelingSystem.getConfigurations().get("reportDirectory");

        File reportdirectory = new File(reportDirectory);
        if (!reportdirectory.exists()) {
            reportdirectory.mkdir();
            Map<String, Object> linkedHM = new LinkedHashMap<String, Object>();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // the rest of this if condition will create a report file with default values

            // user related reports
            Map<String, Map<String, Object>> reportUsers = new LinkedHashMap<String, Map<String, Object>>();
            for (User user : this.users) {
                Map<String, Object> userMetrics = new LinkedHashMap<String, Object>();
                userMetrics.put("number of assigned datasets", 0);
                Map<String, String> datasetCompleteness = new LinkedHashMap<String, String>();
                for (Dataset dataset : user.getAssignedDatasets()) {
                    datasetCompleteness.put("dataset" + dataset.getId(), "0%");
                }
                userMetrics.put("datasets completeness %", datasetCompleteness);
                userMetrics.put("total instances labeled", 0);
                userMetrics.put("total unique instances labeled", 0);
                userMetrics.put("consistency percentage", "0%");
                userMetrics.put("average spent time", 0);
                userMetrics.put("std. dev. spent time", 0);
                reportUsers.put("user" + user.getId(), userMetrics);
            }
            linkedHM.put("userReport", reportUsers);

            // instance related reports
            Map<String, Object> reportInstances = new LinkedHashMap<String, Object>();
            for (Dataset dataset : this.datasets) {
                Map<String, Object> instancesDataset = new LinkedHashMap<String, Object>();
                for (Instance instance : dataset.getInstances()) {
                    Map<String, Object> instanceMetrics = new LinkedHashMap<String, Object>();
                    instanceMetrics.put("total label assignments", 0);
                    instanceMetrics.put("total unique label assignments", 0);
                    instanceMetrics.put("total unique users", 0);
                    instanceMetrics.put("most frequent label", "X 0%");
                    Map<String, String> classLabels = new LinkedHashMap<String, String>();
                    for (Label label : dataset.getLabels()) {
                        classLabels.put(label.getText(), "0%");
                    }
                    instanceMetrics.put("class labels and %", classLabels);
                    instanceMetrics.put("entropy", 0);
                    instancesDataset.put("instance" + instance.getId(), instanceMetrics);
                }
                reportInstances.put("dataset"+dataset.getId(), instancesDataset);
            }
            linkedHM.put("instanceReport", reportInstances);

            // dataset related reports
            Map<String, Object> reportDatasets = new LinkedHashMap<String, Object>();
            for (Dataset dataset : this.datasets) {
                Map<String, Object> datasetMetrics = new LinkedHashMap<String, Object>();
                datasetMetrics.put("completeness %", "0%");
                Map<String, String> classDist = new LinkedHashMap<String, String>();
                for (Label label : dataset.getLabels()) {
                    classDist.put(label.getText(), "0%");
                }
                datasetMetrics.put("class label distribution", classDist);

                datasetMetrics.put("total assigned users", 0);
                Map<String, String> userCompleteness = new LinkedHashMap<String, String>();
                Map<String, String> userConsistency = new LinkedHashMap<String, String>();
                for (User user : dataset.getAssignedUsers()) {
                    userConsistency.put("user" + user.getId(), "0%");
                    userCompleteness.put("user" + user.getId(), "0%");
                }
                datasetMetrics.put("assigned users completeness %", userCompleteness);
                datasetMetrics.put("assigned users consistency %", userConsistency);
                reportDatasets.put("dataset" + dataset.getId(), datasetMetrics);
            }
            linkedHM.put("datasetReport", reportDatasets);

            String json = gson.toJson(linkedHM);
            PrintWriter pw = new PrintWriter(String.format("%s/report.json", reportDirectory));
            pw.write(json);
            pw.flush();
            pw.close();
        }
    }

    public void updateLabelAssignments() {

    }
}
