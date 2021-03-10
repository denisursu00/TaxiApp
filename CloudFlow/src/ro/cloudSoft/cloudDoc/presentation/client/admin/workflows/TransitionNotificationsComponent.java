package ro.cloudSoft.cloudDoc.presentation.client.admin.workflows;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.CustomGridCellRenderer;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.TransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.TransitionNotificationModelType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.renderers.TransitionNotificationModelDetailsGridCellRenderer;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils.ConfirmCallback;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class TransitionNotificationsComponent extends ContentPanel {
	
	private TransitionNotificationWindow window;

	private ToolBar toolBar;
	
	private Button addButton;
	private Button deleteButton;
	
	private Menu addMenu;
	
	private Grid<TransitionNotificationModel> grid;
	
	public TransitionNotificationsComponent() {
		initWindow();
		initComponent();
		initToolBar();
		initGrid();
	}
	
	private void initComponent() {
		setHeaderVisible(false);
		setLayout(new FitLayout());
	}
	
	private void initWindow() {
		window = new TransitionNotificationWindow(this);
	}
	
	private void initToolBar() {
		
		addButton = new Button();
		addButton.setText(GwtLocaleProvider.getConstants().ADD());
		addButton.setIconStyle("icon-add");
		
		addMenu = new Menu();
		addButton.setMenu(addMenu);
		
		initAddMenuItems();
		
		deleteButton = new Button();
		deleteButton.setText(GwtLocaleProvider.getConstants().DELETE());
		deleteButton.setIconStyle("icon-delete");
		deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				
				final List<TransitionNotificationModel> selectedNotifications = grid.getSelectionModel().getSelectedItems();
				if (selectedNotifications.isEmpty()) {
					return;
				}
				
				ComponentUtils.confirm(GwtLocaleProvider.getConstants().DELETE(),
						GwtLocaleProvider.getMessages().CONFIRM_TRANSITION_NOTIFICATIONS(),
						new ConfirmCallback() {
					
					@Override
					public void onYes() {
						for (TransitionNotificationModel selectedNotification : selectedNotifications) {
							grid.getStore().remove(selectedNotification);
						}
					}
				});
			}
		});
		
		toolBar = new ToolBar();
		
		toolBar.add(addButton);
		toolBar.add(deleteButton);
		
		setTopComponent(toolBar);
	}
	
	private void initAddMenuItems() {
		for (final TransitionNotificationModelType notificationType : TransitionNotificationModelType.values()) {
			MenuItem addOfTypeMenuItem = new MenuItem();
			addOfTypeMenuItem.setText(notificationType.getLabel());
			addOfTypeMenuItem.addSelectionListener(new SelectionListener<MenuEvent>() {
				
				@Override
				public void componentSelected(MenuEvent event) {
					window.prepareForAdd(notificationType);
				}
			});
			addMenu.add(addOfTypeMenuItem);
		}
	}
	
	private void initGrid() {
		
		int width = 150;
		
		ColumnConfig typeColumnConfig = new ColumnConfig();
		typeColumnConfig.setHeader(GwtLocaleProvider.getConstants().TYPE());
		typeColumnConfig.setRenderer(new CustomGridCellRenderer<TransitionNotificationModel>() {
			
			@Override
			public Object doRender(TransitionNotificationModel model, String property, ColumnData config, int rowIndex,
					int colIndex, ListStore<TransitionNotificationModel> store, Grid<TransitionNotificationModel> grid) {
				
				return model.getTypeLabel();
			}
		});
		typeColumnConfig.setWidth(width);

		ColumnConfig emailSubjectColumnConfig = new ColumnConfig();
		emailSubjectColumnConfig.setHeader(GwtLocaleProvider.getConstants().EMAIL_SUBJECT());
		emailSubjectColumnConfig.setId(TransitionNotificationModel.PROPERTY_EMAIL_SUBJECT_TEMPLATE);
		emailSubjectColumnConfig.setWidth(width);
		
		ColumnConfig emailContentColumnConfig = new ColumnConfig();
		emailContentColumnConfig.setHeader(GwtLocaleProvider.getConstants().EMAIL_BODY());
		emailContentColumnConfig.setId(TransitionNotificationModel.PROPERTY_EMAIL_CONTENT_TEMPLATE);
		emailContentColumnConfig.setWidth(width);
		
		ColumnConfig detailsColumnConfig = new ColumnConfig();
		detailsColumnConfig.setHeader(GwtLocaleProvider.getConstants().DETAILS());
		detailsColumnConfig.setRenderer(TransitionNotificationModelDetailsGridCellRenderer.INSTANCE);
		detailsColumnConfig.setWidth(width);
		
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		columnConfigs.add(typeColumnConfig);
		columnConfigs.add(emailSubjectColumnConfig);
		columnConfigs.add(emailContentColumnConfig);
		columnConfigs.add(detailsColumnConfig);
		
		grid = new Grid<TransitionNotificationModel>(new ListStore<TransitionNotificationModel>(), new ColumnModel(columnConfigs));
		grid.setAutoExpandColumn(emailContentColumnConfig.getId());
		grid.setStripeRows(true);
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		grid.addListener(Events.RowDoubleClick, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent event) {
				TransitionNotificationModel selectedNotification = grid.getSelectionModel().getSelectedItem();
				if (selectedNotification == null) {
					return;
				}
				
				Integer selectedNotificationIndex = grid.getStore().indexOf(selectedNotification);
				
				window.prepareForEdit(selectedNotificationIndex, selectedNotification);
			}
		});
		
		add(grid);
	}
	
	public void reset() {
		grid.getStore().removeAll();
		window.reset();
	}
	
	public void setNotifications(List<TransitionNotificationModel> notifications) {
		grid.getStore().add(notifications);
	}
	
	public void doWithSelectedDocumentTypes(List<DocumentTypeModel> documentTypes) {
		window.doWithSelectedDocumentTypes(documentTypes);
	}
	
	public void onSave(Integer index, TransitionNotificationModel savedNotification) {
		if (index == null) {
			grid.getStore().add(savedNotification);
		} else {
			
			TransitionNotificationModel oldNotificationAtIndex = grid.getStore().getAt(index);
			if (oldNotificationAtIndex == null) {
				throw new IllegalStateException("Nu s-a mai gasit notificarea la index-ul [" + index + "].");
			}
			grid.getStore().remove(oldNotificationAtIndex);
			
			grid.getStore().insert(savedNotification, index);
		}
	}
	
	public List<TransitionNotificationModel> getNotifications() {
		return grid.getStore().getModels();
	}
}