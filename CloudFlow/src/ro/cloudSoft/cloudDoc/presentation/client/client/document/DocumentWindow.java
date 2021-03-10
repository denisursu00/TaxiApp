package ro.cloudSoft.cloudDoc.presentation.client.client.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ro.cloudSoft.cloudDoc.presentation.client.client.workflows.ChooseManualDestinationIdWindow;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtCerereDeConcediuConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtCereriDeConcediuConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.NavigationConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowInstanceResponseModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeTemplateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ExtendedDocumentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils.ConfirmCallback;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtCereriDeConcediuBusinessUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtDocumentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtSecurityUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtValidateUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.NavigationUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content.GwtPermissionBusinessUtils;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DocumentWindow extends Window {
	
	private static final int ORIGINAL_WIDTH = 1200;
	private static final int ORIGINAL_HEIGHT = 600;
	
	private AutoNumberValuePreparatorHelper autoNumberValuePreparatorHelper = new AutoNumberValuePreparatorHelper();
	private boolean isDocumentStable = false;
	private boolean canEdit = false;
	private boolean canSend = false;
	
	private List<DocumentTypeTemplateModel> templates  = new ArrayList<DocumentTypeTemplateModel>();
	
	private TabPanel tabPanel = new TabPanel();
	
	private DocumentMainTab mainTab = new DocumentMainTab();
	private DocumentHistoryTab historyTab = new DocumentHistoryTab();
	private DocumentPermissionsTab permissionsTab = new DocumentPermissionsTab();
	private DocumentVersionsTab versionsTab = new DocumentVersionsTab();
	private DocumentWorkflowGraphTab documentWorkflowGraphTab = new DocumentWorkflowGraphTab();
	
	private Button editButton;
	private Button closeButton;
	private Button saveAndCloseButton;
	private Button saveButton;
	private Button sendButton;
	private Button printButton;
	private Button cancelButton;
	private Button anulareCerereDeConcediuButton;
	
	// fluxul pe care este setat tipul de document
	private WorkflowModel workflow;
	
	public DocumentWindow() {
		initWindow();		
		initTabPanel();
		initButtons();
	} 
	
	private void initWindow() {
		this.setHeading(GwtLocaleProvider.getConstants().DOCUMENT());
		this.setSize(ORIGINAL_WIDTH, ORIGINAL_HEIGHT);
		this.setMinWidth(600);
		this.setMinHeight(400);
		this.setLayout(new FitLayout());
		this.setMaximizable(true);
		this.setModal(true);
		this.setConstrain(false);
		addListener(Events.Resize, new Listener<WindowEvent>(){
			public void handleEvent(WindowEvent be) {
				calculateResizePercentage(be.getWidth(), be.getHeight());	
			}			
		});
	}
	
	public void prepareForAdd(final Long documentTypeId, final String documentLocationRealName, final String parentFolderId) {
		
		LoadingManager.get().loading();
		GwtServiceProvider.getDocumentService().clearTemporaryAttachments(new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(Void nothing) {
				
				reset();
				show();
				toFront();
				
				LoadingManager.get().loading();
				GwtServiceProvider.getDocumentTypeService().getDocumentTypeById(documentTypeId, new AsyncCallback<DocumentTypeModel>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					@Override
					public void onSuccess(final DocumentTypeModel documentType) {
						GwtServiceProvider.getWorkflowService().getWorkflowByDocumentType(documentType.getId(), new AsyncCallback<WorkflowModel>(){
							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
								LoadingManager.get().loadingComplete();
							}
							@Override
							public void onSuccess(final WorkflowModel workflowModel) {						
								GwtServiceProvider.getWorkflowService().getCurrentState(workflowModel, null, new AsyncCallback<WorkflowStateModel>() {
									@Override
									public void onFailure(Throwable exception) {
										MessageUtils.displayError(exception);
										LoadingManager.get().loadingComplete();
									}
									@Override
									public void onSuccess(final WorkflowStateModel currentState) {
										GwtServiceProvider.getWorkflowService().checkSendingRights(workflowModel, null, new AsyncCallback<Boolean>() {
											@Override
											public void onFailure(Throwable exception) {
												MessageUtils.displayError(exception);
												LoadingManager.get().loadingComplete();
											}
											@Override
											public void onSuccess(Boolean canSend) {
												DocumentWindow.this.canEdit = true;
												DocumentWindow.this.canSend = canSend;
		
												DocumentWindow.this.templates = documentType.getTemplates();
												
												workflow = workflowModel;
												sendButton.setVisible(workflow != null ? true : false);
												autoNumberValuePreparatorHelper.extractAutoNumberMetadataDefinitionInfo(documentType);
												setHeading(GwtLocaleProvider.getConstants().DOCUMENT() + " " + documentType.getName());
												mainTab.prepareForAdd(documentType, documentLocationRealName, parentFolderId, workflowModel, currentState);
												permissionsTab.prepareForAdd(documentType, documentLocationRealName, parentFolderId, workflowModel, currentState);
											//	historyTab.prepareForAdd(documentType, documentLocationRealName,parentFolderId);
												documentWorkflowGraphTab.prepareForAdd(documentType, documentLocationRealName, parentFolderId, workflowModel, currentState);
												updateHistoryTabVisibility(false);
												updateVersionsTabVisibility(false);
												onAdd();
												
												LoadingManager.get().loadingComplete();
											}
										});
									}
								});
							}
						});
					}
				});
				
				LoadingManager.get().loadingComplete();
			}
		});
	}

	public void prepareForViewOrEdit(final String documentId, final String documentLocationRealName) {
		
		LoadingManager.get().loading();
		GwtServiceProvider.getDocumentService().clearTemporaryAttachments(new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(Void nothing) {
				
			    reset();
			    show();
			    toFront();
			    
			    LoadingManager.get().loading();
			    GwtServiceProvider.getDocumentService().getExtendedDocumentById(documentId, documentLocationRealName, new AsyncCallback<ExtendedDocumentModel>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					@Override
					public void onSuccess(final ExtendedDocumentModel extendeDocumentModel) {
					    DocumentModel document = extendeDocumentModel.getDocumentModel();
					    DocumentTypeModel documentType = extendeDocumentModel.getDocumentTypeModel();
					    
					    DocumentWindow.this.canEdit = GwtPermissionBusinessUtils.canEdit(document.getPermissions(), GwtRegistryUtils.getUserSecurity());
					    DocumentWindow.this.canSend = extendeDocumentModel.getSendingRights();

					    DocumentWindow.this.templates = documentType.getTemplates();

					    workflow = extendeDocumentModel.getWorkflowModel();
					    sendButton.setVisible(workflow != null ? true : false);
					    autoNumberValuePreparatorHelper.extractAutoNumberMetadataDefinitionInfo(documentType);
					    setHeading(GwtLocaleProvider.getConstants().DOCUMENT() + " " + documentType.getName());
					    // TODO
					    document.setDocumentLocationRealName(documentLocationRealName);
			
					    mainTab.prepareForViewOrEdit(documentType, document, extendeDocumentModel.getWorkflowModel(), extendeDocumentModel.getWorkflowStateModel());
						updateHistoryTabVisibility(true);
						permissionsTab.prepareForViewOrEdit(documentType, document, extendeDocumentModel.getWorkflowModel(), extendeDocumentModel.getWorkflowStateModel());
						historyTab.prepareForViewOrEdit(documentType, document);
						//tabul de versiuni apare doar pentru documentele care sunt marcate sa pastreze toate versiunile
						if (documentType.isKeepAllVersions().booleanValue()) {
						    versionsTab.prepareForViewOrEdit(documentType, document, extendeDocumentModel.getWorkflowModel(), extendeDocumentModel.getWorkflowStateModel());
						}
						updateVersionsTabVisibility(documentType.isKeepAllVersions().booleanValue());
						documentWorkflowGraphTab.prepareForViewOrEdit(documentType, document, extendeDocumentModel.getWorkflowModel(), extendeDocumentModel.getWorkflowStateModel());
						
					    if (GwtDocumentUtils.isLocked(document) && GwtSecurityUtils.canAccessLockedDocument(document, GwtRegistryUtils.getUserSecurity())) {
							onEdit();
					    } else {
							onView();
					    }
					    
					    LoadingManager.get().loadingComplete();
					}
			    });
			    
			    LoadingManager.get().loadingComplete();
			}
		});
	}
								
	public void prepareForViewVersion(final String versionNR, final String documentId, final String documentLocationRealName) {
		
		LoadingManager.get().loading();
		GwtServiceProvider.getDocumentService().clearTemporaryAttachments(new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(Void nothing) {
				
				reset();
				show();
				toFront();
				
				GwtServiceProvider.getDocumentService().getDocumentFromVersion(versionNR, documentId, documentLocationRealName,  new AsyncCallback<DocumentModel>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
					}
					@Override
					public void onSuccess(final DocumentModel document) {
						GwtServiceProvider.getDocumentTypeService().getDocumentTypeById(document.getDocumentTypeId(), new AsyncCallback<DocumentTypeModel>() {
							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
							}
							@Override
							public void onSuccess(final DocumentTypeModel documentType) {
								GwtServiceProvider.getWorkflowService().getWorkflowForDocument(documentLocationRealName, documentId, documentType.getId(), new AsyncCallback<WorkflowModel>(){
									@Override
									public void onFailure(Throwable exception) {
										MessageUtils.displayError(exception);
										LoadingManager.get().loadingComplete();
									}
									@Override
									public void onSuccess(WorkflowModel workflow) {
										autoNumberValuePreparatorHelper.extractAutoNumberMetadataDefinitionInfo(documentType);
				
										DocumentWindow.this.templates = documentType.getTemplates();
										
										document.setDocumentLocationRealName(documentLocationRealName);
										mainTab.prepareForViewOrEdit(documentType, document, workflow, null);
										permissionsTab.prepareForViewOrEdit(documentType, document, workflow, null);
										updateHistoryTabVisibility(false);
										//tabul de versiuni apare doar pentru documentele care sunt marcate sa pastreze toate versiunile
										if (documentType.isKeepAllVersions()) {
											versionsTab.prepareForViewOrEdit(documentType, document, workflow, null);
										}
										updateVersionsTabVisibility(false);
										documentWorkflowGraphTab.prepareForViewOrEdit(documentType, document, workflow, null);
										onViewVersion();
									}
								});
							}
						});
					}
				});
				
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private boolean isValid() {
		boolean isMainTabValid = mainTab.isValid();
		boolean isPermissionsTabValid = permissionsTab.isValid();		
		boolean isVersionTabValid = versionsTab.isValid();
		boolean isDocumentWorkflowGraphTabValid = documentWorkflowGraphTab.isValid();
		return isMainTabValid && isPermissionsTabValid && isVersionTabValid && isDocumentWorkflowGraphTabValid;
	}
	
	private DocumentModel getDocumentFromTabs() {
		DocumentModel document = new DocumentModel();
		mainTab.populate(document);
		permissionsTab.populate(document);
		versionsTab.populate(document);
		historyTab.populate(document);
		documentWorkflowGraphTab.populate(document);
		
		return document;
	}
	
	private void initTabPanel() {
		
		tabPanel.add(mainTab);
		tabPanel.add(historyTab);
		tabPanel.add(permissionsTab);		
		tabPanel.add(versionsTab);
		
		boolean isWorkflowGraphViewGeneratorEnabled = GwtRegistryUtils.getAppComponentsAvailabilityConstants().isWorkflowGraphViewGeneratorEnabled();
		if (isWorkflowGraphViewGeneratorEnabled) {
			tabPanel.add(documentWorkflowGraphTab);
		}
		
		add(tabPanel);
	}
	
	private void initButtons() {
		
		editButton = new Button(GwtLocaleProvider.getConstants().EDIT());
		closeButton = new Button(GwtLocaleProvider.getConstants().CLOSE());
		saveAndCloseButton = new Button(GwtLocaleProvider.getConstants().SAVE_AND_CLOSE());
		saveButton = new Button(GwtLocaleProvider.getConstants().SAVE());
		sendButton = new Button(GwtLocaleProvider.getConstants().SEND());
		printButton = new Button(GwtLocaleProvider.getConstants().EXPORT());
		cancelButton = new Button(GwtLocaleProvider.getConstants().CANCEL());
		anulareCerereDeConcediuButton = new Button(GwtLocaleProvider.getConstants().ANULARE_CERERE_DE_CONCEDIU());
		
		addButtonActions();
		
		ButtonBar buttonBar = getButtonBar();
		
		buttonBar.add(editButton);
		buttonBar.add(closeButton);
		buttonBar.add(saveAndCloseButton);
		buttonBar.add(saveButton);
		buttonBar.add(sendButton);
		buttonBar.add(printButton);
		buttonBar.add(cancelButton);
		buttonBar.add(anulareCerereDeConcediuButton);
	}
	
	private void addButtonActions() {
		editButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				LoadingManager.get().loading();
				GwtServiceProvider.getDocumentService().clearTemporaryAttachments(new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					@Override
					public void onSuccess(Void nothing) {
						final String documentLocationRealName = mainTab.getDocumentLocationRealName();
						final String documentId = mainTab.getDocumentId();
						GwtServiceProvider.getDocumentService().checkout(documentId, documentLocationRealName, new AsyncCallback<DocumentModel>() {
							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
								LoadingManager.get().loadingComplete();
							}
							@Override
							public void onSuccess(final DocumentModel document) {
								GwtServiceProvider.getDocumentTypeService().getDocumentTypeById(document.getDocumentTypeId(), new AsyncCallback<DocumentTypeModel>() {
									@Override
									public void onFailure(Throwable exception) {
										MessageUtils.displayError(exception);
										LoadingManager.get().loadingComplete();
									}
									@Override
									public void onSuccess(final DocumentTypeModel documentType) {
										autoNumberValuePreparatorHelper.extractAutoNumberMetadataDefinitionInfo(documentType);
										GwtServiceProvider.getWorkflowService().getWorkflowForDocument(documentLocationRealName, documentId, documentType.getId(), new AsyncCallback<WorkflowModel>(){
											@Override
											public void onFailure(Throwable exception) {
												MessageUtils.displayError(exception);
												LoadingManager.get().loadingComplete();
											}
											@Override
											public void onSuccess(final WorkflowModel workflow) {
												GwtServiceProvider.getWorkflowService().getCurrentState(workflow, document, new AsyncCallback<WorkflowStateModel>() {
													@Override
													public void onFailure(Throwable exception) {
														MessageUtils.displayError(exception);
														LoadingManager.get().loadingComplete();
													}
													@Override
													public void onSuccess(final WorkflowStateModel currentState) {
														GwtServiceProvider.getWorkflowService().checkSendingRights(workflow, document, new AsyncCallback<Boolean>() {
															@Override
															public void onFailure(Throwable exception) {
																MessageUtils.displayError(exception);
																LoadingManager.get().loadingComplete();
															}
															@Override
															public void onSuccess(Boolean canSend) {
																
																reset();
																
																DocumentWindow.this.canSend = canSend;
																
																DocumentWindow.this.templates = documentType.getTemplates();
																
																DocumentWindow.this.workflow = workflow;
																sendButton.setVisible(workflow != null ? true : false);
																										
																// TODO
																document.setDocumentLocationRealName(documentLocationRealName);
																mainTab.prepareForViewOrEdit(documentType, document, workflow, currentState);
																permissionsTab.prepareForViewOrEdit(documentType, document, workflow, currentState);
																versionsTab.prepareForViewOrEdit(documentType, document, workflow, currentState);
																historyTab.prepareForViewOrEdit(documentType, document);
																documentWorkflowGraphTab.prepareForViewOrEdit(documentType, document, workflow, currentState);
																onEdit();
																
																AppEventController.fireEvent(AppEventType.Document);
																
																LoadingManager.get().loadingComplete();
															}
														});
													}
												});
											};
										});
									}
								});
							}
						});
					}
				});
			}
		});
		closeButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
			public void handleEvent(ButtonEvent be) {
				hide();				
			}
		});
		saveAndCloseButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				if (isValid()) {
					LoadingManager.get().loading();
					DocumentModel document = getDocumentFromTabs();
					Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate = autoNumberValuePreparatorHelper.getDefinitionIdsForValuesToGenerate(document);
					
					GwtServiceProvider.getDocumentService().checkin(document, mainTab.isKeepAllVersions().booleanValue(), document.getParentFolderId(),
							document.getDocumentLocationRealName(), definitionIdsForAutoNumberMetadataValuesToGenerate, document.getNamesForAttachmentsToDelete(),
							new AsyncCallback<String>() {
						
						@Override
						public void onFailure(Throwable exception) {
							MessageUtils.displayError(exception);
							LoadingManager.get().loadingComplete();
						}
						@Override
						public void onSuccess(String documentId) {
							DocumentWindow.this.mainTab.setDocumentId(documentId);
							String documentLocationRealName = DocumentWindow.this.mainTab.getDocumentLocationRealName();
							DocumentWindow.this.mainTab.updateAttachmentRelatedComponentsAfterDocumentSave(documentLocationRealName, documentId);
							onSaveAndClose();
							AppEventController.fireEvent(AppEventType.Document);
							MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().DOCUMENT_SAVED());
							hide();
							LoadingManager.get().loadingComplete();
						}
					});
				} else {
					ErrorHelper.displayErrors();
				}
			}
		});
		saveButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
			public void handleEvent(ButtonEvent be) {
				if (isValid()) {
					DocumentModel document = getDocumentFromTabs();
					LoadingManager.get().loading();
					GwtServiceProvider.getDocumentService().save(document, document.getParentFolderId(), document.getDocumentLocationRealName(), document.getNamesForAttachmentsToDelete(), new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable exception) {
							MessageUtils.displayError(exception);
							LoadingManager.get().loadingComplete();
						}
						@Override
						public void onSuccess(String documentId) {
							DocumentWindow.this.mainTab.setDocumentId(documentId);
							String documentLocationRealName = DocumentWindow.this.mainTab.getDocumentLocationRealName();
							DocumentWindow.this.mainTab.updateAttachmentRelatedComponentsAfterDocumentSave(documentLocationRealName, documentId);
							onSave();
							MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().DOCUMENT_SAVED());
							AppEventController.fireEvent(AppEventType.Document);
							LoadingManager.get().loadingComplete();
						}
					});
				} else {
					ErrorHelper.displayErrors();
				}
			}
		});
		
		sendButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				if (isValid() && workflow != null) {
					sendButton.setEnabled(false);
					final DocumentModel documentModel = getDocumentFromTabs();
					if (!isDocumentStable) {
						LoadingManager.get().loading();
						Collection<Long> definitionIdsForAutoNumberMetadataValuesToGenerate = autoNumberValuePreparatorHelper.getDefinitionIdsForValuesToGenerate(documentModel);
						
						GwtServiceProvider.getDocumentService().checkinAndGetDocument(documentModel, mainTab.isKeepAllVersions().booleanValue(), documentModel.getParentFolderId(),
								documentModel.getDocumentLocationRealName(), definitionIdsForAutoNumberMetadataValuesToGenerate, documentModel.getNamesForAttachmentsToDelete(),
								new AsyncCallback<DocumentModel>() {
							
							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
								LoadingManager.get().loadingComplete();
							}
							@Override
							public void onSuccess(DocumentModel document) {
								sendDocumentToWorkflow(document);
								LoadingManager.get().loadingComplete();
							}
						});
					} else {
						sendDocumentToWorkflow(documentModel);
					}
				} else {
					ErrorHelper.displayErrors();
				}
			}
		});
		
		cancelButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
			public void handleEvent(ButtonEvent be) {
				
				LoadingManager.get().loading();
				
				final String documentLocationRealName = mainTab.getDocumentLocationRealName();
				final String documentId = mainTab.getDocumentId();
				
				GwtServiceProvider.getDocumentService().undoCheckout(documentId, documentLocationRealName, new AsyncCallback<DocumentModel>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					@Override
					public void onSuccess(final DocumentModel document) {
						GwtServiceProvider.getDocumentTypeService().getDocumentTypeById(document.getDocumentTypeId(), new AsyncCallback<DocumentTypeModel>() {
							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
								LoadingManager.get().loadingComplete();
							}
							@Override
							public void onSuccess(final DocumentTypeModel documentType) {
								autoNumberValuePreparatorHelper.extractAutoNumberMetadataDefinitionInfo(documentType);
								GwtServiceProvider.getWorkflowService().getWorkflowForDocument(documentLocationRealName, documentId, documentType.getId(), new AsyncCallback<WorkflowModel>() {
									@Override
									public void onFailure(Throwable exception) {
										MessageUtils.displayError(exception);
										LoadingManager.get().loadingComplete();
									}
									@Override
									public void onSuccess(final WorkflowModel workflow) {
										GwtServiceProvider.getWorkflowService().getCurrentState(workflow, document, new AsyncCallback<WorkflowStateModel>() {
											@Override
											public void onFailure(Throwable exception) {
												MessageUtils.displayError(exception);
												LoadingManager.get().loadingComplete();
											}
											@Override
											public void onSuccess(final WorkflowStateModel currentState) {
												GwtServiceProvider.getWorkflowService().checkSendingRights(workflow, document, new AsyncCallback<Boolean>() {
													@Override
													public void onFailure(Throwable exception) {
														MessageUtils.displayError(exception);
														LoadingManager.get().loadingComplete();
													}
													@Override
													public void onSuccess(Boolean canSend) {
														
														reset();
														
														DocumentWindow.this.canSend = canSend;
														
														DocumentWindow.this.templates = documentType.getTemplates();
														
														DocumentWindow.this.workflow = workflow;
														sendButton.setVisible(workflow != null ? true : false);
														
														// TODO
														document.setDocumentLocationRealName(documentLocationRealName);
														mainTab.prepareForViewOrEdit(documentType, document, workflow, currentState);
														permissionsTab.prepareForViewOrEdit(documentType, document, workflow, currentState);
														versionsTab.prepareForViewOrEdit(documentType, document, workflow, currentState);
														historyTab.prepareForViewOrEdit(documentType, document);
														documentWorkflowGraphTab.prepareForViewOrEdit(documentType, document, workflow, currentState);
														onCancel();
														
														AppEventController.fireEvent(AppEventType.Document);
														
														LoadingManager.get().loadingComplete();
													}
												});
											}
										});
									}
								});
							}
						});
					}
				});
			}
		});
		
		anulareCerereDeConcediuButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent be) {
				ComponentUtils.confirm(GwtLocaleProvider.getConstants().ANULARE_CERERE_DE_CONCEDIU(),
						GwtLocaleProvider.getMessages().CONFIRM_ANULARE_CERERE_DE_CONCEDIU(),
						new ConfirmCallback() {
					
					@Override
					public void onYes() {
						
						String documentLocationRealName = mainTab.getDocumentLocationRealName();
						String documentId = mainTab.getDocumentId();
						
						LoadingManager.get().loading();
						GwtServiceProvider.getCereriDeConcediuService().anuleaza(documentLocationRealName, documentId, new AsyncCallback<Void>() {
							
							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
								LoadingManager.get().loadingComplete();
							}
							
							@Override
							public void onSuccess(Void nothing) {
								
								MessageUtils.display(GwtLocaleProvider.getConstants().ANULARE_CERERE_DE_CONCEDIU(),
									GwtLocaleProvider.getMessages().CEREREA_DE_CONCEDIU_A_FOST_ANULATA());
								hide();
								
								AppEventController.fireEvent(AppEventType.Document);
								AppEventController.fireEvent(AppEventType.Task);
								
								LoadingManager.get().loadingComplete();
							}
						});
					}
				});
			}
		});
		
		printButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				
				if (DocumentWindow.this.templates.isEmpty()) {
					return;
				}				

				final String documentLocationRealName = DocumentWindow.this.mainTab.getDocumentLocationRealName();
				final String documentId = DocumentWindow.this.mainTab.getDocumentId();
				
				if (DocumentWindow.this.templates.size() == 1) {
					String templateName = DocumentWindow.this.templates.get(0).getName();
					goToPrintDocument(documentLocationRealName, documentId, templateName);
				} else {
					ChooseTemplateWindow chooseTemplateWindow = new ChooseTemplateWindow(DocumentWindow.this.templates) {
						
						@Override
						protected void doWhenOkPressed(String templateName) {
							goToPrintDocument(documentLocationRealName, documentId, templateName);
						}
					};
					chooseTemplateWindow.prepareForSelect();
				}
			}
		});
	}
	
	private void goToPrintDocument(String documentLocationRealName, String documentId, String templateName) {
		
		String url = NavigationConstants.getExportDocumentLink();
		String parameters = "?documentLocationRealName=" + documentLocationRealName + "&documentId=" + documentId + "&templateName=" + templateName;
		
		NavigationUtils.redirect(url + parameters);
	}
	
	private void sendDocumentToWorkflow(final DocumentModel documentModel) {
		LoadingManager.get().loading();
		GwtServiceProvider.getDocumentService().sendDocumentToWorkflow(workflow.getId(), null, null, documentModel, new AsyncCallback<WorkflowInstanceResponseModel>() {
			@Override
			public void onFailure(Throwable exception) {
				sendButton.setEnabled(true);
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(WorkflowInstanceResponseModel response) {
				
				List<String> possibleTransitionNames = response.getCandidateTransitionNames();
				boolean manualAssignment = response.getManualAssignment();							
				
				// daca primul task dupa pornirea fluxului e cu asignare manuala
				// atunci va trebui sa il pun sa aleaga userul destinatie
				if (manualAssignment) {
					sendManualDestinationId(workflow, null, documentModel);
				}
				
				// daca vin tranzitii							
				if (possibleTransitionNames != null && !possibleTransitionNames.isEmpty()) {
					ChooseTransitionWindow chooseTransitionWindow = new ChooseTransitionWindow(possibleTransitionNames) {
						@Override
						public void doWhenOkPressed(String transitionName) {
							LoadingManager.get().loading();
							GwtServiceProvider.getDocumentService().sendDocumentToWorkflow(workflow.getId(), transitionName, null, documentModel, new AsyncCallback<WorkflowInstanceResponseModel>() {
								@Override
								public void onFailure(Throwable exception) {
									sendButton.setEnabled(true);
									MessageUtils.displayError(exception);
									LoadingManager.get().loadingComplete();
								}
								@Override
								public void onSuccess(WorkflowInstanceResponseModel response) {
									// daca dupa alegerea tranzitiei exista un task cu asignare manuala atunci
									// trebuie sa aleaga user-ul caruia i se va face asignare
									final String transitionName = response.getChosenTransitionName();
									if (response.getManualAssignment()) {
										sendManualDestinationId(workflow, transitionName, documentModel);
									}else {
										if (response.isWorkflowFinished()) {
											MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().DOCUMENT_WORKFLOW_FINISHED());
										} else {
											MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().DOCUMENT_SENT());
										}
										AppEventController.fireEvent(AppEventType.Document);
										AppEventController.fireEvent(AppEventType.Task);
										onSend();
										DocumentWindow.this.hide();
									}
									LoadingManager.get().loadingComplete();
								}
							});
						}
					};
					toBack();
					chooseTransitionWindow.show();
					chooseTransitionWindow.toFront();
				}
				
				if (!manualAssignment && (possibleTransitionNames == null || possibleTransitionNames.isEmpty())) {
					if (response.isWorkflowFinished()) {
						MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().DOCUMENT_WORKFLOW_FINISHED());
					} else {
						MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().DOCUMENT_SENT());
					}
					AppEventController.fireEvent(AppEventType.Document);
					AppEventController.fireEvent(AppEventType.Task);
					onSend();
					DocumentWindow.this.hide();
				}
				
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private void reset() {
		this.setHeading(GwtLocaleProvider.getConstants().DOCUMENT());
		
		this.templates.clear();
		
		this.tabPanel.setSelection(mainTab);
		this.workflow = null;
	}
	
	private void setReadOnly(boolean readOnly) {
		mainTab.setReadOnly(readOnly);
		permissionsTab.setReadOnly(readOnly);
		versionsTab.setReadOnly(readOnly);
		historyTab.setReadOnly(readOnly);
		documentWorkflowGraphTab.setReadOnly(readOnly);
	}
	
	private void onAdd() {
		
		isDocumentStable = false;
		// formularul
		setReadOnly(false);
		// butoanele
		editButton.setEnabled(false);
		saveAndCloseButton.setEnabled(true);
		saveButton.setEnabled(true);
		sendButton.setEnabled(this.canSend);
		this.printButton.setEnabled(false);
		cancelButton.setEnabled(false);
		
		anulareCerereDeConcediuButton.setVisible(isAnulareCerereDeConcediuButtonVisible(Operation.ADD));
		
		mainTab.onAdd();
	}
	
	private void onView() {
		
		isDocumentStable = true;
		// formularul
		setReadOnly(true);
		// butoanele
		editButton.setEnabled(this.canEdit);
		saveAndCloseButton.setEnabled(false);
		saveButton.setEnabled(false);
		sendButton.setEnabled(this.canSend);
		this.printButton.setEnabled(!this.templates.isEmpty());
		cancelButton.setEnabled(false);
		
		anulareCerereDeConcediuButton.setVisible(isAnulareCerereDeConcediuButtonVisible(Operation.OTHER_THAN_ADD));
	}
	
	private void onViewVersion() {
		
		isDocumentStable = true;
		// formularul
		setReadOnly(true);
		// butoanele
		editButton.setEnabled(false);
		saveAndCloseButton.setEnabled(false);
		saveButton.setEnabled(false);
		sendButton.setEnabled(false);
		printButton.setEnabled(false);
		cancelButton.setEnabled(false);
		
		anulareCerereDeConcediuButton.setVisible(isAnulareCerereDeConcediuButtonVisible(Operation.OTHER_THAN_ADD));
	}
	
	private void onEdit() {
		
		isDocumentStable = false;
		// formularul
		setReadOnly(false);
		// butoanele
		editButton.setEnabled(false);
		saveAndCloseButton.setEnabled(true);
		saveButton.setEnabled(true);
		sendButton.setEnabled(this.canSend);
		this.printButton.setEnabled(!this.templates.isEmpty());
		cancelButton.setEnabled(canCancel());
		
		anulareCerereDeConcediuButton.setVisible(isAnulareCerereDeConcediuButtonVisible(Operation.OTHER_THAN_ADD));
		
		mainTab.onEdit();
	}
	
	private void onSaveAndClose() {
		
		isDocumentStable = true;
		// formularul
		setReadOnly(true);
		// butoanele
		editButton.setEnabled(this.canEdit);
		saveAndCloseButton.setEnabled(false);
		saveButton.setEnabled(false);
		sendButton.setEnabled(this.canSend);
		this.printButton.setEnabled(!this.templates.isEmpty());
		cancelButton.setEnabled(false);
		
		anulareCerereDeConcediuButton.setVisible(isAnulareCerereDeConcediuButtonVisible(Operation.OTHER_THAN_ADD));
	}
	
	private void onSave() {
		
		isDocumentStable = false;
		// formularul
		setReadOnly(false);
		// butoanele
		editButton.setEnabled(false);
		saveAndCloseButton.setEnabled(true);
		saveButton.setEnabled(true);
		sendButton.setEnabled(this.canSend);
		this.printButton.setEnabled(!this.templates.isEmpty());
		cancelButton.setEnabled(canCancel());
		
		anulareCerereDeConcediuButton.setVisible(isAnulareCerereDeConcediuButtonVisible(Operation.OTHER_THAN_ADD));
	}
	
	private void onCancel() {
		
		isDocumentStable = true;
		// formularul
		setReadOnly(true);
		// butoanele
		editButton.setEnabled(this.canEdit);
		saveAndCloseButton.setEnabled(false);
		saveButton.setEnabled(false);
		sendButton.setEnabled(this.canSend);
		this.printButton.setEnabled(!this.templates.isEmpty());
		cancelButton.setEnabled(false);
		
		anulareCerereDeConcediuButton.setVisible(isAnulareCerereDeConcediuButtonVisible(Operation.OTHER_THAN_ADD));
	}
	
	private void onSend() {
		
		isDocumentStable = true;
		// formularul
		setReadOnly(true);
		// butoanele
		editButton.setEnabled(this.canEdit);
		saveAndCloseButton.setEnabled(false);
		saveButton.setEnabled(false);
		sendButton.setEnabled(false);
		this.printButton.setEnabled(!this.templates.isEmpty());
		cancelButton.setEnabled(false);
		
		anulareCerereDeConcediuButton.setVisible(isAnulareCerereDeConcediuButtonVisible(Operation.OTHER_THAN_ADD));
	}
	
	private boolean isAnulareCerereDeConcediuButtonVisible(Operation operation) {
		
		if (operation == Operation.ADD) {
			return false;
		}
		
		GwtCereriDeConcediuConstants constants = GwtRegistryUtils.getCereriDeConcediuConstants();
		
		Long documentTypeId = mainTab.getDocumentTypeId();
		if (!constants.isCerereDeConcediu(documentTypeId)) {
			return false;
		}
		
		String authorUserIdAsString = mainTab.getAuthorUserIdAsString();
		if (!GwtCereriDeConcediuBusinessUtils.hasPermissionForAnulare(GwtRegistryUtils.getUserSecurity(), authorUserIdAsString)) {
			return false;
		}
		
		GwtCerereDeConcediuConstants constantsForCerere = constants.getFor(documentTypeId);
		String anulataMetadataValue = mainTab.getMetadataValue(constantsForCerere.getAnulataMetadataId());		
		if ((anulataMetadataValue != null) && anulataMetadataValue.equals(constantsForCerere.getAnulataMetadataPositiveValue())) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Verifica daca se poate efectua operatia de anulare.
	 * @return <code>true</code> daca se poate efectua operatia de anulare,
	 * <code>false</code> altfel
	 */
	private boolean canCancel() {
		return GwtValidateUtils.isTrue(mainTab.getHasStableVersion());
	}
	
	/**
	 * Deschide o fereastra pentru alegerea unui user din organigrama si iltrimite pe flux.
	 * @param workflow
	 * @param transitionName
	 * @param documentModel
	 */
	private void sendManualDestinationId(final WorkflowModel workflow, final String transitionName, final DocumentModel documentModel) {
		ChooseManualDestinationIdWindow chooseManualDestinationIdWindow = new ChooseManualDestinationIdWindow() {
			@Override
			public void onSendDestinationIdToWorkflow(String destinationId) {
				LoadingManager.get().loading();
				GwtServiceProvider.getDocumentService().sendDocumentToWorkflow(workflow.getId(), transitionName, destinationId, documentModel, new AsyncCallback<WorkflowInstanceResponseModel>() {
					@Override
					public void onFailure(Throwable exception) {
						sendButton.setEnabled(true);
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					@Override
					public void onSuccess(WorkflowInstanceResponseModel response) {
						MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().DOCUMENT_SENT());
						AppEventController.fireEvent(AppEventType.Document);
						AppEventController.fireEvent(AppEventType.Task);
						onSend();
						DocumentWindow.this.hide();
						LoadingManager.get().loadingComplete();
					}
				});
			};
		};
		toBack();
		chooseManualDestinationIdWindow.show();
		chooseManualDestinationIdWindow.toFront();
	}
	
	/**
	 * Schimba vizibilitatea tab-ului cu versiunile.
	 * @param mustBeVisible daca trebuie ca tab-ul sa fie vizibil
	 */
	private void updateVersionsTabVisibility(boolean mustBeVisible) {
		/*if (mustBeVisible) {
			if (!this.tabPanel.getItems().contains(this.versionsTab)) {
				this.tabPanel.add(this.versionsTab);
			}
		} else {
			if (this.tabPanel.getItems().contains(this.versionsTab)) {
				this.tabPanel.remove(this.versionsTab);
			}
		}*/
		this.versionsTab.setEnabled(mustBeVisible);
	}
	/**
	 * Schimba vizibilitatea tab-ului cu istoricul.
	 * @param mustBeVisible daca trebuie ca tab-ul sa fie vizibil
	 */
	private void updateHistoryTabVisibility(boolean mustBeVisible) {
		/*if (mustBeVisible) {
			if (!this.tabPanel.getItems().contains(this.historyTab)) {
				this.tabPanel.add(this.historyTab);
			}
		} else {
			if (this.tabPanel.getItems().contains(this.historyTab)) {
				this.tabPanel.remove(this.historyTab);
			}
		}*/
		this.historyTab.setEnabled(mustBeVisible);
	}
	
	private void calculateResizePercentage(int w, int h){
		float widthP = (float)w / (float)ORIGINAL_WIDTH;
		float heightP = (float)h / (float)ORIGINAL_HEIGHT;		
		permissionsTab.changeSize(widthP, heightP);
	}
	
	/**
	 * Clasa este specializata in prepararea valorilor metadatelor noi de tip
	 * auto number, pentru a fi generate.
	 * 
	 * 
	 */
	private static class AutoNumberValuePreparatorHelper {
		
		private Set<Long> allAutoNumberMetadataDefinitionIdsForDocumentType = new HashSet<Long>();
		
		/** Extrage informatiile necesare din tipul de document, legate de definitiile metadatelor de tip auto number. */
		public void extractAutoNumberMetadataDefinitionInfo(DocumentTypeModel documentType) {
			
			allAutoNumberMetadataDefinitionIdsForDocumentType.clear();
			
			for (MetadataDefinitionModel metadataDefinition : documentType.getMetadataDefinitions()) {
				if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_AUTO_NUMBER)) {
					allAutoNumberMetadataDefinitionIdsForDocumentType.add(metadataDefinition.getId());
				}
			}
		}
		
		public Collection<Long> getDefinitionIdsForValuesToGenerate(DocumentModel document) {
			Map<Long, MetadataInstanceModel> metadataInstanceMap = document.getMetadataInstanceMap();
			Set<Long> definitionIdsForValuesToGenerate = new HashSet<Long>();
			for (Long metadataDefinitionId : allAutoNumberMetadataDefinitionIdsForDocumentType) {
				if (metadataInstanceMap.get(metadataDefinitionId) == null) {
					definitionIdsForValuesToGenerate.add(metadataDefinitionId);
				}
			}
			return definitionIdsForValuesToGenerate;
		}
	}
	
	private static enum Operation { ADD, OTHER_THAN_ADD }
}