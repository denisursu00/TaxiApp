package ro.cloudSoft.cloudDoc.domain.project;

import java.util.Date;
import java.util.List;

public class TasksViewModel {

	private Long projectId;

	private String numeProiect;
	private String abreviereProiect;
	private String descriere;
	private String utilizatorResponsabil;
	private Date dataInceputProiect;
	private Date dataSfarsitProiect;
	private Date dataImplementarii;
	private Integer gradDeRealizareEstimatDeResponsabil;
	private List<TasksComisieGlViewModel> comisiiGlImplicate;
	private List<TasksParticipantViewModel> participanti;
	private List<TasksActivitateViewModel> activitati;

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getNumeProiect() {
		return numeProiect;
	}

	public void setNumeProiect(String numeProiect) {
		this.numeProiect = numeProiect;
	}

	public String getAbreviereProiect() {
		return abreviereProiect;
	}

	public void setAbreviereProiect(String abreviereProiect) {
		this.abreviereProiect = abreviereProiect;
	}

	public String getDescriere() {
		return descriere;
	}

	public void setDescriere(String descriere) {
		this.descriere = descriere;
	}

	public String getUtilizatorResponsabil() {
		return utilizatorResponsabil;
	}

	public void setUtilizatorResponsabil(String utilizatorResponsabil) {
		this.utilizatorResponsabil = utilizatorResponsabil;
	}

	public Date getDataInceputProiect() {
		return dataInceputProiect;
	}

	public void setDataInceputProiect(Date dataInceputProiect) {
		this.dataInceputProiect = dataInceputProiect;
	}

	public Date getDataSfarsitProiect() {
		return dataSfarsitProiect;
	}

	public void setDataSfarsitProiect(Date dataSfarsitProiect) {
		this.dataSfarsitProiect = dataSfarsitProiect;
	}

	public Date getDataImplementarii() {
		return dataImplementarii;
	}

	public void setDataImplementarii(Date dataImplementarii) {
		this.dataImplementarii = dataImplementarii;
	}

	public Integer getGradDeRealizareEstimatDeResponsabil() {
		return gradDeRealizareEstimatDeResponsabil;
	}

	public void setGradDeRealizareEstimatDeResponsabil(Integer gradDeRealizareEstimatDeResponsabil) {
		this.gradDeRealizareEstimatDeResponsabil = gradDeRealizareEstimatDeResponsabil;
	}

	public List<TasksComisieGlViewModel> getComisiiGlImplicate() {
		return comisiiGlImplicate;
	}

	public void setComisiiGlImplicate(List<TasksComisieGlViewModel> comisiiGlImplicate) {
		this.comisiiGlImplicate = comisiiGlImplicate;
	}

	public List<TasksParticipantViewModel> getParticipanti() {
		return participanti;
	}

	public void setParticipanti(List<TasksParticipantViewModel> participanti) {
		this.participanti = participanti;
	}

	public List<TasksActivitateViewModel> getActivitati() {
		return activitati;
	}

	public void setActivitati(List<TasksActivitateViewModel> activitati) {
		this.activitati = activitati;
	}

}
