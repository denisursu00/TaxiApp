import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { AppError, DateConstants, DateUtils, FormUtils, MessageDisplayer, NomenclatorConstants, NomenclatorService, NomenclatorValueModel, ObjectUtils, PrezentaSedinteMembriiReportFilterModel, PrezentaSedinteMembriiReportModel, ReportService, TranslateUtils } from "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { ReportConstants } from "../constants/report.constants";
import { Table } from "primeng/table";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
    selector: "app-prezenta-sedinte-membrii-report.component",
    templateUrl: "./prezenta-sedinte-membrii-report.component.html"
})
export class PrezentaSedinteMembriiReportComponent implements OnInit {

	@ViewChild(Table)
    public dataTable: Table;
    
    public columns: Column[] = [];
    private readonly EMPTY_REPORT: PrezentaSedinteMembriiReportModel = new PrezentaSedinteMembriiReportModel();

    private messageDisplayer: MessageDisplayer;
    private reportService: ReportService;
    private nomenclatorService: NomenclatorService;
    private translateUtils: TranslateUtils;

    public reportVisible: boolean;
    public loading: boolean;

    private formBuilder: FormBuilder;
    public formGroup: FormGroup;

    public dateFormat: string;
    public yearRange: string;

    public totalMembrii: number;
    public report: PrezentaSedinteMembriiReportModel;
    public rowGroupMetadata: any;

    public institutieMembruCDSelectItems: SelectItem[];

    public scrollHeight: string;

    public constructor(reportService: ReportService, formBuilder: FormBuilder, messageDisplayer: MessageDisplayer,
        nomenclatorService: NomenclatorService, translateUtils: TranslateUtils) {

        this.reportService = reportService;
        this.formBuilder = formBuilder;
        this.messageDisplayer = messageDisplayer;
        this.nomenclatorService = nomenclatorService;
        this.translateUtils = translateUtils;
        this.init();
    }

    private prepareInstitutieMembruCDSelectItems(): void {
        this.institutieMembruCDSelectItems = [];
        
        this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII_CD, {
            onSuccess: (institutiiMembruCD: NomenclatorValueModel[]): void => {

                institutiiMembruCD.forEach(institutieMembruCD => {
                    let id:string = institutieMembruCD.id.toString();
                    let nume:string = institutieMembruCD[NomenclatorConstants.INSTITUTII_CD_ATTR_KEY_DENUMIRE_INSTITUTIE];
              
                    this.institutieMembruCDSelectItems.push({ label: nume, value: id });
                });

                ListItemUtils.sortByLabel(this.institutieMembruCDSelectItems);
            },
            onFailure: (appError: AppError): void => {
                this.messageDisplayer.displayAppError(appError);
            }
        });
    }

    private init(): void {
        this.loading = false;
        this.reportVisible = false;
        this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
        this.yearRange = DateUtils.getDefaultYearRange();
        this.prepareInstitutieMembruCDSelectItems();
        this.report = this.EMPTY_REPORT;
        this.scrollHeight = (window.innerHeight - 180) + "px";
        this.prepareColumns();
    }

    private prepareColumns(): any {
        this.columns.push(this.buildColumn("tipSedinta", this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTA_MEMBRII_FILTER_TIP_SEDINTA")));
        this.columns.push(this.buildColumn("dataSedintaForDisplay", this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTA_MEMBRII_DATA_SEDINTA")));
        this.columns.push(this.buildColumn("institutieMembru", this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTA_MEMBRII_INSTITUTIE_MEMBRU_CD")));
        this.columns.push(this.buildColumn("membru", this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTA_MEMBRII_MEMBRU_CD")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }
    public ngOnInit(): void {
        this.formGroup = this.formBuilder.group([]);
        this.formGroup.addControl("tipSedinta", new FormControl(ReportConstants.DOCUMENT_PREZENTA_CD_PVG_TIP_SEDINTA_CD_VALUE));
        this.formGroup.addControl("institutieMembruCD", new FormControl());
        this.formGroup.addControl("dataSedintaDeLa", new FormControl(null, [Validators.required]));
        this.formGroup.addControl("dataSedintaPanaLa", new FormControl(null, [Validators.required]));
        this.updateRowGroupMetaData();
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
        
        let reportFilter: PrezentaSedinteMembriiReportFilterModel = this.prepareFilterModel();

        this.reportService.getPrezentaSedinteMembriiReport(reportFilter, {
            onSuccess: (theReport: PrezentaSedinteMembriiReportModel): void => {
                
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

    public prepareFilterModel(): PrezentaSedinteMembriiReportFilterModel {
        let filter: PrezentaSedinteMembriiReportFilterModel = new PrezentaSedinteMembriiReportFilterModel();
        
        filter.tipSedinta = this.tipSedintaFormControl.value;
        filter.institutieMembru = this.institutieMembruCDFormControl.value;
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

    public get dataSedintaDeLaFormControl(): FormControl {
        return this.getFormControlByName("dataSedintaDeLa");
    }

    public get institutieMembruCDFormControl(): FormControl {
        return this.getFormControlByName("institutieMembruCD");
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
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_SEDINTA_MEMBRII");
		this.dataTable.exportCSV();
	}
}