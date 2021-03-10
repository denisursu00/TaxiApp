import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DateConstants, DateUtils, FormUtils, TranslateUtils, DeplasariDeconturiService, NomenclatorService, NomenclatorConstants, NomenclatorValueModel, GetNomenclatorValuesRequestModel, NomenclatorSimpleFilter, NomenclatorFilter, CheltuieliArbSiReprezentantArbReportModel, CheltuieliArbSiReprezentantArbReportFilterModel, CheltuieliArbSiReprezentantArbReportDateFilterModel, CheltuieliArbSiRePrezentantArbRowModel } from "./../../../shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from "./../../../shared";
import { ReportService, MessageDisplayer } from "./../../../shared";
import { SelectItem, Column } from "primeng/primeng";
import { ValutaForCheltuieliReprezentantArbEnum, CheltuialaArbModel } from "../../../shared/model/deplasari-deconturi";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-cheltuieli-arb-si-reprezentant-arb-report",
	templateUrl: "./cheltuieli-arb-si-reprezentant-arb-report.component.html"
})
export class CheltuieliArbSiReprezentantArbReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new CheltuieliArbSiReprezentantArbReportModel();

	
	@ViewChild(Table)
	public dataTable: Table; 
	public columns: Column[] = []; 
	public cheltuieliReprezentantArbColumns: Column[] = [];
	
	private translateUtils: TranslateUtils;
	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;
	private reportService: ReportService;
	private deplasariDeconturiService: DeplasariDeconturiService;
	private nomenclatorService: NomenclatorService;

	private organismInternId: number;
	private organismInternationalId: number;

	public reportVisible: boolean;
	public loading: boolean;
	public showComitet: boolean = false;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public titularSelectItems: SelectItem[];
	public organismSelectItems: SelectItem[];
	public comitetSelectItems: SelectItem[];
	public valutaSelectItems: SelectItem[];
	public numarDecizieSelectItems: SelectItem[];
	public dateFormat: string;
	public yearRange: string;

	public report: CheltuieliArbSiReprezentantArbReportModel;
	public valuta: string;

	public scrollHeight: string;

	public constructor(translateUtils: TranslateUtils, reportsService: ReportService, deplasariDeconturiService: DeplasariDeconturiService, formBuilder: FormBuilder,
			messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils,  nomenclatorService: NomenclatorService) {
		this.translateUtils = translateUtils;
		this.reportService = reportsService;
		this.deplasariDeconturiService = deplasariDeconturiService;
		this.nomenclatorService = nomenclatorService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;		
		this.init();
	}

	private prepareTitularSelectItems() : void {
		this.titularSelectItems = [];
		this.deplasariDeconturiService.getTitulariOfExistingDeplasariDeconturi( {
			onSuccess: (titulari: string[]): void => {
				let selectItems: SelectItem[] = [];
				titulari.forEach((titular: string) => {
					let selectItem: SelectItem = {
						value: titular,
						label: titular
					};
					selectItems.push(selectItem);
				});
				this.titularSelectItems = selectItems;

				ListItemUtils.sortByLabel(this.titularSelectItems);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});	
	}

	private prepareValutaSelectItems(): void {
		this.valutaSelectItems = [
			{label: this.translateUtils.translateLabel(ValutaForCheltuieliReprezentantArbEnum.RON), value: ValutaForCheltuieliReprezentantArbEnum.RON},
			{label: this.translateUtils.translateLabel(ValutaForCheltuieliReprezentantArbEnum.EUR), value: ValutaForCheltuieliReprezentantArbEnum.EUR},
			{label: this.translateUtils.translateLabel(ValutaForCheltuieliReprezentantArbEnum.USD), value: ValutaForCheltuieliReprezentantArbEnum.USD}
		];

		ListItemUtils.sortByLabel(this.valutaSelectItems);
	}

	private prepareTipOrganism(): void {
		this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_TIP_ORGANISM, {
            onSuccess: (tipOrganisme: NomenclatorValueModel[]): void => { 
                tipOrganisme.forEach(tipOrganism => {
					let tip: string = tipOrganism[NomenclatorConstants.TIP_ORGANISM_ATTR_KEY_TIP];
					if (tip === NomenclatorConstants.TIP_ORGANISM_TIP_ORGANISM_INTERN) {
						this.organismInternId = tipOrganism["id"];
					}
					if (tip === NomenclatorConstants.TIP_ORGANISM_TIP_ORGANISM_INTERNATIONAL) {
						this.organismInternationalId = tipOrganism["id"];
					}
                });                
            },
            onFailure: (appError: AppError) => {
                this.messageDisplayer.displayAppError(appError);
            }
		});	
	}

	private prepareOrganismSelectItems(): void {
		this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_ORGANISME, {
            onSuccess: (organisme: NomenclatorValueModel[]): void => { 
				let selectItems: SelectItem[] = [];
                organisme.forEach(organism => {
					let selectItem: SelectItem = {
						value: organism,
						label: organism[NomenclatorConstants.ORGANISME_ATTR_KEY_DENUMIRE]
					};
					selectItems.push(selectItem);
				});   
				this.organismSelectItems = selectItems;   

				ListItemUtils.sortByLabel(this.organismSelectItems);       
            },
            onFailure: (appError: AppError) => {
                this.messageDisplayer.displayAppError(appError);
            }
		});	
	}

	private prepareComitetSelectItems(): void {
		let filter: GetNomenclatorValuesRequestModel = this.buildGetNomenclatorValuesRequestModel();
		this.nomenclatorService.getNomenclatorValues(filter, {
            onSuccess: (comitete: NomenclatorValueModel[]): void => { 
				let selectItems: SelectItem[] = [];
                comitete.forEach(comitet => {
					let selectItem: SelectItem = {
						value: comitet[NomenclatorConstants.COMITETE_ATTR_KEY_DENUMIRE],
						label: comitet[NomenclatorConstants.COMITETE_ATTR_KEY_DENUMIRE]
					};
					selectItems.push(selectItem);
				});   
				this.comitetSelectItems = selectItems;
				
				ListItemUtils.sortByLabel(this.comitetSelectItems); 
            },
            onFailure: (appError: AppError) => {
                this.messageDisplayer.displayAppError(appError);
            }
		});	
	}

	private buildGetNomenclatorValuesRequestModel(): GetNomenclatorValuesRequestModel {
		let filter: GetNomenclatorValuesRequestModel = new GetNomenclatorValuesRequestModel();
		filter.nomenclatorCode = NomenclatorConstants.NOMENCLATOR_CODE_COMITETE;
		filter.filters = this.buildNomenclatorFilter();
		return filter;
	}

	private buildNomenclatorFilter(): NomenclatorFilter[] {
		let filters : NomenclatorFilter[] = [];
		
		let nomenclatorFilter: NomenclatorSimpleFilter = new NomenclatorSimpleFilter();
		nomenclatorFilter.attributeKey = NomenclatorConstants.COMITETE_ATTR_KEY_ORGANISM;
		nomenclatorFilter.value = this.organismFormControl.value.id;

		filters.push(nomenclatorFilter);
		return filters;
	}

	private init(): void {
		this.loading = false;
		this.reportVisible = false;
		this.report = CheltuieliArbSiReprezentantArbReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 300) + "px";
		this.prepareTitularSelectItems();
		this.prepareValutaSelectItems();
		this.prepareTipOrganism();
		this.prepareOrganismSelectItems();
		this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("explicatie", this.translateUtils.translateLabel("REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_EXPLICATIE")));
        this.columns.push(this.buildColumn("suma", this.translateUtils.translateLabel("REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_SUMA")));
        this.columns.push(this.buildColumn("valuta", this.translateUtils.translateLabel("REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_VALUTA")));
        this.columns.push(this.buildColumn("numerarSauCard", this.translateUtils.translateLabel("REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NUMERAR_SAU_CARD")));
	
		this.cheltuieliReprezentantArbColumns.push(this.buildColumn("explicatie", this.translateUtils.translateLabel("REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_EXPLICATIE")));
        this.cheltuieliReprezentantArbColumns.push(this.buildColumn("suma", this.translateUtils.translateLabel("REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_SUMA")));
		this.cheltuieliReprezentantArbColumns.push(this.buildColumn("valuta", this.translateUtils.translateLabel("REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_VALUTA")));
		this.cheltuieliReprezentantArbColumns.push(this.buildColumn("numerarSauCard", this.translateUtils.translateLabel("REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB_NUMERAR_SAU_CARD")));
	
	}
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }
	
	public ngOnInit(): void {
		this.formGroup.addControl("titular", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("organism", new FormControl());
		this.formGroup.addControl("comitet", new FormControl());
		this.formGroup.addControl("dataDecontDeLa", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("dataDecontPanaLa", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("valuta", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("numarDecizie", new FormControl(null, [Validators.required]));
	}

	public onChangeFiltruOrganism(): void{
		if (ObjectUtils.isNotNullOrUndefined(this.organismFormControl.value)) {
			let tipOrganismId: number = Number.parseInt(this.organismFormControl.value[NomenclatorConstants.ORGANISME_ATTR_KEY_TIP]);

			if (tipOrganismId === this.organismInternationalId){
				this.showComitet = true;
				this.prepareComitetSelectItems();
			} else {
				this.showComitet = false;
			}		
		} else {
			this.showComitet = false;
		}
	}

	private prepareNumarDeciziiSelectItems() : void {
		this.numarDecizieSelectItems = [];
		this.deplasariDeconturiService.getAllNumarDeciziiByFilter(this.prepareCheltuieliArbSiReprezentantArbReportDateFilterModel(), {
			onSuccess: (decizii: string[]): void => {
				let selectItems: SelectItem[] = [];
				decizii.forEach((dedcizie: string) => {
					let selectItem: SelectItem = {
						value: dedcizie,
						label: dedcizie
					};
					selectItems.push(selectItem);
				});
				this.numarDecizieSelectItems = selectItems;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});	
	}

	private prepareCheltuieliArbSiReprezentantArbReportDateFilterModel(): CheltuieliArbSiReprezentantArbReportDateFilterModel {
		let filter: CheltuieliArbSiReprezentantArbReportDateFilterModel = new CheltuieliArbSiReprezentantArbReportDateFilterModel();
		filter.dataDecontDeLa = this.dataDecontDeLaFormControl.value;
		filter.dataDecontPanaLa = this.dataDecontPanaLaFormControl.value;
		return filter;
	}

	public dataDecontDeLaChange(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.dataDecontDeLaFormControl.value) && ObjectUtils.isNotNullOrUndefined(this.dataDecontPanaLaFormControl.value) ) {
			this.prepareNumarDeciziiSelectItems();
		}
	}

	public dataDecontPanaLaChange(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.dataDecontDeLaFormControl.value) && ObjectUtils.isNotNullOrUndefined(this.dataDecontPanaLaFormControl.value) ) {
			this.prepareNumarDeciziiSelectItems();
		}
	}

	private lock(): void {
		this.loading = true;
	}

	private unlock(): void {
		this.loading = false;
	}

	public onView(): void {
		if (!this.isFilterValid()) {
			return;
		}		
		this.lock();
		this.reportVisible = false;
		let reportFilter: CheltuieliArbSiReprezentantArbReportFilterModel = this.prepareFilterModel();
		this.reportService.getCheltuieliArbSiReprezentantArbReport(reportFilter, {
			onSuccess: (theReport: CheltuieliArbSiReprezentantArbReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = CheltuieliArbSiReprezentantArbReportComponent.EMPTY_REPORT;
				}
				this.report = this.prepareReport(theReport);		
				this.reportVisible = true;
				this.unlock();
			},
			onFailure: (error: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private prepareReport(report : CheltuieliArbSiReprezentantArbReportModel ): CheltuieliArbSiReprezentantArbReportModel {
		this.valuta = this.translateUtils.translateLabel(this.valutaFormControl.value);
		
		report.cheltuieliArb.rows.forEach((cheluiala: CheltuieliArbSiRePrezentantArbRowModel) => {	
			cheluiala.explicatie =  this.translateUtils.translateLabel(cheluiala.explicatie);
			cheluiala.valuta = this.translateUtils.translateLabel(cheluiala.valuta);
			cheluiala.numerarSauCard = this.translateUtils.translateLabel(cheluiala.numerarSauCard);
		});
		report.cheltuieliReprezentantArb.rows.forEach((cheluiala: CheltuieliArbSiRePrezentantArbRowModel) => {
			cheluiala.explicatie =  this.translateUtils.translateLabel(cheluiala.explicatie);
			cheluiala.valuta = this.translateUtils.translateLabel(cheluiala.valuta);
			cheluiala.numerarSauCard = this.translateUtils.translateLabel(cheluiala.numerarSauCard);
		});

		return report;
	}

	private isFilterValid(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}

	private prepareFilterModel(): CheltuieliArbSiReprezentantArbReportFilterModel {
		let filter: CheltuieliArbSiReprezentantArbReportFilterModel = new CheltuieliArbSiReprezentantArbReportFilterModel();
		filter.titular = this.titularFormControl.value;
		if (ObjectUtils.isNotNullOrUndefined(this.organismFormControl.value)) {
			filter.organismId = this.organismFormControl.value.id;
			filter.comitet = this.comitetFormControl.value;
		}
		filter.dataDecontDeLa = this.dataDecontDeLaFormControl.value;
		filter.dataDecontPanaLa = this.dataDecontPanaLaFormControl.value;
		filter.numarDecizie = this.numarDecizieFormControl.value;
		filter.valuta = this.valutaFormControl.value;
		return filter;
	}

	public onExportExcel(): void {
		alert("not implemented yet");
	}

	public get footerVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.report) && (ArrayUtils.isNotEmpty(this.report.cheltuieliArb.rows) || ArrayUtils.isNotEmpty(this.report.cheltuieliReprezentantArb.rows) );
	}

	public get titularFormControl(): FormControl {
		return this.getFormControlByName("titular");
	}

	public get organismFormControl(): FormControl {
		return this.getFormControlByName("organism");
	}

	public get comitetFormControl(): FormControl {
		return this.getFormControlByName("comitet");
	}

	public get dataDecontDeLaFormControl(): FormControl {
		return this.getFormControlByName("dataDecontDeLa");
	}

	public get dataDecontPanaLaFormControl(): FormControl {
		return this.getFormControlByName("dataDecontPanaLa");
	}
	
	public get valutaFormControl(): FormControl {
		return this.getFormControlByName("valuta");
	}
	
	public get numarDecizieFormControl(): FormControl {
		return this.getFormControlByName("numarDecizie");
	}

	private getFormControlByName(name: string): FormControl {
		return <FormControl> this.formGroup.get(name);
	}
	
	public exportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.dataTable)) {
			return;
		}
		this.dataTable.exportFunction = (exportCell: any) => {
			exportCell.data = ExportUtils.replaceQuotationMarkIfStringData( exportCell.data);
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB");
		this.dataTable.exportCSV();
	}
}
