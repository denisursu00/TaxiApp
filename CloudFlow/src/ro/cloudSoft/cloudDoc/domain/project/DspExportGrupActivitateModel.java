package ro.cloudSoft.cloudDoc.domain.project;

import java.util.List;

public class DspExportGrupActivitateModel {
	private String subactivityName;
	private List<DspExportActivitateModel> activities;
	public String getSubactivityName() {
		return subactivityName;
	}
	public void setSubactivityName(String subactivityName) {
		this.subactivityName = subactivityName;
	}
	public List<DspExportActivitateModel> getActivities() {
		return activities;
	}
	public void setActivities(List<DspExportActivitateModel> activities) {
		this.activities = activities;
	}
	
}
