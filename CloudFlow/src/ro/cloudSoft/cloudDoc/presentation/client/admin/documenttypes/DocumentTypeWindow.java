package ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils.ConfirmCallback;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DocumentTypeWindow extends Window {
	
	private TabPanel tabPanel = new TabPanel();

	private GeneralTab generalTab = new GeneralTab();
	private InitiatorsTab initiatorsTab = new InitiatorsTab();
	private AttachmentsTab attachmentsTab = new AttachmentsTab();
	private MetadataTab metadataTab = new MetadataTab();
	private CollectionsTab collectionsTab = new CollectionsTab(metadataTab);

	private Button saveButton;
	private Button cancelButton;
	
	public DocumentTypeWindow() {		
		initWindow();
		initTabs();
		initButtons();
		addButtonActions();
	}

	private void initWindow() {
		setSize(1024, 680);
		setMinWidth(600);
		setMinHeight(450);
		setMaximizable(true);
		setModal(true);
		setLayout(new FitLayout());
	}

	private void initTabs() {
		
		tabPanel.add(generalTab);
		tabPanel.add(initiatorsTab);
		tabPanel.add(attachmentsTab);
		tabPanel.add(metadataTab);
		tabPanel.add(collectionsTab);
		
		add(tabPanel);
	}
	
	private void initButtons() {
		saveButton = new Button();
		saveButton.setText(GwtLocaleProvider.getConstants().SAVE());
		cancelButton = new Button();
		cancelButton.setText(GwtLocaleProvider.getConstants().CANCEL());
		
		getButtonBar().add(saveButton);
		getButtonBar().add(cancelButton);
	}
	
	private void addButtonActions() {
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				if (isValid()) {
					
					final DocumentTypeModel documentTypeToSave = getModelFromForms();
					
					Long documentTypeId = documentTypeToSave.getId();
					boolean isEdit = (documentTypeId != null);
					if (isEdit) {
						LoadingManager.get().loading();
						GwtServiceProvider.getDocumentService().existDocumentsOfType(documentTypeId, new AsyncCallback<Boolean>() {
							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
								LoadingManager.get().loadingComplete();
							}
							@Override
							public void onSuccess(Boolean documentsOfTypeExist) {
								if (documentsOfTypeExist) {
									ComponentUtils.confirm(GwtLocaleProvider.getConstants().SAVE(),
											GwtLocaleProvider.getMessages().CONFIRM_EDIT_DOCUMENT_TYPE_DOCUMENTS_EXIST(),
											new ConfirmCallback() {
										
										@Override
										public void onYes() {
											saveDocumentType(documentTypeToSave);
										}
									});
								} else {
									saveDocumentType(documentTypeToSave);
								}
								LoadingManager.get().loadingComplete();
							}
						});
					} else {
						saveDocumentType(documentTypeToSave);
					}
				} else {
					ErrorHelper.displayErrors();
				}
			}
			
			private void saveDocumentType(DocumentTypeModel documentTypeToSave) {
				LoadingManager.get().loading();
				GwtServiceProvider.getDocumentTypeService().saveDocumentType(
						documentTypeToSave, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					@Override
					public void onSuccess(Void nothing) {
						MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().DOCUMENT_TYPE_SAVED());
						AppEventController.fireEvent(AppEventType.DocumentType);
						LoadingManager.get().loadingComplete();
						hide();
					}
				});
			}
		});
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				hide();
			};
		});
	}
	
	private boolean isValid() {
		boolean generalTabIsValid = generalTab.isValid();
		boolean initiatorsTabIsValid = initiatorsTab.isValid();
		boolean attachmentsTabIsValid = attachmentsTab.isValid();
		boolean metadataTabIsValid = metadataTab.isValid();
		boolean collectionsTabIsValid = collectionsTab.isValid();
		return generalTabIsValid & initiatorsTabIsValid & attachmentsTabIsValid
			& metadataTabIsValid & collectionsTabIsValid;
	}
	
	/**
	 * Pregateste fereastra pentru adaugare sau modificare.
	 * <br><br>
	 * NOTA: Aceasta metoda trebuie apelata inainte actiunilor propriu-zise pentru ca face "curatenie" in spatiul
	 * temporar de pe server. 
	 * 
	 * @param doAfter ce sa faca dupa pregatire
	 */
	private void prepareForAddOrEdit(final Runnable doAfter) {
		LoadingManager.get().loading();
		GwtServiceProvider.getDocumentTypeService().prepareForAddOrEdit(new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(Void nothing) {
				doAfter.run();
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	public void prepareForAdd() {
		prepareForAddOrEdit(new Runnable() {
			@Override
			public void run() {
				setHeading(GwtLocaleProvider.getConstants().DOC_TYPE()+" : ");
				show();
				generalTab.prepareForAdd();
				initiatorsTab.prepareForAdd();
				attachmentsTab.prepareForAdd();
				metadataTab.prepareForAdd();
				collectionsTab.prepareForAdd();
			}
		});
	}
	
	public void prepareForEdit(final Long documentTypeId) {
		prepareForAddOrEdit(new Runnable() {
			@Override
			public void run() {
				LoadingManager.get().loading();
				GwtServiceProvider.getDocumentTypeService().getDocumentTypeById(documentTypeId, new AsyncCallback<DocumentTypeModel>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					@Override
					public void onSuccess(final DocumentTypeModel documentType) {
						
						GwtServiceProvider.getWorkflowService().getWorkflowStatesByDocumentType(documentTypeId, new AsyncCallback<List<WorkflowStateModel>>() {
							
							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
								LoadingManager.get().loadingComplete();								
							}
							
							@Override
							public void onSuccess(List<WorkflowStateModel> workflowStates) {
								
								setHeading(GwtLocaleProvider.getConstants().DOC_TYPE() + " : " + documentType.getName());
								show();
								
								generalTab.prepareForEdit(documentType);
								initiatorsTab.prepareForEdit(documentType);
								attachmentsTab.prepareForEdit(documentType, workflowStates);
								metadataTab.prepareForEdit(documentType, workflowStates);
								collectionsTab.prepareForEdit(documentType, workflowStates);
								
								LoadingManager.get().loadingComplete();
							}
						});
					}
				});
			}
		});
	}
	
	private DocumentTypeModel getModelFromForms() {
		DocumentTypeModel documentType = new DocumentTypeModel();
		
		generalTab.populate(documentType);
		initiatorsTab.populate(documentType);
		attachmentsTab.populate(documentType);
		metadataTab.populate(documentType);
		collectionsTab.populate(documentType);
		
		return documentType;
	}
}