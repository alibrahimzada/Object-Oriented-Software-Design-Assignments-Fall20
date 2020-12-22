package main;

import tests.TestSuiteRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataLabelingSystem {
	// attributes of the DataLabelingSystem class
    private Log systemLog;
    private Map<String, Object> configurations;
    private DataManager dataManager;

	// constructor of the DataLabelingSystem class
    public DataLabelingSystem() {
        this.createSystemLog(); // calling this method upon the creation of a new object
        this.configurations = new HashMap<String, Object>();
        this.dataManager = new DataManager(this);
    }

	// this method creates a Log instance for the current simulation
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

	// this method returns the Log instance
    public Log getSystemLog() {
        return this.systemLog;
    }

	// this method parses the config.json file and stores its content in specific attributes
    public void parseConfigurations() {
        try {
            Object obj = new JSONParser().parse(new FileReader("config.json"));
            JSONObject jsonObject = (JSONObject) obj;
            this.configurations = (HashMap<String, Object>) jsonObject;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        this.systemLog.getLogger().info("successfully parsed software configurations");
    }

	// this method returns the configurations hashmap
    public Map<String, Object> getConfigurations() {
        return this.configurations;
    }

	// this method parses the user data from configurations and ask data manager to create them
    public void loadUsers() {
        JSONArray users = (JSONArray) this.configurations.get("users"); // getting the users info from the configurations
        this.dataManager.addUsers(users); //passing the userCount and users' info to addUsers to populate the attributes users
    }

	// this method parses the dataset data from configurations and ask data manager to create them
    public void loadDatasets() {
        JSONArray datasets = (JSONArray) this.configurations.get("datasets");
		this.dataManager.addDatasets(datasets);
    }

	// this method asks data manager to load previous label assignments from previous simulations
    public void loadLabelAssignments() {
		this.dataManager.addLabelAssignments();
		this.systemLog.getLogger().info("successfully loaded the previous label assignments");
    }

	// this method assign labels to the instances of the current dataset
    public void assignLabels() {
		// retrieve the current dataset object and consistency check probability
        int currentDatasetId = ((Long) this.configurations.get("currentDatasetId")).intValue();
        double consistencyCheckProbability = ((Double) this.configurations.get("consistencyCheckProbability"));
        Dataset dataset = this.dataManager.getDataset(currentDatasetId);
        List<User> assignedUsers = dataset.getAssignedUsers();
		
		// for each user assigned to current dataset
        for (User user : assignedUsers) {
			// for each instance available inside current dataset
            for (Instance instance : dataset.getInstances()) {

				// this condition is to make sure a user do not label instances more than once
				if (user.getUniqueInstances(dataset).contains(instance)) {
					continue;
				}

				// generate a random number [1, 100]
                Random random = new Random();
                int randomNumber = random.nextInt(100) + 1;
				
				// if the random number is smaller than consistency check probability * 100
				// and there is at least 1 label assignment done by this user, then show an
				// instance which was already labeled by this user, else show a new instance
                if (randomNumber <= consistencyCheckProbability*100 && user.getLabelAssignments().size() > 0) {
                    instance = user.getRandomInstance();
                }

				// assign label(s) to the instance and add it to the corresponding data structures
                LabelAssignment labelAssignment = new LabelAssignment(user, instance, dataset.getLabels(), new RandomLabelingMechanism());
                labelAssignment.assignLabels(dataset.getMaxLabel());
				this.dataManager.addLabelAssignment(labelAssignment);
				this.systemLog.getLogger().info(String.format("an instance with id=%d from dataset with id=%d has been labeled by a user with id=%d", instance.getId(), instance.getDataset().getId(), user.getId()));

                try {
					// update the output label assignments JSON as well as the report JSON
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

        system.loadUsers(); // create instances of the users available in config.json
        
        system.loadDatasets(); // create instances of datasets avaialble in config.json

        system.loadLabelAssignments(); // load previous label assignments, create their instances and add them to the data structures

        system.assignLabels(); // assigning labels to the instances of the dataset and adding the labelAssignments info to the arrayList labelAssignments
    }
}
