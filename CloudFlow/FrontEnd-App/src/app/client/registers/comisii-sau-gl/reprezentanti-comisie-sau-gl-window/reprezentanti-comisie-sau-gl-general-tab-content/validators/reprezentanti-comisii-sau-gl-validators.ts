import { ValidatorFn, AbstractControl } from "@angular/forms";
import { ObjectUtils, NomenclatorUtils } from "@app/shared/utils";

export class ReprezentantiComisiiSauGlValidators {

	public static dataInceputMandatVicepresedinteRequiredValidator(provider: DataInceputMandatVicepresedinteValidationProvider): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			let controlValue: any = control.value;
			if (NomenclatorUtils.fieldValueHasValue(provider.getVicepresedinteFieldValue()) && ObjectUtils.isNullOrUndefined(controlValue)) {
				return { required: true};
			}
			return null;
		};
	}
}

export interface DataInceputMandatVicepresedinteValidationProvider {
	getVicepresedinteFieldValue(): any;
}