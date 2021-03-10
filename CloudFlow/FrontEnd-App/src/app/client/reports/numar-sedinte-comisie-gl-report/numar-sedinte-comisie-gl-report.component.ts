import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DocumentTypeModel, DateConstants, DateUtils, FormUtils, NomenclatorConstants, NomenclatorService, JoinedNomenclatorUiAttributesValueModel, DecontCheltuieliAlteDeconturiReportModel, DecontCheltuieliAlteDeconturiReportFilterModel, AlteDeconturiService, DocumentTypeService, NomenclatorValueModel, DocumentConstants, NumarSedinteComisieGlReportModel, NumarSedinteComisieGlReportFilterModel, DocumentIdentifierModel } from "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils, TranslateUtils } from "@app/shared";
import { ReportService, MessageDisplayer } from "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-numar-sedinte-comisie-gl-report",
	templateUrl: "./numar-sedinte-comisie-gl-report.component.html"
})
export class NumarSedinteComisieGlReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new NumarSedinteComisieGlReportModel();

	@ViewChild(Table)
    public dataTable: Table;  
	public columns: Column[] = [];	

	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private confirmationUtils: ConfirmationUtils;
	private reportService: ReportService;
	private documentTypeService: DocumentTypeService;
	private nomenclatorService: NomenclatorService;
	
	public reportVisible: boolean;
	public loading: boolean;
	public filtruDataDecontDisabled: boolean;
	public titularSelected: boolean;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public comisieSelectItems: SelectItem[];
	public dateFormat: string;
	public yearRange: string;

	public mode: string = "single";
	public documentTypeId: number;

	public report: NumarSedinteComisieGlReportModel;

	public scrollHeight: string;

	public constructor(reportsService: ReportService, formBuilder: FormBuilder,
			messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils, confirmationUtils: ConfirmationUtils,
			nomenclatorService: NomenclatorService, documentTypeService: DocumentTypeService) {
		this.reportService = reportsService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.confirmationUtils = confirmationUtils;	
		this.nomenclatorService = nomenclatorService;
		this.documentTypeService = documentTypeService;	
		this.init();
	}

	private prepareComisieSelectItems(): void {
        this.comisieSelectItems = [];
        
        this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_COMISII_SAU_GL, {
            onSuccess: (comisii: NomenclatorValueModel[]): void => {
                comisii.forEach(comisie => {
                    let id:number = comisie.id;
                    let nume:string = comisie[NomenclatorConstants.COMISII_SAU_GL_ATTR_KEY_DENUMIRE];
              
                    this.comisieSelectItems.push({ label: nume, value: id });
                });
		
				ListItemUtils.sortByLabel(this.comisieSelectItems);
            },
            onFailure: (appError: AppError): void => {
                this.messageDisplayer.displayAppError(appError);
            }
        });
	}

	private prepareDocumentTypeId(): void {
        this.documentTypeService.getDocumentTypeIdByName(DocumentConstants.DOCUMENT_TYPE_NAME_PREZENTA_GOMISII_GL, {
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
		this.filtruDataDecontDisabled = true;
		this.titularSelected = false;
		this.report = NumarSedinteComisieGlReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 250) + "px";
		this.prepareComisieSelectItems();
		this.prepareDocumentTypeId();
	    this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("numeComisieGl", this.translateUtils.translateLabel("RAPORT_SEDINTE_COMISIE_GL_NUME_COMISIE_GL")));
        this.columns.push(this.buildColumn("dataSedinta", this.translateUtils.translateLabel("RAPORT_SEDINTE_COMISIE_GL_DATA_SEDINTA")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }
	
	public ngOnInit(): void {
		this.formGroup.addControl("dataSedintaDeLa", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("dataSedintaPanaLa", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("comisie", new FormControl());
		this.formGroup.addControl("document", new FormControl());
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
		let reportFilter: NumarSedinteComisieGlReportFilterModel = this.prepareFilterModel();
		this.reportService.getNumarSedinteComisieGlReport(reportFilter, {
			onSuccess: (theReport: NumarSedinteComisieGlReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = NumarSedinteComisieGlReportComponent.EMPTY_REPORT;
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

	private prepareFilterModel(): NumarSedinteComisieGlReportFilterModel {
		let filter: NumarSedinteComisieGlReportFilterModel = new NumarSedinteComisieGlReportFilterModel();
		
		filter.dataSedintaDeLa = this.dataSedintaDeLaFormControl.value;
		filter.dataSedintaPanaLa = this.dataSedintaPanaLaFormControl.value;
		filter.comisieId = this.comisieFormControl.value;
		if (ObjectUtils.isNotNullOrUndefined(this.documentFormControl.value)) {
			let document: DocumentIdentifierModel = new DocumentIdentifierModel();

			document.documentId = this.documentFormControl.value[0].value.documentId;
			document.documentLocationRealName = this.documentFormControl.value[0].value.documentLocationRealName;
			
			filter.document = document;
		}
		
		return filter;
	}

	public onExportExcel(): void {
		alert("not implemented yet");
	}

	public get footerVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.report) && ArrayUtils.isNotEmpty(this.report.rows);
	}

	public get dataSedintaDeLaFormControl(): FormControl {
		return this.getFormControlByName("dataSedintaDeLa");
	}

	public get dataSedintaPanaLaFormControl(): FormControl {
		return this.getFormControlByName("dataSedintaPanaLa");
	}

	public get comisieFormControl(): FormControl {
		return this.getFormControlByName("comisie");
	}

	public get documentFormControl(): FormControl {
		return this.getFormControlByName("document");
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
			if (exportCell.field === "dataSedinta" ) {
				return DateUtils.formatForDisplay(exportCell.data);
			}  else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.RAPORT_SEDINTE_COMISIE_GL");
		this.dataTable.exportCSV();
	}
}