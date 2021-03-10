package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CollectionInstanceModel extends BaseModel implements IsSerializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_ID= "id";
	public static final String PROPERTY_METADATA_INSTANCE_LIST= "metadataInstanceList";
	
	public CollectionInstanceModel()
	{
		super();
	}

	public String getId() {
		return get(PROPERTY_ID);
	}
	
	public void setId(String id) 
	{
		set(PROPERTY_ID, id);
	}
	
	public  List<MetadataInstanceModel> getMetadataInstanceList() {
		return get(PROPERTY_METADATA_INSTANCE_LIST);
	}
	public void setMetadataInstanceList(List<MetadataInstanceModel> metadataInstanceList) 
	{
		set(PROPERTY_METADATA_INSTANCE_LIST, metadataInstanceList);
	}
	
	
	
}
