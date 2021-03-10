import { JsonProperty, JsonObject } from "json2typescript";

@JsonObject
export class MoveFolderRequestModel {
	
	@JsonProperty("folderToMoveId", String)
	public folderToMoveId: string = null;
	
	@JsonProperty("destinationFolderId", String)
	public destinationFolderId: string = null;
	
	@JsonProperty("documentLocationRealName", String)
	public documentLocationRealName: string = null;
}