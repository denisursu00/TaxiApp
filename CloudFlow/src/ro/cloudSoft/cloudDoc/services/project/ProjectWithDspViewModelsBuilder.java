package ro.cloudSoft.cloudDoc.services.project;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.core.constants.NomenclatorConstants;
import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.Subactivity;
import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.ProjectWithDspViewModel;
import ro.cloudSoft.cloudDoc.services.parameters.ParametersService;
import ro.cloudSoft.cloudDoc.utils.nomenclator.NomenclatorValueUtils;

public class ProjectWithDspViewModelsBuilder {

	private static String GRAD_IMPORTANTA_PROIECT_0 = "0";
	private static String GRAD_IMPORTANTA_PROIECT_1 = "1";
	private static String GRAD_IMPORTANTA_PROIECT_2 = "2";
	private static String GRAD_IMPORTANTA_PROIECT_3 = "3";
	private static String GRAD_IMPORTANTA_PROIECT_4 = "4";
	private static String GRAD_IMPORTANTA_PROIECT_5 = "5";

	private static String IMPORTANTA_PROIECT_LA_AFISARE_1 = "1";
	private static String IMPORTANTA_PROIECT_LA_AFISARE_2 = "2";
	private static String IMPORTANTA_PROIECT_LA_AFISARE_3 = "3";
	
	private final ProjectDao projectDao;
	private final ParametersService parametersService;
	private final List<Project> projects;
	private final boolean showAllProjects;
	
	public ProjectWithDspViewModelsBuilder( ProjectDao projectDao, ParametersService parametersService, List<Project> projects, boolean showAllProjects) {
		this.projectDao = projectDao;
		this.parametersService = parametersService;
		this.projects = projects;
		this.showAllProjects = showAllProjects;
		
	}
	
	private ProjectWithDspViewModel createModel(Project project) throws AppException {
		ProjectWithDspViewModel model = new ProjectWithDspViewModel();
		model.setId(project.getId());
		model.setName(project.getName());
		model.setResponsibleUserName(project.getResponsibleUser().getDisplayName());
		model.setImportanceDegreeColor(getImportanceDegreeColor(project));
		model.setDegreeOfAchievement(getRealizationDegreeOfAchievement(project));
		model.setSubprojects(project.getSubactivities().stream().map(Subactivity::getName).collect(Collectors.toList()));
		return model;
	}
	
	private String getImportanceDegreeColor(Project project) throws AppException {
		NomenclatorValue gradImportanta = project.getGradImportanta();
		return NomenclatorValueUtils.getAttributeValueAsString(gradImportanta, NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_CULOARE_GRAD_DE_IMPORTANTA);
	}
	
	private ProjectRealizationDegree getRealizationDegreeOfAchievement(Project project) throws AppException {
		
		ProjectWithDspDegreeOfAchievementCalculator projectDegreeOfAchievementCalculator = new ProjectWithDspDegreeOfAchievementCalculator(projectDao, parametersService, project);
		
		BigDecimal projectDegreeOfAchievement = projectDegreeOfAchievementCalculator.calculate();
		
		return ProjectRealizationDegreeProvider.getProjectRealizationDegree(projectDegreeOfAchievement);
	}

	private Set<ProjectWithDspViewModel> getAllProjectWithDspViewModel() throws AppException {

		Map<String, List<ProjectWithDspViewModel>> projectsMap = new HashMap<>();
		
		projectsMap.put(GRAD_IMPORTANTA_PROIECT_0, new ArrayList<>());
		projectsMap.put(GRAD_IMPORTANTA_PROIECT_1, new ArrayList<>());
		projectsMap.put(GRAD_IMPORTANTA_PROIECT_2, new ArrayList<>());
		projectsMap.put(GRAD_IMPORTANTA_PROIECT_3, new ArrayList<>());
		projectsMap.put(GRAD_IMPORTANTA_PROIECT_4, new ArrayList<>());
		projectsMap.put(GRAD_IMPORTANTA_PROIECT_5, new ArrayList<>());
		
		for (Project project : projects) {
			NomenclatorValue gradImportanta = project.getGradImportanta();
			String gradImportantaAsString = NomenclatorValueUtils.getAttributeValueAsString(gradImportanta, NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_VALOARE_GRAD_IMPORTANTA);
			projectsMap.get(gradImportantaAsString).add(createModel(project));
		}
		
		Set<ProjectWithDspViewModel> projects = new LinkedHashSet<>();
		projects.addAll(projectsMap.get(GRAD_IMPORTANTA_PROIECT_0));
		projects.addAll(projectsMap.get(GRAD_IMPORTANTA_PROIECT_1));
		projects.addAll(projectsMap.get(GRAD_IMPORTANTA_PROIECT_2));
		projects.addAll(projectsMap.get(GRAD_IMPORTANTA_PROIECT_3));
		projects.addAll(projectsMap.get(GRAD_IMPORTANTA_PROIECT_4));
		projects.addAll(projectsMap.get(GRAD_IMPORTANTA_PROIECT_5));
		
		return projects;
	}
	
	private Set<ProjectWithDspViewModel> getFilteredProjectWithDspViewModels() throws AppException {
		
		Map<String, List<ProjectWithDspViewModel>> projectsMap = new HashMap<>();
		projectsMap.put(IMPORTANTA_PROIECT_LA_AFISARE_1, new ArrayList<>());
		projectsMap.put(IMPORTANTA_PROIECT_LA_AFISARE_2, new ArrayList<>());
		projectsMap.put(IMPORTANTA_PROIECT_LA_AFISARE_3, new ArrayList<>());
		
		for (Project project : projects) {
			if (projectHasTaskInProgressWithEndDateLowerThanCurrentDate(project)) {
				projectsMap.get(IMPORTANTA_PROIECT_LA_AFISARE_1).add(createModel(project));
			} else if (projectHasTaskInProgressWithEndDateEqualWithCurrentDate(project)) {
				projectsMap.get(IMPORTANTA_PROIECT_LA_AFISARE_2).add(createModel(project));
			} else if (projectHasImportanceDegreeOne(project)) {
				projectsMap.get(IMPORTANTA_PROIECT_LA_AFISARE_3).add(createModel(project));
			}
		}
		
		Set<ProjectWithDspViewModel> projects = new LinkedHashSet<>();
		projects.addAll(projectsMap.get(IMPORTANTA_PROIECT_LA_AFISARE_1));
		projects.addAll(projectsMap.get(IMPORTANTA_PROIECT_LA_AFISARE_2));
		projects.addAll(projectsMap.get(IMPORTANTA_PROIECT_LA_AFISARE_3));
	
		return projects;
	}
	
	private boolean projectHasTaskInProgressWithEndDateLowerThanCurrentDate(Project project) {
		List<Task> tasks = projectDao.getProjectTasks(project.getId());
		for (Task task : tasks) {
			if (task.getEndDate().before(new Date())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean projectHasTaskInProgressWithEndDateEqualWithCurrentDate(Project project) {
		List<Task> tasks = projectDao.getProjectTasks(project.getId());
		for (Task task : tasks) {
			if (task.getEndDate().equals(new Date())) {
				return true;
			}
		}
		return false;
	}
	
	private boolean projectHasImportanceDegreeOne(Project project) throws AppException {
		NomenclatorValue gradImportanta = project.getGradImportanta();
		String gradImportantaAsString = NomenclatorValueUtils.getAttributeValueAsString(gradImportanta, NomenclatorConstants.IMPORTANTA_PROIECTE_ATTRIBUTE_KEY_VALOARE_GRAD_IMPORTANTA);
		if (gradImportantaAsString.equals(GRAD_IMPORTANTA_PROIECT_1)) {
			return true;
		}
		return false;
	}
	
	public Set<ProjectWithDspViewModel> build() throws AppException {
		if (showAllProjects) {
			return getAllProjectWithDspViewModel();
		}
		return getFilteredProjectWithDspViewModels();
	}
}
