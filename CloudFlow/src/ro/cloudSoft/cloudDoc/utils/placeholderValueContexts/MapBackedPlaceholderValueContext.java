package ro.cloudSoft.cloudDoc.utils.placeholderValueContexts;

import java.util.Map;

/**
 * 
 */
public class MapBackedPlaceholderValueContext implements PlaceholderValueContext {

	private final Map<String, String> placeholderValues;
	
	public MapBackedPlaceholderValueContext(Map<String, String> placeholderValues) {
		this.placeholderValues = placeholderValues;
	}

	@Override
	public String getValue(String placeholderName) {
		return placeholderValues.get(placeholderName);
	}
}