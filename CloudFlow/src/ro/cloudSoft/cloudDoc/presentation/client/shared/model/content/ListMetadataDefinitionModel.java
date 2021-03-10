package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ListMetadataDefinitionModel extends MetadataDefinitionModel
		implements IsSerializable {
	
	private static final long serialVersionUID = 1905232419906105442L;
	
	public static final String PROPERTY_MULTIPLE_SELECTION =  "multipleSelection";
	public static final String PROPERTY_EXTENDABLE =  "extendable";
	public static final String PROPERTY_LIST_ITEMS =  "listItems";
	
	public ListMetadataDefinitionModel(){}
	
	public void setMultipleSelection(Boolean multipleSelection){
		set(PROPERTY_MULTIPLE_SELECTION, multipleSelection);
	}
	
	public Boolean isMultipleSelection(){
		return get(PROPERTY_MULTIPLE_SELECTION); 
	}
	
	public void setExtendable(Boolean extendable){
		set(PROPERTY_EXTENDABLE, extendable);
	}
	
	public Boolean isExtendable(){
		return get(PROPERTY_EXTENDABLE); 
	}
	
	public void setListItems(List<ListMetadataItemModel> listItems){
		set(PROPERTY_LIST_ITEMS, listItems);
	}
	
	public List<ListMetadataItemModel> getListItems(){
		return get(PROPERTY_LIST_ITEMS); 
	}

}
