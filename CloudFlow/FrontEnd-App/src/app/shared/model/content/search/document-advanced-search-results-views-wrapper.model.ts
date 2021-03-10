import { JsonProperty, JsonObject } from "json2typescript";
import { DocumentAdvancedSearchResultsViewModel } from "./document-advanced-search-results-view.model";

@JsonObject
export class DocumentAdvancedSearchResultsViewsWrapperModel {

	@JsonProperty("representativeMetadataDefinitionLabelById", Object)
	public representativeMetadataDefinitionLabelById: object = null;

	@JsonProperty("searchResultsViews", [DocumentAdvancedSearchResultsViewModel])
	public searchResultsViews: DocumentAdvancedSearchResultsViewModel[] = [];
}