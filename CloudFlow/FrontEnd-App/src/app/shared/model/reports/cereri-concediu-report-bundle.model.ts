import { JsonObject, JsonProperty } from "json2typescript";
import { ListMetadataItemModel } from "./../content/list-metadata-item.model";

@JsonObject
export class CereriConcediuReportBundle {
	
	@JsonProperty("tipConcediuItems", [ListMetadataItemModel])
	public tipConcediuItems: ListMetadataItemModel[] = [];
}