import { Component, Input, Output, OnInit, OnChanges, SimpleChanges, EventEmitter } from "@angular/core";
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from "@angular/forms";
import { TranslateUtils, BooleanUtils, ObjectUtils, ArrayUtils, NomenclatorUtils, ProjectService, StringUtils, ProjectModel } from "@app/shared";
import { AppError, MessageDisplayer, MetadataDefinitionModel } from "@app/shared";

@Component({
	selector: "app-metadata-project-field",
	templateUrl: "./metadata-project-field.component.html",
	providers: [
		{ provide: NG_VALUE_ACCESSOR, useExisting: MetadataProjectFieldComponent, multi: true }
	]
})
export class MetadataProjectFieldComponent implements ControlValueAccessor, OnInit {

	private readonly EMPTY_TEXT: string = "";

	@Input()
	public metadataDefinition: MetadataDefinitionModel;

	@Input()
	public readonly: boolean;

	@Output()
	public valueChanged: EventEmitter<number | number[]>;

	private fieldValue: number[];

	public placeholder: string;
	public projectUiValue: string;
	public projectUiValues: MultipleSelectionProjectUiValue[];

	public clearEnabled: boolean;
	public viewDspEnabled: boolean;
	public selectEnabled: boolean;

	private projectService: ProjectService;
	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;

	public projectSelectionWindowVisible: boolean;
	public projectSelectionWindowSelectionMode: string;

	public dspViewWindowVisible: boolean;
	public dspViewWindowProjectId: number;

	private cachedProject: object;

	public selectedProject: MultipleSelectionProjectUiValue;

	private onChange: any = () => { };
	private onTouched: any = () => { };

	public constructor(projectService: ProjectService, messageDisplayer: MessageDisplayer, translateUtils: TranslateUtils) {
		this.projectService = projectService;
		this.messageDisplayer = messageDisplayer;
		this.translateUtils = translateUtils;
		
		this.selectEnabled = false;
		this.clearEnabled = false;
		
		this.placeholder = this.EMPTY_TEXT;
		this.projectUiValue = this.EMPTY_TEXT;
		this.projectUiValues = [];
		this.projectSelectionWindowVisible = false;

		this.cachedProject = {};

		this.valueChanged = new EventEmitter();
	}

	public ngOnInit(): void {
		this.updatePerspective();
	}

	public get singleSelection(): boolean {
		return BooleanUtils.isFalse(this.metadataDefinition.multipleProjectsSelection);
	}

	public get multipleSelection(): boolean {
		return BooleanUtils.isTrue(this.metadataDefinition.multipleProjectsSelection);
	}

	public onSelect(): void {
		this.projectSelectionWindowSelectionMode = "single";
		if (this.multipleSelection) {
			this.projectSelectionWindowSelectionMode = "multiple";
		}
		this.projectSelectionWindowVisible = true;
	}

	public onClear(): void {
		this.clearValue();
	}

	public onViewDsp(): void {
		if (this.singleSelection) {
			this.dspViewWindowProjectId = this.fieldValue[0];
		} else if (this.multipleSelection) {
			this.dspViewWindowProjectId = this.selectedProject.projectId;
		}
		this.dspViewWindowVisible = true;
	}

	private clearValue(): void {
		if (this.singleSelection) {
			this.fieldValue = [];
		} else if (this.multipleSelection) {
			let projectIdToRemove: number = null;
			this.fieldValue.forEach((theProjectId: number) => {
				if (theProjectId === this.selectedProject.projectId) {
					projectIdToRemove = theProjectId;
				}
			});
			ArrayUtils.removeElement(this.fieldValue, projectIdToRemove);
			this.selectedProject = null;
		}
		this.propagateValue();
		this.updatePerspective();
		this.loadProjectUiValues();
	}

	public onProjectSelectionWindowValuesSelected(selectedProjectIdOrIds: number | number[]): void {

		if (ObjectUtils.isNullOrUndefined(selectedProjectIdOrIds)) {
			return;
		}
		this.projectSelectionWindowVisible = false;
		if (this.singleSelection) {
			let selectedProjectId: number = <number> selectedProjectIdOrIds;
			if (this.fieldValueContainsProject(selectedProjectId)) {
				return;
			}
			this.fieldValue = [];
			this.fieldValue.push(selectedProjectId);
		} else if (this.multipleSelection) {
			let newFieldValue: number[] = [...this.fieldValue];
			let selectedProjectIds: number[] = <number[]> selectedProjectIdOrIds;
			let areChanges: boolean = false;
			selectedProjectIds.forEach((selectedProjectId: number) => {
				if (!this.fieldValueContainsProject(selectedProjectId)) {
					newFieldValue.push(selectedProjectId);
					areChanges = true;
				}
			});
			if (!areChanges) {
				return;
			}
			this.fieldValue = newFieldValue;
		} else {
			throw new Error("wrong selection");
		}
		
		this.propagateValue();
		this.updatePerspective();
		this.loadProjectUiValues();
	}

	private fieldValueContainsProject(projectId: number): boolean {
		if (ArrayUtils.isEmpty(this.fieldValue)) {
			return false;
		}
		let found: boolean = false;
		this.fieldValue.forEach((fielValueProjectId: number) => {
			if (fielValueProjectId === projectId) {
				found = true;
			}
		});
		return found;
	}

	private loadProjectUiValues(): void {

		this.projectUiValue = this.EMPTY_TEXT;
		this.placeholder = this.EMPTY_TEXT;
		this.projectUiValues = [];

		if (ArrayUtils.isEmpty(this.fieldValue)) {			
			return;
		}

		this.resolveCachedProject(() => {
			if (this.singleSelection) {				
				if (this.fieldValue.length === 1) {
					let project: ProjectModel = this.cachedProject[this.fieldValue[0]];
					this.projectUiValue = project.name + " (" + project.projectAbbreviation + ")";
				}
			} else if (this.multipleSelection) {
				this.fieldValue.forEach((value: number) => {
					let projectModel: ProjectModel = this.cachedProject[value];
					let projectUiValue: MultipleSelectionProjectUiValue = new MultipleSelectionProjectUiValue();
					projectUiValue.projectAbbreviation = projectModel.projectAbbreviation;
					projectUiValue.projectName = projectModel.name;
					projectUiValue.projectId = projectModel.id;
					this.projectUiValues.push(projectUiValue);
				});
				this.sortProjectUiValues();
			}
		});
	}

	private sortProjectUiValues(): void {
		if (ArrayUtils.isEmpty(this.projectUiValues)) {
			return;
		}
		this.projectUiValues.sort((uiValue1: MultipleSelectionProjectUiValue, uiValue2: MultipleSelectionProjectUiValue): number => {
			if (uiValue1.projectName < uiValue2.projectName) {
				return -1;
			}
			if (uiValue1.projectName > uiValue2.projectName) {
				return 1;
			}
			return 0;
		});
	}

	private resolveCachedProject(callback: () => void): void {

		let projectIds: number[] = [];
		this.fieldValue.forEach((projectId: number) => {
			let project: ProjectModel = this.cachedProject[projectId];
			if (ObjectUtils.isNullOrUndefined(project)) {
				projectIds.push(projectId);
			}
		});

		if (ArrayUtils.isEmpty(projectIds)) {
			callback();
			return;
		}

		let counter: number = 0;
		projectIds.forEach((projectId: number) => {
			this.projectService.getProjectById(projectId, {
				onSuccess: (project: ProjectModel): void => {
					counter++;
					this.cachedProject[projectId] = project;
					if (counter === projectIds.length) {
						callback();
					}
				},
				onFailure: (error: AppError): void => {
					// ?
				}
			});		
		});
	}

	public onProjectSelectionWindowClosed(event: any): void {
		this.projectSelectionWindowVisible = false;
		this.onTouched();
	}

	public onDspViewWindowClosed(event: any): void {
		this.dspViewWindowVisible = false;
	}

	public onProjectSelect(): void {
		this.updatePerspective();
	}

	public onProjectUnselect(): void {
		this.updatePerspective();
	}

	private propagateValue(): void {
		if (ArrayUtils.isEmpty(this.fieldValue)) {
			this.onChange(null);			
		} else {
			this.onChange(this.fieldValue);			
		}
		this.onTouched();
	}

	private updatePerspective(): void {

		this.clearEnabled = false;
		this.viewDspEnabled = false;
		this.selectEnabled = false;
		
		if (this.singleSelection) {
			this.viewDspEnabled = ArrayUtils.isNotEmpty(this.fieldValue);			
			if (BooleanUtils.isFalse(this.readonly)) {
				this.clearEnabled = ArrayUtils.isNotEmpty(this.fieldValue);
				this.selectEnabled = true;
			}
		} else if (this.multipleSelection) {
			this.viewDspEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedProject);
			if (BooleanUtils.isFalse(this.readonly)) {
				this.clearEnabled = ObjectUtils.isNotNullOrUndefined(this.selectedProject);
				this.selectEnabled = true;
			}
		}
	}

	public writeValue(valueOrValues: number[]): void {
		this.fieldValue = [];
		if (ObjectUtils.isNotNullOrUndefined(valueOrValues)) {
			if (ObjectUtils.isArray(valueOrValues)) {
				valueOrValues.forEach((value: number) => {
					this.fieldValue.push(value);
				});
			} else {
				throw new Error("object is not valid");
			}
		}
		this.updatePerspective();
		this.loadProjectUiValues();
	}

	public registerOnChange(fn: any): void {
		this.onChange = fn;
	}

	public registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}
}

export class MultipleSelectionProjectUiValue {

	public projectId: number;

	public projectName: string;
	public projectAbbreviation: string;

	public get uiValue(): string {
		return this.projectName + " (" + this.projectAbbreviation + ")";
	}
}