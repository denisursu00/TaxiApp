import { JsonObject, JsonProperty } from "json2typescript";
import { RegistruIntrariViewModel } from "./registru-intrari-view.model";

@JsonObject
export class RegistruIntrariViewModelPagingList {
	
	@JsonProperty("totalCount", Number)
	public totalCount: number = undefined;

	@JsonProperty("offset", Number)
	public offset: number = undefined;

	@JsonProperty("elements", [RegistruIntrariViewModel])
	public elements: RegistruIntrariViewModel[] = undefined;
}