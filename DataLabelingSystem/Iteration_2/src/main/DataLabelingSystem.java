package main;

import tests.TestSuiteRunner;
import java.util.HashMap;

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

    public void assignLabels() {
        /*
            For every dataset, iterates through all the users. For every user, iterates through
            all the instances of that dataset. Then, creates an object of the LabelAssignment class,
            passing the user, the labels, the instance and the specified LabelingMechanism. 

            It also invokes the method assignLabels of the LabelAssignment object then stores all
            the labelAssignments in the arrayList labelAssignments. 
        */

        // for (int i = 0; i < this.datasets.size(); i++) {
        //     for (int j = 0; j < this.users.size(); j++) {
        //         ArrayList<Instance> instances = this.datasets.get(i).getInstances();
        //         ArrayList<Label> labels = this.datasets.get(i).getLabels();
        //         int maxLabel = this.datasets.get(i).getMaxLabel();
        //         for (int k = 0; k < instances.size(); k++) {
        //             LabelAssignment labelAssignment = new LabelAssignment(this.users.get(j), instances.get(k), labels, new RandomLabelingMechanism());
        //             labelAssignment.assignLabels(maxLabel);
        //             this.labelAssignments.add(labelAssignment);
        //             this.systemLog.getLogger().info(String.format("an instance with id %d from a dataset with id %d has been labeled by a user with id %d", instances.get(k).getId(), this.datasets.get(i).getId(), this.users.get(j).getId()));
        //         }
        //     }

        //     try {
        //         this.exportLabelAssignments(this.datasets.get(i));
        //         this.labelAssignments.clear();
        //     } catch (FileNotFoundException ex) {
        //         ex.printStackTrace();
        //         this.systemLog.getLogger().info("could not export the labeled dataset");
        //     }
        // }
    }

    public static void main(String[] args) {
        // if (!new TestSuiteRunner().runTests()) return; // run all unit tests, return if any not passed.

        DataLabelingSystem system = new DataLabelingSystem(); //creating a DataLabelingSystem object
        
        system.parseConfigurations(); // parsing the config.json file to populate the attribute configurations 

        system.loadUsers();
        
        system.loadDatasets();

        system.assignLabels(); // assigning labels to the instances of the dataset and adding the labelAssignments info to the arrayList labelAssignments
    }
}
