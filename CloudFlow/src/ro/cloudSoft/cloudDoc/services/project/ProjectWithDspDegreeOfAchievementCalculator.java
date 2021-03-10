package ro.cloudSoft.cloudDoc.services.project;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.core.constants.ParameterConstants;
import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.ProjectEstimation;
import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.domain.project.TaskStatus;
import ro.cloudSoft.cloudDoc.services.parameters.ParametersService;
import ro.cloudSoft.cloudDoc.utils.log.LogHelper;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;
import ro.cloudSoft.common.utils.DateUtils;

public class ProjectWithDspDegreeOfAchievementCalculator {
	
	private static final LogHelper LOGGER = LogHelper.getInstance(ProjectWithDspDegreeOfAchievementCalculator.class);
	
	private static int SCALE = 2;
	
	private final ProjectDao projectDao;
	private final ParametersService parametersService;
	private final Project project;
	
	private MathContext mathContext;
	
	public ProjectWithDspDegreeOfAchievementCalculator(final ProjectDao projectDao, final ParametersService parametersService, final Project project) {
		this.projectDao = projectDao;
		this.parametersService = parametersService;
		this.project = project;
		
		mathContext = new  MathContext(SCALE, RoundingMode.HALF_UP);
	}
	
	public BigDecimal calculate() throws AppException {
		
		int numberOfFinalizedActions = 0;
		int numberOfFinalizedActionsBeforeDeadline = 0;
		
		
		List<Task> tasks = projectDao.getProjectTasks(project.getId());
		for (Task task : tasks) {
			if (task.getStatus().equals(TaskStatus.FINALIZED)) {
				numberOfFinalizedActions++;
			}
			
			if (task.getFinalizedDate() != null ) {
				if (task.isPermanent()) {
					numberOfFinalizedActionsBeforeDeadline++;
				} else if (task.getEndDate() != null && (DateUtils.isBeforeDate(task.getFinalizedDate(), task.getEndDate()) || DateUtils.isSameDate(task.getFinalizedDate(), task.getEndDate()))){
					numberOfFinalizedActionsBeforeDeadline++;
				}
			}
		}
		
		BigDecimal projectRealizationDegree = getWeightOfRatioBetweenFinalizedActionsAndEstimatedActions(project, numberOfFinalizedActions);
		
		if (numberOfFinalizedActions > 0) {
			BigDecimal weightOfRatioBetweenFinalizedActionsBeforeDeadlineAndNumberOfFinalizedActions = 
					getWeightOfRatioBetweenFinalizedActionsBeforeDeadlineAndNumberOfFinalizedActions(numberOfFinalizedActionsBeforeDeadline, numberOfFinalizedActions);
			
			projectRealizationDegree = projectRealizationDegree.add(weightOfRatioBetweenFinalizedActionsBeforeDeadlineAndNumberOfFinalizedActions, mathContext);
		}
		
		
		BigDecimal weightOfResponsibleUserEstimation = getWeightOfResponsibleUserEstimation(project);
		projectRealizationDegree = projectRealizationDegree.add(weightOfResponsibleUserEstimation);
		
		projectRealizationDegree = projectRealizationDegree
			.divide(new BigDecimal(100), mathContext);
		
		BigDecimal daysBetweenProjectEndDateAndStartDate = getDaysBetweenProjectEndDateAndStartDate(project);
		BigDecimal daysBetweenCurrentDateAndProjectStartDate = getDaysBetweenCurrentDateAndProjectStartDate(project);
		if (daysBetweenProjectEndDateAndStartDate == BigDecimal.ZERO || daysBetweenCurrentDateAndProjectStartDate == BigDecimal.ZERO) {
			projectRealizationDegree = BigDecimal.ZERO;
		} else {
			projectRealizationDegree = projectRealizationDegree
					.divide(daysBetweenCurrentDateAndProjectStartDate.divide(daysBetweenProjectEndDateAndStartDate, mathContext), mathContext);
		}
		
		if (projectRealizationDegree.compareTo(BigDecimal.ONE) >= 0) {
			return BigDecimal.ONE;
		}
		
		return projectRealizationDegree.round(mathContext);
	}
	
	private BigDecimal getWeightOfRatioBetweenFinalizedActionsAndEstimatedActions(Project project, int numberOfFinalizedActions) throws AppException {
		int numberOfEstimatedActionsFromDspInPercent = getNumberOfEstimatedActionFromDsp(project);
		Integer weightOfFinalizedActionsPerNumberOfEstimatedActionsFromDsp = parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_PROIECT_GRAD_REALIZARE_PONDERE_IN_PROCENTE_PENTRU_NR_ACTIUNI_REALIZATE_PE_NR_ACTIUNI_ESTIMATE);

		if (numberOfEstimatedActionsFromDspInPercent == 0 ) {
			return BigDecimal.ZERO;
		}
		BigDecimal ratioBetweenFinalizedActionsAndEstimatedActions = new BigDecimal(numberOfFinalizedActions).divide(new BigDecimal(numberOfEstimatedActionsFromDspInPercent), mathContext);
		return ratioBetweenFinalizedActionsAndEstimatedActions.multiply(new BigDecimal(weightOfFinalizedActionsPerNumberOfEstimatedActionsFromDsp), mathContext);
	}

	private BigDecimal getWeightOfRatioBetweenFinalizedActionsBeforeDeadlineAndNumberOfFinalizedActions(int numberOfFinalizedActionsBeforeDeadline, int numberOfFinalizedActions) throws AppException {
		Integer weightOfFinalizedActionsBeforeDeadlineInPercent = parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_PROIECT_GRAD_REALIZARE_PONDERE_IN_PROCENTE_PENTRU_REALIZARE_ACTIUNI_IN_TERMEN);
		if (numberOfFinalizedActions == 0) {
			return BigDecimal.ZERO;
		}
		BigDecimal ratioBetweenFinalizedActionsBeforeDeadlineAndNumberOfFinalizedActions = new BigDecimal(numberOfFinalizedActionsBeforeDeadline).divide(new BigDecimal(numberOfFinalizedActions), mathContext);
		
		return ratioBetweenFinalizedActionsBeforeDeadlineAndNumberOfFinalizedActions.multiply(new BigDecimal(weightOfFinalizedActionsBeforeDeadlineInPercent), mathContext);
	}
	
	private BigDecimal getWeightOfResponsibleUserEstimation(Project project) throws AppException {
		Integer weightOfUserResponsibleEstimationInPercent = parametersService.getParamaterValueAsIntegerByParameterName(ParameterConstants.PARAM_NAME_PROIECT_GRAD_REALIZARE_PONDERE_IN_PROCENTE_PENTRU_ESTIMARE_RESPONSABIL);
		int responsibleUserEstimationInPercent = getProjectLastEstimation(project).getEstimationInPercent();
		BigDecimal responsibleUserEstimation = new BigDecimal(responsibleUserEstimationInPercent).divide(new BigDecimal(100), mathContext);
		return responsibleUserEstimation.multiply(new BigDecimal(weightOfUserResponsibleEstimationInPercent), mathContext);
	}
	
	private BigDecimal getDaysBetweenCurrentDateAndProjectStartDate(Project project) {
		return new BigDecimal(DateUtils.pozitiveNumberDaysBetween(project.getStartDate(), new Date()));
	}
	
	private BigDecimal getDaysBetweenProjectEndDateAndStartDate(Project project) {
		return new BigDecimal(DateUtils.pozitiveNumberDaysBetween(project.getStartDate(), project.getEndDate()));
	}
	
	private ProjectEstimation getProjectLastEstimation(Project project) {
		for (ProjectEstimation estimation : project.getEstimations()) {
			if (estimation.getEndDate() == null) {
				return estimation;
			}
		}
		throw new RuntimeException("Cannot find project estimation.");
	}
	
	private int getNumberOfEstimatedActionFromDsp(Project project) throws AppException {
		NomenclatorValue incadrareProiect = project.getIncadrareProiect();
		String numarActiuniEstimateAsString = NomenclatorValueUtils.getAttributeValueAsString(incadrareProiect, NomenclatorConstants.INCADRARI_PROIECTE_ATTRIBUTE_KEY_NUMAR_ACTIUNI_ESTIMATE);
		return Integer.valueOf(numarActiuniEstimateAsString);
	}
	
}
