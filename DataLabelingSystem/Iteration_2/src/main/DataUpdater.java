package main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import com.google.gson.GsonBuilder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;

public class DataUpdater {
    private DataManager dataManager;

    public DataUpdater(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void updateLabelAssignments(Dataset currentDataset) throws Exception {
        String outputDirectory = (String) this.dataManager.getDataLabelingSystem().getConfigurations().get("labeledDataDirectory");
        Map<String, Object> linkedHM = new LinkedHashMap<String, Object>();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        linkedHM.put("dataset id", currentDataset.getId());
        linkedHM.put("dataset name", currentDataset.getName());
        linkedHM.put("maximum number of labels per instance", currentDataset.getMaxLabel());

        JSONArray classLabels = new JSONArray();
        for (int i = 0; i < currentDataset.getLabels().size(); i++) {
            Map label = new LinkedHashMap(2);
            label.put("label id", currentDataset.getLabels().get(i).getId());
            label.put("label text", currentDataset.getLabels().get(i).getText());
            classLabels.add(label);
        }
        linkedHM.put("class labels", classLabels);

        JSONArray instances = new JSONArray();
        for (int i = 0; i < currentDataset.getInstances().size(); i++) {
            Map instance = new LinkedHashMap(2);
            instance.put("id", currentDataset.getInstances().get(i).getId());
            instance.put("instance", currentDataset.getInstances().get(i).getText());
            instances.add(instance);
        }
        linkedHM.put("instances", instances);

        JSONArray labelAssignments = new JSONArray();
        for (int i = 0; i < this.dataManager.getLabelAssignments().size(); i++) {
            Map assignmentMap = new LinkedHashMap(5);
            LabelAssignment currentLabelAssignment = this.dataManager.getLabelAssignments().get(i);
            assignmentMap.put("instance id", currentLabelAssignment.getInstance().getId());
            ArrayList<Integer> assignedLabelsId = new ArrayList<Integer>();
            for (int j = 0; j < currentLabelAssignment.getAssignedLabels().size(); j++) {
                assignedLabelsId.add(currentLabelAssignment.getAssignedLabels().get(j).getId());
            }
            assignmentMap.put("class label ids", assignedLabelsId);
            assignmentMap.put("user id", currentLabelAssignment.getUser().getId());
            assignmentMap.put("datetime", currentLabelAssignment.getDate().toString());
            assignmentMap.put("time spent", currentLabelAssignment.getTimeSpent());
            labelAssignments.add(assignmentMap);
        }
        linkedHM.put("class label assignments", labelAssignments);

        JSONArray users = new JSONArray();
        for (int i = 0; i < this.dataManager.getUsers().size(); i++) {
            Map userMap = new LinkedHashMap<>(3);
            userMap.put("user id", this.dataManager.getUsers().get(i).getId());
            userMap.put("user name", this.dataManager.getUsers().get(i).getName());
            userMap.put("user type", this.dataManager.getUsers().get(i).getType());
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
        String reportDirectory = (String) this.dataManager.getDataLabelingSystem().getConfigurations().get("reportDirectory");

        File reportdirectory = new File(reportDirectory);
        Map<String, Object> linkedHM = new LinkedHashMap<String, Object>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (!reportdirectory.exists()) {
            reportdirectory.mkdir();

            // the rest of this if condition will create a report file with default values

            // user related reports
            Map<String, Map<String, Object>> reportUsers = new LinkedHashMap<String, Map<String, Object>>();
            for (User user : this.dataManager.getUsers()) {
                Map<String, Object> userMetrics = new LinkedHashMap<String, Object>();
                // userMetrics.put("number of assigned datasets", 0);
                Map<String, String> datasetCompleteness = new LinkedHashMap<String, String>();
                for (int i = 0; i < user.getTotalDatasets(); i++) {
                    datasetCompleteness.put("dataset" + user.getAssignedDatasets().get(i).getId(), "0%");
                    userMetrics.put("number of assigned datasets", i+1);
                }
                userMetrics.put("datasets completeness %", datasetCompleteness);
                userMetrics.put("total instances labeled", 0);
                userMetrics.put("total unique instances labeled", 0);
                userMetrics.put("consistency percentage", "0%");
                userMetrics.put("average spent time", 0);
                userMetrics.put("std. dev. spent time", 0);
                reportUsers.put("user" + user.getId(), userMetrics);
            }
            linkedHM.put("userReport", reportUsers);

            // instance related reports
            Map<String, Object> reportInstances = new LinkedHashMap<String, Object>();
            for (Dataset dataset : this.dataManager.getDatasets()) {
                Map<String, Object> instancesDataset = new LinkedHashMap<String, Object>();
                for (Instance instance : dataset.getInstances()) {
                    Map<String, Object> instanceMetrics = new LinkedHashMap<String, Object>();
                    instanceMetrics.put("total label assignments", 0);
                    instanceMetrics.put("total unique label assignments", 0);
                    instanceMetrics.put("total unique users", 0);
                    instanceMetrics.put("most frequent label", "X 0%");
                    Map<String, Double> classLabels = new LinkedHashMap<String, Double>();
                    for (Label label : dataset.getLabels()) {
                        classLabels.put(label.getText(), 0.0);
                    }
                    instanceMetrics.put("class labels and %", classLabels);
                    instanceMetrics.put("entropy", 0);
                    instancesDataset.put("instance" + instance.getId(), instanceMetrics);
                }
                reportInstances.put("dataset"+dataset.getId(), instancesDataset);
            }
            linkedHM.put("instanceReport", reportInstances);

            // dataset related reports
            Map<String, Object> reportDatasets = new LinkedHashMap<String, Object>();
            for (Dataset dataset : this.dataManager.getDatasets()) {
                Map<String, Object> datasetMetrics = new LinkedHashMap<String, Object>();
                datasetMetrics.put("completeness %", "0%");
                Map<String, Double> classDist = new LinkedHashMap<String, Double>();
                Map<String, Integer> uniqueInstancesCount = new LinkedHashMap<String, Integer>();
                for (Label label : dataset.getLabels()) {
                    classDist.put(label.getText(), 0.0);
                    uniqueInstancesCount.put(label.getText(), 0);
                }
                datasetMetrics.put("class label distribution", classDist);
                datasetMetrics.put("unique instances count", uniqueInstancesCount);
                datasetMetrics.put("total assigned users", dataset.getTotalAssignedUsers());
                Map<String, String> userCompleteness = new LinkedHashMap<String, String>();
                Map<String, String> userConsistency = new LinkedHashMap<String, String>();
                for (User user : dataset.getAssignedUsers()) {
                    userConsistency.put("user" + user.getId(), "0%");
                    userCompleteness.put("user" + user.getId(), "0%");
                }
                datasetMetrics.put("assigned users completeness %", userCompleteness);
                datasetMetrics.put("assigned users consistency %", userConsistency);
                reportDatasets.put("dataset" + dataset.getId(), datasetMetrics);
            }
            linkedHM.put("datasetReport", reportDatasets);

            String json = gson.toJson(linkedHM);
            PrintWriter pw = new PrintWriter(String.format("%s/report.json", reportDirectory));
            pw.write(json);
            pw.flush();
            pw.close();
        }

        Object obj = new JSONParser().parse(new FileReader("reports/report.json"));
        JSONObject jsonObject = (JSONObject) obj;
        linkedHM = (Map) jsonObject;

        // updating user metrics
        Map<String, Object> userMap = (Map) linkedHM.get("userReport");
        for (User user : currentDataset.getAssignedUsers()) {
            Map<String, Object> userMetrics = (Map<String, Object>) userMap.get("user" + user.getId());
            Map<String, String> datasetCompleteness = (Map<String, String>) userMetrics.get("datasets completeness %");
            datasetCompleteness.put("dataset" + currentDataset.getId(), user.getCompletenessPercentage(currentDataset));
            userMetrics.put("total instances labeled", user.getTotalLabelings());
            userMetrics.put("total unique instances labeled", user.getTotalUniqueLabelings());
            userMetrics.put("consistency percentage", user.getConsistencyPercentage());
            userMetrics.put("average spent time", user.getAverageTime());
            userMetrics.put("std. dev. spent time", user.getStdDevTime());
        }

        // updating instance metrics
        Map<String, Object> instanceMap = (Map) ((Map) linkedHM.get("instanceReport")).get("dataset" + currentDataset.getId());
        for (Instance instance : currentDataset.getInstances()) {
            Map<String, Object> instanceMetrics = (Map) instanceMap.get("instance" + instance.getId());
            instanceMetrics.put("total label assignments", instance.getTotalLabelAssignments());
            instanceMetrics.put("total unique label assignments", instance.getUniqueLabelAssignments());
            instanceMetrics.put("total unique users", instance.getUniqueUsers());
            instanceMetrics.put("most frequent label", instance.getFrequentLabel());
            instanceMetrics.put("class labels and %", instance.getClassLabelDistribution());
            instanceMetrics.put("entropy", instance.getEntropy());
        }

        // updating dataset metrics
        Map<String, Object> datasetMetrics = (Map) ((Map) linkedHM.get("datasetReport")).get("dataset" + currentDataset.getId());
        datasetMetrics.put("completeness %", currentDataset.getCompleteness());
        datasetMetrics.put("class label distribution", currentDataset.getDistribution(this.dataManager.getLabelAssignments()));
        datasetMetrics.put("unique instances count", currentDataset.getUniqueInstances(this.dataManager.getLabelAssignments()));
        datasetMetrics.put("total assigned users", currentDataset.getTotalAssignedUsers());
        datasetMetrics.put("assigned users completeness %", currentDataset.getUserCompletenessPercentange());
        datasetMetrics.put("assigned users consistency %", currentDataset.getUserConsistencyPercentage());

        String json = gson.toJson(linkedHM);
        PrintWriter pw = new PrintWriter(String.format("%s/report.json", reportDirectory));
        pw.write(json);
        pw.flush();
        pw.close();
    }
}
