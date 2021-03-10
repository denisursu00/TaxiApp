import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DocumentTypeModel, DateConstants, DateUtils, FormUtils, ActiuniOrganizateDeArbReportModel, ActiuniOrganizateDeArbReportFilterModel, TranslateUtils } from "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from "@app/shared";
import { ReportService, MessageDisplayer } from "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { NumarSedinteSiParticipantiReportFilterModel, NumarSedinteSiParticipantiReportRowModel, NumarSedinteSiParticipantiReportModel } from "@app/shared";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";

@Component({
	selector: "app-actiuni-organizate-de-arb-report",
	templateUrl: "./actiuni-organizate-de-arb-report.component.html"
})
export class ActiuniOrganizateDeArbReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new ActiuniOrganizateDeArbReportModel();

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

	public tipSedintaSelectItems: SelectItem[];
	public dateFormat: string;
	public yearRange: string;

	public report: ActiuniOrganizateDeArbReportModel;

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
		this.report = ActiuniOrganizateDeArbReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 180) + "px";
	    this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("dataForDisplay", this.translateUtils.translateLabel("REPORT_ACTIUNI_ORGANIZATE_DE_ARB_DATA")));
        this.columns.push(this.buildColumn("actiune", this.translateUtils.translateLabel("REPORT_ACTIUNI_ORGANIZATE_DE_ARB_ACTIUNE")));
        this.columns.push(this.buildColumn("subiectAgenda", this.translateUtils.translateLabel("REPORT_ACTIUNI_ORGANIZATE_DE_ARB_SUBIECT_AGENDA")));
        this.columns.push(this.buildColumn("detaliuSubiectAgenda", this.translateUtils.translateLabel("REPORT_ACTIUNI_ORGANIZATE_DE_ARB_DETALIU_SUBIECT_AGENDA")));
        this.columns.push(this.buildColumn("participanti", this.translateUtils.translateLabel("REPORT_ACTIUNI_ORGANIZATE_DE_ARB_PARTICIPANTI")));
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
		this.dataInceputFormControl.setValidators(Validators.required);
		this.dataSfarsitFormControl.setValidators(Validators.required);
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
		let reportFilter: ActiuniOrganizateDeArbReportFilterModel = this.prepareFilterModel();
		this.reportService.getActiuniOrganizateDeArbReport(reportFilter, {
			onSuccess: (theReport: ActiuniOrganizateDeArbReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = ActiuniOrganizateDeArbReportComponent.EMPTY_REPORT;
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

	private prepareFilterModel(): ActiuniOrganizateDeArbReportFilterModel {
		let filter: ActiuniOrganizateDeArbReportFilterModel = new ActiuniOrganizateDeArbReportFilterModel();
		filter.dataInceput = this.dataInceputFormControl.value;
		filter.dataSfarsit = this.dataSfarsitFormControl.value;
		return filter;
	}

	public onExportExcel(): void {
		alert("not implemented yet");
	}

	public get footerVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.report) && ArrayUtils.isNotEmpty(this.report.rows);
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
			return exportCell.data;
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_ACTIUNI_ORGANIZATE_DE_ARB");
		this.dataTable.exportCSV();
	}
}
