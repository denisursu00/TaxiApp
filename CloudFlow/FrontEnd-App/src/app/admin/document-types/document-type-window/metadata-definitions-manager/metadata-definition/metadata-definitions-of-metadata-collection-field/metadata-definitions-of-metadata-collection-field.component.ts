import { Component, Input, OnInit } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { MetadataDefinitionModel, WorkflowStateModel, ArrayUtils, ObjectUtils } from "./../../../../../../shared";
import { MetadataDefinitionsOfMetadataCollectionWindowInputData } from "./../../../metadata-definitions-of-metadata-collection-window/metadata-definitions-of-metadata-collection-window-input-data";

@Component({
	selector: "app-metadata-definitions-of-metadata-collection-field",
	template: `
		<p-button (onClick)="onEditMetadataDefinitionsAction($event)" [label]="('LABELS.METADATAS_OF_COLLECTION' | translate) + ': ' + metadatasSize"></p-button>
		<app-metadata-definitions-of-metadata-collection-window *ngIf="metadataDefinitionsOfMetadataCollectionWindowVisible"
			[inputData]="metadataDefinitionsOfMetadataCollectionWindowInputData"
			(dataSaved)="onMetadataDefinitionsOfMetadataCollectionWindowDataSaved($event)"
			(canceled)="onMetadataDefinitionsOfMetadataCollectionWindowCanceled($event)">
		</app-metadata-definitions-of-metadata-collection-window>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: MetadataDefinitionsOfMetadataCollectionFieldComponent, multi: true }
	]
})
export class MetadataDefinitionsOfMetadataCollectionFieldComponent implements ControlValueAccessor, OnInit {

	@Input()
	public workflowStates: WorkflowStateModel[];

	private innerValue: MetadataDefinitionModel[];

	public metadataDefinitionsOfMetadataCollectionWindowVisible: boolean;
	public metadataDefinitionsOfMetadataCollectionWindowInputData: MetadataDefinitionsOfMetadataCollectionWindowInputData;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor() {
		this.metadataDefinitionsOfMetadataCollectionWindowVisible = false;
	}

	public ngOnInit(): void {
	}

	public get metadatasSize(): number {
		if (ArrayUtils.isNotEmpty(this.innerValue)) {
			return this.innerValue.length;
		}
		return 0;
	}

	private propagateInnerValue(): void {
		this.onChange(this.innerValue);
		this.onTouched();
	}

	public onEditMetadataDefinitionsAction(event: any): void {
		this.metadataDefinitionsOfMetadataCollectionWindowInputData = new MetadataDefinitionsOfMetadataCollectionWindowInputData();
		this.metadataDefinitionsOfMetadataCollectionWindowInputData.metadataDefinitions = [];
		if (ArrayUtils.isNotEmpty(this.innerValue)) {
			this.innerValue.forEach((metadataDefinition: MetadataDefinitionModel) => {
				this.metadataDefinitionsOfMetadataCollectionWindowInputData.metadataDefinitions.push(metadataDefinition.clone());
			});
		}
		this.metadataDefinitionsOfMetadataCollectionWindowInputData.workflowStates = this.workflowStates;
		this.metadataDefinitionsOfMetadataCollectionWindowVisible = true;
	}

	public onMetadataDefinitionsOfMetadataCollectionWindowDataSaved(metadataDefinitions: MetadataDefinitionModel[]): void {
		this.innerValue = metadataDefinitions;
		this.propagateInnerValue();
		this.metadataDefinitionsOfMetadataCollectionWindowVisible = false;
	}

	public onMetadataDefinitionsOfMetadataCollectionWindowCanceled(metadataDefinitions: MetadataDefinitionModel[]): void {
		this.metadataDefinitionsOfMetadataCollectionWindowVisible = false;
	}

	public writeValue(metadataDefinitions: MetadataDefinitionModel[]): void {
		this.innerValue = metadataDefinitions;
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}