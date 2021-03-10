package ro.cloudSoft.cloudDoc.domain.project;

import java.util.List;

public class TaskFilter {
	
	private String propertyName;
	private List<String> values;
	private TaskFilterValueType valueType;
	private TaskFilterMatchMode matchMode;
	private TaskFilterApplicability aplicability;
	
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public TaskFilterValueType getValueType() {
		return valueType;
	}
	public void setValueType(TaskFilterValueType valueType) {
		this.valueType = valueType;
	}
	public TaskFilterMatchMode getMatchMode() {
		return matchMode;
	}
	public void setMatchMode(TaskFilterMatchMode matchMode) {
		this.matchMode = matchMode;
	}
	public TaskFilterApplicability getAplicability() {
		return aplicability;
	}
	public void setAplicability(TaskFilterApplicability aplicability) {
		this.aplicability = aplicability;
	}
}