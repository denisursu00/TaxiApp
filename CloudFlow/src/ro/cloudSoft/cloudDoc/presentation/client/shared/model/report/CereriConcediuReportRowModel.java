package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class CereriConcediuReportRowModel {

	private String angajat;
	private String tipConcediu;
	private Date dataInitiere;
	private Date dataInceput;
	private Date dataSfarsit;
	private String status;
	private String motivRespingere;

	public String getAngajat() {
		return angajat;
	}

	public void setAngajat(String angajat) {
		this.angajat = angajat;
	}

	public String getTipConcediu() {
		return tipConcediu;
	}

	public void setTipConcediu(String tipConcediu) {
		this.tipConcediu = tipConcediu;
	}

	public Date getDataInitiere() {
		return dataInitiere;
	}

	public void setDataInitiere(Date dataInitiere) {
		this.dataInitiere = dataInitiere;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMotivRespingere() {
		return motivRespingere;
	}

	public void setMotivRespingere(String motivRespingere) {
		this.motivRespingere = motivRespingere;
	}

}
