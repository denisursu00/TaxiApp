package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AutoNumberMetadataDefinitionModel extends MetadataDefinitionModel
		implements IsSerializable {
	
	private static final long serialVersionUID = -617848639751437868L;
	public static String PROPERTY_PREFIX = "prefix";
	public static String PROPERTY_NUMBER_LENGTH = "numberLength";
	
	public AutoNumberMetadataDefinitionModel(){}
	
	public void setPrefix(String prefix){
		set(PROPERTY_PREFIX, prefix);
	}
	
	public String getPrefix(){
		return get(PROPERTY_PREFIX); 
	}
	
	public void setNumberLength(Integer numberLength){
		set(PROPERTY_NUMBER_LENGTH, numberLength);
	}
	
	public Integer getNumberLength(){
		return get(PROPERTY_NUMBER_LENGTH); 
	}

}
