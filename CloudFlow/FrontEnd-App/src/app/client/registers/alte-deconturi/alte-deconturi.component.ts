import { Component, ViewChild, OnInit } from "@angular/core";
import { AlteDeconturiService, MessageDisplayer, ObjectUtils, AppError, DateConstants, DateUtils, ArrayUtils, UiUtils, TranslateUtils, ConfirmationUtils, PageConstants, MonthEnum, ClientPermissionEnum, ComponentPermissionsWrapper, ConfirmationWindowFacade, PDialogMinimizer } from "@app/shared";
import { SelectItem, Column, Dialog } from "primeng/primeng";
import { Table } from "primeng/table";
import { AlteDeconturiViewModel, AlteDeconturiModel, AlteDeconturiCheltuieliViewModel, TipAvansPrimitEnum } from "@app/shared/model/alte-deconturi";
import { AuthManager } from "@app/shared/auth";

@Component({
	selector: "app-alte-deconturi",
	templateUrl: "./alte-deconturi.component.html"
})
export class AlteDeconturiComponent implements OnInit, ComponentPermissionsWrapper {
	
	private static readonly ADD_PERMISSION: string = ClientPermissionEnum.EDIT_ALTE_DECONTURI;
	private static readonly EDIT_PERMISSION: string = ClientPermissionEnum.EDIT_ALTE_DECONTURI;
	private static readonly DELETE_PERMISSION: string = ClientPermissionEnum.EDIT_ALTE_DECONTURI;
	
	private static readonly COLUMN_MONTH: string = "monthForDisplay";
	private static readonly COLUMN_TITULAR_DECONT: string = "titularDecont";
	private static readonly COLUMN_NUMAR_DECONT: string = "numarDecontForDisplay";
	private static readonly COLUMN_DATA_DECONT: string = "dataDecontForDisplay";
	private static readonly COLUMN_AVANS_PRIMIT: string = "avansPrimit";
	private static readonly COLUMN_TIP_AVANS_PRIMIT: string = "tipAvansPrimitForDisplay";
	private static readonly COLUMN_CHELTUIELI: string = "cheltuieli";
	private static readonly COLUMN_TOTAL_CHELTUIELI: string = "totalCheltuieli";
	private static readonly COLUMN_TOTAL_DE_INCASAT_RESTITUIT: string = "totalDeIncasatRestituit";
	private static readonly COLUMN_ANULAT: string = "anulatForDisplay";
	private static readonly COLUMN_MOTIV_ANULARE: string = "motivAnulare";
	private static readonly COLUMN_FINALIZAT: string = "finalizatForDisplay";
	
	private alteDeconturiService: AlteDeconturiService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private authManager: AuthManager;

	@ViewChild(Table)
	public alteDeconturiDataTable: Table;
	
	public tableVisible: boolean = false;
	public selectedData: AlteDeconturiViewModel;
	public columns: Column[];
	public alteDeconturi: AlteDeconturiModel;
	public alteDeconturiViewModels: AlteDeconturiViewModel[];
	public pageOffset: number = 0;
	public pageRows: number = 10;
	public rowsPerPageOptions: number[];
	public dataLoading: boolean;
	public booleanFilterTypes: SelectItem[];
	public dateFormat: String;
	public yearRange: String;
	
	public years: number[];
	public yearItems: SelectItem[];
	public selectedYear: number;

	public alteDeconturiWindowVisible: boolean = false;
	public decontId: number;
	public alteDeconturiMode: "add" | "view";

	public anulareAlteDeconturiWindowVisible: boolean;

	public addButtonEnabled: boolean = false;
	public viewButtonEnabled: boolean = false;
	public finalizeButtonEnabled: boolean = false;
	public deleteButtonEnabled: boolean = false;
	public cancelButtonEnabled: boolean = false;

	public alteDeconturiCheltuieliVisible = false;
	public alteDeconturiCheltuieliForView: AlteDeconturiCheltuieliViewModel[];

	public alteDeconturiCheltuieliWidth: number;
	public alteDeconturiCheltuieliHeight: number | string;

	public selectedMonthFilterValues: any[];
	public selectedTitularDecontFilterValue: string;
	public selectedNumarDecontFilterValue: string;
	public selectedDataDecontFilterValue: Date;
	public selectedAnulatBooleanFilterValues: any[];
	public selectedFinalizatBooleanFilterValues: any[];
	public selectedTipAvansPrimitFilterValues: any[];

	public monthFilterValues: SelectItem[];
	public tipAvansPrimitFilterValues: SelectItem[];

	public confirmationWindow: ConfirmationWindowFacade;

	public loadingVisible: boolean = false;
	public scrollHeight: string;
	public scrollHeightAlteDeconturiCheltuieli: string;
	private pDialogMinimizer: PDialogMinimizer;

	public constructor(alteDeconturiService: AlteDeconturiService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils,
			authManager: AuthManager) {
		this.alteDeconturiService = alteDeconturiService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.authManager = authManager;
		this.confirmationWindow = new ConfirmationWindowFacade();
		this.lockTable();
		this.init();
	}

	private lockTable(): void {
		this.dataLoading = true;
	}

	private unlockTable(): void {
		this.dataLoading = false;
	}

	private init(): void {
		this.tableVisible = true;
		this.alteDeconturiViewModels = [];
		this.booleanFilterTypes = [];
		this.years = [];
		this.yearItems = [];
		this.rowsPerPageOptions = PageConstants.DEAFAULT_ROW_PER_PAGE_OPTIONS;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.scrollHeight = (window.innerHeight - 350) + "px";
		this.scrollHeightAlteDeconturiCheltuieli = (window.innerHeight - 150) + "px";

		this.prepareColumns();
		this.adjustSize();
	}

	private prepareColumns(): void {
		this.columns = [];
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("MONTH"), AlteDeconturiComponent.COLUMN_MONTH, "NOMENCLATOR", "in", true, "month"));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("ALTE_DECONTURI_TITULAR_DECONT"), AlteDeconturiComponent.COLUMN_TITULAR_DECONT, "TEXT", "contains", true));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("ALTE_DECONTURI_NUMAR_DECONT"), AlteDeconturiComponent.COLUMN_NUMAR_DECONT, "TEXT", "contains", true));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("ALTE_DECONTURI_DATA_DECONT"), AlteDeconturiComponent.COLUMN_DATA_DECONT, "DATE", "equals", true, "dataDecont"));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("ALTE_DECONTURI_AVANS_PRIMIT"), AlteDeconturiComponent.COLUMN_AVANS_PRIMIT));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("ALTE_DECONTURI_TIP_AVANS_PRIMIT"), AlteDeconturiComponent.COLUMN_TIP_AVANS_PRIMIT, "NOMENCLATOR", "in", true, "tipAvansPrimit"));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("ALTE_DECONTURI_CHELTUIELI"), AlteDeconturiComponent.COLUMN_CHELTUIELI));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("ALTE_DECONTURI_TOTAL_CHELTUIELI"), AlteDeconturiComponent.COLUMN_TOTAL_CHELTUIELI));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("ALTE_DECONTURI_TOTAL_DE_INCASAT_RESTITUIT"), AlteDeconturiComponent.COLUMN_TOTAL_DE_INCASAT_RESTITUIT));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("ALTE_DECONTURI_ANULAT"), AlteDeconturiComponent.COLUMN_ANULAT, "BOOLEAN", "equals", true, "anulat"));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("ALTE_DECONTURI_MOTIV_ANULARE"), AlteDeconturiComponent.COLUMN_MOTIV_ANULARE));
		this.columns.push(this.buildColumn(this.translateUtils.translateLabel("ALTE_DECONTURI_FINALIZAT"), AlteDeconturiComponent.COLUMN_FINALIZAT, "BOOLEAN", "equals", true, "finalizat"));
	}

	private buildColumn(header: string, field: string, filterType?: string, filterMatchMode?: string, filter?: boolean, filterField?: string): Column {
		let column: Column = new Column();
		column.header = header;
		column.field = field;
		if (ObjectUtils.isNotNullOrUndefined(filterField)) {
			column.filterField = filterField;
		} else {
			column.filterField = field;
		}
		column.filterType = filterType;
		column.filterMatchMode = filterMatchMode;
		column.filter = filter;
		return column;
	}
	
	private adjustSize(): void {
		this.alteDeconturiCheltuieliHeight = "auto";
		this.alteDeconturiCheltuieliWidth = UiUtils.getDialogWidth();
	}

	public getNumberOfTotalRecords(): number {
		return ArrayUtils.isNotEmpty(this.alteDeconturiViewModels) ? this.alteDeconturiViewModels.length : 0;
	}

	public ngOnInit(): void {
		this.prepareFilterValues();
		this.changePerspective();
	}
	
	private prepareFilterValues(): void {
		this.prepareYearFilterValues();
		this.prepareMonthFilterValues();
		this.prepareTipAvansPrimitFilterValues();
		this.prepareBooleanFilterTypes();
	}

	private prepareYearFilterValues(): void {
		this.getYearsOfExistingDeconturi();
	}

	private getYearsOfExistingDeconturi(): void {
		this.alteDeconturiService.getYearsOfExistingDeconturi({
			onSuccess: (years: number[]): void => {
				this.years = years;
				this.prepareYearItems();
				let currentYear: number = new Date().getFullYear();
				this.selectedYear = currentYear;
				this.getAllAlteDeconturiViewModelsByYear(currentYear);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareYearItems(): void {
		this.years.forEach((year: number) => {
			let yearItem: SelectItem = { label: year.toString(), value: year };
			this.yearItems.push(yearItem);
		});
		
		let currentYearItem: SelectItem = { label: new Date().getFullYear().toString(), value: new Date().getFullYear() };
		let yearItemsWithCurrentYearItem: SelectItem[] = this.yearItems.filter(yearItem => yearItem.value === currentYearItem.value);
		if (ArrayUtils.isEmpty(yearItemsWithCurrentYearItem)) {
			this.yearItems.push(currentYearItem);
		}
	}

	private getAllAlteDeconturiViewModelsByYear(year: number): void {
		this.alteDeconturiService.getAllAlteDeconturiViewModelsByYear(year, {
			onSuccess: (alteDeconturiViewModels: AlteDeconturiViewModel[]): void => {
				this.alteDeconturiViewModels = alteDeconturiViewModels;
				this.unlockTable();
			},
			onFailure: (appError: AppError): void => {
				this.unlockTable();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareMonthFilterValues(): void {
		this.monthFilterValues = [
			{label: this.translateUtils.translateLabel(MonthEnum.JANUARY), value: MonthEnum.JANUARY},
			{label: this.translateUtils.translateLabel(MonthEnum.FEBRUARY), value: MonthEnum.FEBRUARY},
			{label: this.translateUtils.translateLabel(MonthEnum.MARCH), value: MonthEnum.MARCH},
			{label: this.translateUtils.translateLabel(MonthEnum.APRIL), value: MonthEnum.APRIL},
			{label: this.translateUtils.translateLabel(MonthEnum.MAY), value: MonthEnum.MAY},
			{label: this.translateUtils.translateLabel(MonthEnum.JUNE), value: MonthEnum.JUNE},
			{label: this.translateUtils.translateLabel(MonthEnum.JULY), value: MonthEnum.JULY},
			{label: this.translateUtils.translateLabel(MonthEnum.AUGUST), value: MonthEnum.AUGUST},
			{label: this.translateUtils.translateLabel(MonthEnum.SEPTEMBER), value: MonthEnum.SEPTEMBER},
			{label: this.translateUtils.translateLabel(MonthEnum.OCTOBER), value: MonthEnum.OCTOBER},
			{label: this.translateUtils.translateLabel(MonthEnum.NOVEMBER), value: MonthEnum.NOVEMBER},
			{label: this.translateUtils.translateLabel(MonthEnum.DECEMBER), value: MonthEnum.DECEMBER}
		];
	}

	private prepareTipAvansPrimitFilterValues(): void {
		this.tipAvansPrimitFilterValues = [
			{label: this.translateUtils.translateLabel(TipAvansPrimitEnum.CARD), value: TipAvansPrimitEnum.CARD},
			{label: this.translateUtils.translateLabel(TipAvansPrimitEnum.NUMERAR), value: TipAvansPrimitEnum.NUMERAR}
		];
	}

	private prepareBooleanFilterTypes(): void {
		this.booleanFilterTypes = [
			{label: this.translateUtils.translateLabel("YES"), value: true},
			{label: this.translateUtils.translateLabel("NO"), value: false}
		];
	}

	public booleanColumns(column: Column) {
		if (column.field === AlteDeconturiComponent.COLUMN_ANULAT ||
				column.field === AlteDeconturiComponent.COLUMN_FINALIZAT) {
			return true;
		}
		return false;
	}
	
	public onDataSelected(event: any): void {
		this.decontId = this.selectedData.id;
		this.changePerspective();
	}

	public onDataUnselected(event: any): void {
		this.decontId = undefined;
		this.changePerspective();
	}

	public onYearValueChanged(event: any): void {
		this.getAllAlteDeconturiViewModelsByYear(this.selectedYear);
	}

	public refresh(): void {
		this.lockTable();
		this.resetTableFilters();
		this.alteDeconturiViewModels = [];
		this.selectedData = null;
		this.decontId = undefined;
		this.pageRows = 10;
		this.pageOffset = 0;
		let currentYear: number = new Date().getFullYear();
		this.selectedYear = currentYear;
		this.getAllAlteDeconturiViewModelsByYear(currentYear);
		this.changePerspective();
	}

	private resetTableFilters(): void {
		this.selectedMonthFilterValues = [];
		this.selectedTitularDecontFilterValue = null;
		this.selectedNumarDecontFilterValue = null;
		this.selectedDataDecontFilterValue = null;
		this.selectedTipAvansPrimitFilterValues = [];
		this.selectedAnulatBooleanFilterValues = [];
		this.selectedFinalizatBooleanFilterValues = [];
		this.alteDeconturiDataTable.reset();
	}	

	public onAdd(): void {
		this.alteDeconturiMode = "add";
		this.alteDeconturiWindowVisible = true;
	}

	public onView(): void {
		this.alteDeconturiMode = "view";
		this.alteDeconturiWindowVisible = true;
	}

	public onCancel(): void {
		this.anulareAlteDeconturiWindowVisible = true;
	}

	public onRefresh(): void {
		this.refresh();
	}

	public changePerspective(): void {
		this.disableAllButtons();
		this.addButtonEnabled = this.isAddPermissionAllowed();
		if (ObjectUtils.isNotNullOrUndefined(this.decontId)) {
			this.viewButtonEnabled = true;
			this.isDecontCanceled();
		}
	}

	private disableAllButtons(): void {
		this.addButtonEnabled = false;
		this.viewButtonEnabled = false;
		this.cancelButtonEnabled = false;
		this.finalizeButtonEnabled = false;
		this.deleteButtonEnabled = false;
	}

	// TODO - Ar trebui renunat la ea - verificat din model nu cu apel backend
	private isDecontCanceled(): void {
		this.loadingVisible = true;
		this.alteDeconturiService.isDecontCanceled(this.decontId, {
			onSuccess: (isCanceled: boolean): void => {
				this.isDecontFinalized(isCanceled);
			},
			onFailure: (appError: AppError): void => {
				this.loadingVisible = false;
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	// TODO - Ar trebui renunat la ea - verificat din model nu cu apel backend
	private isDecontFinalized(isCanceled: boolean): void {
		this.loadingVisible = true;
		this.alteDeconturiService.isDecontFinalized(this.decontId, {
			onSuccess: (isFinalized: boolean): void => {
				if (isFinalized && !isCanceled) {
					this.cancelButtonEnabled = this.isEditPermissionAllowed();
				} else if (!isFinalized && !isCanceled) {
					this.finalizeButtonEnabled = this.isEditPermissionAllowed();
					this.deleteButtonEnabled = this.isDeletePermissionAllowed();
				}
				this.loadingVisible = false;
			},
			onFailure: (appError: AppError): void => {
				this.loadingVisible = false;
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onAlteDeconturiWindowClosed(): void {
		this.alteDeconturiWindowVisible = false;
		this.refresh();
	}

	public onAnulareAlteDeconturiWindowClosed(motivAnulare: string): void {
		this.anulareAlteDeconturiWindowVisible = false;
		this.refresh();
	}

	public onViewCheltuieli(cheltuieli: AlteDeconturiCheltuieliViewModel[]): void {
		this.alteDeconturiCheltuieliForView = cheltuieli;
		this.alteDeconturiCheltuieliVisible = true;
	}

	public onCloseAlteDeconturiCheltuieli(event: any): void {
		this.alteDeconturiCheltuieliVisible = false;
	}

	public onShowAlteDeconturiCheltuieli(event: any, pDialog: Dialog): void {
		this.pDialogMinimizer = new PDialogMinimizer(pDialog);
		pDialog.center();
		setTimeout(() => {
			pDialog.maximize();
		}, 0);
	}
	
	public onToggleMinimizeAlteDeconturiCheltuieli(pDialog: Dialog): void {
		this.pDialogMinimizer.toggleMinimize();		
	}

	public get alteDeconturiCheltuieliMinimized(): boolean {
		if (ObjectUtils.isNullOrUndefined(this.pDialogMinimizer)) {
			return false;
		}
		return this.pDialogMinimizer.minimized;
	}

	public isColumnTranslatable(column: Column) {
		return column.field === AlteDeconturiComponent.COLUMN_ANULAT ||
			column.field === AlteDeconturiComponent.COLUMN_TIP_AVANS_PRIMIT ||
			column.field === AlteDeconturiComponent.COLUMN_MONTH ||
			column.field === AlteDeconturiComponent.COLUMN_FINALIZAT;
	}

	public isCheltuieliColumn(column: Column): boolean {
		return column.field === AlteDeconturiComponent.COLUMN_CHELTUIELI; 
	}

	public onRemove(): void {
		this.confirmationWindow.confirm({
			approve: (): void => {
				this.confirmationWindow.hide();
				this.deleteDecontById(this.selectedData.id);
			},
			reject: (): void => {
				this.confirmationWindow.hide();
			}
		}, "CONFIRM_DELETE_DECONT");
	}

	private deleteDecontById(decontId: number): void {
		this.loadingVisible = true;
		this.alteDeconturiService.deleteDecontById(decontId, {
			onSuccess: (): void => {
				this.loadingVisible = false;
				this.messageDisplayer.displaySuccess("DECONT_DELETED_SUCCESSFULLY");
				this.refresh();
			},
			onFailure: (appError: AppError): void => {
				this.loadingVisible = false;
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onExportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.alteDeconturiDataTable)) {
			return;
		}
		this.alteDeconturiDataTable.exportFilename = "Alte_Deconturi";
		this.alteDeconturiDataTable.exportFunction = (exportCell) => {
			if (exportCell.field === AlteDeconturiComponent.COLUMN_MONTH 
					|| exportCell.field === AlteDeconturiComponent.COLUMN_TIP_AVANS_PRIMIT 
					|| exportCell.field === AlteDeconturiComponent.COLUMN_ANULAT
					|| exportCell.field === AlteDeconturiComponent.COLUMN_FINALIZAT) {
				return this.translateUtils.translateCode(exportCell.data);
			} else if (exportCell.field === AlteDeconturiComponent.COLUMN_CHELTUIELI) {
				let cheltuieli: AlteDeconturiCheltuieliViewModel[] = exportCell.data;
				let joinedCheltuieli: string = "";
				if (ArrayUtils.isNotEmpty(cheltuieli)) {
					cheltuieli.forEach((cheltuiala: AlteDeconturiCheltuieliViewModel, index: number) => {
						if (index > 0) {
							joinedCheltuieli += "; ";
						}
						joinedCheltuieli += this.translateUtils.translateLabel(cheltuiala.tipDocumentJustificativ) + " - " + cheltuiala.valoareCheltuiala.toString();
					});
				}
				return joinedCheltuieli;
			}
			return exportCell.data;
		};
		this.alteDeconturiDataTable.exportCSV();
	}

	public onFinalize(): void {
		this.confirmationWindow.confirm({
			approve: (): void => {
				this.confirmationWindow.hide();
				this.finalizeDecontById(this.selectedData.id);
			},
			reject: (): void => {
				this.confirmationWindow.hide();
			}
		}, "CONFIRM_FINALIZE_DECONT");
	}

	private finalizeDecontById(decontId: number): void {
		this.loadingVisible = true;
		this.alteDeconturiService.finalizeDecontById(decontId, {
			onSuccess: (): void => {
				this.loadingVisible = false;
				this.messageDisplayer.displaySuccess("DECONT_FINALIZED_SUCCESSFULLY");
				this.refresh();
			},
			onFailure: (appError: AppError): void => {
				this.loadingVisible = false;
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	isAddPermissionAllowed(): boolean {
		return this.authManager.hasPermission(AlteDeconturiComponent.ADD_PERMISSION);
	}

	isEditPermissionAllowed(): boolean {
		return this.authManager.hasPermission(AlteDeconturiComponent.EDIT_PERMISSION);
	}

	isDeletePermissionAllowed(): boolean {
		return this.authManager.hasPermission(AlteDeconturiComponent.DELETE_PERMISSION);
	}
}