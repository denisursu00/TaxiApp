import { Component, Input, OnInit, OnChanges, ViewChild, EventEmitter, Output } from "@angular/core";
import { Page } from "../../model/page";
import { Column, SelectItem } from "primeng/primeng";
import { NomenclatorService } from "../../service";
import { AppError, NomenclatorValueModel, JoinedNomenclatorUiAttributesValueModel, NomenclatorModel, NomenclatorAttributeModel, AttributeTypeEnum } from "../../model";
import { PagingList, NomenclatorValueViewModel, NomenclatorValueAsViewSearchRequestModel, NomenclatorFilter, NomenclatorSortedAttribute } from "../../model";
import { MessageDisplayer } from "../../message-displayer";
import { TranslateUtils, ArrayUtils, ObjectUtils, StringUtils, DateUtils } from "../../utils";
import { NomenclatorMultipleFilter } from "../../model/nomenclator/nomenclator-multiple-filter";
import { NomenclatorSimpleFilter } from "../../model/nomenclator/nomenclator-simple-filter";
import { DateConstants, PageConstants, NomenclatorConstants } from "../../constants";
import { Table } from "primeng/table";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-nomenclator-data-table",
	templateUrl: "./nomenclator-data-table.component.html"
})
export class NomenclatorDataTableComponent implements OnChanges {

	private static readonly DEFAULT_EXPORT_CSV_FILE_NAME: string = "export";
	private static readonly SCROLLH_HEIGHT_POUNDS_FROM_WINDOW_INNER_HEIGHT = 0.7;
	private static readonly MAX_INT_VALUE = 2147483647;

	@ViewChild(Table)
	public nomenclatorDataTable: Table;

	@Input()
	public nomenclatorId: number;

	@Input()
	public selectionMode: string = "single" || "multiple";

	@Input()
	public customFilters: NomenclatorFilter[];

	@Input()
	public customFilterByValueIds: number[];
	
	@Input()
	public customSortedAttributes: NomenclatorSortedAttribute[];

	@Output()
	public selectionChanged: EventEmitter<Array<object> | object>;

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	public selectedData: Array<object> | object;
	
	public tableVisible: boolean = false;
	public dataLoading: boolean;

	public pageData: Page<any>;
	public columns: Column[] = [];	
	public pageOffset: number = 0;
	public booleanFilterTypes: SelectItem[];
	public nomenclatorFilterData: any[];
	public dateFormat: String;
	public yearRange: String;
	public rowsPerPageOptions: number[];

	public scrollHeight: string;

	private filters: NomenclatorFilter[];
	private sortedAttributes: NomenclatorSortedAttribute[];
	
	private exportCSVFileName: string;

	private filterHashCode: any;

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.selectionChanged = new EventEmitter();
		this.init();


	}

	private init(): void {
		this.tableVisible = false;
		this.pageData = new Page<any>();
		this.nomenclatorFilterData = [];
		this.filters = [];
		this.sortedAttributes = [];
		this.exportCSVFileName = NomenclatorDataTableComponent.DEFAULT_EXPORT_CSV_FILE_NAME;
		this.prepareBooleanFilterTypes();

		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.rowsPerPageOptions = PageConstants.DEAFAULT_ROW_PER_PAGE_OPTIONS;
		this.scrollHeight = (window.innerHeight - 350) + "px";

	}

	public ngOnChanges(): void {
		this.reset();
		if (ObjectUtils.isNotNullOrUndefined(this.nomenclatorDataTable)) {
			this.nomenclatorDataTable.reset();
		}
		this.prepareTableColumns();
	}

	private reset(): void {
		this.pageData = new Page<any>();
		this.columns = [];
		this.nomenclatorFilterData = [];
		this.filters = [];
		this.sortedAttributes = [];
		this.pageOffset = 0;
		this.selectedData = null;
		this.selectionChanged.emit(null);
		this.exportCSVFileName = NomenclatorDataTableComponent.DEFAULT_EXPORT_CSV_FILE_NAME;
	}

	private lockTable(): void {
		this.dataLoading = true;
	}

	private unlockTable(): void {
		this.dataLoading = false;
	}
	
	private prepareTableColumns(): void {
		this.tableVisible = false;
		this.nomenclatorService.getNomenclator(this.nomenclatorId, {
			onSuccess: (nomenclator: NomenclatorModel): void => {
				this.columns = [];
				nomenclator.attributes.forEach((attribute: NomenclatorAttributeModel) => {
					if (attribute.type === AttributeTypeEnum.NOMENCLATOR) {
						this.prepareNomenclatorFilterValues(attribute);
					}
					this.columns.push(this.buildColumn(attribute));
				});
				this.tableVisible = true;
				this.exportCSVFileName = nomenclator.name;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}	

	private buildColumn(attribute: NomenclatorAttributeModel): Column {
		let column: Column = new Column();
		column.header = attribute.name;
		column.field = attribute.key;
		column.filterType = attribute.type;
		column.filterMatchMode = this.getColumnMatchModeByAttributeType(attribute);
		column.filter = true;
		if (ArrayUtils.isNotEmpty(this.customFilters)) {
			this.customFilters.forEach((customFilter: NomenclatorFilter) => {
				if (attribute.key === customFilter.attributeKey) {
					column.filter = false;
				}
			});
		}
		return column;
	}

	private getColumnMatchModeByAttributeType(attribute: NomenclatorAttributeModel): string {
		switch (attribute.type) {
			case AttributeTypeEnum.TEXT : {
				return "contains";
			}
			case AttributeTypeEnum.NUMERIC : {
				return "equals";
			}
			
			case AttributeTypeEnum.DATE : {
				return "equals";
			}
			
			case AttributeTypeEnum.BOOLEAN : {
				return "equals";
			}
			
			case AttributeTypeEnum.NOMENCLATOR : {
				return "in";
			}
		}
	}

	private prepareBooleanFilterTypes(): void {
		this.booleanFilterTypes = [
			{label: this.translateUtils.translateLabel("YES"), value: true},
			{label: this.translateUtils.translateLabel("NO"), value: false}
		];
	}

	private prepareNomenclatorFilterValues(attribute: NomenclatorAttributeModel): void {
		this.nomenclatorService.getNomenclatorUIValuesAsFilterForNomenclatorAttributeThatUseIt(attribute.typeNomenclatorId, attribute.id, {
			onSuccess: (concatenatedAttributesViewModels: JoinedNomenclatorUiAttributesValueModel[]): void => {
				this.nomenclatorFilterData[attribute.key] = this.prepareNomenclatorFilterValuesSelectItems(concatenatedAttributesViewModels);
	
				ListItemUtils.sortByLabel(this.nomenclatorFilterData[attribute.key]);
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

	private prepareNomenclatorValues(): void {
		this.lockTable();
		let requestModel = this.prepareSearchNomenclatorValuesAsViewRequestMode();

		this.nomenclatorService.searchNomenclatorValuesAsView(requestModel, {
			onSuccess: (page: PagingList<NomenclatorValueViewModel>): void => {		
				this.pageData.items = [];
				this.pageData.totalItems = page.totalCount;

				page.elements.forEach((value: NomenclatorValueViewModel) => {
					let pageItem: any = {};
					pageItem["id"] = value.id;
					Object.entries(value.attributes).forEach((attribute: {}) => {
						let attributeKey: string = attribute[0];
						let attributeValue: string = attribute[1];
						pageItem[attributeKey] = attributeValue;
					});
					this.pageData.items.push(pageItem);
				});

				this.unlockTable();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareSearchNomenclatorValuesAsViewRequestMode(): NomenclatorValueAsViewSearchRequestModel {
		let requestModel: NomenclatorValueAsViewSearchRequestModel = new NomenclatorValueAsViewSearchRequestModel();
		requestModel.nomenclatorId = this.nomenclatorId;
		
		let filterAsString: string = JSON.stringify(this.filters);
		if (this.filterHashCode != StringUtils.hashCode(filterAsString)) {
			requestModel.offset = 0;
		} else {
			requestModel.offset = this.pageOffset;
		}	
		this.filterHashCode = StringUtils.hashCode(filterAsString);

		requestModel.pageSize = this.pageData.pageSize;
		requestModel.valueIds = [];
		if (ArrayUtils.isNotEmpty(this.customFilterByValueIds)) {
			requestModel.valueIds = [...this.customFilterByValueIds];
		}
		requestModel.filters = this.filters;
		requestModel.sortedAttributes = this.sortedAttributes;
		return requestModel;
	}

	public onLazyLoad(event: any): void {		
		this.pageOffset = event.first;
		this.pageData.pageSize = event.rows;		
		this.prepareFilters(event.filters);
		this.prepareSortedAttributes(event.sortField, event.sortOrder);
		this.prepareNomenclatorValues();
	}

	public prepareFilters(filters: {}): void {
		
		this.filters = [];
		this.sortedAttributes = [];
		if (ArrayUtils.isNotEmpty(this.customFilters)) {
			this.filters = [... this.customFilters];
		}
		if (ArrayUtils.isNotEmpty(this.customSortedAttributes)) {
			this.sortedAttributes = [... this.customSortedAttributes];
		}

		if (ObjectUtils.isNullOrUndefined(filters)) {
			return;
		}

		Object.entries(filters).forEach((filter: {}) => {
			let filterKey: string = filter[0];
			let filterValue: any = filter[1].value;
			if (ObjectUtils.isDate(filterValue)) {
				filterValue = (<Date>filterValue);
			}
			this.filters.push(this.buildNomenclatorFilter(filterKey, filterValue));
		});
	}

	private buildNomenclatorFilter(filterKey: string, filterValue: any): NomenclatorFilter {
		if (ObjectUtils.isArray(filterValue)) {
			let nomenclatorFilter: NomenclatorMultipleFilter = new NomenclatorMultipleFilter();
			nomenclatorFilter.attributeKey = filterKey;
			nomenclatorFilter.values = filterValue;
			return nomenclatorFilter;
		} else {
			let nomenclatorFilter: NomenclatorSimpleFilter = new NomenclatorSimpleFilter();
			nomenclatorFilter.attributeKey = filterKey;
			nomenclatorFilter.value = filterValue;
			return nomenclatorFilter;
		}
	}

	public prepareSortedAttributes(sortedAttribute: string, sortOrder: number): void {

		if (StringUtils.isBlank(sortedAttribute) || ObjectUtils.isNullOrUndefined(sortOrder)) {
			return;
		}
		
		this.sortedAttributes = [];
		let nomenclatorSortedAttribute: NomenclatorSortedAttribute = new NomenclatorSortedAttribute();
		nomenclatorSortedAttribute.attributeKey = sortedAttribute;
		if (sortOrder === 1) {
			nomenclatorSortedAttribute.type = "ASC";
		} else if (sortOrder === -1) {
			nomenclatorSortedAttribute.type = "DESC";
		}
		this.sortedAttributes.push(nomenclatorSortedAttribute);
	}

	public refresh(): void {
		this.prepareTableColumns();
		this.selectedData = null;
		this.selectionChanged.emit(null);
		this.prepareNomenclatorValues();
	}

	public onDataSelected(event: any): void {
		this.selectionChanged.emit(this.selectedData);
	}

	public onDataUnselected(event: any): void {
		this.selectionChanged.emit(this.selectedData);
	}

	public exportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.nomenclatorDataTable)) {
			return;
		}

		let pageOffset =  this.pageOffset;
		let pageSize = this.pageData.pageSize;

		let requestModel: NomenclatorValueAsViewSearchRequestModel = new NomenclatorValueAsViewSearchRequestModel();
		requestModel.nomenclatorId = this.nomenclatorId;
		requestModel.offset = 0;
		requestModel.pageSize = NomenclatorDataTableComponent.MAX_INT_VALUE;
		requestModel.valueIds = [];
		if (ArrayUtils.isNotEmpty(this.customFilterByValueIds)) {
			requestModel.valueIds = [...this.customFilterByValueIds];
		}
		requestModel.filters = this.filters;
		requestModel.sortedAttributes = this.sortedAttributes;

		this.nomenclatorService.searchNomenclatorValuesAsView(requestModel, {
			onSuccess: (page: PagingList<NomenclatorValueViewModel>): void => {		
				let pageData = new Page<any>();
				pageData.items = [];
				pageData.totalItems = page.totalCount;

				page.elements.forEach((value: NomenclatorValueViewModel) => {
					let pageItem: any = {};
					pageItem["id"] = value.id;
					Object.entries(value.attributes).forEach((attribute: {}) => {
						let attributeKey: string = attribute[0];
						let attributeValue: string = attribute[1];
						pageItem[attributeKey] = attributeValue;
					});
					pageData.items.push(pageItem);
				});
				
				let dataTable: Table =  this.nomenclatorDataTable;
				dataTable.value = pageData.items
				dataTable.exportFilename = this.exportCSVFileName;

				dataTable.exportCSV();
				this.pageOffset = pageOffset;
				this.pageData.pageSize = pageSize;
				this.refresh();				
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
}