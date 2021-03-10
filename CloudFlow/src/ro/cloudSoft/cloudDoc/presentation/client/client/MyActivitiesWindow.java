package ro.cloudSoft.cloudDoc.presentation.client.client;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.client.document.DocumentWindow;
import ro.cloudSoft.cloudDoc.presentation.client.client.utils.widgets.WindowToBeUsedOnDesktop;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.ModelConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEvent;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventController;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventHandler;
import ro.cloudSoft.cloudDoc.presentation.client.shared.event.AppEventType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.MyActivitiesViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MyActivitiesWindow extends WindowToBeUsedOnDesktop implements AppEventHandler {

	private ToolBar toolBar;
	private ToggleButton iconsMyActivitiesViewButton;
	private ToggleButton detailsMyActivitiesViewButton;
	private Button refreshMyActivitiesButton;
	
	private ListStore<MyActivitiesViewModel> myActivitiesStore;
	private Grid<MyActivitiesViewModel> myActivitiesGrid;
	private ListView<MyActivitiesViewModel> myActivitiesListView;
	
	private DocumentWindow documentWindow;
	
	public MyActivitiesWindow() {
		setHeading(GwtLocaleProvider.getConstants().MY_ACTIVITIES());
		setMaximizable(true);
		setMinimizable(true);
		int width = com.google.gwt.user.client.Window.getClientWidth() - 300;
		int height = com.google.gwt.user.client.Window.getClientHeight() - 100;
		setSize(width, height);
		setLayout(new FitLayout());
		
		documentWindow = WindowManager.getDocumentWindow();
		
		initDocumentToolBar();
		myActivitiesStore = new ListStore<MyActivitiesViewModel>();
		initDocumentsGrid();
		initDocumentsListView();
		changeMyActivitiesViewPerspective(MyActivitiesView.DETAILS);
		addListeners();
		
		addListener(Events.Show, new Listener<BaseEvent>() {
			@Override
			public void handleEvent(BaseEvent be) {
				loadMyActivities();				
			}
		});
		
		AppEventController.subscribe(this, AppEventType.Task);
	}
	
	
	private void initDocumentToolBar(){
		toolBar = new ToolBar();
		
		detailsMyActivitiesViewButton = new ToggleButton();
		detailsMyActivitiesViewButton.setIconStyle("view_list");
		detailsMyActivitiesViewButton.setToolTip(GwtLocaleProvider.getConstants().VIEW_DETAILS_TOOLTIP());
		detailsMyActivitiesViewButton.addSelectionListener(
				new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent event) {
				changeMyActivitiesViewPerspective(MyActivitiesView.DETAILS);
			}
		});

		iconsMyActivitiesViewButton = new ToggleButton();
		iconsMyActivitiesViewButton.setIconStyle("view_icons");
		iconsMyActivitiesViewButton.setToolTip(GwtLocaleProvider.getConstants().VIEW_ICON_TOOLTIP());
		iconsMyActivitiesViewButton.addSelectionListener(
				new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent event) {
				changeMyActivitiesViewPerspective(MyActivitiesView.ICONS);
			}
		});
		
		refreshMyActivitiesButton = new Button();
		refreshMyActivitiesButton.setIconStyle("icon-refresh");
		
		toolBar.add(iconsMyActivitiesViewButton);
		toolBar.add(detailsMyActivitiesViewButton);
		toolBar.add(new SeparatorToolItem());
		toolBar.add(refreshMyActivitiesButton);
		setTopComponent(toolBar);
	}
	
	private void initDocumentsGrid() {
		List<ColumnConfig> cols = new ArrayList<ColumnConfig>();
		
		ColumnConfig colDocumentName = new ColumnConfig();
		colDocumentName.setHeader(GwtLocaleProvider.getConstants().DOCUMENT_NAME());
		colDocumentName.setId(MyActivitiesViewModel.PROPERTY_DOCUMENT_NAME);
		colDocumentName.setWidth(100);

		ColumnConfig colSender = new ColumnConfig();
		colSender.setHeader(GwtLocaleProvider.getConstants().WORKFLOW_SENDER());
		colSender.setId(MyActivitiesViewModel.PROPERTY_WORKFLOW_SENDER);
		colSender.setWidth(100);
		
		ColumnConfig colCurrentStatus = new ColumnConfig();
		colCurrentStatus.setHeader(GwtLocaleProvider.getConstants().WORKFLOW_CURRENT_STATUS());
		colCurrentStatus.setId(MyActivitiesViewModel.PROPERTY_WORKFLOW_CURRENT_STATUS);
		colCurrentStatus.setWidth(100);
		
		ColumnConfig colWorkflow = new ColumnConfig();
		colWorkflow.setHeader(GwtLocaleProvider.getConstants().WORKFLOW());
		colWorkflow.setId(MyActivitiesViewModel.PROPERTY_WORKFLOW_NAME);
		colWorkflow.setWidth(100);

		ColumnConfig colDocumentType = new ColumnConfig();
		colDocumentType.setHeader(GwtLocaleProvider.getConstants().DOCUMENT_TYPE_NAME());
		colDocumentType.setId(MyActivitiesViewModel.PROPERTY_DOCUMENT_TYPE_NAME);
		colDocumentType.setWidth(100);
		
		ColumnConfig colCreatedDate = new ColumnConfig();
		colCreatedDate.setHeader(GwtLocaleProvider.getConstants().DOC_CREATED());
		colCreatedDate.setId(MyActivitiesViewModel.PROPERTY_DOCUMENT_CREATED_DATE);
		colCreatedDate.setDateTimeFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		colCreatedDate.setWidth(100);
		
		ColumnConfig colAuthor = new ColumnConfig();
		colAuthor.setHeader(GwtLocaleProvider.getConstants().DOC_AUTHOR());
		colAuthor.setId(MyActivitiesViewModel.PROPERTY_DOCUMENT_AUTHOR);
		colAuthor.setWidth(100);
		
		cols.add(colDocumentName);
		cols.add(colSender);
		cols.add(colCurrentStatus);
		cols.add(colWorkflow);
		cols.add(colDocumentType);
		cols.add(colCreatedDate);
		cols.add(colAuthor);
		
		ColumnModel model = new ColumnModel(cols);
				
		myActivitiesGrid = new Grid<MyActivitiesViewModel>(myActivitiesStore, model);
		myActivitiesGrid.setAutoExpandColumn(MyActivitiesViewModel.PROPERTY_DOCUMENT_NAME);
		GridView view = new GridView();
		view.setForceFit(true);
		myActivitiesGrid.setView(view);
	}
	
	private void initDocumentsListView() {
		myActivitiesListView = new ListView<MyActivitiesViewModel>() {
			@Override
			protected MyActivitiesViewModel prepareData(
					MyActivitiesViewModel model) {
				String documentName = model.getDocumentName();
                model.set(ModelConstants.PROPERTY_SHORT_NAME, Format.ellipse(documentName, 15));
                return model;
			}
		};
		myActivitiesListView.setStore(myActivitiesStore);
		myActivitiesListView.setTemplate(getDocumentsListViewTemplate());
		myActivitiesListView.setItemSelector("div.container");
		myActivitiesListView.setBorders(false);
	}
	
	private enum MyActivitiesView {
		ICONS,
		DETAILS
	}
	
	private void changeMyActivitiesViewPerspective(MyActivitiesView documentView) {
		removeAll();
		setTopComponent(toolBar);
		switch (documentView) {
			case ICONS:
				iconsMyActivitiesViewButton.toggle(true);
				detailsMyActivitiesViewButton.toggle(false);
				add(myActivitiesListView);
				break;
			case DETAILS:
				iconsMyActivitiesViewButton.toggle(false);
				detailsMyActivitiesViewButton.toggle(true);
				add(myActivitiesGrid);
				break;
		}
		layout();
	}
	
	private String getDocumentsListViewTemplate() {
		StringBuilder template = new StringBuilder();

		template.append("<tpl for=\".\">");
		template.append("	<div class=\"container\" id=\"{" + MyActivitiesViewModel.PROPERTY_DOCUMENT_NAME + "}\" style=\"display: inline-block; text-align: center; width: 128px;\">");
		template.append("		<div>");
		template.append("			<img src=\"" + GWT.getModuleBaseURL() + "images/gray/interfata/file.png\" title=\"{" + MyActivitiesViewModel.PROPERTY_DOCUMENT_NAME + "}\">");
		template.append("		</div>");
		template.append("		<div>{" + ModelConstants.PROPERTY_SHORT_NAME + "}</div>");
		template.append("	</div>");
		template.append("</tpl>");
		template.append("<div class=\"x-clear\"></div>");
		
		return template.toString();
    }
	
	private void addListeners(){
		refreshMyActivitiesButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				loadMyActivities();
			}
		});
		
		Listener<BaseEvent> doubleClickOnActivityListener = new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				MyActivitiesViewModel selectedActivity = getSeletedActivity();
				String documentLocationRealName = selectedActivity.getDocumentLocationRealName();
				if ((selectedActivity != null) && (documentLocationRealName != null)) {
					toBack();
					documentWindow.prepareForViewOrEdit(selectedActivity.getDocumentId(), documentLocationRealName);
				}
			}
		};
		myActivitiesGrid.addListener(Events.RowDoubleClick, doubleClickOnActivityListener);
		myActivitiesListView.addListener(Events.DoubleClick, doubleClickOnActivityListener);
	}
	
	private void loadMyActivities(){
		myActivitiesStore.removeAll();
		LoadingManager.get().loading();
		GwtServiceProvider.getDocumentSearchService().getMyActivites(new AsyncCallback<List<MyActivitiesViewModel>>(){
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(List<MyActivitiesViewModel> myActivities) {
				myActivitiesStore.add(myActivities);
				LoadingManager.get().loadingComplete();			
			}
		});
	}
	
	private MyActivitiesViewModel getSeletedActivity() {
		if (iconsMyActivitiesViewButton.isPressed())
			return myActivitiesListView.getSelectionModel().getSelectedItem();
		else if (detailsMyActivitiesViewButton.isPressed())
			return myActivitiesGrid.getSelectionModel().getSelectedItem();
		else
			return null;
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		if (event.getType() == AppEventType.Task) {
			if (isVisible() || ComponentUtils.isWindowMinimized(this)) {
				loadMyActivities();
			}
		}
	}
}