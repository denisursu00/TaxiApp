package ro.cloudSoft.cloudDoc.presentation.client.admin;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.admin.groups.GroupWindow;
import ro.cloudSoft.cloudDoc.presentation.client.shared.CustomGridCellRenderer;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
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
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class GroupsPanel extends ContentPanel {
	
	private ToolBar toolBar;
	private Button addButton;
	private Button delButton;
	private Button refreshButton;
	private Grid<GroupModel> groupsGrid;
	private GroupWindow groupWindow;  
	
	public GroupsPanel() {
		super();
		this.setBodyBorder(true);
		initWindow();
		groupWindow = new GroupWindow(this);
		initToolBar();
		initGroupsGrid();
		addButtonActions();
	}
	
	private void initWindow(){
		this.setHeading(GwtLocaleProvider.getConstants().ADMIN_GROUPS());
		this.setLayout( new FitLayout());

		addListener( Events.Attach, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent event) {
				refresh();
			};
		});
	}
	
	private void initToolBar(){
		toolBar = new ToolBar();
		toolBar.setStyleName("admin_toolbar");
		toolBar.setSpacing(10);
		toolBar.setBorders(true);
		
		addButton = new Button();
		addButton.setText(GwtLocaleProvider.getConstants().ADD());
		addButton.setIconStyle("icon-add");
		addButton.setStyleName("admin_toolbar_buttons");
		addButton.setBorders(true);
		

		delButton = new Button();
		delButton.setText(GwtLocaleProvider.getConstants().DELETE());
		delButton.setIconStyle("icon-delete");
		delButton.setStyleName("admin_toolbar_buttons");
		delButton.setBorders(true);

		refreshButton = new Button();
		refreshButton.setText(GwtLocaleProvider.getConstants().REFRESH());
		refreshButton.setIconStyle("icon-refresh");
		refreshButton.setStyleName("admin_toolbar_buttons");
		refreshButton.setBorders(true);
		
		toolBar.add( addButton );
		toolBar.add( delButton );
		toolBar.add( refreshButton );
		setTopComponent( toolBar );
	}
	
	private void addButtonActions() {
		addButton.addSelectionListener( new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected( ButtonEvent event) {
				groupWindow.prepareForAdd();
			};
		});	
		
		delButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
			
			@Override
			public void handleEvent(ButtonEvent be) {
				final GroupModel selectedGroup = getSelectedGroup();
				if (selectedGroup != null) {
					ComponentUtils.confirm(GwtLocaleProvider.getConstants().DELETE(),
							GwtLocaleProvider.getMessages().CONFIRM_DELETE_GROUP(),
							new ConfirmCallback() {
						
						@Override
						public void onYes() {
							LoadingManager.get().loading();
							GwtServiceProvider.getOrgService().deleteGroup(selectedGroup, new AsyncCallback<Void>() {
								
								@Override
								public void onFailure(Throwable exception) {
									MessageUtils.displayError(exception);
									LoadingManager.get().loadingComplete();
								}
								
								@Override
								public void onSuccess(Void nothing) {
									
									MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().GROUP_DELETED());
									refresh();
									
									LoadingManager.get().loadingComplete();
								}
							});
						}
					});
				}		
			}
		});
		
		refreshButton.addSelectionListener( new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected( ButtonEvent event) {
				refresh();
			};
		});
	}
	
	private void initGroupsGrid() {
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		
		ColumnConfig iconColumnConfig = new ColumnConfig();	
		iconColumnConfig.setId("icon");
		iconColumnConfig.setWidth(2);		
		iconColumnConfig.setRenderer(new CustomGridCellRenderer<GroupModel>() {
			
			@Override
			public Object doRender(GroupModel model, String property, ColumnData config, int rowIndex,
					int colIndex, ListStore<GroupModel> store, Grid<GroupModel> grid) {
				
				AbstractImagePrototype icon = IconHelper.createStyle("icon-role", 16, 16);
				return icon.createImage();				
			}			
		});
		
		ColumnConfig nameColumnConfig = new ColumnConfig();
		nameColumnConfig.setHeader(GwtLocaleProvider.getConstants().NAME());
		nameColumnConfig.setId( GroupModel.PROPERTY_NAME );
		nameColumnConfig.setWidth( 120 );
		
		ColumnConfig descriptionColumnConfig = new ColumnConfig();
		descriptionColumnConfig.setHeader(GwtLocaleProvider.getConstants().DESCRIPTION());
		descriptionColumnConfig.setId( GroupModel.PROPERTY_DESCRIPTION );
		descriptionColumnConfig.setWidth( 1000 );
		
		columnConfigs.add(iconColumnConfig);
		columnConfigs.add( nameColumnConfig );
		columnConfigs.add( descriptionColumnConfig );
		
		ColumnModel columnModel = new ColumnModel( columnConfigs );
		
		groupsGrid = new Grid<GroupModel>( new ListStore<GroupModel>(), columnModel );
		groupsGrid.setAutoExpandColumn( GroupModel.PROPERTY_DESCRIPTION );
		groupsGrid.getSelectionModel().setSelectionMode( SelectionMode.SINGLE );
		
		GridView gridView = new GridView();
		gridView.setForceFit(true);
		groupsGrid.setView(gridView);
		groupsGrid.setStripeRows(true);
	
		
		add( groupsGrid );
		
		groupsGrid.addListener( Events.OnClick, new Listener<GridEvent<GroupModel>>() {
			
			public void handleEvent(GridEvent<GroupModel> event) {
				// Ia entitatea selectata.
				GroupModel selectedGroup = event.getModel();
				changeToolBarPerspective( selectedGroup );
			}
		});
		groupsGrid.addListener( Events.OnDoubleClick, new Listener<GridEvent<GroupModel>>() {
			
			public void handleEvent( GridEvent<GroupModel> event ) {
				// Ia entitatea selectata.
				GroupModel selectedGroup = event.getModel();
				changeToolBarPerspective( selectedGroup );
				if ( selectedGroup != null ) {
					groupWindow.prepareForEdit( selectedGroup.getId() );
				}
			}
		});
	}
	
	private void changeToolBarPerspective(GroupModel groupModel) {
		if ( groupModel != null ) {
			delButton.setEnabled(true);
		} else {
			delButton.setEnabled(false);			
		}
	}
	
	public void refresh() {
		changeToolBarPerspective(null);
		LoadingManager.get().loading();
		GwtServiceProvider.getOrgService().getGroups(new AsyncCallback<List<GroupModel>>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(List<GroupModel> groups) {
				groupsGrid.getStore().removeAll();
				groupsGrid.getStore().add(groups);
				LoadingManager.get().loadingComplete();
			}			
		});
		
	}
	
	private GroupModel getSelectedGroup() {
		return groupsGrid.getSelectionModel().getSelectedItem();
	}	
}