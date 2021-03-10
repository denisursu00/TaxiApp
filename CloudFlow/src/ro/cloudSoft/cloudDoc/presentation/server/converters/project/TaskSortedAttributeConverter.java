package ro.cloudSoft.cloudDoc.presentation.server.converters.project;

import ro.cloudSoft.cloudDoc.domain.project.SortedTaskAttribute;
import ro.cloudSoft.cloudDoc.domain.project.SortedTaskAttributeOrderDirection;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.SortedTaskAttributeModel;

public class TaskSortedAttributeConverter {
	
	public SortedTaskAttribute toTaskSortedAttribute(SortedTaskAttributeModel model) {
		SortedTaskAttribute taskSortedAttribute = new SortedTaskAttribute();
		taskSortedAttribute.setPropertyName(model.getPropertyName());
		taskSortedAttribute.setOrder(SortedTaskAttributeOrderDirection.valueOf(model.getOrder()));
		return taskSortedAttribute;
	}
}
