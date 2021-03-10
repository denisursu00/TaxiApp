/**
 * 
 */
package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtValidateUtils;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class MetadataInstanceModel extends BaseModel implements IsSerializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_METADATA_DEFINITION_ID = "metadataDefinitionId";
	public static final String PROPERTY_VALUES = "values";
	
	private static final String SEPARATOR_VALUES = ", ";
	
	public MetadataInstanceModel() {}
	
	public String getValue() {
		List<String> values = this.getValues();
		return (GwtValidateUtils.hasElements(values)) ? values.get(0) : null;
	}
	
	public void setValue(String value) {
		List<String> values = new ArrayList<String>();
		values.add(value);
		this.setValues(values);
	}
	
	public String getValuesAsString() {
		List<String> values = getValues();
		if (values == null || values.isEmpty()) {
			return "";
		}
		StringBuilder valuesString = new StringBuilder();
		for (String value : values) {
			valuesString.append(value);
			valuesString.append(SEPARATOR_VALUES);
		}
		valuesString.delete(valuesString.lastIndexOf(SEPARATOR_VALUES), valuesString.length());
		return valuesString.toString();
	}

	public  Long getMetadataDefinitionId() {
		return get(PROPERTY_METADATA_DEFINITION_ID);
	}
	
	public void setMetadataDefinitionId(Long id){
		set(PROPERTY_METADATA_DEFINITION_ID, id);
	}

	public  List<String> getValues() {
		 return get(PROPERTY_VALUES);
	}

	public  void setValues(List<String> values) {
		set (PROPERTY_VALUES,values);	
	}
}