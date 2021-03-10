package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class CereriConcediuReportFilterModel {

	private Date deLaData;
	private Date panaLaData;
	private String tipConcediu;
	private Long angajatId;
	private String status;
	
	public static final String REPORT_CERERI_CONCEDIU_CONCEDIU_APROBAT = "CONCEDIU_APROBAT";
	public static final String REPORT_CERERI_CONCEDIU_CONCEDIU_RESPINS = "CONCEDIU_RESPINS";

	public Date getDeLaData() {
		return deLaData;
	}

	public void setDeLaData(Date deLaData) {
		this.deLaData = deLaData;
	}

	public Date getPanaLaData() {
		return panaLaData;
	}

	public void setPanaLaData(Date panaLaData) {
		this.panaLaData = panaLaData;
	}

	public String getTipConcediu() {
		return tipConcediu;
	}

	public void setTipConcediu(String tipConcediu) {
		this.tipConcediu = tipConcediu;
	}

	public Long getAngajatId() {
		return angajatId;
	}

	public void setAngajatId(Long angajatId) {
		this.angajatId = angajatId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
