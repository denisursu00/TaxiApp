import { JsonProperty, JsonObject } from "json2typescript";
import { SelectItem } from "primeng/api";

@JsonObject
export class SelectItemModel implements SelectItem{

     @JsonProperty("itemValue", String)
     public value: string = null;

     @JsonProperty("itemLabel", String)
     public label: string = null;
}