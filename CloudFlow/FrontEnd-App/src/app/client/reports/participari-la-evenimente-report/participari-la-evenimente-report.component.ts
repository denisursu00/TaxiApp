import { Component, OnInit, ViewChild } from "@angular/core";
import { ParticipariLaEvenimenteReportRowModel, MessageDisplayer, ReportService, DateConstants, DateUtils, ParticipariLaEvenimenteReportFilterModel, FormUtils, ObjectUtils, 
    AppError, TaskStatus, ProjectService, ProjectModel, TranslateUtils, OrganizationService, UserModel } from "@app/shared";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { SelectItem } from "primeng/api";
import { Table } from "primeng/table";
import { Column } from "primeng/primeng";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";
import { ProjectSubactivityModel } from "@app/shared/model/project/project-subactivity.model";

@Component({
    selector: "app-participari-la-evenimente-report",
    templateUrl: "./participari-la-evenimente-report.component.html"
})
export class ParticipariLaEvenimenteReportComponent implements OnInit {
    @ViewChild(Table)
    public dataTable: Table;
    
    private organizationService: OrganizationService;
    private translateUtils: TranslateUtils;
    private messageDisplayer: MessageDisplayer;
    private reportService: ReportService;
    private projectService: ProjectService;

	public columns: Column[] = [];

    public reportVisible: boolean;
    public loading: boolean;

    private formBuilder: FormBuilder;
    public formGroup: FormGroup;

    public dateFormat: string;
    public yearRange: string;

    public report: ParticipariLaEvenimenteReportRowModel[];

    public statusTaskSelectItems: SelectItem[];
    public proiecteSelectItems: SelectItem[];
    public responsabilSelectItems: SelectItem[];
    public subproiecteSelectItems: SelectItem[];

    public scrollHeight: string;

    public constructor(reportService: ReportService, formBuilder: FormBuilder, messageDisplayer: MessageDisplayer,
                        projectService: ProjectService, translateUtils: TranslateUtils, organizationService: OrganizationService) {

        this.reportService = reportService;
        this.formBuilder = formBuilder;
        this.messageDisplayer = messageDisplayer;
        this.projectService = projectService;
        this.translateUtils = translateUtils;
        this.organizationService = organizationService;

        this.init();
    } 

    private init(): void {
        this.loading = false;
        this.reportVisible = false;
        this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
        this.yearRange = DateUtils.getDefaultYearRange();
        this.scrollHeight = (window.innerHeight - 180) + "px";
        this.prepareResponsabilSelectItems();
        this.prepareStatusTaskSelectItems();
        this.prepareProiecteSelectItems();
        this.prepareReportColumns();
    }
    
    private prepareReportColumns(): any {
        this.columns.push(this.buildColumn("numeProiect", this.translateUtils.translateCode("LABELS.REPORT_PARTICIPARI_LA_EVENIMENTE_PROIECT")));
        this.columns.push(this.buildColumn("numeSubproiect", this.translateUtils.translateCode("LABELS.PROJECT_SUBACTIVITY")));
        this.columns.push(this.buildColumn("numeTask", this.translateUtils.translateCode("LABELS.REPORT_PARTICIPARI_LA_EVENIMENTE_NUME_TASK")));
        this.columns.push(this.buildColumn("dataInceputForDisplay", this.translateUtils.translateCode("LABELS.REPORT_PARTICIPARI_LA_EVENIMENTE_DATA_INCEPUT")));
        this.columns.push(this.buildColumn("dataSfarsitForDisplay", this.translateUtils.translateCode("LABELS.REPORT_PARTICIPARI_LA_EVENIMENTE_DATA_SFARSIT")));
        this.columns.push(this.buildColumn("responsabiliTask", this.translateUtils.translateCode("LABELS.REPORT_PARTICIPARI_LA_EVENIMENTE_RESPONSABIL_TASK")));
        this.columns.push(this.buildColumn("participariLa", this.translateUtils.translateCode("LABELS.REPORT_PARTICIPARI_LA_EVENIMENTE_PARTICIPARI_LA")));
        this.columns.push(this.buildColumn("statusTask", this.translateUtils.translateCode("LABELS.REPORT_PARTICIPARI_LA_EVENIMENTE_STATUS_TASK")));
    }

    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }

    private prepareResponsabilSelectItems(): void {
        this.responsabilSelectItems = [];
        
        this.organizationService.getAllUsersWithAssignedTasks( {
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

    private prepareStatusTaskSelectItems(): void {

        this.statusTaskSelectItems = [];
        let taskStatusArray = Object.keys(TaskStatus);

        taskStatusArray.forEach(taskStatus => {
            this.statusTaskSelectItems.push({ label: this.translateUtils.translateLabel( "TASK_" + taskStatus), value: taskStatus });
        });
        this.statusTaskSelectItems = this.statusTaskSelectItems.filter((item) => {
            return item.value !== TaskStatus.CANCELLED;
        });

        ListItemUtils.sortByLabel(this.statusTaskSelectItems);
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

    public ngOnInit(): void {
        this.formGroup = this.formBuilder.group([]);
        this.formGroup.addControl("dataInceput", new FormControl(null, [Validators.required]));
        this.formGroup.addControl("dataSfarsit", new FormControl(null, [Validators.required]));
        this.formGroup.addControl("responsabilTask", new FormControl());
        this.formGroup.addControl("statusTask", new FormControl());
        this.formGroup.addControl("proiecte", new FormControl());
        this.formGroup.addControl("subproiecte", new FormControl({value: null, disabled: true}));
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

        let reportFilter: ParticipariLaEvenimenteReportFilterModel = this.prepareParticipariLaEvenimenteReportFilterModel();

        this.reportService.getParticipariLaEvenimenteReport(reportFilter, {
            onSuccess: (report: ParticipariLaEvenimenteReportRowModel[]): void => {

                this.report = report;
                this.reportVisible = true;
                this.unlock();
            },
            onFailure: (error: AppError) => {
                this.unlock();
                this.messageDisplayer.displayAppError(error);
            }
        });
    }

    private isFilterValid(): boolean {
        FormUtils.validateAllFormFields(this.formGroup);

        return this.formGroup.valid;
    }

    private prepareParticipariLaEvenimenteReportFilterModel(): ParticipariLaEvenimenteReportFilterModel {

        let filter: ParticipariLaEvenimenteReportFilterModel = new ParticipariLaEvenimenteReportFilterModel();

        filter.dataInceput = this.dataInceputFormControl.value;
        filter.dataSfarsit = this.dataSfarsitFormControl.value;
        filter.idUserResponsabilTask = Number.parseInt(this.responsabilTaskFormControl.value);
        filter.statusTask = this.statusTaskFormControl.value;
        filter.idProiect = this.proiecteFormControl.value;
        filter.subprojectIds = this.subproiecteFormControl.value;

        return filter;
    }

    public onExportCSV(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.dataTable)) {
			return;
        }
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_PARTICIPARI_LA_EVENIMENTE");
		this.dataTable.exportFunction = (exportCell: any) => {
			exportCell.data = ExportUtils.replaceQuotationMarkIfStringData( exportCell.data);
		    if (exportCell.field === "statusTask") {
                return this.translateUtils.translateCode("LABELS.TASK_" + exportCell.data);
            }
				return exportCell.data;
		};
		this.dataTable.exportCSV();
    }
    
    public onProiectSelectionChanged(event: any): void {
        this.prepareSubactivitySelectItems(event.value);
    }

    public get footerVisible(): boolean {
        return ObjectUtils.isNotNullOrUndefined(this.report);
    }

    public get dataInceputFormControl(): FormControl {
        return this.getFormControlByName("dataInceput");
    }

    public get dataSfarsitFormControl(): FormControl {
        return this.getFormControlByName("dataSfarsit");
    }

    public get responsabilTaskFormControl(): FormControl {
        return this.getFormControlByName("responsabilTask");
    }

    public get statusTaskFormControl(): FormControl {
        return this.getFormControlByName("statusTask");
    }

    public get proiecteFormControl(): FormControl {
        return this.getFormControlByName("proiecte");
    }

    public get subproiecteFormControl(): FormControl {
        return this.getFormControlByName("subproiecte");
    }

    private getFormControlByName(name: string): FormControl {
        return <FormControl> this.formGroup.get(name);
    }
}