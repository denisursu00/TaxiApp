package ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 
 */
public interface ModelWithAdditionalProperties extends Serializable {
	
	AdditionalPropertyProviderForModel[] getAdditionalPropertyProviders();

	<X> X getNonAdditionalPropertyValue(String propertyName);
	
	<X extends Object> X getNonAdditionalPropertyValue(String propertyName, X valueWhenNull);
	
	Map<String, Object> getNonAdditionalProperties();
	
	Collection<String> getNonAdditionalPropertyNames();
}