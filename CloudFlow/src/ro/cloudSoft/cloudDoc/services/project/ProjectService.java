package ro.cloudSoft.cloudDoc.services.project;

import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.project.DspViewModel;
import ro.cloudSoft.cloudDoc.domain.project.TaskAttachment;
import ro.cloudSoft.cloudDoc.domain.project.TasksViewModel;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DownloadableFile;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ExportType;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.PageRequest;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.CompleteTaskRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.GetPagedProjectTaskViewModelsRequestModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.GradDeRealizarePentruProiecteleCuDspModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.ProjectModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.ProjectViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.ProjectWithDspViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.SubactivityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskAttachmentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskEventModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskViewModel;
import ro.cloudSoft.common.utils.PagingList;

public interface ProjectService {
	
	List<ProjectModel> getAllProjects();
	
	List<TaskModel> getProjectTasks(Long projectId);
	
	List<TaskModel> getUserTasks(Long userId);
	
	TaskModel getTask(Long taskId);
	
	Long saveTask(TaskModel taskModel, SecurityManager userSecurity) throws AppException;
	
	void finalizeTask(CompleteTaskRequestModel requestModel, SecurityManager userSecurity) throws AppException;
	
	void cancelTask(Long taskId) throws AppException;
	
	List<TaskViewModel> getUserInProgressTasksModels(Long userId);
	
	TaskViewModel getTaskViewModel(Long taskId);
	
	TaskAttachment getTaskAttachment(Long attachmentId);
	
	List<ProjectModel> getUserProjects(Long userId);
	
	Long saveProject(ProjectModel projectModel, SecurityManager userSecurity);
	
	List<UserModel> getProjectParticipants(Long projectId);

	ProjectModel getProjectById(Long projectId);
	
	void closeProject(Long projectId, SecurityManager userSecurity) throws AppException;
	
	PagingList<TaskViewModel> getPagedProjectTaskViewModels(PageRequest<GetPagedProjectTaskViewModelsRequestModel> pageRequst, SecurityManager userSecurity);

	Set<ProjectWithDspViewModel> getProjectWithDspViewModels(SecurityManager userSecurity, boolean allProjects) throws AppException;
	
	List<TaskModel> getFinalizedTasksOfProject(Long projectId);
	
	List<TaskModel> getInProgressTasksOfProject(Long projectId);
	
	GradDeRealizarePentruProiecteleCuDspModel getGradDeRealizarePentruProiecteleCuDspModel(SecurityManager userSecurity) throws AppException;
	
	List<ProjectModel> getAllOpenedProjectsWithDsp();
	
	String getProjectNameById(Long projectId);
	
	DspViewModel getDspViewModelByProjectId(Long projectId, SecurityManager userSecurity) throws AppException;	
	
	TasksViewModel getTasksViewModelByProjectId(Long projectId, SecurityManager userSecurity) throws AppException;	
	
	DownloadableFile downloadTaskAttachment(Long id);
	
	ProjectViewModel getProjectViewModelById(Long id);
	
	DownloadableFile exportDsp(Long projectId, ExportType exportType) throws AppException;
	
	List<String> getAllInProgressTaskNamesByProjectAbbreviation(String projectAbbreviation);

	boolean existsAbbreviation(String projectAbbreviation);
	
	boolean existsName(String name);
	
	List<TaskEventModel> getAllTaskEvents();
	
	void updateTaskEventDescription(TaskEventModel taskEvent);
	
	List<SubactivityModel> getAllProjectSubactivities(Long projectId);
	
	boolean isSubactivityUsed(Long subactivityId);
}
