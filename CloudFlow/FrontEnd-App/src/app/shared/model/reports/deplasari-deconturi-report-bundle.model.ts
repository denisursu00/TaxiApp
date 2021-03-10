import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "@app/shared/json-mapper";
import { SelectItemModel } from "@app/shared/select-item.model";

@JsonObject
export class DeplasariDeconturiReportBundleModel {

    @JsonProperty ("denumiriInstitutii", [SelectItemModel])
    public denumiriInstitutii: SelectItemModel[] = [];

    @JsonProperty("nomenclatorReprezentatOrgId", Number)
    public nomenclatorReprezentatOrgId: number = null;
    
    @JsonProperty("nomenclatorOrganismeId", Number)
    public nomenclatorOrganismeId: number = null;

    @JsonProperty ("denumiriComiteteList", [SelectItemModel])
    public denumiriComiteteList: SelectItemModel[] = [];
    
    @JsonProperty ("oraseList", [SelectItemModel])
    public oraseList: SelectItemModel[] = [];
    
    @JsonProperty ("titulariDeconturi", [SelectItemModel])
    public titulariDeconturi: SelectItemModel[] = [];

}
