import { WorkflowModel } from "@app/shared";
import { Input, OnInit } from "@angular/core";

export abstract class WorkflowTabContent implements OnInit{

	@Input()
	public mode: "add" | "edit";

	@Input()
	public inputData: WorkflowModel;

	public ngOnInit(): void {
		this.doWhenNgOnInit();
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isEdit()) {
			this.prepareForEdit();
		}
	}

	protected isAdd(): boolean {
		return (this.mode === "add");
	}

	protected isEdit(): boolean {
		return (this.mode === "edit");
	}

	protected abstract doWhenNgOnInit(): void;

	protected abstract reset(): void;
	
	protected abstract prepareForAdd(): void;

	protected abstract prepareForEdit(): void;

	protected abstract populateForSave(workflowModel: WorkflowModel): void;

	protected abstract isValid(): boolean;
}