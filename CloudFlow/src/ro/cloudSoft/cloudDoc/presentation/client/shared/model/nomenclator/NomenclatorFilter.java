package ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator;

import org.hibernate.criterion.MatchMode;

public abstract class NomenclatorFilter {
	
	private String attributeKey;
	
	private MatchMode matchMode;

	public String getAttributeKey() {
		return attributeKey;
	}
	public void setAttributeKey(String attributeKey) {
		this.attributeKey = attributeKey;
	}
	public MatchMode getMatchMode() {
		return matchMode;
	}
	public void setMatchMode(MatchMode matchMode) {
		this.matchMode = matchMode;
	}
}	
	
