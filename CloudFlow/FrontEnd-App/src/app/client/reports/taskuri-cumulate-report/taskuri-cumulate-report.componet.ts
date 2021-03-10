import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DateConstants, DateUtils, FormUtils, OrganizationService, UserModel, TaskuriCumulateReportModel, TaskStatus, TranslateUtils, TaskuriCumulateReportFilterModel } from  "@app/shared";
import { ArrayUtils, ObjectUtils, AppError, ConfirmationUtils } from  "@app/shared";
import { ReportService, MessageDisplayer } from  "@app/shared";
import { SelectItem, Column } from "primeng/primeng";
import { Table } from "primeng/table";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-taskuri-cumulate-report",
	templateUrl: "./taskuri-cumulate-report.componet.html"
})
export class TaskuriCumulateReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new TaskuriCumulateReportModel();

	@ViewChild(Table)
    public dataTable: Table;  
	public columns: Column[] = [];	

	private messageDisplayer: MessageDisplayer;
    private translateUtils: TranslateUtils;
	private reportService: ReportService;
    private organizationService: OrganizationService;
	
	public reportVisible: boolean;
	public loading: boolean;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public userAsignatSelectItems: SelectItem[];
	public statusSelectItems: SelectItem[];
	public zonaTaskSelectItems: SelectItem[];
	public dateFormat: string;
    public yearRange: string;
    public defaultDate: Date = new Date();

	public report: TaskuriCumulateReportModel;

	public scrollHeight: string;

	public constructor(reportsService: ReportService, formBuilder: FormBuilder, messageDisplayer: MessageDisplayer,
			organizationService: OrganizationService, translateUtils: TranslateUtils) {
		this.reportService = reportsService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.organizationService = organizationService;
        this.translateUtils = translateUtils;
        this.init();
        
	}

    private prepareStatusTaskSelectItems(): void {

        this.statusSelectItems = [];
        let taskStatusArray = Object.keys(TaskStatus);

        taskStatusArray.forEach(taskStatus => {
            this.statusSelectItems.push({ label: this.translateUtils.translateLabel( "TASK_" + taskStatus), value: taskStatus });
		});
				
		ListItemUtils.sortByLabel(this.statusSelectItems);
    }

    private prepareZonaTaskSelectItems(): void {

        this.zonaTaskSelectItems = [];

        this.zonaTaskSelectItems.push({ label: this.translateUtils.translateLabel( "REPORT_TASKURI_CUMULATE_ZONA_FLUXURI"), value: "FLUXURI" });
		this.zonaTaskSelectItems.push({ label: this.translateUtils.translateLabel( "REPORT_TASKURI_CUMULATE_ZONA_PROIECTE"), value: "PROIECTE" });
					
		ListItemUtils.sortByLabel(this.zonaTaskSelectItems);
    }	
    
    private prepareUserAsignatSelectItems(): void {
        this.userAsignatSelectItems = [];

		this.organizationService.getUsers({
			onSuccess: (users: UserModel[]): void => {
				users.forEach(user => {
                    let id: number = Number.parseInt(user.userId);
                    let nume: string = user.displayName;
                
                    this.userAsignatSelectItems.push({ label: nume, value: id });
				});
				
				ListItemUtils.sortByLabel(this.userAsignatSelectItems);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private init(): void {
		this.loading = false;
		this.reportVisible = false;
		this.report = TaskuriCumulateReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.prepareStatusTaskSelectItems();
		this.prepareZonaTaskSelectItems();
		this.prepareUserAsignatSelectItems();
	    this.prepareColumns();
    }

    private prepareColumns(): void {
        this.columns.push(this.buildColumn("zonaTask", this.translateUtils.translateLabel("REPORT_TASKURI_CUMULATE_ZONA_TASK")));
        this.columns.push(this.buildColumn("denumireTask", this.translateUtils.translateLabel("REPORT_TASKURI_CUMULATE_DENUMIRE_TASK")));
        this.columns.push(this.buildColumn("userAsignat", this.translateUtils.translateLabel("REPORT_TASKURI_CUMULATE_USER_ASIGNAT")));
        this.columns.push(this.buildColumn("status", this.translateUtils.translateLabel("REPORT_TASKURI_CUMULATE_STATUS")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }
	
	
	public ngOnInit(): void {
        this.formGroup.addControl("zonaTask", new FormControl());
        this.formGroup.addControl("deLaData", new FormControl(this.defaultDate, [Validators.required]));
        this.formGroup.addControl("panaLaData", new FormControl(this.defaultDate, [Validators.required]));
		this.formGroup.addControl("userAsignat", new FormControl());
		this.formGroup.addControl("status", new FormControl());	
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
		let reportFilter: TaskuriCumulateReportFilterModel = this.prepareFilterModel();
		this.reportService.getTaskuriCumulate(reportFilter, {
			onSuccess: (theReport: TaskuriCumulateReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
                    theReport = TaskuriCumulateReportComponent.EMPTY_REPORT; 
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

	private prepareFilterModel(): TaskuriCumulateReportFilterModel {
		let filter: TaskuriCumulateReportFilterModel = new TaskuriCumulateReportFilterModel();
	
        filter.zonaTask = this.zonaTaskFormControl.value;
        filter.deLaData = this.deLaDataFormControl.value;
        filter.panaLaData = this.panaLaDataFormControl.value;
        filter.userAsignat = this.userAsignatFormControl.value;
        filter.status = this.statusFormControl.value;

        return filter;
	}

	public onExportExcel(): void {
		alert("not implemented yet");
	}

	public get footerVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.report) && ArrayUtils.isNotEmpty(this.report.rows);
	}
    
	public get zonaTaskFormControl(): FormControl {
		return this.getFormControlByName("zonaTask");
	}

    public get deLaDataFormControl(): FormControl {
		return this.getFormControlByName("deLaData");
	}

    public get panaLaDataFormControl(): FormControl {
		return this.getFormControlByName("panaLaData");
	}

    public get userAsignatFormControl(): FormControl {
		return this.getFormControlByName("userAsignat");
	}

	public get statusFormControl(): FormControl {
		return this.getFormControlByName("status");
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
			if (exportCell.field === "zonaTask" ) {
				return this.translateUtils.translateCode("LABELS.REPORT_TASKURI_CUMULATE_ZONA_" + exportCell.data);
			} else if (exportCell.field === "status" ) {
				return this.translateUtils.translateCode("LABELS.TASK_" + exportCell.data);
			} else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_TASKURI_CUMULATE");
		this.dataTable.exportCSV();
	}
}