package ro.cloudSoft.cloudDoc.domain.notifications;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ro.cloudSoft.cloudDoc.domain.project.Project;

@Entity
@Table( name = "NOTIFICARE_EXPIRARE_PROIECT")
public class NotificareExpirareProiect {

	private Long id;
	private Project project;
	private Date projectEndDate;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "proiect_id")
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Column(name = "proiect_end_date")
	public Date getProjectEndDate() {
		return projectEndDate;
	}

	public void setProjectEndDate(Date projectEndDate) {
		this.projectEndDate = projectEndDate;
	}
	
}
