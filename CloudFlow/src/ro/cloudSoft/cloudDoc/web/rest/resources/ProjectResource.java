package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.domain.project.DspViewModel;
import ro.cloudSoft.cloudDoc.domain.project.TasksViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
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
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskEventModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskViewModel;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.project.ProjectService;
import ro.cloudSoft.common.utils.PagingList;

@Component
@Path("/Project")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ProjectResource extends BaseResource {
	
	@Autowired
	public ProjectService projectService;
	
	@POST
	@Path("/getAllProjects")
	public List<ProjectModel> getAllProjects() {
		return projectService.getAllProjects();
	}

	@POST
	@Path("/getProjectTasks/{projectId}")
	public List<TaskModel> getProjectTasks(@PathParam("projectId") Long projectId) {
		return projectService.getProjectTasks(projectId);
	}
	
	@POST
	@Path("/getUserTasks/{userId}")
	public List<TaskModel> getUserTasks(@PathParam("userId") Long userId) {
		return projectService.getUserTasks(userId);
	}
	
	@POST
	@Path("/getTask/{taskId}")
	public TaskModel getTask(@PathParam("taskId") Long taskId) {
		return projectService.getTask(taskId);
	}
	
	@POST
	@Path("/saveTask")
	public void saveTask(TaskModel taskModel) throws PresentationException {
		try {
			projectService.saveTask(taskModel, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/completeTask")
	public void completeTask(CompleteTaskRequestModel requestModel) throws PresentationException {
		try {
			projectService.finalizeTask(requestModel, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/cancelTask/{taskId}")
	public void cancelTask(@PathParam("taskId") Long taskId) throws PresentationException{
		try {
			projectService.cancelTask(taskId);
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getUserInProgressTasksModels/{userId}")
	public List<TaskViewModel> getUserInProgressTasksModels(@PathParam("userId") Long userId) {
		return projectService.getUserInProgressTasksModels(userId);
	}
	
	@POST
	@Path("/getTaskViewModel/{taskId}")
	public TaskViewModel getTaskViewModel(@PathParam("taskId") Long taskId) {
		return projectService.getTaskViewModel(taskId);
	}

	@POST
	@Path("/getUserProjects")
	public List<ProjectModel> getUserProjects() {
		return projectService.getUserProjects(getSecurity().getUserId());
	}
	
	@POST
	@Path("/saveProject")
	public void saveProject(ProjectModel projectModel) {
		projectService.saveProject(projectModel, getSecurity());
	}

	@POST
	@Path("/getProjectParticipants/{projectId}")
	public List<UserModel> getProjectParticipants(@PathParam("projectId") Long projectId) {
		return projectService.getProjectParticipants(projectId);
	}

	@POST
	@Path("/getProjectById/{projectId}")
	public ProjectModel getProjectById(@PathParam("projectId") Long projectId) {
		return projectService.getProjectById(projectId);
	}

	@POST
	@Path("/closeProject/{projectId}")
	public void closeProject(@PathParam("projectId") Long projectId) throws PresentationException {
		try {
			projectService.closeProject(projectId, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getPagedProjectTaskViewModels")
	public PagingList<TaskViewModel> getPagedProjectTaskViewModels(PageRequest<GetPagedProjectTaskViewModelsRequestModel> pageRequest) {
		return projectService.getPagedProjectTaskViewModels(pageRequest, getSecurity());
	}
	
	@POST
	@Path("/getProjectWithDspViewModels/{allProjects}")
	public Set<ProjectWithDspViewModel> getProjectWithDspViewModels(@PathParam("allProjects") Boolean allProjects) throws PresentationException {
		try {
			Set<ProjectWithDspViewModel> models = projectService.getProjectWithDspViewModels(getSecurity(), allProjects);
			return models;
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getFinalizedTasksOfProject/{projectId}")
	public List<TaskModel> getFinalizedTasksOfProject(@PathParam("projectId") Long projectId) {
		return projectService.getFinalizedTasksOfProject(projectId);
	}

	@POST
	@Path("/getInProgressTasksOfProject/{projectId}")
	public List<TaskModel> getInProgressTasksOfProject(Long projectId) {
		return projectService.getInProgressTasksOfProject(projectId);
	}
	
	@POST
	@Path("/getAllOpenedProjectsWithDsp")
	public List<ProjectModel> getAllOpenedProjectsWithDsp() {
		return projectService.getAllOpenedProjectsWithDsp();
	}
	
	@POST
	@Path("/getDspViewModelByProjectId/{projectId}")
	public DspViewModel getDspViewModelByProjectId(@PathParam("projectId") Long projectId) throws PresentationException {
		try {
			return projectService.getDspViewModelByProjectId(projectId, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@POST
	@Path("/getTasksViewModelByProjectId/{projectId}")
	public TasksViewModel getTasksViewModelByProjectId(@PathParam("projectId") Long projectId) throws PresentationException {
		try {
			return projectService.getTasksViewModelByProjectId(projectId, getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}

	@POST
	@Path("/getGradDeRealizarePentruProiecteleCuDspModel")
	public GradDeRealizarePentruProiecteleCuDspModel getGradDeRealizarePentruProiecteleCuDspModel() throws PresentationException {
		try {
			return projectService.getGradDeRealizarePentruProiecteleCuDspModel(getSecurity());
		} catch (AppException ae) {
			throw PresentationExceptionUtils.getPresentationException(ae);
		}
	}
	
	@GET
	@Path("/downloadTaskAttachment/{attachmentId}")
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response downloadTaskAttachment(@PathParam("attachmentId") Long attachmentId) throws PresentationException {
		DownloadableFile downloadableFile = projectService.downloadTaskAttachment(attachmentId);
		return buildDownloadableFileResponse(downloadableFile);
	}

	@POST
	@Path("/getProjectViewModelById/{projectId}")
	public ProjectViewModel getProjectViewModelById(@PathParam("projectId") Long projectId) {
		return projectService.getProjectViewModelById(projectId);
	}
	
	@GET
	@Path("/exportDsp/{projectId}/{exportType}")
	@Produces({MediaType.APPLICATION_OCTET_STREAM})
	public Response exportDspAsPdfByProjectId(@PathParam("projectId") Long projectId, @PathParam("exportType") ExportType exportType) throws PresentationException {
		try {
			DownloadableFile downloadableFile = projectService.exportDsp(projectId, exportType);
			return buildDownloadableFileResponse(downloadableFile);
		} catch (AppException e) {
			throw PresentationExceptionUtils.getPresentationException(e);
		}
	}
	
	@POST
	@Path("/getAllInProgressTaskNamesByProjectAbbreviation/{projectAbbreviation}")
	public List<String>  getAllInProgressTaskNamesByProjectAbbreviation(@PathParam("projectAbbreviation") String projectAbbreviation) {
		return projectService.getAllInProgressTaskNamesByProjectAbbreviation(projectAbbreviation);
	}
	
	@POST
	@Path("/existsAbbreviation/{projectAbbreviation}")
	public boolean existsAbbreviation(@PathParam("projectAbbreviation") String projectAbbreviation) {
		return projectService.existsAbbreviation(projectAbbreviation);
	}
	
	@POST
	@Path("/existsName/{projectName}")
	public boolean existsName(@PathParam("projectName") String projectName) {
		return projectService.existsName(projectName);
	}
	
	@POST
	@Path("/getAllTaskEvents")
	public List<TaskEventModel> getAllTaskEvents(){
		return projectService.getAllTaskEvents();
	}
	
	@POST
	@Path("/updateTaskEventDescription")
	public void updateTaskEventDescription(TaskEventModel taskEvent) {
		projectService.updateTaskEventDescription(taskEvent);
	}
	
	@POST
	@Path("/getAllProjectSubactivities/{projectId}")
	public List<SubactivityModel> getAllProjectSubactivities(@PathParam("projectId") Long projectId){
		return projectService.getAllProjectSubactivities(projectId);
	}
	
	@POST
	@Path("/isSubactivityUsedInAnyTask/{subactivityId}")
	public boolean isSubactivityUsedInAnyTask(@PathParam("subactivityId") Long subactivityId) {
		return projectService.isSubactivityUsed(subactivityId);
	}
}
