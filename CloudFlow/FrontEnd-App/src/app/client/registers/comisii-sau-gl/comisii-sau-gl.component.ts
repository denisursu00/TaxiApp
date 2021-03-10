import { Component, OnInit, ViewChild } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { ReprezentantiComisieSauGLWindowInputData } from "./reprezentanti-comisie-sau-gl-window";
import { ComisieSauGLService, MessageDisplayer, MimeTypeSelectorComponent, NomenclatorConstants, NomenclatorDataToolbarPermissions, ComponentPermissionsWrapper, ConfirmationWindowFacade, ClientPermissionEnum, NomenclatorSortedAttribute } from "@app/shared";
import { ObjectUtils, ArrayUtils, NomenclatorService, AppError, ConfirmationUtils } from "@app/shared";
import { NomenclatorDataTableComponent, NomenclatorDataToolbarInputData, NomenclatorDataToolbarCustomTool, NomenclatorAttributeModel, NomenclatorDataToolbarActionPerformedType } from "@app/shared";
import { AuthManager } from "@app/shared/auth";


@Component({
	selector: "app-comisii-sau-gl",
	templateUrl: "./comisii-sau-gl.component.html"
})
export class ComisiiSauGLComponent implements OnInit, ComponentPermissionsWrapper {

	private static readonly ADD_PERMISSION: string = ClientPermissionEnum.EDIT_COMISII_GL;
	private static readonly EDIT_PERMISSION: string = ClientPermissionEnum.EDIT_COMISII_GL;
	private static readonly DELETE_PERMISSION: string = ClientPermissionEnum.EDIT_COMISII_GL;

	@ViewChild(NomenclatorDataTableComponent)
	public nomenclatorDataTableComponent: NomenclatorDataTableComponent;

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	private authManager: AuthManager;

	public nomenclatorDataTableNomenclatorId: number;
	public nomenclatorDataTableMode: string;
	public nomenclatorDataTableVisible: boolean;

	public toolbarInputData: NomenclatorDataToolbarInputData;
	public toolbarPermissions: NomenclatorDataToolbarPermissions;
	public toolbarCustomTools: NomenclatorDataToolbarCustomTool[];

	private reprezentantiCustomTool: NomenclatorDataToolbarCustomTool;

	public selectedNomenclatorId: number;
	public selectedNomenclatorValue: object;

	public reprezentantiWindowVisible: boolean;
	public reprezentantiWindowMode: "edit" | "view";
	public reprezentantiWindowInputData: ReprezentantiComisieSauGLWindowInputData; 

	public customSortedAttributes: NomenclatorSortedAttribute[] = [];

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer,
			authManager: AuthManager) {
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.authManager = authManager;

		this.toolbarCustomTools = [];
		this.reprezentantiCustomTool = {
			labelCode: "REPREZENTANTI_COMISIE_SAU_GL",
			enabled: false,
			handle: (): void => {
				this.reprezentantiWindowInputData = new ReprezentantiComisieSauGLWindowInputData();
				this.reprezentantiWindowInputData.comisiiSauGLNomenclatorId = this.selectedNomenclatorId;
				this.reprezentantiWindowInputData.comisiiSauGLId = this.selectedNomenclatorValue["id"];
				this.reprezentantiWindowInputData.comisiiSauGlName = this.selectedNomenclatorValue[NomenclatorConstants.COMISII_SAU_GL_ATTR_KEY_DENUMIRE];
				this.reprezentantiWindowMode = this.isEditPermissionAllowed() ? "edit" : "view";
				this.reprezentantiWindowVisible = true;

			}
		};
		this.toolbarCustomTools.push(this.reprezentantiCustomTool);
	}

	public ngOnInit(): void {
		this.setToolbarPermissions();
		this.nomenclatorService.getNomenclatorIdByCodeAsMap([NomenclatorConstants.NOMENCLATOR_CODE_COMISII_SAU_GL], {
			onSuccess: (nomenclatorIdByCode: object): void => {
				if (ObjectUtils.isNotNullOrUndefined(nomenclatorIdByCode)) {
					let comisiiGLNomenclatorId: number = nomenclatorIdByCode[NomenclatorConstants.NOMENCLATOR_CODE_COMISII_SAU_GL];
					if (ObjectUtils.isNotNullOrUndefined(comisiiGLNomenclatorId)) {
						this.nomenclatorDataTableNomenclatorId = comisiiGLNomenclatorId;		
						this.selectedNomenclatorId = comisiiGLNomenclatorId;
						this.changeToolbarInputData();				
						this.nomenclatorDataTableVisible = true;

						let sortedAttribute: NomenclatorSortedAttribute = new NomenclatorSortedAttribute();
						sortedAttribute.attributeKey = NomenclatorConstants.COMISII_SAU_GL_ATTR_KEY_DENUMIRE;
						sortedAttribute.type = "ASC";

						this.customSortedAttributes.push(sortedAttribute);
					}					
				}
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private setToolbarPermissions(): void {
		this.toolbarPermissions = new NomenclatorDataToolbarPermissions();
		this.toolbarPermissions.add = this.isAddPermissionAllowed();
		this.toolbarPermissions.edit = this.isEditPermissionAllowed();
		this.toolbarPermissions.delete = this.isDeletePermissionAllowed();
	}

	public onNomenclatorSelected(nomenclatorId: number): void {
		this.nomenclatorDataTableNomenclatorId = nomenclatorId;
		this.selectedNomenclatorId = nomenclatorId;
		this.nomenclatorDataTableMode = "single";
		this.nomenclatorDataTableVisible = true;
		this.changeToolbarInputData();
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
		
		this.reprezentantiCustomTool.enabled = ObjectUtils.isNotNullOrUndefined(this.selectedNomenclatorValue);
	}

	public onReprezentantiWindowClosed(event: any): void {
		this.reprezentantiWindowVisible = false;
	}

	isAddPermissionAllowed(): boolean {
		return this.authManager.hasPermission(ComisiiSauGLComponent.ADD_PERMISSION);
	}

	isEditPermissionAllowed(): boolean {
		return this.authManager.hasPermission(ComisiiSauGLComponent.EDIT_PERMISSION);
	}

	isDeletePermissionAllowed(): boolean {
		return this.authManager.hasPermission(ComisiiSauGLComponent.DELETE_PERMISSION);
	}
}