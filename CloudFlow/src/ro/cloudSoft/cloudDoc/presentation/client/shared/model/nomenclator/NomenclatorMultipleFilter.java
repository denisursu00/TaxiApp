package ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator;

import java.util.List;

public class NomenclatorMultipleFilter extends NomenclatorFilter {
	
	private List<String> values;

	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
}
