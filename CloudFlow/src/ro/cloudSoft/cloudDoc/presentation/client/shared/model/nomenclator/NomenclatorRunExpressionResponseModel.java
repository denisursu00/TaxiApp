package ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator;

import java.util.Map;

public class NomenclatorRunExpressionResponseModel {
	
	private Map<String, Map<String, String>> resultsByAttributeKey;
	
	public Map<String, Map<String, String>> getResultsByAttributeKey() {
		return resultsByAttributeKey;
	}
	
	public void setResultsByAttributeKey(Map<String, Map<String, String>> resultsByAttributeKey) {
		this.resultsByAttributeKey = resultsByAttributeKey;
	}
}


