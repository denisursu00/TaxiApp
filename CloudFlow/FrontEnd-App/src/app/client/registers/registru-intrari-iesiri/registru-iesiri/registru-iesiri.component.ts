import { Component, ViewChild, OnInit, Output, EventEmitter } from "@angular/core";
import { RegistruIesiriSelectorComponent, RegistruIesiriSelectorInputData} from "./registru-iesiri-selector/registru-iesiri-selector.component";
import { ObjectUtils, RegistruIntrariIesiriService, AppError, MessageDisplayer, ConfirmationUtils, Message, ArrayUtils, ComponentPermissionsWrapper, ClientPermissionEnum, ConfirmationWindowFacade, AdminPermissionEnum, ConfirmationCallback, TranslateUtils, NomenclatorConstants, NomenclatorService } from "@app/shared";
import { RegistruIesiriDestinatarViewModel } from "@app/shared/model/registru-intrari-iesiri/registru-iesiri-destinatari-view.model";
import { AuthManager } from "@app/shared/auth";

@Component({
	selector: "app-registru-iesiri",
	templateUrl: "./registru-iesiri.component.html"
})
export class RegistruIesiriComponent implements OnInit,ComponentPermissionsWrapper {

	private static readonly ADD_PERMISSION: string = ClientPermissionEnum.EDIT_REGISTRU_INTRARI_IESIRI;
	private static readonly EDIT_PERMISSION: string = ClientPermissionEnum.EDIT_REGISTRU_INTRARI_IESIRI;
	private static readonly DELETE_PERMISSION: string = ClientPermissionEnum.EDIT_REGISTRU_INTRARI_IESIRI;

	@Output()
	public outRegisterSelectorRefreshed: EventEmitter<void>;

	@ViewChild(RegistruIesiriSelectorComponent)
	private registruIesiriSelector: RegistruIesiriSelectorComponent;
	
	private registruIntrariIesiriService: RegistruIntrariIesiriService;
	private messageDisplayer: MessageDisplayer;
	private authManager: AuthManager;
	private nomenclatorService: NomenclatorService;
	private translateUtils: TranslateUtils;

	public registruIesiriWindowVisible: boolean = false;
	public registruIesiriId: number;
	public nomenclatorIdByCode: Map<string, number>;
	public registruIesiriMode: "add" | "edit" | "view";

	public anulareRegistruIntrariIesiriWindowVisible: boolean;

	public addButtonEnabled: boolean = false;
	public editButtonEnabled: boolean = false;
	public viewButtonEnabled: boolean = false;
	public finalizeButtonEnabled: boolean = false;
	public cancelButtonEnabled: boolean = false;
	public refreshButtonEnabled: boolean = false;
	public exportCSVButtonEnabled: boolean = false;
	
	public registruIesiriSelectorInputData: RegistruIesiriSelectorInputData;
	
	public confirmationWindowVisible: boolean;

	public loadingVisible: boolean = false;

	public constructor(registruIntrariIesiriService: RegistruIntrariIesiriService, 
			messageDisplayer: MessageDisplayer, authManager: AuthManager, translateUtils: TranslateUtils, nomenclatorService: NomenclatorService) {
		this.registruIntrariIesiriService = registruIntrariIesiriService;
		this.messageDisplayer = messageDisplayer;
		this.registruIesiriSelectorInputData = new RegistruIesiriSelectorInputData();
		this.authManager = authManager;
		this.nomenclatorService = nomenclatorService;
		this.translateUtils = translateUtils;
		this.confirmationWindowVisible = false;
		this.nomenclatorIdByCode = new Map();
		this.outRegisterSelectorRefreshed = new EventEmitter();
	}
	ngOnInit(): void {
		this.changePerspective();
		this.prepareNomenclatorValuesForNomenclatorValueSelectors();
	}

	public onAdd(): void {
		this.registruIesiriMode = "add";
		this.registruIesiriId = null;
		this.registruIesiriWindowVisible = true;
	}

	public onEdit(): void {
		this.registruIesiriMode = "edit";
		this.registruIesiriWindowVisible = true;
	}

	public onView(): void {
		this.registruIesiriMode = "view";
		this.registruIesiriWindowVisible = true;
	}

	public onCancel(): void {
		this.anulareRegistruIntrariIesiriWindowVisible = true;
	}

	public onRefresh(): void {
		this.reloadRegistruIesiriSelectorData();
		this.outRegisterSelectorRefreshed.emit();
	}

	public onExportCSV(): void {
		if (ObjectUtils.isNullOrUndefined(this.registruIesiriSelector)) {
			return;
		}
		this.registruIesiriSelector.exportCSV();
	}

	public changePerspective(): void {
		this.disableAllButtons();
		this.addButtonEnabled = this.isAddPermissionAllowed();
		if (ObjectUtils.isNotNullOrUndefined(this.registruIesiriSelector.selectedData)) {
			this.viewButtonEnabled = true;
			if (this.isEditPermissionAllowed()) {
				if (!this.isRegistruIesiriCanceled()) {
					if (!this.isRegistruIesiriFinalized()) {
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
		}
		this.refreshButtonEnabled = true;
		this.exportCSVButtonEnabled = true;
	}

	private prepareNomenclatorValuesForNomenclatorValueSelectors(): void {
		this.nomenclatorService.getNomenclatorIdByCodeAsMap([NomenclatorConstants.NOMENCLATOR_CODE_REGISTRU_IESIRI_TIP_DOCUMENT], {
			onSuccess: (nomenclatorIdByCode: object): void => {
				if (nomenclatorIdByCode){
					for( let [key, value] of Object.entries(nomenclatorIdByCode)){
						this.nomenclatorIdByCode.set(key, value);
					}
				}
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
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

	private isRegistruIesiriCanceled(): boolean {
		if (ObjectUtils.isNotNullOrUndefined(this.registruIesiriSelector.selectedData)) {
			return this.registruIesiriSelector.selectedData.anulat;
		}
		return false;
	}

	private isRegistruIesiriFinalized(): boolean {
		if (ObjectUtils.isNotNullOrUndefined(this.registruIesiriSelector.selectedData)) {
			return this.registruIesiriSelector.selectedData.finalizat;	
		}
		return false;
	}

	public registruIesiriSelectionChanged(registruIesiriId: number): void {
		this.registruIesiriId = registruIesiriId;
		this.changePerspective();
	}

	public onRegistruIesiriWindowClosed(): void {
		this.registruIesiriWindowVisible = false;
		this.reloadRegistruIesiriSelectorData();
		this.outRegisterSelectorRefreshed.emit();
	}

	public reloadRegistruIesiriSelectorData(): void {
		this.registruIesiriSelector.refresh();
		this.changePerspective();
	}

	public onAnulareRegistruIntrariIesiriWindowClosed(motivAnulare: string): void {
		this.anulareRegistruIntrariIesiriWindowVisible = false;
		this.reloadRegistruIesiriSelectorData();
		this.outRegisterSelectorRefreshed.emit();
	}

	public onFinalize(): void {
		this.confirmationWindowVisible = true;
	}

	public confirmFinalize(): ConfirmationCallback{
		return {
			approve: (): void => {
				this.confirmationWindowVisible = false;
				this.loadingVisible = true;
				this.registruIntrariIesiriService.finalizeRegistruIesiri(this.registruIesiriSelector.selectedData.id, {
					onSuccess: (): void => {
						this.messageDisplayer.displaySuccess("REGISTRU_IESIRI_FINALIZED_SUCCESSFULLY");
						this.onRefresh();
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
		return [new Message("warn", "CONFIRM_FINALIZE_REGISTRU_IESIRI"),
			new Message("info", this.translateUtils.translateLabel("REGISTRU_IESIRI_NR_INREGISTRARE")+": "+this.registruIesiriSelector.selectedData.numarInregistrare, false)];
	}

	isAddPermissionAllowed(): boolean {
		return this.authManager.hasPermission(RegistruIesiriComponent.ADD_PERMISSION);
	}

	isEditPermissionAllowed(): boolean {
		return this.authManager.hasPermission(RegistruIesiriComponent.EDIT_PERMISSION);
	}

	isElevatedEditPermissionAllowed(): boolean {
		return this.authManager.hasPermission(AdminPermissionEnum.EDIT_REGISTRU_INTRARI_IESIRI_ELEVATED);
	}

	isDeletePermissionAllowed(): boolean {
		return this.authManager.hasPermission(RegistruIesiriComponent.DELETE_PERMISSION);
	}
}