import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { FormBuilder, FormGroup, FormControl } from "@angular/forms";
import { ArrayUtils, ObjectUtils, DiplomaMembruReprezentantiComisieSauGLModel, StringUtils, FormUtils, StringValidators } from "@app/shared";
import { Validators, AbstractControl, ValidatorFn } from "@angular/forms";

@Component({
	selector: "app-diploma-membru-reprezentanti-comisie-sau-gl-window",
	template: `
		<p-dialog [(visible)]="dialogVisible" 
				[modal]="true"
				[maximizable]="true" 
				[closable]="true" 
				[showHeader]="true"
				width="500" 
				appendTo="body"
				(onHide)="onHide($event)">				
			<p-header>{{'LABELS.' + headerLabelCode | translate}}</p-header>
			<form [formGroup]="formGroup">
				<div class="ui-grid ui-grid-responsive ui-grid-pad ui-fluid">
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							{{'LABELS.MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_DIPLOMA_DENUMIRE' | translate}} *:
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<input type="text" formControlName="denumire" pInputText>
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<div *ngIf="denumireFormControl.invalid && (denumireFormControl.dirty || denumireFormControl.touched)" class="ui-message ui-messages-error ui-corner-all">
								<span class="fa fa-close"></span>					
								<span *ngIf="denumireFormControl.errors['required'] || denumireFormControl.errors['blank']">{{'MESSAGES.VALIDATOR_MANDATORY_FIELD' | translate}}</span>
								<span *ngIf="denumireFormControl.errors['duplicated']">{{'MESSAGES.VALIDATOR_DUPLICATED_FIELD' | translate}}</span>
							</div>
						</div>
					</div>
				</div>
				<div class="ui-grid ui-grid-responsive ui-grid-pad ui-fluid">
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							{{'LABELS.MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_DIPLOMA_AN' | translate}} *:
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<input type="text" formControlName="an" pInputText pKeyFilter="pint">
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<div *ngIf="anFormControl.invalid && (anFormControl.dirty || anFormControl.touched)" class="ui-message ui-messages-error ui-corner-all">
								<span class="fa fa-close"></span>					
								<span *ngIf="anFormControl.errors['required'] || anFormControl.errors['blank']">{{'MESSAGES.VALIDATOR_MANDATORY_FIELD' | translate}}</span>
							</div>
						</div>
					</div>
				</div>
				<div class="ui-grid ui-grid-responsive ui-grid-pad ui-fluid">
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							{{'LABELS.MEMBRU_REPREZENTANTI_COMISIE_SAU_GL_DIPLOMA_OBSERVATII' | translate}}:
						</div>
					</div>
					<div class="ui-grid-row">
						<div class="ui-grid-col-12">
							<textarea formControlName="observatii" pInputTextarea></textarea>
						</div>
					</div>
				</div>
			</form>
			<p-footer>
				<p-button (onClick)="onOKAction($event)" [label]="'LABELS.OK' | translate"></p-button>
				<p-button (onClick)="onCancelAction($event)" [label]="'LABELS.CANCEL' | translate"></p-button>
			</p-footer>
		</p-dialog>
	`
})
export class DiplomaMembruReprezentantiComisieSauGLWindowComponent implements OnInit {
	
	@Input()
	public takenDenumiri: string[];

	@Input()
	public diploma: DiplomaMembruReprezentantiComisieSauGLModel;

	@Output()
	private saved: EventEmitter<DiplomaMembruReprezentantiComisieSauGLModel>;

	@Output()
	private closed: EventEmitter<void>;

	public dialogVisible: boolean;
	
	public headerLabelCode: string;

	private formBuilder: FormBuilder;
	public formGroup: FormGroup;

	public constructor(formBuilder: FormBuilder) {
		this.formBuilder = formBuilder;
		this.dialogVisible = true;
		this.saved = new EventEmitter();
		this.closed = new EventEmitter();
	}

	public ngOnInit(): void {
		
		this.headerLabelCode = ObjectUtils.isNullOrUndefined(this.diploma) ? "ADD" : "EDIT";
		
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("denumire", new FormControl(null, [Validators.required, StringValidators.blank(), StringValidators.duplicated(this.takenDenumiri)]));
		this.formGroup.addControl("an", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("observatii", new FormControl());
		
		if (ObjectUtils.isNotNullOrUndefined(this.diploma)) {
			this.formGroup.patchValue({
				denumire: this.diploma.denumire,
				an: this.diploma.an,
				observatii: this.diploma.observatii
			});
		}
	}

	public onOKAction(event: any): void {
		
		FormUtils.validateAllFormFields(this.formGroup);
		if (this.formGroup.invalid) {
			return;
		}
		
		let diploma: DiplomaMembruReprezentantiComisieSauGLModel = new DiplomaMembruReprezentantiComisieSauGLModel();
		if (ObjectUtils.isNotNullOrUndefined(this.diploma)) {
			diploma.id = this.diploma.id;
		}
		diploma.denumire = this.denumireFormControl.value;
		diploma.an = this.anFormControl.value;
		diploma.observatii = this.observatiiFormControl.value;
		
		this.saved.emit(diploma);
		this.dialogVisible = false;	
	}

	public onCancelAction(event: any): void {
		this.dialogVisible = false;
	}

	public onHide(event: any): void {
		this.closed.emit();
	}
	
	public get denumireFormControl(): FormControl {
		return <FormControl> this.formGroup.get("denumire");
	}

	public get anFormControl(): FormControl {
		return <FormControl> this.formGroup.get("an");
	}

	public get observatiiFormControl(): FormControl {
		return <FormControl> this.formGroup.get("observatii");
	}
}