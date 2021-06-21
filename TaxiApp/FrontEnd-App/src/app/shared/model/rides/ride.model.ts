import { JsonDateConverter } from "@app/shared/json-mapper";
import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class RideModel {

    @JsonProperty("id", Number)
	public id: number = null;

    @JsonProperty("startTime", JsonDateConverter)
	public startTime: Date = null;

    @JsonProperty("endTime", JsonDateConverter)
	public endTime: Date = null;

    @JsonProperty("startLocation", String)
	public startLocation: string = null;

    @JsonProperty("endLocation", String)
	public endLocation: string = null;

    @JsonProperty("startAdress", String)
	public startAdress: string = null;

    @JsonProperty("endAdress", String)
	public endAdress: string = null;

    @JsonProperty("price", Number)
	public price: number = null;

    @JsonProperty("canceled", Boolean)
	public canceled: boolean = null;

	@JsonProperty("finished", Boolean)
	public finished: boolean = null;

    @JsonProperty("clientId", Number)
	public clientId: number = null;

    @JsonProperty("driverId", Number)
	public driverId: number = null;

	@JsonProperty("dispatcherId", Number)
	public dispatcherId: number = null;

    @JsonProperty("paymentType", String)
	public paymentType: string = null;

	@JsonProperty("observations", String)
	public observations: string = null;

	@JsonProperty("carCategoryId", Number)
	public carCategoryId: number = null;

}

export enum PaymentType {

	CASH = "CASH",
	CARD = "CARD"
}