package ro.cloudSoft.cloudDoc.presentation.client.shared.model.project;

import java.util.List;

import ro.cloudSoft.cloudDoc.services.project.ProjectRealizationDegree;

public class ProjectWithDspViewModel {
	
	private Long id;
	private String name;
	private String responsibleUserName;
	private ProjectRealizationDegree degreeOfAchievement;
	private String importanceDegreeColor;
	private List<String> subprojects;
	
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
	public String getResponsibleUserName() {
		return responsibleUserName;
	}
	public void setResponsibleUserName(String responsibleUserName) {
		this.responsibleUserName = responsibleUserName;
	}
	public ProjectRealizationDegree getDegreeOfAchievement() {
		return degreeOfAchievement;
	}
	public void setDegreeOfAchievement(ProjectRealizationDegree degreeOfAchievement) {
		this.degreeOfAchievement = degreeOfAchievement;
	}
	public String getImportanceDegreeColor() {
		return importanceDegreeColor;
	}
	public void setImportanceDegreeColor(String importanceDegreeColor) {
		this.importanceDegreeColor = importanceDegreeColor;
	}
	public List<String> getSubprojects() {
		return subprojects;
	}
	public void setSubprojects(List<String> subprojects) {
		this.subprojects = subprojects;
	}
}
