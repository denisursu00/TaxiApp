import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "./../../../json-mapper";

@JsonObject
export class DocumentSelectionSearchResultViewModel {
	
	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	@JsonProperty("documentId", String)
	public documentId: string = null;

	@JsonProperty("documentName", String)
	public documentName: string = null;
	
	@JsonProperty("author", String)
	public author: string = null;
	
	@JsonProperty("createdDate", JsonDateConverter)
	public createdDate: Date = null;
}