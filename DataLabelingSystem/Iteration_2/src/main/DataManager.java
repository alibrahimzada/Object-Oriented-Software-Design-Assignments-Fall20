package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileReader;

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
            long userId = (long) currentUser.get("user id");
            String userName = (String) currentUser.get("user name");
            String userType = (String) currentUser.get("user type");
            long isAvailable = (long) currentUser.get("is available");
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
            String datasetFilePath = (String) currentDataset.get("file path");
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
                if (i == this.users.get(j).getId()) {
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
            JSONArray assignedUsers = (JSONArray) currentDataset.get("users");
            
            Dataset datasetObject = new Dataset((int) datasetId, datasetName, (int) maxLabel);

            this.datasets.add(datasetObject);
            datasetObject.addLabels(labels);  
            datasetObject.addInstances(instances);
            this.addAssignedUsers(assignedUsers, datasetObject);
            this.dataLabelingSystem.getSystemLog().getLogger().info(String.format("successfully added a dataset %s with id=%d", datasetName, datasetId));
        }
    }

    public void exportLabelAssignments(Dataset dataset) throws FileNotFoundException {
        // String outputDirectory = (String) this.dataLabelingSystem.getConfigurations().get("labeled data directory");
        // Map<String, Object> linkedHM = new LinkedHashMap<String, Object>();

        // Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // linkedHM.put("dataset id", dataset.getId());
        // linkedHM.put("dataset name", dataset.getName());
        // linkedHM.put("maximum number of labels per instance", dataset.getMaxLabel());

        // JSONArray classLabels = new JSONArray();
        // for (int i = 0; i < dataset.getLabels().size(); i++) {
        //     Map label = new LinkedHashMap(2);
        //     label.put("label id", dataset.getLabels().get(i).getId());
        //     label.put("label text", dataset.getLabels().get(i).getText());
        //     classLabels.add(label);
        // }
        // linkedHM.put("class labels", classLabels);

        // JSONArray instances = new JSONArray();
        // for (int i = 0; i < dataset.getInstances().size(); i++) {
        //     Map instance = new LinkedHashMap(2);
        //     instance.put("id", dataset.getInstances().get(i).getId());
        //     instance.put("instance", dataset.getInstances().get(i).getText());
        //     instances.add(instance);
        // }
        // linkedHM.put("instances", instances);

        // JSONArray labelAssignments = new JSONArray();
        // for (int i = 0; i < this.labelAssignments.size(); i++) {
        //     Map assignmentMap = new LinkedHashMap(4);
        //     LabelAssignment currentLabelAssignment = this.labelAssignments.get(i);
        //     assignmentMap.put("instance id", currentLabelAssignment.getInstance().getId());
        //     ArrayList<Integer> assignedLabelsId = new ArrayList<Integer>();
        //     for (int j = 0; j < currentLabelAssignment.getAssignedLabels().size(); j++) {
        //         assignedLabelsId.add(currentLabelAssignment.getAssignedLabels().get(j).getId());
        //     }
        //     assignmentMap.put("class label ids", assignedLabelsId);
        //     assignmentMap.put("user id", currentLabelAssignment.getUser().getId());
        //     assignmentMap.put("datetime", currentLabelAssignment.getDate().toString());
        //     labelAssignments.add(assignmentMap);
        // }
        // linkedHM.put("class label assignments", labelAssignments);

        // JSONArray users = new JSONArray();
        // for (int i = 0; i < this.users.size(); i++) {
        //     Map userMap = new LinkedHashMap<>(3);
        //     userMap.put("user id", this.users.get(i).getId());
        //     userMap.put("user name", this.users.get(i).getName());
        //     userMap.put("user type", this.users.get(i).getType());
        //     users.add(userMap);
        // }
        // linkedHM.put("users", users);
        // String json = gson.toJson(linkedHM);

        // File directory = new File(outputDirectory);
        // if (!directory.exists()) {
        //     directory.mkdir();
        // }

        // PrintWriter pw = new PrintWriter(String.format("%s/dataset%d-output.json", outputDirectory, dataset.getId()));
        // pw.write(json);
          
        // pw.flush(); 
        // pw.close();

        // this.dataLabelingSystem.getSystemLog().getLogger().info(String.format("the labels for dataset %d have been assigned, and the JSON file has been created", dataset.getId()));
    }

}
