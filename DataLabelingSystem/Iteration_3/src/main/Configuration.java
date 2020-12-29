package main;

import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Configuration {
	private DataLabelingSystem dataLabelingSystem;
	private JSONArray users;
	private JSONArray datasets;
	private int currentDatasetId;
	private String labeledDataDirectory;
	private String reportDirectory;

	public Configuration(DataLabelingSystem dataLabelingSystem) {
		this.dataLabelingSystem = dataLabelingSystem;
	}

	// this method parses the config.json file and stores its content in specific attributes
	public void parseConfigurations() {
		try {
			Object obj = new JSONParser().parse(new FileReader("config.json"));
			JSONObject jsonObject = (JSONObject) obj;
			this.users = (JSONArray) jsonObject.get("users");
			this.datasets = (JSONArray) jsonObject.get("datasets");
			this.currentDatasetId = ((Long) jsonObject.get("currentDatasetId")).intValue();
			this.labeledDataDirectory = (String) jsonObject.get("labeledDataDirectory");
			this.reportDirectory = (String) jsonObject.get("reportDirectory");

		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		this.dataLabelingSystem.getSystemLog().getLogger().info("successfully parsed software configurations");
	}

	public JSONArray getUsers() {
		return this.users;
	}

	public JSONArray getDatasets() {
		return this.datasets;
	}

	public int getCurrentDatasetId() {
		return this.currentDatasetId;
	}

	public String getLabeledDataDirectory() {
		return this.labeledDataDirectory;
	}

	public String getReportDirectory() {
		return this.reportDirectory;
	}
}
