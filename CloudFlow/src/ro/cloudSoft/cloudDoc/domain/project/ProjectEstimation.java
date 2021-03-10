package ro.cloudSoft.cloudDoc.domain.project;

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

@Entity
@Table(name="PROJECT_ESTIMATION")
public class ProjectEstimation {
	
	private Long id;
	private Integer estimationInPercent;
	private Date startDate;
	private Date endDate;
	private Project project;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "estimation_in_percent")
	public Integer getEstimationInPercent() {
		return estimationInPercent;
	}
	
	public void setEstimationInPercent(Integer estimationInPercent) {
		this.estimationInPercent = estimationInPercent;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}
}
