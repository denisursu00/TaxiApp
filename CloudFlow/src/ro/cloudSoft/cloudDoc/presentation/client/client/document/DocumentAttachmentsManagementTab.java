package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.cloudSoft.cloudDoc.presentation.client.client.document.DocumentAttachmentsPanel.DocumentAttachmentsPanelTab;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.DocumentAttachmentsPreviewTab.DocumentAttachmentsPreviewTabAttachmentsInfoProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.NavigationConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AttachmentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentAttachmentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MimeTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtFileUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtFormatUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtLogUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtValidateUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtXmlUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content.GwtAttachmentPermissionsBusinessUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content.GwtMandatoryAttachmentBusinessHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content.GwtMandatoryAttachmentBusinessHelper.CurrentDocumentStateProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.CustomText;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.FormPanelWithIEFix;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.layout.TableRowLayout;
import com.google.gwt.user.client.ui.Anchor;

public class DocumentAttachmentsManagementTab extends TabItem implements DocumentAttachmentsPanelTab, CurrentDocumentStateProvider, DocumentAttachmentsPreviewTabAttachmentsInfoProvider {
	
	private static final String RESPONSE_MESSAGE_OK = "OK";
	
	private final DocumentGeneralPanel generalPanel;

	private WorkflowStateModel currentState;

	private GwtMandatoryAttachmentBusinessHelper mandatoryAttachmentBusinessHelper;
	
	private Set<String> allowedExtensionsInLowerCase;
	private LinkedHashMap<String, DocumentWindowRelatedAttachmentInfo> existingAttachmentInfoMap;
	private Map<String, Anchor> existingAttachmentLinkMap;
	private Map<String, Button> deleteAttachmentButtonMap;
	private List<String> namesForAttachmentsToDelete;
	
	private ContentPanel mainPanel;
	
	private FormPanel uploadAttachmentFormPanel;
	private LayoutContainer existingAttachmentsContainer;
	
	private CustomText addAttachmentText;
	private FileUploadField attachmentFileUploadField;
	private Button addAttachmentButton;
	
	private Status allowedAttachmentTypesStatus;

	public DocumentAttachmentsManagementTab(DocumentGeneralPanel generalPanel) {

		setText(GwtLocaleProvider.getConstants().MANAGEMENT());
		setLayout(new FitLayout());
		
		this.generalPanel = generalPanel;
		
		this.allowedExtensionsInLowerCase = new HashSet<String>();
		this.existingAttachmentInfoMap = new LinkedHashMap<String, DocumentWindowRelatedAttachmentInfo>();
		this.existingAttachmentLinkMap = new HashMap<String, Anchor>();
		this.deleteAttachmentButtonMap = new HashMap<String, Button>();
		this.namesForAttachmentsToDelete = new ArrayList<String>();
		
		
		this.mainPanel = new ContentPanel();
		ComponentUtils.setBottomBorderOnly(mainPanel);
		this.mainPanel.setHeaderVisible(false);
		this.mainPanel.setScrollMode(Scroll.AUTOY);
		
		uploadAttachmentFormPanel = new FormPanelWithIEFix();
		uploadAttachmentFormPanel.addListener(Events.Submit, new Listener<FormEvent>() {
			@Override
			public void handleEvent(FormEvent fe) {
				String respone = fe.getResultHtml();
				if (isUploadAttachmentResponseOk(respone)) {
					String fileName = GwtFileUtils.getFileNameFromPath(attachmentFileUploadField.getValue());
					addExistingAttachment(null, null, null, fileName);					
				} else {
					GwtLogUtils.log("La adaugarea unui atasament, NU s-a receptionat mesajul de OK, ci urmatorul: [" + respone + "].");
					MessageUtils.displayError(GwtLocaleProvider.getMessages().UPLOAD_ATTACHMENT_ERROR());
				}
				attachmentFileUploadField.setValue(null);
				LoadingManager.get().loadingComplete();
			}
		});
		uploadAttachmentFormPanel.setAction(NavigationConstants.getUploadAttachmentLink());
		uploadAttachmentFormPanel.setEncoding(Encoding.MULTIPART);
		ComponentUtils.setBottomBorderOnly(uploadAttachmentFormPanel);
		uploadAttachmentFormPanel.setHeaderVisible(false);
		uploadAttachmentFormPanel.setMethod(Method.POST);

		TableRowLayout tableRowLayout = new TableRowLayout();
		tableRowLayout.setCellPadding(5);
		uploadAttachmentFormPanel.setLayout(tableRowLayout);
		
		addAttachmentText = new CustomText();
		addAttachmentText.setStyleName("textNormal");
		addAttachmentText.setText(GwtLocaleProvider.getConstants().ADD_ATTACHMENT());
		uploadAttachmentFormPanel.add(addAttachmentText);
		
		attachmentFileUploadField = new FileUploadField();
		attachmentFileUploadField.setName("attachment");
		attachmentFileUploadField.setWidth(320);
		uploadAttachmentFormPanel.add(attachmentFileUploadField);
		
		addAttachmentButton = new Button();
		addAttachmentButton.setText(GwtLocaleProvider.getConstants().ADD());
		addAttachmentButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent be) {
				
				if (isAttachmentFormValid()) {
					LoadingManager.get().loading();
					/*
					 * Face submit la formular. De raspuns se va ocupa
					 * listener-ul formularului.
					 */
					uploadAttachmentFormPanel.submit();
				} else {
					ErrorHelper.displayErrors();
				}
			}
		});
		uploadAttachmentFormPanel.add(addAttachmentButton);
		
		this.mainPanel.add(uploadAttachmentFormPanel);
		
		existingAttachmentsContainer = new LayoutContainer();
		TableLayout tableLayout = new TableLayout();
		//tableLayout.setBorder(1);
		tableLayout.setColumns(2);
		tableLayout.setCellPadding(10);
		existingAttachmentsContainer.setLayout(tableLayout);
		this.mainPanel.add(existingAttachmentsContainer);
		
		this.allowedAttachmentTypesStatus = new Status();
		this.allowedAttachmentTypesStatus.setStyleName("textNormal");
		this.mainPanel.setBottomComponent(this.allowedAttachmentTypesStatus);
		
		this.add(mainPanel);
	}
	
	@Override
	public void reset() {
		this.allowedExtensionsInLowerCase.clear();
		this.existingAttachmentInfoMap.clear();
		this.existingAttachmentLinkMap.clear();
		this.deleteAttachmentButtonMap.clear();
		this.namesForAttachmentsToDelete.clear();
		this.attachmentFileUploadField.setValue(null);
		this.existingAttachmentsContainer.removeAll();
		this.existingAttachmentsContainer.layout();
		this.allowedAttachmentTypesStatus.setText(null);
	}
	
	@Override
	public void prepareForAdd(DocumentTypeModel documentType, String documentLocationRealName,
			String parentFolderId, WorkflowModel workflow, WorkflowStateModel currentState) {
		
		this.currentState = currentState;
		this.updateValidationRelatedItems(documentType);		
	}
	
	@Override
	public void prepareForViewOrEdit(DocumentTypeModel documentType,
			DocumentModel document,WorkflowModel workflow,
			WorkflowStateModel currentState) {
		
		this.currentState = currentState;
		for (DocumentAttachmentModel attachment : document.getAttachments()) {
			String attachmentName = attachment.getName();
			addExistingAttachment(document.getDocumentLocationRealName(), document.getId(), document.getVersionNumber(), attachmentName);
		}
		this.updateValidationRelatedItems(documentType);
	}
	
	private void updateValidationRelatedItems(DocumentTypeModel documentType) {
		
		mandatoryAttachmentBusinessHelper = new GwtMandatoryAttachmentBusinessHelper(documentType, this, generalPanel);
		
		List<String> allowedAttachmentTypes = new ArrayList<String>();
		for (MimeTypeModel mimeType : documentType.getAllowedAttachmentTypes()) {
			this.allowedExtensionsInLowerCase.add(mimeType.getExtension().toLowerCase());
			allowedAttachmentTypes.add(mimeType.toString());
		}
		
		this.allowedAttachmentTypesStatus.setText(GwtLocaleProvider.getConstants().ALLOWED_TYPES() + ": " + GwtFormatUtils.getDelimitedString(allowedAttachmentTypes));
	}
	
	private boolean isAttachmentFormValid() {
		boolean isAttachmentFormValid = true;
		
		String fileName = attachmentFileUploadField.getValue();
		
		if (!GwtValidateUtils.isCompleted(fileName)) {
			isAttachmentFormValid = false;
			ErrorHelper.addError(GwtLocaleProvider.getMessages().REQUIRED_FIELD());
		} else if (!allowedExtensionsInLowerCase.contains(GwtFileUtils.getExtension(fileName).toLowerCase())) {
			isAttachmentFormValid = false;
			ErrorHelper.addError(GwtLocaleProvider.getMessages().ATTACHMENTS_HAVE_TO_BE_THE_RIGHT_TYPE());
		}
		
		return isAttachmentFormValid;
	}
	
	private boolean isUploadAttachmentResponseOk(String responseText) {
		String responseTextWithoutTags = GwtXmlUtils.stripAllTags(responseText);
		return (responseTextWithoutTags.equals(RESPONSE_MESSAGE_OK));
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		this.uploadAttachmentFormPanel.setEnabled(!readOnly && GwtAttachmentPermissionsBusinessUtils.canAddAttachments(this.currentState));
		for (Button deleteButton : this.deleteAttachmentButtonMap.values()) {
			deleteButton.setEnabled(!readOnly && GwtAttachmentPermissionsBusinessUtils.canDeleteAttachments(this.currentState));
		}
	}
	
	@Override
	public boolean isValid() {
		boolean hasRequiredAttachment = true;
		
		if (getMandatoryAttachmentBusinessHelper().isMandatoryAttachment() && this.existingAttachmentLinkMap.isEmpty()) {
			hasRequiredAttachment = false;
			ErrorHelper.addError(GwtLocaleProvider.getMessages().AT_LEAST_ONE_ATTACHMENT_REQUIRED());
		}
		
		return hasRequiredAttachment;
	}
	
	@Override
	public void populate(DocumentModel document) {
		document.setAttachments(new ArrayList<>());
		document.setNamesForAttachmentsToDelete(this.namesForAttachmentsToDelete);
	}
	
	private String getDownloadUrl(String documentLocationRealName, String documentId, Integer versionNumber, String attachmentName) {
		String downloadUrlWithoutParameters = NavigationConstants.getDownloadAttachmentLink();
		String downloadUrl = AttachmentUrlHelper.getUrlWithAttachmentParameters(downloadUrlWithoutParameters, documentLocationRealName, documentId, versionNumber, attachmentName);
		return downloadUrl;
	}
	
	private void addExistingAttachment(final String documentLocationRealName, final String documentId, Integer versionNumber, final String attachmentName) {
		
		final Anchor downloadAttachmentLink = new Anchor();
		downloadAttachmentLink.setText(attachmentName);
		String downloadUrl = getDownloadUrl(documentLocationRealName, documentId, versionNumber, attachmentName);
		downloadAttachmentLink.setHref(downloadUrl);
		
		DocumentWindowRelatedAttachmentInfo attachmentInfo = new DocumentWindowRelatedAttachmentInfo();
		attachmentInfo.setAttachmentName(attachmentName);
		attachmentInfo.setDocumentLocationRealName(documentLocationRealName);
		attachmentInfo.setDocumentId(documentId);
		attachmentInfo.setVersionNumber(versionNumber);
		this.existingAttachmentInfoMap.put(attachmentName, attachmentInfo);
		
		this.existingAttachmentLinkMap.put(attachmentName, downloadAttachmentLink);
		this.existingAttachmentsContainer.add(downloadAttachmentLink);
		
		final Button deleteAttachmentButton = new Button();
		deleteAttachmentButton.setIconStyle("icon-delete");
		deleteAttachmentButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				existingAttachmentInfoMap.remove(attachmentName);
				existingAttachmentLinkMap.remove(attachmentName);
				deleteAttachmentButtonMap.remove(attachmentName);
				namesForAttachmentsToDelete.add(attachmentName);
				existingAttachmentsContainer.remove(downloadAttachmentLink);
				existingAttachmentsContainer.remove(deleteAttachmentButton);
				existingAttachmentsContainer.layout();
			}
		});
		this.deleteAttachmentButtonMap.put(attachmentName, deleteAttachmentButton);
		this.existingAttachmentsContainer.add(deleteAttachmentButton);
		
		this.existingAttachmentsContainer.layout();
	}
	
	public void updateAttachmentRelatedComponentsAfterDocumentSave(
			String documentLocationRealName, String documentId) {
		
		Integer versionNumber = null; // Nu poate fi vorba de o versiune intrucat s-a salvat (modificat) documentul.
		
		for (String attachmentName : this.existingAttachmentLinkMap.keySet()) {
			
			DocumentWindowRelatedAttachmentInfo attachmentInfo = this.existingAttachmentInfoMap.get(attachmentName);
			attachmentInfo.setDocumentLocationRealName(documentLocationRealName);
			attachmentInfo.setDocumentId(documentId);
			attachmentInfo.setVersionNumber(versionNumber);
			
			Anchor existingAttachmentLink = this.existingAttachmentLinkMap.get(attachmentName);
			String newDownloadUrl = getDownloadUrl(documentLocationRealName, documentId, versionNumber, attachmentName);
			existingAttachmentLink.setHref(newDownloadUrl);
		}
	}
	
	@Override
	public WorkflowStateModel getCurrentState() {
		return currentState;
	}
	
	@Override
	public List<DocumentWindowRelatedAttachmentInfo> getAttachmentsInfo() {
		return new ArrayList<DocumentWindowRelatedAttachmentInfo>(existingAttachmentInfoMap.values());
	}
	
	private GwtMandatoryAttachmentBusinessHelper getMandatoryAttachmentBusinessHelper() {
		if (mandatoryAttachmentBusinessHelper == null) {
			throw new IllegalStateException("Helper-ul de business pentru logica atasamentului obligatoriu NU este setat.");
		}
		return mandatoryAttachmentBusinessHelper;
	}
}