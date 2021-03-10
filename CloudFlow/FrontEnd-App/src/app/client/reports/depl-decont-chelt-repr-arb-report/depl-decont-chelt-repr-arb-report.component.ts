import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DocumentTypeModel, DateConstants, DateUtils, FormUtils, NomenclatorConstants, DeplasariDeconturiService } from "./../../../shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils, TranslateUtils } from "./../../../shared";
import { ReportService, MessageDisplayer } from "./../../../shared";
import { SelectItem, Column } from "primeng/primeng";
import { DeplDecontCheltReprArbReportFilterModel, DeplDecontCheltReprArbReportRowModel, DeplDecontCheltReprArbReportModel, NomenclatorService, JoinedNomenclatorUiAttributesValueModel } from "./../../../shared";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "depl-decont-chelt-repr-arb-report",
	templateUrl: "./depl-decont-chelt-repr-arb-report.component.html"
})
export class DeplDecontCheltReprArbReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new DeplDecontCheltReprArbReportModel();

	@ViewChild(Table)
    public dataTable: Table;  
	public columns: Column[] = [];	

	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private confirmationUtils: ConfirmationUtils;
	private reportService: ReportService;
	private nomenclatorService: NomenclatorService;
	private deplasariDeconturiService: DeplasariDeconturiService;
	
	public reportAllTitularVisible: boolean;
	public reportOneTitularVisible: boolean;
	public loading: boolean;
	public filtruDataDecontDisabled: boolean;
	public filtruDataDecontDeLaDisabled: boolean;
	public filtruDataDecontPanaLaDisabled: boolean;
	public titularSelected: boolean;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public titularSelectItems: SelectItem[];
	public dateFormat: string;
	public yearRange: string;

	public report: DeplDecontCheltReprArbReportModel;

	public scrollHeight: string;

	public constructor(reportsService: ReportService, nomenclatorService: NomenclatorService, formBuilder: FormBuilder,
			messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils, confirmationUtils: ConfirmationUtils, 
			deplasariDeconturiService: DeplasariDeconturiService) {
		this.reportService = reportsService;
		this.nomenclatorService = nomenclatorService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.confirmationUtils = confirmationUtils;	
		this.deplasariDeconturiService = deplasariDeconturiService;	
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

	private init(): void {
		this.loading = false;
		this.reportAllTitularVisible = false;
		this.reportOneTitularVisible = false;
		this.filtruDataDecontDisabled = true;
		this.filtruDataDecontDeLaDisabled = false;
		this.filtruDataDecontPanaLaDisabled = false;
		this.titularSelected = false;
		this.report = DeplDecontCheltReprArbReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.scrollHeight = (window.innerHeight - 200) + "px";
		this.yearRange = DateUtils.getDefaultYearRange();
		this.prepareTitularSelectItems();
	    this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("titular", this.translateUtils.translateLabel("REPORT_CHELTUIELI_REPREZENTANT_ARB_TITULAR")));
        this.columns.push(this.buildColumn("dataDocumentJustificativ", this.translateUtils.translateLabel("REPORT_CHELTUIELI_REPREZENTANT_ARB_DATA_DOC_JUSTIFICATIV")));
        this.columns.push(this.buildColumn("nrDocumentJustificativ", this.translateUtils.translateLabel("REPORT_CHELTUIELI_REPREZENTANT_ARB_NR_DOC_JUSTIFICATIV")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }
	
	
	public ngOnInit(): void {
		this.formGroup.addControl("titular", new FormControl());
		this.formGroup.addControl("dataDecont", new FormControl());
		this.formGroup.addControl("dataDecontDeLa", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("dataDecontPanaLa", new FormControl(null, [Validators.required]));
	}

	private lock(): void {
		this.loading = true;
	}

	private unlock(): void {
		this.loading = false;
	}

	public onChangeFiltruTitular(): void {
		if (this.titularFormControl.value != null) {
			this.filtruDataDecontDisabled = false;
			this.filtruDataDecontDeLaDisabled = true;
			this.filtruDataDecontPanaLaDisabled = true;
			this.titularSelected = true;
			this.dataDecontFormControl.setValidators([Validators.required]);
			this.dataDecontFormControl.updateValueAndValidity();
			this.dataDecontDeLaFormControl.setValue(null);
			this.dataDecontDeLaFormControl.clearValidators();
			this.dataDecontDeLaFormControl.updateValueAndValidity();
			this.dataDecontPanaLaFormControl.setValue(null);
			this.dataDecontPanaLaFormControl.clearValidators();
			this.dataDecontPanaLaFormControl.updateValueAndValidity();
		} else {
			this.filtruDataDecontDisabled = true;
			this.filtruDataDecontDeLaDisabled = false;
			this.filtruDataDecontPanaLaDisabled = false;
			this.titularSelected = false;
			this.dataDecontFormControl.clearValidators();
			this.dataDecontFormControl.setValue(null);
			this.dataDecontFormControl.updateValueAndValidity();
			this.dataDecontDeLaFormControl.setValidators([Validators.required]);
			this.dataDecontDeLaFormControl.updateValueAndValidity();
			this.dataDecontPanaLaFormControl.setValidators([Validators.required]);
			this.dataDecontPanaLaFormControl.updateValueAndValidity();
		}
	}

	public onView(): void {
		if (!this.isFilterValid()) {
			return;
		}		
		this.lock();
		this.reportAllTitularVisible = false;
		this.reportOneTitularVisible = false;
		let reportFilter: DeplDecontCheltReprArbReportFilterModel = this.prepareFilterModel();
		this.reportService.getDeplasariDeconturiDecontCheltuieliReprezentantArb(reportFilter, {
			onSuccess: (theReport: DeplDecontCheltReprArbReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = DeplDecontCheltReprArbReportComponent.EMPTY_REPORT;
				}
				this.report = theReport;
				this.displayAppropiateTable();
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

	private prepareFilterModel(): DeplDecontCheltReprArbReportFilterModel {
		let filter: DeplDecontCheltReprArbReportFilterModel = new DeplDecontCheltReprArbReportFilterModel();
		filter.titular = this.titularFormControl.value;
		if (this.titularFormControl.value == null) {
			filter.dataDecont = null;
			filter.dataDecontDeLa = this.dataDecontDeLaFormControl.value;
			filter.dataDecontPanaLa = this.dataDecontPanaLaFormControl.value;
		} else {
			filter.dataDecont = this.dataDecontFormControl.value;
			filter.dataDecontDeLa = null;
			filter.dataDecontPanaLa = null;
		}
		
		return filter;
	}

	private displayAppropiateTable(): void {
		
		if (this.titularFormControl.value == null) {
			this.reportAllTitularVisible = true;
		} else {
			this.reportAllTitularVisible = false;;
		}

		this.reportOneTitularVisible = !this.reportAllTitularVisible;
		
	}

	public onExportExcel(): void {
		alert("not implemented yet");
	}

	public get footerVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.report) && ArrayUtils.isNotEmpty(this.report.rows);
	}

	public get titularFormControl(): FormControl {
		return this.getFormControlByName("titular");
	}

	public get dataDecontFormControl(): FormControl {
		return this.getFormControlByName("dataDecont");
	}

	public get dataDecontDeLaFormControl(): FormControl {
		return this.getFormControlByName("dataDecontDeLa");
	}

	public get dataDecontPanaLaFormControl(): FormControl {
		return this.getFormControlByName("dataDecontPanaLa");
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
			if (exportCell.field === "dataDocumentJustificativ" ) {
				return DateUtils.formatForDisplay(exportCell.data);
			} else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.RAPORT_DEPLASARI_DECONTURI_CHELTUIELI_REPREZENTANT_ARB");
		this.dataTable.exportCSV();
	}
}
