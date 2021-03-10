import { JsonDateConverter } from "@app/shared/json-mapper";
import { JsonObject, JsonProperty } from "json2typescript";

@JsonObject
export class NivelReprezentareComisiiReportRowModel {

    @JsonProperty("institutia", String)
    public institutia: string = null;

    @JsonProperty("nrPresedintiComisie", Number)
    public nrPresedintiComisie: number = null;

    @JsonProperty("nrVicepresedintiComisie", Number)
    public nrVicepresedintiComisie: number = null;

    @JsonProperty("nrReprezOrganismeInterne", Number)
    public nrReprezOrganismeInterne: number = null;

    @JsonProperty("nrReprezOrganismeInternationale", Number)
    public nrReprezOrganismeInternationale: number = null;

    @JsonProperty("totalReprezentare", Number)
    public totalReprezentare: number = null;

    @JsonProperty("procentReprezentare", Number)
    public procentReprezentare: number = null;

    @JsonProperty("rankClasamentConducatori", Number)
    public rankClasamentConducatori: number = null;

}