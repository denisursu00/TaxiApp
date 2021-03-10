import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl } from "@angular/forms";
import { DateConstants, DateUtils, FormUtils, DocumenteTrimiseDeArbReportModel, DocumenteTrimiseDeArbReportFilterModel, TranslateUtils, UiUtils } from "./../../../shared";
import { ObjectUtils, AppError, ConfirmationUtils } from "./../../../shared";
import { ReportService, MessageDisplayer } from "./../../../shared";
import { Table } from "primeng/table";
import { Column } from "primeng/primeng";
import { ExportUtils } from "@app/shared/utils/export-utils";

@Component({
	selector: "app-documente-trimise-de-arb-raport",
	templateUrl: "./documente-trimise-de-arb-raport.component.html"
})
export class DocumenteTrimiseDeArbReportComponent implements OnInit {

	private static readonly EMPTY_REPORT = [];
	private static readonly COLUMN_STYLE: string = "style";
	
	@ViewChild(Table)
    public dataTable: Table;  
	public columns: Column[] = [];	

	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;
	private reportService: ReportService;
	private translateUtils: TranslateUtils;
	
	public reportVisible: boolean;
	public loading: boolean;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public dateFormat: string;
	public yearRange: string;

	public report: DocumenteTrimiseDeArbReportModel[];

	public scrollHeight: string;

	public constructor(reportsService: ReportService, formBuilder: FormBuilder,
			messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils, translateUtils: TranslateUtils) {
		this.reportService = reportsService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;	
		this.translateUtils = translateUtils;	
		this.init();
	}

	private init(): void {
		this.loading = false;
		this.reportVisible = false;
		this.report = DocumenteTrimiseDeArbReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 180) + "px";
	    this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("dataInregistrare", this.translateUtils.translateLabel("REPORT_DOCUMENTE_TRIMISE_DE_ARB_DATA_INREGISTRARE")));
        this.columns.push(this.buildColumn("institutie", this.translateUtils.translateLabel("REPORT_DOCUMENTE_TRIMISE_DE_ARB_INSTITUTIE")));
        this.columns.push(this.buildColumn("document", this.translateUtils.translateLabel("REPORT_DOCUMENTE_TRIMISE_DE_ARB_DOCUMENT")));
        this.columns.push(this.buildColumn("numarInregistrareIesireForDisplay", this.translateUtils.translateLabel("REPORT_DOCUMENTE_TRIMISE_DE_ARB_NUMAR_INREGISTRARE_IESIRE")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }
	
	public ngOnInit(): void {
		this.formGroup.addControl("dataInceput", new FormControl());
		this.formGroup.addControl("dataSfarsit", new FormControl());
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
        let reportFilter: DocumenteTrimiseDeArbReportFilterModel = this.prepareFilterModel();
		this.reportService.getDocumenteTrimiseDeArbReport(reportFilter, {
			onSuccess: (theReport: DocumenteTrimiseDeArbReportModel[]): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = DocumenteTrimiseDeArbReportComponent.EMPTY_REPORT;
				}
				this.report = theReport;
				this.report.forEach(element => {
					element[DocumenteTrimiseDeArbReportComponent.COLUMN_STYLE] = {};	
					UiUtils.appendTableCellCollapseStyle(element[DocumenteTrimiseDeArbReportComponent.COLUMN_STYLE]);
				});					
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

	private prepareFilterModel(): DocumenteTrimiseDeArbReportFilterModel {
		let filter: DocumenteTrimiseDeArbReportFilterModel = new DocumenteTrimiseDeArbReportFilterModel();
		
		filter.dataInceput = this.dataInceputFormControl.value;
        filter.dataSfarsit = this.dataSfarsitFormControl.value;

		return filter;
	}

	public get footerVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.report);
	}

	public get dataInceputFormControl(): FormControl {
		return this.getFormControlByName("dataInceput");
	}

	public get dataSfarsitFormControl(): FormControl {
		return this.getFormControlByName("dataSfarsit");
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
			if (exportCell.field === "dataInregistrare" ) {
				return DateUtils.formatForDisplay(exportCell.data);
			} else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_DOCUMENTE_TRIMISE_DE_ARB");
		this.dataTable.exportCSV();
	}

	public onRowSelect(event: any) {
		this.report.forEach(element => {
			if (element.numarInregistrareIesire === event.data.numarInregistrareIesire && element.institutie === event.data.institutie) {
				element[DocumenteTrimiseDeArbReportComponent.COLUMN_STYLE] = {};
			} else {
				UiUtils.appendTableCellCollapseStyle(element[DocumenteTrimiseDeArbReportComponent.COLUMN_STYLE]);
			}
		});
	}

	public onRowUnselect(event: any) {
		UiUtils.appendTableCellCollapseStyle(event.data[DocumenteTrimiseDeArbReportComponent.COLUMN_STYLE]);
	}
}
