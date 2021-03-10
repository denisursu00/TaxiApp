package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.SortedTaskAttributeModel;

public class GetPagedProjectTaskViewModelsRequestModel {
	
	private Long projectId;
	private List<TaskFilterModel> filters;
	private SortedTaskAttributeModel sortedAttribute;
	
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public List<TaskFilterModel> getFilters() {
		return filters;
	}
	public void setFilters(List<TaskFilterModel> filters) {
		this.filters = filters;
	}
	public SortedTaskAttributeModel getSortedAttribute() {
		return sortedAttribute;
	}
	public void setSortedAttribute(SortedTaskAttributeModel sortedAttribute) {
		this.sortedAttribute = sortedAttribute;
	}
}
