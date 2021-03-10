import { Component, ViewChild, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { MetadataDefinitionModel, ArrayUtils, ObjectUtils, ConfirmationUtils, UiUtils, BaseWindow } from "@app/shared";
import { MetadataTypeConstants } from "@app/shared";
import { MetadataDefinitionInputData } from "./../metadata-definitions-manager/metadata-definition/metadata-definition-input-data";
import { MetadataDefinitionsManagerInputData } from "./../metadata-definitions-manager/metadata-definitions-manager-input-data";
import { MetadataDefinitionsManagerComponent } from "./../metadata-definitions-manager/metadata-definitions-manager.component";
import { MetadataDefinitionsOfMetadataCollectionWindowInputData } from "./metadata-definitions-of-metadata-collection-window-input-data";
import { Dialog } from "primeng/primeng";

@Component({
	selector: "app-metadata-definitions-of-metadata-collection-window",
	templateUrl: "./metadata-definitions-of-metadata-collection-window.component.html"
})
export class MetadataDefinitionsOfMetadataCollectionWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public inputData: MetadataDefinitionsOfMetadataCollectionWindowInputData;

	@Output()
	private dataSaved: EventEmitter<MetadataDefinitionModel[]>;

	@Output()
	private canceled: EventEmitter<void>;

	@ViewChild(MetadataDefinitionsManagerComponent)
	public metadataDefinitionsManager: MetadataDefinitionsManagerComponent;	
	public metadataDefinitionsManagerVisible: boolean;
	public metadataDefinitionsManagerInputData: MetadataDefinitionsManagerInputData;

	public windowVisible: boolean;

	public constructor() {
		super();
		this.windowVisible = false;
		this.dataSaved = new EventEmitter();
		this.canceled = new EventEmitter();
	}

	public ngOnInit(): void {
		this.windowVisible = true;		
		this.metadataDefinitionsManagerInputData = new MetadataDefinitionsManagerInputData();
		if (ObjectUtils.isNotNullOrUndefined(this.inputData)) {
			this.metadataDefinitionsManagerInputData.metadataDefinitions = this.inputData.metadataDefinitions;
			this.metadataDefinitionsManagerInputData.workflowStates = this.inputData.workflowStates;
		}
		this.metadataDefinitionsManagerVisible = true;
	}

	public onOKAction(event: any): void {
		this.windowVisible = false;
		this.dataSaved.emit(this.metadataDefinitionsManager.getMetadataDefinitions());
	}

	public onCancelAction(event: any): void {
		this.windowVisible = false;
		this.canceled.emit();
	}

	public onHide(event: any): void {
		this.canceled.emit();
	}
}