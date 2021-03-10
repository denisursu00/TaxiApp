package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class NivelReprezentareComisiiReportFilterModel {
	
	private Long institutieId;
	private Date dataExpirareMandatDeLa;
	private Date dataExpirareMandatPanaLa;
	
	public Long getInstitutieId() {
		return institutieId;
	}
	public void setInstitutieId(Long institutieId) {
		this.institutieId = institutieId;
	}
	public Date getDataExpirareMandatDeLa() {
		return dataExpirareMandatDeLa;
	}
	public void setDataExpirareMandatDeLa(Date dataExpirareMandatDeLa) {
		this.dataExpirareMandatDeLa = dataExpirareMandatDeLa;
	}
	public Date getDataExpirareMandatPanaLa() {
		return dataExpirareMandatPanaLa;
	}
	public void setDataExpirareMandatPanaLa(Date dataExpirareMandatPanaLa) {
		this.dataExpirareMandatPanaLa = dataExpirareMandatPanaLa;
	}
	
}
