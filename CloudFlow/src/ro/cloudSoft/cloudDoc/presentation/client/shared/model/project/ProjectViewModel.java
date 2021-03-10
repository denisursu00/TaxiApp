package ro.cloudSoft.cloudDoc.presentation.client.shared.model.project;

import java.util.Date;
import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.domain.project.ProjectStatus;
import ro.cloudSoft.cloudDoc.domain.project.ProjectType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;

public class ProjectViewModel {
	
	private Long id;
	private String name;
	private String description;
	private Long initiatorId;
	private Long responsibleUserId;
	private String projectAbbreviation;
	private String documentId;
	private String documentLocationRealName;
	private List<OrganizationEntityModel> participants;
	private List<ProjectEstimationModel> estimations;
	private Date startDate;
	private Date endDate;
	private ProjectStatus status;
	private ProjectType type;
	private List<ProjectComisiiSauGlViewModel> comisiiSauGl;
	private Date implementationDate;
	private Integer importance;
	private List<SubactivityModel> subactivities;
	
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
	public Long getInitiatorId() {
		return initiatorId;
	}
	public void setInitiatorId(Long initiatorId) {
		this.initiatorId = initiatorId;
	}
	public Long getResponsibleUserId() {
		return responsibleUserId;
	}
	public void setResponsibleUserId(Long responsibleUserId) {
		this.responsibleUserId = responsibleUserId;
	}
	public String getProjectAbbreviation() {
		return projectAbbreviation;
	}
	public void setProjectAbbreviation(String projectAbbreviation) {
		this.projectAbbreviation = projectAbbreviation;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	public List<OrganizationEntityModel> getParticipants() {
		return participants;
	}
	public void setParticipants(List<OrganizationEntityModel> participants) {
		this.participants = participants;
	}
	public List<ProjectEstimationModel> getEstimations() {
		return estimations;
	}
	public void setEstimations(List<ProjectEstimationModel> estimations) {
		this.estimations = estimations;
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
	public ProjectStatus getStatus() {
		return status;
	}
	public void setStatus(ProjectStatus status) {
		this.status = status;
	}
	public ProjectType getType() {
		return type;
	}
	public void setType(ProjectType type) {
		this.type = type;
	}
	public List<ProjectComisiiSauGlViewModel> getComisiiSauGl() {
		return comisiiSauGl;
	}
	public void setComisiiSauGl(List<ProjectComisiiSauGlViewModel> comisiiSauGl) {
		this.comisiiSauGl = comisiiSauGl;
	}
	public Date getImplementationDate() {
		return implementationDate;
	}
	public void setImplementationDate(Date implementationDate) {
		this.implementationDate = implementationDate;
	}
	public Integer getImportance() {
		return importance;
	}
	public void setImportance(Integer importance) {
		this.importance = importance;
	}
	public List<SubactivityModel> getSubactivities() {
		return subactivities;
	}
	public void setSubactivities(List<SubactivityModel> subactivities) {
		this.subactivities = subactivities;
	}
}
