package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;

import com.extjs.gxt.ui.client.widget.TabItem;

public abstract class DocumentBaseTab extends TabItem {

	protected abstract void reset();
	public abstract void prepareForAdd(DocumentTypeModel documentType, String documentLocationRealName, String parentFolderId, WorkflowModel workflow, WorkflowStateModel currentState);
	public abstract void prepareForViewOrEdit(DocumentTypeModel documentType, DocumentModel document, WorkflowModel workflow, WorkflowStateModel currentState);
	public abstract void setReadOnly(boolean readOnly);
	public abstract boolean isValid();
	public abstract void populate(DocumentModel document);
}