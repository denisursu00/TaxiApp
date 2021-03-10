package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class MetadataCollectionInstanceModel extends BaseModel implements IsSerializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_METADATA_DEFINITION_ID = "metadataDefinitionId";
	public static final String PROPERTY_COLLECTION_INSTANCE_ROWS = "collectionInstanceRows";
	
	public MetadataCollectionInstanceModel() {
	}
	
	public Long getMetadataDefinitionId() {
		return get(PROPERTY_METADATA_DEFINITION_ID);
	}

	public void setMetadataDefinitionId(Long metadataDefinitionId) {
		set(PROPERTY_METADATA_DEFINITION_ID, metadataDefinitionId);
	}

	public List<CollectionInstanceModel> getCollectionInstanceRows() {
		return get(PROPERTY_COLLECTION_INSTANCE_ROWS);
	}
	
	public void setCollectionInstanceRows(List<CollectionInstanceModel> collectionInstanceRows) {
		set(PROPERTY_COLLECTION_INSTANCE_ROWS, collectionInstanceRows);
	}
}
