import { Component, OnInit, EventEmitter, Output, Input } from "@angular/core";
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";
import { FormUtils, DateConstants, DateUtils, UiUtils, ProjectEstimationModel, ObjectUtils } from "@app/shared";

@Component({
	selector: "app-project-estimation-window",
	templateUrl: "./project-estimation-window.component.html"
})
export class ProjectEstimationWindowComponent implements OnInit {

	@Input()
	public projectId: number;

	@Input()
	public minDate: Date;

	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public dataSaved: EventEmitter<ProjectEstimationModel>;

	private formBuilder: FormBuilder;
	public form: FormGroup;

	public dateFormat: string;
	public yearRange: string;
	
	public windowVisible: boolean = true;

	public width: number;
	public height: string;

	public minEstimationInPercent: number;
	public maxEstimationInPercent: number;

	public constructor(formBuilder: FormBuilder) {
		this.formBuilder = formBuilder;
		this.windowClosed = new EventEmitter<void>();
		this.dataSaved = new EventEmitter<ProjectEstimationModel>();
		this.init();
	}

	private init(): void {
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();

		this.minEstimationInPercent = 0;
		this.maxEstimationInPercent = 100;

		this.adjustSize();

		this.prepareForm();
		this.showWindow();
	}

	public ngOnInit(): void {
		if (ObjectUtils.isNullOrUndefined(this.projectId)) {
			throw new Error("Property [projectId] cannot be null or undefined.");
		}
		if (ObjectUtils.isNullOrUndefined(this.minDate)) {
			throw new Error("Property [minDate] cannot be null or undefined.");
		}
	}

	private adjustSize(): void {
		this.width = UiUtils.getDialogWidth();
		this.height = "auto";
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			estimationInPercent: [null, Validators.required],
			startDate: [null, Validators.required]
		});
	}

	public onSaveAction(): void {
		if (!this.isValid()) {
			return;
		}
		this.dataSaved.emit(this.prepareProjectEstimationModelFromForm());
		this.closeWindow();
	}
	
	public isValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	private prepareProjectEstimationModelFromForm(): ProjectEstimationModel {
		let model: ProjectEstimationModel = new ProjectEstimationModel();
		model.estimationInPercent = Number(this.estimationInPercentFormControl.value);
		model.startDate = new Date(this.startDateFormControl.value);
		model.projectId = this.projectId;
		return model;
	}
	
	public onCancelAction(): void {
		this.closeWindow();
	}
	
	public onHide(event: any): void {
		this.closeWindow();
	}

	private showWindow(): void {
		this.windowVisible = true;
	}

	private closeWindow(): void {
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	public get estimationInPercentFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "estimationInPercent");
	}
	
	public get startDateFormControl(): AbstractControl {
		return FormUtils.getControlByName(this.form, "startDate");
	}
}
