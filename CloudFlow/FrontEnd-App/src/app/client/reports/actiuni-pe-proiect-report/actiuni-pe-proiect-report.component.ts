import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl } from "@angular/forms";
import { DateConstants, DateUtils, FormUtils, ProjectService, ProjectModel, ActiuniPeProiectReportModel, ActiuniPeProiectReportFilterModel, TranslateUtils, StringUtils } from "./../../../shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from "./../../../shared";
import { ReportService, MessageDisplayer } from "./../../../shared";
import { SelectItem, Column } from "primeng/primeng";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ReportConstants } from "../constants/report.constants";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";
import { ProjectSubactivityModel } from "@app/shared/model/project/project-subactivity.model";

@Component({
	selector: "app-actiuni-pe-proiect-report",
	templateUrl: "./actiuni-pe-proiect-report.component.html"
})
export class ActiuniPeProiectReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new ActiuniPeProiectReportModel();

	@ViewChild("actiuniPeProiectTaskTable")
	public actiuniPeProiectTaskTable: Table;

	@ViewChild("actiuniPeProiectRegistruIntrariIesiriTable")
	public actiuniPeProiectRegistruIntrariIesiriTable: Table;

	@ViewChild("actiuniPeProiectNotaCDTable")
	public actiuniPeProiectNotaCDTable: Table;

	public columnsActiuniPeProiectTask: Column[] = [];
	public columnsActiuniPeProiectRegistruIntrariIesiri: Column[] = [];	
	public columnsActiuniPeProiectNotaCD: Column[] = [];
	private translateUtils: TranslateUtils;
	
	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;
	private reportService: ReportService;
	private projectService: ProjectService;
	
	public reportVisible: boolean;
	public loading: boolean;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public dateFormat: string;
	public yearRange: string;

	public report: ActiuniPeProiectReportModel;
	
	public proiecteSelectItems: SelectItem[];
    public subproiecteSelectItems: SelectItem[];

	public rowGroupMetadataActiuniPeProiectTask: any;
	public rowGroupMetadataActiuniPeProiectRegistruIntrariIesiri: any;
	public rowGroupMetadataActiuniPeProiectNotaCD: any;

	public rowGroupMetadataActiuniPeProiectTaskProiectSequenceList: Array<number>;
	public rowGroupMetadataActiuniPeProiectTaskSubproiectSequenceList: Array<number>;
	public rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriProiectSequenceList: Array<number>;
	public rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriSubproiectSequenceList: Array<number>;

	public scrollHeight: string;

	public constructor(reportsService: ReportService, formBuilder: FormBuilder,
			messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils,
			projectService: ProjectService, translateUtils: TranslateUtils) {
		this.reportService = reportsService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;	
		this.projectService = projectService;	
		this.translateUtils = translateUtils;
		this.init();
	}

	private init(): void {
		this.loading = false;
		this.reportVisible = false;
		this.report = ActiuniPeProiectReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 200) + "px";
		this.rowGroupMetadataActiuniPeProiectTaskProiectSequenceList = [];
		this.rowGroupMetadataActiuniPeProiectTaskSubproiectSequenceList = [];
		this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriProiectSequenceList = [];
		this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriSubproiectSequenceList = [];
        this.prepareProiecteSelectItems();
		this.prepareColumns();
    }

    private prepareColumns(): any {
        this.columnsActiuniPeProiectTask.push(this.buildColumn("numeProiect", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_NUME_PROIECT_ACTIUNI")));
        this.columnsActiuniPeProiectTask.push(this.buildColumn("numeSubproiect", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_NUME_SUBPROIECT_ACTIUNI")));
        this.columnsActiuniPeProiectTask.push(this.buildColumn("tipActiune", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_TIP_ACTIUNE")));
        this.columnsActiuniPeProiectTask.push(this.buildColumn("actiuni", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_ACTIUNI")));
        this.columnsActiuniPeProiectTask.push(this.buildColumn("dataInceputTask", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_DATA_INCEPUT_TASK")));
		this.columnsActiuniPeProiectTask.push(this.buildColumn("status", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_STATUS")));
		
        this.columnsActiuniPeProiectRegistruIntrariIesiri.push(this.buildColumn("numeProiect", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_NUME_PROIECT_DOCUMENTE")));
        this.columnsActiuniPeProiectRegistruIntrariIesiri.push(this.buildColumn("tipDocument", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_TIP_DOCUMENT")));
        this.columnsActiuniPeProiectRegistruIntrariIesiri.push(this.buildColumn("document", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_DOCUMENT")));
        this.columnsActiuniPeProiectRegistruIntrariIesiri.push(this.buildColumn("dataInregistrareForDisplay", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_DATA_INREGISTRARE")));
        this.columnsActiuniPeProiectRegistruIntrariIesiri.push(this.buildColumn("numarDeInregistrare", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_NUMAR_DE_INREGISTRARE")));

		this.columnsActiuniPeProiectNotaCD.push(this.buildColumn("numeProiect", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_NUME_PROIECT_NOTE_CD")));
		this.columnsActiuniPeProiectNotaCD.push(this.buildColumn("tipNota", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_TIP_NOTA")));
		this.columnsActiuniPeProiectNotaCD.push(this.buildColumn("subiectNota", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_SUBIECT_NOTA")));
		this.columnsActiuniPeProiectNotaCD.push(this.buildColumn("dataCdArbForDisplay", this.translateUtils.translateLabel("REPORT_ACTIUNI_PE_PROIECT_DATA_CD_ARB")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }

	private prepareProiecteSelectItems() {
        this.proiecteSelectItems = [];

        this.projectService.getAllProjects({
            onSuccess: (proiecte: ProjectModel[]): void => {
                
                proiecte.forEach(proiect => {
                    this.proiecteSelectItems.push({ label: proiect.name, value: proiect.id});
				});
				
				ListItemUtils.sortByLabel(this.proiecteSelectItems);                
            },
            onFailure: (appError: AppError): void => {
                this.messageDisplayer.displayAppError(appError);
            }
        });
	}

    private getProjectSubactivities(projectId: number): Promise<ProjectSubactivityModel[]> {
		return new Promise<ProjectSubactivityModel[]>((resolve, reject) => {
			this.projectService.getProjectSubactivities(projectId, {
				onSuccess: (subactivities: ProjectSubactivityModel[]): void => {
					resolve(subactivities);
				},onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
					reject();
				}
			});
		});
	}
	
	private prepareSubactivitySelectItems(projectId: number): void{
		this.subproiecteFormControl.reset();
        if (projectId != null){
            this.getProjectSubactivities(projectId)
                .then(subactivities => {
                    this.subproiecteSelectItems = [];
                    subactivities.forEach(subactivity => {
                        this.subproiecteSelectItems.push({value: subactivity.id, label: subactivity.name});
					});
					this.subproiecteSelectItems.sort((a,b) => a.label.localeCompare(b.label));
                    this.subproiecteSelectItems.splice(0, 0, {value: -1, label: this.translateUtils.translateLabel("WITHOUT_SUBPROJECT")});
                    this.subproiecteFormControl.enable();
                });
        }else{
            this.subproiecteSelectItems = [];
            this.subproiecteFormControl.disable();
        }
	}
	
	public ngOnInit(): void {
		this.formGroup.addControl("dataInceput", new FormControl());
		this.formGroup.addControl("dataSfarsit", new FormControl());
        this.formGroup.addControl("proiect", new FormControl());
        this.formGroup.addControl("subproiecte", new FormControl({value: null, disabled: true}));
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
		let reportFilter: ActiuniPeProiectReportFilterModel = this.prepareFilterModel();
		this.reportService.getActiuniPeProiectReport(reportFilter, {
			onSuccess: (theReport: ActiuniPeProiectReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = ActiuniPeProiectReportComponent.EMPTY_REPORT;
				}
				this.report = theReport;
				this.updateRowGroupMetaDataActiuniPeProiectNotaCD();

				this.updateRowGroupMetaDataActiuniPeProiectTaskSequenceLists();
				this.updateRowGroupMetaDataActiuniPeProiectRegistruIntrariIesiriSequenceLists();

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

	private prepareFilterModel(): ActiuniPeProiectReportFilterModel {
		let filter: ActiuniPeProiectReportFilterModel = new ActiuniPeProiectReportFilterModel();
		
		filter.dataInceput = this.dataInceputFormControl.value;
		filter.dataSfarsit = this.dataSfarsitFormControl.value;
		filter.proiectId = this.proiectFormControl.value;
		filter.subprojectIds = this.subproiecteFormControl.value;
		
		return filter;
	}

	public onExportExcel(): void {
		alert("not implemented yet");
	}

	public get footerVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.report) && (ArrayUtils.isNotEmpty(this.report.actiuniPeProiectRegistruIntrariIesiri) || ArrayUtils.isNotEmpty(this.report.actiuniPeProiectTask));
	}

	public get dataInceputFormControl(): FormControl {
		return this.getFormControlByName("dataInceput");
	}

	public get dataSfarsitFormControl(): FormControl {
		return this.getFormControlByName("dataSfarsit");
	}

    public get proiectFormControl(): FormControl {
        return this.getFormControlByName("proiect");
	}

	public get subproiecteFormControl(): FormControl {
		return this.getFormControlByName("subproiecte");
	}
	
	private getFormControlByName(name: string): FormControl {
		return <FormControl> this.formGroup.get(name);
	}
    
    public onProiectSelectionChanged(event: any): void {
        this.prepareSubactivitySelectItems(event.value);
    }


	public onExportCsvActiuniPeProiectTask(): void {
		if (ObjectUtils.isNullOrUndefined(this.actiuniPeProiectTaskTable)) {
			return;
		}
		this.actiuniPeProiectTaskTable.exportFunction = (exportCell: any) => {
			exportCell.data = ExportUtils.replaceQuotationMarkIfStringData( exportCell.data);
			if (exportCell.field === "dataInceputTask" || exportCell.field === "dataInregistrare") {
				return DateUtils.formatForDisplay(exportCell.data);
			} else if (exportCell.field === "status" ) {
				return this.translateUtils.translateCode("LABELS.TASK_" +exportCell.data);
			} else {
				return exportCell.data;
			}
		};
		this.actiuniPeProiectTaskTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_ACTIUNI_PE_PROIECT") + " - Actiuni";
		this.actiuniPeProiectTaskTable.exportCSV();
	}

	public onExportCsvActiuniPeProiectRegistruIntrariIesiri(): void {
		if (ObjectUtils.isNullOrUndefined(this.actiuniPeProiectRegistruIntrariIesiriTable)) {
			return;
		}
		this.actiuniPeProiectRegistruIntrariIesiriTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_ACTIUNI_PE_PROIECT") + " - Documente";
		this.actiuniPeProiectRegistruIntrariIesiriTable.exportCSV();
	}

	public onExportCsvActiuniPeProiectNotaCD(): void {
		if (ObjectUtils.isNullOrUndefined(this.actiuniPeProiectNotaCDTable)) {
			return;
		}
		this.actiuniPeProiectNotaCDTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_ACTIUNI_PE_PROIECT") + " - Note CD";
		this.actiuniPeProiectNotaCDTable.exportCSV();
	}

	public onSortActiuniPeProiectNotaCD() {
		this.updateRowGroupMetaDataActiuniPeProiectNotaCD();
	}
	
	public onSortActiuniPeProiectRegistruIntrariIesiri() {
		this.updateRowGroupMetaDataActiuniPeProiectRegistruIntrariIesiri();
	}

	public onSortActiuniPeProiectTask() {
		this.updateRowGroupMetaDataActiuniPeProiectTask();
	}

	public updateRowGroupMetaDataActiuniPeProiectNotaCD() {
		this.rowGroupMetadataActiuniPeProiectNotaCD = {};
		if (this.report.actiuniPeProiectNotaCD) {
			for (let i = 0; i < this.report.actiuniPeProiectNotaCD.length; i++) {
				let rowData = this.report.actiuniPeProiectNotaCD[i];
				let tipNota = rowData.tipNota;
				if (i === 0) {
					this.rowGroupMetadataActiuniPeProiectNotaCD[tipNota] = { index: 0, size: 1 };
				} else {
					let previousRowData = this.report.actiuniPeProiectNotaCD[i - 1];
					let previousRowGroupTipNota = previousRowData.tipNota;
					if (tipNota === previousRowGroupTipNota) {
						this.rowGroupMetadataActiuniPeProiectNotaCD[tipNota].size++;
						this.rowGroupMetadataActiuniPeProiectNotaCD[tipNota].index = i;
					} else {
						this.rowGroupMetadataActiuniPeProiectNotaCD[tipNota] = { index: i, size: 1 };
					}
				}
			}
		}
	}

	public updateRowGroupMetaDataActiuniPeProiectTask() {
		this.rowGroupMetadataActiuniPeProiectTask = {};
		if (this.report.actiuniPeProiectTask) {
			for (let i = 0; i < this.report.actiuniPeProiectTask.length; i++) {
				let rowData = this.report.actiuniPeProiectTask[i];
				let tipActiune = rowData.tipActiune;
				if (StringUtils.isBlank(tipActiune)) {
					tipActiune = ReportConstants.ACTIUN_PE_PROIECT_TIP_ACTIUNE_NULL_VALUE_FOR_GROUPING;
				}
					if (i === 0) {					
						this.rowGroupMetadataActiuniPeProiectTask[tipActiune] = { index: 0, size: 1 };			
					} else {
						let previousRowData = this.report.actiuniPeProiectTask[i - 1];
						let previousRowGroupTipActiune = previousRowData.tipActiune;
						
						if (StringUtils.isBlank(previousRowGroupTipActiune)) {
							previousRowGroupTipActiune = ReportConstants.ACTIUN_PE_PROIECT_TIP_ACTIUNE_NULL_VALUE_FOR_GROUPING;
						}
						if (tipActiune === previousRowGroupTipActiune) {
							this.rowGroupMetadataActiuniPeProiectTask[tipActiune].size++;
							this.rowGroupMetadataActiuniPeProiectTask[tipActiune].index = i;
						} else {
							this.rowGroupMetadataActiuniPeProiectTask[tipActiune] = { index: i, size: 1 };
						}
					}
			}
		}
	}

	public updateRowGroupMetaDataActiuniPeProiectTaskSequenceLists(): void {
		this.rowGroupMetadataActiuniPeProiectTaskProiectSequenceList.splice(0,this.rowGroupMetadataActiuniPeProiectTaskProiectSequenceList.length);
		this.rowGroupMetadataActiuniPeProiectTaskSubproiectSequenceList.splice(0, this.rowGroupMetadataActiuniPeProiectTaskSubproiectSequenceList.length);
		if (this.report.actiuniPeProiectTask){
			for (let i = 0; i < this.report.actiuniPeProiectTask.length; i++){
				if (i === 0){ 
					this.rowGroupMetadataActiuniPeProiectTaskProiectSequenceList.push(1);
					this.rowGroupMetadataActiuniPeProiectTaskSubproiectSequenceList.push(1);
					continue;
				}
				if (this.report.actiuniPeProiectTask[i].numeProiect === this.report.actiuniPeProiectTask[i-1].numeProiect){
					this.rowGroupMetadataActiuniPeProiectTaskProiectSequenceList.push(this.rowGroupMetadataActiuniPeProiectTaskProiectSequenceList[i-1] + 1);
				}else{
					this.rowGroupMetadataActiuniPeProiectTaskProiectSequenceList.push(1);
				}
				if (this.report.actiuniPeProiectTask[i].numeSubproiect === this.report.actiuniPeProiectTask[i-1].numeSubproiect
					&& this.rowGroupMetadataActiuniPeProiectTaskProiectSequenceList[i] !== 1){
					this.rowGroupMetadataActiuniPeProiectTaskSubproiectSequenceList.push(this.rowGroupMetadataActiuniPeProiectTaskSubproiectSequenceList[i - 1] + 1);
				}else{
					this.rowGroupMetadataActiuniPeProiectTaskSubproiectSequenceList.push(1);
				}
			}
		}
	}

	public updateRowGroupMetaDataActiuniPeProiectRegistruIntrariIesiriSequenceLists(): void {
		this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriProiectSequenceList.splice(0,this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriProiectSequenceList.length);
		this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriSubproiectSequenceList.splice(0, this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriSubproiectSequenceList.length);
		if (this.report.actiuniPeProiectRegistruIntrariIesiri){
			for (let i = 0; i < this.report.actiuniPeProiectRegistruIntrariIesiri.length; i++){
				if (i === 0){ 
					this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriProiectSequenceList.push(1);
					this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriSubproiectSequenceList.push(1);
					continue;
				}
				if (this.report.actiuniPeProiectRegistruIntrariIesiri[i].numeProiect === this.report.actiuniPeProiectRegistruIntrariIesiri[i-1].numeProiect){
					this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriProiectSequenceList.push(this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriProiectSequenceList[i-1] + 1);
				}else{
					this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriProiectSequenceList.push(1);
				}
				if (this.report.actiuniPeProiectRegistruIntrariIesiri[i].numeSubproiect === this.report.actiuniPeProiectRegistruIntrariIesiri[i-1].numeSubproiect
					&& this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriProiectSequenceList[i] !== 1){
					this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriSubproiectSequenceList.push(this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriSubproiectSequenceList[i - 1] + 1);
				}else{
					this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiriSubproiectSequenceList.push(1);
				}
			}
		}
	}

	public updateRowGroupMetaDataActiuniPeProiectRegistruIntrariIesiri() {
		this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiri = {};
		if (this.report.actiuniPeProiectRegistruIntrariIesiri) {
			for (let i = 0; i < this.report.actiuniPeProiectRegistruIntrariIesiri.length; i++) {
				let rowData = this.report.actiuniPeProiectRegistruIntrariIesiri[i];
				let tipDocument = rowData.tipDocument;
				if (i === 0) {
					this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiri[tipDocument] = { index: 0, size: 1 };
				} else {
					let previousRowData = this.report.actiuniPeProiectRegistruIntrariIesiri[i - 1];
					let previousRowGroupTipDocument = previousRowData.tipDocument;
					if (tipDocument === previousRowGroupTipDocument) {
						this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiri[tipDocument].size++;
						this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiri[tipDocument].index = i;
					} else {
						this.rowGroupMetadataActiuniPeProiectRegistruIntrariIesiri[tipDocument] = { index: i, size: 1 };
					}
				}
			}
		}
	}
}
