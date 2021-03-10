package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class PrezentaSedintaCdPvgMembriiReportFilterModel {

	private String tipSedinta;
	private Date dataSedintaDeLa;	
	private Date dataSedintaPanaLa;
	private String institutieMembru;
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
	public String getInstitutieMembru() {
		return institutieMembru;
	}
	public void setInstitutieMembru(String institutieMembru) {
		this.institutieMembru = institutieMembru;
	}
	
}
