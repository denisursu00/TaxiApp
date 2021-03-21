import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class PasswordChangeModel {
    
    @JsonProperty("currentPassword", String)
    public currentPassword: string = null;
    @JsonProperty("newPassword", String)
    public newPassword: string = null;

}