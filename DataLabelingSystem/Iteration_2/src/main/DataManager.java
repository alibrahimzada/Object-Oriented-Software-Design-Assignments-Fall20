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

    public void exportLabelAssignments() {

    }

}
