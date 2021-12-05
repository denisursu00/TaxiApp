import { Component } from "@angular/core";
import { ParameterModel, ParametersService, AppError, MessageDisplayer, ObjectUtils, ConfirmationUtils, TranslateUtils } from "@app/shared";
import { Column } from "primeng/primeng";

@Component({
	selector: "app-parameters",
	templateUrl: "./parameters.component.html",
	styleUrls: ["./parameters.component.css"]
})
export class ParametersComponent {

	private parametersService: ParametersService;
	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;
	private translateUtils: TranslateUtils;

	public parameters: ParameterModel[];
	public selectedParameter: ParameterModel;

	public parameterWindowVisible: boolean;
	public parameterWindowMode: "add" | "edit";
	public parameterWindowParameterId: number;

	public deleteButtonDisabled: boolean;
	public editButtonDisabled: boolean;

	public scrollHeight: string;

	public columns: Column[];

	public constructor(parameterService: ParametersService, messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils, translateUtils: TranslateUtils) {
		this.parametersService = parameterService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.translateUtils = translateUtils;
		this.init();
	}

	public init(): void {
		this.columns = [];
		this.initiateColumns();
		this.parameterWindowVisible = false;
		this.deleteButtonDisabled = true;
		this.editButtonDisabled = true;
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.loadParameters();
	}

	private initiateColumns(): void {
		let parName = new Column();
		parName.header = this.translateUtils.translateLabel("PARAMETER_NAME");
		let parDesc = new Column();
		parDesc.header = this.translateUtils.translateLabel("PARAMETER_DESCRIPTION");
		let parVal = new Column();
		parVal.header = this.translateUtils.translateLabel("PARAMETER_VALUE");
		let parType = new Column();
		parType.header = this.translateUtils.translateLabel("PARAMETER_TYPE");
		this.columns.push(parName);
		this.columns.push(parDesc);
		this.columns.push(parVal);
		this.columns.push(parType);
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
		this.deleteButtonDisabled = true;
		this.editButtonDisabled = true;
		this.selectedParameter = null;
		this.changePerspective();
	}

	public onParameterWindowDataSaved(): void {
		this.selectedParameter = null;
		this.changePerspective();
		this.loadParameters();
	}
}
