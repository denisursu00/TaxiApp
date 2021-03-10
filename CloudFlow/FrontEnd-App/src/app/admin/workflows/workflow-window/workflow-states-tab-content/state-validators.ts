import { ValidatorFn, AbstractControl } from "@angular/forms";
import { WorkflowStateModel, ArrayUtils, ObjectUtils } from "@app/shared";
import { WorkflowStatesTabContentComponent } from ".";

export class WorkflowStateValidators {

	public static duplicatedStateCodesValidation(workflowStatesProvider: WorkflowStatesProvider): ValidatorFn {
		return (control: AbstractControl): {[key: string]: any} => {
			let isValid: boolean = true;
			workflowStatesProvider.getStates().forEach((state: WorkflowStateModel) => {
				if (state.code === control.value) {
					isValid = false;
				}
				if (ObjectUtils.isNotNullOrUndefined(workflowStatesProvider.getExcludeState())
						&& workflowStatesProvider.getExcludeState() === state) {
					isValid = true;
				}
			});
			return isValid ? null : { duplicateStateCode: true };
		};
	}

	public static duplicatedStateNamesValidation(workflowStatesProvider: WorkflowStatesProvider): ValidatorFn {
		return (control: AbstractControl): {[key: string]: any} => {
			let isValid: boolean = true;
			workflowStatesProvider.getStates().forEach((state: WorkflowStateModel) => {
				if (state.name === control.value) {
					isValid = false;
				}
				if (ObjectUtils.isNotNullOrUndefined(workflowStatesProvider.getExcludeState())
						&& workflowStatesProvider.getExcludeState() === state) {
					isValid = true;
				}
			});
			return isValid ? null : { duplicateStateName: true };
		};
	}
}

export interface WorkflowStatesProvider {
	
	getStates(): WorkflowStateModel[];
	getExcludeState(): WorkflowStateModel;
}