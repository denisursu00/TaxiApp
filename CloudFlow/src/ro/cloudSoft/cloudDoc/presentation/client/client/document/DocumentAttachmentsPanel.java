package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class DocumentAttachmentsPanel extends ContentPanel {
	
	private TabPanel tabPanel;
	
	private DocumentAttachmentsManagementTab attachmentsManagementTab;
	private DocumentAttachmentsPreviewTab attachmentsPreviewTab;

	public DocumentAttachmentsPanel(DocumentGeneralPanel generalPanel) {

		setHeaderVisible(false);
		setLayout(new FitLayout());
		
		tabPanel = new TabPanel();
		add(tabPanel);
		
		attachmentsManagementTab = new DocumentAttachmentsManagementTab(generalPanel);
		tabPanel.add(attachmentsManagementTab);
		
		attachmentsPreviewTab = new DocumentAttachmentsPreviewTab(attachmentsManagementTab);
		tabPanel.add(attachmentsPreviewTab);
	}
	
	public void reset() {
		
		attachmentsManagementTab.reset();
		attachmentsPreviewTab.reset();
		
		tabPanel.setSelection(attachmentsManagementTab);
	}
	
	public void prepareForAdd(DocumentTypeModel documentType, String documentLocationRealName, String parentFolderId, WorkflowModel workflow, WorkflowStateModel currentState) {
		attachmentsManagementTab.prepareForAdd(documentType, documentLocationRealName, parentFolderId, workflow, currentState);
		attachmentsPreviewTab.prepareForAdd(documentType, documentLocationRealName, parentFolderId, workflow, currentState);
	}
	
	public void prepareForViewOrEdit(DocumentTypeModel documentType, DocumentModel document, WorkflowModel workflow, WorkflowStateModel currentState) {
		attachmentsManagementTab.prepareForViewOrEdit(documentType, document, workflow, currentState);
		attachmentsPreviewTab.prepareForViewOrEdit(documentType, document, workflow, currentState);
	}
	
	public void setReadOnly(boolean readOnly) {
		attachmentsManagementTab.setReadOnly(readOnly);
		attachmentsPreviewTab.setReadOnly(readOnly);
	}
	
	public boolean isValid() {
		return (attachmentsManagementTab.isValid() & attachmentsPreviewTab.isValid());
	}
	
	public void populate(DocumentModel document) {
		attachmentsManagementTab.populate(document);
		attachmentsPreviewTab.populate(document);
	}
	
	public void updateAttachmentRelatedComponentsAfterDocumentSave(String documentLocationRealName, String documentId) {
		attachmentsManagementTab.updateAttachmentRelatedComponentsAfterDocumentSave(documentLocationRealName, documentId);
		attachmentsPreviewTab.updateAttachmentRelatedComponentsAfterDocumentSave(documentLocationRealName, documentId);
	}
	
	public static interface DocumentAttachmentsPanelTab {
		
		void reset();
		
		void prepareForAdd(DocumentTypeModel documentType, String documentLocationRealName,
			String parentFolderId, WorkflowModel workflow, WorkflowStateModel currentState);
		
		void prepareForViewOrEdit(DocumentTypeModel documentType,
			DocumentModel document, WorkflowModel workflow,
			WorkflowStateModel currentState);
		
		void setReadOnly(boolean readOnly);
		
		boolean isValid();
		
		void populate(DocumentModel document);
		
		void updateAttachmentRelatedComponentsAfterDocumentSave(
			String documentLocationRealName, String documentId);
	}
}