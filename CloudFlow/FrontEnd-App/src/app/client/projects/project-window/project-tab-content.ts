import { ProjectModel, ProjectViewModel} from "@app/shared";
import { Input, OnInit } from "@angular/core";

export abstract class ProjectTabContent implements OnInit {
	
	@Input()
	public mode: "add" | "viewOrEdit";

	@Input()
	public project: ProjectViewModel;

	public ngOnInit(): void {
		this.doWhenOnInit();
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isViewOrEdit()) {
			this.prepareForViewOrEdit();
		}
	}

	protected abstract prepareForAdd(): void;
	
	protected abstract prepareForViewOrEdit(): void;

	protected abstract doWhenOnInit(): void;

	public isAdd(): boolean {
		return (this.mode === "add");
	}

	public isViewOrEdit(): boolean {
		return (this.mode === "viewOrEdit");
	}

	protected abstract reset(): void;

	protected abstract populateForSave(project: ProjectModel): void;

	protected abstract isValid(): boolean;
}