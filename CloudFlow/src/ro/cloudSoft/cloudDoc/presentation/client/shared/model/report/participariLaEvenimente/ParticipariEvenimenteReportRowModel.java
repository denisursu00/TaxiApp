package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.participariLaEvenimente;

import java.util.Date;
import java.util.List;

public class ParticipariEvenimenteReportRowModel {

	private String numeProiect;
	private String numeSubproiect;
	private String numeTask;
	private Date dataInceput;
	private Date dataSfarsit;
	private List<String> responsabiliTask;
	private String participariLa;
	private String statusTask;

	public String getNumeProiect() {
		return numeProiect;
	}

	public void setNumeProiect(String numeProiect) {
		this.numeProiect = numeProiect;
	}

	public String getNumeTask() {
		return numeTask;
	}

	public void setNumeTask(String numeTask) {
		this.numeTask = numeTask;
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

	public List<String> getResponsabiliTask() {
		return responsabiliTask;
	}

	public void setResponsabiliTask(List<String> responsabiliTask) {
		this.responsabiliTask = responsabiliTask;
	}

	public String getParticipariLa() {
		return participariLa;
	}

	public void setParticipariLa(String participariLa) {
		this.participariLa = participariLa;
	}

	public String getStatusTask() {
		return statusTask;
	}

	public void setStatusTask(String statusTask) {
		this.statusTask = statusTask;
	}

	public String getNumeSubproiect() {
		return numeSubproiect;
	}

	public void setNumeSubproiect(String numeSubproiect) {
		this.numeSubproiect = numeSubproiect;
	}

}
