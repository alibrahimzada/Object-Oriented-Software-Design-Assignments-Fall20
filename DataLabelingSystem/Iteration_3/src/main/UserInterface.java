package main;

import java.util.Scanner;

import tests.TestSuiteRunner;

public class UserInterface {
	private DataLabelingSystem dataLabelingSystem;

	public UserInterface() {
		this.dataLabelingSystem = new DataLabelingSystem(this);
	}

	// this method initiates the process of loading metadata
	private void initiateSystem() {
		this.dataLabelingSystem.getConfigurations().parseConfigurations();
		this.dataLabelingSystem.loadUsers();
		this.dataLabelingSystem.loadDatasets();
		this.dataLabelingSystem.loadLabelAssignments();
	}

	// this method will handle the login processes
	private void login() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("Please enter your username: ");
			String userName = sc.nextLine();			
			System.out.println("Please enter your password: ");
			String userPassword = sc.nextLine();

			// this condition will check if username and password are blank
			if (userName.isBlank() && userPassword.isBlank()) {
				// if so, the assigned bots will label the current dataset and the program terminates
				this.dataLabelingSystem.assignLabels(false);
				System.exit(0);
			// this condition will check if the entered username and password are valid
			} else if (this.dataLabelingSystem.authorizeLogin(userName, userPassword)) {
				System.out.println("Login successful! Welcome " + userName);
				break;
			}
			System.out.println("Failed to login! Please try again");
		}
	}

	private void labelInstances() {
		this.dataLabelingSystem.assignLabels(true);
		System.out.println("Bots/Humans finished labeling their assigned datasets. Goodbye!");
	}

	public String[] getUserSelections(Instance instance) {
		System.out.println(String.format("\nInstance %d: %s", instance.getId(), instance.getText()));
		System.out.println("\nPossible Labels:");
		for (Label label : instance.getDataset().getLabels()) {
			System.out.println(label.getId() + " - " + label.getText());
		}
		int maxLabel = instance.getDataset().getMaxLabel();
		System.out.println("\nPlease enter your selected label(s)' number separated by space. This instance can have at most " + maxLabel + " label(s)");
		Scanner sc = new Scanner(System.in);
		String userSelections = sc.nextLine();
		String[] userSelectionsArray = userSelections.split("\\s+");
		while (userSelectionsArray.length > maxLabel) {
			System.out.println("Invalid selection! Please try again.");
			userSelections = sc.nextLine();
			userSelectionsArray = userSelections.split("\\s+");
		}
		return userSelectionsArray;  // split user selections by whitespace
	}

	public static void main(String[] args) {
		if (!new TestSuiteRunner().runTests()) return; // run all unit tests, return if any not passed.

		UserInterface userInterface = new UserInterface();
		
		userInterface.initiateSystem();

		userInterface.login();

		userInterface.labelInstances();
	}
}
