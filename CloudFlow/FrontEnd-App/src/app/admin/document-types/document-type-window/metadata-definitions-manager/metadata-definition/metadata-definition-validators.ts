import { ValidatorFn, AbstractControl } from "@angular/forms";
import { StringUtils, ObjectUtils, ArrayUtils, MetadataDefinitionModel, WorkflowConstants, WorkflowStateUtils, BooleanUtils } from "@app/shared";
import { ValueOfMetadataDefaultValueField } from "../../../index";

export class MetadataDefinitionValidators {

	public static metadataNameValidator(takenNames: string[]): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			
			let controlValue: string = control.value;
			if (StringUtils.isBlank(controlValue)) {
				return { required: true };
			}
			
			let regExp: RegExp = new RegExp(/^[a-zA-Z0-9_]+$/);
			let isPattern: boolean = regExp.test(controlValue);
			if (!isPattern) {
				return { pattern: true};
			}

			if (ArrayUtils.isEmpty(takenNames)) {
				return null;
			}
			let duplicated: boolean = false;
			takenNames.forEach((takenValue: string) => {
				if (takenValue === controlValue) {
					duplicated = true;
				}
			});
			return duplicated ? { duplicated: true } : null;
		};
	}

	public static defaultMetadataValueValidator(provider: DefaultMetadataValueValidationProvider): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			if (StringUtils.isBlank(provider.getMetadataType())) {
				return null;
			}
			let controlValue: ValueOfMetadataDefaultValueField = control.value;
			if (ObjectUtils.isNullOrUndefined(controlValue) || StringUtils.isBlank(controlValue.value)) {
				return null;
			}
			if (provider.getMetadataType() === MetadataDefinitionModel.TYPE_NUMERIC) {
				return StringUtils.isNotNumeric(controlValue.value) ? { invalidNumber: true } : null;
			}
			return null;
		};
	}

	public static numberLengthValidator(provider: NumberLengthValidationProvider): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			if (StringUtils.isBlank(provider.getAutoNumberPrefix())) {
				return null;
			}
			let controlValue: any = control.value;
			if (ObjectUtils.isNullOrUndefined(controlValue) || (ObjectUtils.isString(controlValue) && StringUtils.isBlank(controlValue))) {
				return null;
			}			
			let numberLength: number = ObjectUtils.isString(controlValue) ? parseInt(controlValue, 0) : controlValue;
			return (numberLength <= provider.getAutoNumberPrefix().length) ? { numberLengthLowerThanPrefixLength: true} : null;
		};
	}

	public static invisibleValidator(provider: InvisibleValidationProvider): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			let controlValue: boolean = <boolean> control.value;
			if (BooleanUtils.isFalse(controlValue)) {
				return null;
			}
			if (BooleanUtils.isFalse(provider.isMandatory())) {
				return null;
			}
			if (StringUtils.isBlank(provider.getMandatoryInStates()) || StringUtils.isBlank(provider.getInvisibleInStates())) {
				return { mandatoryAndInvisibleAtTheSameTime : true };
			}
			return null;
		};
	}

	public static invisibleInStatesValidator(provider: InvisibleInStatesValidationProvider): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			if (BooleanUtils.isFalse(provider.isMandatory())) {
				return null;
			}
			if (StringUtils.isBlank(provider.getMandatoryInStates())) {
				return null;
			}
			let controlValue: any = control.value;
			if (StringUtils.isBlank(controlValue)) {
				return null;
			}
			let found: boolean = false;
			let invisibleInStateCodes: string[] = (<string> controlValue).split(WorkflowConstants.STEPS_SEPARATOR);
			invisibleInStateCodes.forEach((invisibleInStateCode: string) => {
				if (WorkflowStateUtils.isStateFound(provider.getMandatoryInStates(), invisibleInStateCode)) {
					found = true;
				}
			});
			return found ? { mandatoryAndInvisibleAtTheSameStep : true} : null;
		};
	}
}

export interface DefaultMetadataValueValidationProvider {

	getMetadataType(): string;
}

export interface NumberLengthValidationProvider {

	getAutoNumberPrefix(): string;
}

export interface InvisibleValidationProvider {

	isMandatory(): boolean;

	getMandatoryInStates(): string;

	getInvisibleInStates(): string;
}

export interface InvisibleInStatesValidationProvider {

	isMandatory(): boolean;

	getMandatoryInStates(): string;
}