package ro.cloudSoft.cloudDoc.presentation.client.admin.workflows;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;

import com.extjs.gxt.ui.client.widget.TabItem;

public abstract class WorkflowAbstractTab extends TabItem {

	public WorkflowAbstractTab() {
		super();
	}

	public abstract void reset();
	
	public abstract void prepareForAdd();

	public abstract void prepareForEdit(WorkflowModel workflowModel);

	public abstract void populateForSave(WorkflowModel workflowModel);

	public abstract boolean isValid();
	
	public void doWithSelectedDocumentTypes(List<DocumentTypeModel> documentTypes) {}
}