import { JsonProperty, JsonObject } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DateUtils } from "../../utils";

@JsonObject
export class PrezentaSedinteCdPvgInvitatiExterniReportRowModel {

    @JsonProperty("tipSedinta", String)
    public tipSedinta: string = null;

    @JsonProperty("dataSedinta", JsonDateConverter)
    public dataSedinta: Date = null;

    @JsonProperty("institutieInvitat", String)
    public institutieInvitat: string = null;

    @JsonProperty("invitatAcreditat", String)
    public invitatAcreditat: string = null;

    @JsonProperty("invitatInlocuitorNume", String)
    public invitatInlocuitorNume: string = null;

    @JsonProperty("invitatInlocuitorPrenume", String)
    public invitatInlocuitorPrenume: string = null;

    public get dataSedintaForDisplay(): string {
        return DateUtils.formatForDisplay(this.dataSedinta);
    }

    public get invitatInlocuitorForDisplay(): string {
        if (this.invitatInlocuitorNume != null && this.invitatInlocuitorPrenume != null) {
            return this.invitatInlocuitorNume + " " + this.invitatInlocuitorPrenume;
        } else {
            if (this.invitatInlocuitorNume != null) {
                return this.invitatInlocuitorNume ;
            }
            if (this.invitatInlocuitorPrenume != null){
                this.invitatInlocuitorPrenume;
            }
        }
        return "";
    }
}