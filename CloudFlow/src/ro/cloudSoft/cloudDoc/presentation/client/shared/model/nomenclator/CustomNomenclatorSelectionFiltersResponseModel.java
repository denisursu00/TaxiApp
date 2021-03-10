package ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator;

import java.util.List;
import java.util.Map;

public class CustomNomenclatorSelectionFiltersResponseModel {
	
	private boolean selectable;
	private List<Long> valueIds;
	private Map<String, List<String>> attributeValuesByKey;
	
	public boolean isSelectable() {
		return selectable;
	}
	
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}
	
	public List<Long> getValueIds() {
		return valueIds;
	}
	
	public void setValueIds(List<Long> valueIds) {
		this.valueIds = valueIds;
	}
	
	public Map<String, List<String>> getAttributeValuesByKey() {
		return attributeValuesByKey;
	}
	
	public void setAttributeValuesByKey(Map<String, List<String>> attributeValuesByKey) {
		this.attributeValuesByKey = attributeValuesByKey;
	}
}
