package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class PrezentaSedintaCdPvgInvitatiExterniFilterModel {
	private String tipSedinta;
	private Date dataSedintaDeLa;
	private Date dataSedintaPanaLa;
	private Long institutieInvitatId;
	private Long invitatAcreditatId;
	private String invitatInlocuitorNume;
	private String invitatInlocuitorPrenume;
	
	public String getTipSedinta() {
		return tipSedinta;
	}
	public void setTipSedinta(String tipSedinta) {
		this.tipSedinta = tipSedinta;
	}
	public Date getDataSedintaDeLa() {
		return dataSedintaDeLa;
	}
	public void setDataSedintaDeLa(Date dataSedintaDeLa) {
		this.dataSedintaDeLa = dataSedintaDeLa;
	}
	public Date getDataSedintaPanaLa() {
		return dataSedintaPanaLa;
	}
	public void setDataSedintaPanaLa(Date dataSedintaPanaLa) {
		this.dataSedintaPanaLa = dataSedintaPanaLa;
	}
	public Long getInstitutieInvitatId() {
		return institutieInvitatId;
	}
	public void setInstitutieInvitatId(Long institutieInvitatId) {
		this.institutieInvitatId = institutieInvitatId;
	}
	public Long getInvitatAcreditatId() {
		return invitatAcreditatId;
	}
	public void setInvitatAcreditatId(Long invitatAcreditatId) {
		this.invitatAcreditatId = invitatAcreditatId;
	}
	public String getInvitatInlocuitorNume() {
		return invitatInlocuitorNume;
	}
	public void setInvitatInlocuitorNume(String invitatInlocuitorNume) {
		this.invitatInlocuitorNume = invitatInlocuitorNume;
	}
	public String getInvitatInlocuitorPrenume() {
		return invitatInlocuitorPrenume;
	}
	public void setInvitatInlocuitorPrenume(String invitatInlocuitorPrenume) {
		this.invitatInlocuitorPrenume = invitatInlocuitorPrenume;
	}


}
