import { DocumentSearchResultsViewModel } from "./document-search-results-view.model";
import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class DocumentSimpleSearchResultsViewModel extends DocumentSearchResultsViewModel {

	@JsonProperty("documentTypeName", String)
	public documentTypeName: string = null;
}