import { JsonProperty, JsonObject } from "json2typescript";

@JsonObject
export class NomenclatorValueViewModel {
	
	@JsonProperty("id", Number)
	public id: number = undefined;

	@JsonProperty("attributes", [])
	public attributes: AttributeMap<String, String>[] = undefined;
}

export interface AttributeMap<K, V> {
	key: K;
	value: V;
}