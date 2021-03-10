import { Component, OnInit, ViewChild, Input, Output, EventEmitter } from "@angular/core";
import { 
	AppError, 
	MessageDisplayer, 
	TranslateUtils,
	ObjectUtils,
	ArrayUtils,
	BooleanUtils,
	ConfirmationUtils,
	ReprezentantiComisieSauGLModel,
	ComisieSauGLService,
	ReprezentantiComisieSauGLEditBundleModel,
	BaseWindow
} from "@app/shared";
import { ReprezentantiComisieSauGLGeneralTabContentComponent } from "./reprezentanti-comisie-sau-gl-general-tab-content";
import { ReprezentantiComisieSauGLMembriTabContentComponent } from "./reprezentanti-comisie-sau-gl-membri-tab-content";
import { Message } from "@app/shared";
import { ReprezentantiComisieSauGLTabContent, ReprezentantiComisieSauGLTabContentInputData } from "./reprezentanti-comisie-sau-gl-tab-content";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-reprezentanti-comisie-sau-gl-window",
	templateUrl: "./reprezentanti-comisie-sau-gl-window.component.html"
})
export class ReprezentantiComisieSauGLWindowComponent extends BaseWindow implements OnInit {

	private static readonly GENERAL_TAB_INDEX: number = 0;
	private static readonly MEMBERS_TAB_INDEX: number = 1;
	
	@Input()
	public mode: "edit" | "view";

	@Input()
	public inputData: ReprezentantiComisieSauGLWindowInputData;
	
	@Output()
	private windowClosed: EventEmitter<void>;

	@ViewChild(ReprezentantiComisieSauGLGeneralTabContentComponent)
	private generalTabContent: ReprezentantiComisieSauGLGeneralTabContentComponent;

	@ViewChild(ReprezentantiComisieSauGLMembriTabContentComponent)
	private membriTabContent: ReprezentantiComisieSauGLMembriTabContentComponent;

	private reprezentantiComisieSauGLService: ComisieSauGLService;

	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private confirmationUtils: ConfirmationUtils;

	public windowVisible: boolean = false;

	public tabContentVisible: boolean = false;
	public tabContentReadonly: boolean = false;
	public tabContentInputData: ReprezentantiComisieSauGLTabContentInputData;

	public saveActionEnabled: boolean;
	public readonly: boolean;

	public messagesWindowVisible: boolean = false;
	public messagesWindowMessages: Message[];

	public activeTabIndex: number;

	public constructor(reprezentantiComisieSauGLService: ComisieSauGLService, messageDisplayer: MessageDisplayer, 
			translateUtils: TranslateUtils, confirmationUtils: ConfirmationUtils) {
		super();
		this.reprezentantiComisieSauGLService = reprezentantiComisieSauGLService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		this.confirmationUtils = confirmationUtils;
		this.windowClosed = new EventEmitter<void>();
		this.activeTabIndex = ReprezentantiComisieSauGLWindowComponent.GENERAL_TAB_INDEX;
	}

	public ngOnInit(): void {
		this.init();
		if (ObjectUtils.isNullOrUndefined(this.mode)) {
			this.mode = "view";
		}
		this.saveActionEnabled = this.isEdit();
	}

	private init(): void {
		this.prepareForEdit();
	}

	private hideWindow(): void {
		this.unlock();
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	private openWindow(): void {
		this.unlock();
		this.windowVisible = true;
	}

	public onTabChanged(event: any): void {
		this.activeTabIndex = event.index;
	}

	private prepareForEdit(): void {
		this.lock();	
		this.reprezentantiComisieSauGLService.getReprezentantiEditBundleByComisieSauGLId(this.inputData.comisiiSauGLId, {
			onSuccess: (bundle: ReprezentantiComisieSauGLEditBundleModel): void => {
				this.tabContentInputData = new ReprezentantiComisieSauGLTabContentInputData();
				this.tabContentInputData.comisieSauGLId = this.inputData.comisiiSauGLId;
				this.tabContentInputData.comisieSauGL = bundle.comisieSauGL;
				this.tabContentInputData.comisieSauGLNomenclatorId = this.inputData.comisiiSauGLNomenclatorId;
				this.tabContentInputData.persoaneNomenclatorId = bundle.persoaneNomenclatorId;	
				this.tabContentInputData.membriCDNomenclatorId = bundle.membriCDNomenclatorId;
				this.tabContentInputData.institutiiNomenclatorId = bundle.institutiiNomenclatorId;
				this.tabContentInputData.reprezentantiModel = bundle.reprezentanti;
				this.tabContentInputData.nrAniValabilitateMandatPresedinteVicepresedinte = bundle.nrAniValabilitateMandatPresedinteVicepresedinte;
				this.tabContentInputData.nrAniValabilitateMandatMembruCdCoordonator = bundle.nrAniValabilitateMandatMembruCdCoordonator;
				this.tabContentInputData.isCategorieComisie = bundle.isCategorieComisie;
				this.tabContentReadonly = this.isView();
				this.tabContentVisible = true;
				this.openWindow();
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
				this.hideWindow();
			}
		});
	}

	private isEdit(): boolean {
		return this.mode === "edit";
	}

	private isView(): boolean {
		return this.mode === "view";
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	private prepareModel(): ReprezentantiComisieSauGLModel {
		let model: ReprezentantiComisieSauGLModel = new ReprezentantiComisieSauGLModel();
		this.generalTabContent.populateModel(model);
		this.membriTabContent.populateModel(model);
		return model;
	}

	private isValid(): boolean {
		let isValid: boolean = true;
		if (!this.membriTabContent.isValid()) {
			isValid = false;
			this.activeTabIndex = ReprezentantiComisieSauGLWindowComponent.MEMBERS_TAB_INDEX;
		}
		if (!this.generalTabContent.isValid()) {
			isValid = false;
			this.activeTabIndex = ReprezentantiComisieSauGLWindowComponent.GENERAL_TAB_INDEX;
		}
		return isValid;
	}

	public onSaveAction(event: any): void {		
		if (!this.isValid()) {
			this.displayValidationMessages();
			return;
		}		
		let model: ReprezentantiComisieSauGLModel = this.prepareModel();
		this.saveReprezentanti(model);
	}

	private saveReprezentanti(reprezentantiComisieSauGL: ReprezentantiComisieSauGLModel): void {		
		this.lock();
		this.reprezentantiComisieSauGLService.saveReprezentanti(reprezentantiComisieSauGL, {
			onSuccess: (): void => {
				this.unlock();
				this.messageDisplayer.displaySuccess("SAVED_SUCCESSFULLY");				
				this.windowClosed.emit();
				this.hideWindow();
			},
			onFailure: (error: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	public onCloseAction(event: any): void {
		this.hideWindow();
	}

	private displayValidationMessages(): void {
		this.messagesWindowMessages = [];
		this.addMessagesFromTabContent(this.generalTabContent);
		this.addMessagesFromTabContent(this.membriTabContent);
		this.messagesWindowVisible = true;
	}

	private addMessagesFromTabContent(reprezentantiComisieSauGLTabContent: ReprezentantiComisieSauGLTabContent) {
		if (ObjectUtils.isNullOrUndefined(reprezentantiComisieSauGLTabContent)) {
			return;
		}
		let tabMessages: Message[] = reprezentantiComisieSauGLTabContent.getMessages();
		if (ArrayUtils.isEmpty(tabMessages)) {
			return;
		}
		tabMessages.forEach((tabMessage: Message) => {
			this.messagesWindowMessages.push(tabMessage);
		});
	}

	public onMessagesWindowClosed(event: any) {
		this.messagesWindowVisible = false;
	}
}

export class ReprezentantiComisieSauGLWindowInputData {
	public comisiiSauGLId: number;
	public comisiiSauGLNomenclatorId: number;
	public comisiiSauGlName: string;
}