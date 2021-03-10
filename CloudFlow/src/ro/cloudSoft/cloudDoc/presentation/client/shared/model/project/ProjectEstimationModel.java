package ro.cloudSoft.cloudDoc.presentation.client.shared.model.project;

import java.util.Date;

public class ProjectEstimationModel {
	
	private Long id;
	private Integer estimationInPercent;
	private Date startDate;
	private Date endDate;
	private Long projectId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getEstimationInPercent() {
		return estimationInPercent;
	}
	public void setEstimationInPercent(Integer estimationInPercent) {
		this.estimationInPercent = estimationInPercent;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
}
