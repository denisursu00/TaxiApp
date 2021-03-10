import { Component, OnInit, ViewChild } from "@angular/core";
import { MessageDisplayer, ReportService, DateConstants, DateUtils, AppError, FormUtils, ObjectUtils, PrezentaSedinteCdPvgInvitatiARBReportFilterModel, PrezentaSedinteCdPvgInvitatiARBReportModel, NomenclatorService, PrezentaSedinteMembriiReportModel, ValueOfNomenclatorValueField, NomenclatorConstants, NomenclatorValueModel, NomenclatorModel, TranslateUtils } from "@app/shared";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { SelectItem } from "primeng/api";
import { ReportConstants } from "../constants/report.constants";
import { PrezentaReprezentativitateReportModel } from "@app/shared/model/reports/prezenta-reprezentativitate-report.model";
import { PrezentaReprezentativitateReportFilterModel } from "@app/shared/model/reports/prezenta-reprezentativitate-report-filter.model";
import { Table } from "primeng/table";
import { Column } from "primeng/primeng";
import { ExportUtils } from "@app/shared/utils/export-utils";

@Component({
    selector: "app-prezenta-reprezentativitate-report",
    templateUrl: "./prezenta-reprezentativitate-report.component.html"
})
export class PrezentaReprezentativitateReportComponent implements OnInit {
    
    private readonly EMPTY_REPORT: PrezentaReprezentativitateReportModel = new PrezentaReprezentativitateReportModel();

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

    public report: PrezentaReprezentativitateReportModel;
    public rowGroupMetadata: any;

    public invitatARBSelectItems: SelectItem[];

    public constructor(reportService: ReportService, nomenclatorService: NomenclatorService, formBuilder: FormBuilder, 
        messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
        
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
        this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("banca", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_BANCA")));
        this.columns.push(this.buildColumn("cod", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_COD")));
        this.columns.push(this.buildColumn("level0", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_LEVEL_0")));
        this.columns.push(this.buildColumn("level1", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_LEVEL_1")));
        this.columns.push(this.buildColumn("level2", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_LEVEL_2")));
        this.columns.push(this.buildColumn("level3", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_LEVEL_3")));
        this.columns.push(this.buildColumn("level3Plus", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_LEVEL_3_PLUS")));
        this.columns.push(this.buildColumn("inAfaraNom", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_IN_AFARA_NOMNINALIZARILOR")));
        this.columns.push(this.buildColumn("totalPrezenta", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_TOTAL_PREZENTA")));
        this.columns.push(this.buildColumn("coeficientStructural", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_COEFICIENT_STRUCTURAL")));
        this.columns.push(this.buildColumn("notaFinalaPrezenta", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_NOTA_FINALA_PREZENTA")));
        this.columns.push(this.buildColumn("raspunsuriBanci.denumireBanca", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_BANCA")));
        this.columns.push(this.buildColumn("raspunsuriBanci.nrRaspunsuriFaraPropuneri", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_RASPUNSURI_FARA_PROPUNERI")));
        this.columns.push(this.buildColumn("raspunsuriBanci.nrRaspunsuriCuPropuneri", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_RASPUNSURI_CU_PROPUNERI")));
        this.columns.push(this.buildColumn("raspunsuriBanci.nrTotalRaspunsuriPropuneri", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_TOTAL_RASPUNSURI")));
        this.columns.push(this.buildColumn("raspunsuriBanci.coeficientStructuralCalitateRaspunsuri", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_COEFICIENT_STRUCTURAL_CALITATE_RASP")));
        this.columns.push(this.buildColumn("raspunsuriBanci.notaRaspunsuriAjustataCuCalitateaRaspunsurilor", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_NOTA_RASPUNSURI_ADJUSTATA_CU_CALITATEA_RASP")));
        this.columns.push(this.buildColumn("raspunsuriBanci.nrRaspunsuriFaraIntarziere", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_RASPUNSURI_FARA_INTARZIERE")));
        this.columns.push(this.buildColumn("raspunsuriBanci.nrRaspunsuriCuIntarzierePesteOZi", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_RASPUNSURI_CU_INTARZIERE_PESTE_O_ZI")));
        this.columns.push(this.buildColumn("raspunsuriBanci.nrTotalRaspunsuriTermen", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_TOTAL_RASPUNSURI")));
        this.columns.push(this.buildColumn("raspunsuriBanci.coeficientStructuralVitezaRaspunsuri", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_COEFICIENT_STRUCTURAL_VITEZA_RASP")));
        this.columns.push(this.buildColumn("raspunsuriBanci.notaRaspunsuriAjustataCuVitezaRaspunsurilor", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_NOTA_RASPUNSURI_ADJUSTATA_CU_VITEZA_RASP")));
        this.columns.push(this.buildColumn("raspunsuriBanci.notaTotalaRaspunsuriBanci", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_NOTA_TOTALA_RASP_BANCI")));
        this.columns.push(this.buildColumn("notaFinalaRaspunsuriBanci", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_NOTA_FINALA_RASP_BANCI")));
        this.columns.push(this.buildColumn("banca", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_BANCA")));
        this.columns.push(this.buildColumn("notaFinalaBanca", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_NOTA_FINALA_BANCA")));
        this.columns.push(this.buildColumn("rankNotaBanca", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE_RANK_NOTA_BANCA")));
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

        let reportFilter: PrezentaReprezentativitateReportFilterModel = this.prepareFilterModel();

        this.reportService.getPrezentaComisiiArbReprezentativitate(reportFilter, {
            onSuccess: (theReport: PrezentaReprezentativitateReportModel): void => {

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

    public prepareFilterModel(): PrezentaReprezentativitateReportFilterModel {
        let filter: PrezentaReprezentativitateReportFilterModel = new PrezentaReprezentativitateReportFilterModel();

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
                || exportCell.field === "notaFinalaPrezenta"
                || exportCell.field === "raspunsuriBanci.coeficientStructuralCalitateRaspunsuri"
                || exportCell.field === "raspunsuriBanci.notaRaspunsuriAjustataCuCalitateaRaspunsurilor"
                || exportCell.field === "raspunsuriBanci.coeficientStructuralVitezaRaspunsuri"
                || exportCell.field === "raspunsuriBanci.notaRaspunsuriAjustataCuVitezaRaspunsurilor"
                || exportCell.field === "raspunsuriBanci.notaTotalaRaspunsuriBanci"
                || exportCell.field === "notaFinalaRaspunsuriBanci"
                || exportCell.field === "notaFinalaBanca" ) {
                return exportCell.data.toFixed(2);
            } else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE");
		this.dataTable.exportCSV();
	}
	
}