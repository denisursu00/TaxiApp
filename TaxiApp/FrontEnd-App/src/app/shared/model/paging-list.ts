import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class PagingList<T> {

	@JsonProperty("totalCount", Number)
	public totalCount: number = undefined;

	@JsonProperty("offset", Number)
	public offset: number = undefined;

	@JsonProperty("elements", [])
	public elements: T[] = undefined;
}