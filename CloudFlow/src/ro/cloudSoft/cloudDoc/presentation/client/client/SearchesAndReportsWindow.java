package ro.cloudSoft.cloudDoc.presentation.client.client;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.client.search.AdvancedSearchTab;
import ro.cloudSoft.cloudDoc.presentation.client.client.search.SearchResultsWindow;
import ro.cloudSoft.cloudDoc.presentation.client.client.search.SimpleSearchTab;
import ro.cloudSoft.cloudDoc.presentation.client.client.utils.widgets.WindowToBeUsedOnDesktop;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSearchCriteriaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentAdvancedSearchResultsViewsWrapperModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentSimpleSearchResultsViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SearchesAndReportsWindow extends WindowToBeUsedOnDesktop {

	private TabPanel tabPanel;
	private SimpleSearchTab simpleSearchTab;
	private AdvancedSearchTab advancedSearchTab;
	private Button searchButton;
	private Button reportButton;
	
	private SearchResultsWindow resultsWindow = new SearchResultsWindow();
	
	public SearchesAndReportsWindow() {
		
		setHeading(GwtLocaleProvider.getConstants().SEARCHES_AND_REPORTS());
		setMaximizable(true);
		setMinimizable(true);
		int width = com.google.gwt.user.client.Window.getClientWidth() - 300;
		int height = com.google.gwt.user.client.Window.getClientHeight() - 100;
		setSize(width, height);
		setLayout(new FitLayout());
		
		initTabPanel();
		
		searchButton = new Button(GwtLocaleProvider.getConstants().SEARCH());
		reportButton = new Button(GwtLocaleProvider.getConstants().REPORT());
		
		add(tabPanel);
		addButton(searchButton);
		addButton(reportButton);
		
		addListeners();
	}
	
	private void initTabPanel(){
		tabPanel = new TabPanel();
		
		simpleSearchTab = new SimpleSearchTab();
		advancedSearchTab = new AdvancedSearchTab();
		
		tabPanel.add(simpleSearchTab);
		tabPanel.add(advancedSearchTab);
	}
	
	public void prepareTabForms() {
		simpleSearchTab.prepareSimpleSearchForm();
		advancedSearchTab.prepareAdvancedSearchForm();
	}
	
	private void addListeners() {
		
		this.addListener(Events.Show, new Listener<ComponentEvent>() {
			public void handleEvent(ComponentEvent ce) {
				SearchesAndReportsWindow.this.prepareTabForms();
			}
		});
		
		searchButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				final DocumentSearchCriteriaModel criteria = getDocumentSearchCriteriaModel();
				if (criteria != null){
					final String documentLocationRealName =  criteria.getDocumentLocationRealName();
					if (tabPanel.getSelectedItem().equals(simpleSearchTab)){
						LoadingManager.get().loading();
						GwtServiceProvider.getDocumentSearchService().findDocumentsUsingSimpleSearch(criteria,
								new AsyncCallback<List<DocumentSimpleSearchResultsViewModel>>() {
							
							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
								LoadingManager.get().loadingComplete();
							}
							
							@Override
							public void onSuccess(List<DocumentSimpleSearchResultsViewModel> searchResultsViews) {
								resultsWindow.displayResults(documentLocationRealName, searchResultsViews);
								LoadingManager.get().loadingComplete();
							}
						});
					} else if (tabPanel.getSelectedItem().equals(advancedSearchTab)) {
						LoadingManager.get().loading();
						GwtServiceProvider.getDocumentSearchService().findDocumentsUsingAdvancedSearch(criteria,
								new AsyncCallback<DocumentAdvancedSearchResultsViewsWrapperModel>() {
							
							@Override
							public void onFailure(Throwable exception) {
								MessageUtils.displayError(exception);
								LoadingManager.get().loadingComplete();
							}
							
							@Override
							public void onSuccess(DocumentAdvancedSearchResultsViewsWrapperModel searchResultsViewsWrapper) {
								resultsWindow.displayResults(documentLocationRealName, searchResultsViewsWrapper);
								LoadingManager.get().loadingComplete();
							}
						});
					} else {
						throw new IllegalStateException("Cautare necunoscuta: [" + tabPanel.getSelectedItem().getText() + "]");
					}
				}
			}
		});
		
		reportButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (tabPanel.getSelectedItem().equals(simpleSearchTab))
					simpleSearchTab.submitSimpleSearchForm();
				else
					advancedSearchTab.submitAdvancedSearchForm();
			}
		});
	}
	
	private DocumentSearchCriteriaModel getDocumentSearchCriteriaModel(){
		DocumentSearchCriteriaModel documentSearchCriteriaModel = null;
		if (tabPanel.getSelectedItem().equals(simpleSearchTab)){
			if (simpleSearchTab.isSimpleSearchFromValid()){
				documentSearchCriteriaModel = new DocumentSearchCriteriaModel();
				simpleSearchTab.populateForSearch(documentSearchCriteriaModel);
			}
		}else{
			if (advancedSearchTab.isAdvancedSearchFormValid()){
				documentSearchCriteriaModel = new DocumentSearchCriteriaModel();
				advancedSearchTab.populateForSearch(documentSearchCriteriaModel);
			}
		}
		return documentSearchCriteriaModel;
	}
}