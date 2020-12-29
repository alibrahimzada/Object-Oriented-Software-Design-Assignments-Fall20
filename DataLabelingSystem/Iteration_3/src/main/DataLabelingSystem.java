package main;

import java.util.Random;
import java.util.logging.Level;

public class DataLabelingSystem {
	// attributes of the DataLabelingSystem class
	private Log systemLog;
	private UserInterface userInterface;
    private Configuration configurations;
	private DataManager dataManager;
	private User currentUser;
	private Dataset currentDataset;

	// constructor of the DataLabelingSystem class
    public DataLabelingSystem(UserInterface userInterface) {
		this.createSystemLog(); // calling this method upon the creation of a new object
		this.userInterface = userInterface;
        this.configurations = new Configuration(this);
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

	// this method returns the configurations hashmap
    public Configuration getConfigurations() {
        return this.configurations;
    }

	// this method parses the user data from configurations and ask data manager to create them
    public void loadUsers() {
        this.dataManager.addUsers(this.configurations.getUsers()); //passing the userCount and users' info to addUsers to populate the attributes users
    }

	// this method parses the dataset data from configurations and ask data manager to create them
    public void loadDatasets() {
		this.dataManager.addDatasets(this.configurations.getDatasets());
		this.currentDataset = this.dataManager.getDataset(this.configurations.getCurrentDatasetId());
    }

	// this method asks data manager to load previous label assignments from previous simulations
    public void loadLabelAssignments() {
		this.dataManager.addLabelAssignments();
		this.systemLog.getLogger().info("successfully loaded the previous label assignments");
	}
	
	// this method authorizes user login
	public boolean authorizeLogin(String userName, String userPassword) {
		for (User user : this.dataManager.getUsers()) {
			if (user.getName().equals(userName) && user.getPassword().equals(userPassword)) {
				this.currentUser = user;
				this.systemLog.getLogger().info(user.getName() + " has successfully logged in.");
				return true;
			}
		}
		return false;
	}

	// this method assign labels to the instances of the current dataset
    public void assignLabels() {
		// for each user assigned to current dataset
        for (User user : this.currentDataset.getAssignedUsers()) {
			// for each instance available inside current dataset
            for (Instance instance : this.currentDataset.getInstances()) {

				// this condition is to make sure a user do not label instances more than once
				if (user.getUniqueInstances(this.currentDataset).contains(instance)) {
					continue;
				}

				// generate a random number [1, 100]
                Random random = new Random();
                int randomNumber = random.nextInt(100) + 1;
				
				// if the random number is smaller than consistency check probability * 100
				// and there is at least 1 label assignment done by this user, then show an
				// instance which was already labeled by this user, else show a new instance
                if (randomNumber <= user.getConsistencyProb()*100 && user.getLabelAssignments().size() > 0) {
                    instance = user.getRandomInstance();
                }

				// assign label(s) to the instance and add it to the corresponding data structures
				LabelAssignment labelAssignment = null;
				if (user.getType().equals("RandomBot")) {
					labelAssignment = new LabelAssignment(user, instance, this.currentDataset.getLabels(), new RandomLabelingMechanism());
				} else if (user.getType().equals("Human")) {
					String[] userSelections = this.userInterface.getUserSelections(instance);
					ManualLabelingMechanism manualLabelingMechanism = new ManualLabelingMechanism();
					manualLabelingMechanism.setUserSelections(userSelections);
					labelAssignment = new LabelAssignment(user, instance, this.currentDataset.getLabels(), manualLabelingMechanism);
				} else if (user.getType().equals("RuleBasedBot")) {
					labelAssignment = new LabelAssignment(user, instance, this.currentDataset.getLabels(), new RuleBasedLabelingMechanism());
				}
				labelAssignment.assignLabels(this.currentDataset.getMaxLabel());
				this.dataManager.addLabelAssignment(labelAssignment);
				this.systemLog.getLogger().info(String.format("an instance with id=%d from dataset with id=%d has been labeled by a user with id=%d", instance.getId(), instance.getDataset().getId(), user.getId()));

                try {
					// update the output label assignments JSON as well as the report JSON
					this.dataManager.getDataUpdater().updateLabelAssignments(this.currentDataset);
					this.systemLog.getLogger().info("successfully updated the label assignments file");
					this.dataManager.getDataUpdater().updateReport(this.currentDataset);
					this.systemLog.getLogger().info("successfully updated the report");
                } catch (Exception ex) {
                    ex.printStackTrace();
					this.systemLog.getLogger().info("could not update the files due to an error");
                }
            }
        }
    }
}
