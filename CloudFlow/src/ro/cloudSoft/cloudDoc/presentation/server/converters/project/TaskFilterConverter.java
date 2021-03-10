package ro.cloudSoft.cloudDoc.presentation.server.converters.project;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.project.TaskFilter;
import ro.cloudSoft.cloudDoc.domain.project.TaskFilterMatchMode;
import ro.cloudSoft.cloudDoc.domain.project.TaskFilterApplicability;
import ro.cloudSoft.cloudDoc.domain.project.TaskFilterValueType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskFilterModel;

public class TaskFilterConverter {
	
	public TaskFilter toTaskFilter(TaskFilterModel model) {
		TaskFilter filter = new TaskFilter();
		filter.setPropertyName(model.getPropertyName());
		filter.setValues(model.getValues());
		filter.setValueType(TaskFilterValueType.valueOf(model.getValueType()));
		filter.setMatchMode(TaskFilterMatchMode.valueOf(model.getMatchMode()));
		filter.setAplicability(TaskFilterApplicability.valueOf(model.getAplicability()));
		return filter;
	}
	
	public List<TaskFilter> toTaskFilters(List<TaskFilterModel> models) {
		List<TaskFilter> filters = new ArrayList<>();
		for (TaskFilterModel model : models) {
			filters.add(toTaskFilter(model));
		}
		return filters;
	}
}
