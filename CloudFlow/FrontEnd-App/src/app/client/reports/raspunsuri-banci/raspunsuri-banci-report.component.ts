import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, FormGroup } from "@angular/forms";
import { RaspunsuriBanciReportBundleModel } from "@app/shared/model/reports/raspunsuri-banci-report-bundle.model";
import { RaspunsuriBanciReportFilterModel } from "@app/shared/model/reports/raspunsuri-banci-report-filter.model";
import { RaspunsuriBanciReportRowModel } from "@app/shared/model/reports/raspunsuri-banci-report-row.model.";
import { AppError, DateConstants, DateUtils, MessageDisplayer, ObjectUtils, ReportService, TranslateUtils } from "./../../../shared";
import { Table } from "primeng/table";
import { Column } from "primeng/primeng";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
    selector: 'app-raspunsuri-banci-report',
    templateUrl: './raspunsuri-banci-report.component.html'
})
export class RaspunsuriBanciReportComponent implements OnInit {

    private readonly EMPTY_REPORT: RaspunsuriBanciReportRowModel[] = [];

	@ViewChild(Table)
    public dataTable: Table;  
	public columns: Column[] = [];	

    private messageDisplayer: MessageDisplayer;
    private reportService: ReportService;
    private translateUtils: TranslateUtils;

    public reportVisible: boolean;
    public loading: boolean;

    private formBuilder: FormBuilder;
    public formGroup: FormGroup;

    public dateFormat: string;
    public yearRange: string;

    public report: RaspunsuriBanciReportRowModel[];

    public rowGroupMetadata: any;

    public bancaSelectItems = [];
    public proiectSelectItems = [];

    public scrollHeight: string;

    public constructor(reportService: ReportService, formBuilder: FormBuilder, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {

        this.messageDisplayer = messageDisplayer;
        this.reportService = reportService;
        this.formBuilder = formBuilder;
        this.reportService = reportService;
        this.translateUtils = translateUtils;

        this.init();
    }

    private init(): void {
        this.loading = false;
        this.reportVisible = false;
        this.formGroup = this.formBuilder.group([]);
        this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
        this.yearRange = DateUtils.getDefaultYearRange();
        this.report = this.EMPTY_REPORT;
        this.scrollHeight = (window.innerHeight - 250) + "px";
        this.addFormControls();
        this.prepareSelectItemsDataFromBundle();
        this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("denumireBanca", this.translateUtils.translateLabel("REPORT_RASPUNSURI_BANCI_BANCA")));
        this.columns.push(this.buildColumn("nrRaspunsuriFaraPropuneri", this.translateUtils.translateLabel("REPORT_RASPUNSURI_BANCI_RASPUNSURI_FARA_PROPUNERI")));
        this.columns.push(this.buildColumn("nrRaspunsuriCuPropuneri", this.translateUtils.translateLabel("REPORT_RASPUNSURI_BANCI_RASPUNSURI_CU_PROPUNERI")));
        this.columns.push(this.buildColumn("nrTotalRaspunsuriPropuneri", this.translateUtils.translateLabel("REPORT_RASPUNSURI_BANCI_TOTAL_RASPUNSURI")));
        this.columns.push(this.buildColumn("coeficientStructuralCalitateRaspunsuri", this.translateUtils.translateLabel("REPORT_RASPUNSURI_BANCI_COEF_STRUCT_CALITATE_RASPUNSURILOR")));
        this.columns.push(this.buildColumn("notaRaspunsuriAjustataCuCalitateaRaspunsurilor", this.translateUtils.translateLabel("REPORT_RASPUNSURI_BANCI_NOTA_RASP_ADJUST_CU_CALITATE_RASPUNSURILOR")));
        this.columns.push(this.buildColumn("nrRaspunsuriFaraIntarziere", this.translateUtils.translateLabel("REPORT_RASPUNSURI_BANCI_NR_RASP_FARA_INTARZIERE")));
        this.columns.push(this.buildColumn("nrRaspunsuriCuIntarzierePesteOZi", this.translateUtils.translateLabel("REPORT_RASPUNSURI_BANCI_NR_RASP_CU_INTARZIERE_PESTE_O_ZI")));
        this.columns.push(this.buildColumn("nrTotalRaspunsuriTermen", this.translateUtils.translateLabel("REPORT_RASPUNSURI_BANCI_TOTAL_RASPUNSURI")));
        this.columns.push(this.buildColumn("coeficientStructuralVitezaRaspunsuri", this.translateUtils.translateLabel("REPORT_RASPUNSURI_BANCI_COEF_STRUCT_VITEZA_RASPUNSURI")));
        this.columns.push(this.buildColumn("notaRaspunsuriAjustataCuVitezaRaspunsurilor", this.translateUtils.translateLabel("REPORT_RASPUNSURI_BANCI_NOTA_RASP_AJUSTATA_CU_VITEZA_RASPUSURILOR")));
        this.columns.push(this.buildColumn("notaTotalaRaspunsuriBanci", this.translateUtils.translateLabel("REPORT_RASPUNSURI_BANCI_NOTA_TOTALA_RASPUSURI_BANCI")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }
	

    public ngOnInit(): void {
    }

    private lock(): void {
        this.loading = true;
    }

    public onView(): void {

        this.lock();
        this.reportVisible = true;
       
        let reportFilter: RaspunsuriBanciReportFilterModel =  this.prepareFilterModel();

        this.reportService.getRaspunsuriBanciReport(reportFilter, {
            onSuccess: (theReport: RaspunsuriBanciReportRowModel[]): void => {
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

    private unlock(): void {
        this.loading = false;
    }

    public onExportExcel(): void {
        alert("not implemented yet");
    }

    public get footerVisible(): boolean {
        return ObjectUtils.isNotNullOrUndefined(this.report);
    }

    private getFormControlByName(name: string): FormControl {
        return <FormControl>this.formGroup.get(name);
    }
   
    private prepareSelectItemsDataFromBundle() {
        this.reportService.getRaspunsuriBanciReportBundle({
            onSuccess: (bundle: RaspunsuriBanciReportBundleModel): void => {
                this.populateDDLFromBundle(bundle);
            },
            onFailure: (appError: AppError): void => {
                this.messageDisplayer.displayAppError(appError);
            }
        });

    }
    private populateDDLFromBundle(bundle: RaspunsuriBanciReportBundleModel): void {

        this.bancaSelectItems = [];
        this.proiectSelectItems = [];

        if (ObjectUtils.isNotNullOrUndefined(bundle)) {
            if (ObjectUtils.isNotNullOrUndefined(bundle.denumiriBanci)) {
                bundle.denumiriBanci.forEach(element => {
                    this.bancaSelectItems.push(element);
                });                
            }
            if (ObjectUtils.isNotNullOrUndefined(bundle.proiecte)) {
                bundle.proiecte.forEach(element => {
                    this.proiectSelectItems.push(element);
                });
            }
        }

        ListItemUtils.sortByLabel(this.bancaSelectItems);
        ListItemUtils.sortByLabel(this.proiectSelectItems);
    }
    private prepareFilterModel(): RaspunsuriBanciReportFilterModel {

        let filter: RaspunsuriBanciReportFilterModel = new RaspunsuriBanciReportFilterModel();

        if (ObjectUtils.isNotNullOrUndefined(this.bancaFormControl.value)) {
            filter.denumireBanca = this.bancaFormControl.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.proiectFormControl.value)) {
            filter.proiectId = Number.parseInt(this.proiectFormControl.value);
        }
        if (ObjectUtils.isNotNullOrUndefined(this.termenRaspunsDeLaFormControl.value)) {
            filter.termenRaspunsDela = this.termenRaspunsDeLaFormControl.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.termenRaspunsPanaLaFormControl.value)) {
            filter.termenRaspunsPanaLa = this.termenRaspunsPanaLaFormControl.value;
        }
        
        return filter;
    }

    private addFormControls(): void {
        this.formGroup.addControl("banca", new FormControl());
        this.formGroup.addControl("proiect", new FormControl());
        this.formGroup.addControl("termenRaspunsDeLa", new FormControl());
        this.formGroup.addControl("termenRaspunsPanaLa", new FormControl());
    }

    private get bancaFormControl(): FormControl {
        return this.getFormControlByName("banca");
    }
    private get proiectFormControl(): FormControl {
        return this.getFormControlByName("proiect");
    }
    private get termenRaspunsDeLaFormControl(): FormControl {
        return this.getFormControlByName("termenRaspunsDeLa");
    }
    private get termenRaspunsPanaLaFormControl(): FormControl {
        return this.getFormControlByName("termenRaspunsPanaLa");
    }
    
	public exportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.dataTable)) {
			return;
		}
		this.dataTable.exportFunction = (exportCell: any) => {
			exportCell.data = ExportUtils.replaceQuotationMarkIfStringData( exportCell.data);
		    if (exportCell.field === "coeficientStructuralCalitateRaspunsuri" 
                || exportCell.field === "notaRaspunsuriAjustataCuCalitateaRaspunsurilor"
                || exportCell.field === "coeficientStructuralVitezaRaspunsuri"
                || exportCell.field === "notaRaspunsuriAjustataCuVitezaRaspunsurilor"
                || exportCell.field === "notaTotalaRaspunsuriBanci" ) {
                return exportCell.data.toFixed(2);
            } else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_RASPUNSURI_BANCI");
		this.dataTable.exportCSV();
	}
	
}