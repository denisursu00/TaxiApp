import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DocumentTypeModel, DateConstants, DateUtils, FormUtils, NumarParticipantiSedinteComisieGlReportModel, NumarParticipantiSedinteComisieGlReportFilterModel, OrganizationService, NomenclatorService, NomenclatorValueModel, NomenclatorConstants, UserModel, GroupConstants, DocumentTypeService, DocumentConstants, ListMetadataItemModel, NumarParticipantiSedinteComisieGlReportBundle, StringUtils, ReprezentantiBancaPerFunctieSiComisieReportModel, ReprezentantiBancaPerFunctieSiComisieReportFilterModel, CentralizatorPrezentaPerioadaReportModel, CentralizatorPrezentaPerioadaReportFilterModel, TranslateUtils } from "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from "@app/shared";
import { ReportService, MessageDisplayer } from "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-centralizator-prezenta-perioada-report",
	templateUrl: "./centralizator-prezenta-perioada-report.component.html"
})
export class CentralizatorPrezentaPerioadaReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new CentralizatorPrezentaPerioadaReportModel();
	private static readonly IN_AFARA_NOM_ID: number = -1;

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
	public levelSelectItems: SelectItem[];

	public dateFormat: string;
	public yearRange: string;

	public report: CentralizatorPrezentaPerioadaReportModel;

	public scrollHeight: string;

	public constructor(reportsService: ReportService, formBuilder: FormBuilder,
			messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils, organizationService: OrganizationService, 
			nomenclatorService: NomenclatorService, documentTypeService: DocumentTypeService,  translateUtils: TranslateUtils) {
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

	private prepareBancaSelectItems(): void {
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

	private prepareLevelSelectItems(): void {
        this.levelSelectItems = [];
        
        this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_LEVEL_FUNCTII_PERSOANE, {
            onSuccess: (levels: NomenclatorValueModel[]): void => {
                levels.forEach(level => {
                    let id:number = level.id;
                    let nume:string = level[NomenclatorConstants.LEVEL_FUNCTII_PERSOANE_ATTR_KEY_DENUMIRE];
              
                    this.levelSelectItems.push({ label: nume, value: id });
				});
				
				this.levelSelectItems.push({ 
					label: this.translateUtils.translateLabel("RAPORT_CENTRALIZATOR_PREZENTA_PERIOADA_IN_AFARA_NOM"), 
					value: CentralizatorPrezentaPerioadaReportComponent.IN_AFARA_NOM_ID 
				});
				ListItemUtils.sortByLabel(this.levelSelectItems);
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
		this.report = CentralizatorPrezentaPerioadaReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 200) + "px";
		this.prepareBancaSelectItems();
		this.prepareComisieSelectItems();
		this.prepareLevelSelectItems();
	    this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("banca", this.translateUtils.translateLabel("RAPORT_CENTRALIZATOR_PREZENTA_PERIOADA_BANCA")));
        this.columns.push(this.buildColumn("comisie", this.translateUtils.translateLabel("RAPORT_CENTRALIZATOR_PREZENTA_PERIOADA_COMISIE")));
        this.columns.push(this.buildColumn("participariLevel0", this.translateUtils.translateLabel("RAPORT_CENTRALIZATOR_PREZENTA_PERIOADA_PARTICIPARI_LEVEL_0")));
        this.columns.push(this.buildColumn("participariLevel1", this.translateUtils.translateLabel("RAPORT_CENTRALIZATOR_PREZENTA_PERIOADA_PARTICIPARI_LEVEL_1")));
        this.columns.push(this.buildColumn("participariLevel2", this.translateUtils.translateLabel("RAPORT_CENTRALIZATOR_PREZENTA_PERIOADA_PARTICIPARI_LEVEL_2")));
        this.columns.push(this.buildColumn("participariLevel3", this.translateUtils.translateLabel("RAPORT_CENTRALIZATOR_PREZENTA_PERIOADA_PARTICIPARI_LEVEL_3")));
        this.columns.push(this.buildColumn("participariLevel3Plus", this.translateUtils.translateLabel("RAPORT_CENTRALIZATOR_PREZENTA_PERIOADA_PARTICIPARI_LEVEL_3_PLUS")));
        this.columns.push(this.buildColumn("participariInAfaraNom", this.translateUtils.translateLabel("RAPORT_CENTRALIZATOR_PREZENTA_PERIOADA_PARTICIPARI_IN_AFARA_NOM")));
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
		this.formGroup.addControl("level", new FormControl());
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
		let reportFilter: CentralizatorPrezentaPerioadaReportFilterModel = this.prepareFilterModel();
		
		this.reportService.getCentralizatorPrezentaPerioadaReport(reportFilter, {
			onSuccess: (theReport: CentralizatorPrezentaPerioadaReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = CentralizatorPrezentaPerioadaReportComponent.EMPTY_REPORT;
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

	private prepareFilterModel(): CentralizatorPrezentaPerioadaReportFilterModel {
		let filter: CentralizatorPrezentaPerioadaReportFilterModel = new CentralizatorPrezentaPerioadaReportFilterModel();
	
		filter.dataSedintaDeLa = this.dataSedintaDeLaFormControl.value;
		filter.dataSedintaPanaLa = this.dataSedintaPanaLaFormControl.value;
		filter.comisieId = this.comisieFormControl.value;
		filter.bancaId = this.bancaFormControl.value;
		filter.levelId = this.levelFormControl.value;

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

	public get levelFormControl(): FormControl {
		return this.getFormControlByName("level");
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
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.RAPORT_CENTRALIZATOR_PREZENTA_PERIOADA");
		this.dataTable.exportCSV();
	}
	
}