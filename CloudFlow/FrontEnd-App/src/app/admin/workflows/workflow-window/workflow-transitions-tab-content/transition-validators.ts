import { ValidatorFn, AbstractControl } from "@angular/forms";
import { WorkflowTransitionModel, ObjectUtils, WorkflowStateModel, ArrayUtils } from "@app/shared";


export class TransitionValidators {

	public static transitionNameValidator(workflowTransitionsProvider: WorkflowTransitionsAndStatesProvider): ValidatorFn {
		return (control: AbstractControl): {[key: string]: any} => {
			if (ObjectUtils.isNullOrUndefined(control.value)) {
				return null;
			}
			if (ArrayUtils.isEmpty(workflowTransitionsProvider.getTransitions())) {
				return;
			}

			let transitionName: string = control.value;
			let transitionWithNameExists: boolean = false;

			workflowTransitionsProvider.getTransitions().forEach((transition: WorkflowTransitionModel) => {
				if (transition.name === transitionName) {
					if (ObjectUtils.isNotNullOrUndefined(workflowTransitionsProvider.getExcludeTransition())) {
						if (workflowTransitionsProvider.getExcludeTransition().startState.code !== transition.startState.code) {
							transitionWithNameExists = true;
						}
					} else {
						transitionWithNameExists = true;
					}
				}
			});
			return transitionWithNameExists ? { transitionWithNameExists: true } : null;
		};
	}

	public static transitionStartStateValidator(workflowTransitionsProvider: WorkflowTransitionsAndStatesProvider): ValidatorFn {
		return (control: AbstractControl): {[key: string]: any} => {
			if (ObjectUtils.isNullOrUndefined(control.value)) {
				return null;
			}
			if ((<WorkflowStateModel>control.value).stateType !== WorkflowStateModel.STATETYPE_START) {
				return null;
			}
			if (ArrayUtils.isEmpty(workflowTransitionsProvider.getTransitions())) {
				return;
			}
			
			let startStateExists: boolean = false;
			workflowTransitionsProvider.getTransitions().forEach((transition: WorkflowTransitionModel) => {
				if (transition.startState.stateType === WorkflowStateModel.STATETYPE_START) {
					if (ObjectUtils.isNotNullOrUndefined(workflowTransitionsProvider.getExcludeTransition())) {
						if (workflowTransitionsProvider.getExcludeTransition().startState.code !== transition.startState.code) {
							startStateExists = true;
						}
					} else {
						startStateExists = true;
					}
				}
			});
			return startStateExists ? { startStateExists: true } : null;
		};
	}

	public static transitionFinalStateValidator(workflowTransitionsProvider: WorkflowTransitionsAndStatesProvider): ValidatorFn {
		return (control: AbstractControl): {[key: string]: any} => {
			if (ObjectUtils.isNullOrUndefined(control.value)) {
				return null;
			}
			if (ObjectUtils.isNullOrUndefined(workflowTransitionsProvider.getStartState())) {
				return null;
			}
			if (ArrayUtils.isEmpty(workflowTransitionsProvider.getTransitions())) {
				return;
			}

			let finalState = <WorkflowStateModel>control.value;
			
			let finalStateExists: boolean = false;
			workflowTransitionsProvider.getTransitions().forEach((transition: WorkflowTransitionModel) => {
				if (transition.finalState.code === finalState.code && transition.startState.code === workflowTransitionsProvider.getStartState().code) {
					if (ObjectUtils.isNotNullOrUndefined(workflowTransitionsProvider.getExcludeTransition())) {
						if (workflowTransitionsProvider.getExcludeTransition().startState.code !== transition.startState.code) {
							finalStateExists = true;
						}
					} else {
						finalStateExists = true;
					}
				}
			});
			return finalStateExists ? { finalStateExists: true } : null;
		};
	}
}

export interface WorkflowTransitionsAndStatesProvider {
	
	getTransitions(): WorkflowTransitionModel[];
	getExcludeTransition(): WorkflowTransitionModel;
	getStartState(): WorkflowStateModel;
}