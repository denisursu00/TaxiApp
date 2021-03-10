import { Component, OnInit, Input, ViewChild, Output, EventEmitter } from "@angular/core";
import { FolderGeneralTabContentComponent } from "./folder-general-tab-content/folder-general-tab-content.component";
import { FolderSecurityTabContentComponent } from "./folder-security-tab-content/folder-security-tab-content.component";
import { FolderWindowInputData } from "./folder-window-input-data";
import {
	FolderModel,
	StringUtils,
	FolderTreeNode,
	DocumentLocationModel,
	GetFolderPathRequestModel,
	FolderService,
	AppError,
	MessageDisplayer,
	PermissionModel,
	ObjectUtils,
	BaseWindow
} from "@app/shared";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-folder-window",
	templateUrl: "./folder-window.component.html",
	styleUrls: ["./folder-window.component.css"]
})
export class FolderWindowComponent extends BaseWindow implements OnInit {

	public static readonly FOLDER_WINDOW_NEW_MODE = "new";
	public static readonly FOLDER_WINDOW_EDIT_MODE = "edit";

	@Input()
	public inputData: FolderWindowInputData;

	@Input()
	public mode: "new" | "edit";

	@Output()
	private windowClosed: EventEmitter<void>;

	@Output()
	private folderSaved: EventEmitter<void>;

	@ViewChild(FolderGeneralTabContentComponent)
	private generalTabContent: FolderGeneralTabContentComponent;

	@ViewChild(FolderSecurityTabContentComponent)
	private securityTabContent: FolderSecurityTabContentComponent;

	private folderService: FolderService;
	private messageDisplayer: MessageDisplayer;

	public visible: boolean;

	public path: string;
	public folder: FolderModel;
	public permissions: PermissionModel[];

	public tabContentVisible: boolean = false;

	public constructor(folderService: FolderService, messageDisplayer: MessageDisplayer) {
		super();
		this.folderService = folderService;
		this.messageDisplayer = messageDisplayer;
		this.windowClosed = new EventEmitter<void>();
		this.folderSaved = new EventEmitter<void>();
		this.init();
	}

	private init(): void {
		this.visible = false;
	}

	public ngOnInit(): void {
		if (this.mode === FolderWindowComponent.FOLDER_WINDOW_NEW_MODE) {
			this.prepareForAdd();
		} else if (this.mode === FolderWindowComponent.FOLDER_WINDOW_EDIT_MODE) {
			this.prepareForEdit();
		}
	}

	public prepareForAdd(): void {
		this.folderService.getFolderPath(this.buildGetFolderPathRequestModel(), {
			onSuccess: (path: string): void => {
				this.path = path;
				this.visible = true;
				this.tabContentVisible = true;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public prepareForEdit(): void {
		this.folderService.getFolderById(this.inputData.folderId, this.inputData.documentLocationRealName, {
			onSuccess: (folder: FolderModel): void => {
				this.folderService.getFolderPath(this.buildGetFolderPathRequestModel(), {
					onSuccess: (path: string): void => {
						this.path = path;
						this.folder = folder;
						this.permissions = folder.permissions;
						this.visible = true;
						this.tabContentVisible = true;
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private buildGetFolderPathRequestModel(): GetFolderPathRequestModel {
		return new GetFolderPathRequestModel(this.inputData.documentLocationRealName, this.inputData.folderId);
	}

	public onSave(): void {
		if (!this.generalTabContent.isValid() || !this.securityTabContent.isValid()) {
			return;
		}
		
		this.folderService.saveFolder(this.getFolderModel(), {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("FOLDER_SAVED");
				this.folderSaved.emit();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private getFolderModel(): FolderModel {
		let folder: FolderModel = new FolderModel();
		this.generalTabContent.populateFolder(folder);
		this.securityTabContent.populateFolder(folder);
		return folder;
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}
}