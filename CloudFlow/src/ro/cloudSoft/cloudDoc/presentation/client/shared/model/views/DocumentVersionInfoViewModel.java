package ro.cloudSoft.cloudDoc.presentation.client.shared.model.views;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DocumentVersionInfoViewModel extends BaseModel implements IsSerializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String PROPERTY_VERSION_NUMBER = "versionNumber";
	public static final String PROPERTY_VERSION_AUTHOR = "versionAuthor";
	public static final String PROPERTY_VERSION_CREATION_DATE = "versionCreationDate";
	
	public DocumentVersionInfoViewModel() 
	{
	}
	public String getVersionNumber() {
		return get(PROPERTY_VERSION_NUMBER);
	}
	public void setVersionNumber(String versionNumber) {
		set(PROPERTY_VERSION_NUMBER,versionNumber);
	}
	public  String getVersionAuthor() {
		return get(PROPERTY_VERSION_AUTHOR);
	}
	public  void setVersionAuthor(String versionAuthor) {
		set(PROPERTY_VERSION_AUTHOR,versionAuthor);
	}
	public  Date getVersionCreationDate() {
		return get(PROPERTY_VERSION_CREATION_DATE);
	}
	public  void setVersionCreationDate(Date versionCreationDate) {
		set(PROPERTY_VERSION_CREATION_DATE,versionCreationDate);
	}
}
