package ro.cloudSoft.cloudDoc.presentation.client.shared.utils.model;

import java.util.Collection;
import java.util.Map;

import com.extjs.gxt.ui.client.data.BaseModel;

/**
 * 
 */
public abstract class BaseModelWithAdditionalProperties extends BaseModel implements ModelWithAdditionalProperties {
	
	private static final long serialVersionUID = 1L;
	
	private ModelWithAdditionalPropertiesDelegate delegate = new ModelWithAdditionalPropertiesDelegate(this);
	
	@Override
	public <X> X getNonAdditionalPropertyValue(String propertyName) {
		return super.get(propertyName);
	}
	
	@Override
	public <X> X get(String propertyName) {
		return delegate.get(propertyName);
	}
	
	@Override
	public <X extends Object> X getNonAdditionalPropertyValue(String propertyName, X valueWhenNull) {
		return super.get(propertyName, valueWhenNull);
	}

	@Override
	public <X extends Object> X get(String propertyName, X valueWhenNull) {
		return delegate.get(propertyName, valueWhenNull);
	}

	@Override
	public Map<String, Object> getNonAdditionalProperties() {
		return super.getProperties();
	}
	
	@Override
	public Map<String, Object> getProperties() {
		return delegate.getProperties();
	}
	
	@Override
	public Collection<String> getNonAdditionalPropertyNames() {
		return super.getPropertyNames();
	}

	@Override
	public Collection<String> getPropertyNames() {
		return delegate.getPropertyNames();
	}
}