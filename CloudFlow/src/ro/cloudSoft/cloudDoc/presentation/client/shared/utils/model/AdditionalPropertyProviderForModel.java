package ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model;

import java.io.Serializable;

/**
 * 
 */
public interface AdditionalPropertyProviderForModel extends Serializable {
	
	String getPropertyName();
	
	Object getPropertyValue();
}