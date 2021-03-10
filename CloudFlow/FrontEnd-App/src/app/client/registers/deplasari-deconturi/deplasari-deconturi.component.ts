import { Component, ViewChild } from "@angular/core";
import { Column, SelectItem } from "primeng/primeng";
import { PageConstants, DateConstants, DateUtils, ObjectUtils, ArrayUtils, TranslateUtils, AppError, MessageDisplayer, ConfirmationUtils, MonthEnum, ComponentPermissionsWrapper, ClientPermissionEnum, ConfirmationWindowFacade } from "@app/shared";
import { DeplasariDeconturiService } from "@app/shared/service/deplasari-deconturi.service";
import { DeplasareDecontViewModel } from "@app/shared/model/deplasari-deconturi/deplasare-decont-view.model";
import { ApelativReprezentantArbDeplasareDecontEnum, CheltuialaArbModel, CheltuialaReprezentantArbModel } from "@app/shared/model/deplasari-deconturi";
import { Table } from "primeng/table";
import { AuthManager } from "@app/shared/auth";

@Component({
	selector: "app-deplasari-deconturi",
	templateUrl: "./deplasari-deconturi.component.html"
})
export class DeplasariDeconturiComponent implements ComponentPermissionsWrapper {

	private static readonly ADD_PERMISSION: string = ClientPermissionEnum.EDIT_DEPLASARI_DECONTURI;
	private static readonly EDIT_PERMISSION: string = ClientPermissionEnum.EDIT_DEPLASARI_DECONTURI;
	private static readonly DELETE_PERMISSION: string = ClientPermissionEnum.EDIT_DEPLASARI_DECONTURI;

	private static readonly COLUMN_MONTH: string = "month";
	private static readonly COLUMN_MONTH_FOR_DISPLAY: string = "monthForDisplay";
	private static readonly COLUMN_NUMAR_INREGISTRARE: string = "numarInregistrare";
	private static readonly COLUMN_NUMAR_INREGISTRARE_FOR_DISPLAY: string = "numarInregistrareForDisplay";
	private static readonly COLUMN_APELATIV: string = "apelativ";
	private static readonly COLUMN_APELATIV_FOR_DISPLAY: string = "apelativForDisplay";
	private static readonly COLUMN_RPREZENTANT_ARB: string = "denumireReprezentantArb";
	private static readonly COLUMN_INSTITUTIA: string = "denumireInstitutie";
	private static readonly COLUMN_FUNCTIA: string = "functie";
	private static readonly COLUMN_NUMAR_DECIZIE: string = "numarDecizie";
	private static readonly COLUMN_DATA_DECIZIE_FOR_DISPLAY: string = "dataDecizieForDisplay";
	private static readonly COLUMN_DATA_DECIZIE: string = "dataDecizie";
	private static readonly COLUMN_DENUMIRE_ORGANISM: string = "denumireOrganism";
	private static readonly COLUMN_ABREVIERE_ORGANISM: string = "abreviereOrganism";
	private static readonly COLUMN_COMITET: string = "denumireComitet";
	private static readonly COLUMN_NUMAR_DEPLASARI_EFECTUATE: string = "numarDeplasariEfectuate";
	private static readonly COLUMN_NUMAR_DEPLASARI_BUGETATE_RAMASE: string = "numarDeplasariBugetateRamase";
	private static readonly COLUMN_EVENIMENT: string = "eveniment";
	private static readonly COLUMN_TARA: string = "tara";
	private static readonly COLUMN_ORAS: string = "oras";
	private static readonly COLUMN_DATA_PLECARE_FOR_DISPLAY: string = "dataPlecareForDisplay";
	private static readonly COLUMN_DATA_PLECARE: string = "dataPlecare";
	private static readonly COLUMN_DATA_SOSIRE_FOR_DISPLAY: string = "dataSosireForDisplay";
	private static readonly COLUMN_DATA_SOSIRE: string = "dataSosire";
	private static readonly COLUMN_DATA_CONFERINTA_INCEPUT_FOR_DISPLAY: string = "dataConferintaInceputForDisplay";
	private static readonly COLUMN_DATA_CONFERINTA_INCEPUT: string = "dataConferintaInceput";
	private static readonly COLUMN_DATA_CONFERINTA_SFARSIT_FOR_DISPLAY: string = "dataConferintaSfarsitForDisplay";
	private static readonly COLUMN_DATA_CONFERINTA_SFARSIT: string = "dataConferintaSfarsit";
	private static readonly COLUMN_NUMAR_NOPTI: string = "numarNopti";
	private static readonly COLUMN_MINUTA_INTALNIRII_TRANSMISA_FOR_DISPLAY: string = "minutaIntalnireTransmisaForDisplay";
	private static readonly COLUMN_MINUTA_INTALNIRII_TRANSMISA: string = "minutaIntalnireTransmisa";
	private static readonly COLUMN_OBSERVATII: string = "observatii";

	private static readonly COLUMN_CHELTUIELI_ARB: string = "cheltuieliArb";
	private static readonly COLUMN_CHELTUIELI_ARB_TITULAR_DECONT: string = "cheltuieliArbTitularDecont";
	private static readonly COLUMN_CHELTUIELI_ARB_TIP_DECONT: string = "cheltuieliArbTipDecont";
	private static readonly COLUMN_CHELTUIELI_ARB_DATA_DECONT: string = "cheltuieliArbDataDecont";
	private static readonly COLUMN_CHELTUIELI_ARB_DATA_DECONT_FOR_DISPLAY: string = "cheltuieliArbDataDecontForDisplay";

	private static readonly COLUMN_TOTAL_CHELTUIELI_ARB_VALUTA_EUR: string = "totalCheltuieliArbValutaEur";
	private static readonly COLUMN_TOTAL_CHELTUIELI_ARB_VALUTA_USD: string = "totalCheltuieliArbValutaUsd";
	private static readonly COLUMN_TOTAL_CHELTUIELI_ARB_VALUTA_RON: string = "totalCheltuieliArbValutaRon";
	private static readonly COLUMN_TOTAL_CHELTUIELI_ARB_RON: string = "totalCheltuieliArbRon";
	

	private static readonly COLUMN_CHELTUIELI_REPREZENTANT_ARB: string = "cheltuieliReprezentantArb";
	private static readonly COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_VALUTA_EUR: string = "totalCheltuieliReprezentantArbValutaEur";
	private static readonly COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_VALUTA_USD: string = "totalCheltuieliReprezentantArbValutaUsd";
	private static readonly COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_VALUTA_RON: string = "totalCheltuieliReprezentantArbValutaRon";
	private static readonly COLUMN_REPREZENTANT_ARB_TOTAL_DIURNA_RON: string = "totalDiurnaRon";
	private static readonly COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_RON: string = "totalCheltuieliReprezentantArbRon";

	private static readonly COLUMN_REPREZENTANT_ARB_AVANS_PRIMIT_RON: string = "avansPrimitRon";
	private static readonly COLUMN_REPREZENTANT_ARB_TOTAL_DE_INCASAT: string = "totalDeIncasat";

	private static readonly COLUMN_ANULAT: string = "anulat";
	private static readonly COLUMN_ANULAT_FOR_DISPLAY: string = "anulatForDisplay";
	private static readonly COLUMN_MOTIV_ANULARE: string = "motivAnulare";
	private static readonly COLUMN_FINALIZAT: string = "finalizat";
	private static readonly COLUMN_FINALIZAT_FOR_DISPLAY: string = "finalizatForDisplay";

	@ViewChild(Table)
	public deplasariDeconturiDataTable: Table;
	
	private translateUtils: TranslateUtils;
	private deplasariDeconturiService: DeplasariDeconturiService;
	private messageDisplayer: MessageDisplayer;
	private authManager: AuthManager;

	public deplasareDecontViewModels: DeplasareDecontViewModel[];

	public selectedDeplasareDecont: DeplasareDecontViewModel;
	public selectedDeplasareDecontId: number;
	public selectedFilterValueMap: any = {};
	
	public tableVisible: boolean = false;
	public columns: Column[];
	public pageOffset: number = 0;
	public pageRows: number = 10;
	public rowsPerPageOptions: number[];
	public dataLoading: boolean;
	public booleanFilterTypes: SelectItem[];
	public dateFormat: String;
	public yearRange: String;
	
	public years: number[];
	public yearItems: SelectItem[];
	public selectedYear: number;
	
	public deplasariDeconturiWindowVisible: boolean = false;
	public deplasariDeconturiWindowMode: "add" | "edit" | "view";
	public deplasariDeconturiWindowDecontId: number;
	
	public monthFilterValues: SelectItem[];
	public apelativFilterValues: SelectItem[];

	public deplasareDecontCanceled: boolean = false;
	
	public addButtonEnabled: boolean = false;
	public editButtonEnabled: boolean = false;
	public viewButtonEnabled: boolean = false;
	public cancelButtonEnabled: boolean = false;
	public deleteButtonEnabled: boolean = false;
	public finalizeButtonEnabled: boolean = false;
	
	public anulareDeplasareIesiriWindowVisible: boolean = false;

	public cheltuieliArbManagerWindowVisible: boolean = false;
	public cheltuieliArbManagerCheltuieliArb: CheltuialaArbModel[];

	public cheltuieliReprezentantArbManagerWindowVisible: boolean = false;
	public cheltuieliReprezentantArbManagerCheltuieliArb: CheltuialaReprezentantArbModel[];

	public loadingVisible: boolean = false;
	public confirmationWindow: ConfirmationWindowFacade;

	public scrollHeight: string;

	public constructor(deplasariDeconturiService: DeplasariDeconturiService, translateUtils: TranslateUtils, messageDisplayer: MessageDisplayer,
			authManager: AuthManager) {
		this.deplasariDeconturiService = deplasariDeconturiService;
		this.translateUtils = translateUtils;
		this.messageDisplayer = messageDisplayer;
		this.authManager = authManager;
		this.confirmationWindow = new ConfirmationWindowFacade();
		this.lockTable();
		this.init();
	}

	private lockTable(): void {
		this.dataLoading = true;
	}

	private unlockTable(): void {
		this.dataLoading = false;
	}

	private init(): void {
		this.tableVisible = true;
		this.monthFilterValues = [];
		this.apelativFilterValues = [];
		this.booleanFilterTypes = [];
		this.years = [];
		this.yearItems = [];
		this.rowsPerPageOptions = PageConstants.DEAFAULT_ROW_PER_PAGE_OPTIONS;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 400) + "px";

		this.prepareColumns();
		this.prepareFilterValues();
		this.changeButtonsPerspective();
	}

	private prepareColumns(): void {
		this.columns = [];
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("MONTH"), DeplasariDeconturiComponent.COLUMN_MONTH_FOR_DISPLAY, "NOMENCLATOR", "in", true, DeplasariDeconturiComponent.COLUMN_MONTH));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_APELATIV"), DeplasariDeconturiComponent.COLUMN_APELATIV_FOR_DISPLAY, "NOMENCLATOR", "in", true, DeplasariDeconturiComponent.COLUMN_APELATIV));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_REPREZENTANT_ARB"), DeplasariDeconturiComponent.COLUMN_RPREZENTANT_ARB, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_RPREZENTANT_ARB));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_INSTITUTIE"), DeplasariDeconturiComponent.COLUMN_INSTITUTIA, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_INSTITUTIA));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_FUNCTIA"), DeplasariDeconturiComponent.COLUMN_FUNCTIA, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_FUNCTIA));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_NUMAR_DECIZIE"), DeplasariDeconturiComponent.COLUMN_NUMAR_DECIZIE, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_NUMAR_DECIZIE));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_DATA_DECIZIE"), DeplasariDeconturiComponent.COLUMN_DATA_DECIZIE_FOR_DISPLAY, "DATE", "equals", true, DeplasariDeconturiComponent.COLUMN_DATA_DECIZIE));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_DENUMIRE_ORGANISM"), DeplasariDeconturiComponent.COLUMN_DENUMIRE_ORGANISM, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_DENUMIRE_ORGANISM));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_ABREVIERE_ORGANISM"), DeplasariDeconturiComponent.COLUMN_ABREVIERE_ORGANISM, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_ABREVIERE_ORGANISM));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_COMITET"), DeplasariDeconturiComponent.COLUMN_COMITET, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_COMITET));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_NUMAR_DEPLASARI_EFECTUATE"), DeplasariDeconturiComponent.COLUMN_NUMAR_DEPLASARI_EFECTUATE, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_NUMAR_DEPLASARI_EFECTUATE));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_NUMAR_DEPLASARI_BUGETATE_RAMASE"), DeplasariDeconturiComponent.COLUMN_NUMAR_DEPLASARI_BUGETATE_RAMASE, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_NUMAR_DEPLASARI_BUGETATE_RAMASE));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_EVENIMENT"), DeplasariDeconturiComponent.COLUMN_EVENIMENT, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_EVENIMENT));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_TARA"), DeplasariDeconturiComponent.COLUMN_TARA, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_TARA));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_ORAS"), DeplasariDeconturiComponent.COLUMN_ORAS, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_ORAS));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_DATA_PLECARE"), DeplasariDeconturiComponent.COLUMN_DATA_PLECARE_FOR_DISPLAY, "DATE", "equals", true, DeplasariDeconturiComponent.COLUMN_DATA_PLECARE));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_DATA_SOSIRE"), DeplasariDeconturiComponent.COLUMN_DATA_SOSIRE_FOR_DISPLAY, "DATE", "equals", true, DeplasariDeconturiComponent.COLUMN_DATA_SOSIRE));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_DATA_CONFERINTA_INCEPUT"), DeplasariDeconturiComponent.COLUMN_DATA_CONFERINTA_INCEPUT_FOR_DISPLAY, "DATE", "equals", true, DeplasariDeconturiComponent.COLUMN_DATA_CONFERINTA_INCEPUT));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_DATA_CONFERINTA_SFARSIT"), DeplasariDeconturiComponent.COLUMN_DATA_CONFERINTA_SFARSIT_FOR_DISPLAY, "DATE", "equals", true, DeplasariDeconturiComponent.COLUMN_DATA_CONFERINTA_SFARSIT));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_NUMAR_NOPTI"), DeplasariDeconturiComponent.COLUMN_NUMAR_NOPTI, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_NUMAR_NOPTI));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_MINUTA_INTALNIRE_TRANSMISA"), DeplasariDeconturiComponent.COLUMN_MINUTA_INTALNIRII_TRANSMISA_FOR_DISPLAY, "NOMENCLATOR", "in", true, DeplasariDeconturiComponent.COLUMN_MINUTA_INTALNIRII_TRANSMISA));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_OBSERVATII"), DeplasariDeconturiComponent.COLUMN_OBSERVATII, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_OBSERVATII));

		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_ARB_TITULAR_DECONT"), DeplasariDeconturiComponent.COLUMN_CHELTUIELI_ARB_TITULAR_DECONT, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_CHELTUIELI_ARB_TITULAR_DECONT));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_ARB_TIP_DECONT"), DeplasariDeconturiComponent.COLUMN_CHELTUIELI_ARB_TIP_DECONT, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_CHELTUIELI_ARB_TIP_DECONT));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_NUMAR_INREGISTRARE"), DeplasariDeconturiComponent.COLUMN_NUMAR_INREGISTRARE_FOR_DISPLAY, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_NUMAR_INREGISTRARE_FOR_DISPLAY));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_ARB_DATA_DECONT"), DeplasariDeconturiComponent.COLUMN_CHELTUIELI_ARB_DATA_DECONT_FOR_DISPLAY, "DATE", "equals", true, DeplasariDeconturiComponent.COLUMN_CHELTUIELI_ARB_DATA_DECONT));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_ARB"), DeplasariDeconturiComponent.COLUMN_CHELTUIELI_ARB));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_ARB_TOTAL_VALUTA_EUR"), DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_ARB_VALUTA_EUR, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_ARB_VALUTA_EUR));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_ARB_TOTAL_VALUTA_USD"), DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_ARB_VALUTA_USD, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_ARB_VALUTA_USD));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_ARB_TOTAL_VALUTA_RON"), DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_ARB_VALUTA_RON, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_ARB_VALUTA_RON));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_ARB_TOTAL_RON"), DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_ARB_RON, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_ARB_RON));

		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_REPREZENTANT_ARB"), DeplasariDeconturiComponent.COLUMN_CHELTUIELI_REPREZENTANT_ARB));
		// tslint:disable-next-line:max-line-length
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_REPREZENTANT_ARB_TOTAL_VALUTA_EUR"), DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_VALUTA_EUR, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_VALUTA_EUR));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_REPREZENTANT_ARB_TOTAL_VALUTA_USD"), DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_VALUTA_USD, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_VALUTA_USD));
		// tslint:disable-next-line:max-line-length
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_REPREZENTANT_ARB_TOTAL_VALUTA_RON"), DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_VALUTA_RON, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_VALUTA_RON));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("REPREZENTANT_ARB_TOTAL_DIURNA"), DeplasariDeconturiComponent.COLUMN_REPREZENTANT_ARB_TOTAL_DIURNA_RON, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_REPREZENTANT_ARB_TOTAL_DIURNA_RON));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_REPREZENTANT_ARB_TOTAL_RON"), DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_RON, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_RON));
		
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_REPREZENTANT_ARB_AVANS_PRIMIT"), DeplasariDeconturiComponent.COLUMN_REPREZENTANT_ARB_AVANS_PRIMIT_RON, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_REPREZENTANT_ARB_AVANS_PRIMIT_RON));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("CHELTUIELI_REPREZENTANT_ARB_TOTAL_DE_INCASAT"), DeplasariDeconturiComponent.COLUMN_REPREZENTANT_ARB_TOTAL_DE_INCASAT, "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_REPREZENTANT_ARB_TOTAL_DE_INCASAT));

		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_ANULAT"), DeplasariDeconturiComponent.COLUMN_ANULAT_FOR_DISPLAY, "NOMENCLATOR", "in", true, DeplasariDeconturiComponent.COLUMN_ANULAT));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_MOTIV_ANULARE"), DeplasariDeconturiComponent.COLUMN_MOTIV_ANULARE,  "TEXT", "contains", true, DeplasariDeconturiComponent.COLUMN_MOTIV_ANULARE));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("DEPLASARI_DECONTURI_FINALIZAT"), DeplasariDeconturiComponent.COLUMN_FINALIZAT_FOR_DISPLAY, "NOMENCLATOR", "in", true, DeplasariDeconturiComponent.COLUMN_FINALIZAT));
	}

	private buildColumn(header: string, field: string, filterType?: string, filterMatchMode?: string, filter?: boolean, filterField?: string): Column {
		let column: Column = new Column();
		column.header = header;
		column.field = field;
		if (ObjectUtils.isNotNullOrUndefined(filterField)) {
			column.filterField = filterField;
		} else {
			column.filterField = field;
		}
		this.selectedFilterValueMap[field] = null;
		column.filterType = filterType;
		column.filterMatchMode = filterMatchMode;
		column.filter = filter;
		return column;
	}
	
	private prepareFilterValues(): void {
		this.prepareYearFilterValues();
		this.prepareMonthFilterValues();
		this.prepareApelativFilterValues();
		this.prepareBooleanFilterTypes();
	}

	private prepareYearFilterValues(): void {
		this.getYearsOfExistingDeplasariDeconturi();
	}

	private getYearsOfExistingDeplasariDeconturi(): void {
		this.deplasariDeconturiService.getYearsOfExistingDeplasariDeconturi({
			onSuccess: (years: number[]): void => {
				this.years = years;
				this.prepareYearItems();
				let currentYear: number = new Date().getFullYear();
				this.selectedYear = currentYear;
				this.prepareAllDeplasariDeconturiViewModelsByYear(currentYear);
			},
			onFailure: (appError: AppError): void => {
				this.unlockTable();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareAllDeplasariDeconturiViewModelsByYear(year: number): void {
		this.getAllDeplasariDeconturiViewModelsByYear(year);
	}

	private getAllDeplasariDeconturiViewModelsByYear(year: number): void {
		this.deplasariDeconturiService.getAllDeplasariDeconturiViewModelsByYear(year, {
			onSuccess: (deplasareDecontViewModels: DeplasareDecontViewModel[]): void => {
				this.deplasareDecontViewModels = deplasareDecontViewModels;
				this.unlockTable();
			},
			onFailure: (appError: AppError): void => {
				this.unlockTable();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareYearItems(): void {
		this.yearItems = [];
		this.years.forEach((year: number) => {
			let yearItem: SelectItem = { label: year.toString(), value: year };
			this.yearItems.push(yearItem);
		});
		
		let currentYearItem: SelectItem = { label: new Date().getFullYear().toString(), value: new Date().getFullYear() };
		let yearItemsWithCurrentYearItem: SelectItem[] = this.yearItems.filter(yearItem => yearItem.value === currentYearItem.value);
		if (ArrayUtils.isEmpty(yearItemsWithCurrentYearItem)) {
			this.yearItems.push(currentYearItem);
		}
	}

	private prepareMonthFilterValues(): void {
		this.monthFilterValues = [
			{label: this.translateUtils.translateLabel(MonthEnum.JANUARY), value: MonthEnum.JANUARY},
			{label: this.translateUtils.translateLabel(MonthEnum.FEBRUARY), value: MonthEnum.FEBRUARY},
			{label: this.translateUtils.translateLabel(MonthEnum.MARCH), value: MonthEnum.MARCH},
			{label: this.translateUtils.translateLabel(MonthEnum.APRIL), value: MonthEnum.APRIL},
			{label: this.translateUtils.translateLabel(MonthEnum.MAY), value: MonthEnum.MAY},
			{label: this.translateUtils.translateLabel(MonthEnum.JUNE), value: MonthEnum.JUNE},
			{label: this.translateUtils.translateLabel(MonthEnum.JULY), value: MonthEnum.JULY},
			{label: this.translateUtils.translateLabel(MonthEnum.AUGUST), value: MonthEnum.AUGUST},
			{label: this.translateUtils.translateLabel(MonthEnum.SEPTEMBER), value: MonthEnum.SEPTEMBER},
			{label: this.translateUtils.translateLabel(MonthEnum.OCTOBER), value: MonthEnum.OCTOBER},
			{label: this.translateUtils.translateLabel(MonthEnum.NOVEMBER), value: MonthEnum.NOVEMBER},
			{label: this.translateUtils.translateLabel(MonthEnum.DECEMBER), value: MonthEnum.DECEMBER}
		];
	}

	private prepareApelativFilterValues(): void {
		this.apelativFilterValues = [
			{label: this.translateUtils.translateLabel(ApelativReprezentantArbDeplasareDecontEnum.DOMNUL), value: ApelativReprezentantArbDeplasareDecontEnum.DOMNUL},
			{label: this.translateUtils.translateLabel(ApelativReprezentantArbDeplasareDecontEnum.DOAMNA), value: ApelativReprezentantArbDeplasareDecontEnum.DOAMNA},
			{label: this.translateUtils.translateLabel(ApelativReprezentantArbDeplasareDecontEnum.DOMNISOARA), value: ApelativReprezentantArbDeplasareDecontEnum.DOMNISOARA},
		];
	}

	private prepareBooleanFilterTypes(): void {
		this.booleanFilterTypes = [
			{label: this.translateUtils.translateLabel("YES"), value: true},
			{label: this.translateUtils.translateLabel("NO"), value: false}
		];
	}

	public isColumnTranslatable(column: Column) {
		return column.field === DeplasariDeconturiComponent.COLUMN_APELATIV_FOR_DISPLAY ||
			column.field === DeplasariDeconturiComponent.COLUMN_MONTH_FOR_DISPLAY ||
			column.field === DeplasariDeconturiComponent.COLUMN_MINUTA_INTALNIRII_TRANSMISA_FOR_DISPLAY ||
			column.field === DeplasariDeconturiComponent.COLUMN_ANULAT_FOR_DISPLAY ||
			column.field === DeplasariDeconturiComponent.COLUMN_FINALIZAT_FOR_DISPLAY;
	}

	public isColumnDecimalNumber(column: Column) {
		return 	column.field === DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_ARB_VALUTA_EUR ||
			column.field === DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_ARB_VALUTA_USD ||
			column.field === DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_ARB_VALUTA_RON ||
			column.field === DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_ARB_RON ||
			column.field === DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_VALUTA_EUR ||
			column.field === DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_VALUTA_USD ||
			column.field === DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_VALUTA_RON ||
			column.field === DeplasariDeconturiComponent.COLUMN_REPREZENTANT_ARB_TOTAL_DIURNA_RON ||
			column.field === DeplasariDeconturiComponent.COLUMN_TOTAL_CHELTUIELI_REPREZENTANT_ARB_RON ||
			column.field === DeplasariDeconturiComponent.COLUMN_REPREZENTANT_ARB_AVANS_PRIMIT_RON ||
			column.field === DeplasariDeconturiComponent.COLUMN_REPREZENTANT_ARB_TOTAL_DE_INCASAT;
	}

	public isColumnCheltuieliArb(column: Column) {
		return column.field === DeplasariDeconturiComponent.COLUMN_CHELTUIELI_ARB;
	}

	public isColumnCheltuieliReprezentantArb(column: Column) {
		return column.field === DeplasariDeconturiComponent.COLUMN_CHELTUIELI_REPREZENTANT_ARB;
	}

	public getNumberOfTotalRecords(): number {
		return ArrayUtils.isNotEmpty(this.deplasareDecontViewModels) ? this.deplasareDecontViewModels.length : 0;
	}

	public onAdd(): void {
		this.deplasariDeconturiWindowMode = "add";
		this.deplasariDeconturiWindowVisible = true;
	}

	public onEdit(): void {
		this.deplasariDeconturiWindowMode = "edit";
		this.deplasariDeconturiWindowDecontId = this.selectedDeplasareDecont.id;
		this.deplasariDeconturiWindowVisible = true;
	}

	public onCancel(): void {
		this.anulareDeplasareIesiriWindowVisible = true;
	}

	public onRefresh(): void {
		this.refresh();
	}
	
	public onDataSelected(event: any): void {
		this.selectedDeplasareDecontId = this.selectedDeplasareDecont.id;
		this.isDeplasareDecontCanceled();
	}

	private isDeplasareDecontCanceled(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.selectedDeplasareDecontId)) {
			this.deplasariDeconturiService.isDeplasareDecontCanceled(this.selectedDeplasareDecontId, {
				onSuccess: (isCanceled: boolean): void => {
					this.deplasareDecontCanceled = isCanceled;
					this.changeButtonsPerspective();
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
				}
			});
		}
	}

	public onDataUnselected(event: any): void {
		this.deplasareDecontCanceled = false;
		this.selectedDeplasareDecont = null;
		this.selectedDeplasareDecontId = null;
		this.changeButtonsPerspective();
	}

	public onDeplasariDeconturiWindowClosed(): void {
		this.deplasariDeconturiWindowVisible = false;
		this.refresh();
	}

	public onYearValueChanged(event: any): void {
		this.getAllDeplasariDeconturiViewModelsByYear(this.selectedYear);
	}

	public refresh(): void {
		this.lockTable();
		this.resetTableFilters();
		this.deplasareDecontViewModels = [];
		this.selectedDeplasareDecont = null;
		this.selectedDeplasareDecontId = null;
		this.pageRows = 10;
		this.pageOffset = 0;
		let currentYear: number = new Date().getFullYear();
		this.selectedYear = currentYear;
		this.getAllDeplasariDeconturiViewModelsByYear(currentYear);
		this.changeButtonsPerspective();
		this.prepareYearItems();		
	}

	private resetTableFilters(): void {
		this.apelativFilterValues = [];
		this.booleanFilterTypes = null;
		this.monthFilterValues = [];
		this.deplasariDeconturiDataTable.reset();
			
		for (let key in this.selectedFilterValueMap ) {
			this.selectedFilterValueMap[key] = null;
		}

		this.prepareFilterValues();
	}

	public changeButtonsPerspective(): void {
		this.disableAllButtons();
		this.addButtonEnabled = this.isAddPermissionAllowed();
		if (ObjectUtils.isNotNullOrUndefined(this.selectedDeplasareDecontId)) {
			this.viewButtonEnabled = true;
			if (this.isEditPermissionAllowed()) {
				if (this.selectedDeplasareDecont.finalizat) {
					this.cancelButtonEnabled = !this.deplasareDecontCanceled;
				} else if (!this.deplasareDecontCanceled) {
					this.editButtonEnabled = true;
					this.finalizeButtonEnabled = true;
				}
			}
			if (this.isDeletePermissionAllowed()) {
				this.deleteButtonEnabled = (!this.selectedDeplasareDecont.finalizat && !this.deplasareDecontCanceled);
			}
		}
	}

	private disableAllButtons(): void {
		this.addButtonEnabled = false;
		this.editButtonEnabled = false;
		this.viewButtonEnabled = false;
		this.cancelButtonEnabled = false;
		this.finalizeButtonEnabled = false;
	}

	public onAnulareDeplasareWindowClosed(): void {
		this.anulareDeplasareIesiriWindowVisible = false;
		this.refresh();
	}

	public onRemove(): void {
		this.confirmationWindow.confirm({			
			approve: (): void => {
				this.confirmationWindow.hide();
				this.loadingVisible = true;
				this.deplasariDeconturiService.removeDeplasareDecont(this.selectedDeplasareDecont.id, {
					onSuccess: (): void => {
						this.loadingVisible = false;
						this.messageDisplayer.displaySuccess("DEPLASARE_DECONT_DELETED_SUCCESSFULLY");
						this.refresh();
					},
					onFailure: (appError: AppError) => {
						this.loadingVisible = false;
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			reject: (): void => {
				this.confirmationWindow.hide();
			}
		}, "CONFIRM_DELETE_DECONT");
	}

	public onFinalize(): void {
		this.confirmationWindow.confirm({
			approve: (): void => {
				this.confirmationWindow.hide();
				this.loadingVisible = true;
				this.deplasariDeconturiService.finalizeDeplasareDecont(this.selectedDeplasareDecont.id, {
					onSuccess: (): void => {
						this.loadingVisible = false;
						this.messageDisplayer.displaySuccess("DEPLASARE_DECONT_FINALIZED_SUCCESSFULLY");
						this.refresh();
					},
					onFailure: (appError: AppError) => {
						this.loadingVisible = false;
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			reject: (): void => {
				this.confirmationWindow.hide();
			}
		}, "CONFIRM_FINALIZE_DECONT");
	}

	public onView(): void {
		this.deplasariDeconturiWindowMode = "view";
		this.deplasariDeconturiWindowDecontId = this.selectedDeplasareDecont.id;
		this.deplasariDeconturiWindowVisible = true;
	}

	public onViewCheltuieliArb(cheltuieliArb: CheltuialaArbModel[]): void {
		this.cheltuieliArbManagerCheltuieliArb = cheltuieliArb;
		this.cheltuieliArbManagerWindowVisible = true;
	}

	public onCheltuieliArbManagerWindowClosed(): void {
		this.cheltuieliArbManagerCheltuieliArb = [];
		this.cheltuieliArbManagerWindowVisible = false;
	}

	public onViewCheltuieliReprezentantArb(cheltuieliReprezentantArb: CheltuialaReprezentantArbModel[]): void {
		this.cheltuieliReprezentantArbManagerCheltuieliArb = cheltuieliReprezentantArb;
		this.cheltuieliReprezentantArbManagerWindowVisible = true;
	}

	public onCheltuieliReprezentantArbManagerWindowClosed(): void {
		this.cheltuieliReprezentantArbManagerCheltuieliArb = [];
		this.cheltuieliReprezentantArbManagerWindowVisible = false;
	}

	isAddPermissionAllowed(): boolean {
		return this.authManager.hasPermission(DeplasariDeconturiComponent.ADD_PERMISSION);
	}

	isEditPermissionAllowed(): boolean {
		return this.authManager.hasPermission(DeplasariDeconturiComponent.EDIT_PERMISSION);
	}

	isDeletePermissionAllowed(): boolean {
		return this.authManager.hasPermission(DeplasariDeconturiComponent.DELETE_PERMISSION);
	}
}