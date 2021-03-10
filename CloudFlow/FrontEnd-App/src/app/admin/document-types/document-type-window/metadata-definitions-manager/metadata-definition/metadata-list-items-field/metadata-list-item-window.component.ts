import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { FormBuilder, FormGroup, FormControl } from "@angular/forms";
import { ArrayUtils, ObjectUtils, DocumentTemplateModel, ListMetadataItemModel, StringUtils, FormUtils } from "@app/shared";
import { Validators, AbstractControl, ValidatorFn } from "@angular/forms";
import { StringValidators } from "@app/shared/validators/index";

@Component({
	selector: "app-metadata-list-item-window",
	template: `
		<p-dialog [(visible)]="windowVisible" 
				[modal]="true"
				[maximizable]="true" 
				[closable]="true" 
				[showHeader]="true"
				width="450" 
				appendTo="body"
				(onHide)="onHide($event)">				
			<p-header>{{'LABELS.' + headerLabelCode | translate}}</p-header>
			<form [formGroup]="formGroup">
				<div class="ui-grid ui-grid-responsive ui-grid-pad ui-fluid">
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							{{'LABELS.LABEL' | translate}} *:
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<input type="text" formControlName="label" pInputText>
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<div class="ui-message ui-messages-error ui-corner-all" *ngIf="labelFormControl.invalid && (labelFormControl.dirty || labelFormControl.touched)">
								<span class="fa fa-close"></span>					
								<span *ngIf="labelFormControl.errors['required'] || labelFormControl.errors['blank']">{{'MESSAGES.VALIDATOR_MANDATORY_FIELD' | translate}}</span>
								<span *ngIf="labelFormControl.errors['duplicated']">{{'MESSAGES.VALIDATOR_DUPLICATED_FIELD' | translate}}</span>
							</div>
						</div>
					</div>
				</div>
				<div class="ui-grid ui-grid-responsive ui-grid-pad ui-fluid">
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							{{'LABELS.VALUE' | translate}} *:
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<input type="text" formControlName="value" pInputText>
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<div class="ui-message ui-messages-error ui-corner-all" *ngIf="valueFormControl.invalid && (valueFormControl.dirty || valueFormControl.touched)">
								<span class="fa fa-close"></span>					
								<span *ngIf="valueFormControl.errors['required'] || valueFormControl.errors['blank']">{{'MESSAGES.VALIDATOR_MANDATORY_FIELD' | translate}}</span>
								<span *ngIf="valueFormControl.errors['duplicated']">{{'MESSAGES.VALIDATOR_DUPLICATED_FIELD' | translate}}</span>
							</div>
						</div>
					</div>
				</div>
				<div class="ui-grid ui-grid-responsive ui-grid-pad ui-fluid">
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							{{'LABELS.ORDER_NUMBER' | translate}} *:
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<input type="text" formControlName="orderNumber" pInputText pKeyFilter="pint">
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<div class="ui-message ui-messages-error ui-corner-all" *ngIf="orderNumberFormControl.invalid && (orderNumberFormControl.dirty || orderNumberFormControl.touched)">
								<span class="fa fa-close"></span>					
								<span *ngIf="orderNumberFormControl.errors['required'] || orderNumberFormControl.errors['blank']">{{'MESSAGES.VALIDATOR_MANDATORY_FIELD' | translate}}</span>
							</div>
						</div>
					</div>
				</div>
			</form>
			<p-footer>
				<p-button (onClick)="onOKAction($event)" label="OK"></p-button>
				<p-button (onClick)="onCancelAction($event)" [label]="'LABELS.CANCEL' | translate"></p-button>
			</p-footer>
		</p-dialog>
	`
})
export class MetadataListItemWindowComponent implements OnInit {

	@Input()
	public takenValues: string[];

	@Input()
	public takenLabels: string[];

	@Input()
	public suggestedOrderNumber: number;

	@Input()
	public listItem: ListMetadataItemModel;

	@Output()
	private saved: EventEmitter<ListMetadataItemModel>;

	@Output()
	private canceled: EventEmitter<void>;

	public windowVisible: boolean;
	
	public headerLabelCode: string;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public constructor(formBuilder: FormBuilder) {
		this.formBuilder = formBuilder;
		this.windowVisible = true;
		this.saved = new EventEmitter();
		this.canceled = new EventEmitter();
		this.formGroup = this.formBuilder.group([]);
	}
	
	public ngOnInit(): void {
		this.headerLabelCode = ObjectUtils.isNullOrUndefined(this.listItem) ? "ADD" : "EDIT";
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("label", new FormControl(null, [Validators.required, StringValidators.blank(), StringValidators.duplicated(this.takenLabels)]));
		this.formGroup.addControl("value", new FormControl(null, [Validators.required, StringValidators.blank(), StringValidators.duplicated(this.takenValues)]));
		this.formGroup.addControl("orderNumber", new FormControl(null, [Validators.required]));
		if (ObjectUtils.isNotNullOrUndefined(this.listItem)) {
			this.formGroup.patchValue({
				label: this.listItem.label,
				value: this.listItem.value,
				orderNumber: this.listItem.orderNumber
			});
		} else {
			if (ObjectUtils.isNotNullOrUndefined(this.suggestedOrderNumber)) {
				this.formGroup.patchValue({
					orderNumber: this.suggestedOrderNumber
				});
			}
		}
	}

	public onOKAction(event: any): void {
		FormUtils.validateAllFormFields(this.formGroup);
		if (this.formGroup.invalid) {
			return;
		}
		let item: ListMetadataItemModel = new ListMetadataItemModel();
		if (ObjectUtils.isNotNullOrUndefined(this.listItem)) {
			item.id = this.listItem.id;
		}
		item.value = this.valueFormControl.value;
		item.label = this.labelFormControl.value;
		item.orderNumber = parseInt(<string> this.orderNumberFormControl.value, 0);
		this.saved.emit(item);
		this.windowVisible = false;		
	}

	public onCancelAction(event: any): void {
		this.canceled.emit();
		this.windowVisible = false;
	}

	public onHide(event: any): void {
		this.canceled.emit();
	}

	public get valueFormControl(): FormControl {
		return <FormControl> this.formGroup.get("value");
	}

	public get labelFormControl(): FormControl {
		return <FormControl> this.formGroup.get("label");
	}

	public get orderNumberFormControl(): FormControl {
		return <FormControl> this.formGroup.get("orderNumber");
	}
}