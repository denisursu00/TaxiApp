package ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator;

import java.util.Map;

public class CustomNomenclatorSelectionFiltersRequestModel {
	
	private Long nomenclatorId;
	private Long attributeId;
	private Map<String, String> attributeValueByKey;
	
	public Long getNomenclatorId() {
		return nomenclatorId;
	}
	
	public void setNomenclatorId(Long nomenclatorId) {
		this.nomenclatorId = nomenclatorId;
	}
	
	public Long getAttributeId() {
		return attributeId;
	}
	
	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}
	
	public Map<String, String> getAttributeValueByKey() {
		return attributeValueByKey;
	}
	
	public void setAttributeValueByKey(Map<String, String> attributeValueByKey) {
		this.attributeValueByKey = attributeValueByKey;
	}
}
