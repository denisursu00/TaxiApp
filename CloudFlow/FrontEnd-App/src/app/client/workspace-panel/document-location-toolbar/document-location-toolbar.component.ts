import { Component, Output, EventEmitter } from "@angular/core";
import { DocumentLocationService, MessageDisplayer, AppError, ConfirmationUtils } from "@app/shared";

@Component({
	selector: "app-document-location-toolbar",
	templateUrl: "./document-location-toolbar.component.html",
	styleUrls: ["./document-location-toolbar.component.css"]
})
export class DocumentLocationToolbarComponent {

	@Output()
	private documentLocationSavedOrDeleted: EventEmitter<void>;
	
	private documentLocationService: DocumentLocationService;
	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;

	public isDisabledForAdd: boolean;
	public isDisabledForEdit: boolean;
	public isDisabledForDelete: boolean;

	public documentLocationWindowVisible: boolean;

	public documentLocationRealName: string;
	public documentLocationWindowMode: string;

	public constructor(documentLocationService: DocumentLocationService, messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils) {
		this.documentLocationService = documentLocationService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.init();
	}

	private init(): void {
		this.documentLocationWindowVisible = false;
		this.documentLocationSavedOrDeleted = new EventEmitter<void>();
		this.disableAllActions();
	}

	public onAdd(): void {
		this.documentLocationWindowMode = "new";
		this.documentLocationWindowVisible = true;
	}

	public onEdit(): void {
		this.documentLocationWindowMode = "edit";
		this.documentLocationWindowVisible = true;
	}

	public onDelete(): void {
		this.confirmationUtils.confirm("DELETE_DOCUMENT_LOCATION_CONFIRM_QUESTION", {
			approve: () => {
				this.documentLocationService.deleteDocumentLocation(this.documentLocationRealName, {
					onSuccess: (): void => {
						this.messageDisplayer.displaySuccess("DOCUMENT_LOCATION_DELETED");
						this.emitDocumentLocationSavedOrDeletedEvent();
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			reject: () => {
			}
		},
		"DELETE_DOCUMENT_LOCATION_CONFIRM_TITLE"
		);
	}

	private disableAllActions(): void {
		this.isDisabledForAdd = false;
		this.isDisabledForEdit = false;
		this.isDisabledForDelete = false;
	}

	public setDocumentLocation(documentLocationRealName: string): void {
		this.documentLocationRealName = documentLocationRealName;
	}

	public onDocumentLocationWindowClosed(): void {
		this.documentLocationWindowVisible = false;
	}

	public onDocumentLocationSaved(): void {
		this.emitDocumentLocationSavedOrDeletedEvent();
	}

	private emitDocumentLocationSavedOrDeletedEvent(): void {
		this.documentLocationSavedOrDeleted.emit();
		this.documentLocationWindowVisible = false;
	}
}
