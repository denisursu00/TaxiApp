import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class ListMetadataItemModel {

	@JsonProperty("id", Number)
	public id: number = null;

	@JsonProperty("value", String)
	public value: string = null;

	@JsonProperty("label", String)
	public label: string = null;

	@JsonProperty("orderNumber", Number)
	public orderNumber: number = null;

	public clone(): ListMetadataItemModel {
		let clone: ListMetadataItemModel = new ListMetadataItemModel();
		clone.id = this.id;
		clone.value = this.value;
		clone.label = this.label;
		clone.orderNumber = this.orderNumber;
		return clone;
	}
}