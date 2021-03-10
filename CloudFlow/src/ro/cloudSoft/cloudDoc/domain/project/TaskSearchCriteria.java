package ro.cloudSoft.cloudDoc.domain.project;

import java.util.List;

public class TaskSearchCriteria {
	
	private Long projectId;
	private Long userId;
	private List<TaskFilter> filters;
	private SortedTaskAttribute sortedAttribute;
	
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<TaskFilter> getFilters() {
		return filters;
	}
	public void setFilters(List<TaskFilter> filters) {
		this.filters = filters;
	}
	public SortedTaskAttribute getSortedAttribute() {
		return sortedAttribute;
	}
	public void setSortedAttribute(SortedTaskAttribute sortedAttribute) {
		this.sortedAttribute = sortedAttribute;
	}
}
