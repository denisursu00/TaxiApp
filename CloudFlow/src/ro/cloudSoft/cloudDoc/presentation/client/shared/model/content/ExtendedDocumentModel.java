package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ExtendedDocumentModel extends BaseModel implements IsSerializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 6870951184925342947L;

    public static final String PROPERTY_DOCUMENTMODEL = "documentModel";
    public static final String PROPERTY_DOCUMENTTYPEMODEL = "documentTypeModel";
    public static final String PROPERTY_WORKFLOWMODEL = "workflowModel";
    public static final String PROPERTY_SENDINGRIGHTS = "sendingRights";
    public static final String PROPERTY_WORKFLOWSTATEMODEL = "workflowStateModel";
    
    public static final String PROPERTY_CAN_USER_EDIT = "canUserEdit";
    public static final String PROPERTY_CAN_USER_ACCESS_LOCKED_DOCUMENT = "canUserAccessLockedDocument";

    public ExtendedDocumentModel()
    {

    }

    public DocumentModel getDocumentModel()
    {
	return get(PROPERTY_DOCUMENTMODEL);
    }

    public void setDocumentModel(DocumentModel documentModel )
    {
	set(PROPERTY_DOCUMENTMODEL, documentModel);
    }

    public DocumentTypeModel getDocumentTypeModel()
    {
	return get(PROPERTY_DOCUMENTTYPEMODEL);
    }

    public void setDocumentTypeModel(DocumentTypeModel documentModel )
    {
	set(PROPERTY_DOCUMENTTYPEMODEL, documentModel);
    }
    
    public WorkflowModel getWorkflowModel()
    {
	return get(PROPERTY_WORKFLOWMODEL);
    }

    public void setWorkflowModel(WorkflowModel workflowModel )
    {
	set(PROPERTY_WORKFLOWMODEL, workflowModel);
    }
    
    public Boolean getSendingRights()
    {
	return get(PROPERTY_SENDINGRIGHTS);
    }

    public void setSendingRights(Boolean sendingRights )
    {
	set(PROPERTY_SENDINGRIGHTS, sendingRights);
    }
    
    
    public WorkflowStateModel getWorkflowStateModel()
    {
	return get(PROPERTY_WORKFLOWSTATEMODEL);
    }

    public void setWorkflowStateModel(WorkflowStateModel workflowStateModel )
    {
	set(PROPERTY_WORKFLOWSTATEMODEL, workflowStateModel);
    }
    
    // 
    
    public Boolean getCanUserSend()
    {
	return get(PROPERTY_SENDINGRIGHTS);
    }

    public void setCanUserSend(Boolean sendingRights )
    {
	set(PROPERTY_SENDINGRIGHTS, sendingRights);
    }
    
    public Boolean getCanUserEdit() {
    	return get(PROPERTY_CAN_USER_EDIT);
    }

    public void setCanUserEdit(Boolean canEdit ) {
    	set(PROPERTY_CAN_USER_EDIT, canEdit);
    }
    
    public Boolean getCanUserAccessLockedDocument() {
    	return get(PROPERTY_CAN_USER_ACCESS_LOCKED_DOCUMENT);
    }

    public void setCanUserAccessLockedDocument(Boolean canEdit ) {
    	set(PROPERTY_CAN_USER_ACCESS_LOCKED_DOCUMENT, canEdit);
    }   
}
