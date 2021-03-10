import { JsonObject, JsonProperty } from "json2typescript";
import { SimpleListItemModel } from "../simple-list-item.model";

@JsonObject
export class PrezentaComisiiGlInIntervalReportBundle {
	
	@JsonProperty("institutieItems", [SimpleListItemModel])
	public institutieItems: SimpleListItemModel[] = [];
	
	@JsonProperty("participantAcreditatItems", [SimpleListItemModel])
	public participantAcreditatItems: SimpleListItemModel[] = [];
}