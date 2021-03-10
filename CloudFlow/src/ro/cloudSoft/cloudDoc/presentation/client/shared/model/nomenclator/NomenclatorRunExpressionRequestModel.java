package ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator;

import java.util.Map;

public class NomenclatorRunExpressionRequestModel {
	
	private Long nomenclatorId;
	private Map<String, String> attributeValueByKey;
	
	public Long getNomenclatorId() {
		return nomenclatorId;
	}
	public void setNomenclatorId(Long nomenclatorId) {
		this.nomenclatorId = nomenclatorId;
	}
	public Map<String, String> getAttributeValueByKey() {
		return attributeValueByKey;
	}
	public void setAttributeValueByKey(Map<String, String> attributeValueByKey) {
		this.attributeValueByKey = attributeValueByKey;
	}
}
