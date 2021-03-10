import { JsonObject, JsonProperty } from "json2typescript";
import { JsonDateConverter } from "../../json-mapper";
import { ProjectStatus, ArieDeCuprindere } from "../project";
import { getLocaleNumberSymbol } from "@angular/common";
import { EvaluareGradDeRealizareProiectEnum } from "./dashboard-proiecte-evaluare-grad-realizare-enum";
import { DateUtils, TranslateUtils } from "@app/shared/utils";


JsonObject
export class DashboardProiecteReportRowModel {
 
    @JsonProperty("arieActivitateBancara", String)
    public arieActivitateBancara: string = null;
    
    @JsonProperty("importantaActuala", String)
    public importantaActuala: string = null;
    
    @JsonProperty("importantaActualaCulare", String)
    public importantaActualaCuloare: string = null;

    public get importantaActualaCuloareStyle(): any {
        return { "background-color": this.importantaActualaCuloare };
    }
    
    @JsonProperty("denumireProiect", String)
    public denumireProiect: string = null;
    
    @JsonProperty("abreviereProiect", String)
    public abreviereProiect: string = null;
    
	@JsonProperty("dataInitierii", JsonDateConverter)
    public dataInitierii: Date = null;

    public get dataInitieriiForDisplay(): string {
        return DateUtils.formatForDisplay(this.dataInitierii);
    }

	@JsonProperty("termendeFinalizare", JsonDateConverter)
    public termendeFinalizare: Date = null;

    public get termendeFinalizareForDisplay(): string {
        return DateUtils.formatForDisplay(this.termendeFinalizare);
    }
    
	@JsonProperty("gradRealizareEstimat", Number)
    public gradRealizareEstimat: number = null;
    
    @JsonProperty("incadrareProiect", String)
    public incadrareProiect: string = null;
    
	@JsonProperty("gradRealizareProiect", Number)
    public gradRealizareProiect: number = null;
    
	@JsonProperty("nrZilePerioadaScursa", Number)
    public nrZilePerioadaScursa: number = null;
    
	@JsonProperty("nrZilePerioadaRamasa", Number)
    public nrZilePerioadaRamasa: number = null;
    
	@JsonProperty("nrZileProiect", Number)
    public nrZileProiect: number = null;
    
	@JsonProperty("parametru1", Number)
    public parametru1: number = null;

    public get parametru1ForDisplay(): string {
        if (this.parametru1 == -1) {
            return "LABELS.PROJECT_PARAMETER_VALUE_PROIECTUL_INCA_NU_A_INCEPUT";
        }
        return this.parametru1.toString();
    }
    
	@JsonProperty("parametru2", Number)
    public parametru2: number = null;

    public get parametru2ForDisplay(): string {
        if (this.parametru1 == -1) {
            return "LABELS.PROJECT_PARAMETER_VALUE_PROIECTUL_INCA_NU_A_INCEPUT";
        }
        return this.parametru2.toString();
    }
         
    @JsonProperty("evaluareGradRealizare", String)
    public evaluareGradRealizare: string = null;

    public get evaluareGradRealizareForDisplay(): string {
      
        if (this.evaluareGradRealizare === EvaluareGradDeRealizareProiectEnum.FOARTE_BINE) {
            return "LABELS.PROJECT_GRAD_REALIZARE_FOARTE_BINE";
        } else if (this.evaluareGradRealizare === EvaluareGradDeRealizareProiectEnum.BINE) {
            return "LABELS.PROJECT_GRAD_REALIZARE_SATISFACTOR";
        } else  if (this.evaluareGradRealizare === EvaluareGradDeRealizareProiectEnum.SATISFACATOR) {
            return "LABELS.PROJECT_GRAD_REALIZARE_BINE";
        } else if (this.evaluareGradRealizare === EvaluareGradDeRealizareProiectEnum.NESATISFACATOR) {
            return "LABELS.PROJECT_GRAD_REALIZARE_NESATISFACTOR";
        } else {
            return "LABELS.NONE";
        } 
    }
    
    @JsonProperty("actiuneDeUrmat", String)
    public actiuneDeUrmat: string = null;
    
	@JsonProperty("deadlineActiuneDeUrmat", JsonDateConverter)
    public deadlineActiuneDeUrmat: Date = null;

    public get deadlineActiuneDeUrmatForDisplay(): string {
        return DateUtils.formatForDisplay(this.deadlineActiuneDeUrmat);
    }
    
	@JsonProperty("prioritate", Number)
    public prioritate: number = null;
    
    @JsonProperty("descriere", String)
    public descriere: string = null;
    
    @JsonProperty("responsabilproiect", String)
    public responsabilproiect: string = null;
    
    @JsonProperty("delegatResponsabilArb", String)
    public delegatResponsabilArb: string = null;
    
    @JsonProperty("stadiu", String)
    public stadiu: string = null;

    public get stadiuForDisplay(): string {
        if (this.stadiu === ProjectStatus.INITIATED) {
            return "LABELS.PROJECT_STATUS_INITIATED";
        } else if (this.stadiu === ProjectStatus.CLOSED) {
            return "LABELS.PROJECT_STATUS_CLOSED";
        } else return "LABELS.PROJECT_STATUS_NONE";
    }
    
    @JsonProperty("impact", String)
    public impact: string = null;
    
	@JsonProperty("proiectInitiatDeARB", Boolean)
    public isProiectInitiatDeARB: boolean = null;

    public get proiectInitiatDeARBDisplay(): string {
        if (this.isProiectInitiatDeARB == null) {
            return "LABELS.NONE";
        }
		if (this.isProiectInitiatDeARB) {
			return "LABELS.YES";
		} else {
            return "LABELS.NO";
        }
	}
    
    @JsonProperty("arieDeCuprindere", String)
    public arieDeCuprindere: string = null;
    public get arieDeCuprindereForDisplay(): String {
        if (this.arieDeCuprindere === ArieDeCuprindere.INTERN) {
            return "LABELS.REPORT_DASHBOARD_PROIECTE_ARIE_DE_CUPRINDERE_INTERN";
        } else if (this.arieDeCuprindere === ArieDeCuprindere.INTERNATIONAL) {
            return "LABELS.REPORT_DASHBOARD_PROIECTE_ARIE_DE_CUPRINDERE_INTERNATIONAL";
        } else {
            return "LABELS.NONE";
        } 

    }
    
    @JsonProperty("proiectInitiatDeAltaEntitate", String)
    public proiectInitiatDeAltaEntitate: string = null;
    
}
