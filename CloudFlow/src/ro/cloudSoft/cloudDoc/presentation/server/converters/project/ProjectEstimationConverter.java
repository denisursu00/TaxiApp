package ro.cloudSoft.cloudDoc.presentation.server.converters.project;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.dao.project.ProjectDao;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.ProjectEstimation;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.ProjectEstimationModel;

public class ProjectEstimationConverter {
	
	private ProjectDao projectDao;
	
	public ProjectEstimationModel toModel(ProjectEstimation entity) {
		ProjectEstimationModel model = new ProjectEstimationModel();
		model.setId(entity.getId());
		model.setEstimationInPercent(entity.getEstimationInPercent());
		model.setStartDate(entity.getStartDate());
		model.setEndDate(entity.getEndDate());
		model.setProjectId(entity.getProject().getId());
		
		return model;
	}
	
	public ProjectEstimation toEntity(ProjectEstimationModel model, Project project) {
		ProjectEstimation entity = null;
		if (model.getId() != null) {
			entity = projectDao.findProjectEstimationById(model.getId());
		} else {
			entity = new ProjectEstimation();
		}
		entity.setId(model.getId());
		entity.setEstimationInPercent(model.getEstimationInPercent());
		entity.setStartDate(model.getStartDate());
		entity.setEndDate(model.getEndDate());
		entity.setProject(project);
		
		return entity;
	}
	
	public List<ProjectEstimationModel> toModels(List<ProjectEstimation> entities) {
		List<ProjectEstimationModel> models = new ArrayList<>();
		for (ProjectEstimation entity : entities) {
			models.add(toModel(entity));
		}
		return models;
	}
	
	public List<ProjectEstimation> toEntities(List<ProjectEstimationModel> models, Project project) {
		List<ProjectEstimation> entities = new ArrayList<>();
		for (ProjectEstimationModel model : models) {
			entities.add(toEntity(model, project));
		}
		return entities;
	}
	
	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}
}
