import { JsonObject, JsonProperty } from "json2typescript";
import { SelectItemModel } from "@app/shared/select-item.model";

@JsonObject
export class RaspunsuriBanciReportBundleModel {

    @JsonProperty ("denumiriBanci", [SelectItemModel])
    public denumiriBanci: SelectItemModel[] = [];
    
    @JsonProperty ("proiecte", [SelectItemModel])
    public proiecte: SelectItemModel[] = [];
    
}