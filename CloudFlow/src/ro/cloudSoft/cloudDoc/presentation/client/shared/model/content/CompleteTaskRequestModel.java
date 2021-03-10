package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.TaskAttachmentModel;

public class CompleteTaskRequestModel {
	
	private Long taskId;
	private String comments;
	private List<TaskAttachmentModel> attachments;
	
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public List<TaskAttachmentModel> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<TaskAttachmentModel> attachments) {
		this.attachments = attachments;
	}
}
