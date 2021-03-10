package ro.cloudSoft.cloudDoc.presentation.client.shared.model.project;

import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.project.TaskPriority;
import ro.cloudSoft.cloudDoc.domain.project.TaskStatus;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;

public class TaskModel {
	
	private Long id;
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	private Date finalizedDate;
	private TaskPriority priority;
	private String projectAbbreviation;
	private String comments;
	private Long projectId;
	private List<OrganizationEntityModel> taskAssignments;
	private List<TaskAttachmentModel> taskAttachments;
	private TaskStatus status;
	private Long inititatorId;
	private boolean permanent;
	private String participationsTo;
	private String explications;
	private Date evenimentStartDate;
	private Date evenimentEndDate;
	private Long subactivityId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Date getFinalizedDate() {
		return finalizedDate;
	}
	public void setFinalizedDate(Date finalizedDate) {
		this.finalizedDate = finalizedDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public TaskPriority getPriority() {
		return priority;
	}
	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}
	public String getProjectAbbreviation() {
		return projectAbbreviation;
	}
	public void setProjectAbbreviation(String projectAbbreviation) {
		this.projectAbbreviation = projectAbbreviation;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public List<OrganizationEntityModel> getTaskAssignments() {
		return taskAssignments;
	}
	public void setTaskAssignments(List<OrganizationEntityModel> taskAssignments) {
		this.taskAssignments = taskAssignments;
	}
	public List<TaskAttachmentModel> getTaskAttachments() {
		return taskAttachments;
	}
	public void setTaskAttachments(List<TaskAttachmentModel> taskAttachments) {
		this.taskAttachments = taskAttachments;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	public Long getInititatorId() {
		return inititatorId;
	}
	public void setInititatorId(Long inititatorId) {
		this.inititatorId = inititatorId;
	}
	public boolean isPermanent() {
		return permanent;
	}
	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}
	public String getParticipationsTo() {
		return participationsTo;
	}
	public void setParticipationsTo(String participationsTo) {
		this.participationsTo = participationsTo;
	}
	public String getExplications() {
		return explications;
	}
	public void setExplications(String explications) {
		this.explications = explications;
	}
	public Date getEvenimentStartDate() {
		return evenimentStartDate;
	}
	public void setEvenimentStartDate(Date evenimentStartDate) {
		this.evenimentStartDate = evenimentStartDate;
	}
	public Date getEvenimentEndDate() {
		return evenimentEndDate;
	}
	public void setEvenimentEndDate(Date evenimentEndDate) {
		this.evenimentEndDate = evenimentEndDate;
	}
	public Long getSubactivityId() {
		return subactivityId;
	}
	public void setSubactivityId(Long subactivityId) {
		this.subactivityId = subactivityId;
	}
}
