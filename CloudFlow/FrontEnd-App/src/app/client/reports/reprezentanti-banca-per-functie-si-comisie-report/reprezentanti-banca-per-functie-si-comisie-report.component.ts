import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DocumentTypeModel, DateConstants, DateUtils, FormUtils, NumarParticipantiSedinteComisieGlReportModel, NumarParticipantiSedinteComisieGlReportFilterModel, OrganizationService, NomenclatorService, NomenclatorValueModel, NomenclatorConstants, UserModel, GroupConstants, DocumentTypeService, DocumentConstants, ListMetadataItemModel, NumarParticipantiSedinteComisieGlReportBundle, StringUtils, ReprezentantiBancaPerFunctieSiComisieReportModel, ReprezentantiBancaPerFunctieSiComisieReportFilterModel, DocumentIdentifierModel, TranslateUtils } from "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from "@app/shared";
import { ReportService, MessageDisplayer } from "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-reprezentanti-banca-per-functie-si-comisie-report",
	templateUrl: "./reprezentanti-banca-per-functie-si-comisie-report.component.html"
})
export class ReprezentantiBancaPerFunctieSiComisieReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new ReprezentantiBancaPerFunctieSiComisieReportModel();

	@ViewChild(Table)
    public dataTable: Table;  
	public columns: Column[] = [];	

	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;
	private reportService: ReportService;
	private organizationService: OrganizationService;
	private nomenclatorService: NomenclatorService;
	private documentTypeService: DocumentTypeService;
	private translateUtils: TranslateUtils;
	
	public reportVisible: boolean;
	public loading: boolean;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public comisieSelectItems: SelectItem[];
	public institutieSelectItems: SelectItem[];

	public dateFormat: string;
	public yearRange: string;

	public mode: string = "multiple";
	public documentTypeId: number;

	public report: ReprezentantiBancaPerFunctieSiComisieReportModel;

	public scrollHeight: string;

	public constructor(reportsService: ReportService, formBuilder: FormBuilder,
			messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils, organizationService: OrganizationService, 
			nomenclatorService: NomenclatorService, documentTypeService: DocumentTypeService, translateUtils: TranslateUtils) {
		this.reportService = reportsService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;		
		this.organizationService = organizationService;
		this.nomenclatorService = nomenclatorService;
		this.documentTypeService = documentTypeService;
		this.translateUtils = translateUtils;
		this.init();
	}

	private prepareDocumentTypeIdSelectItems(): void {
        this.documentTypeService.getDocumentTypeIdByName(DocumentConstants.DOCUMENT_TYPE_NAME_PREZENTA_GOMISII_GL, {
            onSuccess: (typeId: number): void => { 
			   this.documentTypeId = typeId;                
            },
            onFailure: (appError: AppError) => {
                this.messageDisplayer.displayAppError(appError);
            }
		});	
	}

	private prepareInstitutieSelectItems(): void {
        this.institutieSelectItems = [];
        
        this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII, {
            onSuccess: (institutii: NomenclatorValueModel[]): void => {
                institutii.forEach(institutie => {
                    let id:number = institutie.id;
                    let nume:string = institutie[NomenclatorConstants.INSTITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE];
              
                    this.institutieSelectItems.push({ label: nume, value: id });
				});
				
				ListItemUtils.sortByLabel(this.institutieSelectItems);
            },
            onFailure: (appError: AppError): void => {
                this.messageDisplayer.displayAppError(appError);
            }
        });
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

	private init(): void {
		this.loading = false;
		this.reportVisible = false;
		this.report = ReprezentantiBancaPerFunctieSiComisieReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.prepareDocumentTypeIdSelectItems();
		this.prepareInstitutieSelectItems();
		this.prepareComisieSelectItems();
	    this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("numeComisie", this.translateUtils.translateLabel("RAPORT_REPREZENTANTI_BANCA_PER_FUNCTIE_SI_COMISIE_COMISIE")));
        this.columns.push(this.buildColumn("institutie", this.translateUtils.translateLabel("RAPORT_REPREZENTANTI_BANCA_PER_FUNCTIE_SI_COMISIE_INSTITUTIE")));
        this.columns.push(this.buildColumn("numeParticipant", this.translateUtils.translateLabel("RAPORT_REPREZENTANTI_BANCA_PER_FUNCTIE_SI_COMISIE_NUME_PARTICIPANT")));
        this.columns.push(this.buildColumn("functie", this.translateUtils.translateLabel("RAPORT_REPREZENTANTI_BANCA_PER_FUNCTIE_SI_COMISIE_FUNCTIE")));
        this.columns.push(this.buildColumn("email", this.translateUtils.translateLabel("RAPORT_REPREZENTANTI_BANCA_PER_FUNCTIE_SI_COMISIE_EMAIL")));
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
		this.formGroup.addControl("institutie", new FormControl());
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
		let reportFilter: ReprezentantiBancaPerFunctieSiComisieReportFilterModel = this.prepareFilterModel();
		
		this.reportService.getReprezentantiBancaPerFunctieSiComisieReport(reportFilter, {
			onSuccess: (theReport: ReprezentantiBancaPerFunctieSiComisieReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = ReprezentantiBancaPerFunctieSiComisieReportComponent.EMPTY_REPORT;
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

	private prepareFilterModel(): ReprezentantiBancaPerFunctieSiComisieReportFilterModel {
		let filter: ReprezentantiBancaPerFunctieSiComisieReportFilterModel = new ReprezentantiBancaPerFunctieSiComisieReportFilterModel();
	
		filter.dataSedintaDeLa = this.dataSedintaDeLaFormControl.value;
		filter.dataSedintaPanaLa = this.dataSedintaPanaLaFormControl.value;
		filter.comisieId = this.comisieFormControl.value;
		filter.institutieId = this.institutieFormControl.value;

		if (ObjectUtils.isNotNullOrUndefined(this.documentFormControl.value)) {
			let documents = this.documentFormControl.value;
			let documentsFilter: DocumentIdentifierModel[] = [];

			documents.forEach(document => {
				let documentFilter: DocumentIdentifierModel = new DocumentIdentifierModel();
				documentFilter.documentId = document.value.documentId;
				documentFilter.documentLocationRealName = document.value.documentLocationRealName;
				documentsFilter.push(documentFilter)
			});

			filter.documents = documentsFilter;
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

	public get institutieFormControl(): FormControl {
		return this.getFormControlByName("institutie");
	}

	public get documentFormControl(): FormControl {
		return this.getFormControlByName("document");
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
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.RAPORT_REPREZENTANTI_BANCA_PER_FUNCTIE_SI_COMISIE");
		this.dataTable.exportCSV();
	}
	
}