import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { DateConstants } from "@app/shared/constants";
import { ObjectUtils } from "@app/shared/utils";

@JsonObject
export class DeplasariDeconturiRowModel {
//1
    @JsonProperty("luna", Number)
    public luna: number = null;
   
    public get lunaForDisplay(): string {
		return "LABELS." + DateConstants.getMonthCodeByIndex(this.luna);
	}
//2
    @JsonProperty("apelativ", String)
    public apelativ: string = null;
//3
    public get apelativForDisplay(): string {
        return "LABELS." + this.apelativ;
    }
    @JsonProperty("reprezentantArbOioro", String)
    public reprezentantArbOioro: String = null;
//4
    @JsonProperty("functia", String)
    public functia: string = null;
//5
    @JsonProperty("institutie", String)
    public institutie: string = null;
//6
    @JsonProperty("numarDecizie", String)
    public numarDecizie: string = null;
//7
    @JsonProperty("dataDecizie", JsonDateConverter)
    public dataDecizie: Date = null;
//8
    @JsonProperty("denumireOrganism", String)
    public denumireOrganism: string = null;
//9
    @JsonProperty("abreviereOrganism", String)
    public abreviereOrganism: string = null;
//10
    @JsonProperty("comitet", String)
    public comitet: string = null;
//11
    @JsonProperty("numarDeplasariEfectuate", Number)
    public numarDeplasariEfectuate: number = null;
//12
    @JsonProperty("numarDeplasariBugetateRamase", Number)
    public numarDeplasariBugetateRamase: number = null;
//13
    @JsonProperty("eveniment", String)
    public eveniment: string = null;
//14
    @JsonProperty("tara", String)
    public tara: string = null;
//15
    @JsonProperty("oras", String)
    public oras: string = null;
//16
    @JsonProperty("dataPlecare", JsonDateConverter)
    public dataPlecare: Date = null;
//17
    @JsonProperty("dataSosire", JsonDateConverter)
    public dataSosire: Date = null;
//18
    @JsonProperty("dataInceputConferinta", JsonDateConverter)
    public dataInceputConferinta: Date = null;
//19
    @JsonProperty("dataSfarsitConferinta", JsonDateConverter)
    public dataSfarsitConferinta: Date = null;
//20
    @JsonProperty("numarNopti", Number)
    public numarNopti: number = null;
//21
    @JsonProperty("saTransmisMinutaIntalnitii", Boolean)
    public saTransmisMinutaIntalnitii: boolean = null;
    
    public get saTransmisMinutaIntalniriiForDisplay(): string {
    if (this.saTransmisMinutaIntalnitii) {
                return "LABELS.YES";
            }
            return "LABELS.NO";
        }
    
//22
    @JsonProperty("observatii", String)
    public observatii: string = null;
//23
    @JsonProperty("titularDecont", String)
    public titularDecont: string = null;
//24
    @JsonProperty("tipDecont", String)
    public tipDecont: string = null;
//25
    @JsonProperty("numarDecont", String)
    public numarDecont: string = null;
//26
    @JsonProperty("dataDecont", JsonDateConverter)
    public dataDecont: Date = null;
//27
    @JsonProperty("valuta", String)
    public valuta: string = null;

    public get valutaForDisplay(): String {
        if (ObjectUtils.isNullOrUndefined(this.valuta)) {
            return "";
        }
        return "LABELS." + this.valuta;
    }
//28
    @JsonProperty("cursValutar", Number)
    public cursValutar: number = null;
//29
    @JsonProperty("numarDocumentJustificativ", String)
    public numarDocumentJustificativ: String = null;
//30
    @JsonProperty("dataDocumentJustificativ", JsonDateConverter)
    public dataDocumentJustificativ: Date = null;
//31
    @JsonProperty("cazare", Number)
    public cazare: number = null;
//32
    @JsonProperty("biletDeAvion", Number)
    public biletDeAvion: number = null;
//33
    @JsonProperty("asiguareMedicala", Number)
    public asiguareMedicala: number = null;
//34
    @JsonProperty("transferAeroport", Number) 
    public transferAeroport: number = null;
//35
    @JsonProperty("alteCheltuieli", Number)
    public alteCheltuieli: number = null;
//36
    @JsonProperty("totalCheltuieliPerValuta", Number)
    public totalCheltuieliPerValuta: number = null;
//37
    @JsonProperty("totalCheltuieliInRON", Number)
    public totalCheltuieliInRON: number = null;
//38
    @JsonProperty("valuta2", String)
    public valuta2: string = null;
    
        public get valuta2ForDisplay(): String {
            if (ObjectUtils.isNullOrUndefined(this.valuta2)) {
                return "";
            }
            return "LABELS." + this.valuta2;
        }
//39
    @JsonProperty("cursValutar2", Number)
    public cursValutar2: number = null;
//40
    @JsonProperty("numarDocumentJustificativ2", String)
    public numarDocumentJustificativ2: string = null;
//41
    @JsonProperty("dataDocumentJustificativ2", JsonDateConverter)
    public dataDocumentJustificativ2: Date = null;
//42
    @JsonProperty("cazare2", Number)
    public cazare2:number = null;
//43
    @JsonProperty("biletDeAvion2", Number)
    public biletDeAvion2: number = null;
//44
    @JsonProperty("taxiTrenMetrou", Number)
    public taxiTrenMetrou: number = null;
//45
    @JsonProperty("diurnaZilnica", Number)
    public diurnaZilnica: number = null;
//46
    @JsonProperty("numarZile", Number)
    public numarZile: number = null;
//47
    @JsonProperty("totalDiurnaInRON", Number)
    public totalDiurnaInRON: number = null;
//48
    @JsonProperty("comisionUtilizareCard", Number)
    public comisionUtilizareCard: number = null;
//49
    @JsonProperty("alteCheltuieli2", Number)
    public alteCheltuieli2: Number = null;
//50
    @JsonProperty("avans", Number)
    public avans: number = null;
//51
    @JsonProperty("totalCheltuieliPerValuta2", Number)
    public totalCheltuieliPerValuta2: number = null;
//52
    @JsonProperty("totalCheltuieliInRON2", Number)
    public totalCheltuieliInRON2: number = null;
//53
    @JsonProperty("totalDeIncasatDeRestituitInRON", Number)
    public totalDeIncasatDeRestituitInRON: number = null;

 }