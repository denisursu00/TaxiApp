import { Injectable } from "@angular/core";
import { ApiPathConstants } from "./../constants/api-path.constants";
import { AppError } from "./../model/app-error";
import { AsyncCallback } from "./../async-callback";
import { ApiCaller } from "./../api-caller";
import { MyActivitiesModel } from "../model/my-activities.model";
import { DocumentSearchCriteriaModel } from "../model/document-search-criteria.model";
import { DocumentSimpleSearchResultsViewModel } from "../model/content/search/document-simple-search-results-view.model";
import { DocumentAdvancedSearchResultsViewsWrapperModel } from "../model/content/search/document-advanced-search-results-views-wrapper.model";
import { DocumentSelectionSearchFilterModel, DocumentSelectionSearchResultViewModel } from "./../model";

@Injectable()
export class DocumentSearchService {

	private apiCaller: ApiCaller;

	public constructor(apiCaller: ApiCaller) {
		this.apiCaller = apiCaller;
	}

	public getMyActivities(callback: AsyncCallback<MyActivitiesModel[], AppError>): void {
		this.apiCaller.call<MyActivitiesModel[]>(ApiPathConstants.GET_MY_ACTIVITIES, null, MyActivitiesModel, callback);
	}

	public findDocumentsUsingSimpleSearch(documentSearchCriteria: DocumentSearchCriteriaModel, callback: AsyncCallback<DocumentSimpleSearchResultsViewModel[], AppError>): void {
		this.apiCaller.call<DocumentSimpleSearchResultsViewModel[]>(
			ApiPathConstants.FIND_DOCUMENTS_USING_SIMPLE_SEARCH,
			documentSearchCriteria,
			DocumentSimpleSearchResultsViewModel,
			callback
		);
	}

	public findDocumentsUsingAdvancedSearch(documentSearchCriteria: DocumentSearchCriteriaModel, callback: AsyncCallback<DocumentAdvancedSearchResultsViewsWrapperModel, AppError>): void {
		this.apiCaller.call<DocumentAdvancedSearchResultsViewsWrapperModel>(
			ApiPathConstants.FIND_DOCUMENTS_USING_ADVANCED_SEARCH,
			documentSearchCriteria,
			DocumentAdvancedSearchResultsViewsWrapperModel,
			callback
		);
	}

	public findDocumentsForSelection(filterModel: DocumentSelectionSearchFilterModel, callback: AsyncCallback<DocumentSelectionSearchResultViewModel[], AppError>): void {
		this.apiCaller.call<DocumentSelectionSearchResultViewModel[]>(
			ApiPathConstants.FIND_DOCUMENTS_FOR_SELECTION,
			filterModel,
			DocumentSelectionSearchResultViewModel,
			callback
		);
	}
}
