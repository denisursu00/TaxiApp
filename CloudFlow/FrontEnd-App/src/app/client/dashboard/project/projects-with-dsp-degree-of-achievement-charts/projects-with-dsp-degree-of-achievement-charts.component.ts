import { Component, OnInit } from "@angular/core";
import { ProjectService, GradDeRealizarePentruProiecteleCuDspModel, AppError, MessageDisplayer, StringUtils } from "@app/shared";

@Component({
	selector: "app-projects-with-dsp-degree-of-achievement-charts",
	templateUrl: "./projects-with-dsp-degree-of-achievement-charts.component.html"
})
export class ProjectsWithDspDegreeOfAchievementChartsComponent {

	private projectService: ProjectService;
	private messageDisplayer: MessageDisplayer;
	
	public minValue: number = 0;
	public maxValue: number = 100;
	public thickness: number = 35;
	public unitLabel: String = "%";
	public size: number = 200;
	
	public valoarePentruGradDeRealizareGeneral: number = 0;
	public labelPentruGradGeneral: String;
	public backgroundColorPentruGradGeneral: String = "#0000cc";
	public foregroundColorPentruGradGeneral: String = "#000080";

	public valoarePentruGradDeImportantaCritic: number = 0;
	public backgroundColorPentruGradDeImportantaCritic: String = "#ff0000";
	public foregroundColorPentruGradDeImportantaCritic: String = "#b30000";
	
	public valoarePentruGradDeImportantaImportant: number = 0;
	public backgroundColorPentruGradDeImportantaImportant: String = "#ffff00";
	public foregroundColorPentruGradDeImportantaImportant: String = "#b3b300";
	
	public valoarePentruGradDeImportantaInAsteptare: number = 0;
	public backgroundColorPentruGradDeImportantaInAsteptare: String = "#99cc00";
	public foregroundColorPentruGradDeImportantaInAsteptare: String = "#608000";
	
	public valoarePentruGradDeImportantaStrategic: number = 0;
	public backgroundColorPentruGradDeImportantaStrategic: String = "#6699ff";
	public foregroundColorPentruGradDeImportantaStrategic: String = "#1a66ff";

	public constructor(projectService: ProjectService, messageDisplayer: MessageDisplayer) {
		this.projectService = projectService;
		this.messageDisplayer = messageDisplayer;

		this.init();
	}

	private init(): void {

		this.projectService.getGradDeRealizarePentruProiecteleCuDspModel({
			onSuccess: (model: GradDeRealizarePentruProiecteleCuDspModel): void => {
				this.prepareValoarePentruDiagramaCuGradDeRealizareGeneral(model);
				this.prepareValoarePentruDiagramaCuGradDeRealizareCritic(model);
				this.prepareValoarePentruDiagramaCuGradDeRealizareImportant(model);
				this.prepareValoarePentruDiagramaCuGradDeRealizareInAsteptare(model);
				this.prepareValoarePentruDiagramaCuGradDeRealizareStrategic(model);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareValoarePentruDiagramaCuGradDeRealizareGeneral(model: GradDeRealizarePentruProiecteleCuDspModel): void {

		let valoareInProcente = model.gradDeRealizareGeneral * 100;
		this.valoarePentruGradDeRealizareGeneral = valoareInProcente;
	}

	private prepareValoarePentruDiagramaCuGradDeRealizareCritic(model: GradDeRealizarePentruProiecteleCuDspModel): void {

		let valoareInProcente = model.gradDeRealizareCritic * 100;
		this.valoarePentruGradDeImportantaCritic = valoareInProcente;
	}

	private prepareValoarePentruDiagramaCuGradDeRealizareImportant(model: GradDeRealizarePentruProiecteleCuDspModel): void {
		
		let valoareInProcente = model.gradDeRealizareImportant * 100;
		this.valoarePentruGradDeImportantaImportant = valoareInProcente;
	}

	private prepareValoarePentruDiagramaCuGradDeRealizareInAsteptare(model: GradDeRealizarePentruProiecteleCuDspModel): void {
		
		let valoareInProcente = model.gradDeRealizareInAsteptare * 100;
		this.valoarePentruGradDeImportantaInAsteptare = valoareInProcente;
	}

	private prepareValoarePentruDiagramaCuGradDeRealizareStrategic(model: GradDeRealizarePentruProiecteleCuDspModel): void {
		
		let valoareInProcente = model.gradDeRealizareStrategic * 100;
		this.valoarePentruGradDeImportantaStrategic = valoareInProcente;
	}
}