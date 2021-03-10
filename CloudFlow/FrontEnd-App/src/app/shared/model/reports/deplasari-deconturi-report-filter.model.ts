import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "@app/shared/json-mapper";

@JsonObject
export class DeplasariDeconturiReportFilterModel {

    @JsonProperty("denumireInstitutie", String)
    public denumireInstitutie: string = null;

    @JsonProperty("reprezentantId", Number)
    public reprezentantId: number = null;

    @JsonProperty("organismId", Number)
    public organismId: number = null;

    @JsonProperty("denumireComitet", String)
    public denumireComitet: string = null;

    @JsonProperty("oras", String)
    public oras: string = null;

    @JsonProperty("dataPlecareDeLa", JsonDateConverter)
    public dataPlecareDeLa: Date = null;
    
    @JsonProperty("dataPlecarePanaLa", JsonDateConverter)
    public dataPlecarePanaLa: Date = null;
    
    @JsonProperty("dataSosireDeLa", JsonDateConverter)
    public dataSosireDeLa: Date = null;
    
    @JsonProperty("dataSosirePanaLa", JsonDateConverter)
    public dataSosirePanaLa: Date = null;

    @JsonProperty("titularDecont", String)
    public titularDecont: string = null;

}
