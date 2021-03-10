package ro.cloudSoft.cloudDoc.domain.project;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.User;

@Entity
@Table(name = "TASK")
	
public class Task {
	private Long id;
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	private Date finalizedDate;
	private TaskPriority priority;
	private String comments;
	private Project project;
	private List<OrganizationEntity> assignments;
	private List<TaskAttachment> attachments;
	private TaskStatus status;
	private User initiator;
	private boolean permanent;
	private String participationsTo;
	private String explications;
	private Date evenimentStartDate;
	private Date evenimentEndDate;
	private Subactivity subactivity;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "start_date")
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(name = "end_date")
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Column(name = "finalized_date")
	public Date getFinalizedDate() {
		return finalizedDate;
	}

	public void setFinalizedDate(Date finalizedDate) {
		this.finalizedDate = finalizedDate;
	}
	
	@Column(name = "priority")
	@Enumerated(EnumType.STRING)
	public TaskPriority getPriority() {
		return priority;
	}

	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}
	
	@Column(name = "comments")
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(
		name = "task_assignment_oe",
		joinColumns = @JoinColumn(name = "task_id", nullable = false, referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "assignee_org_entity_id", nullable = false, referencedColumnName = "org_entity_id")
	)
	public List<OrganizationEntity> getAssignments() {
		return assignments;
	}

	public void setAssignments(List<OrganizationEntity> assignments) {
		this.assignments = assignments;
	}

	
	@OneToMany(mappedBy = "task")
	@Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<TaskAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<TaskAttachment> attachments) {
		this.attachments = attachments;
	}
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	public TaskStatus getStatus() {
		return status;
	}
	
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "initiator_id", referencedColumnName = "org_entity_id", nullable = false)
	public User getInitiator() {
		return initiator;
	}

	public void setInitiator(User initiator) {
		this.initiator = initiator;
	}
	
	@Column(name = "permanent")
	public boolean isPermanent() {
		return permanent;
	}

	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}
	
	@Column(name = "participations_to")
	public String getParticipationsTo() {
		return participationsTo;
	}

	public void setParticipationsTo(String participationsTo) {
		this.participationsTo = participationsTo;
	}
	
	@Column(name = "explications")
	public String getExplications() {
		return explications;
	}

	public void setExplications(String explications) {
		this.explications = explications;
	}

	@Column(name = "eveniment_start_date")
	public Date getEvenimentStartDate() {
		return evenimentStartDate;
	}

	public void setEvenimentStartDate(Date evenimentStartDate) {
		this.evenimentStartDate = evenimentStartDate;
	}

	@Column(name = "eveniment_end_date")
	public Date getEvenimentEndDate() {
		return evenimentEndDate;
	}

	public void setEvenimentEndDate(Date evenimentEndDate) {
		this.evenimentEndDate = evenimentEndDate;
	}
	
	@OneToOne
	@JoinColumn(name = "subactivity_id", nullable = true)
	public Subactivity getSubactivity() {
		return subactivity;
	}

	public void setSubactivity(Subactivity subactivity) {
		this.subactivity = subactivity;
	}
	
}
