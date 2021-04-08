import { JsonDateConverter } from "@app/shared/json-mapper";
import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class CarModel {

    @JsonProperty("id", Number)
	public id: number = null;

    @JsonProperty("model", String)
	public model: string = null;

    @JsonProperty("registrationNumber", String)
	public registrationNumber: string = null;

    @JsonProperty("lastTechControl", JsonDateConverter)
	public lastTechControl: Date = null;

    @JsonProperty("driverId", Number)
	public driverId: number = null;

}