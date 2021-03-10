package ro.cloudSoft.cloudDoc.domain.project;

import java.util.Date;
import java.util.List;

public class TasksActivitateViewModel {

	private Long id;
	private String denumire;
	private String descriere;
	private Date dataInceput;
	private Date dataSfarsit;
	private TaskPriority prioritate;
	private String responsabil;
	private String participareLa;
	private String explicatii;
	private String comentarii;
	private TaskStatus status;
	private List<TasksActivitateAttachmentViewModel> attachments;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDenumire() {
		return denumire;
	}

	public void setDenumire(String denumire) {
		this.denumire = denumire;
	}

	public String getDescriere() {
		return descriere;
	}

	public void setDescriere(String descriere) {
		this.descriere = descriere;
	}

	public Date getDataInceput() {
		return dataInceput;
	}

	public void setDataInceput(Date dataInceput) {
		this.dataInceput = dataInceput;
	}

	public Date getDataSfarsit() {
		return dataSfarsit;
	}

	public void setDataSfarsit(Date dataSfarsit) {
		this.dataSfarsit = dataSfarsit;
	}

	public TaskPriority getPrioritate() {
		return prioritate;
	}

	public void setPrioritate(TaskPriority prioritate) {
		this.prioritate = prioritate;
	}

	public String getResponsabil() {
		return responsabil;
	}

	public void setResponsabil(String responsabil) {
		this.responsabil = responsabil;
	}

	public String getParticipareLa() {
		return participareLa;
	}

	public void setParticipareLa(String participareLa) {
		this.participareLa = participareLa;
	}

	public String getExplicatii() {
		return explicatii;
	}

	public void setExplicatii(String explicatii) {
		this.explicatii = explicatii;
	}

	public String getComentarii() {
		return comentarii;
	}

	public void setComentarii(String comentarii) {
		this.comentarii = comentarii;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public List<TasksActivitateAttachmentViewModel> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<TasksActivitateAttachmentViewModel> attachments) {
		this.attachments = attachments;
	}

}
