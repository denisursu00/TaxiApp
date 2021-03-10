import { Component, ViewChild, Output, OnInit } from "@angular/core";
import { FolderToolbarComponent } from "./folder-toolbar";
import { FolderModel, DocumentLocationModel, FolderTreeNode, DocumentViewModel, ObjectUtils, FolderSelectorComponent } from "@app/shared";
import { DocumentGridComponent } from "./document-grid/document-grid.component";
import { DocumentWindowComponent } from "@app/client/common/document-window";
import { DocumentWindowInputData } from "@app/client/common/document-window";
import { DocumentToolbarComponent } from "./document-toolbar/document-toolbar.component";
import { DocumentLocationToolbarComponent } from "./document-location-toolbar/index";
import { ClientCommonModule } from "@app/client/common";
import { DocumentLocationSelectorComponent } from "@app/client/common";

@Component({
	selector: "app-workspace-panel",
	templateUrl: "./workspace-panel.component.html",
	styleUrls: ["./workspace-panel.component.css"]
})
export class WorkspacePanelComponent implements OnInit {

	@ViewChild(FolderSelectorComponent)
	private folderSelectorComponent: FolderSelectorComponent;

	@ViewChild(FolderToolbarComponent)
	private folderToolbarComponent: FolderToolbarComponent;

	@ViewChild(DocumentGridComponent)
	private documentsGridComponent: DocumentGridComponent;

	@ViewChild(DocumentWindowComponent)
	private documentWindow: DocumentWindowComponent;

	@ViewChild(DocumentToolbarComponent)
	private documentToolbar: DocumentToolbarComponent;

	@ViewChild(DocumentLocationToolbarComponent)
	private documentLocationToolbar: DocumentLocationToolbarComponent;

	@ViewChild(DocumentLocationSelectorComponent)
	private documentLocationSelector: DocumentLocationSelectorComponent;

	private documentLocation: DocumentLocationModel;
	private folder: FolderTreeNode<DocumentLocationModel | FolderModel>;

	public documentWindowVisible: boolean;
	public documentWindowInputData: DocumentWindowInputData;
	public documentWindowMode: string;

	public blockedPanel: boolean;

	constructor() {}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		this.documentWindowVisible = false;
		this.blockedPanel = false;
	}

	public onDocumentLocationChanged(documentLocation: DocumentLocationModel): void {
		this.documentLocation = documentLocation;
		this.folderSelectorComponent.setDocumentLocation(documentLocation);
		this.documentsGridComponent.setFolderAndResetDocumentGrid(null);
		this.folderToolbarComponent.changePerspectiveByFolderTreeNode(null);
		this.documentLocationToolbar.setDocumentLocation(documentLocation.documentLocationRealName);
		this.resetDocumentToolbarPerspective();
	}

	public onFolderSelected(folder: FolderTreeNode<DocumentLocationModel | FolderModel>): void {
		this.folderToolbarComponent.changePerspectiveByFolderTreeNode(folder);

		if (folder === null || this.folder === folder) {
			return;
		}

		this.folder  = folder;
		this.resetDocumentToolbarPerspective();

		if (folder.isFolder()) {
			this.documentsGridComponent.setFolderAndResetDocumentGrid(<FolderModel>folder.data);
			this.documentToolbar.setFolder(<FolderModel>folder.data);
		} else {
			this.documentsGridComponent.setFolderAndResetDocumentGrid(null);
		}
	}

	public onFolderDeleted(): void {
		this.folderSelectorComponent.setDocumentLocation(this.documentLocation);
		this.documentsGridComponent.setFolderAndResetDocumentGrid(null);
		this.resetDocumentToolbarPerspective();
	}

	public onFolderSaved(): void {
		this.folderSelectorComponent.setDocumentLocation(this.documentLocation);
		this.documentsGridComponent.setFolderAndResetDocumentGrid(null);
		this.resetDocumentToolbarPerspective();
	}

	private resetDocumentToolbarPerspective(): void {
		this.documentToolbar.setFolder(null);
		this.documentToolbar.setDocumentIdAndDocumentaLocationRealName(null, null);
	}

	public onDocumentWindowClosed(event: any): void {
		this.documentWindowVisible = false;
	}

	public onDocumentSelectionChanged(documentId: string): void {
		if (ObjectUtils.isNotNullOrUndefined(documentId)) {
			this.documentToolbar.setDocumentIdAndDocumentaLocationRealName(documentId, this.documentLocation.documentLocationRealName);
		} else if (ObjectUtils.isNotNullOrUndefined(this.documentLocation)) {
			this.documentToolbar.setDocumentIdAndDocumentaLocationRealName(null, this.documentLocation.documentLocationRealName);
		} else {
			this.documentToolbar.setDocumentIdAndDocumentaLocationRealName(null, null);
		}
	}

	public onDocumentToolbarDocumentActionPerformed(): void {
		this.documentsGridComponent.refresh();
	}

	public onDocumentLocationSavedOrDeleted(): void {
		this.documentLocationSelector.reset();
	}

	public onDocumentGridLoadStarted(): void {
		this.blockedPanel = true;
	}

	public onDocumentGridLoadEnded(): void {
		this.blockedPanel = false;
	}
}