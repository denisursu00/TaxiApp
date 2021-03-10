package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ModelWithLabel;

import com.google.gwt.user.client.rpc.IsSerializable;

// TODO Trebuie redenumita in "CollectionDefinitionModel".
public class MetadataCollectionDefinitionModel extends MetadataDefinitionModel
		implements ModelWithLabel, IsSerializable {
	
	private static final long serialVersionUID = -5384115030936347679L;	
	
	public static final String PROPERTY_METADATA_DEFINITIONS = "metadataDefinitions";
	
	public MetadataCollectionDefinitionModel() {
		setMetadataDefinitions(new ArrayList<MetadataDefinitionModel>());
	}
	
	public List<MetadataDefinitionModel> getMetadataDefinitions() {
		return get(PROPERTY_METADATA_DEFINITIONS);
	}
	
	public void setMetadataDefinitions(List<MetadataDefinitionModel> metadataDefinitions) {
		set(PROPERTY_METADATA_DEFINITIONS, metadataDefinitions);
	}

}
