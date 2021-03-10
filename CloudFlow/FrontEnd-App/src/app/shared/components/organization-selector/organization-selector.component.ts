import { Component, Input, OnInit, EventEmitter, Output } from "@angular/core";
import { OrganizationService } from "./../../service";
import { MessageDisplayer } from "./../../message-displayer";
import { AppError, OrganizationTreeModel, OrganizationTreeNodeModel, OrganizationTreeNodeType } from "./../../model";
import { TreeNode } from "primeng/components/common/treenode";
import { ArrayUtils, StringUtils, ObjectUtils } from "./../../utils";
import { IconConstants } from "./../../constants";

@Component({
	selector: "app-organization-selector",
	templateUrl: "./organization-selector.component.html"
})
export class OrganizationSelectorComponent implements OnInit {

	@Input()
	public selectionMode: string = "multiple";

	@Input()
	public selectableEntityTypes: ["ou" | "user" | "organization"];

	@Input()
	public visibleEntityTypes: ["ou" | "user" | "organization"];
	
	@Output() 
	public selectionChanged = new EventEmitter<OrganizationTreeNodeModel[]>();

	@Output() 
	public dataLoaded = new EventEmitter<OrganizationTreeModel>();

	public organizationTree: TreeNode[];
	public selectedTreeNodes: TreeNode | TreeNode[];
	public loading: boolean = false;

	private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;

	private isUserSelectable: boolean = false;
	private isOuSelectable: boolean = false;
	private isOrganizationSelectable: boolean = false;

	public constructor(organizationService: OrganizationService, messageDisplayer: MessageDisplayer) {
		this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;
	}

	ngOnInit() {
		this.init();
	}

	private init() {

		if (ObjectUtils.isNotNullOrUndefined(this.selectableEntityTypes) && ObjectUtils.isNotArray(this.selectableEntityTypes)) {
			throw new Error("Input property [selectableEntityTypes] must be an array instance");
		}

		this.organizationTree = [];
		this.loadOrganizationTree();
		
		if (ArrayUtils.isNotEmpty(this.selectableEntityTypes)) {
			this.isUserSelectable = false;
			this.isOuSelectable = false;
			this.selectableEntityTypes.forEach((entityType: string) => {
				if (ObjectUtils.isNotNullOrUndefined(entityType)) {
					if (entityType === "user") {
						this.isUserSelectable = true;
					} else if (entityType === "ou") {
						this.isOuSelectable = true;
					} else if (entityType === "organization") {
						this.isOrganizationSelectable = true;
					} else {	
						throw new Error("selectableEntityType value unknown [" + entityType + "]");
					}
				}
			});
		}
	}

	private reset() {
		this.organizationTree = [];
	}

	public refresh() {
		this.loadOrganizationTree();
	}

	private loadOrganizationTree(): void {
		this.reset();
		this.loading = true;
		this.organizationService.getOrganizationTree({
			onFailure: (appError: AppError) => {
				this.loading = false;
				this.messageDisplayer.displayAppError(appError);
			},
			onSuccess: (organizationTree: OrganizationTreeModel) => {
					
				if (organizationTree == null) {
					this.loading = false;
					return;
				}

				if (!this.isNodeVisible(organizationTree.rootNode)) {
					return;
				}

				let rootNode: TreeNode = this.createTreeNode(organizationTree.rootNode);
				rootNode.expanded = true;
				this.organizationTree.push(rootNode);
				
				this.loading = false;
				this.dataLoaded.emit(organizationTree);
			}
		});
	}

	private getNodeLabel(nodeModel: OrganizationTreeNodeModel): string {
		if (nodeModel.isOrganization()) {
			return nodeModel.name;
		} else if (nodeModel.isOrganizationUnit()) {
			return nodeModel.name;
		} else if (nodeModel.isUser()) {
			if (StringUtils.isNotBlank(nodeModel.title)) {
				if (StringUtils.isNotBlank(nodeModel.customTitleTemplate)) {
					return this.getUserNameWithTitle(nodeModel.name, this.getUserTitleByTemplate(nodeModel.customTitleTemplate, nodeModel.title));
				} else {
					return this.getUserNameWithTitle(nodeModel.name, nodeModel.title);
				}
			} else {
				return nodeModel.name;
			}
		} else {
			throw new Error("Tipul de nod nu este cunoscut pentru determinarea label-ului [" + nodeModel.type + "]");
		}
	}

	private getUserNameWithTitle(name: string, title: string): string {
		return name + " - " + title;
	}

	private getUserTitleByTemplate(template: string, title: string): string {
		return template.replace("{title}", title);
	}

	private getNodeIconName(nodeModel: OrganizationTreeNodeModel): string {
		if (nodeModel.isOrganization()) {
			return IconConstants.ORGANIZATION_ICON;
		} else if (nodeModel.isOrganizationUnit()) {
			return IconConstants.ORGANIZATION_UNIT_ICON;
		} else if (nodeModel.isUser()) {
			return IconConstants.ORGANIZATION_USER_ICON;
		} else {
			throw new Error("Tipul de nod nu este cunoscut pentru determinarea icon-ului [" + nodeModel.type + "]");
		}
	}

	private getIsNodeSelectable(node: OrganizationTreeNodeModel): boolean {
		if (node.isUser()) {
			return this.isUserSelectable;
		} else if (node.isOrganizationUnit()) {
			return this.isOuSelectable;
		} else if (node.isOrganization()) {
			return this.isOrganizationSelectable;
		}
	}

	private isNodeVisible(node: OrganizationTreeNodeModel): boolean {
		if (ArrayUtils.isEmpty(this.visibleEntityTypes)) {
			return true;
		} else if (this.visibleEntityTypes.indexOf("ou") !== -1 && node.isOrganizationUnit()) {
			return true;
		} else if (this.visibleEntityTypes.indexOf("user") !== -1 && node.isUser()) {
			return true;
		} else if (this.visibleEntityTypes.indexOf("organization") !== -1 && node.isOrganization()) {
			return true;
		}
		return false;
	}

	private createTreeNode(nodeModel: OrganizationTreeNodeModel): TreeNode {
		
		let treeNode: TreeNode = {};
		treeNode.label = this.getNodeLabel(nodeModel);
		treeNode.data = nodeModel;
		treeNode.leaf = ArrayUtils.isEmpty(nodeModel.children);
		let iconName = this.getNodeIconName(nodeModel);
		treeNode.expandedIcon = iconName;
		treeNode.collapsedIcon = iconName;
		treeNode.selectable = this.getIsNodeSelectable(nodeModel);
		treeNode.children = [];
		
		if (ArrayUtils.isNotEmpty(nodeModel.children)) {					
			// TODO - Sortarea sa se faca astfel incat intai sa fie useri care sunt manageri
			// apoi ceilalti si la urma unitatile organizatorice (in cadrul unitatilor 
			// organizatorice regula este la fel)
			nodeModel.children.sort((nodeModel1: OrganizationTreeNodeModel, nodeModel2: OrganizationTreeNodeModel): number => {
				if (nodeModel1.name < nodeModel2.name) {
					return -1;
				}
				if (nodeModel1.name > nodeModel2.name) {
					return 1;
				}
				return 0;
			});
			nodeModel.children.forEach((organizationChildNode: OrganizationTreeNodeModel) => {
				if (this.isNodeVisible(organizationChildNode)) {
					treeNode.children.push(this.createTreeNode(organizationChildNode));
				}
			});
		}
		return treeNode;
	}

	public getSelectedNodeModels(): OrganizationTreeNodeModel[] {
		let selectedNodeModels: OrganizationTreeNodeModel[] = [];
		if (ObjectUtils.isNullOrUndefined(this.selectedTreeNodes)) {
			return [];
		}
		if (this.selectedTreeNodes instanceof Array) {		
			if (ArrayUtils.isNotEmpty(this.selectedTreeNodes)) {
				this.selectedTreeNodes.forEach((selectedTreeNode: TreeNode) => {
					selectedNodeModels.push(selectedTreeNode.data);
				});
			}
		} else {
			selectedNodeModels.push(this.selectedTreeNodes.data);
		}
		return selectedNodeModels;
	}

	public onSelectionChange(event): void {
		this.selectionChanged.emit(this.getSelectedNodeModels());
	}	
}