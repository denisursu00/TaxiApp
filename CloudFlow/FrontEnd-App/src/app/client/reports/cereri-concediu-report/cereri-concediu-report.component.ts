import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators} from "@angular/forms";
import { DateConstants, DateUtils, FormUtils, CereriConcediuReportModel, OrganizationService, UserModel, CereriConcediuReportBundle, TranslateUtils, CereriConcediuReportFilterModel, ArrayUtils } from "../../../shared";
import { ObjectUtils, AppError, ConfirmationUtils } from "../../../shared";
import { ReportService, MessageDisplayer } from "../../../shared";
import { SelectItem } from "primeng/api";
import { Table } from "primeng/table";
import { Column } from "primeng/primeng";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-cereri-concediu-report",
	templateUrl: "./cereri-concediu-report.component.html"
})
export class CereriConcediuReportComponent implements OnInit {
	
	private static readonly EMPTY_REPORT = new CereriConcediuReportModel();

	@ViewChild(Table)
	public dataTable: Table;  
	public columns: Column[] = [];	

	private messageDisplayer: MessageDisplayer;
	private organizationService: OrganizationService;
	private translateUtils: TranslateUtils;
	private confirmationUtils: ConfirmationUtils;
	private reportService: ReportService;
	
	public reportVisible: boolean;
	public loading: boolean;
	public filtruDataDecontDisabled: boolean;
	public titularSelected: boolean;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public tipConcediuSelectItems: SelectItem[];
	public angajatiSelectItems: SelectItem[];
	public statusSelectItems: SelectItem[];
	public dateFormat: string;
	public yearRange: string;

	public report: CereriConcediuReportModel;

	public scrollHeight: string;

	public constructor(organizationService: OrganizationService, reportsService: ReportService, formBuilder: FormBuilder,
			messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils, confirmationUtils: ConfirmationUtils) {
        this.organizationService = organizationService;
        this.reportService = reportsService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.confirmationUtils = confirmationUtils;	
		this.init();
    }
    
    private prepareTipConcediuSelectItems(): void {
		this.tipConcediuSelectItems = [];

		this.reportService.getCereriConcediuReportBundle({
			onSuccess: (cereriConcediuReportBundle: CereriConcediuReportBundle): void => {
				let tipConcediuItems = cereriConcediuReportBundle.tipConcediuItems;
			
				tipConcediuItems.forEach(tipConcediuItem => {
                    let label:string = tipConcediuItem.label;
                    let value:string = tipConcediuItem.value;
              
                    this.tipConcediuSelectItems.push({ label: label, value: value });
				});
				
				ListItemUtils.sortByLabel(this.tipConcediuSelectItems);
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

    private prepareAngajatiSelectItems(): void {
        this.angajatiSelectItems = [];

		this.organizationService.getUsers({
			onSuccess: (users: UserModel[]): void => {
				users.forEach(user => {
                    let id: number = Number.parseInt(user.userId);
                    let nume: string = user.displayName;
                
                    this.angajatiSelectItems.push({ label: nume, value: id });
				});
				
				ListItemUtils.sortByLabel(this.angajatiSelectItems);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
    }

    private prepareStatusSelectItems(): void {
		this.statusSelectItems = [
			{ label: this.translateUtils.translateLabel("CONCEDIU_APROBAT"), value: "CONCEDIU_APROBAT" },
			{ label: this.translateUtils.translateLabel("CONCEDIU_RESPINS"), value: "CONCEDIU_RESPINS" }
		];
		
		ListItemUtils.sortByLabel(this.statusSelectItems);
	}

	private init(): void {
		this.loading = false;
		this.reportVisible = false;
		this.filtruDataDecontDisabled = true;
		this.titularSelected = false;
		this.report = CereriConcediuReportComponent.EMPTY_REPORT;
		this.formGroup = this.formBuilder.group([]);
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.prepareTipConcediuSelectItems();
		this.prepareAngajatiSelectItems();
		this.prepareStatusSelectItems();
		this.prepareColumns();
	}

	private prepareColumns(): void {
			this.columns.push(this.buildColumn("angajat", this.translateUtils.translateLabel("REPORT_CERERI_CONCEDIU_ANGAJATI")));
			this.columns.push(this.buildColumn("tipConcediu", this.translateUtils.translateLabel("REPORT_CERERI_CONCEDIU_TIP_CONCEDIU")));
			this.columns.push(this.buildColumn("dataInitiere", this.translateUtils.translateLabel("REPORT_CERERI_CONCEDIU_DATA_INITIERE")));
			this.columns.push(this.buildColumn("dataInceput", this.translateUtils.translateLabel("REPORT_CERERI_CONCEDIU_DATA_INCEPUT")));
			this.columns.push(this.buildColumn("dataSfarsit", this.translateUtils.translateLabel("REPORT_CERERI_CONCEDIU_DATA_SFARSIT")));
			this.columns.push(this.buildColumn("status", this.translateUtils.translateLabel("REPORT_CERERI_CONCEDIU_STATUS")));
			this.columns.push(this.buildColumn("motivRespingere", this.translateUtils.translateLabel("REPORT_CERERI_CONCEDIU_MOTIV_RESPINGERE")));
		}
	
	buildColumn(field: string, header: string): Column {
			let column = new Column();
			column.header =header;
			column.field = field;
			return column;
	}
	
	public ngOnInit(): void {
		this.formGroup.addControl("deLaData", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("panaLaData", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("tipConcediu", new FormControl());
		this.formGroup.addControl("angajati", new FormControl());
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
		let reportFilter: CereriConcediuReportFilterModel = this.prepareFilterModel();
		this.reportService.getCereriConcediuReport(reportFilter, {
			onSuccess: (theReport: CereriConcediuReportModel): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = CereriConcediuReportComponent.EMPTY_REPORT;
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

	private prepareFilterModel(): CereriConcediuReportFilterModel {
		let filter: CereriConcediuReportFilterModel = new CereriConcediuReportFilterModel();
		
		filter.deLaData = this.deLaDataFormControl.value;
		filter.panaLaData = this.panaLaDataFormControl.value;
		filter.tipConcediu = this.tipConcediuFormControl.value;
		filter.angajatId = this.angajatiFormControl.value;
		filter.status = this.statusFormControl.value;
		return filter;
	}

	public onExportExcel(): void {
		alert("not implemented yet");
	}

	public get footerVisible(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.report) && ArrayUtils.isNotEmpty(this.report.rows);
	}

	public get deLaDataFormControl(): FormControl {
		return this.getFormControlByName("deLaData");
	}

	public get panaLaDataFormControl(): FormControl {
		return this.getFormControlByName("panaLaData");
	}

	public get tipConcediuFormControl(): FormControl {
		return this.getFormControlByName("tipConcediu");
	}

	public get angajatiFormControl(): FormControl {
		return this.getFormControlByName("angajati");
	}

	public get statusFormControl(): FormControl {
		return this.getFormControlByName("status");
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
			if (exportCell.field === "dataInitiere"
				|| exportCell.field === "dataInceput"
				|| exportCell.field === "dataSfarsit") {
				return DateUtils.formatForDisplay(exportCell.data);
			} else if (exportCell.field === "status") {
				return this.translateUtils.translateCode("LABELS." + exportCell.data);
			} else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_CERERI_CONCEDIU");
		this.dataTable.exportCSV();
	}
}