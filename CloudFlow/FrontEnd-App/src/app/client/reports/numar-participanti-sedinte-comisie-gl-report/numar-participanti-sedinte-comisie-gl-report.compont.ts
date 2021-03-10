import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DocumentTypeModel, DateConstants, DateUtils, FormUtils, NumarParticipantiSedinteComisieGlReportModel, NumarParticipantiSedinteComisieGlReportFilterModel, OrganizationService, NomenclatorService, NomenclatorValueModel, NomenclatorConstants, UserModel, GroupConstants, DocumentTypeService, DocumentConstants, ListMetadataItemModel, NumarParticipantiSedinteComisieGlReportBundle, StringUtils, TranslateUtils } from "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from "@app/shared";
import { ReportService, MessageDisplayer } from "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-numar-participanti-sedinte-comisie-gl-report",
	templateUrl: "./numar-participanti-sedinte-comisie-gl-report.compont.html"
})
export class NumarParticipantiSedinteComisieGlReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new NumarParticipantiSedinteComisieGlReportModel();

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
	public bancaSelectItems: SelectItem[];
	public calitateMembruSelectItems: SelectItem[];
	public responsabilSelectItems: SelectItem[];

	public dateFormat: string;
	public yearRange: string;

	public report: NumarParticipantiSedinteComisieGlReportModel;

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

	private prepareInstitutieSelectItems(): void {
        this.bancaSelectItems = [];
        
        this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII, {
            onSuccess: (banci: NomenclatorValueModel[]): void => {
                banci.forEach(banca => {
                    let id:number = banca.id;
                    let nume:string = banca[NomenclatorConstants.INSTITUTII_ATTR_KEY_DENUMIRE_INSTITUTIE];
              
                    this.bancaSelectItems.push({ label: nume, value: id });
                });
		
				ListItemUtils.sortByLabel(this.bancaSelectItems);
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

	private prepareResponsabilSelectItems(): void {
        this.responsabilSelectItems = [];
        
        this.organizationService.getUsersFromGroupByGroupName(GroupConstants.GROUP_NAME_RESPONSABIL_GL_COMISIE, {
            onSuccess: (responsabili: UserModel[]): void => {
                responsabili.forEach(responsabil => {
                    let id:number = Number.parseInt(responsabil.userId);
                    let nume:string = responsabil.displayName;
              
                    this.responsabilSelectItems.push({ label: nume, value: id });
                });
			
				ListItemUtils.sortByLabel(this.responsabilSelectItems);
            },
            onFailure: (appError: AppError): void => {
                this.messageDisplayer.displayAppError(appError);
            }
		});
	}

	private prepareCalitateMembruSelectItems(): void {
		this.calitateMembruSelectItems = [];

		this.reportService.getNumarParticipantiSedinteComisieGlReportBundle({
			onSuccess: (numarParticipantiSedinteComisieGlReportBundle: NumarParticipantiSedinteComisieGlReportBundle): void => {
				let calitateMembruItems = numarParticipantiSedinteComisieGlReportBundle.calitateMembruItems;
			
				calitateMembruItems.forEach(calitateMembruItem => {
                    let label:string = calitateMembruItem.label;
                    let value:string = calitateMembruItem.value;
              
                    this.calitateMembruSelectItems.push({ label: label, value: value });
                });
			
				ListItemUtils.sortByLabel(this.calitateMembruSelectItems);
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private init(): void {
		this.loading = false;
		this.reportVisible = false;
		this.report = NumarParticipantiSedinteComisieGlReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.prepareInstitutieSelectItems();
		this.prepareComisieSelectItems();
		this.prepareResponsabilSelectItems();
		this.prepareCalitateMembruSelectItems();
		this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("numeComisie", this.translateUtils.translateLabel("REPORT_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL_NUME_COMISIE")));
        this.columns.push(this.buildColumn("dataSedinta", this.translateUtils.translateLabel("REPORT_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL_DATA_SEDINTA")));
        this.columns.push(this.buildColumn("numeBanca", this.translateUtils.translateLabel("REPORT_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL_NUME_BANCA")));
        this.columns.push(this.buildColumn("participanti", this.translateUtils.translateLabel("REPORT_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL_PARTICIPANTI")));
        this.columns.push(this.buildColumn("functie", this.translateUtils.translateLabel("REPORT_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL_FUNCTIE")));
        this.columns.push(this.buildColumn("departament", this.translateUtils.translateLabel("REPORT_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL_DEPARTAMENT")));
        this.columns.push(this.buildColumn("calitateMembru", this.translateUtils.translateLabel("REPORT_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL_CALITATE_MEMBRU")));
        this.columns.push(this.buildColumn("responsabilComisie", this.translateUtils.translateLabel("REPORT_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL_RESPONSABIL_COMISIE")));
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
		this.formGroup.addControl("banca", new FormControl());
		this.formGroup.addControl("functie", new FormControl());
		this.formGroup.addControl("departament", new FormControl());
		this.formGroup.addControl("calitateMembru", new FormControl());
		this.formGroup.addControl("responsabil", new FormControl());
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
		let reportFilter: NumarParticipantiSedinteComisieGlReportFilterModel = this.prepareFilterModel();
		
		this.reportService.getNumarParticipantiSedinteComisieGlReport(reportFilter, {
			onSuccess: (theReport: NumarParticipantiSedinteComisieGlReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = NumarParticipantiSedinteComisieGlReportComponent.EMPTY_REPORT;
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

	private prepareFilterModel(): NumarParticipantiSedinteComisieGlReportFilterModel {
		let filter: NumarParticipantiSedinteComisieGlReportFilterModel = new NumarParticipantiSedinteComisieGlReportFilterModel();
	
		filter.dataSedintaDeLa = this.dataSedintaDeLaFormControl.value;
		filter.dataSedintaPanaLa = this.dataSedintaPanaLaFormControl.value;
		filter.comisieId = this.comisieFormControl.value;
		filter.bancaId = this.bancaFormControl.value;
		if (StringUtils.isNotBlank(this.functieFormControl.value)) {
			filter.functie = this.functieFormControl.value;
		} 
		if (StringUtils.isNotBlank(this.departamentFormControl.value)) {
			filter.departament = this.departamentFormControl.value;
		} 
		filter.calitateMembru = this.calitateMembruFormControl.value;
		filter.responsabilId = this.responsabilFormControl.value;

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

	public get bancaFormControl(): FormControl {
		return this.getFormControlByName("banca");
	}
	
	public get functieFormControl(): FormControl {
		return this.getFormControlByName("functie");
	}

	public get departamentFormControl(): FormControl {
		return this.getFormControlByName("departament");
	}

	public get calitateMembruFormControl(): FormControl {
		return this.getFormControlByName("calitateMembru");
	}

	public get responsabilFormControl(): FormControl {
		return this.getFormControlByName("responsabil");
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
			if (exportCell.field === "dataSedinta" ) {
				return DateUtils.formatForDisplay(exportCell.data);
			} else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL");
		this.dataTable.exportCSV();
	}

}