package ro.cloudSoft.cloudDoc.dao.project;

import java.util.Date;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.ProjectEstimation;
import ro.cloudSoft.cloudDoc.domain.project.ProjectStatus;
import ro.cloudSoft.cloudDoc.domain.project.Subactivity;
import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.domain.project.TaskSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.project.TaskStatus;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.DashboardProiecteFilterModel;
import ro.cloudSoft.common.utils.PagingList;

public interface ProjectDao {
	
	Long save(Project project);
	
	List<Project> getAll();
	
	List<Project> getByReportFilter(ListaProiectelorCareAuVizatActiunileLuniiReportFilterModel filter);
	
	Project getById(Long id);
	
	Project getByAbreviere(String abreviere);
	
	List<Task> getProjectTasks(Long projectId);
	
	List<Project> getUserProjectsByStatus(Long userId, ProjectStatus status);
	
	Set<User> getProjectParticipants(Long projectId);
	
	PagingList<Task> getPagedProjectTasks(final int offset, final int pageSize, TaskSearchCriteria searchCriteria);

	List<Project> getUserProjectsWithDspByStatus(Long userId, ProjectStatus status);
	
	ProjectEstimation findProjectEstimationById(Long projectEstimationId);
	
	List<Task> getProjectTasksByStatus(Long projectId, TaskStatus status);

	List<Project> getAllOpenedWithDsp();
	
	String getProjectNameById(Long projectId);

	int getNrTaskuriProiectFinalizateInTermen(Long id);

	List<Project> getDspProjectsByDashboardProiecteFilterModel(DashboardProiecteFilterModel filter);

	List<String> getAllAbrevieriProiecte();
	
	boolean existsAbbreviation(String projectAbbreviation);
	
	boolean existsName(String name);
	
	List<Project> getAllByEndDate(Date endDate);
	
	List<Subactivity> getProjectSubactivities(Long projectId);
}
