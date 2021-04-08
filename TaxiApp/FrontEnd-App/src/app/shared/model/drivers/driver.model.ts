import { JsonDateConverter } from "@app/shared/json-mapper";
import { JsonObject, JsonProperty } from "json2typescript";
import { UserModel } from "../organization/user.model";

@JsonObject
export class DriverModel {

    @JsonProperty("id", Number)
	public id: number = null;

    @JsonProperty("birthDate", JsonDateConverter)
	public birthDate: Date = null;

    @JsonProperty("licenceNumber", String)
	public licenceNumber: string = null;

    @JsonProperty("expiryDate", JsonDateConverter)
	public expiryDate: Date = null;

    @JsonProperty("available", Boolean)
	public available: boolean = null;

    @JsonProperty("lastMedExam", JsonDateConverter)
	public lastMedExam: Date = null;

    @JsonProperty("user", UserModel)
	public user: UserModel = null;

}