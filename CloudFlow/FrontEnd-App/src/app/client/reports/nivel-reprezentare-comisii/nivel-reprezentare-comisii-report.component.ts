import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { AppError, DateConstants, DateUtils, FormUtils, MessageDisplayer, NomenclatorConstants, NomenclatorModel, NomenclatorService, ObjectUtils, ReportService, ValueOfNomenclatorValueField, TranslateUtils } from "@app/shared";
import { NivelReprezentareComisiiReportFilterModel } from "@app/shared/model/reports/nivel-reprezentare-comisii-report-filter.model";
import { NivelReprezentareComisiiReportModel } from "@app/shared/model/reports/nivel-reprezentare-comisii-report.model";
import { SelectItem } from "primeng/api";
import { Table } from "primeng/table";
import { Column } from "primeng/primeng";
import { ExportUtils } from "@app/shared/utils/export-utils";

@Component({
    selector: "app-nivel-reprezentare-comisiireport",
    templateUrl: "./nivel-reprezentare-comisii-report.component.html"
})
export class NivelReprezentareComisiiReportComponent implements OnInit {
    
    private readonly EMPTY_REPORT: NivelReprezentareComisiiReportModel = new NivelReprezentareComisiiReportModel();

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

    public report: NivelReprezentareComisiiReportModel;
    public rowGroupMetadata: any;

    public invitatARBSelectItems: SelectItem[];

    public scrollHeight: string;

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
        this.scrollHeight = (window.innerHeight - 220) + "px";
        this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("institutia", this.translateUtils.translateLabel("REPORT_NIVEL_REPREZENTARE_COMISII_INSTITUTIE")));
        this.columns.push(this.buildColumn("nrPresedintiComisie", this.translateUtils.translateLabel("REPORT_NIVEL_REPREZENTARE_COMISII_PRESEDINTI_COMISIE")));
        this.columns.push(this.buildColumn("nrVicepresedintiComisie", this.translateUtils.translateLabel("REPORT_NIVEL_REPREZENTARE_COMISII_VICEPRESEDINTI_COMISIE")));
        this.columns.push(this.buildColumn("nrReprezOrganismeInterne", this.translateUtils.translateLabel("REPORT_NIVEL_REPREZENTARE_COMISII_REPREZENTARE_ORGANISME_INTERNE")));
        this.columns.push(this.buildColumn("nrReprezOrganismeInternationale", this.translateUtils.translateLabel("REPORT_NIVEL_REPREZENTARE_COMISII_REPREZENTARE_ORGANISME_INTERNATIONALE")));
        this.columns.push(this.buildColumn("totalReprezentare", this.translateUtils.translateLabel("REPORT_NIVEL_REPREZENTARE_COMISII_TOTAL_REPREZENTARE")));
        this.columns.push(this.buildColumn("procentReprezentare", this.translateUtils.translateLabel("REPORT_NIVEL_REPREZENTARE_COMISII_PROCENT_REPREZENTARE")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }
	

    public ngOnInit(): void {
        this.formGroup.addControl("institutie", new FormControl());
        this.formGroup.addControl("dataExpirareMandatDeLa", new FormControl());
        this.formGroup.addControl("dataExpirareMandatPanaLa", new FormControl());

        this.nomenclatorService.getNomenclatorByCode(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII, {
            onSuccess: (nomenclator: NomenclatorModel): void => {
                this.institutieFormControl.setValue(new ValueOfNomenclatorValueField(nomenclator.id));

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

        let reportFilter: NivelReprezentareComisiiReportFilterModel = this.prepareFilterModel();

        this.reportService.getNivelReprezentareComisiiReport(reportFilter, {
            onSuccess: (report: NivelReprezentareComisiiReportModel): void => {

                this.report = report;

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

    public prepareFilterModel(): NivelReprezentareComisiiReportFilterModel {
        let filter: NivelReprezentareComisiiReportFilterModel = new NivelReprezentareComisiiReportFilterModel();

        if (ObjectUtils.isNotNullOrUndefined(this.institutieFormControl.value.value)) {
            filter.institutieId = this.institutieFormControl.value.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.dataExpirareMandatDeLaFormControl.value)) {
            DateUtils
            filter.dataExpirareMandatDeLa = this.dataExpirareMandatDeLaFormControl.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.dataExpirareMandatPanaLaFormControl.value)) {
            filter.dataExpirareMandatPanaLa = this.dataExpirareMandatPanaLaFormControl.value;
        }

        return filter;
    }

    public onExportExcel(): void {
        alert("not implemented yet");
    }

    public get footerVisible(): boolean {
        return ObjectUtils.isNotNullOrUndefined(this.report.rows) || this.report.rows == [];
    }

    public get institutieFormControl(): FormControl {
        return this.getFormControlByName("institutie");
    }

    public get dataExpirareMandatDeLaFormControl(): FormControl {
        return this.getFormControlByName("dataExpirareMandatDeLa");
    }

    public get dataExpirareMandatPanaLaFormControl(): FormControl {
        return this.getFormControlByName("dataExpirareMandatPanaLa");
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
			if (exportCell.field === "procentReprezentare" ) {
                return exportCell.data.toFixed(2);
            } else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_NIVEL_REPREZENTARE_COMISII");
		this.dataTable.exportCSV();
	}
	
}