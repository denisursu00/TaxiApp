import { Component, OnInit, Output, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { ProjectService } from "../../service";
import { MessageDisplayer } from "../../message-displayer";
import { SelectItem } from "primeng/primeng";
import { ProjectModel, AppError } from "../../model";

@Component({
	selector: "app-user-projects-selector",
	templateUrl: "./user-projects-selector.component.html",
	providers: [{
		provide: NG_VALUE_ACCESSOR,
		useExisting: UserProjectsSelectorComponent,
		multi: true
	}]
})
export class UserProjectsSelectorComponent implements ControlValueAccessor, OnInit {

	@Output()
	public selectionChanged: EventEmitter<number>;

	private projectService: ProjectService;
	private messageDisplayer: MessageDisplayer;
	
	public projectSelectItems: SelectItem[];
	public selectedProjectId: number;

	public isDropdownDisabled: boolean = false;

	private onChange = (projectId: number) => {};
	private onTouche = () => {};

	public constructor(projectService: ProjectService, messageDisplayer: MessageDisplayer) {
		this.projectService = projectService;
		this.messageDisplayer = messageDisplayer;
		this.selectionChanged = new EventEmitter<number>();
	}

	public ngOnInit(): void {
		this.prepareProjectSelectItems();
	}

	private prepareProjectSelectItems(): void {
		this.projectSelectItems = [];
		this.projectService.getUserProjects({
			onSuccess: (projects: ProjectModel[]): void => {
				projects.forEach((project: ProjectModel) => {
					this.projectSelectItems.push({label: project.name, value: project.id});
				});
				this.projectSelectItems = this.projectSelectItems.sort(function (a, b) {
					return a.label.localeCompare(b.label);
				  });
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	public onProjectChanged(): void {
		this.propagateValue();
		this.selectionChanged.emit(this.selectedProjectId);
	}
	
	private propagateValue(): void {
		this.onChange(this.selectedProjectId);
		this.onTouche();
	}

	public writeValue(projectId: number): void {
		this.selectedProjectId = projectId;
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}
	
	public registerOnTouched(fn: any): void {
		this.onTouche = fn;
	}
	
	public setDisabledState(isDisabled: boolean): void {
		this.isDropdownDisabled = isDisabled;
	}

}
