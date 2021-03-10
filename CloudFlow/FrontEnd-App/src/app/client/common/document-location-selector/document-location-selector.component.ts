import { Component, ViewEncapsulation, Output, EventEmitter, OnInit } from "@angular/core";
import { SelectItem } from "primeng/components/common/selectitem";
import { 
	ArrayUtils,
	AppError,
	MessageDisplayer,
	DocumentLocationService,
	DocumentLocationModel
} from "@app/shared";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-document-location-selector",
	templateUrl: "./document-location-selector.component.html",
	styleUrls: ["./document-location-selector.component.css"],
	encapsulation: ViewEncapsulation.None,
	providers: [{
		provide: NG_VALUE_ACCESSOR,
		useExisting: DocumentLocationSelectorComponent,
		multi: true
	}]
})
export class DocumentLocationSelectorComponent implements OnInit , ControlValueAccessor {

	private documentLocationService: DocumentLocationService;
	private messageDisplayer: MessageDisplayer;

	@Output()
	private documentLocationChanged: EventEmitter<DocumentLocationModel>;

	public documentLocations: DocumentLocationModel[];
	public selectedDocumentLocation: DocumentLocationModel;

	private onChange = (documentLocation: DocumentLocationModel) => {};
	private onTouche = () => {};
	
	public constructor(documentLocationService: DocumentLocationService, messageDisplayer: MessageDisplayer) {
		this.documentLocationService = documentLocationService;
		this.messageDisplayer = messageDisplayer;
		this.documentLocationChanged = new EventEmitter<DocumentLocationModel>();
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		this.reset();
	}

	public reset(): void {
		this.documentLocations = [];
		this.selectedDocumentLocation = null;

		this.populateDocumentLocations();
	}

	public onDocumentLocationChange(event): void {
		this.documentLocationChanged.emit(this.selectedDocumentLocation);
		this.propagateValue();
	}

	private populateDocumentLocations(): void {
		this.documentLocationService.getAllDocumentLocations({
			onFailure: (applicationError: AppError) => {
				this.messageDisplayer.displayAppError(applicationError);
			},
			onSuccess: (documentLocations: DocumentLocationModel[]) => {
				if (ArrayUtils.isEmpty(documentLocations)) {
					return;
				}
				this.documentLocations = documentLocations;
				this.selectedDocumentLocation = documentLocations[0];
				this.documentLocationChanged.emit(this.selectedDocumentLocation);	
				this.propagateValue();
				ListItemUtils.sort(this.documentLocations, "name");
			}
		});
	}
	
	private propagateValue() {
		this.onChange(this.selectedDocumentLocation);
		this.onTouche();
	}

	public writeValue(documentLocation: DocumentLocationModel): void {
		this.selectedDocumentLocation = documentLocation;
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}
	
	public registerOnTouched(fn: any): void {
		this.onTouche = fn;
	}
}