import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import { AppError, ArrayUtils, ConfirmationUtils, DateConstants, DateUtils, DocumentConstants, DocumentIdentifierModel, DocumentTypeService, FormUtils, MessageDisplayer, NomenclatorService, ObjectUtils, OrganizationService, PrezentaComisiiGlInIntervalReportBundle, PrezentaComisiiGlInIntervalReportFilterModel, PrezentaComisiiGlInIntervalReportModel, ReportService, TranslateUtils } from "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-prezenta-comisii-gl-in-interval-report",
	templateUrl: "./prezenta-comisii-gl-in-interval-report.component.html"
})
export class PrezentaComisiiGlInIntervalReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new PrezentaComisiiGlInIntervalReportModel();

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
	public participantAcreditatSelectItems: SelectItem[];
	public dateFormat: string;
	public yearRange: string;

	public report: PrezentaComisiiGlInIntervalReportModel;
	public isParticipantAcreditat: boolean = true;

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
		this.report = PrezentaComisiiGlInIntervalReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.prepareDocumentTypeIdSelectItems();
	    this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("institutie", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_GL_IN_INTERVAL_NUME_INSTITUTIE")));
        this.columns.push(this.buildColumn("prticipant", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_GL_IN_INTERVAL_NUME_PARTICIPANTI")));
        this.columns.push(this.buildColumn("functie", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_GL_IN_INTERVAL_FUNCTIE")));
        this.columns.push(this.buildColumn("departament", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_GL_IN_INTERVAL_DEPARTAMENT")));
        this.columns.push(this.buildColumn("email", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_GL_IN_INTERVAL_EMAIL")));
        this.columns.push(this.buildColumn("telefon", this.translateUtils.translateLabel("REPORT_PREZENTA_COMISII_GL_IN_INTERVAL_TELEFON")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }
	
	public ngOnInit(): void {
		this.formGroup.addControl("document", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("institutie", new FormControl());
		this.formGroup.addControl("participantAcreditat", new FormControl(true));
		this.formGroup.addControl("numeParticipantAcreditat", new FormControl());
		this.formGroup.addControl("numeParticipantInlocuitor", new FormControl());
		this.formGroup.addControl("prenumeParticipantInlocuitor", new FormControl());
		this.formGroup.addControl("functie", new FormControl());
	}

	private lock(): void {
		this.loading = true;
	}

	private unlock(): void {
		this.loading = false;
	}

	public onDocumentChanged(event:any) : void {
		if (ObjectUtils.isNotNullOrUndefined(event)) {
			let document: DocumentIdentifierModel = new DocumentIdentifierModel();

			document.documentId = event[0].value.documentId;
			document.documentLocationRealName = event[0].value.documentLocationRealName;

			this.institutieSelectItems = [];
			this.participantAcreditatSelectItems = [];

			this.reportService.getPrezentaComisiiGlInIntervalReportBundle(document, {
				onSuccess: (bundle: PrezentaComisiiGlInIntervalReportBundle): void => {
					
					let institutiiItems = bundle.institutieItems;
					let partecipantiAcreditatiItems = bundle.participantAcreditatItems;
				
					institutiiItems.forEach(institutie => {
						let label:string = institutie.label;
						let value:string = institutie.value;
				  
						this.institutieSelectItems.push({ label: label, value: value });
					});

					partecipantiAcreditatiItems.forEach(participant => {
						let label:string = participant.label;
						let value:string = participant.value;
				  
						this.participantAcreditatSelectItems.push({ label: label, value: value });
					});

					ListItemUtils.sortByLabel(this.institutieSelectItems);
					ListItemUtils.sortByLabel(this.participantAcreditatSelectItems);
				},
				onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
				}
			});
		}
		
	}
	
	public onView(): void {
		if (!this.isFilterValid()) {
			return;
		}		
		this.lock();
		this.reportVisible = false;
		let reportFilter: PrezentaComisiiGlInIntervalReportFilterModel = this.prepareFilterModel();
		this.reportService.getPrezentaComisiiGlInIntervalReport(reportFilter, {
			onSuccess: (theReport: PrezentaComisiiGlInIntervalReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = PrezentaComisiiGlInIntervalReportComponent.EMPTY_REPORT;
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

	private prepareFilterModel(): PrezentaComisiiGlInIntervalReportFilterModel {
		let filter: PrezentaComisiiGlInIntervalReportFilterModel = new PrezentaComisiiGlInIntervalReportFilterModel();
	
		if (ObjectUtils.isNotNullOrUndefined(this.documentFormControl.value)) {
			let document: DocumentIdentifierModel = new DocumentIdentifierModel();

			document.documentId = this.documentFormControl.value[0].value.documentId;
			document.documentLocationRealName = this.documentFormControl.value[0].value.documentLocationRealName;
			
			filter.document = document;
		}
		
		filter.institutieId = Number.parseInt(this.institutieFormControl.value);

		if (this.participantAcreditatFormControl.value) {
			filter.participantAcreditat = this.numeParticipantAcreditatFormControl.value;
			filter.numeParticipantInlocuitor = null;
			filter.prenumeParticipantInlocuitor = null;
		} else {
			filter.participantAcreditat = null;
			if (ObjectUtils.isNotNullOrUndefined(this.numeParticipantInlocuitorFormControl.value) && this.numeParticipantInlocuitorFormControl.value.length > 0) {
				filter.numeParticipantInlocuitor = this.numeParticipantInlocuitorFormControl.value;
			}
			if (ObjectUtils.isNotNullOrUndefined(this.prenumeParticipantInlocuitorFormControl.value) && this.prenumeParticipantInlocuitorFormControl.value.length > 0) {
				filter.prenumeParticipantInlocuitor = this.prenumeParticipantInlocuitorFormControl.value;
			}
		}

		if (ObjectUtils.isNotNullOrUndefined(this.functieFormControl.value) && this.functieFormControl.value.length > 0) {
			filter.functie = this.functieFormControl.value;
		} 

		return filter;
	}

	public onChangeTipParticipant(event: any): void {
		this.isParticipantAcreditat = event;
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

	public get institutieFormControl(): FormControl {
		return this.getFormControlByName("institutie");
	}

	public get participantAcreditatFormControl(): FormControl {
		return this.getFormControlByName("participantAcreditat");
	}

	public get numeParticipantAcreditatFormControl(): FormControl {
		return this.getFormControlByName("numeParticipantAcreditat");
	}

	public get numeParticipantInlocuitorFormControl(): FormControl {
		return this.getFormControlByName("numeParticipantInlocuitor");
	}
	public get prenumeParticipantInlocuitorFormControl(): FormControl {
		return this.getFormControlByName("prenumeParticipantInlocuitor");
	}
	public get functieFormControl(): FormControl {
		return this.getFormControlByName("functie");
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
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_PREZENTA_COMISII_GL_IN_INTERVAL");
		this.dataTable.exportCSV();
	}
	
}
