package ro.cloudSoft.cloudDoc.presentation.client.shared.model.views;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DocumentHistoryViewModel extends BaseModel implements IsSerializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_WORKFLOW_ACTOR = "workflowActor";
	public static final String PROPERTY_WORKFLOW_TRANSITION_NAME= "workflowTransitionName";
	public static final String PROPERTY_WORKFLOW_TRANSITION_DATE= "workflowTransitionDate";
	public static final String PROPERTY_ORGANIZATION_DEPARTMENT = "departmentName";
	
	public DocumentHistoryViewModel()
	{}

	public  String getWorkflowActor() {
		return get(PROPERTY_WORKFLOW_ACTOR);
	}
	
	public  void setWorkflowActor(String workflowActor) {
		 set(PROPERTY_WORKFLOW_ACTOR,workflowActor);
	}

	public  String getWorkflowTransitionName() {
		return get(PROPERTY_WORKFLOW_TRANSITION_NAME);
	}
	public  void setWorkflowTransitionName(String workflowTransitionName) {
		 set(PROPERTY_WORKFLOW_TRANSITION_NAME,workflowTransitionName);
	}
	public  Date getWorkflowTransitionDate() {
		return get(PROPERTY_WORKFLOW_TRANSITION_DATE);
	}
	public  void setWorkflowTransitionDate(Date workflowTransitionDate) {
		 set(PROPERTY_WORKFLOW_TRANSITION_DATE,workflowTransitionDate);
	}
	public  String getOrganizationDepartment() {
		return get(PROPERTY_ORGANIZATION_DEPARTMENT);
	}
	public  void setOrganizationDepartment(String organizationDepartment) {
		 set(PROPERTY_ORGANIZATION_DEPARTMENT,organizationDepartment);
	}
}
