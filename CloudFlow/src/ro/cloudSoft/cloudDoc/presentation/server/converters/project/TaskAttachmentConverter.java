package ro.cloudSoft.cloudDoc.presentation.server.converters.project;

import java.util.ArrayList;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.project.Task;
import ro.cloudSoft.cloudDoc.domain.project.TaskAttachment;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskAttachmentModel;

public class TaskAttachmentConverter {
	
	public TaskAttachmentModel toModel(TaskAttachment entity) {
		TaskAttachmentModel model = new TaskAttachmentModel();
		model.setId(entity.getId());
		model.setName(entity.getName());
		model.setTaskId(entity.getTask().getId());
		return model;
	}
	
	public TaskAttachment toEntity(TaskAttachmentModel model, Task task) {
		TaskAttachment entity = new TaskAttachment();
		entity.setId(model.getId());
		entity.setName(model.getName());
		entity.setTask(task);
		return entity;
	}
	
	public List<TaskAttachmentModel> toModels(List<TaskAttachment> entities) {
		List<TaskAttachmentModel> models = new ArrayList<TaskAttachmentModel>();
		for (TaskAttachment entity : entities) {
			models.add(toModel(entity));
		}
		return models;
	}
	
	public List<TaskAttachment> toEntities(List<TaskAttachmentModel> models, Task task) {
		List<TaskAttachment> entities = new ArrayList<TaskAttachment>();
		for (TaskAttachmentModel model : models) {
			entities.add(toEntity(model, task));
		}
		return entities;
	}
}
