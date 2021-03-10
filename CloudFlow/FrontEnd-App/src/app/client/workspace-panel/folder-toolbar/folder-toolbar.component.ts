import { Component, Output, ViewChild, EventEmitter } from "@angular/core";
import { FolderWindowInputData } from "../folder-window/folder-window-input-data";
import { 
	FolderModel,
	ConfirmationUtils,
	FolderTreeNode,
	DocumentLocationModel,
	FolderService,
	MessageDisplayer,
	AppError,
	GetFolderPathRequestModel
} from "@app/shared";
import { FolderWindowComponent } from "../folder-window/folder-window.component";

@Component({
	selector: "app-folder-toolbar",
	templateUrl: "./folder-toolbar.component.html",
	styleUrls: ["./folder-toolbar.component.css"]
})
export class FolderToolbarComponent {

	@Output()
	private folderDeleted: EventEmitter<void>;

	@Output()
	private folderSaved: EventEmitter<void>;

	public folderWindowInputData: FolderWindowInputData;
	public folderWindowMode: string;
	public folderWindowVisible: boolean;

	private folderService: FolderService;
	private messageDisplayer: MessageDisplayer;

	private confirmationUtils: ConfirmationUtils;
	private folderTreeNode: FolderTreeNode<DocumentLocationModel | FolderModel>;

	public isDisabledForAdd: boolean;
	public isDisabledForEdit: boolean;
	public isDisabledForDelete: boolean;

	public constructor(folderService: FolderService, messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils) {
		this.folderService = folderService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.folderDeleted = new EventEmitter<void>();
		this.folderSaved = new EventEmitter<void>();
		this.init();
	}

	private init(): void {
		this.folderWindowVisible = false;
		this.disableAllActions();
	}
	
	public onAddFolder(): void {
		this.folderWindowMode = FolderWindowComponent.FOLDER_WINDOW_NEW_MODE;
		this.prepareFolderWindowInputData();
		this.folderWindowVisible = true;
	}

	public onEditFolder(): void {
		this.folderWindowMode = FolderWindowComponent.FOLDER_WINDOW_EDIT_MODE;
		this.prepareFolderWindowInputData();
		this.folderWindowVisible = true;
	}

	private prepareFolderWindowInputData(): void {
		this.folderWindowInputData = new FolderWindowInputData();
		this.folderWindowInputData.documentLocationRealName = this.folderTreeNode.data.documentLocationRealName;
		this.folderWindowInputData.folderId = null;
		if (this.folderTreeNode.isFolder()) {
			this.folderWindowInputData.folderId = this.folderTreeNode.data.id;
		}
	}

	public onFolderWindowClosed(): void {
		this.folderWindowVisible = false;
	}

	public onFolderSaved(): void {
		this.folderSaved.emit();
		this.folderWindowVisible = false;
	}

	public onDeleteFolder(): void {
		this.confirmationUtils.confirm("DELETE_FOLDER_CONFIRM_QUESTION", {
			approve: () => {
				this.folderService.deleteFolder(this.folderTreeNode.data.id, this.folderTreeNode.data.documentLocationRealName, {
					onSuccess: () => {
						this.messageDisplayer.displaySuccess("FOLDER_DELETED");
						this.folderDeleted.emit();
					},
					onFailure: (error: AppError) => {
						this.messageDisplayer.displayAppError(error);
					}
				});
			},
			reject: () => {
			}
		},
		"DELETE_FOLDER_CONFIRM_TITLE"
		);
	}

	public changePerspectiveByFolderTreeNode(folderTreeNode: FolderTreeNode<DocumentLocationModel | FolderModel>): void {
		this.folderTreeNode = folderTreeNode;
		this.changePerspective();
	}

	private disableAllActions(): void {
		this.isDisabledForAdd = true;
		this.isDisabledForEdit = true;
		this.isDisabledForDelete = true;
	}

	private changePerspective(): void {
		this.disableAllActions();

		if (this.folderTreeNode === null) {
			return;
		}

		if (this.folderTreeNode.isDocumentLocation()) {
			this.isDisabledForAdd = false;
		} else if (this.folderTreeNode.isFolder()) {
			this.isDisabledForAdd = false;
			this.isDisabledForEdit = false;
			this.isDisabledForDelete = false;
		}
	}
}