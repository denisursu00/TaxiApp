import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DocumentTypeModel, DateConstants, DateUtils, FormUtils, NomenclatorConstants, NomenclatorService, JoinedNomenclatorUiAttributesValueModel, DecontCheltuieliAlteDeconturiReportModel, DecontCheltuieliAlteDeconturiReportFilterModel, AlteDeconturiService } from "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils, TranslateUtils } from "@app/shared";
import { ReportService, MessageDisplayer } from "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "decont-cheltuieli-alte-deconturi-report",
	templateUrl: "./decont-cheltuieli-alte-deconturi-report.component.html"
})
export class DecontCheltuieliAlteDeconturiReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new DecontCheltuieliAlteDeconturiReportModel();
	private static readonly VALUE_ALL_FOR_TITULAR_SELECT_ITEM : Number = 0;

	@ViewChild(Table)
    public dataTable: Table;  
	public columns: Column[] = [];	

	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private confirmationUtils: ConfirmationUtils;
	private reportService: ReportService;
	private alteDeconturiService: AlteDeconturiService;
	
	public reportAllTitulariVisible: boolean;
	public reportOneTitularVisible: boolean;
	public loading: boolean;
	public filtruDataDecontDisabled: boolean;
	public titularSelected: boolean;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public titularSelectItems: SelectItem[];
	public dateFormat: string;
	public yearRange: string;

	public report: DecontCheltuieliAlteDeconturiReportModel;

	public scrollHeight: string;

	public constructor(reportsService: ReportService, alteDeconturiService: AlteDeconturiService, formBuilder: FormBuilder,
			messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils, confirmationUtils: ConfirmationUtils) {
		this.reportService = reportsService;
		this.alteDeconturiService = alteDeconturiService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.confirmationUtils = confirmationUtils;		
		this.init();
	}

	private prepareTitularSelectItems() : void {
		this.titularSelectItems = [];
		this.alteDeconturiService.getTitulariOfExistingDeconturi( {
			onSuccess: (titulari: string[]): void => {
				this.titularSelectItems = this.prepareNomenclatorFilterValuesSelectItems(titulari);

				ListItemUtils.sortByLabel(this.titularSelectItems);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
		
	}

	private prepareNomenclatorFilterValuesSelectItems(titulari: string[]): SelectItem[] {
		let selectItems: SelectItem[] = [];
		titulari.forEach((titular: string) => {
			let selectItem: SelectItem = {
				value: titular,
				label: titular
			};

			selectItems.push(selectItem);
		});
		return selectItems;
	}

	private init(): void {
		this.loading = false;
		this.reportAllTitulariVisible = false;
		this.reportOneTitularVisible = false;
		this.filtruDataDecontDisabled = true;
		this.titularSelected = false;
		this.report = DecontCheltuieliAlteDeconturiReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 300) + "px";
		this.prepareTitularSelectItems();
		this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("titular", this.translateUtils.translateLabel("REPORT_DECONT_CHELTUIELI_ALTE_DECONTURI_TITULAR")));
        this.columns.push(this.buildColumn("dataDocumentJustificativ", this.translateUtils.translateLabel("REPORT_DECONT_CHELTUIELI_ALTE_DECONTURI_DATA_DOCUMENT_JUSTIFICATIV")));
        this.columns.push(this.buildColumn("numarDocumentJustificativ", this.translateUtils.translateLabel("REPORT_DECONT_CHELTUIELI_ALTE_DECONTURI_NUMAR_DOCUMENT_JUSTIFICATIV")));
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
		this.formGroup.addControl("dataDecontDeLa", new FormControl());
		this.formGroup.addControl("dataDecontPanaLa", new FormControl());
	}

	private lock(): void {
		this.loading = true;
	}

	private unlock(): void {
		this.loading = false;
	}

	public onChangeFiltruTitular(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.titularFormControl.value)) {
			this.filtruDataDecontDisabled = false;
			this.titularSelected = true;
			this.dataDecontFormControl.setValidators([Validators.required]);
			this.dataDecontFormControl.updateValueAndValidity();
			this.dataDecontDeLaFormControl.setValue(null);
			this.dataDecontDeLaFormControl.updateValueAndValidity();
			this.dataDecontPanaLaFormControl.setValue(null);
			this.dataDecontPanaLaFormControl.updateValueAndValidity();
		} else {
			this.filtruDataDecontDisabled = true;
			this.titularSelected = false;
			this.dataDecontFormControl.clearValidators();
			this.dataDecontFormControl.setValue(null);
			this.dataDecontFormControl.updateValueAndValidity();
		}
	}

	public onView(): void {
		if (!this.isFilterValid()) {
			return;
		}		
		this.lock();
		this.reportAllTitulariVisible = false;
		this.reportOneTitularVisible = false;
		let reportFilter: DecontCheltuieliAlteDeconturiReportFilterModel = this.prepareFilterModel();
		this.reportService.getDecontCheltuieliAlteDeconturiReport(reportFilter, {
			onSuccess: (theReport: DecontCheltuieliAlteDeconturiReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = DecontCheltuieliAlteDeconturiReportComponent.EMPTY_REPORT;
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

	private prepareFilterModel(): DecontCheltuieliAlteDeconturiReportFilterModel {
		let filter: DecontCheltuieliAlteDeconturiReportFilterModel = new DecontCheltuieliAlteDeconturiReportFilterModel();
		filter.titular = this.titularFormControl.value;
		if (ObjectUtils.isNullOrUndefined(this.titularFormControl.value)) {
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
		this.reportAllTitulariVisible = ObjectUtils.isNullOrUndefined(this.titularFormControl.value);
		this.reportOneTitularVisible = ObjectUtils.isNotNullOrUndefined(this.titularFormControl.value);
		
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
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_DECONT_CHELTUIELI_ALTE_DECONTURI");
		this.dataTable.exportCSV();
	}
}