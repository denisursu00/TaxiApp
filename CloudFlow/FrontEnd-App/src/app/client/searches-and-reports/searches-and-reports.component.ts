import { Component, ViewChild } from "@angular/core";
import { AdvancedSearchTabContentComponent } from "./advanced-search-tab-content/advanced-search-tab-content.component";
import { DocumentSearchCriteriaModel } from "@app/shared/model/document-search-criteria.model";
import { SimpleSearchTabContentComponent } from "./simple-search-tab-content/simple-search-tab-content.component";
import { DocumentSearchService, MessageDisplayer, AppError, DocumentSimpleSearchResultsViewModel, DocumentAdvancedSearchResultsViewsWrapperModel } from "@app/shared";
import { ResultsTabContentComponent } from "./results-tab-content/results-tab-content.component";

@Component({
	selector: "app-searches-and-reports",
	templateUrl: "searches-and-reports.component.html"
})
export class SearchesAndReportsComponent {

	private static readonly SIMPLE_SEARCH_TAB_INDEX: number = 0;
	private static readonly ADVANCED_SEARCH_TAB_INDEX: number = 1;
	private static readonly RESULTS_TAB_INDEX: number = 2;

	@ViewChild(SimpleSearchTabContentComponent)
	private simpleSearchTab: SimpleSearchTabContentComponent;

	@ViewChild(AdvancedSearchTabContentComponent)
	private advancedSearchTab: AdvancedSearchTabContentComponent;

	@ViewChild(ResultsTabContentComponent)
	private resultsTab: ResultsTabContentComponent;

	private documentSearchService: DocumentSearchService;
	private messageDisplayer: MessageDisplayer;

	public indexOfActiveTab: number;

	public loadingVisible: boolean;
	public isResultsTabVisible: boolean;
	public isSearchButtonVisible: boolean;

	public constructor(documentSearchService: DocumentSearchService, messageDisplayer: MessageDisplayer) {
		this.documentSearchService = documentSearchService;
		this.messageDisplayer = messageDisplayer;
		this.init();
	}
	
	private init(): void {
		this.indexOfActiveTab = SearchesAndReportsComponent.SIMPLE_SEARCH_TAB_INDEX;
		this.loadingVisible = false;
		this.isResultsTabVisible = true;
		this.isSearchButtonVisible = true;
		this.disableResultsTab();
	}

	public onSearchAction(): void {
		this.disableResultsTab();
		let documentSearchCriteria:  DocumentSearchCriteriaModel = new DocumentSearchCriteriaModel();
		if (this.isSimpleTabSelected()) {
			if (!this.simpleSearchTab.isValid()) {
				return;
			}
			this.simpleSearchTab.populateForSearch(documentSearchCriteria);
			this.doSimpleSearch(documentSearchCriteria);
		}
		if (this.isAdvancedTabSelected()) {
			if (!this.advancedSearchTab.isValid()) {
				return;
			}
			this.advancedSearchTab.populateForSearch(documentSearchCriteria);
			this.doAdvancedSearch(documentSearchCriteria);
		}
	}

	private doSimpleSearch(documentSearchCriteria:  DocumentSearchCriteriaModel): void {
		this.lock();
		this.documentSearchService.findDocumentsUsingSimpleSearch(documentSearchCriteria, {
			onSuccess: (documentSimpleSearchResultsViewModel: DocumentSimpleSearchResultsViewModel[]): void => {
				this.resultsTab.displaySimpleSearchResults(documentSearchCriteria.documentLocationRealName, documentSimpleSearchResultsViewModel);
				this.goToResultsTab();
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private doAdvancedSearch(documentSearchCriteria:  DocumentSearchCriteriaModel): void {
		this.lock();
		this.documentSearchService.findDocumentsUsingAdvancedSearch(documentSearchCriteria, {
			onSuccess: (documentAdvancedSearchResultsViewsWrapper: DocumentAdvancedSearchResultsViewsWrapperModel): void => {
				this.resultsTab.displayAdvancedSearchResults(documentSearchCriteria.documentLocationRealName, documentAdvancedSearchResultsViewsWrapper);
				this.goToResultsTab();
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private goToResultsTab(): void {
		this.isResultsTabVisible = true;
		this.indexOfActiveTab = SearchesAndReportsComponent.RESULTS_TAB_INDEX;
		this.isSearchButtonVisible = false;
	}

	private isSimpleTabSelected(): boolean {
		return this.indexOfActiveTab === SearchesAndReportsComponent.SIMPLE_SEARCH_TAB_INDEX;
	}

	private isAdvancedTabSelected(): boolean {
		return this.indexOfActiveTab === SearchesAndReportsComponent.ADVANCED_SEARCH_TAB_INDEX;
	}
	
	private isResultsTabSelected(): boolean {
		return this.indexOfActiveTab === SearchesAndReportsComponent.RESULTS_TAB_INDEX;
	}

	private disableResultsTab(): void {
		this.isResultsTabVisible = false;
	}

	public onTabChanged(event): void {
		this.indexOfActiveTab = event.index;
		this.changeSearchButtonVisibility();
	}

	private changeSearchButtonVisibility(): void {
		this.isSearchButtonVisible = true;
		if (this.isResultsTabSelected()) {
			this.isSearchButtonVisible = false;
		}
	}

	private lock(): void {
		this.loadingVisible = true;
	}

	private unlock(): void {
		this.loadingVisible = false;
	}
}