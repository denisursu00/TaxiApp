package ro.cloudSoft.cloudDoc.presentation.client.admin;

import ro.cloudSoft.cloudDoc.presentation.client.admin.audit.AuditFilterFormPanel;
import ro.cloudSoft.cloudDoc.presentation.client.admin.audit.AuditSearchResultsGrid;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;

public class AuditPanel extends ContentPanel {

	private AuditFilterFormPanel filterFormPanel;
	private ContentPanel searchResultsPanel;
	
	private AuditSearchResultsGrid searchResultsGrid;
	private PagingToolBar pagingToolBar;
	
	public AuditPanel() {
		
		setHeading(GwtLocaleProvider.getConstants().AUDIT());
		setLayout(new BorderLayout());
		
		initFilterFormPanel();
		add(filterFormPanel, new BorderLayoutData(LayoutRegion.WEST, 300));
		
		initSearchResultsPanel();
		add(searchResultsPanel, new BorderLayoutData(LayoutRegion.CENTER));
		
		addListener(Events.Attach, new Listener<BaseEvent>() {
			
			@Override
			public void handleEvent(BaseEvent be) {
				doSearch();
			};
		});
	}
	
	private void initFilterFormPanel() {
		filterFormPanel = new AuditFilterFormPanel(new Runnable() {
			
			@Override
			public void run() {
				doSearch();
			}
		});
	}
	
	private void initSearchResultsPanel() {
		
		searchResultsPanel = new ContentPanel();
		searchResultsPanel.setBodyBorder(true);
		searchResultsPanel.setBorders(false);
		searchResultsPanel.setHeaderVisible(false);
		searchResultsPanel.setLayout(new FitLayout());
		
		searchResultsGrid = new AuditSearchResultsGrid(filterFormPanel);
		searchResultsPanel.add(searchResultsGrid);
		
		pagingToolBar = new PagingToolBar(25);
		pagingToolBar.bind(searchResultsGrid.getPagingLoader());
		searchResultsPanel.setBottomComponent(pagingToolBar);
	}
	
	private void doSearch() {
		pagingToolBar.first();
	}
}