import { Component, ViewChild } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { MimeTypeModel, ArrayUtils, ObjectUtils, MimeTypeService, AppError, MessageDisplayer, MimeTypeSelectorComponent, ConfirmationUtils } from "@app/shared";

@Component({
	selector: "app-mime-types",
	templateUrl: "./mime-types.component.html",
	styleUrls: ["./mime-types.component.css"]
})
export class MimeTypesComponent {

	@ViewChild(MimeTypeSelectorComponent)
	private mimeTypeSelector: MimeTypeSelectorComponent;

	private mimeTypeService: MimeTypeService;
	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;

	private selectedMimeType: MimeTypeModel;
	
	public mimeTypeWindowVisible: boolean;
	public mimeTypeWindowMode: "add" | "edit";

	private selectedMimeTypeId: number;
	private isMimeTypeSaved: boolean;

	public constructor(mimeTypeService: MimeTypeService, messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils) {
		this.mimeTypeService = mimeTypeService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.init();
	}

	private init(): void {
		this.mimeTypeWindowVisible = false;
		this.selectedMimeType = null;
		this.isMimeTypeSaved = false;
	}

	public onMimeTypesSelectionChanged(mimeTypes: MimeTypeModel[]): void {
		this.selectedMimeType = mimeTypes[0];
	}

	public isMimeTypeSelected(): boolean {
		return ObjectUtils.isNotNullOrUndefined(this.selectedMimeType);
	}

	public onAdd(): void {
		this.mimeTypeWindowMode = "add";
		this.mimeTypeWindowVisible = true;
	}

	public onEdit(): void {
		this.mimeTypeWindowMode = "edit";
		this.selectedMimeTypeId = this.selectedMimeType.id;
		this.mimeTypeWindowVisible = true;
	}

	public onRemove(): void {
		this.confirmationUtils.confirm("CONFIRM_DELETE_MIME_TYPE", {
			approve: (): void => {
				this.removeSelectedMimeType();
			}, 
			reject: (): void => {}
		});
	}

	private removeSelectedMimeType(): void {
		this.mimeTypeService.deleteMimeType(this.selectedMimeType.id, {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("MIME_TYPE_DELETED");
				this.mimeTypeSelector.reload();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onRefreshMimeTypesSelector(): void {
		this.mimeTypeSelector.reload();
	}
	
	private onMimeTypeWindowClosed(): void {
		if (this.isMimeTypeSaved) {
			this.mimeTypeSelector.reload();
			this.isMimeTypeSaved = false;
		}
		this.mimeTypeWindowVisible = false;
	}

	private onMimeTypeWindowDataSaved(): void {
		this.isMimeTypeSaved = true;
	}
}
