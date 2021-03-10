import { Component, ViewChild, OnInit, Output, EventEmitter} from "@angular/core";
import { MessageDisplayer, ObjectUtils, AppError, ConfirmationUtils, ComponentPermissionsWrapper, ClientPermissionEnum, ConfirmationWindowFacade, AdminPermissionEnum, ConfirmationCallback, Message, TranslateUtils, NomenclatorService, ValueOfNomenclatorValueField, NomenclatorConstants } from "@app/shared";
import { RegistruIntrariIesiriService } from "@app/shared/service/registru-intrari-iesiri.service";
import { RegistruIntrariSelectorComponent, RegistruIntrariSelectorInputData } from "./registru-intrari-selector/registru-intrari-selector.component";
import { AuthManager } from "@app/shared/auth";

@Component({
	selector: "app-registru-intrari",
	templateUrl: "./registru-intrari.component.html"
})
export class RegistruIntrariComponent implements OnInit, ComponentPermissionsWrapper {

	private static readonly ADD_PERMISSION: string = ClientPermissionEnum.EDIT_REGISTRU_INTRARI_IESIRI;
	private static readonly EDIT_PERMISSION: string = ClientPermissionEnum.EDIT_REGISTRU_INTRARI_IESIRI;
	private static readonly DELETE_PERMISSION: string = ClientPermissionEnum.EDIT_REGISTRU_INTRARI_IESIRI;

	@Output()
	public inRegisterSelectorRefreshed: EventEmitter<void>;

	@ViewChild(RegistruIntrariSelectorComponent)
	private registruIntrariSelectorComponent: RegistruIntrariSelectorComponent;

	private registruIntrariIesiriService: RegistruIntrariIesiriService;
	private messageDisplayer: MessageDisplayer;
	private authManager: AuthManager;
	private nomenclatorService: NomenclatorService;
	private translateUtils: TranslateUtils;

	public registruIntrariSelectorInputData: RegistruIntrariSelectorInputData;
	public selectedRegistruId: number;
	public nomenclatorIdByCode: Map<string, number>;

	public registruIntrariWindowVisible: boolean;
	public registruIntrariWindowMode: string;
	public registruIntrariId: number;
	public confirmationWindowVisible: boolean;
	
	public addButtonEnabled: boolean = false;
	public editButtonEnabled: boolean = false;
	public viewButtonEnabled: boolean = false;
	public finalizeButtonEnabled: boolean = false;
	public cancelButtonEnabled: boolean = false;
	public refreshButtonEnabled: boolean = false;
	public exportCSVButtonEnabled: boolean = false;

	public anulareRegistruIntrariIesiriWindowVisible: boolean = false;

	public loadingVisible: boolean = false;

	public constructor(registruIntrariIesiriService: RegistruIntrariIesiriService, 
			messageDisplayer: MessageDisplayer, authManager: AuthManager, translateUtils: TranslateUtils, nomenclatorService: NomenclatorService) {
		this.registruIntrariIesiriService = registruIntrariIesiriService;
		this.messageDisplayer = messageDisplayer;
		this.authManager = authManager;
		this.nomenclatorService = nomenclatorService;
		this.translateUtils = translateUtils;
		this.registruIntrariSelectorInputData = new RegistruIntrariSelectorInputData();
		this.confirmationWindowVisible = false;
		this.nomenclatorIdByCode = new Map();
		this.inRegisterSelectorRefreshed = new EventEmitter();
	}

	public ngOnInit(): void {
		this.changePerspective();
		this.prepareNomenclatorValuesForNomenclatorValueSelectors([
			NomenclatorConstants.NOMENCLATOR_CODE_INSTITUTII,
			NomenclatorConstants.NOMENCLATOR_CODE_REGISTRU_INTRARI_TIP_DOCUMENT,
			NomenclatorConstants.NOMENCLATOR_CODE_COMISII_SAU_GL
		]);
	}

	private prepareNomenclatorValuesForNomenclatorValueSelectors(codes: string[]):void {
		this.nomenclatorService.getNomenclatorIdByCodeAsMap(codes, {
			onSuccess: (nomenclatorIdByCode: object): void => {
				if (ObjectUtils.isNotNullOrUndefined(nomenclatorIdByCode)) {
					for ( let [key, value] of Object.entries(nomenclatorIdByCode)){
						this.nomenclatorIdByCode.set(key, value);
					}
				}
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onRegistruIntrariWindowClosed(event: any): void {
		this.registruIntrariWindowVisible = false;
		this.registruIntrariSelectorComponent.refresh();
		this.inRegisterSelectorRefreshed.emit();
	}

	public onAdd(event: any): void {
		this.registruIntrariWindowMode = "add";
		this.registruIntrariId = null;
		this.registruIntrariWindowVisible = true;
	}

	public onEdit(event: any): void {
		this.registruIntrariWindowMode = "edit";
		this.registruIntrariId = this.selectedRegistruId;
		this.registruIntrariWindowVisible = true;
	}

	public onView(event: any): void {
		this.registruIntrariWindowMode = "view";
		this.registruIntrariId = this.selectedRegistruId;
		this.registruIntrariWindowVisible = true;
	}

	public onCancel(event: any): void {
		this.registruIntrariId = this.selectedRegistruId;
		this.anulareRegistruIntrariIesiriWindowVisible = true;
	}

	public onRefresh(event: any): void {
		this.registruIntrariSelectorComponent.refresh();
		this.inRegisterSelectorRefreshed.emit();
	}
	
	public onExportCSV(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.registruIntrariSelectorComponent)) {
			return;
		}
		this.registruIntrariSelectorComponent.exportCSV();
	}

	public onAnulareRegistruIntrariIesiriWindowClosed(): void {
		this.anulareRegistruIntrariIesiriWindowVisible = false;
		this.registruIntrariSelectorComponent.refresh();
		this.inRegisterSelectorRefreshed.emit();
	}

	public registruIntrariSelectionChanged(registruIntrariId: number): void {
		this.selectedRegistruId = registruIntrariId;
		this.changePerspective();
	}

	public reloadRegistruIntrariSelectorData(): void {
		this.registruIntrariSelectorComponent.refresh();
		this.changePerspective();
	}

	public changePerspective(): void {
		
		this.disableAllButtons();
		
		this.addButtonEnabled = this.isAddPermissionAllowed();
		this.refreshButtonEnabled = true;
		this.exportCSVButtonEnabled = true;

		if (ObjectUtils.isNotNullOrUndefined(this.selectedRegistruId)) {
			this.viewButtonEnabled = true;
			if (this.isEditPermissionAllowed()) {
				if (!this.isRegistruIntrariCanceled()) {
					if (!this.isRegistruIntrariFinalized()) {
						this.finalizeButtonEnabled = true;
						this.editButtonEnabled = true;
					}else{
						this.cancelButtonEnabled = true;
						if (this.isElevatedEditPermissionAllowed()) {
							this.editButtonEnabled = true;
						}
					}
				}				
			}
		} else {
			this.selectedRegistruId = null;
			this.registruIntrariId = null;
		}
	}

	private disableAllButtons(): void {
		this.addButtonEnabled = false;
		this.editButtonEnabled = false;
		this.viewButtonEnabled = false;
		this.cancelButtonEnabled = false;
		this.finalizeButtonEnabled = false;
		this.refreshButtonEnabled = false;
		this.exportCSVButtonEnabled = false;
	}

	private isRegistruIntrariCanceled(): boolean {
		if (ObjectUtils.isNotNullOrUndefined(this.registruIntrariSelectorComponent.selectedData)) {
			return this.registruIntrariSelectorComponent.selectedData.anulat;
		}
		return false;
	}
	
	private isRegistruIntrariFinalized(): boolean {
		if (ObjectUtils.isNotNullOrUndefined(this.registruIntrariSelectorComponent.selectedData)) {
			return this.registruIntrariSelectorComponent.selectedData.inchis;	
		}
		return false;		
	}

	public onFinalize(): void {
		this.confirmationWindowVisible = true;
	}
	
	public confirmFinalize(): ConfirmationCallback{
		return {
			approve: (): void => {
				this.confirmationWindowVisible = false;
				this.loadingVisible = true;
				this.registruIntrariIesiriService.finalizeRegistruIntrari(this.selectedRegistruId, {
					onSuccess: (): void => {
						this.messageDisplayer.displaySuccess("REGISTRU_INTRARI_FINALIZED_SUCCESSFULLY");
						this.registruIntrariSelectorComponent.refresh();
						this.inRegisterSelectorRefreshed.emit();
						this.loadingVisible = false;
					},
					onFailure: (appError: AppError) => {
						this.loadingVisible = false;
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			reject: (): void => {
				this.confirmationWindowVisible = false;
			}
		};
	}

	public getConfirmFinalizeMessages() {
		return [new Message("warn", "CONFIRM_FINALIZE_REGISTRU_INTRARI"),
			new Message("info", this.translateUtils.translateLabel("REGISTRU_IESIRI_NR_INREGISTRARE")+": "+this.registruIntrariSelectorComponent.selectedData.numarInregistrare, false)];
	}

	isAddPermissionAllowed(): boolean {
		return this.authManager.hasPermission(RegistruIntrariComponent.ADD_PERMISSION);
	}

	isEditPermissionAllowed(): boolean {
		return this.authManager.hasPermission(RegistruIntrariComponent.EDIT_PERMISSION);
	}
	
	isElevatedEditPermissionAllowed(): boolean {
		return this.authManager.hasPermission(AdminPermissionEnum.EDIT_REGISTRU_INTRARI_IESIRI_ELEVATED);
	}

	isDeletePermissionAllowed(): boolean {
		return this.authManager.hasPermission(RegistruIntrariComponent.DELETE_PERMISSION);
	}	
}