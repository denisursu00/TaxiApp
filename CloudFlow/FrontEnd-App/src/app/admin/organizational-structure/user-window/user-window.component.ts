import { Component, Output, EventEmitter, Input, OnInit } from "@angular/core";
import { FormBuilder, FormControl, FormGroup, Validators, AbstractControl } from "@angular/forms";
import { OrganizationTreeNodeModel, AppError, OrganizationService, MessageDisplayer, UserModel, StringUtils, DirectoryUserModel, UiUtils, StringValidators, FormUtils, BooleanUtils, RoleModel, NomenclatorService, UserTypeEnum } from "@app/shared";
import { NomenclatorConstants, NomenclatorValueModel, ArrayUtils, BaseWindow } from "@app/shared";
import { SelectItem, Dialog } from "primeng/primeng";

@Component({
	selector: "app-user-window",
	templateUrl: "./user-window.component.html"
})
export class UserWindowComponent extends BaseWindow implements OnInit {

	@Input()
	private mode: "add" | "edit" | "import";

	@Input()
	private userId: string = null;

	@Input()
	private directoryUser: DirectoryUserModel = null;

	@Input()
	private organizationId: string = null;
	
	@Input()
	private organizationUnitId: string = null;
	
	@Output()
	private windowClosed: EventEmitter<void>;

	private organizationService: OrganizationService;
	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;
	private formBuilder: FormBuilder;
	public form: FormGroup;
	private selectedUser: UserModel;

	public windowVisible: boolean = false;
	public addButtonVisible: boolean;
	public saveButtonVisible: boolean;
	public importButtonVisible: boolean;

	public sourceUserRoles: RoleModel[];
	public targetUserRoles: RoleModel[] = [];

	public titleSelectItems: SelectItem[] = [];

	public constructor(organizationService: OrganizationService, nomenclatorService: NomenclatorService, 
			messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		super();
		this.organizationService = organizationService;
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.windowClosed = new EventEmitter<void>();
		this.init();
	}

	private init(): void {
		this.openWindow();
		this.prepareForm();
	}

	private openWindow(): void {
		this.windowVisible = true;
	}

	private prepareForm(): void {

		this.form = this.formBuilder.group([]);

		this.form.addControl("firstName", new FormControl(null, [Validators.required, StringValidators.blank()]));
		this.form.addControl("lastName", new FormControl(null, [Validators.required, StringValidators.blank()]));
		this.form.addControl("titleText", new FormControl(null, [Validators.required, StringValidators.blank()]));
		this.form.addControl("employeeId", new FormControl());
		this.form.addControl("username", new FormControl(null, [Validators.required, StringValidators.blank()]));
		this.form.addControl("password", new FormControl(null, [Validators.required, StringValidators.blank()]));
		this.form.addControl("email", new FormControl(null, [Validators.required, StringValidators.blank(), Validators.email]));
		this.form.addControl("phone", new FormControl());
		this.form.addControl("fax", new FormControl());
		this.form.addControl("mobile", new FormControl());
		this.form.addControl("isManager", new FormControl(false));
	}

	public ngOnInit(): void {
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isEdit()) {
			this.prepareForEdit();
		} else if (this.isImport()) {
			this.prepareForImport();
		}
	}

	private isAdd(): boolean {
		return this.mode === "add";
	}

	private isEdit(): boolean {
		return this.mode === "edit";
	}

	private isImport(): boolean {
		return this.mode === "import";
	}

	public get isPasswordRequired(): boolean {
		if (this.isAdd()) {
			return true;
		}
		return false;
	}

	private prepareForAdd(): void {
		this.addButtonVisible = true;
		this.saveButtonVisible = false;
		this.importButtonVisible = false;
		this.passwordFormControl.setValue(null);
		this.organizationService.getAllRoles(  {
			onSuccess: (roles: RoleModel[]): void => {
				this.sourceUserRoles = roles;
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
		this.prepareTitleSelectItems();
	}

	private prepareForEdit(): void {
		this.getUserById(this.userId);
		this.addButtonVisible = false;
		this.saveButtonVisible = true;
		this.importButtonVisible = false;
		this.passwordFormControl.clearValidators();
		this.passwordFormControl.updateValueAndValidity();
		this.prepareTitleSelectItems();
	}

	private prepareForImport(): void {
		this.addButtonVisible = false;
		this.saveButtonVisible = false;
		this.importButtonVisible = true;
		this.form.setValue({
			firstName: this.directoryUser.firstName,
			lastName: this.directoryUser.lastName,
			titleText: this.directoryUser.title,
			employeeId: this.directoryUser.employeeNumber,
			username: this.directoryUser.username,
			email: this.directoryUser.email,
			isManager: false
		});
	}

	private prepareTitleSelectItems(): void {
		this.nomenclatorService.getNomenclatorValuesByNomenclatorCode(NomenclatorConstants.NOMENCLATOR_CODE_FUNCTII_PERSOANE, {
			onSuccess: (nomenclatorValues: NomenclatorValueModel[]) => {
				if (ArrayUtils.isNotEmpty(nomenclatorValues)) {
					nomenclatorValues.forEach((nv: NomenclatorValueModel) => {
						this.titleSelectItems.push({
							value: nv[NomenclatorConstants.FUNCTII_PERSOANE_ATTR_KEY_DENUMIRE],
							label: nv[NomenclatorConstants.FUNCTII_PERSOANE_ATTR_KEY_DENUMIRE]
						});
					});				
				}
				this.sortTitleSelectItems();
			}, 
			onFailure: (error: AppError) => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private sortTitleSelectItems(): void {
		this.titleSelectItems.sort((cs1: SelectItem, cs2: SelectItem): number => {
			if (cs1.value < cs2.value) {
				return -1;
			}
			if (cs1.value > cs2.value) {
				return 1;
			}
			return 0;
		});
	}

	private getUserById(userId: string): void {
		this.organizationService.getUserById(userId, {
			onSuccess: (user: UserModel): void => {	
				if (user.type != UserTypeEnum.PERSON) {
					this.emailFormControl.clearValidators();
					this.emailFormControl.setValidators([StringValidators.blank(), Validators.email]);
					this.emailFormControl.updateValueAndValidity()
				}

				this.selectedUser = user;
				this.form.setValue({
					firstName: user.firstName,
					lastName: user.lastName,
					titleText: user.title,
					employeeId: user.employeeNumber,
					username: user.userName,
					password: null,
					email: user.email,
					phone: user.phone,
					fax: user.fax,
					mobile: user.mobile,
					isManager: user.isManager
				});
				this.targetUserRoles = user.roles;
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
		this.organizationService.getAvailableRolesForUser( this.userId, {
			onSuccess: (roles: RoleModel[]): void => {
				this.sourceUserRoles = roles;
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
	
	public get firstNameFormControl(): AbstractControl {
		return this.getControlByName("firstName");
	}

	public get lastNameFormControl(): AbstractControl {
		return this.getControlByName("lastName");
	}

	public get titleTextFormControl(): AbstractControl {
		return this.getControlByName("titleText");
	}

	public get employeeIdFormControl(): AbstractControl {
		return this.getControlByName("employeeId");
	}

	public get usernameFormControl(): AbstractControl {
		return this.getControlByName("username");
	}

	public get emailFormControl(): AbstractControl {
		return this.getControlByName("email");
	}

	public get isManagerFormControl(): AbstractControl {
		return this.getControlByName("isManager");
	}

	public get passwordFormControl(): AbstractControl {
		return this.getControlByName("password");
	}

	private getControlByName(controlName: string): AbstractControl {
		return this.form.controls[controlName];
	}

	private closeWindow(): void {
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	public onHide(event: any): void {
		this.windowClosed.emit();
	}

	public onAddAction(event: any): void {
		FormUtils.validateAllFormFields(this.form);
		if (this.form.valid) {
			let user: UserModel = new UserModel();
			user = this.populateUserForSave(user);
			user.type = UserTypeEnum.PERSON;
			this.saveUser(user);
		}
	}

	public onSaveAction(event: any): void {
		FormUtils.validateAllFormFields(this.form);
		if (this.form.valid) {
			this.selectedUser = this.populateUserForSave(this.selectedUser);	
			this.saveUser(this.selectedUser);
		}
	}

	public onImportAction(event: any): void {
		if (this.form.valid) {
			let user: UserModel = new UserModel();
			user = this.populateUserForSave(user);
			this.saveUser(user);
		}
	}

	public onCancelAction(event: any): void {
		this.windowClosed.emit();
	}

	private populateUserForSave(userModel: UserModel): UserModel {
		userModel.firstName = this.form.value.firstName;
		userModel.lastName = this.form.value.lastName;
		userModel.title = this.form.value.titleText;
		userModel.employeeNumber = this.form.value.employeeId;
		userModel.userName = this.form.value.username;
		let password = this.passwordFormControl.value;
		if (StringUtils.isNotBlank(password)) {
			userModel.password = password;
		} else {
			userModel.password = null;
		}
		userModel.email = this.form.value.email;
		userModel.phone = this.form.value.phone;
		userModel.fax = this.form.value.fax;
		userModel.mobile = this.form.value.mobile;
		userModel.isManager = BooleanUtils.isTrue(this.form.value.isManager);
		userModel.name = userModel.firstName + " " + userModel.lastName;
		if (StringUtils.isNotBlank(this.organizationId)) {
			userModel.organizationId = this.organizationId;
		} else if (StringUtils.isNotBlank(this.organizationUnitId)) {
			userModel.organizationUnitId = this.organizationUnitId;
		}
		userModel.roles = [];
		this.targetUserRoles.forEach(element => {
			let role: RoleModel = new RoleModel();
			role.id = element["id"];
			role.name = element["name"];
			role.description = element["description"];
			userModel.roles.push(role);
		});
		return userModel;
	}

	private saveUser(user: UserModel) {
		this.lock();
		this.organizationService.setUser(user, {
			onSuccess: () => {
				this.messageDisplayer.displaySuccess("USER_SAVED");
				this.windowClosed.emit();
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.unlock();
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}
}