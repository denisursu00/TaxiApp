import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl} from "@angular/forms";
import { DateConstants, DateUtils, FormUtils, ListaProiectelorCareAuVizatActiunileLuniiReportModel, ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel, TranslateUtils } from "../../../shared";
import { ObjectUtils, AppError, ConfirmationUtils } from "../../../shared";
import { ReportService, MessageDisplayer } from "../../../shared";
import { Table } from "primeng/table";
import { Column } from "primeng/primeng";
import { ExportUtils } from "@app/shared/utils/export-utils";

@Component({
	selector: "app-lista-proiectelor-care-au-vizat-actiunile-lunii-report",
	templateUrl: "./lista-proiectelor-care-au-vizat-actiunile-lunii-report.component.html"
})
export class ListaProiectelorCareAuVizatActiunileLuniiReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = [];

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

	public report: ListaProiectelorCareAuVizatActiunileLuniiReportModel[];

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
		this.report = ListaProiectelorCareAuVizatActiunileLuniiReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 180) + "px";
	    this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("denumireProiect", this.translateUtils.translateLabel("REPORT_LISTA_PROIECTELOR_CARE_AU_VIZAT_ACTIUNILE_LUNII_DENUMIRE_PROIECT")));
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
		let reportFilter: ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel = this.prepareFilterModel();
		this.reportService.getListaProiectelorCareAuVizatActiunileLuniiReport(reportFilter, {
			onSuccess: (theReport: ListaProiectelorCareAuVizatActiunileLuniiReportModel[]): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = ListaProiectelorCareAuVizatActiunileLuniiReportComponent.EMPTY_REPORT;
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

	private prepareFilterModel(): ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel {
		let filter: ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel = new ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel();
		
		filter.dataInceput = this.dataInceputFormControl.value;
		filter.dataSfarsit = this.dataSfarsitFormControl.value;

		return filter;
	}

	public onExportExcel(): void {
		alert("not implemented yet");
	}

	public get footerVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.report) ;
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
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_LISTA_PROIECTELOR_CARE_AU_VIZAT_ACTIUNILE_LUNII");
		this.dataTable.exportCSV();
	}
}
