package ro.cloudSoft.cloudDoc.presentation.client.shared;

import java.util.LinkedList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.replacementProfiles.ReplacementProfileModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils.ConfirmCallback;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtRegistryUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 */
public class ReplacementProfilesInnerPanel extends ContentPanel {
	
	private ReplacementProfileWindow replacementProfileWindow;

	private ToolBar buttonsToolBar;
	
	private Button addButton;
	private Button deleteButton;
	private Button refreshButton;

	private Grid<ReplacementProfileModel> grid;
	
	public ReplacementProfilesInnerPanel() {

		setBodyBorder(false);
		setBorders(false);
		setHeaderVisible(false);
		setLayout(new FitLayout());
		
		replacementProfileWindow = new ReplacementProfileWindow();
		
		initButtonsToolBar();
		initGrid();
	}
	
	private void initButtonsToolBar() {
		
		buttonsToolBar = new ToolBar();
		setTopComponent(buttonsToolBar);
		
		addButton = new Button();
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				replacementProfileWindow.prepareForAdd();
			}
		});
		addButton.setText(GwtLocaleProvider.getConstants().ADD());
		buttonsToolBar.add(addButton);
		
		deleteButton = new Button();
		deleteButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				final ReplacementProfileModel selectedReplacementProfile = getSelectedReplacementProfile();
				if (selectedReplacementProfile != null) {
					ComponentUtils.confirm(GwtLocaleProvider.getConstants().DELETE(),
							GwtLocaleProvider.getMessages().CONFIRM_DELETE_REPLACEMENT_PROFILE(),
							new ConfirmCallback() {
						
						@Override
						public void onYes() {
							
							Long replacementProfileId = selectedReplacementProfile.getId();
							
							LoadingManager.get().loading();
							GwtServiceProvider.getReplacementProfilesService().deleteReplacementProfileById(replacementProfileId, new AsyncCallback<Void>() {

								@Override
								public void onFailure(Throwable exception) {
									MessageUtils.displayError(exception);
									LoadingManager.get().loadingComplete();
								}
								
								@Override
								public void onSuccess(Void nothing) {
									MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(),
										GwtLocaleProvider.getMessages().REPLACEMENT_PROFILE_DELETED());
									AppEventController.fireEvent(AppEventType.ReplacementProfile);
									LoadingManager.get().loadingComplete();
								}
							});
						}
					});
				}
			}
		});
		deleteButton.setText(GwtLocaleProvider.getConstants().DELETE());
		buttonsToolBar.add(deleteButton);
		
		refreshButton = new Button();
		refreshButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				refresh();
			}
		});
		refreshButton.setText(GwtLocaleProvider.getConstants().REFRESH());
		buttonsToolBar.add(refreshButton);
	}
	
	private void initGrid() {
		
		List<ColumnConfig> columnConfigs = new LinkedList<ColumnConfig>();
		
		ColumnConfig requesterUsernameColumnConfig = new ColumnConfig();
		requesterUsernameColumnConfig.setHeader(GwtLocaleProvider.getConstants().REQUESTER());
		requesterUsernameColumnConfig.setHidden(!GwtRegistryUtils.getUserSecurity().isUserAdmin());
		requesterUsernameColumnConfig.setId(ReplacementProfileModel.PROPERTY_REQUESTER_USERNAME);
		requesterUsernameColumnConfig.setWidth(250);
		columnConfigs.add(requesterUsernameColumnConfig);
		
		ColumnConfig replacementColumnConfig = new ColumnConfig();
		replacementColumnConfig.setHeader(GwtLocaleProvider.getConstants().REPLACEMENT());
		replacementColumnConfig.setId(ReplacementProfileModel.PROPERTY_REPLACEMENT_DISPLAY_NAME);
		replacementColumnConfig.setWidth(250);
		columnConfigs.add(replacementColumnConfig);
		
		ColumnConfig startDateColumnConfig = new ColumnConfig();
		startDateColumnConfig.setDateTimeFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		startDateColumnConfig.setHeader(GwtLocaleProvider.getConstants().START_DATE());
		startDateColumnConfig.setId(ReplacementProfileModel.PROPERTY_START_DATE);
		startDateColumnConfig.setWidth(100);
		columnConfigs.add(startDateColumnConfig);
		
		ColumnConfig endDateColumnConfig = new ColumnConfig();
		endDateColumnConfig.setDateTimeFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		endDateColumnConfig.setHeader(GwtLocaleProvider.getConstants().END_DATE());
		endDateColumnConfig.setId(ReplacementProfileModel.PROPERTY_END_DATE);
		endDateColumnConfig.setWidth(100);
		columnConfigs.add(endDateColumnConfig);
		
		ColumnConfig statusColumnConfig = new ColumnConfig();
		statusColumnConfig.setHeader(GwtLocaleProvider.getConstants().STATUS());
		statusColumnConfig.setId(ReplacementProfileModel.PROPERTY_STATUS_LOCALIZED_TEXT);
		statusColumnConfig.setWidth(100);
		columnConfigs.add(statusColumnConfig);
		
		ListStore<ReplacementProfileModel> store = new ListStore<ReplacementProfileModel>();
		ColumnModel columnModel = new ColumnModel(columnConfigs);
		
		grid = new Grid<ReplacementProfileModel>(store, columnModel);
		grid.addListener(Events.RowDoubleClick, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent event) {
				ReplacementProfileModel selectedReplacementProfile = getSelectedReplacementProfile();
				if (selectedReplacementProfile != null) {
					Long replacementProfileId = selectedReplacementProfile.getId();
					replacementProfileWindow.prepareForEdit(replacementProfileId);
				}
			}
		});
		
		if (GwtRegistryUtils.getUserSecurity().isUserAdmin()) {
			grid.setAutoExpandColumn(requesterUsernameColumnConfig.getId());
		} else {
			grid.setAutoExpandColumn(replacementColumnConfig.getId());
		}
		grid.setAutoExpandMax(1000);
		grid.setStripeRows(true);
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		add(grid);
	}
	
	private ReplacementProfileModel getSelectedReplacementProfile() {
		return grid.getSelectionModel().getSelectedItem();
	}
	
	public void refresh() {
		LoadingManager.get().loading();
		GwtServiceProvider.getReplacementProfilesService().getVisibleReplacementProfiles(new AsyncCallback<List<ReplacementProfileModel>>() {
			
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			
			@Override
			public void onSuccess(List<ReplacementProfileModel> replacementProfiles) {
				
				grid.getStore().removeAll();
				grid.getStore().add(replacementProfiles);
				
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	public ToolBar getButtonsToolBar() {
		return buttonsToolBar;
	}
	public Button getAddButton() {
		return addButton;
	}
	public Button getDeleteButton() {
		return deleteButton;
	}
	public Button getRefreshButton() {
		return refreshButton;
	}
}