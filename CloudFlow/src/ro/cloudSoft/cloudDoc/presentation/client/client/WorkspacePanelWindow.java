package ro.cloudSoft.cloudDoc.presentation.client.client;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.client.document.DocumentWindow;
import ro.cloudSoft.cloudDoc.presentation.client.client.documentlocation.DocumentLocationWindow;
import ro.cloudSoft.cloudDoc.presentation.client.client.folder.FolderWindow;
import ro.cloudSoft.cloudDoc.presentation.client.client.utils.widgets.WindowToBeUsedOnDesktop;
import ro.cloudSoft.cloudDoc.presentation.client.shared.CustomGridCellRenderer;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.ModelConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEvent;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventHandler;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.DocumentViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils.ConfirmCallback;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtBooleanUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtValidateUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.DeleteConfirmDialog;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.DocumentLocationComboBox;

import com.extjs.gxt.ui.client.Style.ButtonScale;
import com.extjs.gxt.ui.client.Style.IconAlign;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.dnd.DragSource;
import com.extjs.gxt.ui.client.dnd.DropTarget;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel.TreeNode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class WorkspacePanelWindow extends WindowToBeUsedOnDesktop implements AppEventHandler {
	
	/**
	 * Reprezinta tipul de documente permise de adaugat in folder-ul selectat.
	 * Daca sunt permise toate tipurile, acesta este <code>null</code>.
	 */
	private DocumentTypeModel documentType;

	private DocumentLocationWindow documentLocationWindow = new DocumentLocationWindow();
	private FolderWindow folderWindow = new FolderWindow();
	private DocumentWindow documentWindow = WindowManager.getDocumentWindow();

	private ContentPanel leftPanel;
	private ContentPanel centerPanel;
	private Status bottomStatusBar;

	// elementele din leftPanel
	private VerticalPanel toolBarPanel;
	private FolderTreePanelWrapper folderTreePanelWrapper;

	// elementele din toolBarPanel
	private ToolBar documentLocationToolBar;
	private ToolBar folderToolBar;

	// elementele din documentLocationToolBar
	private DocumentLocationComboBox documentLocationComboBox;
	private Button addDocumentLocationButton;
	private Button editDocumentLocationButton;
	private Button deleteDocumentLocationButton;

	// elementele din folderToolBar
	private Button addFolderButton;
	private Button editFolderButton;
	private Button deleteFolderButton;

	// elementele din centerPanel
	private ToolBar documentToolBar;
	private ListStore<DocumentViewModel> documentsStore;
	private Grid<DocumentViewModel> documentsGrid;
	private ListView<DocumentViewModel> documentsListView;
	private PagingToolBar documentsPagingToolBar;
	// elementele din documentToolBar
	private AddDocButton addDocumentButton;
	private Button deleteDocumentButton;
	private ToggleButton iconsDocumentsViewButton;
	private ToggleButton detailsDocumentsViewButton;

	public WorkspacePanelWindow() {

		setHeading(GwtLocaleProvider.getConstants().WORKSPACE_PANEL());
		setLayout(new BorderLayout());
		setMaximizable(true);
		setMinimizable(true);
		setSize(1100, 600);

		initLeftPanel();
		initCenterPanel();

		bottomStatusBar = new Status();
		add(bottomStatusBar, new BorderLayoutData(LayoutRegion.SOUTH, 24));

		addDragAndDropMoveSupport();
		
		// La afisarea ferestrei...
		addListener(Events.Show, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				// Reincarca lista cu tipurile disponibile de document.				
				addDocumentButton.reloadAvailableDocumentTypes();
				//dezactivez butonul de adaugare document pentru ca accesul la el e conditionat de selectarea unui folder
				addDocumentButton.setEnabled(false);
				// Reincarca lista cu document locations.
				documentLocationComboBox.getStore().getLoader().load();				
			}
		});

		AppEventController.subscribe(this, AppEventType.DocumentLocation);
		AppEventController.subscribe(this, AppEventType.Folder);
		AppEventController.subscribe(this, AppEventType.Document);
	}

	private void initLeftPanel() {
		leftPanel = new ContentPanel();
		leftPanel.setHeaderVisible(false);
		leftPanel.setLayout(new FitLayout());

		toolBarPanel = new VerticalPanel();

		initWorkspaceToolBar();
		initFolderToolBar();

		folderTreePanelWrapper = new FolderTreePanelWrapper();
		folderTreePanelWrapper.getTreePanel().getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<ModelData>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<ModelData> sce) {
				ModelData selectedItem = sce.getSelectedItem();
				FolderModel selectedFolder = (selectedItem instanceof FolderModel) ? (FolderModel) selectedItem : null;
				if (selectedFolder != null) {
					addDocumentButton.updateAvailableMenuOptions(selectedFolder.getDocumentTypeId());
				} else {
					addDocumentButton.setEnabled(false);
				}
				populateDocumentStore(selectedFolder);
				updateStatus();
			}
		});

		leftPanel.setTopComponent(toolBarPanel);
		leftPanel.add(folderTreePanelWrapper);

		add(leftPanel, new BorderLayoutData(LayoutRegion.WEST, 340));
	}

	private void initWorkspaceToolBar() {
		documentLocationToolBar = new ToolBar();
		documentLocationToolBar.setWidth(340);

		documentLocationComboBox = new DocumentLocationComboBox();
		documentLocationComboBox.addSelectionChangedListener(new SelectionChangedListener<DocumentLocationModel>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<DocumentLocationModel> event) {
				reloadFolders();
				populateDocumentStore(null);
				updateStatus();
				addDocumentButton.setEnabled(false);
			}
		});
		/*
		 * Dupa incarcarea listei cu document locations, daca exista cel putin
		 * un element, va fi selectat primul.
		 */
		documentLocationComboBox.getStore().getLoader().addLoadListener(new LoadListener() {
			@Override
			public void loaderLoad(LoadEvent le) {
				List<DocumentLocationModel> documentLocations = documentLocationComboBox.getStore().getModels();
				if (!documentLocations.isEmpty()) {
					documentLocationComboBox.setValue(documentLocations.get(0));
				}
			}
		});

		addDocumentLocationButton = new Button();
		addDocumentLocationButton.setText(GwtLocaleProvider.getConstants().ADD_WORKSPACE());
		addDocumentLocationButton.setIconStyle("add_workspace");
		addDocumentLocationButton.setToolTip(GwtLocaleProvider.getConstants().ADD_WORKSPACE_TOOLTIP());
		addDocumentLocationButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				toBack();
				documentLocationWindow.prepareForAdd();
			}
		});

		editDocumentLocationButton = new Button();
		editDocumentLocationButton.setText(GwtLocaleProvider.getConstants().EDIT_WORKSPACE());
		editDocumentLocationButton.setIconStyle("edit_workspace");
		editDocumentLocationButton.setToolTip(GwtLocaleProvider.getConstants().EDIT_WORKSPACE_TOOLTIP());
		editDocumentLocationButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				DocumentLocationModel selectedDocumentLocation = documentLocationComboBox.getValue();
				if (selectedDocumentLocation != null) {
					toBack();
					documentLocationWindow.prepareForEdit(selectedDocumentLocation.getRealName());
				}
			}
		});

		deleteDocumentLocationButton = new Button();
		deleteDocumentLocationButton.setText(GwtLocaleProvider.getConstants().DEL_WORKSPACE());
		deleteDocumentLocationButton.setIconStyle("del_workspace");
		deleteDocumentLocationButton.setToolTip(GwtLocaleProvider.getConstants().DEL_WORKSPACE_TOOLTIP());
		deleteDocumentLocationButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				final DocumentLocationModel selectedDocumentLocation = documentLocationComboBox.getValue();
				if (selectedDocumentLocation != null) {
					ComponentUtils.confirm(GwtLocaleProvider.getMessages().DELETE_DOCUMENT_LOCATION_CONFIRM_TITLE(),
							GwtLocaleProvider.getMessages().DELETE_DOCUMENT_LOCATION_CONFIRM_QUESTION(), new ConfirmCallback() {
						@Override
						public void onYes() {
							LoadingManager.get().loading();
							GwtServiceProvider.getDocumentLocationService().deleteDocumentLocation(selectedDocumentLocation.getRealName(), new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable exception) {
									MessageUtils.displayError(exception);
									LoadingManager.get().loadingComplete();
								}

								@Override
								public void onSuccess(Void nothing) {
									MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().DOCUMENT_LOCATION_DELETED());
									AppEventController.fireEvent(AppEventType.DocumentLocation);
									LoadingManager.get().loadingComplete();
								}
							});
						}
					});
				}
			}
		});

		documentLocationToolBar.add(documentLocationComboBox);
		documentLocationToolBar.add(addDocumentLocationButton);
		documentLocationToolBar.add(editDocumentLocationButton);
		documentLocationToolBar.add(deleteDocumentLocationButton);

		toolBarPanel.add(documentLocationToolBar);
	}

	private void initFolderToolBar() {
		folderToolBar = new ToolBar();
		folderToolBar.setWidth(340);

		addFolderButton = new Button();
		addFolderButton.setText(GwtLocaleProvider.getConstants().ADD_FOLDER());
		addFolderButton.setIconStyle("add_folder");
		addFolderButton.setToolTip(GwtLocaleProvider.getConstants().ADD_FOLDER_TOOLTIP());
		addFolderButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				DocumentLocationModel selectedDocumentLocation = documentLocationComboBox.getValue();
				FolderModel selectedFolder = folderTreePanelWrapper.getSelectedFolder();
				String selectedFolderId = (selectedFolder != null) ? selectedFolder.getId() : null;
				if (selectedDocumentLocation != null) {
					toBack();
					folderWindow.prepareForAdd(selectedDocumentLocation.getRealName(), getCurrentPath(), selectedFolderId);
				}
			}
		});

		editFolderButton = new Button();
		editFolderButton.setText(GwtLocaleProvider.getConstants().EDIT_FOLDER());
		editFolderButton.setIconStyle("edit_folder");
		editFolderButton.setToolTip(GwtLocaleProvider.getConstants().EDIT_FOLDER_TOOLTIP());
		editFolderButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				DocumentLocationModel selectedDocumentLocation = documentLocationComboBox.getValue();
				FolderModel selectedFolder = folderTreePanelWrapper.getSelectedFolder();
				String selectedFolderId = (selectedFolder != null) ? selectedFolder.getId() : null;
				if ((selectedDocumentLocation != null) && (selectedFolderId != null)) {
					toBack();
					folderWindow.prepareForEdit(selectedDocumentLocation.getRealName(), getCurrentPath(), selectedFolderId);
				}
			}
		});

		deleteFolderButton = new Button();
		deleteFolderButton.setText(GwtLocaleProvider.getConstants().DEL_FOLDER());
		deleteFolderButton.setIconStyle("del_folder");
		deleteFolderButton.setToolTip(GwtLocaleProvider.getConstants().DEL_FOLDER_TOOLTIP());
		deleteFolderButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				final DocumentLocationModel selectedDocumentLocation = documentLocationComboBox.getValue();
				final FolderModel selectedFolder = folderTreePanelWrapper.getSelectedFolder();
				if (selectedFolder != null) {
					ComponentUtils.confirm(GwtLocaleProvider.getMessages().DELETE_FOLDER_CONFIRM_TITLE(),
							GwtLocaleProvider.getMessages().DELETE_FOLDER_CONFIRM_QUESTION(), new ConfirmCallback() {
						@Override
						public void onYes() {
							LoadingManager.get().loading();
							GwtServiceProvider.getFolderService().deleteFolder(selectedFolder.getId(),
									selectedDocumentLocation.getRealName(), new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable exception) {
									MessageUtils.displayError(exception);
									LoadingManager.get().loadingComplete();
								}
								@Override
								public void onSuccess(Void nothing) {
									MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().FOLDER_DELETED());
									AppEventController.fireEvent(AppEventType.Folder);
									LoadingManager.get().loadingComplete();
								}
							});
						}
					});
				}
			}
		});

		folderToolBar.add(addFolderButton);
		folderToolBar.add(editFolderButton);
		folderToolBar.add(deleteFolderButton);

		toolBarPanel.add(folderToolBar);
	}

	private void initCenterPanel() {
		centerPanel = new ContentPanel();
		centerPanel.setHeaderVisible(false);
		centerPanel.setLayout(new FitLayout());
		centerPanel.setLayoutOnChange(true);
		initDocumentToolBar();

		BasePagingLoader<PagingLoadResult<DocumentViewModel>> pagedDocumentsLoader = new BasePagingLoader<PagingLoadResult<DocumentViewModel>>(new RpcProxy<PagingLoadResult<DocumentViewModel>>() {
			@Override
			protected void load(Object loadConfig, final AsyncCallback<PagingLoadResult<DocumentViewModel>> callback) {
				
				if (!(loadConfig instanceof PagedDocumentsLoadConfig)) {
					changeDocumentsGridColumnModel(false);
					loadEmptyList(callback);
					return;
				}
				
				PagedDocumentsLoadConfig pagedDocumentsLoadConfig = (PagedDocumentsLoadConfig) loadConfig;
				
				String parentFolderId = pagedDocumentsLoadConfig.getParentFolderId();
				String documentLocationRealName = pagedDocumentsLoadConfig.getDocumentLocationRealName();
				boolean documentsOfSameType = pagedDocumentsLoadConfig.isDocumentsOfSameType();
				
				changeDocumentsGridColumnModel(documentsOfSameType);
				
				if ((parentFolderId == null) || (documentLocationRealName == null)) {
					loadEmptyList(callback);
					return;
				}
				
				LoadingManager.get().loading();
				GwtServiceProvider.getDocumentService().getPagedDocumentsFromFolder(parentFolderId, documentsOfSameType, documentLocationRealName, pagedDocumentsLoadConfig, new AsyncCallback<PagingLoadResult<DocumentViewModel>>() {
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						callback.onFailure(exception);
						LoadingManager.get().loadingComplete();
					}
					@Override
					public void onSuccess(PagingLoadResult<DocumentViewModel> pagedDocumentsLoadResult) {
						callback.onSuccess(pagedDocumentsLoadResult);
						LoadingManager.get().loadingComplete();
					}
				});
			}
			
			private void loadEmptyList(AsyncCallback<PagingLoadResult<DocumentViewModel>> callback) {
				callback.onSuccess(new BasePagingLoadResult<DocumentViewModel>(new ArrayList<DocumentViewModel>(), 0, 0));
			}
		});
		pagedDocumentsLoader.setReuseLoadConfig(false);

		documentsStore = new ListStore<DocumentViewModel>(pagedDocumentsLoader);
		// Documentele se ordoneaza dupa nume.
		documentsStore.setSortField(DocumentViewModel.PROPERTY_DOCUMENT_NAME);
		initDocumentsGrid();
		initDocumentsListView();
		Listener<BaseEvent> doubleClickOnDocumentListener = new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				DocumentViewModel selectedDocument = getSeletedDocument();
				String documentLocationRealName = getSelectedDocumentLocationRealName();
				if ((selectedDocument != null) && (documentLocationRealName != null)) {
					toBack();
					documentWindow.prepareForViewOrEdit(selectedDocument.getDocumentId(), documentLocationRealName);
				}
			}
		};
		documentsGrid.addListener(Events.RowDoubleClick, doubleClickOnDocumentListener);
		documentsListView.addListener(Events.DoubleClick, doubleClickOnDocumentListener);
		
		documentsPagingToolBar = new PagingToolBar(GwtRegistryUtils.getGuiConstants().getWorkspacePanelWindow_documentsPageSize());
		documentsPagingToolBar.bind(pagedDocumentsLoader);
		centerPanel.setBottomComponent(documentsPagingToolBar);
		
		changeDocumentsViewPerspective(DocumentsView.DETAILS);

		add(centerPanel, new BorderLayoutData(LayoutRegion.CENTER));
	}

	private void initDocumentToolBar() {
		documentToolBar = new ToolBar();

		addDocumentButton = new AddDocButton();
		addDocumentButton.setText(GwtLocaleProvider.getConstants().ADD_FILE());
		addDocumentButton.setIconStyle("add_file");
		addDocumentButton.setToolTip(GwtLocaleProvider.getConstants().ADD_FILE_TOOLTIP());
		addDocumentButton.addListener(Events.Enable, new Listener<BaseEvent>(){
			public void handleEvent(BaseEvent be) {
				if (folderTreePanelWrapper.getSelectedFolder()== null)
					addDocumentButton.disable();				
			}
			
		});

		deleteDocumentButton = new Button();
		deleteDocumentButton.setText(GwtLocaleProvider.getConstants().DEL_FILE());
		deleteDocumentButton.setIconStyle("del_file");
		deleteDocumentButton.setToolTip(GwtLocaleProvider.getConstants().DEL_FILE_TOOLTIP());
		deleteDocumentButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				final DocumentLocationModel selectedDocumentLocation = documentLocationComboBox.getValue();
				final DocumentViewModel selectedDocument = getSeletedDocument();
				if (selectedDocument != null) {
					new DeleteConfirmDialog() {
						@Override
						protected void onConfirmation() {
							LoadingManager.get().loading();
							GwtServiceProvider.getDocumentService().deleteDocument(selectedDocument.getDocumentId(), selectedDocumentLocation.getRealName(), new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable exception) {
									MessageUtils.displayError(exception);
									LoadingManager.get().loadingComplete();
								}

								@Override
								public void onSuccess(Void nothing) {
									MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().DOCUMENT_DELETED());
									AppEventController.fireEvent(AppEventType.Document);
									LoadingManager.get().loadingComplete();
								}
							});
						}
					};
				}
			}
		});

		iconsDocumentsViewButton = new ToggleButton();
		iconsDocumentsViewButton.setIconStyle("view_icons");
		iconsDocumentsViewButton.setToolTip(GwtLocaleProvider.getConstants().VIEW_ICON_TOOLTIP());
		iconsDocumentsViewButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent event) {
				changeDocumentsViewPerspective(DocumentsView.ICONS);
			}
		});

		detailsDocumentsViewButton = new ToggleButton();
		detailsDocumentsViewButton.setIconStyle("view_list");
		detailsDocumentsViewButton.setToolTip(GwtLocaleProvider.getConstants().VIEW_DETAILS_TOOLTIP());
		detailsDocumentsViewButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent event) {
				changeDocumentsViewPerspective(DocumentsView.DETAILS);
			}
		});

		documentToolBar.add(addDocumentButton);
		documentToolBar.add(deleteDocumentButton);
		documentToolBar.add(new SeparatorToolItem());
		documentToolBar.add(iconsDocumentsViewButton);
		documentToolBar.add(detailsDocumentsViewButton);

		centerPanel.setTopComponent(documentToolBar);		
	}

	private void initDocumentsGrid() {
		//Column
		documentsGrid = new Grid<DocumentViewModel>(documentsStore, new ColumnModel(new ArrayList<ColumnConfig>()));
		documentsGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

	private void initDocumentsListView() {
		documentsListView = new ListView<DocumentViewModel>(documentsStore) {
			@Override
			protected DocumentViewModel prepareData(DocumentViewModel document) {
				String name = document.getDocumentName();
				document.set(ModelConstants.PROPERTY_SHORT_NAME, Format.ellipse(name, 15));
				return document;
			}
		};
		documentsListView.setTemplate(getDocumentsListViewTemplate());
		documentsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		documentsListView.setItemSelector("div.container");
		documentsListView.setDisplayProperty(DocumentViewModel.PROPERTY_DOCUMENT_NAME);
	}

	private String getDocumentsListViewTemplate() {
		StringBuilder template = new StringBuilder();

		template.append("<tpl for=\".\">");
		template.append("	<div class=\"container\" id=\"{" + DocumentViewModel.PROPERTY_DOCUMENT_NAME + "}\" style=\"display: inline-block; text-align: center; width: 128px;\">");
		template.append("		<div>");
		template.append("			<img src=\"" + GWT.getModuleBaseURL() + "images/gray/interfata/file.png\" title=\"{" + DocumentViewModel.PROPERTY_DOCUMENT_NAME + "}\">");
		template.append("		</div>");
		template.append("		<div>{" + ModelConstants.PROPERTY_SHORT_NAME + "}</div>");
		template.append("	</div>");
		template.append("</tpl>");
		template.append("<div class=\"x-clear\"></div>");
		
		return template.toString();
	}

	private String getSelectedDocumentLocationRealName() {
		DocumentLocationModel selectedDocumentLocation = documentLocationComboBox.getValue();
		if (selectedDocumentLocation == null) {
			return null;
		}
		return selectedDocumentLocation.getRealName();
	}

	private void addDragAndDropMoveSupport() {
		DragSource folderTreeDragSource = new DragSource(folderTreePanelWrapper.getTreePanel());
		DragSource documentsGridDragSource = new DragSource(documentsGrid);
		DragSource documentsListViewDragSource = new DragSource(documentsListView);
		DropTarget folderTreeDropTarget = new DropTarget(folderTreePanelWrapper.getTreePanel());

		// posibilitatea de mutare din structura arborescenta cu foldere
		folderTreeDragSource.addDNDListener(new DNDListener() {
			@Override
			public void dragStart(DNDEvent event) {
				@SuppressWarnings("rawtypes")
				TreeNode nodeForFolderToMove = folderTreePanelWrapper.getTreePanel().findNode(event.getTarget());
				if (nodeForFolderToMove == null) {
					event.setCancelled(true);
					return;
				}
				ModelData folderToMove = nodeForFolderToMove.getModel();
				if (!(folderToMove instanceof FolderModel)) {
					event.setCancelled(true);
					return;
				}
				event.setData(folderToMove);
				event.getStatus().update(GwtLocaleProvider.getConstants().MOVE());
			}
		});
		// posibilitatea de mutare din grid-ul cu documente
		documentsGridDragSource.addDNDListener(new DNDListener() {
			@Override
			public void dragStart(DNDEvent event) {
				
				/*
				 * Pentru a nu incepe drag-ul daca nu am pornit cu mouse-ul din
				 * dreptul unui document.
				 */
				Element r = documentsGrid.getView().findRow(event.getTarget()).cast();
				if (r == null) {
					event.setCancelled(true);
					return;
				}
				
				DocumentViewModel documentToMove = documentsGrid.getSelectionModel().getSelectedItem();
				if (documentToMove == null) {
					event.setCancelled(true);
					return;
				}
				event.setData(documentToMove);
				event.getStatus().update(GwtLocaleProvider.getConstants().MOVE());
			}
		});
		// posibilitatea de mutare din lista cu documente
		documentsListViewDragSource.addDNDListener(new DNDListener() {
			@Override
			public void dragStart(DNDEvent event) {
				DocumentViewModel documentToMove = documentsListView.getSelectionModel().getSelectedItem();
				if (documentToMove == null) {
					event.setCancelled(true);
					return;
				}
				event.setData(documentToMove);
				event.getStatus().update(GwtLocaleProvider.getConstants().MOVE());
			}
		});
		// destinatia elementelor de mutat
		folderTreeDropTarget.setAllowSelfAsSource(true);
		folderTreeDropTarget.addDNDListener(new DNDListener() {
			@Override
			public void dragMove(DNDEvent event) {
				ModelData itemToMove = event.getData();
				@SuppressWarnings("rawtypes")
				TreeNode destinationNode = folderTreePanelWrapper.getTreePanel().findNode(event.getTarget());
				if (destinationNode == null) {
					event.getStatus().setStatus(false);
					return;
				}
				ModelData destinationItem = destinationNode.getModel();
				boolean isNotSelf = !destinationItem.equals(itemToMove);
				/*
				 * Un folder poate fi mutat oriunde.
				 * Un document nu poate fi mutat decat intr-un folder.
				 */
				boolean areOfRightType = (itemToMove instanceof FolderModel) || ((itemToMove instanceof DocumentViewModel) && !(destinationItem instanceof DocumentLocationModel));
				event.getStatus().setStatus(isNotSelf && areOfRightType);
			}

			@Override
			public void dragDrop(DNDEvent event) {
				ModelData itemToMove = event.getData();
				@SuppressWarnings("rawtypes")
				TreeNode destinationNode = folderTreePanelWrapper.getTreePanel().findNode(event.getTarget());
				if (destinationNode == null) {
					event.setCancelled(true);
					return;
				}
				String destinationFolderId = (destinationNode.getModel() instanceof FolderModel) ? ((FolderModel) destinationNode.getModel()).getId() : null;
				DocumentLocationModel currentDocumentLocation = documentLocationComboBox.getValue();
				if (itemToMove instanceof FolderModel) {
					FolderModel folderToMove = (FolderModel) itemToMove;
					LoadingManager.get().loading();
					GwtServiceProvider.getFolderService().moveFolder(folderToMove.getId(), destinationFolderId, currentDocumentLocation.getRealName(), new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable exception) {
							MessageUtils.displayError(exception);
							LoadingManager.get().loadingComplete();
						}

						@Override
						public void onSuccess(Void nothing) {
							MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().FOLDER_MOVED());
							AppEventController.fireEvent(AppEventType.Folder);
							LoadingManager.get().loadingComplete();
						}
					});
				} else if (itemToMove instanceof DocumentViewModel) {
					/*
					 * Un document nu poate fi mutat in radacina unui
					 * document location.
					 */
					if (destinationFolderId == null) {
						event.setCancelled(true);
					}
					DocumentViewModel documentToMove = (DocumentViewModel) itemToMove;
					LoadingManager.get().loading();
					GwtServiceProvider.getDocumentService().moveDocument(documentToMove.getDocumentId(), destinationFolderId, currentDocumentLocation.getRealName(), new AsyncCallback<Void>() {
						@Override
						public void onFailure(Throwable exception) {
							MessageUtils.displayError(exception);
							LoadingManager.get().loadingComplete();
						}

						@Override
						public void onSuccess(Void nothing) {
							MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().DOCUMENT_MOVED());
							AppEventController.fireEvent(AppEventType.Document);
							LoadingManager.get().loadingComplete();
						}
					});
				}
			}
		});
	}

	private void reloadDocumentLocations() {
		documentLocationComboBox.setValue(null);
		documentLocationComboBox.getStore().getLoader().load();
	}

	private void reloadFolders() {
		DocumentLocationModel selectedDocumentLocation = documentLocationComboBox.getValue();
		if (selectedDocumentLocation == null) {
			folderTreePanelWrapper.getTreePanel().getStore().removeAll();
		} else {
			folderTreePanelWrapper.reload(selectedDocumentLocation);
		}
		this.updateStatus();
	}

	private void reloadDocuments() {
		documentsStore.removeAll();
		documentsGrid.getSelectionModel().deselectAll();
		documentsListView.getSelectionModel().deselectAll();

		FolderModel selectedFolder = folderTreePanelWrapper.getSelectedFolder();
		populateDocumentStore(selectedFolder);
	}

	private DocumentViewModel getSeletedDocument() {
		if (ComponentUtils.containerHasComponent(centerPanel, documentsGrid)) {
			return documentsGrid.getSelectionModel().getSelectedItem();
		} else if (ComponentUtils.containerHasComponent(centerPanel, documentsListView)) {
			return documentsListView.getSelectionModel().getSelectedItem();
		} else {
			return null;
		}
	}

	private void changeDocumentsViewPerspective(DocumentsView documentView) {
		
		if (ComponentUtils.containerHasComponent(centerPanel, documentsListView)) {
			centerPanel.remove(documentsListView);
		}
		if (ComponentUtils.containerHasComponent(centerPanel, documentsGrid)) {
			centerPanel.remove(documentsGrid);
		}
		
		switch (documentView) {
			case ICONS:
				iconsDocumentsViewButton.toggle(true);
				detailsDocumentsViewButton.toggle(false);
				centerPanel.add(documentsListView);
				break;
			case DETAILS:
				iconsDocumentsViewButton.toggle(false);
				detailsDocumentsViewButton.toggle(true);
				centerPanel.add(documentsGrid);
				break;
			default:
				break;
		}
		
		centerPanel.layout();
	}

	/**
	 * Populeaza lista de documente.
	 * Daca folder-ul specificat este null, atunci lista de documente va deveni goala.
	 */
	private void populateDocumentStore(final FolderModel parentFolder) {
		documentsStore.removeAll();
		documentsPagingToolBar.clear();
		// Daca nu e nici un folder selectat, nu am ce documente sa afisez.
		if (parentFolder == null) {
			changeDocumentsGridColumnModel(false);
			documentsPagingToolBar.disable();
			return;
		}
		DocumentLocationModel documentLocation = documentLocationComboBox.getValue();
		final String documentLocationRealName = (documentLocation != null) ? documentLocation.getDocumentLocationRealName() : null;
		final String parentFolderId = parentFolder.getId();
		final boolean sameType = (parentFolder.getDocumentTypeId() != null);
		
		if ((documentLocationRealName == null) || (parentFolderId == null)) {
			changeDocumentsGridColumnModel(false);
			documentsPagingToolBar.disable();
			return;
		}
		
		int offset = 0;
		int limit = documentsPagingToolBar.getPageSize();
		
		final PagedDocumentsLoadConfig pagedDocumentsLoadConfig = new PagedDocumentsLoadConfig(documentLocationRealName, parentFolderId, sameType, offset, limit);
		
		if (parentFolder.getDocumentTypeId() == null) {
			this.documentType = null;
			
			documentsPagingToolBar.enable();
			documentsStore.getLoader().load(pagedDocumentsLoadConfig);
		} else {
			LoadingManager.get().loading();
			GwtServiceProvider.getDocumentTypeService().getDocumentTypeById(parentFolder.getDocumentTypeId(), new AsyncCallback<DocumentTypeModel>() {
				@Override
				public void onFailure(Throwable exception) {
					MessageUtils.displayError(exception);
					LoadingManager.get().loadingComplete();
				}
				@Override
				public void onSuccess(DocumentTypeModel documentType) {
					WorkspacePanelWindow.this.documentType = documentType;

					documentsPagingToolBar.enable();
					documentsStore.getLoader().load(pagedDocumentsLoadConfig);
					
					LoadingManager.get().loadingComplete();
				}
			});
		}
	}

	private String getCurrentPath() {
		StringBuilder path = new StringBuilder();

		DocumentLocationModel selectedDocumentLocation = documentLocationComboBox.getValue();
		if (selectedDocumentLocation != null) {
			path.append(selectedDocumentLocation.getName());
			path.append(folderTreePanelWrapper.getSelectedFolderPath());
			path.append("\\");
		}

		return path.toString();
	}

	private void updateStatus() {
		bottomStatusBar.setText(getCurrentPath());
	}

	private void onDocumentLocationEvent() {
		reloadDocumentLocations();
		reloadFolders();
		reloadDocuments();
	}

	private void onFolderEvent() {
		reloadFolders();
		reloadDocuments();
	}

	private void onDocumentEvent() {
		reloadDocuments();
	}

	@Override
	public void handleEvent(AppEvent event) {
		if (event.getType() == AppEventType.DocumentLocation) {
			if (isVisible() || ComponentUtils.isWindowMinimized(this)) {
				onDocumentLocationEvent();
			}
		} else if (event.getType() == AppEventType.Folder) {
			if (isVisible() || ComponentUtils.isWindowMinimized(this)) {
				onFolderEvent();
			}
		} else if (event.getType() == AppEventType.Document) {
			if (isVisible() || ComponentUtils.isWindowMinimized(this)) {
				onDocumentEvent();
			}
		}
	}
	
	private void changeDocumentsGridColumnModel(boolean sameType) {
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

		ColumnConfig nameColumnConfig = new ColumnConfig();
		nameColumnConfig.setHeader(GwtLocaleProvider.getConstants().DOC_NAME());
		nameColumnConfig.setId(DocumentViewModel.PROPERTY_DOCUMENT_NAME);
		nameColumnConfig.setWidth(260);
		columnConfigs.add(nameColumnConfig);

		ColumnConfig typeNameColumnConfig = new ColumnConfig();
		typeNameColumnConfig.setHeader(GwtLocaleProvider.getConstants().DOC_TYPE());
		typeNameColumnConfig.setHidden(true);
		typeNameColumnConfig.setId(DocumentViewModel.PROPERTY_DOCUMENT_TYPE_NAME);
		typeNameColumnConfig.setWidth(160);
		columnConfigs.add(typeNameColumnConfig);
		
		if (sameType && (this.documentType != null)) {
			for (MetadataDefinitionModel metadataDefinition : this.documentType.getMetadataDefinitions()) {
				
				if ((metadataDefinition instanceof MetadataCollectionDefinitionModel)
						|| GwtBooleanUtils.isNotTrue(metadataDefinition.isRepresentative())) {
					continue;
				}
				
				ColumnConfig metadataColumnConfig = new ColumnConfig();
				
				metadataColumnConfig.setHeader(metadataDefinition.getLabel());
				metadataColumnConfig.setId(DocumentViewModel.PREFIX_REPRESENTATIVE_METADATA_PROPERTY_NAME + metadataDefinition.getName());
				metadataColumnConfig.setWidth(100);
				
				columnConfigs.add(metadataColumnConfig);
			}
		}

		ColumnConfig authorNameColumnConfig = new ColumnConfig();
		authorNameColumnConfig.setHeader(GwtLocaleProvider.getConstants().DOC_AUTHOR());
		authorNameColumnConfig.setId(DocumentViewModel.PROPERTY_DOCUMENT_AUTHOR_NAME);
		authorNameColumnConfig.setWidth(160);
		columnConfigs.add(authorNameColumnConfig);

		ColumnConfig createdColumnConfig = new ColumnConfig();
		createdColumnConfig.setHeader(GwtLocaleProvider.getConstants().DOC_CREATED());
		createdColumnConfig.setId(DocumentViewModel.PROPERTY_DOCUMENT_CREATED_DATE);
		createdColumnConfig.setDateTimeFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		createdColumnConfig.setWidth(120);
		columnConfigs.add(createdColumnConfig);

		ColumnConfig lastModifiedColumnConfig = new ColumnConfig();
		lastModifiedColumnConfig.setId(DocumentViewModel.PROPERTY_DOCUMENT_LAST_MODIFIED_DATE);
		lastModifiedColumnConfig.setHeader(GwtLocaleProvider.getConstants().DOC_MODIFIED());
		lastModifiedColumnConfig.setHidden(true);
		lastModifiedColumnConfig.setWidth(120);
		lastModifiedColumnConfig.setDateTimeFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		columnConfigs.add(lastModifiedColumnConfig);

		ColumnConfig lockedColumnConfig = new ColumnConfig();
		lockedColumnConfig.setHeader(GwtLocaleProvider.getConstants().DOC_LOCK());
		lockedColumnConfig.setRenderer(new CustomGridCellRenderer<DocumentViewModel>() {
			
			@Override
			public Object doRender(DocumentViewModel model, String property, ColumnData config, int rowIndex,
					int colIndex, ListStore<DocumentViewModel> store, Grid<DocumentViewModel> grid) {
				
				LayoutContainer lock = new LayoutContainer();
				lock.setSize(16, 16);
				lock.setStyleAttribute("margin-left", "10px");
				if (model.isLocked()) {
					lock.setStyleName("lock_true");
					//lock.setToolTip(GwtLocaleProvider.getConstants().DOC_LOCK_TOOLTIP_ON());
					lock.setToolTip(GwtLocaleProvider.getMessages().LOCKED_BY_USER(model.getDocumentLockedByName()));
				} else {
					lock.setStyleName("lock_false");
					lock.setToolTip(GwtLocaleProvider.getConstants().DOC_LOCK_TOOLTIP_OFF());
				}
				return lock;
			}
		});
		lockedColumnConfig.setWidth(50);
		columnConfigs.add(lockedColumnConfig);

		ColumnModel columnModel = new ColumnModel(columnConfigs);
		
		documentsGrid.reconfigure(documentsStore, columnModel);
		documentsGrid.setAutoExpandColumn(DocumentViewModel.PROPERTY_DOCUMENT_NAME);
		documentsGrid.setAutoExpandMin(150);
	}

	private enum DocumentsView {
		ICONS, DETAILS
	}

	private class FolderTreePanelWrapper extends LayoutContainer {

		private TreePanel<ModelData> treePanel;

		private DocumentLocationModel documentLocation;

		public FolderTreePanelWrapper() {
			setLayout(new FitLayout());

			// proxy pentru loader
			RpcProxy<List<ModelData>> proxy = new RpcProxy<List<ModelData>>() {
				@Override
				protected void load(Object loadConfig, final AsyncCallback<List<ModelData>> callback) {
					if (loadConfig == null) {
						if (documentLocation != null) {
							LoadingManager.get().loading();
							GwtServiceProvider.getDocumentLocationService().getDocumentLocation(documentLocation, new AsyncCallback<List<ModelData>>() {
								@Override
								public void onFailure(Throwable exception) {
									callback.onFailure(exception);
									LoadingManager.get().loadingComplete();
								}
								@Override
								public void onSuccess(List<ModelData> results) {
									callback.onSuccess(results);
									LoadingManager.get().loadingComplete();
								}
							});
						}
					} else {
						if (loadConfig instanceof DocumentLocationModel) {
							LoadingManager.get().loading();
							GwtServiceProvider.getDocumentLocationService().getFoldersFromDocumentLocation(documentLocation.getRealName(), new AsyncCallback<List<ModelData>>() {
								@Override
								public void onFailure(Throwable exception) {
									callback.onFailure(exception);
									LoadingManager.get().loadingComplete();
								}
								@Override
								public void onSuccess(List<ModelData> results) {
									callback.onSuccess(results);
									LoadingManager.get().loadingComplete();
								}
							});
						} else if (loadConfig instanceof FolderModel) {
							FolderModel parentFolder = (FolderModel) loadConfig;
							String parentId = parentFolder.getId();
							
							LoadingManager.get().loading();
							GwtServiceProvider.getFolderService().getFoldersFromFolder(documentLocation.getRealName(), parentId, new AsyncCallback<List<ModelData>>() {
								@Override
								public void onFailure(Throwable exception) {
									callback.onFailure(exception);
									LoadingManager.get().loadingComplete();
								}
								@Override
								public void onSuccess(List<ModelData> results) {
									callback.onSuccess(results);
									LoadingManager.get().loadingComplete();
								}
							});
						}
					}

				}
			};

			// loader pentru store
			BaseTreeLoader<ModelData> treeloader = new BaseTreeLoader<ModelData>(proxy) {
				@Override
				public boolean hasChildren(ModelData parent) {
					return true;
				}
				@Override
				protected void onLoadFailure(Object loadConfig, Throwable exception) {
					MessageUtils.displayError(exception);
				}
			};

			// store pentru arbore
			TreeStore<ModelData> store = new TreeStore<ModelData>(treeloader);
			treePanel = new TreePanel<ModelData>(store);
			treePanel.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			// configurarea afisarii
			treePanel.setLabelProvider(new ModelStringProvider<ModelData>() {
				@Override
				public String getStringValue(ModelData model, String property) {
					if (model instanceof DocumentLocationModel) {
						return ((DocumentLocationModel) model).getName();
					} else if (model instanceof FolderModel) {
						return ((FolderModel) model).getName();
					} else {
						return null;
					}
				}
			});
			// configurarea iconitelor
			treePanel.setIconProvider(new ModelIconProvider<ModelData>() {
				@Override
				public AbstractImagePrototype getIcon(ModelData model) {
					if (model instanceof DocumentLocationModel) {
						return IconHelper.createStyle("icon-documentLocation", 16, 16);
					} else if (model instanceof FolderModel) 
					{
						
						return null;//IconHelper.createStyle("icon-folder", 16, 16);
						
					} else {
						return null;
					}
				}
			});
			// configurarea sortarii
			treePanel.getStore().setStoreSorter(new StoreSorter<ModelData>() {
				@Override
				public int compare(Store<ModelData> store, ModelData m1, ModelData m2, String property) {
					if ((m1 instanceof FolderModel) && (m2 instanceof FolderModel)) {
						FolderModel folder1 = (FolderModel) m1;
						FolderModel folder2 = (FolderModel) m2;
						return folder1.getName().compareToIgnoreCase(folder2.getName());
					}
					if ((m1 instanceof DocumentLocationModel) && (m2 instanceof DocumentLocationModel)) {
						DocumentLocationModel documentLocation1 = (DocumentLocationModel) m1;
						DocumentLocationModel documentLocation2 = (DocumentLocationModel) m2;
						return documentLocation1.getName().compareToIgnoreCase(documentLocation2.getName());
					}
					if ((m1 instanceof DocumentLocationModel) && (m2 instanceof FolderModel)) {
						return -1;
					}
					if ((m1 instanceof FolderModel) && (m2 instanceof DocumentLocationModel)) {
						return 1;
					}
					return 0;
				}
			});
			add(treePanel);
		}

		public void reload(DocumentLocationModel documentLocation) {
			this.documentLocation = documentLocation;
			// Reincarca folderele din workspace-ul selectat.
			treePanel.getStore().getLoader().load();
			/*
			 * Deselecteaza folder-ul selectat inainte (chiar daca se schimba
			 * toata structura, cel vechi inca este selectat).
			 */
			treePanel.getSelectionModel().deselectAll();
		}

		public TreePanel<ModelData> getTreePanel() {
			return treePanel;
		}

		public FolderModel getSelectedFolder() {
			ModelData selectedItem = treePanel.getSelectionModel().getSelectedItem();
			return (selectedItem instanceof FolderModel) ? (FolderModel) selectedItem : null;
		}

		public String getSelectedFolderId() {
			FolderModel selectedFolder = getSelectedFolder();
			if (selectedFolder == null) {
				return null;
			}
			return selectedFolder.getId();
		}

		public String getSelectedFolderPath() {
			List<String> inheritanceTreeFolderNames = new ArrayList<String>();
			FolderModel folder = getSelectedFolder();
			while (folder != null) {
				inheritanceTreeFolderNames.add(folder.getName());
				ModelData parent = treePanel.getStore().getParent(folder);
				folder = (parent instanceof FolderModel) ? (FolderModel) parent : null;
			}

			StringBuilder path = new StringBuilder();
			for (int i = inheritanceTreeFolderNames.size() - 1; i >= 0; i--) {
				path.append("\\");
				path.append(inheritanceTreeFolderNames.get(i));
			}
			return path.toString();
		}
	}

	private class AddDocButton extends Button {
		
		private static final String KEY_DOCUMENT_TYPE_ID = "documentTypeId";

		private Menu menu;

		public AddDocButton() {
			this.setText(GwtLocaleProvider.getConstants().ADD());
			configureButton();
			initMenu();
			//addAction();
		}

		private void initMenu() {
			menu = new Menu();

			
			setMenu(menu);
		}

		private void configureButton() {
			setIconStyle("add_file");
			setIconAlign(IconAlign.LEFT);
			setScale(ButtonScale.SMALL);
		}
		
		/**
		 * Reincarca meniul cu tipurile de documente disponibile pentru creare
		 * document.
		 */
		public void reloadAvailableDocumentTypes() {
			// Impiedica accesul la buton.
			setEnabled(false);
			// Elimina toate optiunile din meniu.
			menu.removeAll();
			GwtServiceProvider.getDocumentTypeService().getAvailableDocumentTypes(new AsyncCallback<List<DocumentTypeModel>>() {
				public void onFailure(Throwable exception) {
					MessageUtils.displayError(exception);
					// Permite accesul la buton.
					setEnabled(true);
				}
				public void onSuccess(List<DocumentTypeModel> modelsList) {
					if (GwtValidateUtils.hasElements(modelsList)) {
						for (final DocumentTypeModel model : modelsList) {
							MenuItem item = new MenuItem(model.getName(), new SelectionListener<MenuEvent>() {
								public void componentSelected(MenuEvent ce) {
									String documentLocationRealName = getSelectedDocumentLocationRealName();
									String parentFolderId = folderTreePanelWrapper.getSelectedFolderId();
									if ((documentLocationRealName != null) && (parentFolderId != null)) {
										toBack();
										documentWindow.prepareForAdd(model.getId(), documentLocationRealName, parentFolderId);
									}
								}
							});
							// Pastreaza in item-ul din meniu ID-ul tipului de document.
							item.setData(KEY_DOCUMENT_TYPE_ID, model.getId());
							menu.add(item);
						}
					}
					// Permite accesul la buton.
					setEnabled(true);
				}
			});
		}
		
		/**
		 * Actualizeaza meniul cu tipurile de documente permise, pentru care
		 * utilizatorul poate fi initiator si care pot fi create in folder-ul
		 * selectat.
		 * @param documentTypeIdOfFolder ID-ul tipului de document permis in folder
		 */
		public void updateAvailableMenuOptions(Long documentTypeIdOfFolder) {
			// Impiedica accesul la buton.
			setEnabled(false);
			// Ia toti "copiii" meniului.
			List<Component> menuItems = menu.getItems();
			for (Component component : menuItems) {
				// Daca nu este vorba de un item din meniu, sare peste el.
				if (!(component instanceof MenuItem)) {
					continue;
				}
				/*
				 * Daca folder-ul nu are restrictie pe tipul documentelor,
				 * fa-le pe toate vizibile.
				 */
				if (documentTypeIdOfFolder == null) {
					component.setVisible(true);
					continue;
				}
				// Ia ID-ul tipului de document al item-ului din meniu.
				Long documentTypeIdOfMenuItem = component.getData(KEY_DOCUMENT_TYPE_ID);
				/*
				 * Daca item-ul din meniu corespunde cu tipul permis de document,
				 * fa-l vizibil, altfel ascunde-l.
				 */
				if (documentTypeIdOfFolder.equals(documentTypeIdOfMenuItem)) {
					component.setVisible(true);
				} else {
					component.setVisible(false);
				}
			}
			// Permite accesul la buton.
			setEnabled(true);
		}
	}
}