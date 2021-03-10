import { Component, Output, EventEmitter, Input, OnInit, OnChanges } from "@angular/core";
import { OrganizationEntityModel } from "@app/shared/model/organization-entity.model";
import { OrganizationTreeNodeModel, GroupModel, OrganizationTreeModel, CalendarUserRightsModel } from "@app/shared/model";
import { IconConstants } from "@app/shared/constants";
import { ObjectUtils, ArrayUtils } from "@app/shared/utils";
import { SelectItem } from "primeng/primeng";

@Component({
	selector: "app-calendar-user-rights-selector",
	templateUrl: "./calendar-user-rights-selector.component.html"
})
export class CalendarUserRightsSelectorComponent implements OnInit, OnChanges {

	public static readonly USER_ORGANIZATION_ENTITY_TYPE: number = 1;

	public static readonly USER_RIGHT_VIEW_VALUE: string = "view";
	public static readonly USER_RIGHT_ADD_VALUE: string = "add";
	public static readonly USER_RIGHT_EDIT_VALUE: string = "edit";
	public static readonly USER_RIGHT_DELETE_VALUE: string = "delete";

	@Input()
	private usersRights: CalendarUserRightsModel[];

	@Input()
	private calendarId: number;

	@Input()
	public premitAll: boolean = false;

	@Input()
	public mode: "add" | "edit";

	@Output()
	public selectionChanged: EventEmitter<CalendarUserRightsModel[]>;

	public selectableEntityTypes: string[];
	public visibleEntityTypes: string[];

	public userRightsItems: SelectItem[];

	public userRightsViewInfo: UserRightsViewInfo[];
	public selectedUserRightsViewInfo: UserRightsViewInfo[];
	public selectedUserRightsViewInfoOnPermitAll: UserRightsViewInfo[];

	private selectedOrganizationTreeNodes: OrganizationTreeNodeModel[];
	
	private organizationTreeNode: OrganizationTreeModel;

	public constructor() {
		this.selectionChanged = new EventEmitter<CalendarUserRightsModel[]>();
		this.init();
	}

	private init(): void {
		this.userRightsViewInfo = [];
		this.selectedUserRightsViewInfo = [];
		this.selectableEntityTypes = ["user"];
		this.visibleEntityTypes = ["organization", "ou", "user"];
		this.prepareRightsItems();
	}

	private prepareRightsItems(): void {
		this.userRightsItems = [
			{ icon: "fa fa-eye", value: CalendarUserRightsSelectorComponent.USER_RIGHT_VIEW_VALUE, },
			{ icon: "fa fa-plus-square", value: CalendarUserRightsSelectorComponent.USER_RIGHT_ADD_VALUE },
			{ icon: "fa fa-pencil-square-o", value: CalendarUserRightsSelectorComponent.USER_RIGHT_EDIT_VALUE },
			{ icon: "fa fa-trash", value: CalendarUserRightsSelectorComponent.USER_RIGHT_DELETE_VALUE }
		];
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNullOrUndefined(this.mode)) {
			throw new Error("Input property [mode] cannot be null or undefined.");
		}
		if (this.mode !=="edit" && this.mode !== "add") {
			throw new Error("Input property [mode] can only have [add] or [edit] value.");
		}
		if (this.mode === "edit") {
			this.prepareForEdit();
		}
	}
	
	public ngOnChanges(): void {
		if (this.premitAll && ArrayUtils.isNotEmpty(this.userRightsViewInfo)) {
			this.selectedUserRightsViewInfoOnPermitAll = [... this.userRightsViewInfo];
			this.userRightsViewInfo = [];
		} else if (ArrayUtils.isNotEmpty(this.selectedUserRightsViewInfoOnPermitAll)) {
			this.userRightsViewInfo = [... this.selectedUserRightsViewInfoOnPermitAll];
			this.selectedUserRightsViewInfoOnPermitAll = [];
		}
	}

	public onOrganizationSelectorDataLoaded(organizationTreeModel: OrganizationTreeModel): void {
		this.organizationTreeNode = organizationTreeModel;
		if (ObjectUtils.isNotNullOrUndefined(this.usersRights)) {
			this.prepareSelectedUserRightsViewingInfo();
		}
	}

	private prepareForEdit(): void {
		if (ObjectUtils.isNullOrUndefined(this.organizationTreeNode)) {
			return;
		}
		this.prepareSelectedUserRightsViewingInfo();
	}

	private prepareSelectedUserRightsViewingInfo() {
		this.selectedUserRightsViewInfo = [];
		this.usersRights.forEach((userRights: CalendarUserRightsModel) => {
			let organizationTreeNode: OrganizationTreeNodeModel = this.findOrganizationTreeNodeByUserRights([this.organizationTreeNode.rootNode], userRights);
			if (ObjectUtils.isNotNullOrUndefined(organizationTreeNode)) {
				this.userRightsViewInfo.push(this.prepareUserRightsViewInfoFromModels(userRights, organizationTreeNode));
			}
		});
		this.selectionChanged.emit(this.prepareUsersRightsModels());
	}

	private prepareUserRightsViewInfoFromModels(userRights: CalendarUserRightsModel, organizationTreeNode: OrganizationTreeNodeModel): UserRightsViewInfo {
		let organizationEntity: OrganizationEntityModel = new OrganizationEntityModel();
		organizationEntity.id = Number(organizationTreeNode.id);
		organizationEntity.type = CalendarUserRightsSelectorComponent.USER_ORGANIZATION_ENTITY_TYPE;

		let userRightsAsArray: string[] = [];
		if (userRights.view) {
			userRightsAsArray.push(CalendarUserRightsSelectorComponent.USER_RIGHT_VIEW_VALUE);
		}
		if (userRights.add) {
			userRightsAsArray.push(CalendarUserRightsSelectorComponent.USER_RIGHT_ADD_VALUE);
		}
		if (userRights.edit) {
			userRightsAsArray.push(CalendarUserRightsSelectorComponent.USER_RIGHT_EDIT_VALUE);
		}
		if (userRights.delete) {
			userRightsAsArray.push(CalendarUserRightsSelectorComponent.USER_RIGHT_DELETE_VALUE);
		}

		let userRightsViewingInfo: UserRightsViewInfo = {
			id: userRights.id,
			entity: organizationEntity,
			icon: IconConstants.ORGANIZATION_USER_ICON,
			label: organizationTreeNode.name,
			rights: userRightsAsArray
		};		
		return userRightsViewingInfo;
	}

	private findOrganizationTreeNodeByUserRights(models: OrganizationTreeNodeModel[], userRights: CalendarUserRightsModel): OrganizationTreeNodeModel {
		if (ArrayUtils.isEmpty(models)) {
			return;
		}
		
		let organizationTreeNodeModel: OrganizationTreeNodeModel = models.filter(orgTreeModel => Number(orgTreeModel.id) === userRights.user.id)[0];
		
		if (ObjectUtils.isNotNullOrUndefined(organizationTreeNodeModel)) {
			return organizationTreeNodeModel;
		}

		for (let index = 0; index < models.length; index++) {
			organizationTreeNodeModel = this.findOrganizationTreeNodeByUserRights(models[index].children, userRights);
			if (ObjectUtils.isNotNullOrUndefined(organizationTreeNodeModel)) {
				return organizationTreeNodeModel;
			}
		}
	}

	public onOrganizationSelectionChanged(organizationTreeNodes: OrganizationTreeNodeModel[]): void {
		this.selectedOrganizationTreeNodes = organizationTreeNodes;
	}

	private prepareAndAddUsersRights(item: OrganizationTreeNodeModel): void {
		let userRightsViewingInfo: UserRightsViewInfo;
		
		if (item instanceof OrganizationTreeNodeModel) {
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

			userRightsViewingInfo = {
				id: null,
				label: item.name,
				icon: icon,
				entity: organizationEntity,
				rights: []
			};

		} else {
			throw new Error("Item cannot be instance of GroupModel");
		}
		
		this.userRightsViewInfo.push(userRightsViewingInfo);
	}

	public onAddUserRights(): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedOrganizationTreeNodes)) {
			return;
		}
		this.selectedOrganizationTreeNodes.forEach((selectedOrganizationTreeNode: OrganizationTreeNodeModel) => {
			if (!this.userRightsExists(selectedOrganizationTreeNode)) {
				this.prepareAndAddUsersRights(selectedOrganizationTreeNode);
				this.selectionChanged.emit(this.prepareUsersRightsModels());
			}
		});
	}

	public onDeleteUserRight(): void {
		this.selectedUserRightsViewInfo.forEach((selectedEntityViewInfo: UserRightsViewInfo) => {
			this.userRightsViewInfo.forEach((entityViewInfo: UserRightsViewInfo) => {
				if (selectedEntityViewInfo === entityViewInfo) {
					let deletedEntitiViewInfoIndex = this.userRightsViewInfo.indexOf(entityViewInfo);
					this.userRightsViewInfo.splice(deletedEntitiViewInfoIndex, 1);
					this.selectionChanged.emit(this.prepareUsersRightsModels());
				}
			});
		});
	}

	private userRightsExists(item: GroupModel | OrganizationTreeNodeModel): boolean {
		let exists: boolean = false;
		this.userRightsViewInfo.forEach((entityViewInfo: UserRightsViewInfo) => {
			if (entityViewInfo.entity.id === Number(item.id)) {
				exists = true;
			}
		});
		return exists;
	}

	public onUserRightsChanged(): void {
		this.selectionChanged.emit(this.prepareUsersRightsModels());
	}

	private prepareUsersRightsModels(): CalendarUserRightsModel[] {
		let usersRights: CalendarUserRightsModel[] = [];
		this.userRightsViewInfo.forEach((item: UserRightsViewInfo) => {
			let userRights: CalendarUserRightsModel = new CalendarUserRightsModel();
			userRights.id = item.id;
			userRights.user = item.entity;
			
			if (ObjectUtils.isNullOrUndefined(this.calendarId)) {
				userRights.calendarId = null;
			} else {
				userRights.calendarId = this.calendarId;
			}
			item.rights.forEach((right: string) => {
				if (right === CalendarUserRightsSelectorComponent.USER_RIGHT_VIEW_VALUE) {
					userRights.view = true;
				} else if (right === CalendarUserRightsSelectorComponent.USER_RIGHT_ADD_VALUE) {
					userRights.add = true;
				} else if (right === CalendarUserRightsSelectorComponent.USER_RIGHT_EDIT_VALUE) {
					userRights.edit = true;
				} else if (right === CalendarUserRightsSelectorComponent.USER_RIGHT_DELETE_VALUE) {
					userRights.delete = true;
				}
			});
			usersRights.push(userRights);
		});
		return usersRights;
	}
}

interface UserRightsViewInfo {
	id: number;
	label: string;
	icon: string;
	entity: OrganizationEntityModel;
	rights: string[];
}