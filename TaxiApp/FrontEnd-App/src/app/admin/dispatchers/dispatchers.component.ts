import { Component } from "@angular/core";
import { AppError, MessageDisplayer, ObjectUtils, ConfirmationUtils, DateUtils, OrganizationService } from "@app/shared";
import { UserModel } from "@app/shared/model/organization/user.model";

@Component({
  selector: 'app-dispatchers',
  templateUrl: './dispatchers.component.html',
  styleUrls: ['./dispatchers.component.css']
})
export class DispatchersComponent {

  	private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;
	private confirmationUtils: ConfirmationUtils;

	public dispatchers: UserModel[];
	public selectedDispatcher: UserModel;

	public dispatcherWindowVisible: boolean;
	public dispatcherWindowMode: "add" | "edit";
	public dispatcherWindowDispatcherId: number;

	public deleteButtonDisabled: boolean;
	public editButtonDisabled: boolean;

	public scrollHeight: string;

	public constructor(organizationService: OrganizationService, messageDisplayer: MessageDisplayer, confirmationUtils: ConfirmationUtils) {
		this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.init();
	}

	public init(): void {
		this.dispatcherWindowVisible = false;
		this.deleteButtonDisabled = true;
		this.editButtonDisabled = true;
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.loadDispatchers();
	}

	public loadDispatchers(): void {
		this.organizationService.getDispatchers({
			onSuccess: (dispatchers: UserModel[]): void => {
				this.dispatchers = dispatchers;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onAddDispatcher(): void {
		this.dispatcherWindowMode = "add";
		this.dispatcherWindowVisible = true;
	}

	public onEditDispatcher(): void {
		this.dispatcherWindowMode = "edit";
		this.dispatcherWindowDispatcherId = this.selectedDispatcher.id;
		this.dispatcherWindowVisible = true;
	}

	public onDeleteDispatcher(): void {
		this.confirmationUtils.confirm("CONFIRM_DELETE_DISPATCHER", {
			approve: (): void => {
				this.organizationService.deleteUserById(this.selectedDispatcher.id, {
					onSuccess: (): void => {
						this.messageDisplayer.displaySuccess("DELETED_SUCCESSFULLY");
						this.selectedDispatcher = null;
						this.changePerspective();
						this.loadDispatchers();
					},
					onFailure: (appError: AppError): void => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			reject: (): void => {}
		});
	}

	public getDateForDisplay(date: Date): String {
		return DateUtils.formatForDisplay(date);
	}

	public onDispatcherSelected(event: any): void {
		this.changePerspective();
	}

	public onDispatcherUnselected(event: any): void {
		this.changePerspective();
	}

	private changePerspective(): void {
		this.deleteButtonDisabled = ObjectUtils.isNullOrUndefined(this.selectedDispatcher);
		this.editButtonDisabled = ObjectUtils.isNullOrUndefined(this.selectedDispatcher);
	}

	public onDispatcherWindowClosed(): void {
		this.dispatcherWindowVisible = false;
		this.deleteButtonDisabled = true;
		this.editButtonDisabled = true;
	}

	public onDispatcherWindowDataSaved(): void {
		this.loadDispatchers();
	}

}
