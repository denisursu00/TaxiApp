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

export class MetadataFormControl extends FormControl {

	protected metadataDefinition: MetadataDefinitionModel;
	protected metadataInstance: MetadataInstanceModel | MetadataCollectionInstanceModel;

	public constructor(metadataDefinition: MetadataDefinitionModel, 
			metadataInstance: MetadataInstanceModel | MetadataCollectionInstanceModel) {
		super();
		this.metadataDefinition = metadataDefinition;
		this.metadataInstance = metadataInstance;

		this.prepareValidators();
		this.prepareValue();		
	}

	private prepareValidators(): void {
		let validators: ValidatorFn[] = [];
		if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_NUMERIC) {
			validators.push(numericValidation);
		}
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

	private getSpecificValue(values: string[]): any {		
		// text, numeric, autonum, textarea, user
		if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_TEXT
				|| this.metadataDefinition.type === MetadataDefinitionModel.TYPE_NUMERIC
				|| this.metadataDefinition.type === MetadataDefinitionModel.TYPE_AUTO_NUMBER
				|| this.metadataDefinition.type === MetadataDefinitionModel.TYPE_TEXT_AREA
				|| this.metadataDefinition.type === MetadataDefinitionModel.TYPE_GROUP
				|| this.metadataDefinition.type === MetadataDefinitionModel.TYPE_DOCUMENT
				|| this.metadataDefinition.type === MetadataDefinitionModel.TYPE_USER) {
			return this.getSingleSpecificValueAsString(values);
		}
		// list
		if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_LIST) {
			if (ArrayUtils.isNotEmpty(values)) {
				return values;
			}
		}
		// date
		if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE) {
			let dateAsString = this.getSingleSpecificValueAsString(values);
			if (StringUtils.isNotBlank(dateAsString)) {
				return DateUtils.parseFromStorage(dateAsString);
			}
		}
		// date time
		if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_DATE_TIME) {
			let dateTimeAsString = this.getSingleSpecificValueAsString(values);
			if (StringUtils.isNotBlank(dateTimeAsString)) {
				return DateUtils.parseDateTimeFromStorage(dateTimeAsString);
			}
		}
		// month
		if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_MONTH) {
			let monthAsString = this.getSingleSpecificValueAsString(values);
			if (StringUtils.isNotBlank(monthAsString)) {
				return DateUtils.parseMonthYearFromStorage(monthAsString);
			}
		}
		// nomenclator
		if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_NOMENCLATOR 
				|| this.metadataDefinition.type === MetadataDefinitionModel.TYPE_CALENDAR) {
			let valueIdAsString: string = this.getSingleSpecificValueAsString(values);
			if (ObjectUtils.isNotNullOrUndefined(valueIdAsString)) {
				return StringUtils.toNumber(valueIdAsString);
			}
		}
		// project
		if (this.metadataDefinition.type === MetadataDefinitionModel.TYPE_PROJECT) {
			if (ArrayUtils.isNotEmpty(values)) {
				let valuesAsNumber: number[] = [];
				values.forEach((value: string) => {
					valuesAsNumber.push(StringUtils.toNumber(value));
				});
				return valuesAsNumber;
			}
		}
		return null;
	}

	private getSingleSpecificValueAsString(values: string[]): string {
		if (ArrayUtils.isNotEmpty(values)) {
			return values[0];
		}
		return null;
	}
}