import { ValidatorFn, AbstractControl } from "@angular/forms";
import { ValueOfNomenclatorValueField } from "./../components";
import { ObjectUtils } from "./../utils/object-utils";

export class NomenclatorValidators {

	public static nomenclatorValueRequired(): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			const value: ValueOfNomenclatorValueField = control.value;
			if (ObjectUtils.isNullOrUndefined(value)) {
				return { required: true };
			}
			if (ObjectUtils.isNullOrUndefined(value.values)) {
				return { required: true };				
			}
			return null;
		};
	}
}