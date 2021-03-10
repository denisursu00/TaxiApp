import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class GetFolderPathRequestModel {

	public constructor(documentLocationRealName: string, folderId: string) {
		this.documentLocationRealName = documentLocationRealName;
		this.folderId = folderId;
	}

	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string;

	@JsonProperty("folderId", String)
	public folderId: string;
}