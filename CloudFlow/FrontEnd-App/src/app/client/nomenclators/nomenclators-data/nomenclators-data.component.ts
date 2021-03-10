import { Component, ViewChild } from "@angular/core";
import { ObjectUtils, ArrayUtils, NomenclatorService, AppError, MessageDisplayer, ConfirmationUtils, NomenclatorModel, NomenclatorDataToolbarPermissions, ClientPermissionEnum, ComponentPermissionsWrapper } from "@app/shared";
import { NomenclatorDataTableComponent, NomenclatorDataToolbarInputData, NomenclatorAttributeModel, NomenclatorDataToolbarActionPerformedType } from "@app/shared";
import { DocumentSelectionWindowInputData } from "@app/client/common";
import { OnInit } from "@angular/core";
import { AuthManager } from "@app/shared/auth";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-nomenclators-data",
	templateUrl: "./nomenclators-data.component.html"
})
export class NomenclatorsDataComponent implements OnInit, ComponentPermissionsWrapper {

	private static readonly ADD_PERMISSION: string = ClientPermissionEnum.EDIT_NOMENCLATORS;
	private static readonly EDIT_PERMISSION: string = ClientPermissionEnum.EDIT_NOMENCLATORS;
	private static readonly DELETE_PERMISSION: string = ClientPermissionEnum.EDIT_NOMENCLATORS;

	@ViewChild(NomenclatorDataTableComponent)
	public nomenclatorDataTableComponent: NomenclatorDataTableComponent;

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	private authManager: AuthManager;

	public nomenclators: NomenclatorModel[];
	public selectedNomenclator: NomenclatorModel;
	public nomenclatorsLoading: boolean = true;

	public nomenclatorDataTableNomenclatorId: number;
	public nomenclatorDataTableMode: string;
	public nomenclatorDataTableVisible: boolean;

	public toolbarInputData: NomenclatorDataToolbarInputData;
	public toolbarPermissions: NomenclatorDataToolbarPermissions;

	public selectedNomenclatorId: number;
	public selectedNomenclatorValue: object;

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer,
			authManager: AuthManager) {
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.authManager = authManager;
	}

	public ngOnInit(): void {
		this.setToolbarPermissions();
		this.loadNomenclators();
	}

	private setToolbarPermissions(): void {
		this.toolbarPermissions = new NomenclatorDataToolbarPermissions();
		this.toolbarPermissions.add = this.isAddPermissionAllowed();
		this.toolbarPermissions.edit = this.isEditPermissionAllowed();
		this.toolbarPermissions.delete = this.isDeletePermissionAllowed();
	}

	private loadNomenclators(): void {
		this.nomenclatorService.getAvailableNomenclatorsForProcessingValuesFromUI({
			onSuccess: (nomenclators: NomenclatorModel[]) => {
				this.nomenclators = nomenclators;
				this.nomenclatorsLoading = false;

				ListItemUtils.sort(this.nomenclators, "name");
			},
			onFailure: (appError: AppError) => {
				this.nomenclatorsLoading = false;
				this.messageDisplayer.displayAppError(appError);
			}
		});	
	}

	public onNomenclatorSelected(event: any): void {
		this.nomenclatorDataTableMode = "single";
		this.selectedNomenclatorValue = null;
		if (ObjectUtils.isNotNullOrUndefined(this.selectedNomenclator)) {
			this.nomenclatorDataTableNomenclatorId = this.selectedNomenclator.id;		
			this.nomenclatorDataTableVisible = true;
			this.selectedNomenclatorId = this.selectedNomenclator.id;	
			this.changeToolbarInputData();
		} else {
			this.nomenclatorDataTableNomenclatorId = null;
			this.nomenclatorDataTableVisible = false;
			this.selectedNomenclatorId = null;			
			this.changeToolbarInputData();
		}
	}

	public onToolbarActionPerformed(type: NomenclatorDataToolbarActionPerformedType): void {
		if (ObjectUtils.isNullOrUndefined(type)) {
			throw new Error("invalid action performed type");
		}
		if (type === NomenclatorDataToolbarActionPerformedType.DATA_CHANGED || type === NomenclatorDataToolbarActionPerformedType.REFRESH) {
			this.refreshData();
		} else if (type === NomenclatorDataToolbarActionPerformedType.EXPORT_CSV) {
			if (ObjectUtils.isNullOrUndefined(this.nomenclatorDataTableComponent)) {
				return;
			}
			this.nomenclatorDataTableComponent.exportCSV();
		}
		this.nomenclatorDataTableComponent.refresh();
	}

	private refreshData(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.nomenclatorDataTableComponent)) {
			this.nomenclatorDataTableComponent.refresh();
		}
	}

	public onNomenclatorDataTableDataSelected(nomenclatorAttributeModelOrModels: object[] | object): void {
		let newSelectedNomenclatorValueId: number = null;
		if (ObjectUtils.isArray(nomenclatorAttributeModelOrModels)) {
			let selectedData: object[] = <object[]> nomenclatorAttributeModelOrModels;
			if (ArrayUtils.isNotEmpty(selectedData) && selectedData.length === 1) {
				this.selectedNomenclatorValue = selectedData[0];
			}
		} else {
			this.selectedNomenclatorValue = <object> nomenclatorAttributeModelOrModels;
		}
		this.changeToolbarInputData();
	}

	private changeToolbarInputData(): void {
		let newToolbarInputData: NomenclatorDataToolbarInputData = new NomenclatorDataToolbarInputData();
		newToolbarInputData.nomenclatorId = this.selectedNomenclatorId;
		if (ObjectUtils.isNotNullOrUndefined(this.selectedNomenclatorValue)) {
			newToolbarInputData.nomenclatorValueId = this.selectedNomenclatorValue["id"];
		}
		this.toolbarInputData = newToolbarInputData;
	}

	isAddPermissionAllowed(): boolean {
		return this.authManager.hasPermission(NomenclatorsDataComponent.ADD_PERMISSION);
	}

	isEditPermissionAllowed(): boolean {
		return this.authManager.hasPermission(NomenclatorsDataComponent.EDIT_PERMISSION);
	}

	isDeletePermissionAllowed(): boolean {
		return this.authManager.hasPermission(NomenclatorsDataComponent.DELETE_PERMISSION);
	}
}