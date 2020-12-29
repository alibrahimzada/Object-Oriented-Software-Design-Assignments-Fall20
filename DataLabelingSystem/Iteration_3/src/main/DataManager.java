package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataManager {
    private DataLabelingSystem dataLabelingSystem;
    private DataUpdater dataUpdater;
    private List<User> users;
    private List<LabelAssignment> labelAssignments;
    private List<Dataset> datasets;
    
    public DataManager(DataLabelingSystem dataLabelingSystem) {
        this.dataLabelingSystem = dataLabelingSystem;
        this.dataUpdater = new DataUpdater(this);
        this.users = new ArrayList<User>();
        this.labelAssignments = new ArrayList<LabelAssignment>();
        this.datasets = new ArrayList<Dataset>();
    }

    public DataLabelingSystem getDataLabelingSystem() {
        return this.dataLabelingSystem;
    }

    public DataUpdater getDataUpdater() {
        return this.dataUpdater;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public User getUser(int userId) {
        User user = null;
        for (int i = 0; i < this.users.size(); i++) {
            if (this.users.get(i).getId() == userId) {
                user = this.users.get(i);
            }
        }
        return user;
    }

    public List<LabelAssignment> getLabelAssignments() {
        return this.labelAssignments;
    }

    public List<Dataset> getDatasets() {
        return this.datasets;
    }

    public Dataset getDataset(int datasetId) {
        Dataset dataset = null;
        for (int i = 0; i < this.datasets.size(); i++) {
            if (this.datasets.get(i).getId() == datasetId) {
                dataset = this.datasets.get(i);
            }
        }
        return dataset;
    }

    public void addLabelAssignment(LabelAssignment labelAssignment) {
        this.labelAssignments.add(labelAssignment);
    }

    public void addUsers(JSONArray users) {
        /*
            Iterates over the users JSONArray, pulls out the values of all the keys, 
            then creates an object of the class User and adds it to the attribute users
            if the user isAvailable. 
        */
        for (int i = 0; i < users.size(); i++) {
            Map<String, Object> currentUser = (HashMap<String, Object>) users.get(i);
            long userId = (long) currentUser.get("userId");
            String userName = (String) currentUser.get("userName");
			String userType = (String) currentUser.get("userType");
			String loginPassword = (String) currentUser.get("loginPassword");
			double consistencyProb = (double) currentUser.get("consistencyCheckProbability");
            long isAvailable = (long) currentUser.get("isAvailable");
            if (isAvailable == 1) {
                User userObject = new User((int) userId, userName, userType, loginPassword, consistencyProb);
                this.users.add(userObject);
                this.dataLabelingSystem.getSystemLog().getLogger().info(String.format("successfully added user %s with id %d", userName, userId));
            }
        }
    }

    private Map<String, Object> parseDatasetFile(Map<String, Object> currentDataset) {
        Map<String, Object> dataset = new HashMap<String, Object>();
        try {
            String datasetFilePath = (String) currentDataset.get("filePath");
            Object obj = new JSONParser().parse(new FileReader(datasetFilePath));
            JSONObject jsonObject = (JSONObject) obj;
            dataset = (HashMap<String, Object>) jsonObject;

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
            Map<String, Object> currentDataset = (HashMap<String, Object>) datasets.get(i);
            Map<String, Object> dataset = this.parseDatasetFile(currentDataset);

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

    public File[] getLabelAssignmentFiles(File labeledDataDirectory) {
        File[] files = labeledDataDirectory.listFiles(new FilenameFilter() { 
            public boolean accept(File dir, String filename){ 
                return filename.endsWith(".json");
            }
        });
        return files;
    }

    public void addLabelAssignments() {
        // this function adds the previous label assignments from different simulations
        String labeledDataDirectoryName = (String) this.dataLabelingSystem.getConfigurations().getLabeledDataDirectory();
        File labeledDataDirectory = new File(labeledDataDirectoryName);
        if (labeledDataDirectory.exists()) {
            File[] labelAssignmentFiles = this.getLabelAssignmentFiles(labeledDataDirectory);
            for (File file : labelAssignmentFiles) {
				String fileName = file.getName();
                try {
                    Object obj = new JSONParser().parse(new FileReader(labeledDataDirectoryName + "/" + fileName));
                    JSONObject jsonObject = (JSONObject) obj;
					Map<String, Object> datasetJSON = (HashMap<String, Object>) jsonObject;
					int currentDatasetId = this.dataLabelingSystem.getConfigurations().getCurrentDatasetId();
					Dataset dataset = this.getDataset(currentDatasetId);
					if (dataset.getId() != Integer.parseInt(fileName.substring(7, 8))) {
						continue;
					}

                    // from here, we create a LabelAssignment object for each labelAssignment in report and add it to data structures
					List<HashMap<String, Object>> labelAssignmentList = (ArrayList<HashMap<String, Object>>) datasetJSON.get("class label assignments");
                    for (HashMap<String, Object> labelAssignmentDetails : labelAssignmentList) {
                        int instanceId = ((Long) labelAssignmentDetails.get("instance id")).intValue();
                        JSONArray assignedLabelIds = (JSONArray) labelAssignmentDetails.get("class label ids");
                        int userId = ((Long) labelAssignmentDetails.get("user id")).intValue();
                        String dateTime = (String) labelAssignmentDetails.get("datetime");
                        double timeSpent = (Double) labelAssignmentDetails.get("time spent");
						User userObject = this.getUser(userId);
                        Instance instanceObject = dataset.getInstance(instanceId);
                        LabelAssignment labelAssignment = new LabelAssignment(userObject, instanceObject, dataset.getLabels(), new RandomLabelingMechanism());
                        labelAssignment.setAssignedLabels(assignedLabelIds);
                        labelAssignment.setDate(dateTime);
                        labelAssignment.setTimeSpent(timeSpent);
                        userObject.addLabelAssignment(labelAssignment);
                        instanceObject.addLabelAssignment(labelAssignment);
                        this.labelAssignments.add(labelAssignment);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    this.dataLabelingSystem.getSystemLog().getLogger().info(String.format("The %s is not in proper format", fileName));
                }
            }
        }
    }
}
