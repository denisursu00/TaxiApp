package ro.cloudSoft.cloudDoc.plugins;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorAttributeTypeEnum;

public class NomenclatorAttributeEvaluationWrapper {

	private NomenclatorAttributeTypeEnum type;
	private String value;
	
	public NomenclatorAttributeEvaluationWrapper(NomenclatorAttributeTypeEnum type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public NomenclatorAttributeTypeEnum getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
}
