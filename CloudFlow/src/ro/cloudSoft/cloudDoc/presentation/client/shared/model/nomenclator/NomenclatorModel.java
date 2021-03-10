package ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator;

import java.util.List;

public class NomenclatorModel {
	
	private Long id;
	private String name;
	private String description;
	private List<NomenclatorAttributeModel> attributes;
	private List<String> uiAttributeNames;
	
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
	public List<NomenclatorAttributeModel> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<NomenclatorAttributeModel> attributes) {
		this.attributes = attributes;
	}
	public List<String> getUiAttributeNames() {
		return uiAttributeNames;
	}
	public void setUiAttributeNames(List<String> uiAttributeNames) {
		this.uiAttributeNames = uiAttributeNames;
	}
}
