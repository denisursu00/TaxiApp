import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class GetPagedDocumentsFromFolderRequestModel {

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;

	@JsonProperty("folderId", String)
	public folderId: string = null;

	@JsonProperty("sameType", Boolean)
	public sameType: boolean = null;
}