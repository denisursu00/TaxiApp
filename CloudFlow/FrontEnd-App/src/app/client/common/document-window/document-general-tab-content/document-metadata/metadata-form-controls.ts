import { Component, Input, OnInit, ViewChild } from "@angular/core";
import { Validators, ValidatorFn, FormControl, FormGroup, FormBuilder, ControlValueAccessor, NgModel, NG_VALUE_ACCESSOR } from "@angular/forms";
import { 
	AppError,
	MessageDisplayer,
	DateConstants,
	ObjectUtils,
	ArrayUtils,
	MetadataDefinitionModel, 
	MetadataCollectionInstanceModel, 
	MetadataCollectionInstanceRowModel, 
	ListMetadataItemModel, 
	UserModel, 
	MetadataInstanceModel, 
	WorkflowStateModel,
	StringUtils,
	DateUtils,
	BooleanUtils} from "@app/shared";
import * as moment from "moment";
import { numericValidation } from "@app/shared";
import { MetadataUtils } from "./metadata-utils";
import { MetadataDocumentValue } from "../../../metadata-document-field/metadata-document-field.component";

export abstract class MetadataFormControl<V> extends FormControl {

	protected metadataDefinition: MetadataDefinitionModel;
	protected metadataInstance: MetadataInstanceModel | MetadataCollectionInstanceModel;
	protected documentWorkflowState: WorkflowStateModel;
	protected readonly: boolean;
	private autoAddDefaultValidators: boolean;

	public constructor(metadataDefinition: MetadataDefinitionModel, 
			metadataInstance: MetadataInstanceModel | MetadataCollectionInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean, autoAddDefaultValidators: boolean) {
		super();
		this.metadataDefinition = metadataDefinition;
		this.metadataInstance = metadataInstance;
		this.documentWorkflowState = documentWorkflowState;
		this.readonly = readonly;
		this.autoAddDefaultValidators = autoAddDefaultValidators;

		this.prepareValidators();
		this.prepareValue();		
	}

	protected isReadonly(): boolean {
		return (this.readonly || MetadataUtils.isRestrictedOnEdit(this.metadataDefinition, this.documentWorkflowState));
	}

	private prepareValidators(): void {
		let validators: ValidatorFn[] = [];
		if (this.autoAddDefaultValidators) {
			if (MetadataUtils.isMetadataMandatory(this.metadataDefinition, this.documentWorkflowState)) {
				validators.push(Validators.required);
			}
		}
		let specificValidators = this.getSpecificValidators();
		specificValidators.forEach((specificValidator: ValidatorFn) => {
			validators.push(specificValidator);
		});
		this.setValidators(validators);
	}

	private prepareValue(): void {		
		if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_METADATA_COLLECTION) {	
			this.setValue(this.metadataInstance);
		} else {
			let values: string[] = [];
			if (ObjectUtils.isNotNullOrUndefined(this.metadataInstance)) {
				values = (<MetadataInstanceModel> this.metadataInstance).values;
			}
			this.setValue(this.getSpecificValue(values));
		}		
	}

	protected abstract getSpecificValue(values: string[]): V;

	protected getSingleSpecificValueAsString(values: string[]): string {
		if (ArrayUtils.isNotEmpty(values)) {
			return values[0];
		}
		return null;
	}

	protected abstract getSpecificValidators(): ValidatorFn[];
}


export class MetadataTextFormControl extends MetadataFormControl<string> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): string {
		return this.getSingleSpecificValueAsString(values);
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}


export class MetadataNumericFormControl extends MetadataFormControl<string> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): string {
		return this.getSingleSpecificValueAsString(values);
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [numericValidation];
	}
}


export class MetadataAutoNumberFormControl extends MetadataFormControl<string> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, false);
	}
	
	protected getSpecificValue(values: string[]): string {
		return this.getSingleSpecificValueAsString(values);
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}


export class MetadataTextAreaFormControl extends MetadataFormControl<string> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): string {
		return this.getSingleSpecificValueAsString(values);
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}


export class MetadataUserFormControl extends MetadataFormControl<string> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): string {
		return this.getSingleSpecificValueAsString(values);
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}


export class MetadataListFormControl extends MetadataFormControl<string[]> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): string[] {
		if (ArrayUtils.isNotEmpty(values)) {
			return values;
		}
		return null;
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}


export class MetadataDateFormControl extends MetadataFormControl<Date> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): Date {
		let dateAsString = this.getSingleSpecificValueAsString(values);
		if (StringUtils.isNotBlank(dateAsString)) {
			return DateUtils.parseFromStorage(dateAsString);
		}
		return null;
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}

export class MetadataDateTimeFormControl extends MetadataFormControl<Date> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): Date {
		let dateTimeAsString = this.getSingleSpecificValueAsString(values);
		if (StringUtils.isNotBlank(dateTimeAsString)) {
			return DateUtils.parseDateTimeFromStorage(dateTimeAsString);
		}
		return null;
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}

export class MetadataMonthFormControl extends MetadataFormControl<Date> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): Date {
		let monthAsString = this.getSingleSpecificValueAsString(values);
		if (StringUtils.isNotBlank(monthAsString)) {
			return DateUtils.parseMonthYearFromStorage(monthAsString);
		}
		return null;
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}

export class MetadataNomenclatorFormControl extends MetadataFormControl<number> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): number {
		let nomenclatorIdAsString: string = this.getSingleSpecificValueAsString(values);
		if (ObjectUtils.isNotNullOrUndefined(nomenclatorIdAsString)) {
			return StringUtils.toNumber(nomenclatorIdAsString);
		}
		return null;
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}

export class MetadataGroupFormControl extends MetadataFormControl<string> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): string {
		return this.getSingleSpecificValueAsString(values);
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}

export class MetadataDocumentFormControl extends MetadataFormControl<string[]> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): string[] {
		return values;
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}

export class MetadataCalendarFormControl extends MetadataFormControl<number> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): number {
		let calendarIdAsString: string = this.getSingleSpecificValueAsString(values);
		if (ObjectUtils.isNotNullOrUndefined(calendarIdAsString)) {
			return StringUtils.toNumber(calendarIdAsString);
		}
		return null;
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}

export class MetadataProjectFormControl extends MetadataFormControl<number[]> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): number[] {		
		if (ArrayUtils.isEmpty(values)) {
			return null;
		}
		let valuesAsNumber: number[] = [];
		values.forEach((value: string) => {
			valuesAsNumber.push(StringUtils.toNumber(value));
		});
		return valuesAsNumber;
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}

export class MetadataCollectionFormControl extends MetadataFormControl<MetadataCollectionInstanceModel> {

	public constructor(metadataDefinition: MetadataDefinitionModel, metadataInstance: MetadataCollectionInstanceModel, 
			documentWorkflowState: WorkflowStateModel, readonly: boolean) {
		super(metadataDefinition, metadataInstance, documentWorkflowState, readonly, true);
	}
	
	protected getSpecificValue(values: string[]): MetadataCollectionInstanceModel {
		throw new Error("Illegal call");
	}

	protected getSpecificValidators(): ValidatorFn[] {
		return [];
	}
}

