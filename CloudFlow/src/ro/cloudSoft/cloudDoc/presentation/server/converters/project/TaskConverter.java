package ro.cloudSoft.cloudDoc.presentation.server.converters.project;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.dao.project.TaskDao;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.Subactivity;
import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.SubactivityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskViewModel;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.OrganizationEntityConverter;
import ro.cloudSoft.cloudDoc.presentation.server.converters.organization.UserConverter;
import ro.cloudSoft.cloudDoc.services.organization.UserService;

public class TaskConverter {
	
	private ProjectDao projectDao;
	private TaskDao taskDao;
	private UserService userService;
	private TaskAttachmentConverter taskAttachmentConverter;
	private ProjectConverter projectConverter;
	
	public TaskModel toModel(Task entity) {
		TaskModel model = new TaskModel();
		model.setId(entity.getId());
		model.setName(entity.getName());
		model.setDescription(entity.getDescription());
		model.setStartDate(entity.getStartDate());
		model.setEndDate(entity.getEndDate());
		model.setFinalizedDate(entity.getFinalizedDate());
		model.setPriority(entity.getPriority());
		model.setComments(entity.getComments());
		model.setProjectId(entity.getProject().getId());
		model.setStatus(entity.getStatus());
		model.setTaskAttachments(taskAttachmentConverter.toModels(entity.getAttachments()));
		model.setInititatorId(entity.getInitiator().getId());
		model.setPermanent(entity.isPermanent());
		model.setParticipationsTo(entity.getParticipationsTo());
		model.setEvenimentStartDate(entity.getEvenimentStartDate());
		model.setEvenimentEndDate(entity.getEvenimentEndDate());
		model.setExplications(entity.getExplications());
		if (entity.getSubactivity() != null) {
			model.setSubactivityId(entity.getSubactivity().getId());
		}
		
		List<OrganizationEntityModel> taskAssignments = new ArrayList<OrganizationEntityModel>();
		for (OrganizationEntity organizationEntity : entity.getAssignments()) {
			OrganizationEntityModel organizationEntityModel = new OrganizationEntityModel();
			organizationEntityModel = OrganizationEntityConverter.getModelFromOrganizationEntity(organizationEntity);
			taskAssignments.add(organizationEntityModel);
		}
		model.setTaskAssignments(taskAssignments);
		
		return model;
	}
	
	public Task toEntity(TaskModel model) {
		Task entity = null;
		if (model.getId() != null) {
			entity = taskDao.findById(model.getId());
		} else {
			entity = new Task();
		}
		entity.setId(model.getId());
		entity.setName(model.getName());
		entity.setDescription(model.getDescription());
		entity.setStartDate(model.getStartDate());
		entity.setEndDate(model.getEndDate());
		entity.setFinalizedDate(model.getFinalizedDate());
		entity.setPriority(model.getPriority());
		entity.setComments(model.getComments());
		entity.setStatus(model.getStatus());
		Project project = projectDao.getById(model.getProjectId());
		entity.setProject(project);
		if (model.getSubactivityId() != null) {
			List<Subactivity> subactivities = project.getSubactivities();
			for (Subactivity subactivity : subactivities) {
				if (subactivity.getId().equals(model.getSubactivityId())) {
					entity.setSubactivity(subactivity);
					break;
				}
			}
		}else {
			entity.setSubactivity(null);
		}
		entity.setAttachments(taskAttachmentConverter.toEntities(model.getTaskAttachments(), entity));
		entity.setInitiator(userService.getUserById(model.getInititatorId()));
		entity.setPermanent(model.isPermanent());
		entity.setParticipationsTo(model.getParticipationsTo());
		entity.setEvenimentStartDate(model.getEvenimentStartDate());
		entity.setEvenimentEndDate(model.getEvenimentEndDate());
		entity.setExplications(model.getExplications());
		
		List<OrganizationEntity> taskAssignments = new ArrayList<OrganizationEntity>();
		for (OrganizationEntityModel organizationEntityModel: model.getTaskAssignments()) {
			OrganizationEntity organizationEntity = OrganizationEntityConverter.getOrganizationEntityFromModel(organizationEntityModel);
			taskAssignments.add(organizationEntity);
		}
		entity.setAssignments(taskAssignments);
		
		return entity;
	}
	
	public TaskViewModel toViewModel(Task entity) {
		TaskViewModel viewModel = new TaskViewModel();
		viewModel.setId(entity.getId());
		viewModel.setName(entity.getName());
		viewModel.setDescription(entity.getDescription());
		viewModel.setStartDate(entity.getStartDate());
		viewModel.setEndDate(entity.getEndDate());
		viewModel.setFinalizedDate(entity.getFinalizedDate());
		viewModel.setPriority(entity.getPriority());
		viewModel.setComments(entity.getComments());
		viewModel.setProject(projectConverter.toModel(entity.getProject()));
		viewModel.setStatus(entity.getStatus());
		viewModel.setTaskAttachments(taskAttachmentConverter.toModels(entity.getAttachments()));
		viewModel.setParticipationsTo(entity.getParticipationsTo());
		viewModel.setEvenimentStartDate(entity.getEvenimentStartDate());
		viewModel.setEvenimentEndDate(entity.getEvenimentEndDate());
		viewModel.setExplications(entity.getExplications());
		if (entity.getSubactivity() != null) {
			viewModel.setSubactivity(new SubactivityModel(entity.getSubactivity().getId(), entity.getSubactivity().getName()));
		}
		
		List<UserModel> assignedUsers = new ArrayList<UserModel>();
		for (OrganizationEntity organizationEntity : entity.getAssignments()) {
			User user = userService.getUserById(organizationEntity.getId());
			assignedUsers.add(UserConverter.getModelFromUser(user));
		}
		viewModel.setAssignedUsers(assignedUsers);
		viewModel.setInitiator(UserConverter.getModelFromUser(entity.getInitiator()));
		viewModel.setPermanent(entity.isPermanent());
		
		return viewModel;
	}
	
	public List<TaskViewModel> toViewModels(List<Task> entities) {
		List<TaskViewModel> viewModels = new ArrayList<TaskViewModel>();
		for (Task entity : entities) {
			viewModels.add(toViewModel(entity));
		}
		return viewModels;
	}
	
	public List<TaskModel> toModels(List<Task> entities) {
		List<TaskModel> models = new ArrayList<TaskModel>();
		for (Task entity : entities) {
			models.add(toModel(entity));
		}
		return models;
	}
	
	public List<Task> toEntities(List<TaskModel> models) {
		List<Task> entities = new ArrayList<Task>();
		for (TaskModel task : models) {
			entities.add(toEntity(task));
		}
		return entities;
	}
	
	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}
	
	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setTaskAttachmentConverter(TaskAttachmentConverter taskAttachmentConverter) {
		this.taskAttachmentConverter = taskAttachmentConverter;
	}
	
	public void setProjectConverter(ProjectConverter projectConverter) {
		this.projectConverter = projectConverter;
	}
}
