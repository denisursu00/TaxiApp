import { JsonObject, JsonProperty } from "json2typescript";
import { ListMetadataItemModel } from "./../content/list-metadata-item.model";

@JsonObject
export class NumarParticipantiSedinteComisieGlReportBundle {
	
	@JsonProperty("calitateMembruItems", [ListMetadataItemModel])
	public calitateMembruItems: ListMetadataItemModel[] = [];
}