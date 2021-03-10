import { Component, ViewChild, Input, Output, OnChanges, SimpleChanges, EventEmitter, OnInit } from "@angular/core";
import {  ObjectUtils, ArrayUtils, ConfirmationUtils } from "./../../utils";
import {  MessageDisplayer  } from "./../../message-displayer";
import {  AppError, NomenclatorAttributeModel  } from "./../../model";
import { NomenclatorService } from "./../../service";
import { NomenclatorDataWindowInputData, NomenclatorDataWindowEvent } from "./../nomenclator-data-window";
import { ConfirmationWindowFacade } from "./../../components/confirmation-window";

@Component({
	selector: "app-nomenclator-data-toolbar",
	templateUrl: "./nomenclator-data-toolbar.component.html"
})
export class NomenclatorDataToolbarComponent implements OnChanges {

	@Input()
	public inputData: NomenclatorDataToolbarInputData;

	@Input()
	public permissions: NomenclatorDataToolbarPermissions;

	@Input()
	public customTools: NomenclatorDataToolbarCustomTool[];

	@Output()
	public actionPerformed: EventEmitter<NomenclatorDataToolbarActionPerformedType>;

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;

	public addButtonEnabled: boolean = false;
	public editButtonEnabled: boolean = false;
	public deleteButtonEnabled: boolean = false;
	public viewButtonEnabled: boolean = false;
	public refreshButtonEnabled: boolean = false;
	public exportCSVButtonEnabled: boolean = false;
	
	public nomenclatorWindowVisible: boolean;
	public nomenclatorWindowInputData: NomenclatorDataWindowInputData;
	public nomenclatorWindowMode: string;

	public loadingVisible: boolean = false;
	public confirmationWindow: ConfirmationWindowFacade;

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer) {
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationWindow = new ConfirmationWindowFacade(); 
		this.actionPerformed = new EventEmitter();
		this.disableAllButtons();
	}

	public ngOnChanges(changes: SimpleChanges): void {
		this.changePerspective();
	}

	private getDefaultPermissions(): NomenclatorDataToolbarPermissions {
		let p: NomenclatorDataToolbarPermissions = new NomenclatorDataToolbarPermissions();
		p.add = true;
		p.delete = true;
		p.edit = true;
		return p;
	}

	private changePerspective(): void {
		this.disableAllButtons();
		if (ObjectUtils.isNullOrUndefined(this.inputData)) {
			return;
		}
		let permissions: NomenclatorDataToolbarPermissions = this.permissions;
		if (ObjectUtils.isNullOrUndefined(permissions)) {
			permissions = this.getDefaultPermissions();
		} 
		if (ObjectUtils.isNotNullOrUndefined(this.inputData.nomenclatorId)) {
			this.addButtonEnabled = permissions.add;
			this.refreshButtonEnabled = true;
			this.exportCSVButtonEnabled = true;
		}
		if (ObjectUtils.isNotNullOrUndefined(this.inputData.nomenclatorValueId)) {
			this.editButtonEnabled = permissions.edit;
			this.viewButtonEnabled = true;
			this.deleteButtonEnabled = permissions.delete;
		}
	}
	
	public onAddNomenclatorData(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.inputData.nomenclatorId)) {
			throw new Error("nomenclatorId null");
		}
		let windowInputData: NomenclatorDataWindowInputData = new NomenclatorDataWindowInputData();
		windowInputData.nomenclatorId = this.inputData.nomenclatorId;
		this.nomenclatorWindowMode = "add";
		this.nomenclatorWindowInputData = windowInputData;
		this.nomenclatorWindowVisible = true;
	}

	public onEditNomenclatorData(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.inputData.nomenclatorValueId)) {
			throw new Error("nomenclatorValue null");
		}
		let windowInputData: NomenclatorDataWindowInputData = new NomenclatorDataWindowInputData();
		windowInputData.nomenclatorId = this.inputData.nomenclatorId;
		windowInputData.nomenclatorValueId = this.inputData.nomenclatorValueId;
		this.nomenclatorWindowMode = "edit";
		this.nomenclatorWindowInputData = windowInputData;
		this.nomenclatorWindowVisible = true;
	}

	public onDeleteNomenclatorData(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.inputData.nomenclatorValueId)) {
			throw new Error("nomenclatorValue null");
		}
		this.confirmationWindow.confirm({
			approve: (): void => {
				this.confirmationWindow.hide();
				this.loadingVisible = true;
				this.nomenclatorService.deleteNomenclatorValue(this.inputData.nomenclatorValueId, {
					onSuccess: (): void => {
						this.loadingVisible = false;
						this.actionPerformed.emit(NomenclatorDataToolbarActionPerformedType.DATA_CHANGED);
					},
					onFailure: (error: AppError): void => {
						this.loadingVisible = false;
						this.messageDisplayer.displayAppError(error);
					}
				});
			},
			reject: (): void => {
				this.confirmationWindow.hide();
			}
		}, "DELETE_CONFIRM");
	}

	public onViewNomenclatorData(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.inputData.nomenclatorValueId)) {
			throw new Error("nomenclatorValue null");
		}
		let windowInputData: NomenclatorDataWindowInputData = new NomenclatorDataWindowInputData();
		windowInputData.nomenclatorId = this.inputData.nomenclatorId;
		windowInputData.nomenclatorValueId = this.inputData.nomenclatorValueId;
		this.nomenclatorWindowMode = "view";
		this.nomenclatorWindowInputData = windowInputData;
		this.nomenclatorWindowVisible = true;
	}

	public onRefreshNomenclatorData(event: any): void {
		this.actionPerformed.emit(NomenclatorDataToolbarActionPerformedType.REFRESH);
	}

	public onExportCSVNomenclatorData(event: any): void {
		this.actionPerformed.emit(NomenclatorDataToolbarActionPerformedType.EXPORT_CSV);
	}

	public onNomenclatorWindowClosed(event: NomenclatorDataWindowEvent): void {
		this.nomenclatorWindowVisible = false;
		if (event.dataChanged) {
			this.actionPerformed.emit(NomenclatorDataToolbarActionPerformedType.DATA_CHANGED);
		}
	}

	private disableAllButtons(): void {
		this.addButtonEnabled = false;
		this.editButtonEnabled = false;
		this.viewButtonEnabled = false;
		this.deleteButtonEnabled = false;
		this.refreshButtonEnabled = false;
		this.exportCSVButtonEnabled = false;
	}
}

export class NomenclatorDataToolbarInputData {
	public nomenclatorId: number;
	public nomenclatorValueId: number;
}

export class NomenclatorDataToolbarPermissions {
	public add: boolean;
	public edit: boolean;
	public delete: boolean;
}

export enum NomenclatorDataToolbarActionPerformedType {
	DATA_CHANGED = "DATA_CHANGED",
	REFRESH = "REFRESH",
	EXPORT_CSV = "EXPORT_CSV"
}

export interface NomenclatorDataToolbarCustomTool {
	labelCode: string;
	enabled: boolean;
	handle(): void;
}