package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;
import java.util.List;

public class PrezentaSedintaCdPvgMembriiReportRowModel {
	
	private String tipSedinta;
	private Date dataSedinta;
	private String institutieMembru;
	private String membru;
	
	public String getTipSedinta() {
		return tipSedinta;
	}
	public void setTipSedinta(String tipSedinta) {
		this.tipSedinta = tipSedinta;
	}
	public Date getDataSedinta() {
		return dataSedinta;
	}
	public void setDataSedinta(Date dataSedinta) {
		this.dataSedinta = dataSedinta;
	}
	public String getInstitutieMembru() {
		return institutieMembru;
	}
	public void setInstitutieMembru(String institutieMembru) {
		this.institutieMembru = institutieMembru;
	}
	public String getMembru() {
		return membru;
	}
	public void setMembru(String membru) {
		this.membru = membru;
	}
	
	
}
