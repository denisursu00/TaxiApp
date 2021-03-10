package ro.cloudSoft.cloudDoc.services.project;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.ParameterConstants;
import ro.cloudSoft.cloudDoc.core.constants.ProjectConstants;
import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.GradDeRealizarePentruProiecteleCuDspModel;
import ro.cloudSoft.cloudDoc.services.parameters.ParametersService;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;

public class GradDeRealizarePentruProiecteleCuDspModelBuilder {

	private static int SCALE = 2;
	private final ProjectDao projectDao;
	private final ParametersService parametersService;
	private final List<Project> projects;
	private MathContext mathContext;
	
	public GradDeRealizarePentruProiecteleCuDspModelBuilder( final ProjectDao projectDao, final ParametersService parametersService, final List<Project> projects) {
		this.projectDao = projectDao;
		this.parametersService = parametersService;
		this.projects = projects;
		mathContext = new  MathContext(SCALE, RoundingMode.HALF_UP);

		
	}
	
	public GradDeRealizarePentruProiecteleCuDspModel build() throws AppException {
		
		GradDeRealizarePentruProiecteleCuDspModel gradDeRealizarePentruProiecteleCuDspModel = new GradDeRealizarePentruProiecteleCuDspModel();
		
		Map<String, List<Project>> proiectePeGradeDeImportanta = new HashMap<>();
		proiectePeGradeDeImportanta.put(ProjectConstants.GRAD_IMPORTANTA_CRITIC, new ArrayList<>());
		proiectePeGradeDeImportanta.put(ProjectConstants.GRAD_IMPORTANTA_IMPORTANT, new ArrayList<>());
		proiectePeGradeDeImportanta.put(ProjectConstants.GRAD_IMPORTANTA_IN_ASTEPTARE, new ArrayList<>());
		proiectePeGradeDeImportanta.put(ProjectConstants.GRAD_IMPORTANTA_STRATEGIC, new ArrayList<>());
		
		for (Project project : projects) {
			NomenclatorValue gradImportanta = project.getGradImportanta();
			String gradImportantaAsString = NomenclatorValueUtils.getAttributeValueAsString(gradImportanta, NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_GRAD_IMPORTANTA);
			proiectePeGradeDeImportanta.get(gradImportantaAsString).add(project);
		}
		
		List<Project> proiecteCuGradDeImportantaCritic = proiectePeGradeDeImportanta.get(ProjectConstants.GRAD_IMPORTANTA_CRITIC);
		BigDecimal mediaAritmeticaAProiectelorCuGradDeImportantaCritic = calculateMediaAritmeticaAGradelorDeRealizareAProiectelor(proiecteCuGradDeImportantaCritic);
		gradDeRealizarePentruProiecteleCuDspModel.setGradDeRealizareCritic(mediaAritmeticaAProiectelorCuGradDeImportantaCritic);

		List<Project> proiecteCuGradDeImportantaImportant = proiectePeGradeDeImportanta.get(ProjectConstants.GRAD_IMPORTANTA_IMPORTANT);
		BigDecimal mediaAritmeticaAProiectelorCuGradDeImportantaImportant = calculateMediaAritmeticaAGradelorDeRealizareAProiectelor(proiecteCuGradDeImportantaImportant);
		gradDeRealizarePentruProiecteleCuDspModel.setGradDeRealizareImportant(mediaAritmeticaAProiectelorCuGradDeImportantaImportant);

		List<Project> proiecteCuGradDeImportantaInAsteptare = proiectePeGradeDeImportanta.get(ProjectConstants.GRAD_IMPORTANTA_IN_ASTEPTARE);
		BigDecimal mediaAritmeticaAProiectelorCuGradDeImportantaInAsteptare = calculateMediaAritmeticaAGradelorDeRealizareAProiectelor(proiecteCuGradDeImportantaInAsteptare);
		gradDeRealizarePentruProiecteleCuDspModel.setGradDeRealizareInAsteptare(mediaAritmeticaAProiectelorCuGradDeImportantaInAsteptare);

		List<Project> proiecteCuGradDeImportantaInStrategic = proiectePeGradeDeImportanta.get(ProjectConstants.GRAD_IMPORTANTA_STRATEGIC);
		BigDecimal mediaAritmeticaAProiectelorCuGradDeImportantaStrategic = calculateMediaAritmeticaAGradelorDeRealizareAProiectelor(proiecteCuGradDeImportantaInStrategic);
		gradDeRealizarePentruProiecteleCuDspModel.setGradDeRealizareStrategic(mediaAritmeticaAProiectelorCuGradDeImportantaStrategic);
		
		BigDecimal gradDeRealizareGeneral = calculateGradDeRealizareGeneral(proiectePeGradeDeImportanta);
		gradDeRealizarePentruProiecteleCuDspModel.setGradDeRealizareGeneral(gradDeRealizareGeneral);
		
		String gradDeRealizareGeneralReprezentatAsString = ProjectRealizationDegreeProvider.getProjectRealizationDegree(gradDeRealizareGeneral).name();
		gradDeRealizarePentruProiecteleCuDspModel.setGradDeRealizareGeneralReprezentatAsString(gradDeRealizareGeneralReprezentatAsString);
		
		return gradDeRealizarePentruProiecteleCuDspModel;
	}
	
	private BigDecimal calculateGradDeRealizareGeneral(Map<String, List<Project>> proiecteByGradImportanta) throws AppException {
		
		BigDecimal numarProiecteCuGradDeImportantaCritic = new BigDecimal(proiecteByGradImportanta.get(ProjectConstants.GRAD_IMPORTANTA_CRITIC).size());
		BigDecimal pondereProiecteCuGradDeImportantaCritic = new BigDecimal(parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_PROIECT_GRAD_REALIZARE_PONDERE_IN_PROCENTE_PENTRU_PROIECTELE_CU_GRAD_DE_IMPORTANTA_CRITIC));

		BigDecimal numarProiecteCuGradDeImportantaImportant = new BigDecimal(proiecteByGradImportanta.get(ProjectConstants.GRAD_IMPORTANTA_IMPORTANT).size());
		BigDecimal pondereProiecteCuGradDeImportantaImportant = new BigDecimal(parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_PROIECT_GRAD_REALIZARE_PONDERE_IN_PROCENTE_PENTRU_PROIECTELE_CU_GRAD_DE_IMPORTANTA_IMPORTANT));

		BigDecimal numarProiecteCuGradDeImportantaInAsteptare = new BigDecimal(proiecteByGradImportanta.get(ProjectConstants.GRAD_IMPORTANTA_IN_ASTEPTARE).size());
		BigDecimal pondereProiecteCuGradDeImportantaInAsteptare = new BigDecimal(parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_PROIECT_GRAD_REALIZARE_PONDERE_IN_PROCENTE_PENTRU_PROIECTELE_CU_GRAD_DE_IMPORTANTA_IN_ASTEPTARE));

		BigDecimal numarProiecteCuGradDeImportantaStrategic = new BigDecimal(proiecteByGradImportanta.get(ProjectConstants.GRAD_IMPORTANTA_STRATEGIC).size());
		BigDecimal pondereProiecteCuGradDeImportantaStrategic = new BigDecimal(parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_PROIECT_GRAD_REALIZARE_PONDERE_IN_PROCENTE_PENTRU_PROIECTELE_CU_GRAD_DE_IMPORTANTA_STRATEGIC));
		
		BigDecimal numarTotalDeProiecte = numarProiecteCuGradDeImportantaCritic
				.add(numarProiecteCuGradDeImportantaImportant)
				.add(numarProiecteCuGradDeImportantaInAsteptare)
				.add(numarProiecteCuGradDeImportantaStrategic);

		if (numarTotalDeProiecte == BigDecimal.ZERO) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal gradDeRealizare = numarProiecteCuGradDeImportantaCritic.multiply(pondereProiecteCuGradDeImportantaCritic, mathContext);
		gradDeRealizare = gradDeRealizare.add(numarProiecteCuGradDeImportantaImportant.multiply(pondereProiecteCuGradDeImportantaImportant, mathContext), mathContext);
		gradDeRealizare = gradDeRealizare.add(numarProiecteCuGradDeImportantaInAsteptare.multiply(pondereProiecteCuGradDeImportantaInAsteptare, mathContext), mathContext);
		gradDeRealizare = gradDeRealizare.add(numarProiecteCuGradDeImportantaStrategic.multiply(pondereProiecteCuGradDeImportantaStrategic, mathContext), mathContext);

		gradDeRealizare = gradDeRealizare.divide(new BigDecimal(100), mathContext);
		gradDeRealizare = gradDeRealizare.divide(numarTotalDeProiecte, mathContext);
		
		return gradDeRealizare;
	}
	
	private BigDecimal calculateMediaAritmeticaAGradelorDeRealizareAProiectelor(List<Project> proiecte) throws AppException {
		
		if (proiecte.size() == 0) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal numarProiecte = new BigDecimal(proiecte.size());
		BigDecimal suma = calculateSumaGradeDeRealizare(proiecte);
		BigDecimal mediaAritmetica = suma.divide(numarProiecte, mathContext);
		return mediaAritmetica;
	}
	
	private BigDecimal calculateSumaGradeDeRealizare(List<Project> proiecte) throws AppException {

		BigDecimal gradDeRealizarePentruProiecteleCuGradDeCriticitateCritic = BigDecimal.ZERO;
		for (Project project : proiecte) {
			ProjectWithDspDegreeOfAchievementCalculator projectDegreeOfAchievementCalculator = new ProjectWithDspDegreeOfAchievementCalculator(projectDao, parametersService, project); 
			BigDecimal projectDegreeOfAchievement = projectDegreeOfAchievementCalculator.calculate();
			gradDeRealizarePentruProiecteleCuGradDeCriticitateCritic = gradDeRealizarePentruProiecteleCuGradDeCriticitateCritic.add(projectDegreeOfAchievement, mathContext);
		}
		
		return gradDeRealizarePentruProiecteleCuGradDeCriticitateCritic;
	}
}
