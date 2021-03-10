import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { AppError, DateConstants, DateUtils, FormUtils, MessageDisplayer, ObjectUtils, PrezentaSedinteCdPvgInvitatiARBReportFilterModel, PrezentaSedinteCdPvgInvitatiARBReportModel, ReportService, TranslateUtils } from "@app/shared";
import { SelectItem } from "primeng/api";
import { ReportConstants } from "../constants/report.constants";
import { Table } from "primeng/table";
import { Column } from "primeng/primeng";
import { ExportUtils } from "@app/shared/utils/export-utils";

@Component({
    selector: "app-prezenta-sedinte-invitati-arb-report",
    templateUrl: "./prezenta-sedinte-invitati-arb-report.component.html"
})
export class PrezentaSedinteInvitatiARBReportComponent implements OnInit {
    
    private readonly EMPTY_REPORT: PrezentaSedinteCdPvgInvitatiARBReportModel = new PrezentaSedinteCdPvgInvitatiARBReportModel();

    @ViewChild(Table)
    public dataTable: Table;  
    public columns: Column[] = [];	
    private translateUtils: TranslateUtils;

    private messageDisplayer: MessageDisplayer;
    private reportService: ReportService;

    public reportVisible: boolean;
    public loading: boolean;

    private formBuilder: FormBuilder;
    public formGroup: FormGroup;

    public dateFormat: string;
    public yearRange: string;

    public report: PrezentaSedinteCdPvgInvitatiARBReportModel;
    public rowGroupMetadata: any;

    public invitatARBSelectItems: SelectItem[];

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
        this.scrollHeight = (window.innerHeight - 180) + "px";
        this.prepareColumns();
    }

    private prepareColumns(): any {
        this.columns.push(this.buildColumn("tipSedinta", this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTE_INVITATI_ARB_TIP_SEDINTA")));
        this.columns.push(this.buildColumn("dataSedintaForDisplay", this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTE_INVITATI_ARB_DATA_SEDINTA")));
        this.columns.push(this.buildColumn("invitatArb", this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTE_INVITATI_ARB_INVITAT_ARB")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }

    public ngOnInit(): void {
        this.formGroup.addControl("tipSedinta", new FormControl(ReportConstants.DOCUMENT_PREZENTA_CD_PVG_TIP_SEDINTA_CD_VALUE));
        this.formGroup.addControl("invitatARB", new FormControl());
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
        if(!this.isFilterValid()) {
            return;
        }

        this.lock();
        this.reportVisible = true;

        let reportFilter: PrezentaSedinteCdPvgInvitatiARBReportFilterModel = this.prepareFilterModel();

        this.reportService.getPrezentaSedinteCdPvgInvitatiArbReport(reportFilter, {
            onSuccess: (theReport: PrezentaSedinteCdPvgInvitatiARBReportModel): void => {

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

    public prepareFilterModel(): PrezentaSedinteCdPvgInvitatiARBReportFilterModel {
        let filter: PrezentaSedinteCdPvgInvitatiARBReportFilterModel = new PrezentaSedinteCdPvgInvitatiARBReportFilterModel();

        filter.tipSedinta = this.tipSedintaFormControl.value;
        filter.invitatiArb = this.invitatARBFormControl.value;
        filter.dataSedintaDeLa = this.dataSedintaDeLaFormControl.value;
        filter.dataSedintaPanaLa = this.dataSedintaPanaLaFormControl.value;

        return filter;
    }

    public onExportExcel(): void {
        alert("not implemented yet");
    }

    public get footerVisible(): boolean {
        return ObjectUtils.isNotNullOrUndefined(this.report.rows);
    }

    public get tipSedintaFormControl(): FormControl {
        return this.getFormControlByName("tipSedinta");
    }

    public get invitatARBFormControl(): FormControl {
        return this.getFormControlByName("invitatARB");
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

    public onSort(): void {
        this.updateRowGroupMetaData();
    }

    public updateRowGroupMetaData(): void {
        this.rowGroupMetadata = {};

        let reportRows = this.report.rows;

        if(reportRows) {
            reportRows.forEach((row, index) => {
                let tipSedinta = row.tipSedinta;
                let dataSedinta = row.dataSedintaForDisplay;

                if(index == 0) {
                    this.rowGroupMetadata[tipSedinta] = { index: 0, size: 1 };
                } else {
                    let previousTipSedinta = reportRows[index - 1];
                    let previousRowGroupTipSedinta = previousTipSedinta.tipSedinta;

                    if(tipSedinta === previousRowGroupTipSedinta) {
                        this.rowGroupMetadata[tipSedinta].size++;
                    } else {
                        this.rowGroupMetadata[tipSedinta] = { index: index, size: 1 };
                    }
                }

                if(index == 0) {
                    this.rowGroupMetadata[dataSedinta] = { index: 0, size: 1 };
                } else {
                    let previousDataSedinta = reportRows[index - 1];
                    let previousRowGroupDataSedintaForDisplay = previousDataSedinta.dataSedintaForDisplay;

                    if(dataSedinta === previousRowGroupDataSedintaForDisplay) {
                        this.rowGroupMetadata[dataSedinta].size++;
                    } else {
                        this.rowGroupMetadata[dataSedinta] = { index: index, size: 1 };
                    }
                }
            });
        }
    }

    public exportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.dataTable)) {
			return;
		}
		this.dataTable.exportFunction = (exportCell: any) => {
			exportCell.data = ExportUtils.replaceQuotationMarkIfStringData( exportCell.data);
        }
        this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTE_INVITATI_ARB");
		this.dataTable.exportCSV();
	}
}