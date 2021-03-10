import { Component, Input, OnInit } from "@angular/core";
import { DocumentTypeModel, ObjectUtils, StringUtils, MessageDisplayer, FolderModel } from "@app/shared";
import { FormBuilder, FormGroup, AbstractControl, Validators } from "@angular/forms";
import { FormControl } from "@angular/forms/src/model";

@Component({
	selector: "app-folder-general-tab-content",
	templateUrl: "./folder-general-tab-content.component.html",
	styleUrls: ["./folder-general-tab-content.component.css"]
})
export class FolderGeneralTabContentComponent implements OnInit {
	
	@Input()
	public mode: "new" | "edit";

	@Input()
	public path: string;

	@Input()
	public documentLocationRealName: string;

	@Input()
	public folderParentId: string;

	@Input()
	public folder: FolderModel;

	private formBuilder: FormBuilder;
	public form: FormGroup;

	public constructor(formBuilder: FormBuilder) {
		this.formBuilder = formBuilder;
		this.init();
	}

	private init(): void {
		this.form = this.formBuilder.group({
			path: [],
			name: ["", Validators.required],
			description: [""],
			documentType: [null]
		});
	}

	public ngOnInit(): void {
		if (this.mode === "new") {
			this.prepareForAdd();
		} else if (this.mode === "edit") {
			this.prepareForEdit();
		}
	}

	public prepareForAdd(): void {
		this.getControlByName("path").setValue(this.path);
	}

	public prepareForEdit(): void {
		this.getControlByName("path").setValue(this.path);
		this.getControlByName("name").setValue(this.folder.name);
		this.getControlByName("description").setValue(this.folder.description);
		this.getControlByName("documentType").setValue(this.folder.documentTypeId);
	}

	public get folderNameFormControl(): AbstractControl {
		return this.getControlByName("name");
	}

	private getControlByName(controlName: string): AbstractControl {
		return this.form.controls[controlName];
	}

	public populateFolder(folder: FolderModel): void {
		if (ObjectUtils.isNotNullOrUndefined(this.folder)) {
			folder.id = this.folder.id;
		}
		folder.name = this.getControlByName("name").value;
		folder.description = this.getControlByName("description").value;
		folder.documentTypeId = this.getControlByName("documentType").value;
		folder.documentLocationRealName = this.documentLocationRealName;
		folder.parentId = this.folderParentId;
	}

	public isValid(): boolean {
		if (!this.form.valid) {
			this.folderNameFormControl.markAsTouched();
		}
		return this.form.valid;
	}
}
