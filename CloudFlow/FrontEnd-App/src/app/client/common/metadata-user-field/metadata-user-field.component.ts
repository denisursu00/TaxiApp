import { Component, Input, Output, OnInit, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { AppError, MessageDisplayer, UserModel, MetadataDefinitionModel, OrganizationService, ArrayUtils, AclService, SecurityManagerModel, ObjectUtils } from "@app/shared";
import { SelectItem } from "primeng/primeng";

@Component({
	selector: "app-metadata-user-field",
	template: `
		<span *ngIf="loading">{{'MESSAGES.LOADING' | translate}}</span>
		<p-dropdown *ngIf="!loading" 
			[options]="selectItems" 
			[(ngModel)]="selectedItemValue"
			[style]="{'width':'100%'}" 
			[placeholder]="'LABELS.SELECT' | translate" 
			[readonly]="readonly" 
			[editable]="false"
			(onChange)="onValueChanged($event)" 
			(onBlur)="onBlured($event)"
			filter="true"
			appendTo="body">
		</p-dropdown>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: MetadataUserFieldComponent, multi: true }
	]
})
export class MetadataUserFieldComponent implements ControlValueAccessor, OnInit {

	@Input()
	public metadataDefinition: MetadataDefinitionModel;

	@Input()
	public readonly: boolean;

	@Output()
	private valueChanged: EventEmitter<string>;

	private innerValue: string;

	public selectItems: SelectItem[];
	public selectedItemValue: string;

	public loading: boolean = true;

	private organizationService: OrganizationService;
	private aclService: AclService;
	private messageDisplayer: MessageDisplayer;

	private itemsLoaded: boolean;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor(organizationService: OrganizationService, aclService: AclService, messageDisplayer: MessageDisplayer) {
		this.organizationService = organizationService;
		this.aclService = aclService;
		this.messageDisplayer = messageDisplayer;
		this.valueChanged = new EventEmitter();
		this.itemsLoaded = false;
	}

	public ngOnInit(): void {
		this.loadSelectItems();
	}

	private loadSelectItems(): void {
		this.selectItems = [];
		if (!this.metadataDefinition.mandatory) {
			this.selectItems.push({
				value: null,
				label: null
			});
		}
		if (this.metadataDefinition.onlyUsersFromGroup) {
			this.loadSelectItemsFromGroup();
		} else {
			this.loadSelectItemsFromAllUsers();
		}
	}

	private loadSelectItemsFromAllUsers(): void {
		this.organizationService.getSortedUsers({
			onSuccess: (users: UserModel[]): void => {
				this.prepareSelectItemsFromUserModels(users);
				this.loading = false;
				this.itemsLoaded = true;
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
				this.loading = false;
				this.itemsLoaded = true;
			}
		});
	}

	private prepareSelectItemsFromUserModels(userModels: UserModel[]): void {
		this.selectItems = [];
		if (ArrayUtils.isNotEmpty(userModels)) {
			userModels.forEach((user: UserModel) => {
				let selectItem: SelectItem = {
					value: user.userId,
					label: user.displayName
				};
				this.selectItems.push(selectItem);
			});	
		}
		this.establishValue();	
	}

	private establishValue(): void {
		this.selectedItemValue = this.innerValue;		
		if (ObjectUtils.isNullOrUndefined(this.innerValue)) {
			return;
		}		
		let foundInItems: boolean = false;
		if (ArrayUtils.isNotEmpty(this.selectItems)) {
			this.selectItems.forEach((item: SelectItem) => {
				if (item.value === this.innerValue) {
					foundInItems = true;
				}
			});			
		}
		if (!foundInItems) {
			this.innerValue = null;
			this.selectedItemValue = null;
			this.propagateValue();			
		}
	}

	private loadSelectItemsFromGroup(): void {
		this.organizationService.getUsersFromGroup(this.metadataDefinition.idOfGroupOfPermittedUsers, {
			onSuccess: (users: UserModel[]): void => {
				this.prepareSelectItemsFromUserModels(users);
				this.loading = false;
				this.itemsLoaded = true;
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
				this.loading = false;
				this.itemsLoaded = true;
			}
		});
	}

	public onValueChanged(event: any): void {
		this.innerValue = event.value;
		this.propagateValue();
		this.valueChanged.emit(this.innerValue);
	}

	public onBlured(event: any): void {
		this.propagateValue();
	}

	private propagateValue(): void {
		this.onChange(this.innerValue);
		this.onTouched();
	}

	public writeValue(value: string): void {
		this.innerValue = value;
		this.selectedItemValue = this.innerValue;
		if (this.itemsLoaded) {
			this.establishValue();
		}		
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}