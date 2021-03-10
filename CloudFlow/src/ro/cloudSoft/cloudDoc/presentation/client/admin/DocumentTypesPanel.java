package ro.cloudSoft.cloudDoc.presentation.client.admin;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes.DocumentTypeWindow;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEvent;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventHandler;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DocumentTypesPanel extends ContentPanel implements AppEventHandler {
	
	ToolBar toolBar;
	Button addButton;
	Button deleteButton;
	Button refreshButton;
	Grid<DocumentTypeModel> documentTypesGrid;
	DocumentTypeWindow documentTypeWindow = new DocumentTypeWindow();

	public DocumentTypesPanel() {
		
		initWindow();
		initToolBar();
		initDocumentTypesGrid();
		addButtonActions();
		
		AppEventController.subscribe(this, AppEventType.DocumentType);
	}
	
	private void initWindow() {
		this.setHeading(GwtLocaleProvider.getConstants().ADMIN_CONTENT());
		this.setLayout(new FitLayout());

		addListener(Events.Attach, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent event) {
				refresh();
			};
		});
	}
	
	private void initToolBar() {
		toolBar = new ToolBar();
		toolBar.setStyleName("admin_toolbar");
		toolBar.setSpacing(10);
		toolBar.setBorders(true);
		
		
		addButton = new Button();
		addButton.setText(GwtLocaleProvider.getConstants().ADD());
		addButton.setStyleName("admin_toolbar_buttons");
		addButton.setIconStyle("icon-add");
		addButton.setBorders(true);
		
//		editButton = new Button();
//		editButton.setText(GwtLocaleProvider.getConstants().EDIT());
		
		deleteButton = new Button();
		deleteButton.setText(GwtLocaleProvider.getConstants().DELETE());
		deleteButton.setStyleName("admin_toolbar_buttons");
		deleteButton.setIconStyle("icon-delete");
		deleteButton.setBorders(true);

		refreshButton = new Button();
		refreshButton.setText(GwtLocaleProvider.getConstants().REFRESH());
		refreshButton.setStyleName("admin_toolbar_buttons");
		refreshButton.setIconStyle("icon-refresh");
		refreshButton.setBorders(true);
		
		toolBar.add(addButton);
//		toolBar.add(editButton);
		toolBar.add(deleteButton);
		toolBar.add(refreshButton);
		
		setTopComponent(toolBar);
	}
	
	private void addButtonActions() {
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent event) {
				documentTypeWindow.prepareForAdd();
			};
		});
/*		editButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent event) {
				documentTypeWindow.prepareForEdit(getSelectedDocumentType().getId());
			};
		});*/
		
		deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent event) {
				MessageBox.confirm(
					GwtLocaleProvider.getConstants().DELETE(),
					GwtLocaleProvider.getMessages().CONFIRM_DELETE_DOCUMENT_TYPE(),
					new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent mbe) {
							if (mbe.getButtonClicked().getItemId().equals(Dialog.YES)) {
								DocumentTypeModel selectedDocumentType = getSelectedDocumentType();
								
								LoadingManager.get().loading();
								GwtServiceProvider.getDocumentTypeService().deleteDocumentType(selectedDocumentType.getId(), new AsyncCallback<Void>() {
									@Override
									public void onFailure(Throwable exception) {
										MessageUtils.displayError(exception);
										LoadingManager.get().loadingComplete();
									}
									@Override
									public void onSuccess(Void nothing) {
										MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().DOCUMENT_TYPE_DELETED());
										refresh();
										LoadingManager.get().loadingComplete();
									}
								});
							}
						}
					});
			};
		});
		refreshButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				refresh();
			};
		});
	}
	
	private void initDocumentTypesGrid() {
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		
		ColumnConfig nameColumnConfig = new ColumnConfig();
		nameColumnConfig.setHeader(
			GwtLocaleProvider.getConstants().NAME());
		nameColumnConfig.setId(DocumentTypeModel.PROPERTY_NAME);
		nameColumnConfig.setWidth(120);
		
		ColumnConfig descriptionColumnConfig = new ColumnConfig();
		descriptionColumnConfig.setHeader(
			GwtLocaleProvider.getConstants().DESCRIPTION());
		descriptionColumnConfig.setId(DocumentTypeModel.PROPERTY_DESCRIPTION);
		descriptionColumnConfig.setWidth(200);
		
		columnConfigs.add(nameColumnConfig);
		columnConfigs.add(descriptionColumnConfig);
		
		ColumnModel columnModel = new ColumnModel(columnConfigs);
		
		documentTypesGrid = new Grid<DocumentTypeModel>(
			new ListStore<DocumentTypeModel>(), columnModel);
		documentTypesGrid.setAutoExpandColumn(DocumentTypeModel.PROPERTY_NAME);
		documentTypesGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		GridView gridView = new GridView();
		gridView.setForceFit(true);
		documentTypesGrid.setView(gridView);
		documentTypesGrid.setStripeRows(true);
		
		add(documentTypesGrid);
		
		documentTypesGrid.addListener(Events.OnClick,
				new Listener<GridEvent<DocumentTypeModel>>() {
			@Override
			public void handleEvent(GridEvent<DocumentTypeModel> event) {
				// Ia entitatea selectata.
				DocumentTypeModel selectedDocumentType = event.getModel();
				changeToolBarPerspective(selectedDocumentType);
			}
		});
		
		documentTypesGrid.addListener(Events.OnDoubleClick,
				new Listener<GridEvent<DocumentTypeModel>>() {
			@Override
			public void handleEvent(GridEvent<DocumentTypeModel> event) {
				// Ia entitatea selectata.
				DocumentTypeModel selectedDocumentType = event.getModel();
				changeToolBarPerspective(selectedDocumentType);
				if (selectedDocumentType != null) {
					documentTypeWindow.prepareForEdit(selectedDocumentType.getId());
				}
			}
		});
	}
	
	private void changeToolBarPerspective(DocumentTypeModel documentType) {
		if (documentType != null) {
//			editButton.setEnabled(true);
			deleteButton.setEnabled(true);
		} else {
//			editButton.setEnabled(false);
			deleteButton.setEnabled(false);			
		}
	}
	
	public void refresh() {
		changeToolBarPerspective(null);
		LoadingManager.get().loading();
		GwtServiceProvider.getDocumentTypeService().getAllDocumentTypesForDisplay(new AsyncCallback<List<DocumentTypeModel>>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(List<DocumentTypeModel> allDocumentTypesForDisplay) {
				documentTypesGrid.getStore().removeAll();
				documentTypesGrid.getStore().add(allDocumentTypesForDisplay);
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private DocumentTypeModel getSelectedDocumentType() {
		return documentTypesGrid.getSelectionModel().getSelectedItem();
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		if (event.getType().equals(AppEventType.DocumentType)) {
			if (AdminPanelDispatcher.isActivePanel(this)) {
				refresh();
			}
		}
	}
}