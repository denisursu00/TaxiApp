package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.client.document.DocumentAttachmentsPanel.DocumentAttachmentsPanelTab;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtSupportedAttachmentTypesForPreviewConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtFileUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class DocumentAttachmentsPreviewTab extends TabItem implements DocumentAttachmentsPanelTab {
	
	private DocumentAttachmentsPreviewTabAttachmentsInfoProvider attachmentsInfoProvider;

	public DocumentAttachmentsPreviewTab(DocumentAttachmentsPreviewTabAttachmentsInfoProvider attachmentsInfoProvider) {

		this.attachmentsInfoProvider = attachmentsInfoProvider;
		
		setText(GwtLocaleProvider.getConstants().PREVIEW());
		setLayout(new FitLayout());
		
		
		addListener(Events.Select, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				reloadAvailableTabs();
			}
		});
	}
	
	private void reloadAvailableTabs() {

		removeAll();
		
		GwtSupportedAttachmentTypesForPreviewConstants supportedAttachmentTypesForPreviewConstants = GwtRegistryUtils.getSupportedAttachmentTypesForPreviewConstants();
		
		List<DocumentWindowRelatedAttachmentInfo> attachmentWithPreviewSupportedInfos = new ArrayList<DocumentWindowRelatedAttachmentInfo>();
		for (DocumentWindowRelatedAttachmentInfo attachmentInfo : attachmentsInfoProvider.getAttachmentsInfo()) {
			
			String attachmentName = attachmentInfo.getAttachmentName();
			String attachmentExtension = GwtFileUtils.getExtension(attachmentName);
			
			if (supportedAttachmentTypesForPreviewConstants.isFileExtensionSupportedForPreview(attachmentExtension)) {
				attachmentWithPreviewSupportedInfos.add(attachmentInfo);
			}
			
		}
		
		if (!attachmentWithPreviewSupportedInfos.isEmpty()) {
			
			TabPanel tabPanel = new TabPanel();
			tabPanel.setTabScroll(true);
			add(tabPanel);
			
			for (DocumentWindowRelatedAttachmentInfo attachmentWithPreviewSupportedInfo : attachmentWithPreviewSupportedInfos) {
				AttachmentPreviewTab attachmentPreviewTab = new AttachmentPreviewTab(attachmentWithPreviewSupportedInfo);
				tabPanel.add(attachmentPreviewTab);
			}
		}
		
		layout();

	}
	
	@Override
	public void reset() {
		removeAll();
	}
	
	@Override
	public void prepareForAdd(DocumentTypeModel documentType, String documentLocationRealName,
			String parentFolderId, WorkflowModel workflow, WorkflowStateModel currentState) {
		
		// Nu am nimic de facut intrucat acest tab nu are stare, ci se foloseste de provider-ul sau.
	}
	
	@Override
	public void prepareForViewOrEdit(DocumentTypeModel documentType,
			DocumentModel document,WorkflowModel workflow,
			WorkflowStateModel currentState) {

		// Nu am nimic de facut intrucat acest tab nu are stare, ci se foloseste de provider-ul sau.
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		// Nu am nimic de facut intrucat acest tab nu are stare, ci se foloseste de provider-ul sau.
	}
	
	@Override
	public boolean isValid() {
		// Nu am nimic de facut intrucat acest tab nu are stare, ci se foloseste de provider-ul sau.
		return true;
	}
	
	@Override
	public void populate(DocumentModel document) {
		// Nu am nimic de facut intrucat acest tab nu are stare, ci se foloseste de provider-ul sau.
	}
	
	@Override
	public void updateAttachmentRelatedComponentsAfterDocumentSave(
			String documentLocationRealName, String documentId) {

		// Nu am nimic de facut intrucat acest tab nu are stare, ci se foloseste de provider-ul sau.
	}
	
	public static interface DocumentAttachmentsPreviewTabAttachmentsInfoProvider {
		
		List<DocumentWindowRelatedAttachmentInfo> getAttachmentsInfo();
	}
}