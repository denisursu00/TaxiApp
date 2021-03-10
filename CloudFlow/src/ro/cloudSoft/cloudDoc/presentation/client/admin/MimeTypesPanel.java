package ro.cloudSoft.cloudDoc.presentation.client.admin;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEvent;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventHandler;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MimeTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils.ConfirmCallback;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MimeTypesPanel extends ContentPanel implements AppEventHandler {
	
	private ToolBar toolBar;
	private Button addButton;
	private Button deleteButton;
	private Button refreshButton;
	
	private Grid<MimeTypeModel> mimeTypesGrid;
	private MimeTypeWindow mimeTypeWindow = new MimeTypeWindow();
	
	public MimeTypesPanel() {
		
		initPanel();
		initToolBar();
		initMimeTypesGrid();
		addButtonActions();
		
		AppEventController.subscribe(this, AppEventType.MimeType);
	}
	
	private void initPanel() {
		
		setHeading(GwtLocaleProvider.getConstants().ADMIN_MIME_TYPES());
		setLayout(new FitLayout());

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
		toolBar.add(deleteButton);
		toolBar.add(refreshButton);
		
		setTopComponent(toolBar);
	}
	
	private void addButtonActions() {
		
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent event) {
				mimeTypeWindow.prepareForAdd();
			};
		});
		
		deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent event) {
				ComponentUtils.confirm(
						GwtLocaleProvider.getConstants().DELETE(),
						GwtLocaleProvider.getMessages().CONFIRM_DELETE_MIME_TYPE(),
						new ConfirmCallback() {

							@Override
							public void onYes() {
								Long selectedMimeTypeId = getSelectedMimeTypeId();

								LoadingManager.get().loading();
								GwtServiceProvider.getMimeTypeService().deleteMimeType(selectedMimeTypeId, new AsyncCallback<Void>() {
									@Override
									public void onFailure(Throwable exception) {
										MessageUtils.displayError(exception);
										LoadingManager.get().loadingComplete();
									}
									@Override
									public void onSuccess(Void nothing) {
										MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().MIME_TYPE_DELETED());
										AppEventController.fireEvent(AppEventType.MimeType);
										LoadingManager.get().loadingComplete();
									}
								});
							}
						});
			}
		});

		refreshButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent event) {
				refresh();
			};
		});
		
		
	}
	
	private void initMimeTypesGrid() {
		
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		
		ColumnConfig nameColumnConfig = new ColumnConfig();
		
		nameColumnConfig.setHeader(
			GwtLocaleProvider.getConstants().MIME_TYPE_NAME());
		nameColumnConfig.setId(MimeTypeModel.PROPERTY_NAME);
		nameColumnConfig.setWidth(120);
		
		ColumnConfig extensionColumnConfig = new ColumnConfig();
		extensionColumnConfig.setHeader(
			GwtLocaleProvider.getConstants().MIME_TYPE_EXTENSION());
		extensionColumnConfig.setId(MimeTypeModel.PROPERTY_EXTENSION);
		extensionColumnConfig.setWidth(50);
		
		columnConfigs.add(nameColumnConfig);
		columnConfigs.add(extensionColumnConfig);
		
		ColumnModel columnModel = new ColumnModel(columnConfigs);
		
		mimeTypesGrid = new Grid<MimeTypeModel>(new ListStore<MimeTypeModel>(), columnModel);
		mimeTypesGrid.setAutoExpandColumn(MimeTypeModel.PROPERTY_NAME);
		mimeTypesGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		 
		GridView gridView = new GridView();
		gridView.setForceFit(true);
		mimeTypesGrid.setView(gridView);
		mimeTypesGrid.setStripeRows(true);
		
		add(mimeTypesGrid);
		
		mimeTypesGrid.addListener(Events.RowClick,
				new Listener<GridEvent<MimeTypeModel>>() {
			@Override
			public void handleEvent(GridEvent<MimeTypeModel> event) {
				// Ia entitatea selectata.
				MimeTypeModel selectedMimeType = event.getModel();
				changeToolBarPerspective(selectedMimeType);
			}
		});
		
		mimeTypesGrid.addListener(Events.RowDoubleClick,
				new Listener<GridEvent<MimeTypeModel>>() {
			@Override
			public void handleEvent(GridEvent<MimeTypeModel> event) {
				// Ia entitatea selectata.
				MimeTypeModel selectedMimeType = event.getModel();
				changeToolBarPerspective(selectedMimeType);
				if (selectedMimeType != null) {
					mimeTypeWindow.prepareForEdit(selectedMimeType.getId());
				}
			}
		});
		
	}
	
	private void changeToolBarPerspective(MimeTypeModel mimeType) {
		deleteButton.setEnabled(mimeType != null);
	}
	
	public void refresh() {
		changeToolBarPerspective(null);
		LoadingManager.get().loading();
		GwtServiceProvider.getMimeTypeService().getAllMimeTypes(new AsyncCallback<List<MimeTypeModel>>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(List<MimeTypeModel> allMimeTypes) {
				mimeTypesGrid.getStore().removeAll();
				mimeTypesGrid.getStore().add(allMimeTypes);
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private Long getSelectedMimeTypeId() {
		MimeTypeModel selectedMimeType = mimeTypesGrid.getSelectionModel().getSelectedItem();
		if (selectedMimeType != null){
			return selectedMimeType.getId();
		}
		
		return null;
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		
		if (event.getType().equals(AppEventType.MimeType)) {
			if (AdminPanelDispatcher.isActivePanel(this)) {
				refresh();
			}
		}
	}
}
