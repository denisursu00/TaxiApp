import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";

@JsonObject
export class DspParticipantViewModel {

	@JsonProperty("numeParticipant", String)
	public numeParticipant: string = null;
}