import { ValidatorFn, AbstractControl, FormGroup } from "@angular/forms";
import { ObjectUtils } from "../../utils/object-utils";
import { ValidatorUtils } from "../../utils/validator-utils";

export class ReplacementProfileValidators {

	public static startDateGreaterThanEndDate(startDateControlName: string, endDateControlName: string): ValidatorFn {
		return (group: FormGroup): any => {

			let startDateControl: AbstractControl = group.controls[startDateControlName];
			let endDateControl: AbstractControl = group.controls[endDateControlName];

			let statDateControlValue: Date = <Date> startDateControl.value;
			let endDateControlValue: Date = <Date> endDateControl.value;

			if (ObjectUtils.isNullOrUndefined(statDateControlValue) || ObjectUtils.isNullOrUndefined(endDateControlValue)) {
				return;
			}
			
			ValidatorUtils.deleteErrorsFromControlByErrorKey(startDateControl, "startDateGreaterThanEndDate");
			ValidatorUtils.deleteErrorsFromControlByErrorKey(endDateControl, "endDateLowerThanStartDate");

			if (statDateControlValue > endDateControlValue) {
				if (!startDateControl.errors && !endDateControl.errors) {
					startDateControl.setErrors({ startDateGreaterThanEndDate: true });
					endDateControl.setErrors({ endDateLowerThanStartDate: true });
				}
			}
		};
	}
}