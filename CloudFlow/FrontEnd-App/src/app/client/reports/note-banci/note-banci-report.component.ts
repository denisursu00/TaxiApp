import { Component, OnInit, ViewChild } from "@angular/core";
import { MessageDisplayer, ReportService, DateConstants, DateUtils, AppError, FormUtils, ObjectUtils, PrezentaSedinteCdPvgInvitatiARBReportFilterModel, PrezentaSedinteCdPvgInvitatiARBReportModel, NomenclatorService, PrezentaSedinteMembriiReportModel, ValueOfNomenclatorValueField, NomenclatorConstants, NomenclatorValueModel, NomenclatorModel, TranslateUtils } from "@app/shared";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { SelectItem } from "primeng/api";
import { ReportConstants } from "../constants/report.constants";
import { NoteBanciReportModel } from "@app/shared/model/reports/note-banci-report.model";
import { NoteBanciReportFilterModel } from "@app/shared/model/reports/note-banci-report-filter.model";
import { Table } from "primeng/table";
import { Column } from "primeng/primeng";
import { ExportUtils } from "@app/shared/utils/export-utils";

@Component({
    selector: "app-note-banci-report",
    templateUrl: "./note-banci-report.component.html"
})
export class NoteBanciReportComponent implements OnInit {
    
    private readonly EMPTY_REPORT: NoteBanciReportModel = new NoteBanciReportModel();

	@ViewChild(Table)
    public dataTable: Table;  
	public columns: Column[] = [];	

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

    public report: NoteBanciReportModel;
    public rowGroupMetadata: any;

    public invitatARBSelectItems: SelectItem[];

    public scrollHeight: string;

    public constructor(reportService: ReportService, nomenclatorService: NomenclatorService, formBuilder: FormBuilder, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
        
        this.messageDisplayer = messageDisplayer;
        this.reportService = reportService;
        this.nomenclatorService = nomenclatorService;
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
        this.scrollHeight = (window.innerHeight - 300) + "px";
        this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("banca", this.translateUtils.translateLabel("REPORT_NOTE_BANCI_BANCA")));
        this.columns.push(this.buildColumn("cod", this.translateUtils.translateLabel("REPORT_NOTE_BANCI_COD")));
        this.columns.push(this.buildColumn("level0", this.translateUtils.translateLabel("REPORT_NOTE_BANCI_LEVEL_0")));
        this.columns.push(this.buildColumn("level1", this.translateUtils.translateLabel("REPORT_NOTE_BANCI_LEVEL_1")));
        this.columns.push(this.buildColumn("level2", this.translateUtils.translateLabel("REPORT_NOTE_BANCI_LEVEL_2")));
        this.columns.push(this.buildColumn("level3", this.translateUtils.translateLabel("REPORT_NOTE_BANCI_LEVEL_3")));
        this.columns.push(this.buildColumn("level3Plus", this.translateUtils.translateLabel("REPORT_NOTE_BANCI_LEVEL_3_PLUS")));
        this.columns.push(this.buildColumn("inAfaraNom", this.translateUtils.translateLabel("REPORT_NOTE_BANCI_IN_AFARA_NOMNINALIZARILOR")));
        this.columns.push(this.buildColumn("totalPrezenta", this.translateUtils.translateLabel("REPORT_NOTE_BANCI_TOTAL_PREZENTA")));
        this.columns.push(this.buildColumn("coeficientStructural", this.translateUtils.translateLabel("REPORT_NOTE_BANCI_COEFICIENT_STRUCTURAL")));
        this.columns.push(this.buildColumn("notaFinalaPrezenta", this.translateUtils.translateLabel("REPORT_NOTE_BANCI_NOTA_FINALA_PREZENTA")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }

    public ngOnInit(): void {
        this.formGroup.addControl("banca", new FormControl());
        this.formGroup.addControl("dataSedintaDeLa", new FormControl(null, [Validators.required]));
        this.formGroup.addControl("dataSedintaPanaLa", new FormControl(null, [Validators.required]));

        this.nomenclatorService.getNomenclatorByCode(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII, {
            onSuccess: (nomenclator: NomenclatorModel): void => {
                this.bancaFormControl.setValue(new ValueOfNomenclatorValueField(nomenclator.id));

            },
            onFailure: (error: AppError): void => {
                this.messageDisplayer.displayAppError(error);
            }

        });
       
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

        let reportFilter: NoteBanciReportFilterModel = this.prepareFilterModel();

        this.reportService.getNoteBanciReport(reportFilter, {
            onSuccess: (theReport: NoteBanciReportModel): void => {

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

    public prepareFilterModel(): NoteBanciReportFilterModel {
        let filter: NoteBanciReportFilterModel = new NoteBanciReportFilterModel();

        if (ObjectUtils.isNotNullOrUndefined(this.bancaFormControl.value.value)) {
            filter.institutieId = this.bancaFormControl.value.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.dataSedintaDeLaFormControl.value)) {
            filter.dataInceputSedintaDeLa = this.dataSedintaDeLaFormControl.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.dataSedintaPanaLaFormControl.value)) {
            filter.dataInceputSedintaPanaLa = this.dataSedintaPanaLaFormControl.value;
        }

        return filter;
    }

    public onExportExcel(): void {
        alert("not implemented yet");
    }

    public get footerVisible(): boolean {
        return ObjectUtils.isNotNullOrUndefined(this.report.rows);
    }

    public get bancaFormControl(): FormControl {
        return this.getFormControlByName("banca");
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

	public exportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.dataTable)) {
			return;
		}
		this.dataTable.exportFunction = (exportCell: any) => {
			exportCell.data = ExportUtils.replaceQuotationMarkIfStringData( exportCell.data);
		    if (exportCell.field === "coeficientStructural"
                || exportCell.field === "notaFinalaPrezenta" ) {
                return exportCell.data.toFixed(2);
            } else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_NOTE_BANCI");
		this.dataTable.exportCSV();
	}
	
}