import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DocumentTypeModel, DateConstants, DateUtils, FormUtils, NomenclatorConstants, NomenclatorValueModel, NomenclatorService, OrganizationService, GroupConstants, UserModel, DocumentTypeService, DocumentConstants, MembriiAfiliatiReportModel, MembriiAfiliatiReportFilterModel, DocumentIdentifierModel, StringUtils, GetNomenclatorValuesRequestModel, NomenclatorFilter, NomenclatorSimpleFilter, TranslateUtils } from  "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from  "@app/shared";
import { ReportService, MessageDisplayer } from  "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { Table } from "primeng/table";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-membrii-afiliati-report",
	templateUrl: "./membrii-afiliati-report.componente.html"
})
export class MembriiAfiliatiReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new MembriiAfiliatiReportModel();

	@ViewChild(Table)
    public dataTable: Table;  
	public columns: Column[] = [];	

	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;
	private reportService: ReportService;
	private nomenclatorService: NomenclatorService;
    private organizationService: OrganizationService;
	private documentTypeService: DocumentTypeService;
	private translateUtils: TranslateUtils;
	
	public reportVisible: boolean;
	public loading: boolean;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public institutieSelectItems: SelectItem[];
	public reprezentantAcreditatSelectItems: SelectItem[];
	public comisieSelectItems: SelectItem[];
	public dateFormat: string;
	public yearRange: string;

	public report: MembriiAfiliatiReportModel;
	public isReprezentantAcreditat: boolean = true;

	public mode: string = "single";
	public documentTypeId: number;

	public scrollHeight: string;

	public constructor(reportsService: ReportService, formBuilder: FormBuilder,
			messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils, nomenclatorService: NomenclatorService, 
			organizationService: OrganizationService, documentTypeService: DocumentTypeService, translateUtils: TranslateUtils) {
		this.reportService = reportsService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;		
		this.nomenclatorService = nomenclatorService;
		this.organizationService = organizationService;
		this.documentTypeService = documentTypeService;
		this.translateUtils = translateUtils;
		this.init();
	}

	private prepareInstitutieSelectItems(): void {
		this.institutieSelectItems = [];
		let filterTipInstitutii: GetNomenclatorValuesRequestModel = this.buildGetNomenclatorValuesRequestModel(NomenclatorConstants.NOMENCLATOR_CODE_TIP_INSTITUTII,
			NomenclatorConstants.TIP_INSTITUTII_ATTR_KEY_TIP, NomenclatorConstants.TIP_INSTITUTII_TIP_MEMBRU_AFILIAT);
		this.nomenclatorService.getNomenclatorValues(filterTipInstitutii, {
            onSuccess: (tipInstitutii: NomenclatorValueModel[]): void => { 
				let tipInstitutieId: string = tipInstitutii[0].id.toString();
				
				let filterInstitutii: GetNomenclatorValuesRequestModel = this.buildGetNomenclatorValuesRequestModel(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII,
					NomenclatorConstants.INSITUTII_ATTR_KEY_TIP_INSTITUTIE, tipInstitutieId);
				this.nomenclatorService.getNomenclatorValues(filterInstitutii, {
					onSuccess: (institutii: NomenclatorValueModel[]): void => { 
						
						let selectItems: SelectItem[] = [];
						institutii.forEach(institutie => {
							let id:number = institutie.id;
							let nume:string = institutie[NomenclatorConstants.INSTITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE];
							this.institutieSelectItems.push({ label: nume, value: id });
						});   
						
						ListItemUtils.sortByLabel(this.institutieSelectItems);
					},
					onFailure: (appError: AppError) => {
						this.messageDisplayer.displayAppError(appError);
					}
				});         
            },
            onFailure: (appError: AppError) => {
                this.messageDisplayer.displayAppError(appError);
            }
		});	
	}

	private buildGetNomenclatorValuesRequestModel(nomenclatorCode: string, nomenclatorKey: string, nomenclatorKeyValue: string): GetNomenclatorValuesRequestModel {
		let filter: GetNomenclatorValuesRequestModel = new GetNomenclatorValuesRequestModel();
		filter.nomenclatorCode = nomenclatorCode;
		filter.filters = this.buildNomenclatorFilter(nomenclatorKey, nomenclatorKeyValue);
		return filter;
	}

	
	private buildNomenclatorFilter(nomenclatorKey: string, nomenclatorKeyValue: string): NomenclatorFilter[] {
		let filters : NomenclatorFilter[] = [];
		
		let nomenclatorFilter: NomenclatorSimpleFilter = new NomenclatorSimpleFilter();
		nomenclatorFilter.attributeKey = nomenclatorKey;
		nomenclatorFilter.value = nomenclatorKeyValue;

		filters.push(nomenclatorFilter);
		return filters;
	}
	
	private prepareReprezentantAcreditatSelectItems(): void {
        this.reprezentantAcreditatSelectItems = [];
        
        this.organizationService.getUsersFromGroupByGroupName(GroupConstants.GROUP_NAME_RESPONSABIL_GL_COMISIE, {
            onSuccess: (users: UserModel[]): void => {
                users.forEach(user => {
                    let id:string = user.userId;
                    let nume:string = user.displayName;
              
                    this.reprezentantAcreditatSelectItems.push({ label: nume, value: id });
				});
				
				ListItemUtils.sortByLabel(this.reprezentantAcreditatSelectItems);
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
	private init(): void {
		this.loading = false;
		this.reportVisible = false;
		this.report = MembriiAfiliatiReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.prepareInstitutieSelectItems();
		this.prepareReprezentantAcreditatSelectItems();
		this.prepareComisieSelectItems();
		this.prepareDocumentTypeIdSelectItems();
		this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("institutie", this.translateUtils.translateLabel("RAPORT_MEMBRII_AFILIATI_INSTITUTIE")));
        this.columns.push(this.buildColumn("reprezentant", this.translateUtils.translateLabel("RAPORT_MEMBRII_AFILIATI_REPREZENTANT")));
        this.columns.push(this.buildColumn("comisie", this.translateUtils.translateLabel("RAPORT_MEMBRII_AFILIATI_COMISIE_GL")));
        this.columns.push(this.buildColumn("dataSedinta", this.translateUtils.translateLabel("RAPORT_MEMBRII_AFILIATI_DATA")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }
	
	public ngOnInit(): void {
        this.formGroup.addControl("institutie", new FormControl());
        this.formGroup.addControl("comisie", new FormControl());
        this.formGroup.addControl("dataSedintaDeLa", new FormControl(null, [Validators.required]));
        this.formGroup.addControl("dataSedintaPanaLa", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("reprezentantAcreditat", new FormControl(true));
		this.formGroup.addControl("numeReprezentantAcreditat", new FormControl());
		this.formGroup.addControl("prenumeReprezentantAcreditat", new FormControl());
		this.formGroup.addControl("numeReprezentantInlocuitor", new FormControl());
        this.formGroup.addControl("prenumeReprezentantInlocuitor", new FormControl());    
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
		let reportFilter: MembriiAfiliatiReportFilterModel = this.prepareFilterModel();
		this.reportService.getMembriiAfiliatiReport(reportFilter, {
			onSuccess: (theReport: MembriiAfiliatiReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = MembriiAfiliatiReportComponent.EMPTY_REPORT;
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

	private prepareFilterModel(): MembriiAfiliatiReportFilterModel {
		let filter: MembriiAfiliatiReportFilterModel = new MembriiAfiliatiReportFilterModel();
	
		if (ObjectUtils.isNotNullOrUndefined(this.documentFormControl.value)) {
			let document: DocumentIdentifierModel = new DocumentIdentifierModel();

			document.documentId = this.documentFormControl.value[0].value.documentId;
			document.documentLocationRealName = this.documentFormControl.value[0].value.documentLocationRealName;
			
			filter.document = document;
		}
		
		filter.institutieId = this.institutieFormControl.value;
		filter.dataSedintaDeLa = this.dataSedintaDeLaFormControl.value;
		filter.dataSedintaPanaLa = this.dataSedintaPanaLaFormControl.value;
		filter.comisieId = this.comisieFormControl.value;

		if (this.reprezentantAcreditatFormControl.value) {
			if (StringUtils.isNotBlank(this.numeReprezentantAcreditatFormControl.value) ) {
				filter.numeReprezentantAcreditat = this.numeReprezentantAcreditatFormControl.value;
			}
			if (StringUtils.isNotBlank(this.prenumeReprezentantAcreditatFormControl.value) ) {
				filter.prenumeReprezentantAcreditat = this.prenumeReprezentantAcreditatFormControl.value;
			}
			filter.numeReprezentantInlocuitor = null;
			filter.prenumeReprezentantInlocuitor = null;
		} else {
			filter.numeReprezentantAcreditat = null;
			filter.prenumeReprezentantAcreditat = null;
			if (StringUtils.isNotBlank(this.numeReprezentantInlocuitorFormControl.value) ) {
				filter.numeReprezentantInlocuitor = this.numeReprezentantInlocuitorFormControl.value;
			}
			if (StringUtils.isNotBlank(this.prenumeReprezentantInlocuitorFormControl.value) ) {
				filter.prenumeReprezentantInlocuitor = this.prenumeReprezentantInlocuitorFormControl.value;
			}
		}

		return filter;
	}

	public onChangeTipReprezentant(event: any): void {
		this.isReprezentantAcreditat = event;
	}

	public onExportExcel(): void {
		alert("not implemented yet");
	}

	public get footerVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.report) && ArrayUtils.isNotEmpty(this.report.rows);
	}

    public get institutieFormControl(): FormControl {
		return this.getFormControlByName("institutie");
    }
    
	public get comisieFormControl(): FormControl {
		return this.getFormControlByName("comisie");
	}

    public get dataSedintaDeLaFormControl(): FormControl {
		return this.getFormControlByName("dataSedintaDeLa");
	}

    public get dataSedintaPanaLaFormControl(): FormControl {
		return this.getFormControlByName("dataSedintaPanaLa");
	}

    public get reprezentantAcreditatFormControl(): FormControl {
		return this.getFormControlByName("reprezentantAcreditat");
	}

	public get numeReprezentantAcreditatFormControl(): FormControl {
		return this.getFormControlByName("numeReprezentantAcreditat");
	}
	public get prenumeReprezentantAcreditatFormControl(): FormControl {
		return this.getFormControlByName("prenumeReprezentantAcreditat");
	}

	public get numeReprezentantInlocuitorFormControl(): FormControl {
		return this.getFormControlByName("numeReprezentantInlocuitor");
    }
    
	public get prenumeReprezentantInlocuitorFormControl(): FormControl {
		return this.getFormControlByName("prenumeReprezentantInlocuitor");
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
			if (exportCell.field === "dataSedinta" ) {
				return DateUtils.formatForDisplay(exportCell.data);
			}  else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.RAPORT_MEMBRII_AFILIATI");
		this.dataTable.exportCSV();
	}
	
}