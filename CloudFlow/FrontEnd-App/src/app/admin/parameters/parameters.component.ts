import { Component } from "@angular/core";
import { ParameterModel, ParametersService, AppError, MessageDisplayer, ObjectUtils, ConfirmationUtils } from "@app/shared";

@Component({
	selector: "app-parameters",
	templateUrl: "./parameters.component.html"
})
export class ParametersComponent {

	private parametersService: ParametersService;
	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;

	public parameters: ParameterModel[];
	public selectedParameter: ParameterModel;

	public parameterWindowVisible: boolean;
	public parameterWindowMode: "add" | "edit";
	public parameterWindowParameterId: number;

	public deleteButtonDisabled: boolean;
	public editButtonDisabled: boolean;

	public scrollHeight: string;

	public constructor(parameterService: ParametersService, messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils) {
		this.parametersService = parameterService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.init();
	}

	public init(): void {
		this.parameterWindowVisible = false;
		this.deleteButtonDisabled = true;
		this.editButtonDisabled = true;
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.loadParameters();
	}

	public loadParameters(): void {
		this.parametersService.getAllParameters({
			onSuccess: (parameters: ParameterModel[]): void => {
				this.parameters = parameters;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onAddParameter(): void {
		this.parameterWindowMode = "add";
		this.parameterWindowVisible = true;
	}

	public onEditParameter(): void {
		this.parameterWindowMode = "edit";
		this.parameterWindowParameterId = this.selectedParameter.id;
		this.parameterWindowVisible = true;
	}

	public onDeleteParameter(): void {
		this.confirmationUtils.confirm("CONFIRM_DELETE_PARAMETER", {
			approve: (): void => {
				this.parametersService.deleteParameterById(this.selectedParameter.id, {
					onSuccess: (): void => {
						this.messageDisplayer.displaySuccess("DELETED_SUCCESSFULLY");
						this.selectedParameter = null;
						this.changePerspective();
						this.loadParameters();
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			reject: (): void => {}
		});
	}

	public onParameterSelected(event: any): void {
		this.changePerspective();
	}

	public onParameterUnselected(event: any): void {
		this.changePerspective();
	}

	private changePerspective(): void {
		this.deleteButtonDisabled = ObjectUtils.isNullOrUndefined(this.selectedParameter);
		this.editButtonDisabled = ObjectUtils.isNullOrUndefined(this.selectedParameter);
	}

	public onParameterWindowClosed(): void {
		this.parameterWindowVisible = false;
	}

	public onParameterWindowDataSaved(): void {
		this.loadParameters();
	}
}
