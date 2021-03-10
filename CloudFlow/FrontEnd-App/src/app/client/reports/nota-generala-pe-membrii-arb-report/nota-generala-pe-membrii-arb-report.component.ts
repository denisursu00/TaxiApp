import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DateConstants, DateUtils, FormUtils, NomenclatorConstants, NomenclatorValueModel, NomenclatorService, GetNomenclatorValuesRequestModel, NomenclatorFilter, NomenclatorSimpleFilter, NotaGeneralaPeMembriiArbReportModel, NotaGeneralaPeMembriiArbReportFilterModel, TranslateUtils } from  "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from  "@app/shared";
import { ReportService, MessageDisplayer } from  "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-nota-generala-pe-membrii-arb-report",
	templateUrl: "./nota-generala-pe-membrii-arb-report.component.html"
})
export class NotaGeneralaPeMembriiArbReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new NotaGeneralaPeMembriiArbReportModel();

	@ViewChild(Table)
  public dataTable: Table;  
	public columns: Column[] = [];	

	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;
	private reportService: ReportService;
	private nomenclatorService: NomenclatorService;
	private translateUtils: TranslateUtils;
	
	public reportVisible: boolean;
	public loading: boolean;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public bancaSelectItems: SelectItem[];
	public dateFormat: string;
	public yearRange: string;

	public report: NotaGeneralaPeMembriiArbReportModel;

	public scrollHeight: string;

	public constructor(reportsService: ReportService, formBuilder: FormBuilder,
			messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils, nomenclatorService: NomenclatorService, translateUtils: TranslateUtils) {
		this.reportService = reportsService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;		
		this.nomenclatorService = nomenclatorService;
		this.translateUtils = translateUtils;
		this.init();
	}

	private prepareBancaSelectItems(): void {
		this.bancaSelectItems = [];
		let filterTipInstitutii: GetNomenclatorValuesRequestModel = this.buildGetNomenclatorValuesRequestModel(NomenclatorConstants.NOMENCLATOR_CODE_TIP_INSTITUTII,
			NomenclatorConstants.TIP_INSTITUTII_ATTR_KEY_TIP, NomenclatorConstants.TIP_INSTITUTII_TIP_MEMBRU_ARB);
		this.nomenclatorService.getNomenclatorValues(filterTipInstitutii, {
            onSuccess: (tipInstitutii: NomenclatorValueModel[]): void => { 
				let tipInstitutieId: string = tipInstitutii[0].id.toString();
				
				let filterInstitutii: GetNomenclatorValuesRequestModel = this.buildGetNomenclatorValuesRequestModel(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII,
					NomenclatorConstants.INSITUTII_ATTR_KEY_TIP_INSTITUTIE, tipInstitutieId);
				this.nomenclatorService.getNomenclatorValues(filterInstitutii, {
					onSuccess: (institutii: NomenclatorValueModel[]): void => { 

						institutii.forEach(institutie => {
							let id:number = institutie.id;
							let nume:string = institutie[NomenclatorConstants.INSTITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE];
							this.bancaSelectItems.push({ label: nume, value: id });
						});     
						
						ListItemUtils.sortByLabel(this.bancaSelectItems);
					},
					onFailure: (appError: AppError) => {
						this.messageDisplayer.displayAppError(appError);
					}
				});         
            },
            onFailure: (appError: AppError) => {
                this.messageDisplayer.displayAppError(appError);
            }
		});	
	}

	private buildGetNomenclatorValuesRequestModel(nomenclatorCode: string, nomenclatorKey: string, nomenclatorKeyValue: string): GetNomenclatorValuesRequestModel {
		let filter: GetNomenclatorValuesRequestModel = new GetNomenclatorValuesRequestModel();
		filter.nomenclatorCode = nomenclatorCode;
		filter.filters = this.buildNomenclatorFilter(nomenclatorKey, nomenclatorKeyValue);
		return filter;
	}

	
	private buildNomenclatorFilter(nomenclatorKey: string, nomenclatorKeyValue: string): NomenclatorFilter[] {
		let filters : NomenclatorFilter[] = [];
		
		let nomenclatorFilter: NomenclatorSimpleFilter = new NomenclatorSimpleFilter();
		nomenclatorFilter.attributeKey = nomenclatorKey;
		nomenclatorFilter.value = nomenclatorKeyValue;

		filters.push(nomenclatorFilter);
		return filters;
	}
	
	private init(): void {
		this.loading = false;
		this.reportVisible = false;
		this.report = NotaGeneralaPeMembriiArbReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.prepareBancaSelectItems();
		this.prepareColumns();
	}

	private prepareColumns(): void {
			this.columns.push(this.buildColumn("banca", this.translateUtils.translateLabel("RAPORT_NOTA_GENERALA_PE_MEMBRII_ARB_BANCA")));
			this.columns.push(this.buildColumn("comisie", this.translateUtils.translateLabel("RAPORT_NOTA_GENERALA_PE_MEMBRII_ARB_COMISIE")));
			this.columns.push(this.buildColumn("notaFinalaComisie", this.translateUtils.translateLabel("RAPORT_NOTA_GENERALA_PE_MEMBRII_ARB_NOTA_FINALA_COMISIE")));
			this.columns.push(this.buildColumn("rankNotaComisie", this.translateUtils.translateLabel("RAPORT_NOTA_GENERALA_PE_MEMBRII_ARB_RANK_NOTA_COMISIE")));
	}
	
	buildColumn(field: string, header: string): Column {
			let column = new Column();
			column.header =header;
			column.field = field;
			return column;
	}

	
	public ngOnInit(): void {
        this.formGroup.addControl("banca", new FormControl());
        this.formGroup.addControl("dataSedintaDeLa", new FormControl(null, [Validators.required]));
        this.formGroup.addControl("dataSedintaPanaLa", new FormControl(null, [Validators.required]));
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
		let reportFilter: NotaGeneralaPeMembriiArbReportFilterModel = this.prepareFilterModel();
		this.reportService.getNotaGeneralaPeMembriiArbReport(reportFilter, {
			onSuccess: (theReport: NotaGeneralaPeMembriiArbReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = NotaGeneralaPeMembriiArbReportComponent.EMPTY_REPORT;
				}
				this.report = theReport;		
				this.reportVisible = true;
				this.unlock();
			},
			onFailure: (error: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private isFilterValid(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}

	private prepareFilterModel(): NotaGeneralaPeMembriiArbReportFilterModel {
		let filter: NotaGeneralaPeMembriiArbReportFilterModel = new NotaGeneralaPeMembriiArbReportFilterModel();

		filter.bancaId = this.bancaFormControl.value;
		filter.dataSedintaDeLa = this.dataSedintaDeLaFormControl.value;
		filter.dataSedintaPanaLa = this.dataSedintaPanaLaFormControl.value;
		
		return filter;
	}

	public onExportExcel(): void {
		alert("not implemented yet");
	}

	public get footerVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.report) && ArrayUtils.isNotEmpty(this.report.rows);
	}

    public get bancaFormControl(): FormControl {
		return this.getFormControlByName("banca");
    }
    
    public get dataSedintaDeLaFormControl(): FormControl {
		return this.getFormControlByName("dataSedintaDeLa");
	}

    public get dataSedintaPanaLaFormControl(): FormControl {
		return this.getFormControlByName("dataSedintaPanaLa");
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
			if (exportCell.field === "notaFinalaComisie" ) {
					return exportCell.data.toFixed(2);
			} else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.RAPORT_NOTA_GENERALA_PE_MEMBRII_ARB");
		this.dataTable.exportCSV();
	}
	
}