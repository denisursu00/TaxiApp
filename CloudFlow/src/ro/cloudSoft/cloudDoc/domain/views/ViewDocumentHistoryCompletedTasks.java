package ro.cloudSoft.cloudDoc.domain.views;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;


@Entity
@Immutable
@Table(name = "document_history_completed_tasks")
public class ViewDocumentHistoryCompletedTasks {
	
	private Long fakeId;
	private String documentId;
	private String documentLocationRealName;
	private String documentWorkflowStatus;
	private String assigneeUserId;
	private String assigneeUseFirstname;
	private String assigneeUserLastname;
	private Date taskCreatedDate;
	private Date taskEndedDate;
	
	@Id
	@Column(name = "fake_id")
	public Long getFakeId() {
		return fakeId;
	}

	public void setFakeId(Long fakeId) {
		this.fakeId = fakeId;
	}

	@Column(name = "document_id")
	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	@Column(name = "document_location_real_name")
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}

	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}

	@Column(name = "document_workflow_status")
	public String getDocumentWorkflowStatus() {
		return documentWorkflowStatus;
	}

	public void setDocumentWorkflowStatus(String documentWorkflowStatus) {
		this.documentWorkflowStatus = documentWorkflowStatus;
	}

	@Column(name = "assignee_user_id")
	public String getAssigneeUserId() {
		return assigneeUserId;
	}

	public void setAssigneeUserId(String assigneeUserId) {
		this.assigneeUserId = assigneeUserId;
	}

	@Column(name = "assignee_user_firstname")
	public String getAssigneeUseFirstname() {
		return assigneeUseFirstname;
	}

	public void setAssigneeUseFirstname(String assigneeUseFirstname) {
		this.assigneeUseFirstname = assigneeUseFirstname;
	}

	@Column(name = "assignee_user_lastname")
	public String getAssigneeUserLastname() {
		return assigneeUserLastname;
	}

	public void setAssigneeUserLastname(String assigneeUserLastname) {
		this.assigneeUserLastname = assigneeUserLastname;
	}

	@Column(name = "task_created_date")
	public Date getTaskCreatedDate() {
		return taskCreatedDate;
	}

	public void setTaskCreatedDate(Date taskCreatedDate) {
		this.taskCreatedDate = taskCreatedDate;
	}

	@Column(name = "task_ended_date")
	public Date getTaskEndedDate() {
		return taskEndedDate;
	}

	public void setTaskEndedDate(Date taskEndedDate) {
		this.taskEndedDate = taskEndedDate;
	}

}
