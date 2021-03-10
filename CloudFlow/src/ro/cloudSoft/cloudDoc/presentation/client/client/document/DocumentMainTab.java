package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class DocumentMainTab extends DocumentBaseTab {
	
	private DocumentGeneralPanel generalPanel;
	private DocumentAttachmentsPanel attachmentsPanel;
	
	public DocumentMainTab() {
		
		setLayout(new RowLayout(Orientation.HORIZONTAL));
		
		Margins margins = new Margins(5);
		
		generalPanel = new DocumentGeneralPanel();
		add(generalPanel, new RowData(0.34, 1, margins));
		
		attachmentsPanel = new DocumentAttachmentsPanel(generalPanel);
		add(attachmentsPanel, new RowData(0.66, 1, margins));
	}
	
	@Override
	public boolean isValid() {
		return (generalPanel.isValid() && attachmentsPanel.isValid());
	}

	@Override
	public void populate(DocumentModel document) {
		generalPanel.populate(document);
		attachmentsPanel.populate(document);
	}
	
	public void updateAttachmentRelatedComponentsAfterDocumentSave(String documentLocationRealName, String documentId) {
		attachmentsPanel.updateAttachmentRelatedComponentsAfterDocumentSave(documentLocationRealName, documentId);
	}
	
	@Override
	public void prepareForAdd(final DocumentTypeModel documentType, String documentLocationRealName, String parentFolderId, WorkflowModel workflow, WorkflowStateModel currentState) {
		
		reset();
		
		setText(documentType.getName());
		
		generalPanel.prepareForAdd(documentType, documentLocationRealName, parentFolderId, workflow, currentState);
		attachmentsPanel.prepareForAdd(documentType, documentLocationRealName, parentFolderId, workflow, currentState);
	}

	@Override
	public void prepareForViewOrEdit(final DocumentTypeModel documentType, final DocumentModel document, WorkflowModel workflow, WorkflowStateModel currentState) {
		
		reset();
		
		setText(documentType.getName());
		
		generalPanel.prepareForViewOrEdit(documentType, document, workflow, currentState);
		attachmentsPanel.prepareForViewOrEdit(documentType, document, workflow, currentState);
	}
	
	public void onAdd() {
		generalPanel.onAdd();
	}
	
	public void onEdit() {
		generalPanel.onEdit();
	}

	@Override
	protected void reset() {
		
		setText(GwtLocaleProvider.getConstants().DOCUMENT());
		
		generalPanel.reset();
		attachmentsPanel.reset();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		generalPanel.setReadOnly(readOnly);
		attachmentsPanel.setReadOnly(readOnly);
	}

	public String getDocumentId() {
		return generalPanel.getDocumentId();
	}
	public void setDocumentId(String documentId) {
		generalPanel.setDocumentId(documentId);
	}
	public Long getDocumentTypeId() {
		return generalPanel.getDocumentTypeId();
	}
	public String getAuthorUserIdAsString() {
		return generalPanel.getAuthorUserIdAsString();
	}
	public Boolean isKeepAllVersions() {
		return generalPanel.isKeepAllVersions();
	}
	public String getDocumentLocationRealName() {
		return generalPanel.getDocumentLocationRealName();
	}
	public Boolean getHasStableVersion() {
		return generalPanel.getHasStableVersion();
	}
	
	/**
	 * Returneaza valoarea unei metadate, a carei definitie are ID-ul dat.
	 * Daca nu se gaseste campul metadatei, va returna null.
	 */
	public String getMetadataValue(Long metadataDefinitionId) {
		return generalPanel.getMetadataValue(metadataDefinitionId);
	}
}