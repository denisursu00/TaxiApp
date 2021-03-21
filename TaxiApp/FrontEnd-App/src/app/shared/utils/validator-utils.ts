import { AbstractControl } from "@angular/forms";
import { ArrayUtils } from "./array-utils";

export class ValidatorUtils {

	public static deleteErrorsFromControlByErrorKey(control: AbstractControl, errorKey: string): void {
		if (control.errors && control.errors[errorKey]) {
			delete control.errors[errorKey];
			if (ArrayUtils.isEmpty(Object.keys(control.errors))) {
				control.setErrors(null);
			}
		}
	}
}