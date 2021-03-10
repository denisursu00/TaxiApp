import { Component } from "@angular/core";
import { WorkflowTabContent } from "../workflow-tab-content";
import { WorkflowModel, DocumentTypeModel, DocumentTypeService, AppError, MessageDisplayer, FormUtils, ArrayUtils } from "@app/shared";
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";

@Component({
	selector: "app-workflow-general-tab-content",
	templateUrl: "./workflow-general-tab-content.component.html",
	styleUrls: ["./workflow-general-tab-content.component.css"]
})
export class WorkflowGeneralTabContentComponent extends WorkflowTabContent {
	
	private documentTypeService: DocumentTypeService;
	private messageDisplayer: MessageDisplayer;
	private formBuilder: FormBuilder;

	public form: FormGroup;
	public sourceDocumentTypes: DocumentTypeModel[];

	public constructor(documentTypeService: DocumentTypeService, messageDisplayer: MessageDisplayer, formBuilder: FormBuilder) {
		super();
		this.documentTypeService = documentTypeService;
		this.messageDisplayer = messageDisplayer;
		this.formBuilder = formBuilder;
		this.init();
	}

	private init(): void {
		this.sourceDocumentTypes = [];
		this.prepareForm();
	}

	private prepareForm(): void {
		this.form = this.formBuilder.group({
			name: ["", Validators.required],
			description: [""],
			version: [null],
			documentTypes: [[], Validators.required]
		});
	}

	private loadDocumentTypesWithNoWorkflow(): void {
		this.documentTypeService.getDocumentTypesWithNoWorkflow({
			onSuccess: (documentTypes: DocumentTypeModel[]): void => {
				this.sourceDocumentTypes = [...this.sourceDocumentTypes, ...documentTypes];
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	protected doWhenNgOnInit(): void {
		this.loadDocumentTypesWithNoWorkflow();
	}

	public prepareForAdd(): void {
	}

	public prepareForEdit(): void {
		this.populateForm(this.inputData);
	}

	private populateForm(workflow: WorkflowModel): void {
		this.workflowNameFormControl.setValue(workflow.name);
		this.workflowDescriptionFormControl.setValue(workflow.description);
		this.workflowVersionFormControl.setValue(workflow.versionNumber);
		this.workflowDocumentTypesFormControl.setValue(workflow.documentTypes);
	}

	public populateForSave(workflowModel: WorkflowModel): void {
		workflowModel.name = this.workflowNameFormControl.value;
		workflowModel.description = this.workflowDescriptionFormControl.value;
		workflowModel.documentTypes = this.workflowDocumentTypesFormControl.value;
	}

	public isValid(): boolean {
		FormUtils.validateAllFormFields(this.form);
		return this.form.valid;
	}

	public reset(): void {
	}

	public getDocumentTypes(): DocumentTypeModel[] {
		return this.workflowDocumentTypesFormControl.value;
	}

	private getControlByName(name: string): AbstractControl {
		return this.form.controls[name];
	}

	public get workflowNameFormControl(): AbstractControl {
		return this.getControlByName("name");
	}

	public get workflowDescriptionFormControl(): AbstractControl {
		return this.getControlByName("description");
	}

	public get workflowVersionFormControl(): AbstractControl {
		return this.getControlByName("version");
	}

	public get workflowDocumentTypesFormControl(): AbstractControl {
		return this.getControlByName("documentTypes");
	}
}
