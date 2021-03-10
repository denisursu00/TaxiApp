import { Component } from "@angular/core";
import { ProjectTabContent } from "../project-tab-content";
import { ProjectModel, ProjectComisieSauGlViewModel, ArrayUtils, NomenclatorConstants, ObjectUtils, NomenclatorService, MessageDisplayer, AppError, NomenclatorValueModel } from "@app/shared";

@Component({
	selector: "app-project-comisii-sau-gl-tab-content",
	templateUrl: "./project-comisii-sau-gl-tab-content.component.html"
})
export class ProjectComisiiSauGlTabContentComponent extends ProjectTabContent {

	private nomenclatorService: NomenclatorService;
	private messageDisplayer: MessageDisplayer;

	public comisiiSauGl: ProjectComisieSauGlViewModel[];
	public selectedComisieSauGl: ProjectComisieSauGlViewModel;

	public comisiiGLNomenclatorId: number;
	public comisiiGLNomenclatorWindowVisible: boolean; 

	public addButtonDisabled: boolean;
	public deleteButtonDisabled: boolean;

	public scrollHeight: string;

	public constructor(nomenclatorService: NomenclatorService, messageDisplayer: MessageDisplayer) {
		super();
		this.nomenclatorService = nomenclatorService;
		this.messageDisplayer = messageDisplayer;
		this.init();
	}

	public init(): void {
		this.comisiiSauGl = [];
		this.comisiiGLNomenclatorWindowVisible = false;

		this.addButtonDisabled = true;
		this.deleteButtonDisabled = true;

		this.scrollHeight = (window.innerHeight - 300) + "px";

		this.getComisiiGLNomenclatorId();
	}
	
	public getComisiiGLNomenclatorId(): void {
		this.nomenclatorService.getNomenclatorIdByCodeAsMap([NomenclatorConstants.NOMENCLATOR_CODE_COMISII_SAU_GL], {
			onSuccess: (nomenclatorIdByCode: object): void => {
				if (ObjectUtils.isNotNullOrUndefined(nomenclatorIdByCode)) {
					this.comisiiGLNomenclatorId = nomenclatorIdByCode[NomenclatorConstants.NOMENCLATOR_CODE_COMISII_SAU_GL];
					this.changePerspective();
				}
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	public onComisieSauGlSelected(): void {
		this.changePerspective();
	}

	public onComisieSauGlUnselected(): void {
		this.changePerspective();
	}

	private changePerspective(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.selectedComisieSauGl)) {
			this.deleteButtonDisabled = false;
		} else {
			this.deleteButtonDisabled = true;
		}

		if (ObjectUtils.isNotNullOrUndefined(this.comisiiGLNomenclatorId)) {
			this.addButtonDisabled = false;
		} else {
			this.addButtonDisabled = true;
		}
	}

	public onAddComisieSauGl(): void {
		this.comisiiGLNomenclatorWindowVisible = true;
	}

	public onDeleteComisieSauGl(): void {
		ArrayUtils.removeElement(this.comisiiSauGl, this.selectedComisieSauGl);
		this.selectedComisieSauGl = null;
	}

	public comisiiGLNomenclatorValueSelected(valuesIds: number[]): void {
		let id: number = valuesIds[0];
		this.nomenclatorService.getNomenclatorValue(id, {
			onSuccess: (nomenclatorValue: NomenclatorValueModel): void => {
				this.addComisieSauGl(nomenclatorValue);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	} 

	public addComisieSauGl(nomenclatorValue: NomenclatorValueModel): void {
		let exists: boolean = false;
		this.comisiiSauGl.forEach((comisieSauGl: ProjectComisieSauGlViewModel) => {
			if (comisieSauGl.id === nomenclatorValue.id) {
				exists = true;
			}
		});

		if (exists) {
			return;
		}

		let comisieSauGlViewModel: ProjectComisieSauGlViewModel = new ProjectComisieSauGlViewModel();
		comisieSauGlViewModel.id = nomenclatorValue.id;
		comisieSauGlViewModel.denumire = nomenclatorValue[NomenclatorConstants.COMISII_SAU_GL_ATTR_KEY_DENUMIRE];

		this.comisiiSauGl.push(comisieSauGlViewModel);
	}

	public comisiiGLNomenclatorWindowClosed(): void {
		this.comisiiGLNomenclatorWindowVisible = false;
	}

	public prepareForAdd(): void {
		
	}

	public prepareForViewOrEdit(): void {
		this.comisiiSauGl = [...this.project.comisiiSauGl];
	}

	public doWhenOnInit(): void {
	}

	public reset(): void {
	}

	public populateForSave(project: ProjectModel): void {
		let nomenclatorValuesIds: number[] = [];
		this.comisiiSauGl.forEach((comisieSauGl: ProjectComisieSauGlViewModel) => {
			nomenclatorValuesIds.push(comisieSauGl.id);
		}); 
		project.comisiiSauGlIds = nomenclatorValuesIds;
	}

	public isValid(): boolean {
		return true;
	}
}
