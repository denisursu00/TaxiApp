import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { AppError, DateConstants, DateUtils, FormUtils, MessageDisplayer, NomenclatorConstants, NomenclatorModel, NomenclatorService, ObjectUtils, ReportService, ValueOfNomenclatorValueField, TranslateUtils } from "@app/shared";
import { NotaGeneralaReportFilterModel } from "@app/shared/model/reports/nota-generala-report-filter.model";
import { NotaGeneralaReportRowModel } from "@app/shared/model/reports/nota-generala-report-row.model";
import { SelectItem } from "primeng/api";
import { Table } from "primeng/table";
import { Column } from "primeng/primeng";
import { ExportUtils } from "@app/shared/utils/export-utils";

@Component({
    selector: "app-nota-generala-report",
    templateUrl: "./nota-generala-report.component.html"
})
export class NotaGeneralaReportComponent implements OnInit {
    
    private readonly EMPTY_REPORT: NotaGeneralaReportRowModel[] = [];

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

    public reportRows: NotaGeneralaReportRowModel[];
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
        this.reportRows = this.EMPTY_REPORT;
        this.scrollHeight = (window.innerHeight - 180) + "px";
        this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("banca", this.translateUtils.translateLabel("REPORT_NOTA_GENERALA_BANCA")));
        this.columns.push(this.buildColumn("notaFinalaBanca", this.translateUtils.translateLabel("REPORT_NOTA_GENERALA_NOTA_FINALA_BANCA")));
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

        let reportFilter: NotaGeneralaReportFilterModel = this.prepareFilterModel();

        this.reportService.getNotaGeneralaReport(reportFilter, {
            onSuccess: (reportRows: NotaGeneralaReportRowModel[]): void => {

                this.reportRows = reportRows;

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

    public prepareFilterModel(): NotaGeneralaReportFilterModel {
        let filter: NotaGeneralaReportFilterModel = new NotaGeneralaReportFilterModel();

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
        return ObjectUtils.isNotNullOrUndefined(this.reportRows);
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
			if (exportCell.field === "notaFinalaBanca" ) {
                return exportCell.data.toFixed(2);
            } else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_NOTA_GENERALA");
		this.dataTable.exportCSV();
	}
	
}