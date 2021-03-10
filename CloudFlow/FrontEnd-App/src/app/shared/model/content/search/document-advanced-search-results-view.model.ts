import { DocumentSimpleSearchResultsViewModel } from "./document-simple-search-results-view.model";
import { JsonObject, JsonProperty } from "json2typescript";
import { DocumentSearchResultsViewModel } from "./document-search-results-view.model";

@JsonObject
export class DocumentAdvancedSearchResultsViewModel extends DocumentSearchResultsViewModel {

	@JsonProperty("documentMetadataInstanceDisplayValueByDefinitionId", Object)
	public documentMetadataInstanceDisplayValueByDefinitionId: object = null;
}