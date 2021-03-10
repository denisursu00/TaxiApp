package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.NavigationConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.web.GwtUrlBuilder;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.google.gwt.user.client.ui.Image;

/**
 * 
 */
public class DocumentWorkflowGraphTab extends DocumentBaseTab {
	
	public DocumentWorkflowGraphTab() {
		setScrollMode(Scroll.AUTO);
		setText(GwtLocaleProvider.getConstants().WORKFLOW_GRAPH());
	}
	
	@Override
	protected void reset() {
		removeAll();
		layout();
	}
	
	private void viewWorkflowGraph(WorkflowModel workflow, WorkflowStateModel currentState) {
		
		if (workflow == null) {
			setEnabled(false);
			return;
		}

		setEnabled(true);
		
		GwtUrlBuilder imageUrlBuilder = new GwtUrlBuilder(NavigationConstants.getViewWorkflowGraphLink());
		imageUrlBuilder.setParameter("workflowId", workflow.getId());
		if (currentState != null) {
			imageUrlBuilder.setParameter("codeForCurrentState", currentState.getCode());
		}
		String imageUrl = imageUrlBuilder.build();
		
		Image workflowGraphImage = new Image(imageUrl);
		add(workflowGraphImage);
		layout();
	}
	
	@Override
	public void prepareForAdd(DocumentTypeModel documentType, String documentLocationRealName, String parentFolderId, WorkflowModel workflow, WorkflowStateModel currentState) {
		reset();
		viewWorkflowGraph(workflow, currentState);
	}
	
	@Override
	public void prepareForViewOrEdit(DocumentTypeModel documentType, DocumentModel document, WorkflowModel workflow, WorkflowStateModel currentState) {
		reset();
		viewWorkflowGraph(workflow, currentState);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {}
	
	@Override
	public boolean isValid() {
		return true;
	}
	
	@Override
	public void populate(DocumentModel document) {}
}