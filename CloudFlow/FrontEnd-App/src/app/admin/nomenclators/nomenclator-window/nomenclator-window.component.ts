import { Component, Input, Output, EventEmitter, ViewChild, OnInit } from "@angular/core";
import { NomenclatorModel, Message, NomenclatorService, AppError, MessageDisplayer, ObjectUtils, ArrayUtils, BaseWindow } from "@app/shared";
import { NomenclatorGeneralTabContentComponent } from "./nomenclator-general-tab-content/nomenclator-general-tab-content.component";
import { NomenclatorAttributesTabContentComponent } from "./nomenclator-attributes-tab-content/nomenclator-attributes-tab-content.component";
import { NomenclatorTabContent } from "./nomenclator-tab-content";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-nomenclator-window",
	templateUrl: "./nomenclator-window.component.html"
})
export class NomenclatorWindowComponent extends BaseWindow implements OnInit {

	private static readonly GENERAL_TAB_INDEX: number = 0;
	private static readonly ATTRIBUTES_TAB_INDEX: number = 1;

	@Input()
	public mode: "add" | "edit";
	
	@Input()
	public nomenclatorId: number;

	@Output()
	private windowClosed: EventEmitter<void>;

	@ViewChild(NomenclatorGeneralTabContentComponent)
	private generalTabContent: NomenclatorGeneralTabContentComponent;

	@ViewChild(NomenclatorAttributesTabContentComponent)
	private attributesTabContent: NomenclatorAttributesTabContentComponent;

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;

	public tabContentVisible: boolean = false;
	public tabContentMode: string;
	public tabContentInputData: NomenclatorModel = new NomenclatorModel();

	public windowVisible: boolean = false;
	public addButtonVisible: boolean;
	public saveButtonVisible: boolean;
	
	public activeTabIndex: number;

	public messagesWindowVisible: boolean = false;
	public messagesWindowMessages: Message[];

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer) {
		super();
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.activeTabIndex = NomenclatorWindowComponent.GENERAL_TAB_INDEX;
		this.windowClosed = new EventEmitter<void>();
		this.lock();
	}

	public ngOnInit(): void {
		this.init();
	}

	private init(): void {
		this.prepareByMode();
	}

	private prepareByMode(): void {
		this.tabContentMode = this.mode;
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isEdit()) {
			this.prepareForEdit();
		}
	}

	private isAdd(): boolean {
		return this.mode === "add";
	}

	private isEdit(): boolean {
		return this.mode === "edit";
	}

	private prepareForAdd(): void {
		this.addButtonVisible = true;
		this.saveButtonVisible = false;
		this.tabContentVisible = true;
		this.openWindow();
	}

	private prepareForEdit(): void {
		this.nomenclatorService.getNomenclator(this.nomenclatorId, {
			onSuccess: (nomenclator: NomenclatorModel) => {
				this.changePerspectiveForEdit(nomenclator);
				this.openWindow();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
				this.closeWindow();
			}
		});
	}

	private changePerspectiveForEdit(nomenclator: NomenclatorModel): void {
		this.tabContentInputData = nomenclator;
		this.addButtonVisible = false;
		this.saveButtonVisible = true;
		this.tabContentVisible = true;
	}

	private openWindow(): void {
		this.unlock();
		this.windowVisible = true;
	}

	private closeWindow(): void {
		this.unlock();
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	public onTabChanged(event: any): void {
		this.activeTabIndex = event.index;
	}
	
	public onHide(event: any): void {
		this.closeWindow();
	}

	public onCancel(event: any): void {
		this.closeWindow();
	}

	public onSave(event: any): void {
		if (!this.isValid()) {
			this.displayValidationMessages();
			return;
		}
		let nomenclator: NomenclatorModel = this.prepareNomenclator();
		this.saveNomenclator(nomenclator);
	}

	private prepareNomenclator(): NomenclatorModel {
		let nomenclator: NomenclatorModel = new NomenclatorModel();
		this.generalTabContent.populateNomenclator(nomenclator);
		this.attributesTabContent.populateNomenclator(nomenclator);
		return nomenclator;
	}

	private saveNomenclator(nomenclator: NomenclatorModel): void {
		this.nomenclatorService.saveNomenclator(nomenclator, {
			onSuccess: () => {
				this.messageDisplayer.displaySuccess("NOMENCLATOR_SAVED_SUCCESSFULLY");
				this.closeWindow();
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private isValid(): boolean {		
		let isValid: boolean = true;
		if (!this.attributesTabContent.isValid()) {
			isValid = false;
			this.activeTabIndex = NomenclatorWindowComponent.ATTRIBUTES_TAB_INDEX;
		}
		if (!this.generalTabContent.isValid()) {
			isValid = false;
			this.activeTabIndex = NomenclatorWindowComponent.GENERAL_TAB_INDEX;
		}
		return isValid;
	}

	private displayValidationMessages(): void {
		this.messagesWindowMessages = [];
		this.addMessagesFromTabContent(this.generalTabContent);
		this.addMessagesFromTabContent(this.attributesTabContent);
		this.messagesWindowVisible = true;
	}

	private addMessagesFromTabContent(nomenclatorTabContent: NomenclatorTabContent) {
		if (ObjectUtils.isNullOrUndefined(nomenclatorTabContent)) {
			return;
		}
		let tabMessages: Message[] = nomenclatorTabContent.getMessages();
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