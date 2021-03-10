import { Component, ViewChild } from "@angular/core";
import { Table } from "primeng/table";
import { MessageDisplayer, AppError, ConfirmationUtils, DateConstants, DateUtils, NomenclatorService, NomenclatorAttributeModel, JoinedNomenclatorUiAttributesValueModel, TranslateUtils } from "@app/shared";
import { NomenclatorModel, ObjectUtils, ArrayUtils, ComponentPermissionsWrapper, ClientPermissionEnum } from "@app/shared";
import { Column, SelectItem } from "primeng/primeng";
import { RegistruDocumenteJustificativePlatiService } from "@app/shared/service/registru-documente-justificative-plati.service";
import { RegistruDocumenteJustificativePlatiModel, ModLivrareEnum, ModalitatePlataEnum, MonthsEnum } from "@app/shared/model/registru-documente-justificative-plati/registru-documente-justificative-plati.model";
import { RegistruDocumenteJustificativePlatiWindowComponent } from "./registru-documente-justificative-plati-window";
import { AuthManager } from "@app/shared/auth";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-registru-documente-justificative-plati",
	templateUrl: "./registru-documente-justificative-plati.component.html"
})
export class RegistruDocumenteJustificativePlatiComponent implements ComponentPermissionsWrapper {

	private static readonly ADD_PERMISSION: string = ClientPermissionEnum.EDIT_REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI;
	private static readonly EDIT_PERMISSION: string = ClientPermissionEnum.EDIT_REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI;
	private static readonly DELETE_PERMISSION: string = ClientPermissionEnum.EDIT_REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI;

	@ViewChild(RegistruDocumenteJustificativePlatiWindowComponent)
	public registruDocumenteJustificativePlatiComponent: RegistruDocumenteJustificativePlatiWindowComponent;

	@ViewChild(Table)
	public registruDocumenteJustificativePlatiDataTable: Table;

	public registruDocumenteJustificativePlati: RegistruDocumenteJustificativePlatiModel[];
	public registruId: number;
	
	public selectionMode: string = "single";
	
	public addButtonEnabled: boolean = false;
	public editButtonEnabled: boolean = false;
	public cancelButtonEnabled: boolean = false;
	public viewButtonEnabled: boolean = false;
	public refreshButtonEnabled: boolean = false;
	public exportCSVButtonEnabled: boolean = false;

	public selectedData: RegistruDocumenteJustificativePlatiModel;
	
	public tableVisible: boolean = false;

	public columns: Column[] = [];
	
	public pageOffset: number = 0;
	public pageRows: number = 10;

	public dataLoading: boolean;

	public registruDocumenteJustificativePlatiWindowVisible: boolean = false;
	public registruDocumenteJustificativePlatiWindowMode: string;

	public anulareDocumentJustificativPlatiWindowVisible: boolean = false;
	public anulareDocumentJustificativPlatiWindowMode: string;

	public dateFormat: String;
	public yearRange: String;

	public monthsItems: SelectItem[];
	public tipDocumentNomenclatorFilterData: SelectItem[];
	public monedeNomenclatorFilterData: SelectItem[];
	public modLivrareItems: SelectItem[];
	public modalitatePlataItems: SelectItem[];
	public booleanFilterTypes: SelectItem[];

	private registruDocumenteJustificativePlatiService: RegistruDocumenteJustificativePlatiService;
	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private authManager: AuthManager;

	public selectedLunaInregistrareFilterValue: string[];
	public selectedNumarInregistrareFilterValue: string;
	public selectedDataInregistrareFilterValue: Date;
	public selectedEmitentFilterValue: string;
	public selectedTipDocumentFilterValue: string[];
	public selectedNumarDocumentFilterValue: number;
	public selectedDataDocumentFilterValue: Date;
	public selectedModLivrareFilterValue: string[];
	public selectedDetaliiFilterValue: string;
	public selectedValoareFilterValue: number;
	public selectedMonedaFilterValue: string[];
	public selectedDataScadentaFilterValue: Date;
	public selectedModalitatePlataFilterValue: string[];
	public selectedReconciliereCuExtrasBancaFilterValue: boolean[];
	public selectedPlatitFilterValue: boolean[];
	public selectedDataPlatiiFilterValue: Date;
	public selectedIncadrareConformBVCFilterValue: string;
	public selectedIntrareEmitereFilterValue: number;
	public selectedPlataScadentaFilterValue: number;
	public selectedScadentaEmitereFilterValue: number;
	public selectedAnulatFilterValue: boolean[];
	public selectedMotivAnulareFilterValue: string;

	public rowGroupMetadata: any;

	public years: number[];
	public yearItems: SelectItem[];
	public selectedYear: number;

	public scrollHeight: string;

	public constructor(registruDocumenteJustificativePlatiService: RegistruDocumenteJustificativePlatiService, 
			nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils, 
			authManager: AuthManager) {
		this.registruDocumenteJustificativePlatiService = registruDocumenteJustificativePlatiService;
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.authManager = authManager;
		this.init();
	}

	private init(): void {
		this.tableVisible = true;
		this.registruDocumenteJustificativePlati = [];
		this.tipDocumentNomenclatorFilterData = [];
		this.monedeNomenclatorFilterData = [];
		this.years = [];
		this.yearItems = [];

		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();

		this.scrollHeight = (window.innerHeight - 350) + "px";

		this.lockTable();
		this.prepareColumns();
		this.getNomenclatorIdByCodeAsMap(["registru_facturi_tip_document", "monede"]);
		this.prepareMonthsItems();
		this.prepareModLivrareItems();
		this.prepareModalitatePlataItems();
		this.prepareBooleanFilterTypes();
		this.getYearsWithInregistrariDocumenteJustificativePlati();		
		this.updatePerspectiveOfButtons();
	}

	private prepareColumns(): void {
		this.columns = [];
		this.columns.push(
			// TODO - Paginare, sortare, filtrare pe server + grupare pe lunile anului selectat
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_LUNA_INREGISTRARE", "lunaInregistrareForDisplay", "lunaInregistrareForFilter"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_NUMAR_INREGISTRARE", "numarInregistrareForDisplay", "numarInregistrareForFilter"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_DATA_INREGISTRARE", "dataInregistrareForDisplay", "dataInregistrare"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_EMITENT", "emitent", "emitent"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_TIP_DOCUMENT", "tipDocumentForDisplay", "tipDocumentForDisplay"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_NUMAR_DOCUMENT", "numarDocument", "numarDocument"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_DATA_DOCUMENT", "dataDocumentForDisplay", "dataDocument"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_MOD_LIVRARE", "modLivrareForDisplay", "modLivrare"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_DETALII", "detalii", "detalii"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_VALOARE", "valoareForDisplay", "valoare"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_MONEDA", "monedaForDisplay", "monedaForDisplay"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_DATA_SCADENTA", "dataScadentaForDisplay", "dataScadenta"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_MODALITATE_PLATA", "modalitatePlataForDisplay", "modalitatePlata"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_RECONCILIERE_CU_EXTRAS_BANCA", "reconciliereCuExtrasBancaForDisplay", "reconciliereCuExtrasBanca"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_PLATIT", "platitForDisplay", "platit"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_DATA_PLATII", "dataPlatiiForDisplay", "dataPlatii"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_INCADRARE_CONFORM_BVC", "incadrareConformBVC", "incadrareConformBVC"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_INTRARE_EMITERE", "intrareEmitere", "intrareEmitere"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_PLATA_SCADENTA", "plataScadenta", "plataScadenta"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_SCADENTA_EMITERE", "scadentaEmitere", "scadentaEmitere"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_ANULAT", "anulatForDisplay", "anulat"),
			this.buildColumn("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_MOTIV_ANULARE", "motivAnulare", "motivAnulare")
		);
	}

	private buildColumn(headerAsLabelCode: string, field: string, filterField: string): Column {
		let column: Column = new Column();
		column.header = this.translateUtils.translateLabel(headerAsLabelCode);
		column.field = field;	
		column.filterField = filterField;	
		return column;
	}

	private getYearsWithInregistrariDocumenteJustificativePlati(): void {
		this.registruDocumenteJustificativePlatiService.getYearsWithInregistrariDocumenteJustificativePlati({
			onSuccess: (years: number[]) => {
				this.years = years;
				this.prepareYearItems();
				let currentYear: number = new Date().getFullYear();
				this.getAllByYear(currentYear);
				this.selectedYear = currentYear;
			},
			onFailure: (appError: AppError) => {
				this.unlockTable();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareYearItems(): void {
		this.years.forEach(year => {
			let yearItem = { label: year.toString(), value: year };
			this.yearItems.push(yearItem);
		});
		
		let currentYearItem: SelectItem = { label: new Date().getFullYear().toString(), value: new Date().getFullYear() };
		let yearItemsWithCurrentYearItem: SelectItem[] = this.yearItems.filter(yearItem => yearItem.value === currentYearItem.value);
		if (ArrayUtils.isEmpty(yearItemsWithCurrentYearItem)) {
			this.yearItems.push(currentYearItem);
		}
	}

	private getAllByYear(year: number): void {
		this.registruDocumenteJustificativePlatiService.getAllByYear(year, {
			onSuccess: (documenteJustificativePlati: RegistruDocumenteJustificativePlatiModel[]) => {
				this.registruDocumenteJustificativePlati = documenteJustificativePlati;
				this.unlockTable();
			},
			onFailure: (appError: AppError) => {
				this.unlockTable();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private getNomenclatorIdByCodeAsMap(codes: string[]):void {
		this.nomenclatorService.getNomenclatorIdByCodeAsMap(codes, {
			onSuccess: (nomenclatorIdByCode: object): void => {
				if (ObjectUtils.isNotNullOrUndefined(nomenclatorIdByCode)) {
					this.prepareTipDocumentNomenclatorFilterValues(nomenclatorIdByCode["registru_facturi_tip_document"]);
					this.prepareMonedeNomenclatorFilterValues(nomenclatorIdByCode["monede"]);
				}
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private prepareTipDocumentNomenclatorFilterValues(nomenclatorId: number): void {
		this.nomenclatorService.getListOfConcatenatedUiAttributesByNomenclatorId(nomenclatorId, {
			onSuccess: (concatenatedAttributesViewModels: JoinedNomenclatorUiAttributesValueModel[]): void => {
				this.tipDocumentNomenclatorFilterData = this.prepareNomenclatorFilterValuesSelectItems(concatenatedAttributesViewModels);

				ListItemUtils.sortByLabel(this.tipDocumentNomenclatorFilterData);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareMonedeNomenclatorFilterValues(nomenclatorId: number): void {
		this.nomenclatorService.getListOfConcatenatedUiAttributesByNomenclatorId(nomenclatorId, {
			onSuccess: (concatenatedAttributesViewModels: JoinedNomenclatorUiAttributesValueModel[]): void => {
				this.monedeNomenclatorFilterData = this.prepareNomenclatorFilterValuesSelectItems(concatenatedAttributesViewModels);
				
				ListItemUtils.sortByLabel(this.monedeNomenclatorFilterData);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
	
	private prepareNomenclatorFilterValuesSelectItems(concatenatedAttributesViewModels: JoinedNomenclatorUiAttributesValueModel[]): SelectItem[] {
		let selectItems: SelectItem[] = [];
		concatenatedAttributesViewModels.forEach((concatenatedAttributeViewModel: JoinedNomenclatorUiAttributesValueModel) => {
			selectItems.push(this.buildNomenclatorFilterValuesSelectItem(concatenatedAttributeViewModel));
		});
		return selectItems;
	}

	private buildNomenclatorFilterValuesSelectItem(concatenatedAttributeViewModel: JoinedNomenclatorUiAttributesValueModel): SelectItem {
		let selectItem: SelectItem = {
			label: concatenatedAttributeViewModel.value,
			value: concatenatedAttributeViewModel.value
		};
		return selectItem;
	}

	private prepareMonthsItems(): void {
		this.monthsItems = [
			{ label: this.translateUtils.translateLabel(MonthsEnum.JANUARY), value: 0},
			{ label: this.translateUtils.translateLabel(MonthsEnum.FEBRUARY), value: 1},
			{ label: this.translateUtils.translateLabel(MonthsEnum.MARCH), value:2 },
			{ label: this.translateUtils.translateLabel(MonthsEnum.APRIL), value: 3 },
			{ label: this.translateUtils.translateLabel(MonthsEnum.MAY), value: 4 },
			{ label: this.translateUtils.translateLabel(MonthsEnum.JUNE), value: 5 },
			{ label: this.translateUtils.translateLabel(MonthsEnum.JULY), value: 6 },
			{ label: this.translateUtils.translateLabel(MonthsEnum.AUGUST), value: 7 },
			{ label: this.translateUtils.translateLabel(MonthsEnum.SEPTEMBER), value: 8 },
			{ label: this.translateUtils.translateLabel(MonthsEnum.OCTOBER), value: 9 },
			{ label: this.translateUtils.translateLabel(MonthsEnum.NOVEMBER), value: 10 },
			{ label: this.translateUtils.translateLabel(MonthsEnum.DECEMBER), value: 11 }
		];
	}

	private prepareModLivrareItems(): void {
		this.modLivrareItems = [
			{ label: this.translateUtils.translateLabel(ModLivrareEnum.POSTA), value: ModLivrareEnum.POSTA },
			{ label: this.translateUtils.translateLabel(ModLivrareEnum.EMAIL), value: ModLivrareEnum.EMAIL },
			{ label: this.translateUtils.translateLabel(ModLivrareEnum.CURIER), value: ModLivrareEnum.CURIER },
			{ label: this.translateUtils.translateLabel(ModLivrareEnum.LIVRARE_EMITENT), value: ModLivrareEnum.LIVRARE_EMITENT }
		];
		
		ListItemUtils.sortByLabel(this.modLivrareItems);
	}

	private prepareModalitatePlataItems(): void {
		this.modalitatePlataItems = [
			{ label: this.translateUtils.translateLabel(ModalitatePlataEnum.INTERNET_BANKING), value: ModalitatePlataEnum.INTERNET_BANKING },
			{ label: this.translateUtils.translateLabel(ModalitatePlataEnum.ORDIN_DE_PLATA), value: ModalitatePlataEnum.ORDIN_DE_PLATA },
			{ label: this.translateUtils.translateLabel(ModalitatePlataEnum.NUMERAR), value: ModalitatePlataEnum.NUMERAR },
			{ label: this.translateUtils.translateLabel(ModalitatePlataEnum.CARD), value: ModalitatePlataEnum.CARD },
			{ label: this.translateUtils.translateLabel(ModalitatePlataEnum.DIRECT_DEBIT), value: ModalitatePlataEnum.DIRECT_DEBIT }
		];
		
		ListItemUtils.sortByLabel(this.modalitatePlataItems);
	}

	private prepareBooleanFilterTypes(): void {
		this.booleanFilterTypes = [
			{label: this.translateUtils.translateLabel("YES"), value: true},
			{label: this.translateUtils.translateLabel("NO"), value: false}
		];
	}

	private lockTable(): void {
		this.dataLoading = true;
	}

	private unlockTable(): void {
		this.dataLoading = false;
	}

	private updatePerspectiveOfButtons(): void {		
		this.disableAllButtons();
		this.refreshButtonEnabled = true;	
		this.exportCSVButtonEnabled = true;
		let isDataSelected: boolean = ObjectUtils.isNotNullOrUndefined(this.selectedData);	
		this.viewButtonEnabled = isDataSelected;		
		if (this.isAddPermissionAllowed()) {
			this.addButtonEnabled = true;
		}
		if (this.isEditPermissionAllowed() && isDataSelected) {
			if (!this.selectedData.anulat) {
				this.editButtonEnabled = !this.selectedData.platit;
				this.cancelButtonEnabled =  true;
			}
		}
	}

	private disableAllButtons(): void {
		this.addButtonEnabled = false;
		this.editButtonEnabled = false;
		this.viewButtonEnabled = false;
		this.cancelButtonEnabled = false;
		this.refreshButtonEnabled = false;
		this.exportCSVButtonEnabled = false;
	}

	public onDataSelected(event: any): void {
		this.updatePerspectiveOfButtons();
	}

	public onDataUnselected(event: any): void {
		this.updatePerspectiveOfButtons();
	}

	public refresh(): void {
		this.lockTable();
		this.resetTableFilters();
		this.registruDocumenteJustificativePlati = [];
		this.selectedData = null;
		this.pageRows = 10;
		this.pageOffset = 0;
		this.updatePerspectiveOfButtons();
		let currentYear: number = new Date().getFullYear();
		this.getAllByYear(currentYear);
		this.selectedYear = currentYear;
	}

	private resetTableFilters(): void {
		this.selectedLunaInregistrareFilterValue = null;
		this.selectedNumarInregistrareFilterValue = null;
		this.selectedDataInregistrareFilterValue = null;
		this.selectedEmitentFilterValue = null;
		this.selectedTipDocumentFilterValue = null;
		this.selectedNumarDocumentFilterValue = null;
		this.selectedDataDocumentFilterValue = null;
		this.selectedModLivrareFilterValue = null;
		this.selectedDetaliiFilterValue = null;
		this.selectedValoareFilterValue = null;
		this.selectedMonedaFilterValue = null;
		this.selectedDataScadentaFilterValue = null;
		this.selectedModalitatePlataFilterValue = null;
		this.selectedReconciliereCuExtrasBancaFilterValue = null;
		this.selectedPlatitFilterValue = null;
		this.selectedDataPlatiiFilterValue = null;
		this.selectedIncadrareConformBVCFilterValue = null;
		this.selectedIntrareEmitereFilterValue = null;
		this.selectedPlataScadentaFilterValue = null;
		this.selectedScadentaEmitereFilterValue = null;
		this.selectedAnulatFilterValue = null;
		this.selectedMotivAnulareFilterValue = null;
		this.registruDocumenteJustificativePlatiDataTable.reset();
	}

	public onAdd(event: any): void {
		this.registruId = null;
		this.registruDocumenteJustificativePlatiWindowMode = "add";
		this.registruDocumenteJustificativePlatiWindowVisible = true;
	}

	public onEdit(event: any): void {
		this.registruId = this.selectedData.id;
		this.registruDocumenteJustificativePlatiWindowMode = "edit";
		this.registruDocumenteJustificativePlatiWindowVisible = true;
	}

	public onView(event: any): void {
		this.registruId = this.selectedData.id;
		this.registruDocumenteJustificativePlatiWindowMode = "view";
		this.registruDocumenteJustificativePlatiWindowVisible = true;
	}

	public onCancel(event: any): void {
		this.anulareDocumentJustificativPlatiWindowVisible = true;
	}

	private cancelSelectedDocumentJustificativPlati(): void {
		this.registruDocumenteJustificativePlatiService.cancelDocumentJustificativPlati(this.selectedData, {
			onSuccess: () => {
				this.refresh();
				this.messageDisplayer.displaySuccess("REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI_CANCELED_SUCCESSFULLY");
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onRefresh(event: any): void {
		this.refresh();
	}

	public onExportCSV(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.registruDocumenteJustificativePlatiDataTable)) {
			return;
		}
		this.registruDocumenteJustificativePlatiDataTable.exportFilename = "Registru_Facturi";
		this.registruDocumenteJustificativePlatiDataTable.exportFunction = (exportCell: any) => {
			if (exportCell.field === "anulatForDisplay" 
					|| exportCell.field === "modLivrareForDisplay"
					|| exportCell.field === "modalitatePlataForDisplay"
					|| exportCell.field === "platitForDisplay"
					|| exportCell.field === "reconciliereCuExtrasBancaForDisplay"
					|| exportCell.field === "lunaInregistrareForDisplay") {
				return this.translateUtils.translateCode(exportCell.data);
			} else {
				return exportCell.data;
			}
		};
		this.registruDocumenteJustificativePlatiDataTable.exportCSV();
	}

	public onRegistruDocumenteJustificativePlatiWindowClosed(event: any): void {
		this.registruDocumenteJustificativePlatiWindowVisible = false;
		this.refresh();
	}

	public onAnulareDocumentJustificativPlatiWindowClosed(motivAnulare: string): void {
		this.anulareDocumentJustificativPlatiWindowVisible = false;
		if (ObjectUtils.isNotNullOrUndefined(motivAnulare)) {
			this.selectedData.motivAnulare = motivAnulare;
			this.cancelSelectedDocumentJustificativPlati();
		}
	}

	public onYearValueChanged(event: any): void {
		this.getAllByYear(this.selectedYear);
	}

	isAddPermissionAllowed(): boolean {
		return this.authManager.hasPermission(RegistruDocumenteJustificativePlatiComponent.ADD_PERMISSION);
	}

	isEditPermissionAllowed(): boolean {
		return this.authManager.hasPermission(RegistruDocumenteJustificativePlatiComponent.EDIT_PERMISSION);
	}

	isDeletePermissionAllowed(): boolean {
		return this.authManager.hasPermission(RegistruDocumenteJustificativePlatiComponent.DELETE_PERMISSION);
	}

	/*
	// TODO - Paginare, sortare, filtrare pe server + grupare pe lunile anului selectat
    public updateRowGroupMetaData(): void {
        this.rowGroupMetadata = {};
        if (this.registruDocumenteJustificativePlati) {
            for (let i = 0; i < this.registruDocumenteJustificativePlati.length; i++) {
                let rowData = this.registruDocumenteJustificativePlati[i];
                let lunaInregistrare = rowData.lunaInregistrare;
                if (i == 0) {
                    this.rowGroupMetadata[lunaInregistrare] = { index: 0, size: 1 };
                } else {
                    let previousRowData = this.registruDocumenteJustificativePlati[i - 1];
                    let previousLunaInregistrare = previousRowData.lunaInregistrare;
                    if (lunaInregistrare === previousLunaInregistrare) {
                        this.rowGroupMetadata[lunaInregistrare].size++;
					} else {
                        this.rowGroupMetadata[lunaInregistrare] = { index: i, size: 1 };
					}
                }
            }
        }
    }

	// TODO - Paginare, sortare, filtrare pe server + grupare pe lunile anului selectat
    // public onFilter(event: any): void {
    //     this.updateRowGroupMetaData();
    // }

    // public onPage(event: any): void {
    //     this.updateRowGroupMetaData();
    // }
	*/
}