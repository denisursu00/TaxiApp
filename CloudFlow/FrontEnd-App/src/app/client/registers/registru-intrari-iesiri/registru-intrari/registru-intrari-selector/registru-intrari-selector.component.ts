import { Component, ViewChild, Output, EventEmitter, Input, OnInit } from "@angular/core";
import { Column, SelectItem } from "primeng/primeng";
import { RegistruIntrariIesiriService, NomenclatorService, MessageDisplayer, NomenclatorConstants, RegistruIntrariViewModel, AppError, ObjectUtils, JoinedNomenclatorUiAttributesValueModel,
	RegistruIntrariModel, DateConstants, DateUtils, ArrayUtils, TranslateUtils, RaspunsuriBanciCuPropuneriEnum, MonthEnum, RegistruIntrariFilterModel, Page, PageConstants, RegistruIntrariViewModelPagingList, StringUtils } from "@app/shared";
import { Table } from "primeng/table";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";
@Component({
	selector: "app-registru-intrari-selector",
	templateUrl: "./registru-intrari-selector.component.html",
	styleUrls: ["./registru-intrari-selector.component.css"]
})
export class RegistruIntrariSelectorComponent implements OnInit {

	private static readonly COLUMN_NUMAR_INREGISTRARE: string = "numarInregistrare";
	private static readonly COLUMN_NUMAR_INREGISTRARE_FOR_DISPLAY: string = "numarInregistrareForDisplay";

	private static readonly COLUMN_LUNA: string = "luna";
	private static readonly COLUMN_LUNA_FOR_DISPLAY: string = "lunaForDisplay";
	
	private static readonly COLUMN_DATA_INREGISTRARE: string = "dataInregistrare";
	private static readonly COLUMN_DATA_INREGISTRARE_FOR_DISPLAY: string = "dataInregistrareForDisplay";
	
	private static readonly COLUMN_EMITENT: string = "numeEmitent";
	private static readonly COLUMN_DEPARTAMENT_EMITENT: string = "departamentEmitent";
	private static readonly COLUMN_NUMAR_DOCUMENT_EMITENT: string = "numarDocumentEmitent";

	private static readonly COLUMN_DATA_DOCUMENT_EMITENT: string = "dataDocumentEmitent";
	private static readonly COLUMN_DATA_DOCUMENT_EMITENT_FOR_DISPLAY: string = "dataDocumentEmitentForDisplay";
	
	private static readonly COLUMN_TIP_DOCUMENT: string = "tipDocument";
	private static readonly COLUMN_COD_TIP_DOCUMENT: string = "codTipDocument";

	private static readonly COLUMN_TRIMIS_PE_MAIL: string = "trimisPeMail";
	private static readonly COLUMN_TRIMIS_PE_MAIL_FOR_DISPLAY: string = "trimisPeMailForDisplay";

	private static readonly COLUMN_CONTINUT: string = "continut";
	private static readonly COLUMN_NUMAR_PAGINI: string = "numarPagini";
	private static readonly COLUMN_NUMAR_ANEXE: string = "numarAnexe";
	private static readonly COLUMN_REPARTIZAT_CATRE: string = "repartizatCatre";
	private static readonly COLUMN_COMISIE_SAU_GL: string = "comisieSauGL";
	private static readonly COLUMN_PROIECT: string = "proiect";

	private static readonly COLUMN_NECESITA_RASPUNS: string = "necesitaRaspuns";
	private static readonly COLUMN_NECESITA_RASPUNS_FOR_DISPLAY: string = "necesitaRaspunsForDisplay";

	private static readonly COLUMN_TERMEN_RASPUNS: string = "termenRaspuns";
	private static readonly COLUMN_TERMEN_RASPUNS_FOR_DISPLAY: string = "termenRaspunsForDisplay";
	
	private static readonly COLUMN_NUMAR_INREGISTRARE_IESIRI: string = "numarInregistrareOfRegistruIesiri";
	private static readonly COLUMN_OBSERVATII: string = "observatii";

	private static readonly COLUMN_RASPUNSURI_BANCI_CU_PROPUNERI: string = "raspunsuriBanciCuPropuneri";
	private static readonly COLUMN_RASPUNSURI_BANCI_CU_PROPUNERI_FOR_DISPLAY: string = "raspunsuriBanciCuPropuneriForDisplay";

	private static readonly COLUMN_INTRARE_EMITENT: string = "nrZileIntrareEmitent";
	private static readonly COLUMN_RASPUNS_INTRARE: string = "nrZileRaspunsIntrare";
	private static readonly COLUMN_RASPUNS_EMITENT: string = "nrZileRaspunsEmitent";
	private static readonly COLUMN_TERMEN_DATA_RASPUNS: string = "nrZileTermenDataRaspuns";

	private static readonly COLUMN_ANULAT: string = "anulat";
	private static readonly COLUMN_ANULAT_FOR_DISPLAY: string = "anulatForDisplay";

	private static readonly COLUMN_MOTIV_ANULARE: string = "motivAnulare";

	private static readonly COLUMN_FINALIZAT: string = "inchis";
	private static readonly COLUMN_FINALIZAT_FOR_DISPLAY: string = "inchisForDisplay";
	private static readonly COLUMN_SUBACTIVITY_NAME: string = "subactivityName";

	@Input()
	public inputData: RegistruIntrariSelectorInputData;

	@Output()
	public selectionChanged: EventEmitter<number>;
	
	@ViewChild(Table)
	public registruIntrariDataTable: Table;

	private registruIntrariIesiriService: RegistruIntrariIesiriService;
	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private monthSelectItems: SelectItem[];
	private raspunsuriBanciCuPropuneriItems: SelectItem[];
	
	public selectedFilterValueMap: Map<String, any>;

	public tableVisible: boolean = false;
	public selectedData: RegistruIntrariViewModel;
	public columns: Column[];
	public rowsPerPageOptions: number[];
	public pageOffset: number;
	public dataLoading: boolean;
	public booleanFilterTypes: SelectItem[];
	public dateFormat: String;
	public yearRange: String;
	public nomenclatorFilterData: any[];

	public pageData: Page<RegistruIntrariViewModel>;

	
	public years: number[];
	public yearItems: SelectItem[];
	public selectedYear: number;

	public scrollHeight: string;
	
	public constructor(registruIntrariIesiriService: RegistruIntrariIesiriService, nomenclatorService: NomenclatorService, 
			messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
		this.registruIntrariIesiriService = registruIntrariIesiriService;
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.selectionChanged = new EventEmitter<number>();
		this.pageData = new Page<RegistruIntrariViewModel>();
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
		this.selectedFilterValueMap = new Map<String, any>();
		this.nomenclatorFilterData = [];
		this.years = [];
		this.yearItems = [];
		this.scrollHeight = (window.innerHeight - 400) + "px";

		this.rowsPerPageOptions = PageConstants.DEAFAULT_ROW_PER_PAGE_OPTIONS;
		this.pageOffset = 0;
		this.pageData.pageSize = PageConstants.DEFAULT_PAGE_SIZE;

		let currentYear: number = new Date().getFullYear();
		this.selectedYear = currentYear;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.prepareRaspunsuriBanciCuPropuneriItems();
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNullOrUndefined(this.inputData)) {
			throw new Error("input data pt. registru intrari selector nu trebuie sa fie null");
		}
		this.prepareBooleanFilter();
		this.prepareFilterValues();
		this.prepareMonthSelectItems();
		this.prepareColumns();
	}

	private prepareMonthSelectItems(): void {

		this.monthSelectItems = [];

		DateConstants.MONTHS.forEach((month, monthIndex) => {
			this.monthSelectItems.push({ label: this.translateUtils.translateLabel(month), value: monthIndex});
		});
	}

	private prepareBooleanFilter(): void {
		this.booleanFilterTypes = [
			{ label: this.translateUtils.translateLabel("YES"), value: true },
			{ label: this.translateUtils.translateLabel("NO"), value: false }
		];
	}

	private prepareRaspunsuriBanciCuPropuneriItems(): void {
		this.raspunsuriBanciCuPropuneriItems = [
			{label: this.translateUtils.translateLabel("YES"), value: RaspunsuriBanciCuPropuneriEnum.DA},
			{label: this.translateUtils.translateLabel("NO"), value: RaspunsuriBanciCuPropuneriEnum.NU},
			{label: this.translateUtils.translateLabel("NA"), value: RaspunsuriBanciCuPropuneriEnum.NA}
		];
	}

	private prepareColumns(): void {
		this.columns = [];
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_LUNA", RegistruIntrariSelectorComponent.COLUMN_LUNA, RegistruIntrariSelectorComponent.COLUMN_LUNA_FOR_DISPLAY, "MONTH", "in", true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_NUMAR_INREGISTRARE", RegistruIntrariSelectorComponent.COLUMN_NUMAR_INREGISTRARE, RegistruIntrariSelectorComponent.COLUMN_NUMAR_INREGISTRARE_FOR_DISPLAY, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_DATA_INREGISTRARE", RegistruIntrariSelectorComponent.COLUMN_DATA_INREGISTRARE, RegistruIntrariSelectorComponent.COLUMN_DATA_INREGISTRARE_FOR_DISPLAY, "DATE", "equals", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_EMITENT", RegistruIntrariSelectorComponent.COLUMN_EMITENT, RegistruIntrariSelectorComponent.COLUMN_EMITENT, "NOMENCLATOR", "in", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_DEPARTAMENT_EMITENT", RegistruIntrariSelectorComponent.COLUMN_DEPARTAMENT_EMITENT, RegistruIntrariSelectorComponent.COLUMN_DEPARTAMENT_EMITENT, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_NUMAR_DOCUMENT_EMITENT", RegistruIntrariSelectorComponent.COLUMN_NUMAR_DOCUMENT_EMITENT, RegistruIntrariSelectorComponent.COLUMN_NUMAR_DOCUMENT_EMITENT, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_DATA_DOCUMENT_EMITENT", RegistruIntrariSelectorComponent.COLUMN_DATA_DOCUMENT_EMITENT, RegistruIntrariSelectorComponent.COLUMN_DATA_DOCUMENT_EMITENT_FOR_DISPLAY, "DATE", "equals", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_TIP_DOCUMENT", RegistruIntrariSelectorComponent.COLUMN_TIP_DOCUMENT, RegistruIntrariSelectorComponent.COLUMN_TIP_DOCUMENT, "NOMENCLATOR", "in", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_COD_TIP_DOCUMENT", RegistruIntrariSelectorComponent.COLUMN_COD_TIP_DOCUMENT, RegistruIntrariSelectorComponent.COLUMN_COD_TIP_DOCUMENT, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_TRIMIS_PE_MAIL", RegistruIntrariSelectorComponent.COLUMN_TRIMIS_PE_MAIL, RegistruIntrariSelectorComponent.COLUMN_TRIMIS_PE_MAIL_FOR_DISPLAY, "BOOLEAN", "in", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_CONTINUT", RegistruIntrariSelectorComponent.COLUMN_CONTINUT, RegistruIntrariSelectorComponent.COLUMN_CONTINUT, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_NUMAR_PAGINI", RegistruIntrariSelectorComponent.COLUMN_NUMAR_PAGINI, RegistruIntrariSelectorComponent.COLUMN_NUMAR_PAGINI, "NUMBER", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_NUMAR_ANEXE", RegistruIntrariSelectorComponent.COLUMN_NUMAR_ANEXE, RegistruIntrariSelectorComponent.COLUMN_NUMAR_ANEXE, "NUMBER", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_REPARTIZAT_CATRE", RegistruIntrariSelectorComponent.COLUMN_REPARTIZAT_CATRE, RegistruIntrariSelectorComponent.COLUMN_REPARTIZAT_CATRE, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_COMISII_SAU_GL", RegistruIntrariSelectorComponent.COLUMN_COMISIE_SAU_GL, RegistruIntrariSelectorComponent.COLUMN_COMISIE_SAU_GL, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_PROIECTE", RegistruIntrariSelectorComponent.COLUMN_PROIECT, RegistruIntrariSelectorComponent.COLUMN_PROIECT, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("PROJECT_SUBACTIVITY", RegistruIntrariSelectorComponent.COLUMN_SUBACTIVITY_NAME, RegistruIntrariSelectorComponent.COLUMN_SUBACTIVITY_NAME, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_NECESITA_RASPUNS", RegistruIntrariSelectorComponent.COLUMN_NECESITA_RASPUNS, RegistruIntrariSelectorComponent.COLUMN_NECESITA_RASPUNS_FOR_DISPLAY, "BOOLEAN", "in", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_TERMEN_RASPUNS", RegistruIntrariSelectorComponent.COLUMN_TERMEN_RASPUNS, RegistruIntrariSelectorComponent.COLUMN_TERMEN_RASPUNS_FOR_DISPLAY, "DATE", "equals", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_NUMAR_INREGISTRARE_IESIRI", RegistruIntrariSelectorComponent.COLUMN_NUMAR_INREGISTRARE_IESIRI, RegistruIntrariSelectorComponent.COLUMN_NUMAR_INREGISTRARE_IESIRI, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_OBSERVATII", RegistruIntrariSelectorComponent.COLUMN_OBSERVATII, RegistruIntrariSelectorComponent.COLUMN_OBSERVATII, "TEXT", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_RASPUNSURI_BANCI_CU_PROPUNERI", RegistruIntrariSelectorComponent.COLUMN_RASPUNSURI_BANCI_CU_PROPUNERI, RegistruIntrariSelectorComponent.COLUMN_RASPUNSURI_BANCI_CU_PROPUNERI_FOR_DISPLAY, "RASPUNSURI_BANCI", "in", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_INTRARE_EMITENT", RegistruIntrariSelectorComponent.COLUMN_INTRARE_EMITENT, RegistruIntrariSelectorComponent.COLUMN_INTRARE_EMITENT, "NUMBER", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_RASPUNS_INTRARE", RegistruIntrariSelectorComponent.COLUMN_RASPUNS_INTRARE, RegistruIntrariSelectorComponent.COLUMN_RASPUNS_INTRARE, "NUMBER", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_RASPUNS_EMITENT", RegistruIntrariSelectorComponent.COLUMN_RASPUNS_EMITENT, RegistruIntrariSelectorComponent.COLUMN_RASPUNS_EMITENT, "NUMBER", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_TERMEN_DATA_RASPUNS", RegistruIntrariSelectorComponent.COLUMN_TERMEN_DATA_RASPUNS, RegistruIntrariSelectorComponent.COLUMN_TERMEN_DATA_RASPUNS, "NUMBER", "contains", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_ANULAT", RegistruIntrariSelectorComponent.COLUMN_ANULAT, RegistruIntrariSelectorComponent.COLUMN_ANULAT_FOR_DISPLAY, "BOOLEAN", "in", true, true));
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_MOTIV_ANULARE", RegistruIntrariSelectorComponent.COLUMN_MOTIV_ANULARE, RegistruIntrariSelectorComponent.COLUMN_MOTIV_ANULARE, "TEXT", "contains", true, true));		
		this.columns.push(this.buildColumn("REGISTRU_INTRARI_FINALIZAT", RegistruIntrariSelectorComponent.COLUMN_FINALIZAT, RegistruIntrariSelectorComponent.COLUMN_FINALIZAT_FOR_DISPLAY, "BOOLEAN", "in", true, true));
	}

	private buildColumn(headerAsLabelCode: string, filterField: string, field: string, filterType: string, filterMatchMode: string, filter: boolean, sortable: boolean = false): Column {
		let column: Column = new Column();
		column.header = this.translateUtils.translateLabel(headerAsLabelCode);
		column.field = field;
		column.filterType = filterType;
		column.filterMatchMode = filterMatchMode;
		column.filter = filter;
		column.filterField = filterField;
		column.sortable = sortable;
		column.sortField = filterField;
		return column;
	}
	
	private prepareFilterValues(): void {
		this.prepareYearFilterValues();
		this.getNomenclatorIdByCodeAsMap([
			NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII, 
			NomenclatorConstants.NOMENCLATOR_CODE_REGISTRU_INTRARI_TIP_DOCUMENT
		]);
	}

	private prepareYearFilterValues(): void {
		this.getYearsOfExistingIntrari();
	}

	private getYearsOfExistingIntrari(): void {
		this.registruIntrariIesiriService.getYearsOfExistingIntrari({
			onSuccess: (years: number[]): void => {
				this.years = years;
				this.prepareYearItems(years);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareYearItems(years: number[]): void {
		this.yearItems = [];
		
		years.forEach((year: number) => {
			let yearItem: SelectItem = { label: year.toString(), value: year };
			this.yearItems.push(yearItem);
		});
		
		let currentYearItem: SelectItem = { label: new Date().getFullYear().toString(), value: new Date().getFullYear() };
		let yearItemsWithCurrentYearItem: SelectItem[] = this.yearItems.filter(yearItem => yearItem.value === currentYearItem.value);
		if (ArrayUtils.isEmpty(yearItemsWithCurrentYearItem)) {
			this.yearItems.push(currentYearItem);
		}
	}

	private getRequestFilterModel(): RegistruIntrariFilterModel {
		let filter = new RegistruIntrariFilterModel();
		filter.year = this.selectedYear;
		filter.offset = this.pageOffset;
		filter.pageSize = this.pageData.pageSize;

		filter.selectedMonths = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_LUNA);
		filter.registrationNumber = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_NUMAR_INREGISTRARE);
		filter.registrationDate = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_DATA_INREGISTRARE);
		filter.senderIds = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_EMITENT);
		filter.senderDepartment = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_DEPARTAMENT_EMITENT);
		filter.senderDocumentNr = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_NUMAR_DOCUMENT_EMITENT);
		filter.senderDocumentDate = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_DATA_DOCUMENT_EMITENT);
		filter.documentTypeIds = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_TIP_DOCUMENT);
		filter.documentTypeCode = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_COD_TIP_DOCUMENT);
		if (this.selectedFilterValueMap.has(RegistruIntrariSelectorComponent.COLUMN_TRIMIS_PE_MAIL)){
			let boolValues: Array<any> = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_TRIMIS_PE_MAIL);
			filter.isMailed = boolValues.length === 2 ? null : boolValues[0];
		}
		filter.content = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_CONTINUT);
		filter.numberOfPages = StringUtils.toNumber(this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_NUMAR_PAGINI));
		filter.numberOfAnnexes = StringUtils.toNumber(this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_NUMAR_ANEXE));
		filter.assignedUser = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_REPARTIZAT_CATRE);
		filter.committeeWgName = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_COMISIE_SAU_GL);
		filter.projectName = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_PROIECT);
		if (this.selectedFilterValueMap.has(RegistruIntrariSelectorComponent.COLUMN_NECESITA_RASPUNS)){
			let boolValues: Array<any> = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_NECESITA_RASPUNS);
			filter.isAwaitingResponse = boolValues.length === 2 ? null : boolValues[0];
		}
		filter.responseDeadline = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_TERMEN_RASPUNS);
		filter.iesireRegistrationNumber = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_NUMAR_INREGISTRARE_IESIRI);
		filter.remarks = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_OBSERVATII);
		filter.bankResponseProposal = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_RASPUNSURI_BANCI_CU_PROPUNERI);
		filter.nrZileIntrareEmitent = StringUtils.toNumber(this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_INTRARE_EMITENT));
		filter.nrZileRaspunsIntrare = StringUtils.toNumber(this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_RASPUNS_INTRARE));
		filter.nrZileRaspunsEmitent = StringUtils.toNumber(this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_RASPUNS_EMITENT));
		filter.nrZileTermenDataRaspuns = StringUtils.toNumber(this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_TERMEN_DATA_RASPUNS));
		if (this.selectedFilterValueMap.has(RegistruIntrariSelectorComponent.COLUMN_ANULAT)){
			let boolValues: Array<any> = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_ANULAT);
			filter.isCanceled = boolValues.length === 2 ? null : boolValues[0];
		}
		filter.cancellationReason = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_MOTIV_ANULARE);
		if (this.selectedFilterValueMap.has(RegistruIntrariSelectorComponent.COLUMN_FINALIZAT)){
			let boolValues: Array<any> = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_FINALIZAT);
			filter.isFinished = boolValues.length === 2 ? null : boolValues[0];
		}
		filter.subactivityName = this.selectedFilterValueMap.get(RegistruIntrariSelectorComponent.COLUMN_SUBACTIVITY_NAME);

		// specific filtrarii din iesiri
		filter.destinatarId = this.inputData.destinatarId;
		filter.numeDestinatar = this.inputData.numeDestinatar;
		filter.tipDocumentIdDestinatar = this.inputData.tipDocumentId;

		return filter;
	}

	private loadRegistruIntrariViewModelsByFilter(filter: RegistruIntrariFilterModel): void {
		if (this.inputData.isIntrariForIesiri && ObjectUtils.isNullOrUndefined(this.inputData.numeDestinatar) && ObjectUtils.isNullOrUndefined(this.inputData.destinatarId)){
			this.unlockTable();
		}else{
			this.registruIntrariIesiriService.getRegistruIntrariViewModelByFilter(filter, {
				onSuccess: (page: RegistruIntrariViewModelPagingList): void => {
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

	private prepareSelectedData(registruIntrariViewModels: RegistruIntrariViewModel[]): void {
		if (ObjectUtils.isNullOrUndefined(this.inputData.registruIntrariId)) {
			return;
		}
		this.registruIntrariIesiriService.getRegistruIntrariById(this.inputData.registruIntrariId, {
			onSuccess: (registru: RegistruIntrariModel): void => {
				registruIntrariViewModels.forEach((registruIntrariViewModel: RegistruIntrariViewModel) => {
					if (registruIntrariViewModel.id === registru.id) {
						this.selectedData = registruIntrariViewModel;
					}
				});
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private getNomenclatorIdByCodeAsMap(codes: string[]):void {
		this.nomenclatorService.getNomenclatorIdByCodeAsMap(codes, {
			onSuccess: (nomenclatorIdByCode: object): void => {
				if (ObjectUtils.isNotNullOrUndefined(nomenclatorIdByCode)) {
					this.prepareNomenclatorFilterValues(RegistruIntrariSelectorComponent.COLUMN_EMITENT, nomenclatorIdByCode[NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII]);
					this.prepareNomenclatorFilterValues(RegistruIntrariSelectorComponent.COLUMN_TIP_DOCUMENT, nomenclatorIdByCode[NomenclatorConstants.NOMENCLATOR_CODE_REGISTRU_INTRARI_TIP_DOCUMENT]);
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
				
				ListItemUtils.sortByLabel(this.nomenclatorFilterData[columnField]);
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

	public booleanColumns(column: Column): boolean {
		if (column.filterField  === RegistruIntrariSelectorComponent.COLUMN_TRIMIS_PE_MAIL || 
				column.filterField  === RegistruIntrariSelectorComponent.COLUMN_NECESITA_RASPUNS || 
				column.filterField  === RegistruIntrariSelectorComponent.COLUMN_RASPUNSURI_BANCI_CU_PROPUNERI || 
				column.filterField  === RegistruIntrariSelectorComponent.COLUMN_ANULAT ||
				column.filterField  === RegistruIntrariSelectorComponent.COLUMN_FINALIZAT ) {
			return true;
		}
		return false;
	}

	public monthColumns(column: Column): boolean {
		return (column.filterField === RegistruIntrariSelectorComponent.COLUMN_LUNA);
	}
	
	public onDataSelected(event: any): void {
		this.selectionChanged.emit(this.selectedData.id);
	}

	public onDataUnselected(event: any): void {
		this.selectionChanged.emit(undefined);
	}

	public onYearValueChanged(event: any): void {
		this.refresh();
	}

	public refresh(): void {
		this.selectedData = null;
		this.lockTable();
		this.pageOffset = 0;
		this.registruIntrariDataTable.first = 0;
		this.registruIntrariDataTable.sortField = RegistruIntrariSelectorComponent.COLUMN_NUMAR_INREGISTRARE;
		this.registruIntrariDataTable.sortOrder = -1;
		let filter: RegistruIntrariFilterModel = this.getRequestFilterModel();
		filter.sortField = RegistruIntrariSelectorComponent.COLUMN_NUMAR_INREGISTRARE;
		filter.isAscendingOrder = false;
		this.loadRegistruIntrariViewModelsByFilter(filter);
		this.selectionChanged.emit(undefined);
	}

	public onLazyLoad(event: any): void {
		this.pageOffset = event.first;
		this.pageData.pageSize = event.rows;
		let filter: RegistruIntrariFilterModel = this.getRequestFilterModel();
		if (ObjectUtils.isNullOrUndefined(event.sortField)){
			this.registruIntrariDataTable.sortField = RegistruIntrariSelectorComponent.COLUMN_NUMAR_INREGISTRARE;
			this.registruIntrariDataTable.sortOrder = -1;
			filter.sortField = RegistruIntrariSelectorComponent.COLUMN_NUMAR_INREGISTRARE;
			filter.isAscendingOrder = false;
		}else{
			filter.sortField = event.sortField;
			filter.isAscendingOrder = event.sortOrder !== 1 ? false : true;
		}
		this.loadRegistruIntrariViewModelsByFilter(filter);
	}

	public exportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.registruIntrariDataTable)) {
			return;
		}
		this.registruIntrariDataTable.exportFilename = "Registru_Intrari";
		this.registruIntrariDataTable.exportFunction = (exportCell) => {
			if (exportCell.field === RegistruIntrariSelectorComponent.COLUMN_TRIMIS_PE_MAIL_FOR_DISPLAY
					|| exportCell.field === RegistruIntrariSelectorComponent.COLUMN_NECESITA_RASPUNS_FOR_DISPLAY
					|| exportCell.field === RegistruIntrariSelectorComponent.COLUMN_LUNA_FOR_DISPLAY
					|| exportCell.field === RegistruIntrariSelectorComponent.COLUMN_RASPUNSURI_BANCI_CU_PROPUNERI_FOR_DISPLAY
					|| exportCell.field === RegistruIntrariSelectorComponent.COLUMN_ANULAT_FOR_DISPLAY
					|| exportCell.field === RegistruIntrariSelectorComponent.COLUMN_FINALIZAT_FOR_DISPLAY) {
				return this.translateUtils.translateCode(exportCell.data);
			}
			return exportCell.data;
		};
		this.registruIntrariDataTable.exportCSV();
	}
}

export class RegistruIntrariSelectorInputData {
	public registruIesiriId: number;
	public registruIntrariId: number;
	public tipDocumentId: number;
	public destinatarId: number;
	public numeDestinatar: string;
	public isIntrariForIesiri: boolean;
}