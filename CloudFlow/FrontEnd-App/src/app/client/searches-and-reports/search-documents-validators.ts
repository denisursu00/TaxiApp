import { ValidatorFn, AbstractControl, FormGroup } from "@angular/forms";
import { ObjectUtils, ValidatorUtils } from "@app/shared";

export class SearchDocumentsValidators {

	public static fromDateGreaterThanToDate(fromDateControlName: string, toDateControlName: string): ValidatorFn {
		return (group: FormGroup): any => {
			let fromDateControl: AbstractControl = group.controls[fromDateControlName];
			let toDateControl: AbstractControl = group.controls[toDateControlName];

			let fromDateControlValue: Date = <Date> fromDateControl.value;
			let toDateControlValue: Date = <Date> toDateControl.value;

			if (ObjectUtils.isNullOrUndefined(fromDateControlValue) || ObjectUtils.isNullOrUndefined(toDateControlValue)) {
				return;
			}

			ValidatorUtils.deleteErrorsFromControlByErrorKey(fromDateControl, "toDateGreaterThanFromDate");
			ValidatorUtils.deleteErrorsFromControlByErrorKey(toDateControl, "fromDateLowerThanToDate");

			if (fromDateControlValue > toDateControlValue) {
				if (!fromDateControl.errors && !toDateControl.errors) {
					fromDateControl.setErrors({ toDateGreaterThanFromDate: true });
					toDateControl.setErrors({ fromDateLowerThanToDate: true });
				}
			}
		};
	}
}