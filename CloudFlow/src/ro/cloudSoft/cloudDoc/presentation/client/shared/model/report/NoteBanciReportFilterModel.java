package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;
import java.util.List;

public class NoteBanciReportFilterModel {
	
	private Long institutieId;
	private Date dataInceputSedintaDeLa;
	private Date dataInceputSedintaPanaLa;
	
	public Long getInstitutieId() {
		return institutieId;
	}
	public void setInstitutieId(Long institutieId) {
		this.institutieId = institutieId;
	}
	public Date getDataInceputSedintaDeLa() {
		return dataInceputSedintaDeLa;
	}
	public void setDataInceputSedintaDeLa(Date dataInceputSedintaDeLa) {
		this.dataInceputSedintaDeLa = dataInceputSedintaDeLa;
	}
	public Date getDataInceputSedintaPanaLa() {
		return dataInceputSedintaPanaLa;
	}
	public void setDataInceputSedintaPanaLa(Date dataInceputSedintaPanaLa) {
		this.dataInceputSedintaPanaLa = dataInceputSedintaPanaLa;
	}
	
}
