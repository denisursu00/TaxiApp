package ro.cloudSoft.cloudDoc.domain.bpm.notifications;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ro.cloudSoft.cloudDoc.domain.bpm.WorkflowTransition;

@Entity
@Table(name = "transition_notifications")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class TransitionNotification {
	
	private Long id;
	
	private WorkflowTransition transition;
	
	private String emailSubjectTemplate;
	private String emailContentTemplate;
	
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "transition_id", referencedColumnName = "id", nullable = false)
	public WorkflowTransition getTransition() {
		return transition;
	}
	
	@Column(name = "email_subject_template", nullable = false)
	public String getEmailSubjectTemplate() {
		return emailSubjectTemplate;
	}
	
	@Column(name = "email_content_template", nullable = false)
	public String getEmailContentTemplate() {
		return emailContentTemplate;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public void setTransition(WorkflowTransition transition) {
		this.transition = transition;
	}
	public void setEmailSubjectTemplate(String emailSubjectTemplate) {
		this.emailSubjectTemplate = emailSubjectTemplate;
	}
	public void setEmailContentTemplate(String emailContentTemplate) {
		this.emailContentTemplate = emailContentTemplate;
	}
}