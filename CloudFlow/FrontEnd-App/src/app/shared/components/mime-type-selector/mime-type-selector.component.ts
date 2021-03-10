import { Component, Input, Output, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { MimeTypeModel, AppError } from "../../model";
import { MimeTypeService } from "../../service";
import { MessageDisplayer } from "../../message-displayer";
import { ObjectUtils } from "../../utils/object-utils";

@Component({
	selector: "app-mime-type-selector",
	templateUrl: "./mime-type-selector.component.html",
	providers: [{
		provide: NG_VALUE_ACCESSOR,
		useExisting: MimeTypeSelectorComponent,
		multi: true
	}]
})
export class MimeTypeSelectorComponent implements ControlValueAccessor {
	
	@Input()
	public selectionMode: "single" | "multiple" = "single";

	@Input()
	public allElementsSelected: boolean = false;

	@Output()
	private selectionChanged: EventEmitter<MimeTypeModel[]>;

	private mimeTypeService: MimeTypeService;
	private messageDisplayer: MessageDisplayer;

	public mimeTypes: MimeTypeModel[];
	public selectedMimeTypes: MimeTypeModel[] = [];

	public scrollHeight: string;

	private onChange = (mimeTypes: MimeTypeModel[]) => {};
	private onTouche = () => {};

	public constructor(mimeTypeService: MimeTypeService, messageDisplayer: MessageDisplayer) {
		this.mimeTypeService = mimeTypeService;
		this.messageDisplayer = messageDisplayer;
		this.selectionChanged = new EventEmitter<MimeTypeModel[]>();
		this.init();
	}

	private init(): void {
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.loadMimeTypes();
	}

	private loadMimeTypes(): void {
		this.mimeTypeService.getAllMimeTypes({
			onSuccess: (mimeTypes: MimeTypeModel[]): void => {
				this.mimeTypes = mimeTypes;
				this.setSelectedElementsAndPropagateValue(mimeTypes);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private setSelectedElementsAndPropagateValue(mimeTypes: MimeTypeModel[]): void {
		if (this.isSelectionModeMultiple() && this.allElementsSelected) {
			this.selectedMimeTypes = mimeTypes;
		}
		this.propagateValue();
	}

	public isSelectionModeSingle(): boolean {
		return this.selectionMode === "single";
	}

	public isSelectionModeMultiple(): boolean {
		return this.selectionMode === "multiple";
	}

	private onMimeTypeSelected(event: any): void {
		if (this.isSelectionModeSingle()) {
			this.selectedMimeTypes = [event.data];
		}
		this.propagateValue();
	}

	private onMimeTypeUnselected(mimeType: MimeType): void {
		if (this.isSelectionModeSingle()) {
			this.selectedMimeTypes = [];
		}
		this.propagateValue();
	}

	private propagateValue(): void {
		this.selectionChanged.emit(this.selectedMimeTypes);
		this.onChange(this.selectedMimeTypes);
		this.onTouche();
	}

	public writeValue(mimeTypes: MimeTypeModel[]): void {
		this.setSelectedElementsAndPropagateValue(mimeTypes);
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}
	
	public registerOnTouched(fn: any): void {
		this.onTouche = fn;
	}

	public reload(): void {
		this.loadMimeTypes();
		this.selectedMimeTypes = [];
		this.propagateValue();
	}
}
