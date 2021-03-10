package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class RaspunsuriBanciReportFilterModel {
	
	private String denumireBanca;
	private Long proiectId; 
	private Date termenRaspunsDela;
	private Date termenRaspunsPanaLa;

	public String getDenumireBanca() {
		return denumireBanca;
	}
	public void setDenumireBanca(String denumireBanca) {
		this.denumireBanca = denumireBanca;
	}
	public Long getProiectId() {
		return proiectId;
	}
	public void setProiectId(Long proiectId) {
		this.proiectId = proiectId;
	}
	public Date getTermenRaspunsDela() {
		return termenRaspunsDela;
	}
	public void setTermenRaspunsDela(Date termenRaspunsDela) {
		this.termenRaspunsDela = termenRaspunsDela;
	}
	public Date getTermenRaspunsPanaLa() {
		return termenRaspunsPanaLa;
	}
	public void setTermenRaspunsPanaLa(Date termenRaspunsPanaLa) {
		this.termenRaspunsPanaLa = termenRaspunsPanaLa;
	}

}
