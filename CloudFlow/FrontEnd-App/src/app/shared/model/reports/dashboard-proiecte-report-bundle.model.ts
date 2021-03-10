import { JsonObject, JsonProperty } from "json2typescript";
import { SelectItemModel } from "../../select-item.model";

@JsonObject
export class DashboardProiecteReportBundle {

    @JsonProperty ("abrevieriProiect", [SelectItemModel])
    public abrevieriProiect: SelectItemModel[] = [];

    @JsonProperty ("tasks", [SelectItemModel])
    public tasks: SelectItemModel[] = [];

    @JsonProperty ("responsibleUsers", [SelectItemModel])
    public responsibleUsers: SelectItemModel[] = [];

    @JsonProperty ("arieDeCuprindereList", [SelectItemModel])
    public arieDeCuprindereList: SelectItemModel[] = [];

    @JsonProperty ("nomenclatorDomeniiBancareId", Number)
    public nomenclatorDomeniiBancareId: number = null;

    @JsonProperty ("nomenclatorImportantaProiecteId", Number)
    public nomenclatorImportantaProiecteId: number = null;

    @JsonProperty ("nomenclatorIncadrariProiecteId", Number)
    public nomenclatorIncadrariProiecteId: number = null;

}