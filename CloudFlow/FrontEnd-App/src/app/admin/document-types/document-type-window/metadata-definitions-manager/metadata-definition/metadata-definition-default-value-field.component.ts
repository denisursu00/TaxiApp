import { Component, Input, OnInit, OnChanges, SimpleChanges} from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { AppError, TranslateUtils, DateConstants, ArrayUtils, ObjectUtils, StringUtils, MetadataTypeConstants, GroupModel, GroupService, CalendarModel, CalendarService } from "@app/shared";
import { ValueOfNomenclatorValueField, DateUtils } from "@app/shared";
import { MetadataDefinitionModel, ListMetadataItemModel, UserModel, OrganizationService, MessageDisplayer } from "@app/shared";
import { calendarFormat } from "moment";

@Component({
	selector: "app-metadata-definition-default-value-field",
	template: `
		<input *ngIf="metadataType === 'TEXT'"
			type="text"
			[(ngModel)]="valueAsString"
			pInputText
			(input)="onValueChanged($event)"
			(blur)="onValueBlured($event)">

		<textarea *ngIf="metadataType === 'TEXT_AREA'"
			[(ngModel)]="valueAsString"
			pInputTextarea
			(input)="onValueChanged($event)"
			(blur)="onValueBlured($event)">
		</textarea>

		<input *ngIf="metadataType === 'NUMERIC'"
			type="text"
			[(ngModel)]="valueAsString"
			pInputText
			pKeyFilter="num"
			(input)="onValueChanged($event)"
			(blur)="onValueBlured($event)">

		<p-calendar *ngIf="metadataType === 'DATE'"
			[(ngModel)]="valueAsDate"
			[showIcon]="true" 
			[readonlyInput]="false" 
			[dateFormat]="dateFormat" 
			[appendTo]="body" 
			utc="false"
			[monthNavigator]="true" 
			[yearNavigator]="true"
			[yearRange]="yearRange" 
			[showButtonBar]="true" 
			[dataType]="'date'"
			(onSelect)="onValueChanged($event)"
			(onBlur)="onValueBlured($event)">
		</p-calendar>

		<p-calendar *ngIf="metadataType === 'DATE_TIME'"
			[(ngModel)]="valueAsDate"
			[showIcon]="true" 
			[readonlyInput]="false" 
			[dateFormat]="dateTimeFormat"
			[showTime]="true"
			hourFormat="24"
			[appendTo]="body" 
			utc="false"
			[monthNavigator]="true" 
			[yearNavigator]="true" 
			[yearRange]="yearRange"
			[showButtonBar]="true" 
			[dataType]="'date'"
			(onSelect)="onValueChanged($event)"
			(onBlur)="onValueBlured($event)">
		</p-calendar>

		<p-calendar *ngIf="metadataType === 'MONTH'"
			[(ngModel)]="valueAsDate" 
			view="month" 
			[dateFormat]="monthFormat" 
			[yearNavigator]="true" 
			[yearRange]="yearRange"
			[showButtonBar]="true" 
			[dataType]="'date'"
			(onSelect)="onValueChanged($event)"
			(onBlur)="onValueBlured($event)">
		</p-calendar>

		<p-dropdown *ngIf="metadataType === 'USER'"
			[(ngModel)]="valueAsUser"
			[options]="users"
			autoDisplayFirst="false"
			[placeholder]="'LABELS.SELECT' | translate"
			optionLabel="displayName"
			showClear="true"
			[style]="{'width':'100%'}"
			appendTo="body"
			filter="true"
			(onChange)="onValueChanged($event)">
		</p-dropdown>

		<app-nomenclator-value-field *ngIf="metadataType === 'NOMENCLATOR'"
			[(ngModel)]="valueAsNomenclatorValue"
			(valueChanged)="onNomenclatorValueChanged($event)">
		</app-nomenclator-value-field>

		<p-dropdown *ngIf="metadataType === 'LIST'"
			[(ngModel)]="valueAsListItem"
			[options]="listItems"
			autoDisplayFirst="false"
			[placeholder]="'LABELS.SELECT' | translate"
			optionLabel="label"
			showClear="true"
			[style]="{'width':'100%'}"
			appendTo="body"
			filter="true"
			(onChange)="onValueChanged($event)">
		</p-dropdown>

		<p-dropdown *ngIf="metadataType === 'GROUP'"
			[(ngModel)]="valueAsGroup"
			[options]="groups"
			autoDisplayFirst="false"
			[placeholder]="'LABELS.SELECT' | translate"
			optionLabel="name"
			showClear="true"
			[style]="{'width':'100%'}"
			appendTo="body"
			filter="true"
			(onChange)="onValueChanged($event)">
		</p-dropdown>

		<app-calendar-selection-field *ngIf="metadataType === 'CALENDAR'"
			[(ngModel)]="valueAsNumber"
			(selectionChanged)="onValueChanged($event)">
		</app-calendar-selection-field>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: MetadataDefinitionDefaultValueFieldComponent, multi: true }
	]
})
export class MetadataDefinitionDefaultValueFieldComponent implements ControlValueAccessor, OnChanges {

	public metadataType: string;
	public nomenclatorId: number;
	public listItems: ListMetadataItemModel[];
	public users: UserModel[];
	public cachedUsers: object; // Un cache per app ar fi mai ok.
	public groups: GroupModel[];

	public dateFormat: string;
	public dateTimeFormat: string;
	public monthFormat: string;
	public yearRange: string;

	public valueAsString: string;
	public valueAsDate: Date;
	public valueAsUser: UserModel;
	public valueAsNomenclatorValue: ValueOfNomenclatorValueField;
	public valueAsListItem: ListMetadataItemModel;
	public valueAsGroup: GroupModel;
	public valueAsNumber: number;

	private fieldValue: ValueOfMetadataDefaultValueField;

	private organizationService: OrganizationService;
	private groupService: GroupService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor(organizationService: OrganizationService, translateUtils: TranslateUtils, 
			messageDisplayer: MessageDisplayer, groupService: GroupService) {
		this.translateUtils = translateUtils;
		this.organizationService = organizationService;
		this.groupService = groupService;
		this.messageDisplayer = messageDisplayer;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.dateTimeFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.monthFormat = DateConstants.MONTH_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.cachedUsers = {};
	}

	public ngOnChanges(simpleChanges: SimpleChanges): void {
		// Nothing now.
	}

	public onValueChanged(event: any): void {
		this.prepareAndPropagateFieldValue();
	}

	public onValueBlured(event: any): void {
		this.prepareAndPropagateFieldValue();
	}

	private populateAllUsers(callback: () => any): void {
		if (ArrayUtils.isNotEmpty(this.cachedUsers["all"])) {
			this.users = this.cachedUsers["all"];
			callback();
		} else {
			this.organizationService.getUsers({
				onSuccess: (users: UserModel[]): void => {
					this.users = users;
					this.cachedUsers["all"] = users;
					callback();
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
				}
			});
		}
	}

	private populateAllGroups(callback: () => any): void {
		this.groupService.getGroups({
			onSuccess: (groups: GroupModel[]): void => {
				this.groups = groups;
				callback();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private populateUsersFromGroup(groupId: string, callback: () => any): void {
		if (ArrayUtils.isNotEmpty(this.cachedUsers[groupId])) {
			this.users = this.cachedUsers[groupId];
			callback();
		} else {
			this.organizationService.getUsersFromGroup(groupId, {
				onSuccess: (users: UserModel[]): void => {
					this.users = users;
					this.cachedUsers[groupId] = users;
					callback();
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
				}
			});
		}
	}

	private prepareAndPropagateFieldValue(): void {
		this.prepareFieldValue();
		this.propagateFieldValue();
	}

	private prepareFieldValue(): void {	
		this.fieldValue = new ValueOfMetadataDefaultValueField(this.metadataType);
		if (this.metadataType === MetadataDefinitionModel.TYPE_USER) {
			if (ObjectUtils.isNotNullOrUndefined(this.valueAsUser)) {
				this.fieldValue.value = this.valueAsUser.userId;
			}
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_DATE) {
			if (ObjectUtils.isNotNullOrUndefined(this.valueAsDate)) {
				this.fieldValue.value = DateUtils.formatForStorage(this.valueAsDate);
			}
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_DATE_TIME) {
			if (ObjectUtils.isNotNullOrUndefined(this.valueAsDate)) {
				this.fieldValue.value = DateUtils.formatDateTimeForStorage(this.valueAsDate);
			}
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_MONTH) {
			if (ObjectUtils.isNotNullOrUndefined(this.valueAsDate)) {
				this.fieldValue.value = DateUtils.formatMonthYearForStorage(this.valueAsDate);
			}			
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_LIST) {
			if (ObjectUtils.isNotNullOrUndefined(this.valueAsListItem)) {
				this.fieldValue.value = this.valueAsListItem.value;
				this.fieldValue.listItems = this.listItems;
			}
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_NOMENCLATOR) {
			this.fieldValue.nomenclatorId = this.nomenclatorId;
			if (ObjectUtils.isNotNullOrUndefined(this.valueAsNomenclatorValue)) {
				if (ObjectUtils.isNotNullOrUndefined(this.valueAsNomenclatorValue.value)) {
					this.fieldValue.value = this.valueAsNomenclatorValue.value.toString();
				}
			}
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_GROUP) {
			if (ObjectUtils.isNotNullOrUndefined(this.valueAsGroup)) {
				this.fieldValue.value = this.valueAsGroup.id;
			}
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_CALENDAR) {
			if (ObjectUtils.isNotNullOrUndefined(this.valueAsNumber)) {
				this.fieldValue.value = this.valueAsNumber.toString();
			}
		} else {
			this.fieldValue.value = this.valueAsString;
		}
	}

	private propagateFieldValue(): void {
		this.onChange(this.fieldValue);
		this.onTouched();
	}

	private clearSpecificValues(): void {
		this.valueAsString = null;
		this.valueAsUser = null;
		this.valueAsNomenclatorValue = null;
		this.valueAsListItem = null;
		this.metadataType = null;
		this.nomenclatorId = null;
		this.valueAsGroup = null;
		this.valueAsNumber = null;
		this.listItems = [];
	}

	private applySpecificValueFromFieldValue(): void {
		this.clearSpecificValues();
		if (ObjectUtils.isNullOrUndefined(this.fieldValue)) {
			throw new Error("fieldValue cannot be null here");
		}
		if (ObjectUtils.isNullOrUndefined(this.fieldValue.metadataType)) {
			throw new Error("metadataType cannot be null");
		}
		this.metadataType = this.fieldValue.metadataType;
		
		if (this.metadataType === MetadataDefinitionModel.TYPE_USER) {
			if (StringUtils.isNotBlank(this.fieldValue.value) && ArrayUtils.isNotEmpty(this.users)) {
				this.users.forEach((user: UserModel) => {
					if (user.userId === this.fieldValue.value) {
						this.valueAsUser = user;
					}
				}); 
			}
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_DATE) {
			if (StringUtils.isNotBlank(this.fieldValue.value)) {
				this.valueAsDate = DateUtils.parseFromStorage(this.fieldValue.value);
			}
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_DATE_TIME) {
			if (StringUtils.isNotBlank(this.fieldValue.value)) {
				this.valueAsDate = DateUtils.parseDateTimeFromStorage(this.fieldValue.value);
			}
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_MONTH) {
			if (StringUtils.isNotBlank(this.fieldValue.value)) {
				this.valueAsDate = DateUtils.parseMonthYearFromStorage(this.fieldValue.value);
				
			}			
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_LIST) {
			if (ArrayUtils.isNotEmpty(this.fieldValue.listItems)) {
				this.listItems = [];
				this.fieldValue.listItems.forEach((listItem: ListMetadataItemModel) => {
					this.listItems.push(listItem.clone());
					if (listItem.value === this.fieldValue.value) {
						this.valueAsListItem = listItem;
					}
				});
			}
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_NOMENCLATOR) {
			this.nomenclatorId = this.fieldValue.nomenclatorId;
			if (ObjectUtils.isNotNullOrUndefined(this.nomenclatorId)) {
				this.valueAsNomenclatorValue = new ValueOfNomenclatorValueField(this.nomenclatorId);
				if (StringUtils.isNotBlank(this.fieldValue.value)) {
					this.valueAsNomenclatorValue.value = parseInt(this.fieldValue.value, 0);
				}
			}						
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_GROUP) {
			if (StringUtils.isNotBlank(this.fieldValue.value) && ArrayUtils.isNotEmpty(this.groups)) {
				this.groups.forEach((group: GroupModel) => {
					if (group.id === this.fieldValue.value) {
						this.valueAsGroup = group;
					}
				}); 
			}
		} else if (this.metadataType === MetadataDefinitionModel.TYPE_CALENDAR) {
			if (StringUtils.isNotBlank(this.fieldValue.value)) {
				this.valueAsNumber = StringUtils.toNumber(this.fieldValue.value);
			}
		} else {
			this.valueAsString = this.fieldValue.value;
		}
	}

	public onNomenclatorValueChanged(valueOfNomenclatorValueField: ValueOfNomenclatorValueField): void {
		this.prepareAndPropagateFieldValue();
	}

	public writeValue(value: ValueOfMetadataDefaultValueField): void {
		this.fieldValue = value;
		if (ObjectUtils.isNullOrUndefined(this.fieldValue)) {
			this.clearSpecificValues();
			return;
		}		
		if (this.fieldValue.metadataType === MetadataDefinitionModel.TYPE_USER) {
			if (StringUtils.isBlank(this.fieldValue.idOfGroupOfPermittedUsers)) {
				this.populateAllUsers(() => {
					this.applySpecificValueFromFieldValue();
				});
			} else {
				this.populateUsersFromGroup(this.fieldValue.idOfGroupOfPermittedUsers, () => {
					this.applySpecificValueFromFieldValue();
				});
			}			
		} else if (this.fieldValue.metadataType === MetadataDefinitionModel.TYPE_GROUP) {
			this.populateAllGroups(() => {
				this.applySpecificValueFromFieldValue();
			});
		} else {
			this.applySpecificValueFromFieldValue();
		}	
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}

export class ValueOfMetadataDefaultValueField {

	public value: string;
	private _metadataType: string;

	public nomenclatorId: number;              // NOMENCLATOR
	public listItems: ListMetadataItemModel[]; // LIST
	public idOfGroupOfPermittedUsers: string;  // USER

	public constructor(metadataType: string) {
		if (StringUtils.isBlank(metadataType)) {
			throw new Error("metadataType cannot be blank");
		}
		this._metadataType = metadataType;
		
	}

	public get metadataType() {
		return this._metadataType;
	}
}