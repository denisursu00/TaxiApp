import { JsonObject, JsonProperty } from "json2typescript";
import { RegistruIesiriViewModel } from "./registru-iesiri-view.model";

@JsonObject
export class RegistruIesiriViewModelPagingList {
	
	@JsonProperty("totalCount", Number)
	public totalCount: number = undefined;

	@JsonProperty("offset", Number)
	public offset: number = undefined;

	@JsonProperty("elements", [RegistruIesiriViewModel])
	public elements: RegistruIesiriViewModel[] = undefined;
}