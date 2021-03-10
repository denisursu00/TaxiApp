package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.deplasareDecont;

import java.math.BigDecimal;
import java.util.Date;

public class CheltuieliReprezentantArbReportRowModel {

	private String titular;
	private Date dataDocumentJustificativ;
	private String nrDocumentJustificativ;
	private String explicatie;
	private BigDecimal suma;
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	public Date getDataDocumentJustificativ() {
		return dataDocumentJustificativ;
	}
	public void setDataDocumentJustificativ(Date dataDocumentJustificativ) {
		this.dataDocumentJustificativ = dataDocumentJustificativ;
	}
	public String getNrDocumentJustificativ() {
		return nrDocumentJustificativ;
	}
	public void setNrDocumentJustificativ(String nrDocumentJustificativ) {
		this.nrDocumentJustificativ = nrDocumentJustificativ;
	}
	public String getExplicatie() {
		return explicatie;
	}
	public void setExplicatie(String explicatie) {
		this.explicatie = explicatie;
	}
	public BigDecimal getSuma() {
		return suma;
	}
	public void setSuma(BigDecimal suma) {
		this.suma = suma;
	}
	
}
