package ro.cloudSoft.cloudDoc.presentation.client.admin;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.data.AppStoreCache;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.GwtLogActorType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.GwtLogEntrySearchCriteria;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.GwtLogLevel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.log.LogEntryModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtEnumUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtStringUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class LogPanel extends ContentPanel {
	
	private LogEntryDetailsWindow detailsWindow = new LogEntryDetailsWindow();

	private ContentPanel filterContentPanel;
	private ContentPanel logEntriesContentPanel;

	private FormPanel filterFormPanel;
	
	private SimpleComboBox<String> levelSimpleComboBox;
	private DateField timeStartDateField;
	private DateField timeEndDateField;
	
	private TextField<String> moduleTextField;
	private TextField<String> operationTextField;
	
	private SimpleComboBox<String> actorTypeSimpleComboBox;
	private TextField<String> actorDisplayNameTextField;
	private ComboBox<UserModel> userComboBox;
	
	private TextField<String> messageTextField;
	private TextField<String> exceptionTextField;

	private Button resetButton;
	private Button searchButton;
	
	private Grid<LogEntryModel> logEntriesGrid;
	private PagingToolBar logEntriesPagingToolBar;
	
	public LogPanel() {

		setHeading(GwtLocaleProvider.getConstants().HISTORY_AND_ERRORS());
		setLayout(new BorderLayout());
		
		initFilterContentPanel();
		initLogEntriesContentPanel();
		
		addListener(Events.Attach, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				resetForm();
				doSearch();
			};
		});
	}
	
	private void initFilterContentPanel() {
		
		initFilterFields();
		initFilterButtons();

		filterFormPanel = new FormPanel();
		filterFormPanel.setBodyBorder(false);
		filterFormPanel.setBorders(false);
		filterFormPanel.setHeaderVisible(false);
		
		FormData formData = new FormData("90%");
		
		filterFormPanel.add(levelSimpleComboBox, formData);
		filterFormPanel.add(timeStartDateField, formData);
		filterFormPanel.add(timeEndDateField, formData);
		filterFormPanel.add(moduleTextField, formData);
		filterFormPanel.add(operationTextField, formData);
		filterFormPanel.add(actorTypeSimpleComboBox, formData);
		filterFormPanel.add(actorDisplayNameTextField, formData);
		filterFormPanel.add(userComboBox, formData);
		filterFormPanel.add(messageTextField, formData);
		filterFormPanel.add(exceptionTextField, formData);

		filterFormPanel.addButton(resetButton);
		filterFormPanel.addButton(searchButton);
		
		filterContentPanel = new ContentPanel();
		filterContentPanel.setBodyBorder(true);
		filterContentPanel.setBorders(false);
		filterContentPanel.setHeaderVisible(false);
		filterContentPanel.setLayout(new FlowLayout());
		
		filterContentPanel.add(filterFormPanel);
		
		add(filterContentPanel, new BorderLayoutData(LayoutRegion.WEST, 300));
	}
	
	private void initFilterFields() {
		
		levelSimpleComboBox = new SimpleComboBox<String>();
		levelSimpleComboBox.add(GwtEnumUtils.getNames(GwtLogLevel.values()));
		levelSimpleComboBox.setEditable(false);
		levelSimpleComboBox.setFieldLabel(GwtLocaleProvider.getConstants().LEVEL());
		levelSimpleComboBox.setTriggerAction(TriggerAction.ALL);
		
		timeStartDateField = new DateField();
		timeStartDateField.getPropertyEditor().setFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		timeStartDateField.setFieldLabel(GwtLocaleProvider.getConstants().START_DATE());
		
		timeEndDateField = new DateField();
		timeEndDateField.getPropertyEditor().setFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		timeEndDateField.setFieldLabel(GwtLocaleProvider.getConstants().END_DATE());
		
		moduleTextField = new TextField<String>();
		moduleTextField.setFieldLabel(GwtLocaleProvider.getConstants().MODULE());
		
		operationTextField = new TextField<String>();
		operationTextField.setFieldLabel(GwtLocaleProvider.getConstants().OPERATION());
		
		actorTypeSimpleComboBox = new SimpleComboBox<String>();
		actorTypeSimpleComboBox.add(GwtEnumUtils.getNames(GwtLogActorType.values()));
		actorTypeSimpleComboBox.setEditable(false);
		actorTypeSimpleComboBox.setFieldLabel(GwtLocaleProvider.getConstants().ACTOR_TYPE());
		actorTypeSimpleComboBox.setTriggerAction(TriggerAction.ALL);
		
		actorDisplayNameTextField = new TextField<String>();
		actorDisplayNameTextField.setFieldLabel(GwtLocaleProvider.getConstants().ACTOR_NAME());
		
		userComboBox = new ComboBox<UserModel>();
		userComboBox.setDisplayField(UserModel.USER_PROPERTY_DISPLAY_NAME);
		userComboBox.setEditable(false);
		userComboBox.setFieldLabel(GwtLocaleProvider.getConstants().USER());
		userComboBox.setMinListWidth(350);
		userComboBox.setStore(AppStoreCache.getUserListStore());
		userComboBox.setTriggerAction(TriggerAction.ALL);
		
		messageTextField = new TextField<String>();
		messageTextField.setFieldLabel(GwtLocaleProvider.getConstants().MESSAGE());
		
		exceptionTextField = new TextField<String>();
		exceptionTextField.setFieldLabel(GwtLocaleProvider.getConstants().EXCEPTION());
	}
	
	private void initFilterButtons() {
		
		resetButton = new Button();
		resetButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				resetForm();
			}
		});
		resetButton.setText(GwtLocaleProvider.getConstants().RESET());
		
		searchButton = new Button();
		searchButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			
			@Override
			public void componentSelected(ButtonEvent event) {
				doSearch();
			}
		});
		searchButton.setText(GwtLocaleProvider.getConstants().SEARCH());
	}
	
	private void initLogEntriesContentPanel() {
		
		RpcProxy<PagingLoadResult<LogEntryModel>> logEntriesProxy = new RpcProxy<PagingLoadResult<LogEntryModel>>() {

			@Override
			protected void load(Object loadConfig, final AsyncCallback<PagingLoadResult<LogEntryModel>> callback) {
				
				if (!(loadConfig instanceof PagingLoadConfig)) {
					throw new IllegalArgumentException("Proxy-ul functioneaza doar pentru paginare.");
				}
				PagingLoadConfig pagingLoadConfig = (PagingLoadConfig) loadConfig;
				
				GwtLogEntrySearchCriteria searchCriteria = getSearchCriteriaFromForm();
				
				LoadingManager.get().loading();
				GwtServiceProvider.getLogService().searchLog(pagingLoadConfig, searchCriteria, new AsyncCallback<PagingLoadResult<LogEntryModel>>() {
					
					@Override
					public void onSuccess(PagingLoadResult<LogEntryModel> logEntriesPagingLoadResult) {
						callback.onSuccess(logEntriesPagingLoadResult);
						LoadingManager.get().loadingComplete();
					}
					
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						callback.onFailure(exception);
						LoadingManager.get().loadingComplete();
					}
				});
			}
		};
		
		BasePagingLoader<PagingLoadResult<LogEntryModel>> logEntriesLoader = new BasePagingLoader<PagingLoadResult<LogEntryModel>>(logEntriesProxy);
		logEntriesLoader.setRemoteSort(false);
		logEntriesLoader.setReuseLoadConfig(false);
		
		ListStore<LogEntryModel> logEntriesStore = new ListStore<LogEntryModel>(logEntriesLoader);
		
		logEntriesPagingToolBar = new PagingToolBar(25);
		logEntriesPagingToolBar.bind(logEntriesLoader);
		
		ColumnConfig timeColumnConfig = new ColumnConfig();
		timeColumnConfig.setDateTimeFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_LOG);
		timeColumnConfig.setHeader(GwtLocaleProvider.getConstants().DATE_TIME());
		timeColumnConfig.setId(LogEntryModel.PROPERTY_TIME);
		timeColumnConfig.setWidth(160);
		
		ColumnConfig levelColumnConfig = new ColumnConfig();
		levelColumnConfig.setHeader(GwtLocaleProvider.getConstants().LEVEL());
		levelColumnConfig.setId(LogEntryModel.PROPERTY_LEVEL_AS_STRING);
		levelColumnConfig.setWidth(80);
		
		ColumnConfig moduleColumnConfig = new ColumnConfig();
		moduleColumnConfig.setHeader(GwtLocaleProvider.getConstants().MODULE());
		moduleColumnConfig.setId(LogEntryModel.PROPERTY_MODULE);
		moduleColumnConfig.setWidth(450);
		
		ColumnConfig operationColumnConfig = new ColumnConfig();
		operationColumnConfig.setHeader(GwtLocaleProvider.getConstants().OPERATION());
		operationColumnConfig.setId(LogEntryModel.PROPERTY_OPERATION);
		operationColumnConfig.setWidth(250);
		
		ColumnConfig actorDisplayNameColumnConfig = new ColumnConfig();
		actorDisplayNameColumnConfig.setHeader(GwtLocaleProvider.getConstants().ACTOR_NAME());
		actorDisplayNameColumnConfig.setId(LogEntryModel.PROPERTY_ACTOR_DISPLAY_NAME);
		actorDisplayNameColumnConfig.setWidth(200);
		
		ColumnConfig messageColumnConfig = new ColumnConfig();
		messageColumnConfig.setHeader(GwtLocaleProvider.getConstants().MESSAGE());
		messageColumnConfig.setId(LogEntryModel.PROPERTY_MESSAGE);
		messageColumnConfig.setWidth(500);

		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
		columnConfigs.add(timeColumnConfig);
		columnConfigs.add(levelColumnConfig);
		columnConfigs.add(moduleColumnConfig);
		columnConfigs.add(operationColumnConfig);
		columnConfigs.add(actorDisplayNameColumnConfig);
		columnConfigs.add(messageColumnConfig);
		
		ColumnModel columnModel = new ColumnModel(columnConfigs);
		
		logEntriesGrid = new Grid<LogEntryModel>(logEntriesStore, columnModel);
		logEntriesGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		logEntriesGrid.setAutoExpandColumn(messageColumnConfig.getId());
		logEntriesGrid.setAutoExpandMin(messageColumnConfig.getWidth());
		logEntriesGrid.setAutoExpandMax(1000);
		logEntriesGrid.setStripeRows(true);
		
		logEntriesGrid.addListener(Events.RowDoubleClick, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				LogEntryModel selectedEntry = logEntriesGrid.getSelectionModel().getSelectedItem();
				if (selectedEntry != null) {
					detailsWindow.displayLogEntry(selectedEntry);
				}
			}
		});

		logEntriesContentPanel = new ContentPanel();
		logEntriesContentPanel.setBodyBorder(true);
		logEntriesContentPanel.setBorders(false);
		logEntriesContentPanel.setHeaderVisible(false);
		logEntriesContentPanel.setLayout(new FitLayout());
		
		logEntriesContentPanel.add(logEntriesGrid);
		logEntriesContentPanel.setBottomComponent(logEntriesPagingToolBar);
		
		add(logEntriesContentPanel, new BorderLayoutData(LayoutRegion.CENTER));
	}
	
	private void resetForm() {
		filterFormPanel.clear();
	}
	
	private GwtLogEntrySearchCriteria getSearchCriteriaFromForm() {
		
		GwtLogEntrySearchCriteria searchCriteria = new GwtLogEntrySearchCriteria();
		
		String levelAsString = levelSimpleComboBox.getSimpleValue();
		if (GwtStringUtils.isNotBlank(levelAsString)) {
			GwtLogLevel level = GwtLogLevel.valueOf(levelAsString);
			searchCriteria.setLevel(level);
		}
		
		searchCriteria.setTimeStartDate(timeStartDateField.getValue());
		searchCriteria.setTimeEndDate(timeEndDateField.getValue());
		searchCriteria.setModuleText(moduleTextField.getValue());
		searchCriteria.setOperationText(operationTextField.getValue());
		
		String actorTypeAsString = actorTypeSimpleComboBox.getSimpleValue();
		if (GwtStringUtils.isNotBlank(actorTypeAsString)) {
			GwtLogActorType actorType = GwtLogActorType.valueOf(actorTypeAsString);
			searchCriteria.setActorType(actorType);
		}
		
		searchCriteria.setActorDisplayNameText(actorDisplayNameTextField.getValue());
		
		UserModel user = userComboBox.getValue();
		if (user != null) {
			Long userId = user.getUserIdAsLong();
			searchCriteria.setUserId(userId);
		}
		
		searchCriteria.setMessageText(messageTextField.getValue());
		searchCriteria.setExceptionText(exceptionTextField.getValue());

		return searchCriteria;
	}
	
	private void doSearch() {
		logEntriesPagingToolBar.first();
	}
}