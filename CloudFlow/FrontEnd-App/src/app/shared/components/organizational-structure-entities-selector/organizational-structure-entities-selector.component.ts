import { Component, Output, EventEmitter, Input, OnInit } from "@angular/core";
import { OrganizationEntityModel } from "../../model/organization-entity.model";
import { OrganizationTreeNodeModel, GroupModel, OrganizationTreeModel } from "../../model";
import { IconConstants } from "../../constants";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { ObjectUtils, ArrayUtils } from "../../utils";
import { timeout } from "rxjs/operators";

@Component({
	selector: "app-organizational-structure-entities-selector",
	templateUrl: "./organizational-structure-entities-selector.component.html",
	styleUrls: ["./organizational-structure-entities-selector.component.css"],
	providers: [{
		provide: NG_VALUE_ACCESSOR,
		useExisting: OrganizationalStructureEntitiesSelectorComponent,
		multi: true
	}]
})
export class OrganizationalStructureEntitiesSelectorComponent implements ControlValueAccessor, OnInit {

	public static readonly ORGANIZATIONAL_STRUCTURE_TAB_INDEX = 0;
	public static readonly GROUPS_TAB_INDEX = 1;

	@Input()
	private organizationEntities: OrganizationEntityModel[];

	@Input()
	public readonly: boolean = false;

	@Input()
	public groupsVisible: boolean = true;

	@Input()
	public selectableEntityTypes: string[];

	@Input()
	public visibleEntityTypes: string[];

	@Output()
	private selectionChanged: EventEmitter<OrganizationEntityModel[]>;

	public entitiesViewInfo: OrganizationalEntityViewInfo[];
	public selectedEntitiesViewInfo: OrganizationalEntityViewInfo[];

	private activeTabIndex: number;

	private selectedGroups: GroupModel[];
	private selectedOrganizationTreeNodes: OrganizationTreeNodeModel[];

	private groupsLoaded: GroupModel[];
	private organizationTreeNodeLoaded: OrganizationTreeModel;

	private onChange = (entities: OrganizationEntityModel[]) => {};
	private onTouche = () => {};

	public constructor() {
		this.selectionChanged = new EventEmitter<OrganizationEntityModel[]>();
		this.init();
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNullOrUndefined(this.selectableEntityTypes)) {
			this.selectableEntityTypes = ["ou", "user"];
		}
	}

	private init(): void {
		this.activeTabIndex = OrganizationalStructureEntitiesSelectorComponent.ORGANIZATIONAL_STRUCTURE_TAB_INDEX;
		this.entitiesViewInfo = [];
		this.selectedEntitiesViewInfo = [];
		this.selectedGroups = [];
		this.selectedOrganizationTreeNodes = [];
	}

	public onOpenTab(event: any): void {
		this.activeTabIndex = event.index;
	}
	
	public onOrganizationSelectorDataLoaded(organizationTreeModel: OrganizationTreeModel): void {
		this.organizationTreeNodeLoaded = organizationTreeModel;
		if ((ArrayUtils.isNotEmpty(this.groupsLoaded) || !this.groupsVisible) && ArrayUtils.isNotEmpty(this.organizationEntities)) {
			this.prepareOrganizationEntitiesViewInfo(this.organizationEntities);
		}
	}

	public onGroupSelectorDataLoaded(groups: GroupModel[]): void {
		this.groupsLoaded = groups;
		if (ArrayUtils.isNotEmpty(this.organizationEntities) && ObjectUtils.isNotNullOrUndefined(this.organizationTreeNodeLoaded)) {
			this.prepareOrganizationEntitiesViewInfo(this.organizationEntities);
		}
	}

	public onGroupSelectionChanged(groups: GroupModel[]): void {
		this.selectedGroups = groups;
	}

	public onOrganizationSelectionChanged(organizationTreeNodes: OrganizationTreeNodeModel[]): void {
		this.selectedOrganizationTreeNodes = organizationTreeNodes;
	}

	private prepareOrganizationEntitiesViewInfo(entities: OrganizationEntityModel[]): void {
		entities.forEach((entity: OrganizationEntityModel) => {
			if (entity.isUserType() || entity.isOrganizationUnitType()) {
				let organizationTreeNode: OrganizationTreeNodeModel = this.findOrganizationTreeNodeByEntity([this.organizationTreeNodeLoaded.rootNode], entity);
				if (ObjectUtils.isNotNullOrUndefined(organizationTreeNode)) {
					this.prepareAndAddOrganizationEntityViewInfo(organizationTreeNode);
				}
			} else if (entity.isGroupType()) {
				this.groupsLoaded.forEach((group: GroupModel) => {
					if (Number(group.id) === entity.id) {
						this.prepareAndAddOrganizationEntityViewInfo(group);
					}
				});
			}
		});
		this.propagateValue();
	}

	private findOrganizationTreeNodeByEntity(models: OrganizationTreeNodeModel[], entity: OrganizationEntityModel): OrganizationTreeNodeModel {
		if (ArrayUtils.isEmpty(models)) {
			return;
		}
		
		let organizationTreeNodeModel: OrganizationTreeNodeModel = models.filter(orgTreeModel => Number(orgTreeModel.id) === entity.id)[0];
		
		if (ObjectUtils.isNotNullOrUndefined(organizationTreeNodeModel)) {
			return organizationTreeNodeModel;
		}

		for (let index = 0; index < models.length; index++) {
			organizationTreeNodeModel = this.findOrganizationTreeNodeByEntity(models[index].children, entity);
			if (ObjectUtils.isNotNullOrUndefined(organizationTreeNodeModel)) {
				return organizationTreeNodeModel;
			}
		}
	}

	private prepareAndAddOrganizationEntityViewInfo(item: GroupModel | OrganizationTreeNodeModel): void {
		let organizationEntityViewInfo: OrganizationalEntityViewInfo;

		if (item instanceof GroupModel) {
			let organizationEntity: OrganizationEntityModel = new OrganizationEntityModel();
			organizationEntity.id = Number(item.id);
			organizationEntity.type = OrganizationEntityModel.TYPE_GROUP;

			organizationEntityViewInfo = {
				label: item.name,
				icon: IconConstants.ORGANIZATION_GROUP_ICON,
				entity: organizationEntity
			};
		} else if (item instanceof OrganizationTreeNodeModel) {
			let icon: string = "";

			let organizationEntity: OrganizationEntityModel = new OrganizationEntityModel();
			organizationEntity.id = Number(item.id);

			if (item.isOrganization()) {
				organizationEntity.type = OrganizationEntityModel.TYPE_ORG_UNIT;
				icon = IconConstants.ORGANIZATION_UNIT_ICON;
			} else if (item.isOrganizationUnit()) {
				organizationEntity.type = OrganizationEntityModel.TYPE_ORG_UNIT;
				icon = IconConstants.ORGANIZATION_UNIT_ICON;
			} else if (item.isUser()) {
				organizationEntity.type = OrganizationEntityModel.TYPE_USER;
				icon = IconConstants.ORGANIZATION_USER_ICON;
			}

			organizationEntityViewInfo = {
				label: item.name,
				icon: icon,
				entity: organizationEntity
			};
		}
		this.entitiesViewInfo.push(organizationEntityViewInfo);
	}

	public onAddEntity(): void {
		this.selectedEntitiesViewInfo = [];
		if (this.activeTabIndex === OrganizationalStructureEntitiesSelectorComponent.GROUPS_TAB_INDEX) {
			this.selectedGroups.forEach((selectedGroup: GroupModel) => {
				if (!this.entityExists(selectedGroup)) {
					this.prepareAndAddOrganizationEntityViewInfo(selectedGroup);
				}
			});
		} else if (this.activeTabIndex === OrganizationalStructureEntitiesSelectorComponent.ORGANIZATIONAL_STRUCTURE_TAB_INDEX) {
			this.selectedOrganizationTreeNodes.forEach((selectedOrganizationTreeNode: OrganizationTreeNodeModel) => {
				if (!this.entityExists(selectedOrganizationTreeNode)) {
					this.prepareAndAddOrganizationEntityViewInfo(selectedOrganizationTreeNode);
				}
			});
		}
		this.propagateValue();
	}

	public onDeleteEntity(): void {
		this.selectedEntitiesViewInfo.forEach((selectedEntityViewInfo: OrganizationalEntityViewInfo) => {
			this.entitiesViewInfo.forEach((entityViewInfo: OrganizationalEntityViewInfo) => {
				if (selectedEntityViewInfo === entityViewInfo) {
					let deletedEntitiViewInfoIndex = this.entitiesViewInfo.indexOf(entityViewInfo);
					this.entitiesViewInfo.splice(deletedEntitiViewInfoIndex, 1);
				}
			});
		});
		this.propagateValue();
	}

	private getOrganizationEntities(): OrganizationEntityModel[] {
		let entities: OrganizationEntityModel[] = [];
		this.entitiesViewInfo.forEach((entityViewInfo: OrganizationalEntityViewInfo) => {
			entities.push(entityViewInfo.entity);
		});
		return entities;
	}

	private entityExists(item: GroupModel | OrganizationTreeNodeModel): boolean {
		let exists: boolean = false;
		this.entitiesViewInfo.forEach((entityViewInfo: OrganizationalEntityViewInfo) => {
			if (entityViewInfo.entity.id === Number(item.id)) {
				exists = true;
			}
		});
		return exists;
	}

	private propagateValue(): void {
		this.selectionChanged.emit(this.getOrganizationEntities());
		this.onChange(this.getOrganizationEntities());
		this.onTouche();
	}

	public writeValue(entities: OrganizationEntityModel[]): void {
		this.reset();
		if (ArrayUtils.isEmpty(entities)) {
			return;
		}
		this.organizationEntities = entities;
		if (ArrayUtils.isNotEmpty(this.groupsLoaded) && ObjectUtils.isNotNullOrUndefined(this.organizationTreeNodeLoaded)) {
			this.prepareOrganizationEntitiesViewInfo(entities);
		}
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}
	
	public registerOnTouched(fn: any): void {
		this.onTouche = fn;
	}

	private reset(): void {
		this.entitiesViewInfo = [];
		this.organizationEntities = [];
	}
}

interface OrganizationalEntityViewInfo {
	label: string;
	icon: string;
	entity: OrganizationEntityModel;
}
