import { Component, Output, EventEmitter, Input } from "@angular/core";
import { OrganizationService, MessageDisplayer, UserModel, AppError, DirectoryUserSearchCriteriaModel, DirectoryUserModel, DirectoryUsersModel, StringUtils, UiUtils } from "@app/shared";
import { FormBuilder, FormGroup, AbstractControl } from "@angular/forms";

@Component({
	selector: "app-directory-user-search-window",
	templateUrl: "./directory-user-search-window.component.html",
})
export class DirectoryUserSearchWindowComponent {

	@Input()
	private organizationId: string = null;
	
	@Input()
	private organizationUnitId: string = null;

	@Output()	
	private windowClosed: EventEmitter<void>;

	@Output()	
	private dataSearched: EventEmitter<DirectoryUsersModel>;

	private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;
	private formBuilder: FormBuilder;
	private form: FormGroup;
	private directoryUsersModel: DirectoryUsersModel;

	public windowVisible: boolean = false;
	public width: number;

	public constructor(organizationService: OrganizationService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;	
		this.windowClosed = new EventEmitter<void>();
		this.dataSearched = new EventEmitter<DirectoryUsersModel>();
		this.init();
	}

	private init(): void {
		this.openWindow();
		this.prepareForm();
		this.adjustSize();
	}

	private openWindow(): void {
		this.windowVisible = true;
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			firstName: [""],
			lastName: [""],
			username: [""]
		});
	}

	private adjustSize(): void {
		this.width = UiUtils.getDialogWidth();
	}

	private getControlByName(controlName: string): AbstractControl {
		return this.form.controls[controlName];
	}
	
	private get firstNameFormControl(): AbstractControl {
		return this.getControlByName("firstName");
	}
	
	private get lastNameFormControl(): AbstractControl {
		return this.getControlByName("lastName");
	}
	
	private get usernameFormControl(): AbstractControl {
		return this.getControlByName("username");
	}

	private closeWindow(): void {
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	private onHide(event: any): void {
		this.windowClosed.emit();
	}

	public onCancelAction(event: any): void {
		this.windowClosed.emit();
	}

	public onSearchAction(): void {
		let searchCriteria = this.getDirectoryUserSearchCriteria();
		this.organizationService.findUsersInDirectory(searchCriteria, {
			onSuccess: (directoryUsers: DirectoryUserModel[]) => {
				this.directoryUsersModel = {
					directoryUsers: directoryUsers,
					organizationId: StringUtils.isNotBlank(this.organizationId) ? this.organizationId : null,
					organizationUnitId: StringUtils.isNotBlank(this.organizationUnitId) ? this.organizationUnitId : null
				}
				this.windowClosed.emit();
				this.dataSearched.emit(this.directoryUsersModel);
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		})
	}

	private getDirectoryUserSearchCriteria(): DirectoryUserSearchCriteriaModel {
		let searchCriteria: DirectoryUserSearchCriteriaModel = new DirectoryUserSearchCriteriaModel();
		searchCriteria.firstName = this.form.value.firstName;
		searchCriteria.lastName = this.form.value.lastName;
		searchCriteria.username = this.form.value.username;
		return searchCriteria;
	}
}