package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.math.BigDecimal;
import java.util.Date;

public class DecontCheltuieliAlteDeconturiReportRowModel {
	
	private String titular;
	private Date dataDocumentJustificativ;
	private String numarDocumentJustificativ;
	private String explicatie;
	private BigDecimal  suma;
	
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
	
	public String getNumarDocumentJustificativ() {
		return numarDocumentJustificativ;
	}
	
	public void setNumarDocumentJustificativ(String numarDocumentJustificativ) {
		this.numarDocumentJustificativ = numarDocumentJustificativ;
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
