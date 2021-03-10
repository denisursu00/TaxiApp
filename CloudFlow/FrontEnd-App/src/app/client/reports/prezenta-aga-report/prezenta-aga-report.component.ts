import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DateConstants, DateUtils, FormUtils, NomenclatorService, NomenclatorConstants, NomenclatorValueModel, PrezentaAgaReportFilterModel, PrezentaAgaReportModel, DocumentTypeService, DocumentConstants, StringUtils, TranslateUtils } from "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from "@app/shared";
import { ReportService, MessageDisplayer } from "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-prezenta-aga-report",
	templateUrl: "./prezenta-aga-report.component.html"
})
export class PrezentaAgaReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new PrezentaAgaReportModel();

	@ViewChild(Table)
    public dataTable: Table;  
	public columns: Column[] = [];	

	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;
    private reportService: ReportService;
    private nomenclatorService: NomenclatorService;
	private documentTypeService: DocumentTypeService;
	private translateUtils: TranslateUtils;
	
	public reportVisible: boolean;
	public loading: boolean;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public bancaSelectItems: SelectItem[];
	public dateFormat: string;
	public yearRange: string;

	public report: PrezentaAgaReportModel;

	public mode: string = "single";
	public documentTypeId: number;

	public scrollHeight: string;

    public constructor(reportsService: ReportService, formBuilder: FormBuilder, messageDisplayer: MessageDisplayer, 
			confirmationUtils: ConfirmationUtils, nomenclatorService: NomenclatorService, 
			documentTypeService: DocumentTypeService, translateUtils: TranslateUtils) {
		this.reportService = reportsService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
        this.confirmationUtils = confirmationUtils;	
		this.nomenclatorService = nomenclatorService;	
		this.documentTypeService = documentTypeService;
		this.translateUtils = translateUtils;
		this.init();
	}

	private prepareBancaSelectItems(): void {
        this.bancaSelectItems = [];
        this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII, {
            onSuccess: (institutii: NomenclatorValueModel[]): void => { 
                let selectItems: SelectItem[] = [];
                institutii.forEach(institutie => {
                    let selectItem: SelectItem = {
						value: institutie["id"],
						label: institutie[NomenclatorConstants.INSITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE]
					};
					selectItems.push(selectItem);
                });
				this.bancaSelectItems = selectItems;       
				
				ListItemUtils.sortByLabel(this.bancaSelectItems);
            },
            onFailure: (appError: AppError) => {
                this.messageDisplayer.displayAppError(appError);
            }
		});	
	}

	private prepareDocumentTypeIdSelectItems(): void {
        this.documentTypeService.getDocumentTypeIdByName(DocumentConstants.DOCUMENT_TYPE_NAME_PREZENTA_AGA, {
            onSuccess: (typeId: number): void => { 
			   this.documentTypeId = typeId;			                   
            },
            onFailure: (appError: AppError) => {
                this.messageDisplayer.displayAppError(appError);
            }
		});	
	}

	private init(): void {
		this.loading = false;
		this.reportVisible = false;
		this.report = PrezentaAgaReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 250) + "px";
		this.prepareBancaSelectItems();
		this.prepareDocumentTypeIdSelectItems();		
	    this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("banca", this.translateUtils.translateLabel("REPORT_PREZENTA_AGA_BANCA")));
        this.columns.push(this.buildColumn("confirmare", this.translateUtils.translateLabel("REPORT_PREZENTA_AGA_CONFIRMARE")));
        this.columns.push(this.buildColumn("functie", this.translateUtils.translateLabel("REPORT_PREZENTA_AGA_FUNCTIE")));
        this.columns.push(this.buildColumn("necesitaImputernicire", this.translateUtils.translateLabel("REPORT_PREZENTA_AGA_NECESITA_IMPUTERNICIRE")));
        this.columns.push(this.buildColumn("prezentaMembriNivelPresedinte", this.translateUtils.translateLabel("REPORT_PREZENTA_AGA_PREZENTA_MEMBRI_NIVEL_PRESEDINTE")));
        this.columns.push(this.buildColumn("prezentaMembriNivelVicepresedinte", this.translateUtils.translateLabel("REPORT_PREZENTA_AGA_PREZENTA_MEMBRI_NIVEL_VICEPRESEDINTE")));
        this.columns.push(this.buildColumn("prezentaMembriNivelImputernicit", this.translateUtils.translateLabel("REPORT_PREZENTA_AGA_PREZENTA_MEMBRI_NIVEL_IMPUTERNICIT")));
        this.columns.push(this.buildColumn("totalPrezenta", this.translateUtils.translateLabel("REPORT_PREZENTA_AGA_TOTAL_PREZENTA")));
        this.columns.push(this.buildColumn("membriVotanti", this.translateUtils.translateLabel("REPORT_PREZENTA_AGA_MEMBRI_VOTANTI")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }
	
	
	public ngOnInit(): void {
		this.formGroup.addControl("document", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("banca", new FormControl());
		this.formGroup.addControl("functie", new FormControl());
		this.formGroup.addControl("deLaDataInceput", new FormControl());
		this.formGroup.addControl("panaLaDataInceput", new FormControl());
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
        let reportFilter: PrezentaAgaReportFilterModel = this.prepareFilterModel();
 
		this.reportService.getPrezentaAgaReport(reportFilter, {
			onSuccess: (theReport: PrezentaAgaReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = PrezentaAgaReportComponent.EMPTY_REPORT;
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

	private prepareFilterModel(): PrezentaAgaReportFilterModel {
		
		let filter: PrezentaAgaReportFilterModel = new PrezentaAgaReportFilterModel();
		if (ObjectUtils.isNotNullOrUndefined(this.documentFormControl.value)) {
			filter.documentId = this.documentFormControl.value[0].value.documentId;
		}
		filter.bancaId = this.bancaFormControl.value;
		if (StringUtils.isNotBlank(this.functieFormControl.value)) {
			filter.functie = this.functieFormControl.value;
		} 

		if (ObjectUtils.isNotNullOrUndefined(this.deLaDataInceputFormControl.value)) {
            filter.deLaDataInceput = this.deLaDataInceputFormControl.value;
        }
        if (ObjectUtils.isNotNullOrUndefined(this.panaLaDataInceputFormControl.value)) {
            filter.panaLaDataInceput = this.panaLaDataInceputFormControl.value;
        }
		return filter;
	}

	public onExportExcel(): void {
		alert("not implemented yet");
	}

	public get footerVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.report) && ArrayUtils.isNotEmpty(this.report.rows);
	}

	public get documentFormControl(): FormControl {
		return this.getFormControlByName("document");
	}

	public get bancaFormControl(): FormControl {
		return this.getFormControlByName("banca");
	}

	public get functieFormControl(): FormControl {
		return this.getFormControlByName("functie");
	}

	public get deLaDataInceputFormControl(): FormControl {
		return this.getFormControlByName("deLaDataInceput");
	}

	public get panaLaDataInceputFormControl(): FormControl {
		return this.getFormControlByName("panaLaDataInceput");
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
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_AGA");
		this.dataTable.exportCSV();
	}
	
}
