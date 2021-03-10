import { Component, OnInit, Output, EventEmitter, Input } from "@angular/core";
import { NomenclatorService } from "../../service";
import { MessageDisplayer } from "../../message-displayer";
import { ObjectUtils, ArrayUtils, TranslateUtils } from "../../utils";
import {
	AppError,
	NomenclatorFilter,
	NomenclatorModel,
	NomenclatorSortedAttribute
} from "../../model";
import { Dialog } from "primeng/primeng";
import { BaseWindow } from "./../../base-window";

@Component({
	selector: "app-nomenclator-values-selection-window",
	templateUrl: "./nomenclator-values-selection-window.component.html"
})
export class NomenclatorValuesSelectionWindowComponent extends BaseWindow implements OnInit {
	
	@Input()
	public selectionMode: "single" | "multiple";

	@Input()
	public nomenclatorId: number;

	@Input()
	public customFilters: NomenclatorFilter[];

	@Input()
	public customSortedAttributes: NomenclatorSortedAttribute[];

	@Input()
	public customFilterByValueIds: number[];
	
	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public valuesSelected: EventEmitter<number[]>;

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	public visible: boolean = false;

	public selectedValues: any;
	public nomenclatorName: string;

	public nomenclatorDataTableNomenclatorId: number;
	public nomenclatorDataTableCustomFilters: NomenclatorFilter[];
	public nomenclatorDataTableCustomSortedAttributes: NomenclatorSortedAttribute[];
	public nomenclatorDataTableCustomFilterByValueIds: number[];
	public nomenclatorDataTableMode: string;
	public nomenclatorDataTableVisible: boolean;

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
		super();
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.windowClosed = new EventEmitter<void>();
		this.valuesSelected = new EventEmitter<number[]>();
		this.init();
	}

	private init(): void {
		this.nomenclatorName = "";
		this.visible = true;
		this.selectionMode = "single";
		this.windowClosed = new EventEmitter<void>();
	}

	public ngOnInit(): void {
		
		this.nomenclatorDataTableMode = this.selectionMode;
		this.nomenclatorDataTableNomenclatorId = this.nomenclatorId;
		this.nomenclatorDataTableCustomFilters = this.customFilters;
		this.nomenclatorDataTableCustomSortedAttributes = this.customSortedAttributes;
		this.nomenclatorDataTableCustomFilterByValueIds = this.customFilterByValueIds;
		this.nomenclatorDataTableVisible = true;

		this.nomenclatorService.getNomenclator(this.nomenclatorId, {
			onSuccess: (nomenclator: NomenclatorModel): void => {
				this.nomenclatorName = nomenclator.name;
			},
			onFailure: (error: AppError): void => {
				// Nu are importanta.
			}
		});
	}

	public onNomenclatorDataTableSelectionChanged(selectedData: object[] | object): void {
		this.selectedValues = selectedData;
	}

	public onOkAction(event: any): void {

		if (ObjectUtils.isNullOrUndefined(this.selectedValues)) {
			this.windowClosed.emit();
			return;
		}

		let valueIds: number[] = [];
		if (ObjectUtils.isArray(this.selectedValues)) {
			this.selectedValues.forEach((value: any) => {
				valueIds.push(value.id);
			});			
		} else {
			valueIds.push(this.selectedValues.id);
		}

		if (ArrayUtils.isNotEmpty(valueIds)) {
			this.valuesSelected.emit(valueIds);
		}		
		this.windowClosed.emit();

		this.visible = false;
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}
}