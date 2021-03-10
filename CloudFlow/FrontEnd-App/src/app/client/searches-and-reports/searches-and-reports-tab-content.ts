import { DocumentSearchCriteriaModel } from "@app/shared/model/document-search-criteria.model";

export abstract class SearchesAndReportsTabContent {

	protected abstract populateForSearch(documentSearchCriteriaModel: DocumentSearchCriteriaModel): void;
	
	protected abstract reset(): void;
	
	protected abstract isValid(): boolean;
}