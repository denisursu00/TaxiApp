package ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator;

import org.hibernate.criterion.MatchMode;

public class NomenclatorSimpleFilter extends NomenclatorFilter {

	private String value;

	public NomenclatorSimpleFilter() {
		super();
	}

	public NomenclatorSimpleFilter(String attributeKey, String value, MatchMode matchMode) {
		super();
		setAttributeKey(attributeKey);
		setMatchMode(matchMode);
		this.value = value;
	}

	public NomenclatorSimpleFilter(String attributeKey, String value) {
		super();
		setAttributeKey(attributeKey);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
