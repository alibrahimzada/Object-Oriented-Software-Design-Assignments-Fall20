package main;

import tests.TestSuiteRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataLabelingSystem {
    private Log systemLog; // Declaration of an object of type Log 
    private HashMap<String, Object> configurations;
    private DataManager dataManager;

    public DataLabelingSystem() {
        this.createSystemLog(); // calling this method upon the creation of a new object
        this.configurations = new HashMap<String, Object>();
        this.dataManager = new DataManager(this);
    }

    private void createSystemLog() {
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

    public Log getSystemLog() {
        return this.systemLog;
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

    public HashMap<String, Object> getConfigurations() {
        return this.configurations;
    }

    public void loadUsers() {
        JSONArray users = (JSONArray) this.configurations.get("users"); // getting the users info from the configurations
        this.dataManager.addUsers(users); //passing the userCount and users' info to addUsers to populate the attributes users
    }

    public void loadDatasets() {
        JSONArray datasets = (JSONArray) this.configurations.get("datasets");
		this.dataManager.addDatasets(datasets);
    }

    public void loadLabelAssignments() {
		this.dataManager.addLabelAssignments();
		this.systemLog.getLogger().info("successfully loaded the previous label assignments");
    }

    public void assignLabels() {
        int currentDatasetId = ((Long) this.configurations.get("currentDatasetId")).intValue();
        double consistencyCheckProbability = ((Double) this.configurations.get("consistencyCheckProbability"));
        Dataset dataset = this.dataManager.getDataset(currentDatasetId);
        ArrayList<User> assignedUsers = dataset.getAssignedUsers();
        
        for (User user : assignedUsers) {
            for (Instance instance : dataset.getInstances()) {
                Random random = new Random();
                int randomNumber = random.nextInt(100) + 1;

                if (randomNumber <= consistencyCheckProbability*100 && user.getLabelAssignments().size() > 0) {
                    instance = user.getRandomInstance();
                }

                LabelAssignment labelAssignment = new LabelAssignment(user, instance, dataset.getLabels(), new RandomLabelingMechanism());
                labelAssignment.assignLabels(dataset.getMaxLabel());
				this.dataManager.addLabelAssignment(labelAssignment);
				this.systemLog.getLogger().info(String.format("an instance with id=%d from dataset with id=%d has been labeled by a user with id=%d", instance.getId(), instance.getDataset().getId(), user.getId()));

                try {
					this.dataManager.getDataUpdater().updateLabelAssignments(dataset);
					this.systemLog.getLogger().info("successfully updated the label assignments file");
					this.dataManager.getDataUpdater().updateReport(dataset);
					this.systemLog.getLogger().info("successfully updated the report");
                } catch (Exception ex) {
                    ex.printStackTrace();
					this.systemLog.getLogger().info("could not update the files due to an error");
                }
            }
        }
    }

    public static void main(String[] args) {
        // if (!new TestSuiteRunner().runTests()) return; // run all unit tests, return if any not passed.

        DataLabelingSystem system = new DataLabelingSystem(); //creating a DataLabelingSystem object
        
        system.parseConfigurations(); // parsing the config.json file to populate the attribute configurations 

        system.loadUsers();
        
        system.loadDatasets();

        system.loadLabelAssignments();

        system.assignLabels(); // assigning labels to the instances of the dataset and adding the labelAssignments info to the arrayList labelAssignments
    }
}
