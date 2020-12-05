package main;

import tests.TestSuiteRunner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class DataLabelingSystem {
    private Log systemLog; // Declaration of an object of type Log 
    private HashMap<String, Object> configurations;
    private ArrayList<User> users;
    private ArrayList<LabelAssignment> labelAssignments;
    private ArrayList<Dataset> datasets;

    public DataLabelingSystem() {
        this.createLog(); // calling this method upon the creation of a new object
        this.configurations = new HashMap<String, Object>();
        this.users = new ArrayList<User>();
        this.labelAssignments = new ArrayList<LabelAssignment>();
        this.datasets = new ArrayList<Dataset>();
    }

    private void createLog() {
        /*
            Initializes the object "systemLog" of type Log, gets the logger, 
            then sets the level of the logger to INFO.
        */
        try {
            this.systemLog = new Log(); 
            this.systemLog.getLogger().setLevel(Level.INFO);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        this.systemLog.getLogger().info("successfully created logger for this simulation");
    }

    public void parseConfigurations() {
        /*
            Parses the config.json file, converts the json object into 
            a hashmap, then assigns it to the attribute configurations. 
        */
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jsonObject = (JSONObject) obj;
            this.configurations = (HashMap) jsonObject;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        this.systemLog.getLogger().info("successfully parsed software configurations");
    }

    public void addUsers(int userCount, JSONArray users) {
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
                this.systemLog.getLogger().info(String.format("successfully added user %s with id %d", userName, userId));
            }
        }
    }

    public File[] getDatasetFiles(String directoryName) {
        File dir = new File(directoryName);
        File[] files = dir.listFiles(new FilenameFilter() { 
            public boolean accept(File dir, String filename){ 
                return filename.endsWith(".json"); 
            }
        });
        Arrays.sort(files);
        return files;
    }

    public void parseDatasets(File[] datasetNames, String datasetDirectory) {
        /*
            Parses each dataset file in the given directory. Casts the JSON object
            as a hashmap, then passes it to the method add datasets. 
        */
        for (File file : datasetNames) {
            String fileName = file.getName();
            try {
                Object obj = new JSONParser().parse(new FileReader(datasetDirectory + "/" + fileName));
                JSONObject jsonObject = (JSONObject) obj;
                HashMap<String, Object> dataset = (HashMap) jsonObject;
                this.addDataset(dataset);
            } catch (Exception ex) {
                ex.printStackTrace();
                this.systemLog.getLogger().info(String.format("The %s is not in proper format", fileName));
            }
        }
    } 

    public void addDataset(HashMap<String, Object> dataset) {
        /*
            Given a dataset in a hashmap, this method extracts the information from the 
            hashmap, and uses them to create a dataset object. Then, stores the dataset
            object in the attriute datasets.
        */
        long datasetId = (long) dataset.get("dataset id");
        String datasetName = (String) dataset.get("dataset name");
        long maxLabel = (long) dataset.get("maximum number of labels per instance");
        JSONArray labels = (JSONArray) dataset.get("class labels");
        JSONArray instances = (JSONArray) dataset.get("instances");
        Dataset datasetObject = new Dataset((int) datasetId, datasetName, (int) maxLabel);
        this.datasets.add(datasetObject);
        datasetObject.addLabels(labels);  
        datasetObject.addInstances(instances);
        this.systemLog.getLogger().info(String.format("successfully added a dataset %s with id=%d", datasetName, datasetId));
    }

    public void assignLabels() {
        /*
            For every dataset, iterates through all the users. For every user, iterates through
            all the instances of that dataset. Then, creates an object of the LabelAssignment class,
            passing the user, the labels, the instance and the specified LabelingMechanism. 

            It also invokes the method assignLabels of the LabelAssignment object then stores all
            the labelAssignments in the arrayList labelAssignments. 
        */
        for (int i = 0; i < this.datasets.size(); i++) {
            for (int j = 0; j < this.users.size(); j++) {
                ArrayList<Instance> instances = this.datasets.get(i).getInstances();
                ArrayList<Label> labels = this.datasets.get(i).getLabels();
                int maxLabel = this.datasets.get(i).getMaxLabel();
                for (int k = 0; k < instances.size(); k++) {
                    LabelAssignment labelAssignment = new LabelAssignment(this.users.get(j), instances.get(k), labels, new RandomLabelingMechanism());
                    labelAssignment.assignLabels(maxLabel);
                    this.labelAssignments.add(labelAssignment);
                    this.systemLog.getLogger().info(String.format("an instance with id %d from a dataset with id %d has been labeled by a user with id %d", instances.get(k).getId(), this.datasets.get(i).getId(), this.users.get(j).getId()));
                }
            }

            try {
                this.exportLabelAssignments(this.datasets.get(i));
                this.labelAssignments.clear();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                this.systemLog.getLogger().info("could not export the labeled dataset");
            }
        }
    }

    public void exportLabelAssignments(Dataset dataset) throws FileNotFoundException {
        String outputDirectory = (String) this.configurations.get("labeled data directory");
        Map<String, Object> linkedHM = new LinkedHashMap<String, Object>();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        linkedHM.put("dataset id", dataset.getId());
        linkedHM.put("dataset name", dataset.getName());
        linkedHM.put("maximum number of labels per instance", dataset.getMaxLabel());

        JSONArray classLabels = new JSONArray();
        for (int i = 0; i < dataset.getLabels().size(); i++) {
            Map label = new LinkedHashMap(2);
            label.put("label id", dataset.getLabels().get(i).getId());
            label.put("label text", dataset.getLabels().get(i).getText());
            classLabels.add(label);
        }
        linkedHM.put("class labels", classLabels);

        JSONArray instances = new JSONArray();
        for (int i = 0; i < dataset.getInstances().size(); i++) {
            Map instance = new LinkedHashMap(2);
            instance.put("id", dataset.getInstances().get(i).getId());
            instance.put("instance", dataset.getInstances().get(i).getText());
            instances.add(instance);
        }
        linkedHM.put("instances", instances);

        JSONArray labelAssignments = new JSONArray();
        for (int i = 0; i < this.labelAssignments.size(); i++) {
            Map assignmentMap = new LinkedHashMap(4);
            LabelAssignment currentLabelAssignment = this.labelAssignments.get(i);
            assignmentMap.put("instance id", currentLabelAssignment.getInstance().getId());
            ArrayList<Integer> assignedLabelsId = new ArrayList<Integer>();
            for (int j = 0; j < currentLabelAssignment.getAssignedLabels().size(); j++) {
                assignedLabelsId.add(currentLabelAssignment.getAssignedLabels().get(j).getId());
            }
            assignmentMap.put("class label ids", assignedLabelsId);
            assignmentMap.put("user id", currentLabelAssignment.getUser().getId());
            assignmentMap.put("datetime", currentLabelAssignment.getDate().toString());
            labelAssignments.add(assignmentMap);
        }
        linkedHM.put("class label assignments", labelAssignments);

        JSONArray users = new JSONArray();
        for (int i = 0; i < this.users.size(); i++) {
            Map userMap = new LinkedHashMap<>(3);
            userMap.put("user id", this.users.get(i).getId());
            userMap.put("user name", this.users.get(i).getName());
            userMap.put("user type", this.users.get(i).getType());
            users.add(userMap);
        }
        linkedHM.put("users", users);
        String json = gson.toJson(linkedHM);

        File directory = new File(outputDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }

        PrintWriter pw = new PrintWriter(String.format("%s/dataset%d-output.json", outputDirectory, dataset.getId()));
        pw.write(json);
          
        pw.flush(); 
        pw.close();

        this.systemLog.getLogger().info(String.format("the labels for dataset %d have been assigned, and the JSON file has been created", dataset.getId()));
    }

    public static void main(String[] args) {
        if (!new TestSuiteRunner().runTests()) return; // run all unit tests, return if any not passed.

        DataLabelingSystem system = new DataLabelingSystem(); //creating a DataLabelingSystem object
        
        system.parseConfigurations(); // parsing the config.json file to populate the attribute configurations 

        long userCount = (long) system.configurations.get("number of users"); // getting the #of users from the configurations 
        JSONArray users = (JSONArray) system.configurations.get("users"); // getting the users info from the configurations
        system.addUsers((int) userCount, users); //passing the userCount and users' info to addUsers to populate the attributes users
        
        String datasetDirectory = (String) system.configurations.get("dataset directory"); // getting the datasets' directory's name from the configurations 
        File[] datasetNames = system.getDatasetFiles(datasetDirectory); // creating an array of File type containing the datasets
        system.parseDatasets(datasetNames, datasetDirectory);  // pasing the dataNames array and the directory of the datasets info to parse the datasets 

        system.assignLabels(); // assigning labels to the instances of the dataset and adding the labelAssignments info to the arrayList labelAssignments
    }
}
