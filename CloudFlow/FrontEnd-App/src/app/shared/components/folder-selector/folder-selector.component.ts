import { TreeNode } from "primeng/components/common/treenode";
import { Component, OnInit, Output, EventEmitter, Input } from "@angular/core";
import { DocumentLocationModel, FolderTreeNode, FolderModel, AppError, MoveFolderRequestModel } from "../../model";
import { FolderService, DocumentLocationService } from "../../service";
import { MessageDisplayer } from "../../message-displayer";
import { IconConstants } from "../../constants";
import { ArrayUtils } from "../../utils";

@Component({
	selector: "app-folder-selector",
	templateUrl: "./folder-selector.component.html",
	styleUrls: ["./folder-selector.component.css"]
})
export class FolderSelectorComponent implements OnInit {

	@Input()
	private documentLocations: DocumentLocationModel[];

	@Output()
	private folderSelected: EventEmitter<FolderTreeNode<DocumentLocationModel | FolderModel>>;

	private folderService: FolderService;
	private documentLocationService: DocumentLocationService;
	private messageDisplayer: MessageDisplayer;

	private documentLocation: DocumentLocationModel;

	public folders: TreeNode[];
	public selectedNode: TreeNode;
	public isFolderLoading: boolean;

	public constructor(folderService: FolderService, documentLocationService: DocumentLocationService, messageDisplayer: MessageDisplayer) {
		this.folderService = folderService;
		this.documentLocationService = documentLocationService;
		this.messageDisplayer = messageDisplayer;
		this.folderSelected = new EventEmitter<FolderTreeNode<DocumentLocationModel | FolderModel>>();
	}

	public ngOnInit(): void {
		this.init();
		if (ArrayUtils.isNotEmpty(this.documentLocations)) {
			this.populateFolders();
		}
	}

	private init(): void {
		this.isFolderLoading = false;
		this.folders = [];
	}

	private populateFolders(): void {
		this.documentLocations.forEach(documentLocation => {
			let rootNode: TreeNode = {
				label: documentLocation.name,
				data: new FolderTreeNode<DocumentLocationModel>(documentLocation),
				expandedIcon: IconConstants.DOCUMENT_LOCATION_ICON,
				collapsedIcon: IconConstants.DOCUMENT_LOCATION_ICON,
				children: [],
				leaf: false,
				draggable: false,
				droppable: true
			};
			this.folders.push(rootNode);
		});
	}

	private addDocumentLocationAsRootFolder(documentLocation: DocumentLocationModel): void {
		let rootNode: TreeNode = {
			label: documentLocation.name,
			data: new FolderTreeNode<DocumentLocationModel>(documentLocation),
			expandedIcon: IconConstants.DOCUMENT_LOCATION_ICON,
			collapsedIcon: IconConstants.DOCUMENT_LOCATION_ICON,
			children: [],
			leaf: false,
			draggable: false,
			droppable: true
		};
		this.folders.push(rootNode);
	}

	private addChildsToNode(node: TreeNode, children: FolderModel[]): void {
		children.forEach((folder: FolderModel) => {
			this.addChildToNode(node, folder);
		});
	}

	private addChildToNode(parent: TreeNode, folder: FolderModel): void {
		if (this.nodeHasChildByChildName(parent, folder.name)) {
			return;
		}

		let childNode: TreeNode = {
			label: folder.name,
			data: new FolderTreeNode<FolderModel>(folder),
			parent: parent,
			expandedIcon: IconConstants.FOLDER_EXPANDED_ICON,
			collapsedIcon: IconConstants.FOLDER_COLLAPSED_ICON,
			leaf: false,
			children: [],
			draggable: true,
			droppable: true
		};
		parent.children.push(childNode);
	}

	private nodeHasChildByChildName(node: TreeNode, childName: string): boolean {
		let nodeHasChild: boolean = false;
		node.children.forEach((child: TreeNode) => {
			if (child.label === childName) {
				nodeHasChild = true;
			}
		});
		return nodeHasChild;
	}

	public onNodeExpand(event): void {
		this.isFolderLoading = true;
		let expandedNode: TreeNode = event.node;
		let expandedNodeData: FolderTreeNode<DocumentLocationModel | FolderModel> = this.getNodeData(expandedNode);
		
		if (expandedNodeData.isDocumentLocation()) {
			let documentLocationModel: DocumentLocationModel = <DocumentLocationModel> expandedNodeData.data;
			this.documentLocationService.getFoldersFromDocumentLocation(documentLocationModel.documentLocationRealName, {
				onSuccess: (folders: FolderModel[]) => {
					this.addChildsToNode(expandedNode, folders);
					this.isFolderLoading = false;
				},
				onFailure: (error: AppError) => {
					this.messageDisplayer.displayAppError(error);
					this.isFolderLoading = false;
				}
			});
		} else if(expandedNodeData.isFolder()) {
			let folderModel: FolderModel = <FolderModel> expandedNodeData.data;
			let expandedNodeFolderId: string = folderModel.id;
			this.folderService.getFoldersFromFolder(folderModel.documentLocationRealName, expandedNodeFolderId, {
				onSuccess: (folders: FolderModel[]) => {
					this.addChildsToNode(expandedNode, folders);
					this.isFolderLoading = false;
				},
				onFailure: (error: AppError) => {
					this.messageDisplayer.displayAppError(error);
					this.isFolderLoading = false;
				}
			});
		} else {
			throw new Error("The [data] property of [TreeNode] isn't an instance of [" + DocumentLocationModel.name + "] or [" + FolderModel.name + "].");
		}
	}

	public onNodeSelect(event: any): void {
		this.selectedNode = event.node;
		this.folderSelected.emit(this.getNodeData(this.selectedNode));
	}

	public onNodeUnselect(event: any): void {
		this.folderSelected.emit(null);
	}

	public onNodeDrop(event: any): void {
		this.moveFolder(event.dragNode, event.dropNode);
	}
	
	private moveFolder(sourceNode: TreeNode, destinationNode: TreeNode): void {
		let sourceNodeData: FolderTreeNode<DocumentLocationModel | FolderModel> = this.getNodeData(sourceNode);
		let destinationNodeData: FolderTreeNode<DocumentLocationModel | FolderModel> = this.getNodeData(destinationNode);

		let sourceNodeFolderId: string = sourceNodeData.data.id;
		let destinationNodeFolderId: string = null;

		if (destinationNodeData.isFolder()) {
			destinationNodeFolderId = destinationNodeData.data.id;
		}

		this.isFolderLoading = true;
		this.folderService.moveFolder(
			this.buildMoveFolderRequestModel(sourceNodeFolderId, destinationNodeFolderId, this.documentLocation.documentLocationRealName), 
			{
				onSuccess: () => {
					this.messageDisplayer.displaySuccess("FOLDER_MOVED");
					this.isFolderLoading = false;
				},
				onFailure: (error: AppError) => {
					this.messageDisplayer.displayAppError(error);
					this.refresh();
					this.isFolderLoading = false;
				}
			}
		);
	}

	private getNodeData(node: TreeNode): FolderTreeNode<DocumentLocationModel | FolderModel> {
		return node.data;
	}

	private buildMoveFolderRequestModel(folderToMoveId: string, destinationFolderId: string, documentLocationRealName: string): MoveFolderRequestModel {
		let moveFolderRequestModel: MoveFolderRequestModel = new MoveFolderRequestModel();
		moveFolderRequestModel.folderToMoveId = folderToMoveId;
		moveFolderRequestModel.destinationFolderId = destinationFolderId;
		moveFolderRequestModel.documentLocationRealName = documentLocationRealName;
		return moveFolderRequestModel;
	}

	public setDocumentLocation(documentLocation: DocumentLocationModel): void {
		this.documentLocation = documentLocation;
		this.resetTreeAndAddDocumentLocationAsRootNode();
	}

	public refresh(): void {
		this.resetTreeAndAddDocumentLocationAsRootNode();
	}

	private resetTreeAndAddDocumentLocationAsRootNode(): void {
		this.reset();
		this.addDocumentLocationAsRootFolder(this.documentLocation);
	}
	
	private reset(): void {
		this.folders = [];
		this.isFolderLoading = false;
	}
}
