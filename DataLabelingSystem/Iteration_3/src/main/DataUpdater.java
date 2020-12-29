package main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.io.File;
import java.io.PrintWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataUpdater {
    private DataManager dataManager;

    public DataUpdater(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void updateLabelAssignments(Dataset currentDataset) throws Exception {
        String outputDirectory = (String) this.dataManager.getDataLabelingSystem().getConfigurations().getLabeledDataDirectory();
        Map<String, Object> linkedHM = new LinkedHashMap<String, Object>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        linkedHM.put("dataset id", currentDataset.getId());
        linkedHM.put("dataset name", currentDataset.getName());
        linkedHM.put("maximum number of labels per instance", currentDataset.getMaxLabel());

        List<Map<String, Object>> classLabels = new ArrayList<>();
        for (Label label : currentDataset.getLabels()) {
            Map<String, Object> labelMap = new LinkedHashMap<String, Object>(2);
            labelMap.put("label id", label.getId());
            labelMap.put("label text", label.getText());
            classLabels.add(labelMap);
        }
        linkedHM.put("class labels", classLabels);

        List<Map<String, Object>> instances = new ArrayList<>();
        for (Instance instance : currentDataset.getInstances()) {
            Map<String, Object> instanceMap = new LinkedHashMap<String, Object>(2);
            instanceMap.put("id", instance.getId());
            instanceMap.put("instance", instance.getText());
            instances.add(instanceMap);
        }
        linkedHM.put("instances", instances);

        List<Map<String, Object>> labelAssignments = new ArrayList<Map<String, Object>>();
        for (LabelAssignment labelAssignment : this.dataManager.getLabelAssignments()) {
            Map<String, Object> assignmentMap = new LinkedHashMap<String, Object>(5);
            assignmentMap.put("instance id", labelAssignment.getInstance().getId());
            ArrayList<Integer> assignedLabelsId = new ArrayList<Integer>();
            for (Label label : labelAssignment.getAssignedLabels()) {
                assignedLabelsId.add(label.getId());
			}
			assignmentMap.put("class label ids", assignedLabelsId);
			assignmentMap.put("final label (most frequent)", labelAssignment.getInstance().getFinalLabel().getText());
            assignmentMap.put("user id", labelAssignment.getUser().getId());
            assignmentMap.put("datetime", labelAssignment.getDate().toString());
            assignmentMap.put("time spent", labelAssignment.getTimeSpent());
            labelAssignments.add(assignmentMap);
        }
        linkedHM.put("class label assignments", labelAssignments);

        List<Map<String, Object>> users = new ArrayList<Map<String, Object>>();
        for (User user : currentDataset.getAssignedUsers()) {
            Map<String, Object> userMap = new LinkedHashMap<String, Object>(3);
            userMap.put("user id", user.getId());
            userMap.put("user name", user.getName());
            userMap.put("user type", user.getType());
            users.add(userMap);
        }
        linkedHM.put("users", users);

        String json = gson.toJson(linkedHM);
        File directory = new File(outputDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }

        PrintWriter pw = new PrintWriter(String.format("%s/dataset%d-output.json", outputDirectory, currentDataset.getId()));
        pw.write(json);
        pw.flush();
        pw.close();
    }

    public void updateReport(Dataset currentDataset) throws Exception {
        String reportDirectory = (String) this.dataManager.getDataLabelingSystem().getConfigurations().getReportDirectory();
		Map<String, Object> linkedHM = new LinkedHashMap<String, Object>();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // updating user metrics
        Map<String, Object> userMap = new LinkedHashMap<String, Object>();
        for (User user : currentDataset.getAssignedUsers()) {
			Map<String, Object> userMetrics = new LinkedHashMap<String, Object>();
			userMetrics.put("number of assigned datasets", user.getTotalDatasets());
            userMetrics.put("dataset completeness %", user.getCompletenessPercentage());
            userMetrics.put("total instances labeled", user.getTotalLabelings());
            userMetrics.put("total unique instances labeled", user.getTotalUniqueLabelings());
            userMetrics.put("consistency percentage", user.getConsistencyPercentage());
            userMetrics.put("average spent time", user.getAverageTime());
			userMetrics.put("std. dev. spent time", user.getStdDevTime());
			userMap.put("user" + user.getId(), userMetrics);
		}
		linkedHM.put("userReport", userMap);

        // updating instance metrics
        Map<String, Object> instanceMap = new LinkedHashMap<String, Object>();
        for (Instance instance : currentDataset.getInstances()) {
			Map<String, Object> instanceMetrics = new LinkedHashMap<String, Object>();
            instanceMetrics.put("total label assignments", instance.getTotalLabelAssignments());
            instanceMetrics.put("total unique label assignments", instance.getUniqueLabelAssignments());
            instanceMetrics.put("total unique users", instance.getUniqueUsers());
            instanceMetrics.put("most frequent label", instance.getFrequentLabelPercentage());
            instanceMetrics.put("class labels and %", instance.getClassLabelDistribution());
			instanceMetrics.put("entropy", instance.getEntropy());
			instanceMap.put("instance" + instance.getId(), instanceMetrics);
		}
		linkedHM.put("instanceReport", instanceMap);

        // updating dataset metrics
        Map<String, Object> datasetMetrics = new LinkedHashMap<String, Object>();
        datasetMetrics.put("completeness %", currentDataset.getCompleteness());
        datasetMetrics.put("class label distribution", currentDataset.getDistribution(this.dataManager.getLabelAssignments()));
        datasetMetrics.put("unique instances count", currentDataset.getUniqueInstances(this.dataManager.getLabelAssignments()));
        datasetMetrics.put("total assigned users", currentDataset.getTotalAssignedUsers());
        datasetMetrics.put("assigned users completeness %", currentDataset.getUserCompletenessPercentange());
        datasetMetrics.put("assigned users consistency %", currentDataset.getUserConsistencyPercentage());
		linkedHM.put("dataset" + currentDataset.getId() + " Report",  datasetMetrics);

		String json = gson.toJson(linkedHM);
		File reportdirectory = new File(reportDirectory);
		if (!reportdirectory.exists()) {
			reportdirectory.mkdir();
		}

        PrintWriter pw = new PrintWriter(String.format("%s/dataset%d-report.json", reportDirectory, currentDataset.getId()));
        pw.write(json);
        pw.flush();
        pw.close();
    }
}
