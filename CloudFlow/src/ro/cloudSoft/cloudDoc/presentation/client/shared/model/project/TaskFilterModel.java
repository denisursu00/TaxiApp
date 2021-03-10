package ro.cloudSoft.cloudDoc.presentation.client.shared.model.project;

import java.util.List;

public class TaskFilterModel {
	
	private String propertyName;
	private List<String> values;
	private String valueType;
	private String matchMode;
	private String aplicability;
	
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
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public String getMatchMode() {
		return matchMode;
	}
	public void setMatchMode(String matchMode) {
		this.matchMode = matchMode;
	}
	public String getAplicability() {
		return aplicability;
	}
	public void setAplicability(String aplicability) {
		this.aplicability = aplicability;
	}
}