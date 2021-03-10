package ro.cloudSoft.cloudDoc.presentation.client.shared.model.views;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class MyActivitiesViewModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_DOCUMENT_ID = "documentId";
	public static final String PROPERTY_DOCUMENT_LOCATION_REAL_NAME = "documentLocationRealName";
	public static final String PROPERTY_DOCUMENT_NAME = "documentName";
	public static final String PROPERTY_WORKFLOW_SENDER = "workflowSender";
	public static final String PROPERTY_WORKFLOW_CURRENT_STATUS = "workflowCurrentStatus";
	public static final String PROPERTY_WORKFLOW_NAME = "workflowName";
	public static final String PROPERTY_DOCUMENT_TYPE_NAME = "documentTypeName";
	public static final String PROPERTY_DOCUMENT_CREATED_DATE = "documentCreatedDate";
	public static final String PROPERTY_DOCUMENT_AUTHOR = "documentAuthor";
	
	public MyActivitiesViewModel() {
	}
	
	public void setDocumentId(String documentId){
		set(PROPERTY_DOCUMENT_ID, documentId);
	}
	
	public String getDocumentId(){
		return get(PROPERTY_DOCUMENT_ID);
	}
	
	public void setDocumentLocationRealName(String documentLocationRealName){
		set(PROPERTY_DOCUMENT_LOCATION_REAL_NAME, documentLocationRealName);
	}
	
	public String getDocumentLocationRealName(){
		return get(PROPERTY_DOCUMENT_LOCATION_REAL_NAME);
	}
	
	public void setDocumentName(String documentName){
		set(PROPERTY_DOCUMENT_NAME, documentName);
	}
	
	public String getDocumentName(){
		return get(PROPERTY_DOCUMENT_NAME);
	}
	
	public void setWorkflowSender(String workflowSender){
		set(PROPERTY_WORKFLOW_SENDER, workflowSender);
	}
	
	public String getWorkflowSender(){
		return get(PROPERTY_WORKFLOW_SENDER);
	}
	
	public void setWorkflowCurrentStatus(String workflowCurrentStatus){
		set(PROPERTY_WORKFLOW_CURRENT_STATUS, workflowCurrentStatus);
	}
	
	public String getWorkflowCurrentStatus(){
		return get(PROPERTY_WORKFLOW_CURRENT_STATUS);
	}
	
	public void setWorkflowName(String workflowName){
		set(PROPERTY_WORKFLOW_NAME, workflowName);
	}
	
	public String getWorkflowName(){
		return get(PROPERTY_WORKFLOW_NAME);
	}
	
	public void setDocumentTypeName(String documentTypeName){
		set(PROPERTY_DOCUMENT_TYPE_NAME, documentTypeName);
	}
	
	public String getDocumentTypeName(){
		return get(PROPERTY_DOCUMENT_TYPE_NAME);
	}
	
	public void setDocumentCreatedDate(Date documentCreatedDate){
		set(PROPERTY_DOCUMENT_CREATED_DATE, documentCreatedDate);
	}
	
	public Date getDocumentCreatedDate(){
		return (Date)get(PROPERTY_DOCUMENT_CREATED_DATE);
	}
	
	public void setDocumentAuthor(String documentAuthor){
		set(PROPERTY_DOCUMENT_AUTHOR, documentAuthor);
	}
	
	public String getDocumentAuthor(){
		return get(PROPERTY_DOCUMENT_AUTHOR);
	}
	
}
