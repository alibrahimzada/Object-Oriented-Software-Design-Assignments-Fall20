package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class User {

    // attributes of User class
    private int id;
    private String name;
	private String type;
	private String password;
	private double consistencyProb;
    private List<Dataset> assignedDatasets;
    private List<LabelAssignment> labelAssignments;

    // constructor of User class
    public User(int id, String name, String type, String loginPassword, double consistencyProb) {
        this.id = id;
        this.name = name;
		this.type = type;
		this.password = loginPassword;
		this.consistencyProb = consistencyProb;
        this.assignedDatasets = new ArrayList<Dataset>();
        this.labelAssignments = new ArrayList<LabelAssignment>();
    }

    // adds a given dataset to the list of assigned datasets
    public void addAssignedDataset(Dataset dataset) {
        this.assignedDatasets.add(dataset);
    }

    // returns a list of assigned dataset objects assigned to this user
    public List<Dataset> getAssignedDatasets() {
        return this.assignedDatasets;
    }

    // adds a given label assignment to the list of label assignments
    public void addLabelAssignment(LabelAssignment labelAssignment) {
        this.labelAssignments.add(labelAssignment);
    }

    // returns a list of label assignment objects labeled by this user
    public List<LabelAssignment> getLabelAssignments() {
        return this.labelAssignments;
    }

    // returns a random instance object from the label assignment list
    public Instance getRandomInstance() {
        Random random = new Random();
        int randomIndex = random.nextInt(this.getTotalLabelings());
        return this.labelAssignments.get(randomIndex).getInstance();
	}
	
	// returns a list of unique instances labeled by this user
	public List<Instance> getUniqueInstances(Dataset dataset) {
		List<Instance> instances = new ArrayList<Instance>();

        // loop over each label assignment from this user
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            if (!instances.contains(labelAssignment.getInstance()) && labelAssignment.getInstance().getDataset() == dataset) {
                instances.add(labelAssignment.getInstance());
            }
        }

        // return the list
        return instances;
	}

    // returns id of this user
    public int getId() {
        return this.id;
    }

    // returns name of this user
    public String getName() {
        return this.name;
    }

    // returns type of this user
    public String getType() {
        return this.type;
	}

	// returns password of this user
	public String getPassword() {
		return this.password;
	}
	
	public double getConsistencyProb() {
		return this.consistencyProb;
	}

    // returns total number of datasets assigned to this user
    public int getTotalDatasets() {
        return this.assignedDatasets.size();
    }

    // returns completeness percentage of this user from a given dataset
    public Map<String, Double> getCompletenessPercentage() {
		Map<String, Double> datasetCompletenessPercentage = new LinkedHashMap<String, Double>();
		for (Dataset dataset : this.assignedDatasets) {
			double completeness = (this.getUniqueInstances(dataset).size() * 100.0) / dataset.getInstances().size();
			datasetCompletenessPercentage.put("dataset" + dataset.getId(), completeness);
		}

        return datasetCompletenessPercentage;
    }

    // returns total number of labelings done by this user so far
    public int getTotalLabelings() {
        return this.labelAssignments.size();
    }

    // returns total number of unique labelings done by this user so far
    public int getTotalUniqueLabelings() {
		int totalUniqueLabelings = 0;

		// loop over each assigned dataset
		for (Dataset dataset : this.assignedDatasets) {
			totalUniqueLabelings += this.getUniqueInstances(dataset).size();
		}

        // return the length of the list
        return totalUniqueLabelings;
    }

    // returns the consistency percentage of this user from all its labelings so far
    public double getConsistencyPercentage() {
		Map<Instance, List<List<Label>>> instanceFrequency = new HashMap<Instance, List<List<Label>>>();

        for (LabelAssignment labelAssignment : this.labelAssignments) {
            if (!instanceFrequency.containsKey(labelAssignment.getInstance())) {
                instanceFrequency.put(labelAssignment.getInstance(), new ArrayList<>());
            }
            instanceFrequency.get(labelAssignment.getInstance()).add(labelAssignment.getAssignedLabels());
		}

		int totalRecurrentLabelings = 0;
		int totalConsistentRecurrentLabelings = 0;

		for (Map.Entry<Instance, List<List<Label>>> entry : instanceFrequency.entrySet()) {
			if (entry.getValue().size() > 1) {
				Set<List<Label>> uniqueLabels = new HashSet<List<Label>>(entry.getValue());
				if (uniqueLabels.size() == 1) {
					totalConsistentRecurrentLabelings++;
				}
				totalRecurrentLabelings++;
			}			
		}

        if (totalRecurrentLabelings == 0) {
            return 0.0;
        }
		return totalConsistentRecurrentLabelings * 100.0 / totalRecurrentLabelings;
    }

    // returns the average time spent by this user labeling all its instances
    public double getAverageTime() {
        // this variable will be accumulated with the time spent for all label assignments
        double totalTimeSpent = 0;

        // loop over each label assignment instance, and get their time spent in seconds
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            totalTimeSpent += labelAssignment.getTimeSpent();
        }

        // calculate the average time spent by this user
        if (totalTimeSpent == 0) {
            return 0.0;
        }
        return totalTimeSpent / this.getTotalLabelings();
    }

    // returns the standard deviation of time spent by this user labeling all its instances
    public double getStdDevTime() {
        // this variable will be accumulated with the squared difference of time spent and average time
        double squaredDifference = 0;
        double averageTime = this.getAverageTime();

        // loop over each label assignment instance, get their spent time, and calculate their squared difference with average
        for (LabelAssignment labelAssignment : this.labelAssignments) {
            squaredDifference += Math.pow(labelAssignment.getTimeSpent() - averageTime, 2);
        }

        // calculate the standard deviation of time spent by this user
        if (squaredDifference == 0) {
            return 0.0;
        }
        return Math.sqrt(squaredDifference / this.getTotalLabelings());
    }
}
