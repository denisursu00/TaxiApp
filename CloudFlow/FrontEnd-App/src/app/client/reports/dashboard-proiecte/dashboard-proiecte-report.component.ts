import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormControl, FormGroup } from "@angular/forms";
import { SelectItem } from "primeng/api";
import { AppError, DashboardProiecteReportBundle, DashboardProiecteReportFilterModel, DashboardProiecteReportRowModel, DateConstants, FormUtils, MessageDisplayer, NomenclatorConstants, NomenclatorService, ObjectUtils } from "../../../shared"; 
import { ReportService, TranslateUtils, ValueOfNomenclatorValueField, EvaluareGradDeRealizareProiectEnum, DateUtils, ProjectService } from "../../../shared";
import { Table } from "primeng/table";
import { Column } from "primeng/primeng";
import { ExportUtils } from "@app/shared/utils/export-utils";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";


@Component({
	selector: "app-dashboard-proiecte-report.component",
	templateUrl: "./dashboard-proiecte-report.component.html"
})
export class DashboardProiecteReportComponent implements OnInit {

	private static readonly EMPTY_REPORT = new DashboardProiecteReportRowModel();

	@ViewChild(Table)
    public dataTable: Table;  
	public columns: Column[] = [];	

	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private reportService: ReportService;
	private projectService: ProjectService;
	private nomenclatorService: NomenclatorService;

	public loading: boolean;
	public reportRows: DashboardProiecteReportRowModel[];

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public gradRealizareRangeValues: number[] = [0, 100];
	
	public abreviereProiectSelectItems: SelectItem[] ;
	public evaluareGradRealizareSelectItems: SelectItem[];
	public actiuniDeUrmatSelectItems: SelectItem[];
	public allActiuniDeUrmatSelectItems: SelectItem[];
	public responsabilProiectSelectItems: SelectItem[];
	public arieCuprindereSelectItems: SelectItem[];

	public rowGroupMetadata: any[];
	public booleanFilterTypes: SelectItem[];

	public dateFormat: string;
	public yearRange: string;
	
	public scrollHeight: string;

	public constructor(reportsService: ReportService, nomenclatorService: NomenclatorService, formBuilder: FormBuilder,
		messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils, projectService: ProjectService) {
		this.reportService = reportsService;
		this.nomenclatorService = nomenclatorService;
		this.formBuilder = formBuilder;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.projectService = projectService;
		this.init();
	}

	private init(): void {
		this.loading = false;
		this.reportRows = [];
		this.formGroup = this.formBuilder.group([]);
		this.yearRange = DateUtils.getDefaultYearRange();
		this.prepareFiltersData();
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.scrollHeight = (window.innerHeight - 200) + "px";
	    this.prepareColumns();
    }

    private prepareColumns(): any {
        this.columns.push(this.buildColumn("arieActivitateBancara", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_ARIE_ACTIVITATE_BANCARA")));
        this.columns.push(this.buildColumn("importantaActuala", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_IMPORTANTA_ACTUALA")));
        this.columns.push(this.buildColumn("denumireProiect", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_DENUMIRE_PROIECT")));
        this.columns.push(this.buildColumn("abreviereProiect", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_ABREVIERE_PROIECT")));
        this.columns.push(this.buildColumn("dataInitieriiForDisplay", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_DATA_INITIERII")));
        this.columns.push(this.buildColumn("termendeFinalizareForDisplay", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_TERMEN_FINALIZARE")));
        this.columns.push(this.buildColumn("gradRealizareEstimat", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_GRAD_REALIZARE_ESTIMAT")));
        this.columns.push(this.buildColumn("incadrareProiect", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_INCADRARE_PROIECT")));
        this.columns.push(this.buildColumn("gradRealizareProiect", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_GRAD_REALIZARE_PROIECT")));
        this.columns.push(this.buildColumn("nrZilePerioadaScursa", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_PERIOADA_SCURSA")));
        this.columns.push(this.buildColumn("nrZilePerioadaRamasa", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_PERIOADA_RAMASA")));
        this.columns.push(this.buildColumn("nrZileProiect", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_NR_ZILE_PROIECT")));
        this.columns.push(this.buildColumn("parametru1ForDisplay", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_PARAMETRU_1")));
        this.columns.push(this.buildColumn("parametru2ForDisplay", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_PARAMETRU_2")));
        this.columns.push(this.buildColumn("evaluareGradRealizareForDisplay", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_EVALUARE_GRAD_REALIZARE")));
        this.columns.push(this.buildColumn("actiuneDeUrmat", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_ACTIUNE_DE_URMAT")));
        this.columns.push(this.buildColumn("deadlineActiuneDeUrmatForDisplay", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_DEADLINE_ACTIUNE_DE_URMAT")));
        this.columns.push(this.buildColumn("prioritate", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_PRIORITATE")));
        this.columns.push(this.buildColumn("descriere", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_DESCRIERE")));
        this.columns.push(this.buildColumn("responsabilproiect", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_RESPONSABIL_PROIECT")));
        this.columns.push(this.buildColumn("delegatResponsabilArb", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_DELEGAT_RESPONSABIL_ARB")));
        this.columns.push(this.buildColumn("stadiuForDisplay", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_STADIU")));
        this.columns.push(this.buildColumn("impact", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_IMPACT")));
        this.columns.push(this.buildColumn("proiectInitiatDeARBDisplay", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_PROIECT_INITIAT_DE_ARB")));
        this.columns.push(this.buildColumn("arieDeCuprindereForDisplay", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_ARIE_DE_CUPRINDERE")));
        this.columns.push(this.buildColumn("proiectInitiatDeAltaEntitate", this.translateUtils.translateLabel("REPORT_DASHBOARD_PROIECTE_PROIECT_INITIAT_DE_ALTA_ENTITATE")));
    }
    
    buildColumn(field: string, header: string): Column {
        let column = new Column();
        column.header =header;
        column.field = field;
        return column;
    }

	private prepareFiltersData(): void {
			this.prepareBooleanFilterTypes();
		this.prepareSelectItemsDataFromBundle();
		this.evaluareGradRealizareSelectItems = [];
		this.evaluareGradRealizareSelectItems.push({value: EvaluareGradDeRealizareProiectEnum.NESATISFACATOR, label: this.translateUtils.translateLabel("PROJECT_GRAD_REALIZARE_NESATISFACTOR")});
		this.evaluareGradRealizareSelectItems.push({value: EvaluareGradDeRealizareProiectEnum.SATISFACATOR, label: this.translateUtils.translateLabel("PROJECT_GRAD_REALIZARE_SATISFACTOR")});
		this.evaluareGradRealizareSelectItems.push({value: EvaluareGradDeRealizareProiectEnum.BINE, label: this.translateUtils.translateLabel("PROJECT_GRAD_REALIZARE_BINE")});
		this.evaluareGradRealizareSelectItems.push({value: EvaluareGradDeRealizareProiectEnum.FOARTE_BINE, label: this.translateUtils.translateLabel("PROJECT_GRAD_REALIZARE_FOARTE_BINE")});

		ListItemUtils.sortByLabel(this.evaluareGradRealizareSelectItems);
	}
	private prepareSelectItemsDataFromBundle() {
		this.reportService.getDashboardProiecteReportBundle({
			onSuccess: (bundle: DashboardProiecteReportBundle): void => {
				
				this.populateDDLFromBundle(bundle);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});

	}
	private populateDDLFromBundle(bundle: DashboardProiecteReportBundle): void {
		this.abreviereProiectSelectItems = [];
		this.actiuniDeUrmatSelectItems = [];
		this.responsabilProiectSelectItems = [];
		this.arieCuprindereSelectItems = [];

		if (ObjectUtils.isNotNullOrUndefined(bundle)) {
			if (ObjectUtils.isNotNullOrUndefined(bundle.abrevieriProiect)) {
				bundle.abrevieriProiect.forEach(element => {
					this.abreviereProiectSelectItems.push(element);
				});
			}
			if (ObjectUtils.isNotNullOrUndefined(bundle.tasks)) {
				bundle.tasks.forEach(element => {
					this.actiuniDeUrmatSelectItems.push(element);
				});
				this.allActiuniDeUrmatSelectItems = this.actiuniDeUrmatSelectItems;
			}
			if (ObjectUtils.isNotNullOrUndefined(bundle.responsibleUsers)) {
				bundle.responsibleUsers.forEach(element => {
					this.responsabilProiectSelectItems.push(element);
				});
			}
			if (ObjectUtils.isNotNullOrUndefined(bundle.arieDeCuprindereList)) {
				bundle.arieDeCuprindereList.forEach(element => {
					this.arieCuprindereSelectItems.push(element);
				});
			}
		}
		
		ListItemUtils.sortByLabel(this.abreviereProiectSelectItems);
		ListItemUtils.sortByLabel(this.actiuniDeUrmatSelectItems);
		ListItemUtils.sortByLabel(this.responsabilProiectSelectItems);
		ListItemUtils.sortByLabel(this.arieCuprindereSelectItems);
		
		this.arieActivitateBancaraFormControl.setValue(new ValueOfNomenclatorValueField(bundle.nomenclatorDomeniiBancareId));
		this.importantaActualaFormControl.setValue(new ValueOfNomenclatorValueField(bundle.nomenclatorImportantaProiecteId));
		this.incadrareProiectFormControl.setValue(new ValueOfNomenclatorValueField(bundle.nomenclatorIncadrariProiecteId));	
	
	}

	private prepareBooleanFilterTypes(): void {
		this.booleanFilterTypes = [
			{ label: this.translateUtils.translateLabel("YES"), value: true },
			{ label: this.translateUtils.translateLabel("NO"), value: false }
		];
	}

	public onChangeAbreviereProiect(event: any): void {
		let abreviereProiect: string = event.value;

		this.actiuniDeUrmatSelectItems = [];

		if (ObjectUtils.isNotNullOrUndefined(abreviereProiect)) {
			this.projectService.getAllInProgressTaskNamesByProjectAbbreviation(abreviereProiect, {
				onSuccess: (taskNames: string[]): void => {
					taskNames.forEach(taskName => {
						this.actiuniDeUrmatSelectItems.push({label: taskName, value: taskName});
					});

					ListItemUtils.sortByLabel(this.actiuniDeUrmatSelectItems);
				},
				onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
				}
			});
		} else {
			this.actiuniDeUrmatSelectItems = this.allActiuniDeUrmatSelectItems;
		}		
	}

	public ngOnInit(): void {

		this.addFormControls();
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
		let reportFilter: DashboardProiecteReportFilterModel = this.prepareFilterModel();
		this.reportService.getDashboardProiecteReport(reportFilter, {
			onSuccess: (theReport: DashboardProiecteReportRowModel[]): void => {
				if (ObjectUtils.isNullOrUndefined(theReport)) {
					theReport = [];
				}
				this.updateRowGroupMetaData(theReport);
				this.reportRows = theReport;
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

	private prepareFilterModel(): DashboardProiecteReportFilterModel {
		let filter: DashboardProiecteReportFilterModel = new DashboardProiecteReportFilterModel();
		
		if (ObjectUtils.isNotNullOrUndefined(this.arieActivitateBancaraFormControl.value.value)) {
			filter.arieActivitateBancaraId = this.arieActivitateBancaraFormControl.value.value;
		}
		if (ObjectUtils.isNotNullOrUndefined(this.importantaActualaFormControl.value.value)) {
			filter.importantaId = this.importantaActualaFormControl.value.value;
		}
		filter.abreviereProiect = this.abreviereProiectFormControl.value;
		filter.termenFinalizareDeLa = this.termenFinalizareDeLaFormControl.value;
		filter.termenFinalizarePanaLa = this.termenFinalizarePanaLaFormControl.value;
		if (ObjectUtils.isNotNullOrUndefined(this.incadrareProiectFormControl.value.value)) {
			filter.incadrareProiectId = this.incadrareProiectFormControl.value.value;
		}
		filter.gradRealizareProiectDeLa = this.gradRealizareRangeValues[0];
		filter.gradRealizareProiectPanaLa = this.gradRealizareRangeValues[1];
		
		if (ObjectUtils.isNotNullOrUndefined(this.actiuniDeUrmatFormControl.value)) {
			filter.actiuneDeUrmatId =  this.actiuniDeUrmatFormControl.value;
		} else {
			filter.actiuneDeUrmatId = null;
		}
		filter.deadlineActiuniDeUrmatDeLa = this.deadlineActiuneDeLaFormControl.value;
		filter.deadlineActiuniDeUrmatPanaLa = this.deadlineActiunePanaLaFormControl.value;
		if (ObjectUtils.isNotNullOrUndefined(this.responsabilProiectFormControl.value)) {
			filter.idUserResponsabil = Number(this.responsabilProiectFormControl.value);
		}
		if (ObjectUtils.isNotNullOrUndefined(this.proiectInitiatArbFormControl.value)) {
			filter.proiectInitiatDeARB = this.proiectInitiatArbFormControl.value;
		}
		filter.ariaDeCuprindere = this.arieCuprindereFormControl.value;

		filter.evaluareGradDeRealizare = this.evaluareGradRealizareFormControl.value;		
	
		return filter;
	}

	public get footerVisible(): boolean {
		return false;
	}


	public updateRowGroupMetaData(reportRows: DashboardProiecteReportRowModel[]): void {
		this.rowGroupMetadata = [];

		if (reportRows) {
			reportRows.forEach((row, index) => {
				let denumireProiect = row.denumireProiect;

				if (index === 0) {
					this.rowGroupMetadata[denumireProiect] = { index: 0, size: 1 };
				} else {
					let previousRow = reportRows[index - 1];

					if (denumireProiect === previousRow.denumireProiect) {
						this.rowGroupMetadata[denumireProiect].size++;
					} else {
						this.rowGroupMetadata[denumireProiect] = { index: index, size: 1 };
					}
				}
			});
		}
	}

	private addFormControls(): void {
		this.formGroup.addControl("arieActivitateBancara", new FormControl());
		this.formGroup.addControl("importantaActuala", new FormControl());
		this.formGroup.addControl("abreviereProiect", new FormControl());
		this.formGroup.addControl("termenFinalizareDeLa", new FormControl());
		this.formGroup.addControl("termenFinalizarePanaLa", new FormControl());
		this.formGroup.addControl("incadrareProiect", new FormControl());
		this.formGroup.addControl("evaluareGradRealizare", new FormControl());
		this.formGroup.addControl("actiuniDeUrmat", new FormControl());
		this.formGroup.addControl("deadlineActiuneDeLa", new FormControl());
		this.formGroup.addControl("deadlineActiunePanaLa", new FormControl());
		this.formGroup.addControl("responsabilProiect", new FormControl());
		this.formGroup.addControl("proiectInitiatArb", new FormControl());
		this.formGroup.addControl("arieCuprindere", new FormControl());
	}

	public get arieActivitateBancaraFormControl(): FormControl {
		return this.getFormControlByName("arieActivitateBancara");
	}
	public get importantaActualaFormControl(): FormControl {
		return this.getFormControlByName("importantaActuala");
	}
	public get abreviereProiectFormControl(): FormControl {
		return this.getFormControlByName("abreviereProiect");
	}
	public get termenFinalizareDeLaFormControl(): FormControl {
		return this.getFormControlByName("termenFinalizareDeLa");
	}
	public get termenFinalizarePanaLaFormControl(): FormControl {
		return this.getFormControlByName("termenFinalizarePanaLa");
	}
	public get incadrareProiectFormControl(): FormControl {
		return this.getFormControlByName("incadrareProiect");
	}
	public get evaluareGradRealizareFormControl(): FormControl {
		return this.getFormControlByName("evaluareGradRealizare");
	}
	public get actiuniDeUrmatFormControl(): FormControl {
		return this.getFormControlByName("actiuniDeUrmat");
	}
	public get deadlineActiuneDeLaFormControl(): FormControl {
		return this.getFormControlByName("deadlineActiuneDeLa");
	}
	public get deadlineActiunePanaLaFormControl(): FormControl {
		return this.getFormControlByName("deadlineActiunePanaLa");
	}
	public get responsabilProiectFormControl(): FormControl {
		return this.getFormControlByName("responsabilProiect");
	}
	public get proiectInitiatArbFormControl(): FormControl {
		return this.getFormControlByName("proiectInitiatArb");
	}
	public get arieCuprindereFormControl(): FormControl {
		return this.getFormControlByName("arieCuprindere");
	}

	private getFormControlByName(name: string): FormControl {
		return <FormControl>this.formGroup.get(name);
	}

	public exportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.dataTable)) {
			return;
		}
		this.dataTable.exportFunction = (exportCell: any) => {
			exportCell.data = ExportUtils.replaceQuotationMarkIfStringData( exportCell.data);
			if (exportCell.field === "parametru1ForDisplay" 
				|| exportCell.field === "parametru2ForDisplay" 
				|| exportCell.field === "evaluareGradRealizareForDisplay" 
				|| exportCell.field === "stadiuForDisplay" 
				|| exportCell.field === "proiectInitiatDeARBDisplay" 
				|| exportCell.field === "arieDeCuprindereForDisplay" ) {
				return this.translateUtils.translateCode(exportCell.data);
			} else {
				return exportCell.data;
			}
		}
		this.dataTable.exportFilename = this.translateUtils.translateCode("LABELS.REPORT_DASHBOARD_PROIECTE");
		this.dataTable.exportCSV();
	}
}
