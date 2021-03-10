import { Component, OnInit, Output, EventEmitter, Input, ViewChild} from "@angular/core";
import { RegistruIntrariIesiriService, AppError, MessageDisplayer, ArrayUtils, NomenclatorService, ObjectUtils, NomenclatorConstants, 
	JoinedNomenclatorUiAttributesValueModel, RegistruIesiriModel, TranslateUtils, DateConstants, DateUtils, RegistruIesiriFilterModel, PDialogMinimizer, RegistruIesiriViewModelPagingList, StringUtils } from "@app/shared";
import { RegistruIesiriViewModel } from "@app/shared/model/registru-intrari-iesiri/registru-iesiri-view.model";
import { Column, SelectItem, Dialog } from "primeng/primeng";
import { RegistruIesiriDestinatarViewModel } from "@app/shared/model/registru-intrari-iesiri/registru-iesiri-destinatari-view.model";
import { Table } from "primeng/table";
import { Page } from "@app/shared/model/page";
import { PageConstants} from "@app/shared/constants";

@Component({
	selector: "app-registru-iesiri-selector",
	templateUrl: "./registru-iesiri-selector.component.html",
	styleUrls: ["./registru-iesiri-selector.component.css"]
})
export class RegistruIesiriSelectorComponent implements OnInit {

	private static readonly COLUMN_NUMAR_INREGISTRARE: string = "numarInregistrareForDisplay";	
	private static readonly COLUMN_NUMAR_INREGISTRARE_FOR_FILTER: string = "numarInregistrare";	
	private static readonly COLUMN_LUNA: string = "luna";
	private static readonly COLUMN_LUNA_FOR_DISPLAY: string = "lunaForDisplay";
	private static readonly COLUMN_DATA_INREGISTRARE: string = "dataInregistrareForDisplay";
	private static readonly COLUMN_DATA_INREGISTRARE_FOR_FILTER: string = "dataInregistrare";
	private static readonly COLUMN_DESTINATARI: string = "destinatari";
	private static readonly COLUMN_TIP_DOCUMENT: string = "tipDocument";
	private static readonly COLUMN_COD_TIP_DOCUMENT: string = "codTipDocument";
	private static readonly COLUMN_TRIMIS_PE_MAIL: string = "trimisPeMailForDisplay";
	private static readonly COLUMN_CONTINUT: string = "continut";
	private static readonly COLUMN_NUMAR_PAGINI: string = "numarPagini";
	private static readonly COLUMN_NUMAR_ANEXE: string = "numarAnexe";
	private static readonly COLUMN_INTOCMIT_DE_USER: string = "intocmitDeUser";
	private static readonly COLUMN_PROIECT: string = "numeProiecteConcatenate";
	private static readonly COLUMN_NECESITA_RASPUNS: string = "necesitaRaspunsForDisplay";
	private static readonly COLUMN_TERMEN_RASPUNS: string = "termenRaspunsForDisplay";
	private static readonly COLUMN_ANULAT: string = "anulatForDisplay";
	private static readonly COLUMN_MOTIV_ANULARE: string = "motivAnulare";
	private static readonly COLUMN_FINALIZARE: string = "finalizatForDisplay";
	private static readonly COLUMN_SUBACTIVITY_NAME: string = "subactivityName";

	@Input()
	public inputData: RegistruIesiriSelectorInputData;

	@Output()
	public selectionChanged: EventEmitter<number>;
	
	@ViewChild(Table)
	public registruIesiriDataTable: Table;

	@ViewChild("pDialog")
	public destinatariWindow: Dialog;

	public destinatariFilterWindowVisible: Boolean;
	public destinatariViewWindowHeader: string;

	private registruIntrariIesiriService: RegistruIntrariIesiriService;
	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	public tableVisible: boolean = false;
	public columns: Column[];
	public rowsPerPageOptions: number[];
	public pageOffset: number;
	public dataLoading: boolean;
	public pageData: Page<RegistruIesiriViewModel>;


	public selectedData: RegistruIesiriViewModel;

	public nomenclatorFilterData: any[];

	public registruIesiriDestinatariVisible = false;
	public registruIesiriDestinatatriForView: RegistruIesiriDestinatarViewModel[];

	public years: number[];
	public yearItems: SelectItem[];
	public selectedYear: number;
	public dateFormat: String;
	public yearRange: String;
	
	private monthSelectItems: SelectItem[];
	
	public booleanFilterTypes: SelectItem[];
	public selectedFilterValueMap: Map<String, any>;
	public iesiriFilter: RegistruIesiriFilterModel;
	private filterHash: number;

	public scrollHeight: string;

	private pDialogMinimizer: PDialogMinimizer;
	
	public constructor(registruIntrariIesiriService: RegistruIntrariIesiriService, 
			nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer,
			translateUtils: TranslateUtils) {
		this.registruIntrariIesiriService = registruIntrariIesiriService;
		this.nomenclatorService = nomenclatorService;
		this.translateUtils = translateUtils;
		this.selectionChanged = new EventEmitter<number>();
		this.pageData = new Page<RegistruIesiriViewModel>();
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
		this.destinatariFilterWindowVisible = false;
		this.tableVisible = true;
		this.nomenclatorFilterData = [];
		this.years = [];
		this.yearItems = [];
		this.booleanFilterTypes = [];
		this.selectedFilterValueMap = new Map();
		this.scrollHeight = (window.innerHeight - 400) + "px";
		this.rowsPerPageOptions = PageConstants.DEAFAULT_ROW_PER_PAGE_OPTIONS;
		this.pageOffset = 0;
		this.pageData.pageSize = PageConstants.DEFAULT_PAGE_SIZE;
		
		let currentYear: number = new Date().getFullYear();
		this.selectedYear = currentYear;

		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();

		this.destinatariViewWindowHeader = this.translateUtils.translateLabel("REGISTRU_IESIRI_VIZUALIZARE_DESTINATARI");
	}

	public getNumberOfTotalRecords(): number {
		return ArrayUtils.isNotEmpty(this.pageData.items) ? this.pageData.items.length : 0;
	}

	public ngOnInit(): void {	
		if (ObjectUtils.isNullOrUndefined(this.inputData)) {
			throw new Error("input data pt. registru iesiri selector nu trebuie sa fie null");
		}
		this.pDialogMinimizer = new PDialogMinimizer(this.destinatariWindow);
		this.prepareFilterValues();
		this.prepareColumns();
		this.prepareMonthSelectItems();
	}

	private prepareYearFilterValues(): void {
		this.getYearsOfExistingIesiri();
		this.prepareBooleanFilterTypes();
	}

	private prepareBooleanFilterTypes(): void {
		this.booleanFilterTypes = [
			{label: this.translateUtils.translateLabel("YES"), value: true},
			{label: this.translateUtils.translateLabel("NO"), value: false}
		];
	}

	private prepareMonthSelectItems(): void {

		this.monthSelectItems = [];

		DateConstants.MONTHS.forEach((month, monthIndex) => {
			this.monthSelectItems.push({ label: this.translateUtils.translateLabel(month), value: monthIndex});
		});
	}

	private getYearsOfExistingIesiri(): void {
		this.registruIntrariIesiriService.getYearsOfExistingIesiri({
			onSuccess: (years: number[]): void => {
				this.years = years;
				this.prepareYearItems();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareYearItems(): void {
		this.yearItems = [];
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

	private loadRegistruIesiriViewModelsByFilter(filter: RegistruIesiriFilterModel): void {
		if (this.inputData.isIesiriForIntrari && ObjectUtils.isNullOrUndefined(this.inputData.numeEmitent) && ObjectUtils.isNullOrUndefined(this.inputData.emitentId)){
			this.unlockTable();
		}else{
			this.registruIntrariIesiriService.getRegistruIesiriViewModelsByFilter(filter, {
				onSuccess: (page: RegistruIesiriViewModelPagingList): void => {
					this.pageData.items = page.elements;
					this.pageData.totalItems = page.totalCount;
					this.prepareSelectedData(this.pageData.items);
					this.unlockTable();
				},
				onFailure: (appError: AppError): void => {
					this.unlockTable();
					this.messageDisplayer.displayAppError(appError);
				}
			});
		}
	}

	private prepareSelectedData(iesiri: RegistruIesiriViewModel[]): void {
		if (ObjectUtils.isNullOrUndefined(this.inputData.registruIesiriId)) {
			return;
		}
		this.registruIntrariIesiriService.getRegistruIesiri(this.inputData.registruIesiriId, {
			onSuccess: (registruIesiri: RegistruIesiriModel): void => {
				iesiri.forEach((iesire: RegistruIesiriViewModel) => {
					if (iesire.id === registruIesiri.id) {
						this.selectedData = iesire;
					}
				});
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public refresh(): void {
		this.selectedData = null;
		this.lockTable();
		this.pageOffset = 0;
		this.registruIesiriDataTable.first = 0;
		this.registruIesiriDataTable.sortField = RegistruIesiriSelectorComponent.COLUMN_NUMAR_INREGISTRARE_FOR_FILTER;
		this.registruIesiriDataTable.sortOrder = -1;
		let filter: RegistruIesiriFilterModel = this.getRequestFilterModel();
		filter.sortField = RegistruIesiriSelectorComponent.COLUMN_NUMAR_INREGISTRARE_FOR_FILTER;
		filter.isAscendingOrder = false;
		this.loadRegistruIesiriViewModelsByFilter(filter);
		this.selectionChanged.emit(null);
	}

	public onLazyLoad(event: any): void{
		this.pageOffset = event.first;
		this.pageData.pageSize = event.rows;
		let filter: RegistruIesiriFilterModel = this.getRequestFilterModel();
		if (ObjectUtils.isNullOrUndefined(event.sortField)){
			if (this.registruIesiriDataTable) {
				this.registruIesiriDataTable.sortField = RegistruIesiriSelectorComponent.COLUMN_NUMAR_INREGISTRARE_FOR_FILTER;
				this.registruIesiriDataTable.sortOrder = -1;
			}
			filter.sortField = RegistruIesiriSelectorComponent.COLUMN_NUMAR_INREGISTRARE_FOR_FILTER;
			filter.isAscendingOrder = false;
		}else{
			filter.sortField = event.sortField;
			filter.isAscendingOrder = event.sortOrder !== 1 ? false : true;
		}
		this.loadRegistruIesiriViewModelsByFilter(filter);
	}

	private getRequestFilterModel(): RegistruIesiriFilterModel {
		let filter = new RegistruIesiriFilterModel();
		filter.year = this.selectedYear;
		filter.offset = this.pageOffset;
		filter.pageSize = this.pageData.pageSize;
		filter.nrInregistrare = this.selectedFilterValueMap.has(RegistruIesiriSelectorComponent.COLUMN_NUMAR_INREGISTRARE_FOR_FILTER) ?
			this.selectedFilterValueMap.get(RegistruIesiriSelectorComponent.COLUMN_NUMAR_INREGISTRARE_FOR_FILTER) : null;
		filter.selectedMonths = this.selectedFilterValueMap.has(RegistruIesiriSelectorComponent.COLUMN_LUNA) ?
			this.selectedFilterValueMap.get(RegistruIesiriSelectorComponent.COLUMN_LUNA) : null;
		filter.documentTypeIds = this.selectedFilterValueMap.has(RegistruIesiriSelectorComponent.COLUMN_TIP_DOCUMENT) ?
			this.selectedFilterValueMap.get(RegistruIesiriSelectorComponent.COLUMN_TIP_DOCUMENT) : null;
		if (this.selectedFilterValueMap.has("trimisPeMail")){
			let boolValues: Array<any> = this.selectedFilterValueMap.get("trimisPeMail");
			filter.isMailed = boolValues.length === 1 ? boolValues[0] : null;
		}
		filter.developingUser = this.selectedFilterValueMap.has(RegistruIesiriSelectorComponent.COLUMN_INTOCMIT_DE_USER) ?
			this.selectedFilterValueMap.get(RegistruIesiriSelectorComponent.COLUMN_INTOCMIT_DE_USER) : null;
		if (this.selectedFilterValueMap.has("asteptamRaspuns")){
			let boolValues: Array<any> = this.selectedFilterValueMap.get("asteptamRaspuns");
			filter.isAwaitingResponse = boolValues.length === 1 ? boolValues[0] : null;
		}
		if (this.selectedFilterValueMap.has("anulat")){
			let boolValues: Array<any> = this.selectedFilterValueMap.get("anulat");
			filter.isCanceled = boolValues.length === 1 ? boolValues[0] : null;
		}
		if (this.selectedFilterValueMap.has("inchis")){
			let boolValues: Array<any> = this.selectedFilterValueMap.get("inchis");
			filter.isFinished = boolValues.length === 1 ? boolValues[0] : null;
		}
		filter.registrationDate = this.selectedFilterValueMap.has(RegistruIesiriSelectorComponent.COLUMN_DATA_INREGISTRARE_FOR_FILTER) ? 
			this.selectedFilterValueMap.get(RegistruIesiriSelectorComponent.COLUMN_DATA_INREGISTRARE_FOR_FILTER) : null;
		filter.documentTypeCode = this.selectedFilterValueMap.has(RegistruIesiriSelectorComponent.COLUMN_COD_TIP_DOCUMENT) ?
			this.selectedFilterValueMap.get(RegistruIesiriSelectorComponent.COLUMN_COD_TIP_DOCUMENT) : null;
		filter.content = this.selectedFilterValueMap.has(RegistruIesiriSelectorComponent.COLUMN_CONTINUT) ?
			this.selectedFilterValueMap.get(RegistruIesiriSelectorComponent.COLUMN_CONTINUT) : null;
		filter.numberOfPages = this.selectedFilterValueMap.has(RegistruIesiriSelectorComponent.COLUMN_NUMAR_PAGINI) ?
			parseInt(this.selectedFilterValueMap.get(RegistruIesiriSelectorComponent.COLUMN_NUMAR_PAGINI), 10) : null;
		filter.numberOfAnnexes = this.selectedFilterValueMap.has(RegistruIesiriSelectorComponent.COLUMN_NUMAR_ANEXE) ?
			parseInt(this.selectedFilterValueMap.get(RegistruIesiriSelectorComponent.COLUMN_NUMAR_ANEXE), 10) : null;
		filter.projectName = this.selectedFilterValueMap.has("numeProiect") ?
			this.selectedFilterValueMap.get("numeProiect") : null;
		filter.subactivityName = this.selectedFilterValueMap.get("subactivityName");
		filter.responseDeadline = this.selectedFilterValueMap.has("termenRaspuns") ?
			this.selectedFilterValueMap.get("termenRaspuns") : null;
		filter.cancellationReason = this.selectedFilterValueMap.has(RegistruIesiriSelectorComponent.COLUMN_MOTIV_ANULARE) ?
			this.selectedFilterValueMap.get(RegistruIesiriSelectorComponent.COLUMN_MOTIV_ANULARE) : null;

		filter.departamentDestinatar = this.selectedFilterValueMap.get("departamentDestinatar");
		filter.numarInregistrareDestinatar = this.selectedFilterValueMap.get("numarInregistrareDestinatar");
		filter.dataInregistrareDestinatar = this.selectedFilterValueMap.get("dataInregistrareDestinatar");
		filter.comisieGlDestinatar = this.selectedFilterValueMap.get("comisieGlDestinatar");
		filter.observatiiDestinatar = this.selectedFilterValueMap.get("observatiiDestinatar");
		filter.institutiiIdsDestinatar = this.selectedFilterValueMap.get("institutiiIdsDestinatar");
		filter.destinatarNou = this.selectedFilterValueMap.get("destinatarNou");

		let filterString = JSON.stringify(filter);
		if (this.filterHash !== StringUtils.hashCode(filterString)){
			this.pageOffset = 0;
		}else{
			filter.offset = this.pageOffset;
			this.filterHash = StringUtils.hashCode(filterString);
		}
		filter.emitentId = this.inputData.emitentId;
		filter.numeEmitent = this.inputData.numeEmitent;
		filter.tipDocumentIntrareId = this.inputData.tipDocumentId;
		return filter;
	}
	
	public monthColumns(column: Column): boolean {
		return (column.filterField === RegistruIesiriSelectorComponent.COLUMN_LUNA);
	}
	
	private prepareColumns(): void {
		this.columns = [];
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_LUNA", RegistruIesiriSelectorComponent.COLUMN_LUNA_FOR_DISPLAY, RegistruIesiriSelectorComponent.COLUMN_LUNA, "MONTH", "in", true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_NR_INREGISTRARE", RegistruIesiriSelectorComponent.COLUMN_NUMAR_INREGISTRARE, RegistruIesiriSelectorComponent.COLUMN_NUMAR_INREGISTRARE_FOR_FILTER, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_DATA_INREGISTRARE", RegistruIesiriSelectorComponent.COLUMN_DATA_INREGISTRARE, RegistruIesiriSelectorComponent.COLUMN_DATA_INREGISTRARE_FOR_FILTER, "DATE", "equals", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_DESTINATARI", RegistruIesiriSelectorComponent.COLUMN_DESTINATARI, RegistruIesiriSelectorComponent.COLUMN_DESTINATARI, "DESTINATAR"));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_TIP_DOCUMENT", RegistruIesiriSelectorComponent.COLUMN_TIP_DOCUMENT, RegistruIesiriSelectorComponent.COLUMN_TIP_DOCUMENT, "NOMENCLATOR", "in", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_COD_TIP_DOCUMENT", RegistruIesiriSelectorComponent.COLUMN_COD_TIP_DOCUMENT, RegistruIesiriSelectorComponent.COLUMN_COD_TIP_DOCUMENT, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_TRIMIS_PE_MAIL", RegistruIesiriSelectorComponent.COLUMN_TRIMIS_PE_MAIL, "trimisPeMail", "BOOLEAN", "in", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_CONTINUT", RegistruIesiriSelectorComponent.COLUMN_CONTINUT, RegistruIesiriSelectorComponent.COLUMN_CONTINUT, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_NUMAR_PAGINI", RegistruIesiriSelectorComponent.COLUMN_NUMAR_PAGINI, RegistruIesiriSelectorComponent.COLUMN_NUMAR_PAGINI, "NUMBER", "equals", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_NUMAR_ANEXE", RegistruIesiriSelectorComponent.COLUMN_NUMAR_ANEXE, RegistruIesiriSelectorComponent.COLUMN_NUMAR_ANEXE, "NUMBER", "equals", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_INTOCMIT_DE_CATRE", RegistruIesiriSelectorComponent.COLUMN_INTOCMIT_DE_USER, RegistruIesiriSelectorComponent.COLUMN_INTOCMIT_DE_USER, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_PROIECTE", RegistruIesiriSelectorComponent.COLUMN_PROIECT, "numeProiect", "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("PROJECT_SUBACTIVITY", RegistruIesiriSelectorComponent.COLUMN_SUBACTIVITY_NAME, RegistruIesiriSelectorComponent.COLUMN_SUBACTIVITY_NAME, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_NECESITA_RASPUNS", RegistruIesiriSelectorComponent.COLUMN_NECESITA_RASPUNS, "asteptamRaspuns", "BOOLEAN", "in", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_TERMEN_RASPUNS", RegistruIesiriSelectorComponent.COLUMN_TERMEN_RASPUNS, "termenRaspuns", "DATE", "equals", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_ANULAT", RegistruIesiriSelectorComponent.COLUMN_ANULAT, "anulat", "BOOLEAN", "in", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_MOTIV_ANULARE", RegistruIesiriSelectorComponent.COLUMN_MOTIV_ANULARE, RegistruIesiriSelectorComponent.COLUMN_MOTIV_ANULARE, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_IESIRI_FINALIZARE", RegistruIesiriSelectorComponent.COLUMN_FINALIZARE, "inchis", "BOOLEAN", "in", true, true));
	}

	private buildColumn(headerAsLabelCode: string, field: string, filterField?: string, filterType?: string, filterMatchMode?: string, filter?: boolean, sortable = false): Column {
		let column: Column = new Column();
		column.header = this.translateUtils.translateLabel(headerAsLabelCode);
		column.field = field;
		column.filterType = filterType;
		column.filterMatchMode = filterMatchMode;
		column.filter = filter;
		column.sortable = sortable;
		if (ObjectUtils.isNotNullOrUndefined(filterField)) {
			column.filterField = filterField;
			if (filterField === "numeProiect"){
				column.sortField = "numeProiecteConcatenate";
			}else{
				column.sortField = column.filterField;
			}
		} else {
			column.filterField = field;
		}
		return column;
	}
	
	private prepareFilterValues(): void {
		this.prepareYearFilterValues();
		this.getNomenclatorIdByCodeAsMap();
	}

	private getNomenclatorIdByCodeAsMap():void {
		this.nomenclatorService.getNomenclatorIdByCodeAsMap([NomenclatorConstants.NOMENCLATOR_CODE_REGISTRU_IESIRI_TIP_DOCUMENT], {
			onSuccess: (nomenclatorIdByCode: object): void => {
				if (ObjectUtils.isNotNullOrUndefined(nomenclatorIdByCode)) {
					this.prepareNomenclatorFilterValues(RegistruIesiriSelectorComponent.COLUMN_TIP_DOCUMENT, nomenclatorIdByCode[NomenclatorConstants.NOMENCLATOR_CODE_REGISTRU_IESIRI_TIP_DOCUMENT]);
				}
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private prepareNomenclatorFilterValues(columnField: string, nomenclatorAttributeId: number): void {
		this.nomenclatorService.getListOfConcatenatedUiAttributesByNomenclatorId(nomenclatorAttributeId, {
			onSuccess: (concatenatedAttributesViewModels: JoinedNomenclatorUiAttributesValueModel[]): void => {
				this.nomenclatorFilterData[columnField] = this.prepareNomenclatorFilterValuesSelectItems(concatenatedAttributesViewModels);
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
			value: concatenatedAttributeViewModel.id
		};
		return selectItem;
	}
	public isColumnTranslatable(column: Column) {
		return column.field === RegistruIesiriSelectorComponent.COLUMN_TRIMIS_PE_MAIL 
			|| column.field === RegistruIesiriSelectorComponent.COLUMN_NECESITA_RASPUNS
			|| column.field === RegistruIesiriSelectorComponent.COLUMN_ANULAT
			|| column.field === RegistruIesiriSelectorComponent.COLUMN_LUNA_FOR_DISPLAY
			|| column.field === RegistruIesiriSelectorComponent.COLUMN_FINALIZARE;
	}

	public isDestinatariColumn(column: Column): boolean {
		return column.field === RegistruIesiriSelectorComponent.COLUMN_DESTINATARI; 
	}

	public onViewDestinatari(destinatari: RegistruIesiriDestinatarViewModel[], numarInregistrare: string): void {
		this.setDestinatariViewWindowHeader(numarInregistrare);
		let mappedRecipients: Array<RegistruIesiriDestinatarViewModel>;
		let notMappedRecipients: Array<RegistruIesiriDestinatarViewModel>;
		mappedRecipients = destinatari.filter(destinatar => ObjectUtils.isNotNullOrUndefined(destinatar.nrInregistrareIntrare)).sort((d1, d2) => d1.nume.localeCompare(<string>d2.nume));
		notMappedRecipients = destinatari.filter(destinatar => ObjectUtils.isNullOrUndefined(destinatar.nrInregistrareIntrare)).sort((d1, d2) => d1.nume.localeCompare(<string>d2.nume));
		this.registruIesiriDestinatatriForView = [...mappedRecipients, ...notMappedRecipients];
		this.registruIesiriDestinatariVisible = true;
	}

	public setDestinatariViewWindowHeader(numarInregistrare: string): void {
		this.destinatariViewWindowHeader = this.translateUtils.translateLabel("REGISTRU_IESIRI_VIZUALIZARE_DESTINATARI")
		.concat(
			" - ",this.translateUtils.translateLabel("REGISTRU_IESIRI_NR_INREGISTRARE"),
			": ",
			numarInregistrare
		);
	}

	public onCloseRegistruIesiriDestinatari(): void {
		this.registruIesiriDestinatariVisible = false;
	}

	public onShowRegistruIesiriDestinatari(): void {
		this.destinatariWindow.center();
		setTimeout(() => {
			this.destinatariWindow.maximize();
		}, 0);
	}

	public onToggleMinimizeRegistruIesiriDestinatari(): void {
		this.pDialogMinimizer.toggleMinimize();
	}

	public get registruIesiriDestinatariMinimized(): boolean {
		if (ObjectUtils.isNullOrUndefined(this.pDialogMinimizer)) {
			return false;
		}
		return this.pDialogMinimizer.minimized;
	}
	
	public onDataSelected(event: any): void {
		this.selectionChanged.emit(this.selectedData.id);
	}

	public onDataUnselected(event: any): void {
		this.selectionChanged.emit(null);
	}

	public onYearValueChanged(event: any): void {
		this.refresh();
	}

	public onViewDestinatariFilterWindow(){
		this.destinatariFilterWindowVisible = true;
	}

	public onDestinatariWindowClosed(){
		this.destinatariFilterWindowVisible = false;
	}

	public onDestinatariFiltered(destinatariFilter: Map<String, any>){
		this.selectedFilterValueMap.set("departamentDestinatar", destinatariFilter.get("departament"));
		this.selectedFilterValueMap.set("numarInregistrareDestinatar", destinatariFilter.get("numarInregistrare"));
		this.selectedFilterValueMap.set("dataInregistrareDestinatar", destinatariFilter.get("dataInregistrare"));
		this.selectedFilterValueMap.set("comisieGlDestinatar", destinatariFilter.get("comisieGl"));
		this.selectedFilterValueMap.set("observatiiDestinatar", destinatariFilter.get("observatii"));
		this.selectedFilterValueMap.set("institutiiIdsDestinatar", destinatariFilter.get("institutionIds"));
		this.selectedFilterValueMap.set("destinatarNou", destinatariFilter.get("destinatarNou"));
		this.refresh();
	}

	public exportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.registruIesiriDataTable)) {
			return;
		}
		this.registruIesiriDataTable.exportFilename = "Registru_Iesiri";
		this.registruIesiriDataTable.exportFunction = (exportCell) => {
			if (exportCell.field === RegistruIesiriSelectorComponent.COLUMN_TRIMIS_PE_MAIL 
					|| exportCell.field === RegistruIesiriSelectorComponent.COLUMN_NECESITA_RASPUNS 
					|| exportCell.field === RegistruIesiriSelectorComponent.COLUMN_ANULAT
					|| exportCell.field === RegistruIesiriSelectorComponent.COLUMN_LUNA_FOR_DISPLAY
					|| exportCell.field === RegistruIesiriSelectorComponent.COLUMN_FINALIZARE) {
				return this.translateUtils.translateCode(exportCell.data);
			} else if (exportCell.field === RegistruIesiriSelectorComponent.COLUMN_DESTINATARI) {
				let destinatari: RegistruIesiriDestinatarViewModel[] = exportCell.data;
				let joinedDestinatari: string = "";
				if (ArrayUtils.isNotEmpty(destinatari)) {
					destinatari.forEach((destinatar: RegistruIesiriDestinatarViewModel, index: number) => {
						if (index > 0) {
							joinedDestinatari += "; ";
						}
						joinedDestinatari += destinatar.nume;
					});
				}
				return joinedDestinatari;
			}
			return exportCell.data;
		};
		this.registruIesiriDataTable.exportCSV();
	}
}


export class RegistruIesiriSelectorInputData {

	public registruIesiriId: number;
	public tipDocumentId: number;
	public emitentId: number;
	public numeEmitent: string;
	public isIesiriForIntrari: boolean;
}