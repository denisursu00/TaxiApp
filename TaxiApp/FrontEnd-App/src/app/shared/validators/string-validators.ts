import { FormControl, ValidatorFn, AbstractControl } from "@angular/forms";
import { StringUtils } from "../utils/string-utils";
import { ArrayUtils } from "../utils/array-utils";
import { ObjectUtils } from "../utils/object-utils";

export class StringValidators {

	public static blank(): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			const value = control.value;
			if (ObjectUtils.isNullOrUndefined(value)) {
				return null;
			} else {
				if ((<string> value).length > 0 && StringUtils.isBlank(value)) {
					return { blank: true };
				}
			}
			return null;
		};
	}

	public static duplicated(takenValues: string[]): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			if (ArrayUtils.isEmpty(takenValues)) {
				return null;
			}
			let controlValue: string = control.value;
			if (StringUtils.isBlank(controlValue)) {
				return null;
			}
			let duplicated: boolean = false;
			takenValues.forEach((takenValue: string) => {
				if (takenValue === controlValue) {
					duplicated = true;
				}
			});
			return duplicated ? { duplicated: true } : null;
		};
	}

	public static numeric(): ValidatorFn | null {
		return (control: AbstractControl): {[key: string]: any} => {
			const value = control.value;
			if (ObjectUtils.isNullOrUndefined(value)) {
				return null;
			}
			if (typeof value === "number") {
				return null;
			}
			if (StringUtils.isBlank(value)) {
				return null;
			}
			return StringUtils.isNumeric(value) ? null : { invalidNumber: true };
		};
	}
}