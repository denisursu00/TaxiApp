import { Component, OnInit, Input } from "@angular/core";
import { MetadataDefinitionModel, ArrayUtils, ObjectUtils, ConfirmationWindowFacade } from "@app/shared";
import { MetadataTypeConstants } from "@app/shared";
import { Message } from "@app/shared";
import { MetadataDefinitionInputData } from "./metadata-definition/metadata-definition-input-data";
import { MetadataDefinitionsManagerInputData } from "./metadata-definitions-manager-input-data";

@Component({
	selector: "app-metadata-definitions-manager",
	templateUrl: "./metadata-definitions-manager.component.html"
})
export class MetadataDefinitionsManagerComponent implements OnInit {

	@Input()
	public usageMode: "documentType" | "metadataCollection";

	@Input()
	public inputData: MetadataDefinitionsManagerInputData;
	
	public metadataDefinitions: MetadataDefinitionModel[];

	public selectedMetadataDefinition: MetadataDefinitionModel;
	
	public metadataTypeLabelCodeByTypeMap = MetadataTypeConstants.getMetadataTypeLabelCodeByTypeMap();
	
	private messages: Message[];

	public deleteActionEnabled: boolean;
	
	public metadataDefinitionVisible: boolean;
	public metadataDefinitionUsageMode: string;
	public metadataDefinitionInputData: MetadataDefinitionInputData;

	public confirmationWindow: ConfirmationWindowFacade;

	public constructor() {
		this.deleteActionEnabled = false;
		this.metadataDefinitions = [];
		this.metadataDefinitionVisible = false;
		this.confirmationWindow = new ConfirmationWindowFacade();
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.inputData)) {
			if (ArrayUtils.isNotEmpty(this.inputData.metadataDefinitions)) {
				this.metadataDefinitions = this.inputData.metadataDefinitions;
				this.sortMetadataDefinitions();
			}
		}
		this.metadataDefinitionUsageMode = this.usageMode;
	}

	private sortMetadataDefinitions(): void {
		this.metadataDefinitions.sort((md1: MetadataDefinitionModel, md2: MetadataDefinitionModel): number => {
			if (md1.orderNumber < md2.orderNumber) {
				return -1;
			}
			if (md1.orderNumber > md2.orderNumber) {
				return 1;
			}
			return 0;
		});
	}

	public getMetadataDefinitions(): MetadataDefinitionModel[] {
		return this.metadataDefinitions;
	}
	
	public onMetadataDefinitonSelected(event: any): void {		
		let newMetadataDefinitionInputData: MetadataDefinitionInputData = new MetadataDefinitionInputData();
		newMetadataDefinitionInputData.editMetadataDefinition = this.selectedMetadataDefinition.clone();
		if (ObjectUtils.isNotNullOrUndefined(this.inputData)) {
			newMetadataDefinitionInputData.workflowStates = this.inputData.workflowStates;
		}
		newMetadataDefinitionInputData.metadataDefinitions = this.getAllClonedMetadataDefinitions();
		this.metadataDefinitionInputData = newMetadataDefinitionInputData;
		this.changePerspective();	
		this.metadataDefinitionVisible = true;
	}

	public onMetadataDefinitonUnselected(event: any): void {
		this.changePerspective();
		this.metadataDefinitionVisible = false;
	}

	public getMessages(): Message[] {
		return this.messages;
	}

	private changePerspective(): void {
		this.deleteActionEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedMetadataDefinition);
	}

	private getAllMetadataNames(): string[] {
		let allMetadataNames: string[] = [];
		this.metadataDefinitions.forEach((md: MetadataDefinitionModel) => {
			allMetadataNames.push(md.name);
		});
		return allMetadataNames;
	}

	private getAllClonedMetadataDefinitions(): MetadataDefinitionModel[] {
		let allMetadataDefinitions: MetadataDefinitionModel[] = [];
		if (ArrayUtils.isNotEmpty(this.metadataDefinitions)) {
			this.metadataDefinitions.forEach((md: MetadataDefinitionModel) => {
				allMetadataDefinitions.push(md.clone());
			});
		}		
		return allMetadataDefinitions;
	}

	public onAddAction(event: any): void {
		let newMetadataDefinitionInputData: MetadataDefinitionInputData = new MetadataDefinitionInputData();
		newMetadataDefinitionInputData.editMetadataDefinition = null;		
		newMetadataDefinitionInputData.workflowStates = [];
		if (ObjectUtils.isNotNullOrUndefined(this.inputData)) {
			newMetadataDefinitionInputData.workflowStates = this.inputData.workflowStates;
		}
		newMetadataDefinitionInputData.metadataDefinitions = this.getAllClonedMetadataDefinitions();
		this.metadataDefinitionInputData = newMetadataDefinitionInputData;
		this.selectedMetadataDefinition = null;
		this.metadataDefinitionVisible = true;
		this.changePerspective();
	}
	
	public onDeleteAction(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedMetadataDefinition)) {
			return;
		}
		this.confirmationWindow.confirm({
			approve: (): void => {
				ArrayUtils.removeElement(this.metadataDefinitions, this.selectedMetadataDefinition);
				this.selectedMetadataDefinition = null;
				this.metadataDefinitionVisible = false;
				this.changePerspective();
				this.confirmationWindow.hide();
			},
			reject: (): void => {
				this.confirmationWindow.hide();
			}
		}, "DELETE_CONFIRM");
	}

	public onMetadataDefinitionDataSaved(addedOrModifiedMetadataDefinition: MetadataDefinitionModel): void {
		if (ObjectUtils.isNotNullOrUndefined(this.selectedMetadataDefinition)) {
			ArrayUtils.replaceElement(this.metadataDefinitions, this.selectedMetadataDefinition, addedOrModifiedMetadataDefinition);
		} else {
			this.metadataDefinitions.push(addedOrModifiedMetadataDefinition);
		}
		this.selectedMetadataDefinition = null;
		this.metadataDefinitionVisible = false;
		this.changePerspective();
	}

	public onMetadataDefinitionCanceled(): void {
		this.metadataDefinitionVisible = false;
		this.selectedMetadataDefinition = null;
		this.changePerspective();
	}
}