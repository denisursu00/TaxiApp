package ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class ModelWithAdditionalPropertiesDelegate implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private ModelWithAdditionalProperties model;
	private Map<String, AdditionalPropertyProviderForModel> additionalPropertyProviderByPropertyName;
	
	protected ModelWithAdditionalPropertiesDelegate() {
		// Constructor necesar pentru serializare
	}
	
	public ModelWithAdditionalPropertiesDelegate(ModelWithAdditionalProperties modelThatNeedsDelegation) {
		
		model = modelThatNeedsDelegation;
		
		additionalPropertyProviderByPropertyName = new HashMap<String, AdditionalPropertyProviderForModel>();
		for (AdditionalPropertyProviderForModel additionalPropertyProvider : modelThatNeedsDelegation.getAdditionalPropertyProviders()) {
			additionalPropertyProviderByPropertyName.put(additionalPropertyProvider.getPropertyName(), additionalPropertyProvider);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <X> X get(String propertyName) {
		if (isAdditionalProperty(propertyName)) {
			return ((X) getAdditionalPropertyValue(propertyName));
		} else {
			return model.getNonAdditionalPropertyValue(propertyName);
		}
	}

	@SuppressWarnings("unchecked")
	public <X extends Object> X get(String propertyName, X valueWhenNull) {
		if (isAdditionalProperty(propertyName)) {
			Object propertyValue = getAdditionalPropertyValue(propertyName);
			return (propertyValue != null) ? ((X) propertyValue) : valueWhenNull;
		} else {
			return model.getNonAdditionalPropertyValue(propertyName, valueWhenNull);
		}
	}
	
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = model.getNonAdditionalProperties();
		for (AdditionalPropertyProviderForModel additionalPropertyProvider : additionalPropertyProviderByPropertyName.values()) {
			properties.put(additionalPropertyProvider.getPropertyName(), additionalPropertyProvider.getPropertyValue());
		}
		return properties;
	}
	
	public Collection<String> getPropertyNames() {
		Collection<String> propertyNames = model.getNonAdditionalPropertyNames();
		Collection<String> additionalPropertyNames = additionalPropertyProviderByPropertyName.keySet();
		propertyNames.addAll(additionalPropertyNames);
		return propertyNames;
	}
	
	private boolean isAdditionalProperty(String propertyName) {
		return additionalPropertyProviderByPropertyName.keySet().contains(propertyName);
	}
	
	private Object getAdditionalPropertyValue(String propertyName) {
		return additionalPropertyProviderByPropertyName.get(propertyName).getPropertyValue();
	}
}