import { Component, OnInit, Output, EventEmitter, Input } from "@angular/core";
import { NG_VALUE_ACCESSOR, ControlValueAccessor } from "@angular/forms";
import { GroupModel, GroupService, MessageDisplayer, AppError, ArrayUtils, ObjectUtils, MetadataDefinitionModel } from "@app/shared";
import { SelectItem } from "primeng/primeng";

@Component({
	selector: "app-metadata-group-field",
	template: `
		<span *ngIf="loading">{{'MESSAGES.LOADING' | translate}}</span>
		<p-dropdown *ngIf="!loading"
			[options]="groupSelectItems" 
			[(ngModel)]="selectedGroup"
			filter="true"
			[style]="{'width':'100%'}"
			autoDisplayFirst="false"
			[placeholder]="'LABELS.SELECT' | translate" 
			[readonly]="readonly" 
			(onChange)="onValueChanged($event)" 
			filter="true"
			(onBlur)="onSelectionBlured($event)"
			appendTo="body">
		</p-dropdown>
	`,
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: MetadataGroupFieldComponent, multi: true }
	]
})
export class MetadataGroupFieldComponent implements ControlValueAccessor, OnInit {

	@Input()
	public metadataDefinition: MetadataDefinitionModel;

	@Input()
	public readonly: boolean;
	
	@Output()
	public valueChanged: EventEmitter<string>;

	public loading: boolean;

	public groups: GroupModel[];
	public selectedGroup: number;
	public groupSelectItems: SelectItem[];

	private groupService: GroupService;
	private messageDisplayer: MessageDisplayer;

	private fieldValue: string;

	private onChange: any = () => {};
	private onTouched: any = () => {};

	public constructor(groupService: GroupService, messageDisplayer: MessageDisplayer) {
		this.groupService = groupService;
		this.messageDisplayer = messageDisplayer;
		this.valueChanged = new EventEmitter();
		this.loading = true;
	}

	public ngOnInit(): void {
		this.loadGroups();
	}

	private loadGroups(): void {
		this.groupSelectItems = [];
		if (!this.metadataDefinition.mandatory) {
			this.groupSelectItems.push({
				value: null,
				label: null
			});
		}
		this.groupService.getGroups({
			onSuccess: (groups: GroupModel[]) => {
				this.groups = groups;
				this.prepareGroupSelectItems(groups);
				this.selectGroup();
				this.loading = false;
			},
			onFailure: (appError: AppError) => {
				this.messageDisplayer.displayAppError(appError);
			}
		});	
	}
	
	private prepareGroupSelectItems(groups: GroupModel[]): void {
		groups.forEach((group: GroupModel) => {
			this.groupSelectItems.push({value: Number(group.id), label: group.name});
		});
	}

	private selectGroup(): void {
		if (ArrayUtils.isEmpty(this.groups)) {
			return;
		}
		if (ObjectUtils.isNullOrUndefined(this.fieldValue)) {
			return;
		}
		this.groups.forEach((group: GroupModel) => {
			if (this.fieldValue === group.id) {
				this.selectedGroup = Number(group.id);
			}
		});
	}

	private propagateValue(): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedGroup)) {
			this.fieldValue = null;
		} else {
			this.fieldValue = String(this.selectedGroup);
		}
		
		this.onChange(this.fieldValue);
		this.onTouched();
	}

	public onValueChanged(event: any): void {
		this.propagateValue();
		this.valueChanged.emit(this.fieldValue);
	}

	public onSelectionBlured(event: any): void {
		this.onTouched();
	}

	public writeValue(groupId: string): void {
		this.fieldValue = groupId;
		
		this.selectGroup();
	}
	
	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}