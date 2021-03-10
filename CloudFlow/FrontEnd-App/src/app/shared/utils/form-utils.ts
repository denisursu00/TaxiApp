import { FormGroup, FormControl, AbstractControl } from "@angular/forms";
import { BooleanUtils } from "./boolean-utils";

export class FormUtils {

	public static validateAllFormFields(formGroup: FormGroup): void {
		Object.keys(formGroup.controls).forEach((controlName: string) => {
			let control: AbstractControl = formGroup.get(controlName);
			control.markAsDirty();
			control.markAsTouched();
		});
	}

	public static disableAllFormFields(formGroup: FormGroup): void {
		Object.keys(formGroup.controls).forEach((controlName: string) => {
			let control: AbstractControl = formGroup.get(controlName);
			control.disable();
		});
	}

	public static enableAllFormFields(formGroup: FormGroup): void {
		Object.keys(formGroup.controls).forEach((controlName: string) => {
			let control: AbstractControl = formGroup.get(controlName);
			control.disable();
		});
	}

	public static enableOrDisableFormControl(formControl: AbstractControl, enable: boolean): void {
		
		if (BooleanUtils.isTrue(enable)) {
			formControl.enable();
		} else {
			formControl.disable();
		}
	}

	public static  getControlByName(form: FormGroup, name: string): AbstractControl {
		return form.controls[name];
	}
}