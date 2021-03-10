import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DocumentTypeModel, DateConstants, DateUtils, FormUtils, TranslateUtils } from "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from "@app/shared";
import { ReportService, MessageDisplayer } from "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { NumarSedinteSiParticipantiReportFilterModel, NumarSedinteSiParticipantiReportRowModel, NumarSedinteSiParticipantiReportModel } from "@app/shared";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";

@Component({
	selector: "app-numar-sedinte-si-participanti-report",
	templateUrl: "./numar-sedinte-si-participanti-report.component.html"
})
export class NumarSedinteSiParticipantiReportComponent implements OnInit {
		
	@ViewChild(Table)
	public dataTable: Table;

	public columns: Column[] = [];

	private static readonly EMPTY_REPORT = new NumarSedinteSiParticipantiReportModel();

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

	public report: NumarSedinteSiParticipantiReportModel;

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

	private prepareTipSedintaSelectItems(): void {
		this.tipSedintaSelectItems = [];
		this.tipSedintaSelectItems.push({ value: "cd", label: "CD" });
		this.tipSedintaSelectItems.push({ value: "pvg", label: "PVG" });
	}

	private init(): void {
		this.loading = false;
		this.reportVisible = false;
		this.report = NumarSedinteSiParticipantiReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.prepareTipSedintaSelectItems();
		this.prepareColumns();
		
	}

    private prepareColumns(): any {
        this.columns.push(this.buildColumn("dataSedinta", this.translateUtils.translateCode("LABELS.REPORT_NUMAR_SEDINTE_SI_PARTICIPANTI_DATA_SEDINTA")));
        this.columns.push(this.buildColumn("totalMembriiCD", this.translateUtils.translateCode("LABELS.REPORT_NUMAR_SEDINTE_SI_PARTICIPANTI_TOTAL_MEMBRII_CD")));
        this.columns.push(this.buildColumn("totalInvitati", this.translateUtils.translateCode("LABELS.REPORT_NUMAR_SEDINTE_SI_PARTICIPANTI_TOTAL_INVITATI")));
        this.columns.push(this.buildColumn("totalInvitatiARB", this.translateUtils.translateCode("LABELS.REPORT_NUMAR_SEDINTE_SI_PARTICIPANTI_TOTAL_INVITATI_ARB")));
        this.columns.push(this.buildColumn("totalParticipanti", this.translateUtils.translateCode("LABELS.REPORT_NUMAR_SEDINTE_SI_PARTICIPANTI_TOTAL_PARTICIPANTI")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }
	public ngOnInit(): void {
		this.formGroup.addControl("tipSedinta", new FormControl("cd", [Validators.required]));
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
		let reportFilter: NumarSedinteSiParticipantiReportFilterModel = this.prepareFilterModel();
		this.reportService.getNumarSedinteSiParticipantiReport(reportFilter, {
			onSuccess: (theReport: NumarSedinteSiParticipantiReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = NumarSedinteSiParticipantiReportComponent.EMPTY_REPORT;
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

	private prepareFilterModel(): NumarSedinteSiParticipantiReportFilterModel {
		let filter: NumarSedinteSiParticipantiReportFilterModel = new NumarSedinteSiParticipantiReportFilterModel();
		filter.tipSedinta = this.tipSedintaFormControl.value;
		filter.dataSedintaDeLa = this.dataSedintaDeLaFormControl.value;
		filter.dataSedintaPanaLa = this.dataSedintaPanaLaFormControl.value;
		return filter;
	}

	public exportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.dataTable)) {
			return;
		}
		this.dataTable.exportFilename = "Raport numar sedinte CD-PVG si participanti";
		this.dataTable.exportFunction = (exportCell: any) => {
			exportCell.data = ExportUtils.replaceQuotationMarkIfStringData( exportCell.data);
			if (exportCell.field === "dataSedinta" ) {
				return DateUtils.formatForDisplay(exportCell.data);
			} else {
				return exportCell.data;
			}
		}
	
		this.dataTable.exportCSV();
	}

	public get footerVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.report) && ArrayUtils.isNotEmpty(this.report.rows);
	}

	public get tipSedintaFormControl(): FormControl {
		return this.getFormControlByName("tipSedinta");
	}

	public get dataSedintaDeLaFormControl(): FormControl {
		return this.getFormControlByName("dataSedintaDeLa");
	}

	public get dataSedintaPanaLaFormControl(): FormControl {
		return this.getFormControlByName("dataSedintaPanaLa");
	}

	public getFormControlByName(name: string): FormControl {
		return <FormControl> this.formGroup.get(name);
	}
}
