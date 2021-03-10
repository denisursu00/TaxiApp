import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DocumentTypeModel, DateConstants, DateUtils, FormUtils, ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportFilterModel, ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel, TranslateUtils, UiUtils} from "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from "@app/shared";
import { ReportService, MessageDisplayer } from "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { NumarSedinteSiParticipantiReportFilterModel, NumarSedinteSiParticipantiReportRowModel, NumarSedinteSiParticipantiReportModel } from "@app/shared";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";

@Component({
	selector: "app-participare-reprezentanti-arb-la-actiuni-in-afara-asociatiei-report",
	templateUrl: "./participare-reprezentanti-arb-la-actiuni-in-afara-asociatiei-report.component.html"
})
export class ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel();
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

	public tipSedintaSelectItems: SelectItem[];
	public dateFormat: string;
	public yearRange: string;

	public report: ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel;

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
		this.report = ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 180) + "px";
	    this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("data", this.translateUtils.translateLabel("REPORT_PARTICIPARE_REPREZENTANTI_ARB_LA_ACTIUNI_IN_AFARA_ASOCIATIEI_DATA")));
        this.columns.push(this.buildColumn("actiune", this.translateUtils.translateLabel("REPORT_PARTICIPARE_REPREZENTANTI_ARB_LA_ACTIUNI_IN_AFARA_ASOCIATIEI_ACTIUNE")));
        this.columns.push(this.buildColumn("subiectAgenda", this.translateUtils.translateLabel("REPORT_ACTIUNI_ORGANIZATE_DE_ARB_SUBIECT_AGENDA")));
        this.columns.push(this.buildColumn("detaliuSubiectAgenda", this.translateUtils.translateLabel("REPORT_ACTIUNI_ORGANIZATE_DE_ARB_DETALIU_SUBIECT_AGENDA")));
        this.columns.push(this.buildColumn("participanti", this.translateUtils.translateLabel("REPORT_PARTICIPARE_REPREZENTANTI_ARB_LA_ACTIUNI_IN_AFARA_ASOCIATIEI_PARTICIPANTI")));
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
		let reportFilter: ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportFilterModel = this.prepareFilterModel();
		this.reportService.getParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReport(reportFilter, {
			onSuccess: (theReport: ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportComponent.EMPTY_REPORT;
				}
				this.report = theReport;	
				this.report.rows.forEach(element => {
					element[ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportComponent.COLUMN_STYLE] = {};	
					UiUtils.appendTableCellCollapseStyle(element[ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportComponent.COLUMN_STYLE]);
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

	private prepareFilterModel(): ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportFilterModel {
		let filter: ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportFilterModel = new ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportFilterModel();
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

	public getFormControlByName(name: string): FormControl {
		return <FormControl> this.formGroup.get(name);
	}

	public exportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.dataTable)) {
			return;
		}
		this.dataTable.exportFunction = (exportCell: any) => {
			exportCell.data = ExportUtils.replaceQuotationMarkIfStringData( exportCell.data);
			if (exportCell.field === "data" ) {
				return DateUtils.formatForDisplay(exportCell.data);
			} else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_PARTICIPARE_REPREZENTANTI_ARB_LA_ACTIUNI_IN_AFARA_ASOCIATIEI");
		this.dataTable.exportCSV();
	}

	public onRowSelect(event: any) {
		this.report.rows.forEach(element => {
			if (element.data === event.data.data && element.actiune === event.data.actiune && element.participanti === event.data.participanti) {
				element[ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportComponent.COLUMN_STYLE] = {};
			} else {
				UiUtils.appendTableCellCollapseStyle(element[ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportComponent.COLUMN_STYLE]);
			}
		});
	}

	public onRowUnselect(event: any) {
		UiUtils.appendTableCellCollapseStyle(event.data[ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportComponent.COLUMN_STYLE]);
	}
}