import { FormControl } from "@angular/forms";
import { ObjectUtils, StringUtils } from "./../utils";

export function numericValidation(control: FormControl) {
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
	return StringUtils.isNumeric(value) ? null : { numericValidation: true };
}