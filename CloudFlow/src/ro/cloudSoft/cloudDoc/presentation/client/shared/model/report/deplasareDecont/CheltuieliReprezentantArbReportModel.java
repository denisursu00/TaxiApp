package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.deplasareDecont;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CheltuieliReprezentantArbReportModel {

	private String titular;
	private String numarInregistrareDecont;
	private Date dataDecont;
	private BigDecimal avansPrimitCard;
	private BigDecimal avansPrimitNumerar;
	private BigDecimal diferentaDeIncasat;
	private BigDecimal diferentaDeRestituit;
	private List <CheltuieliReprezentantArbReportRowModel> rows;
	private BigDecimal totalCheltuieli;
	
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	public String getNumarInregistrareDecont() {
		return numarInregistrareDecont;
	}
	public void setNumarInregistrareDecont(String numarInregistrareDecontCheltuieli) {
		this.numarInregistrareDecont = numarInregistrareDecontCheltuieli;
	}
	public Date getDataDecont() {
		return dataDecont;
	}
	public void setDataDecont(Date dataDecont) {
		this.dataDecont = dataDecont;
	}
	public BigDecimal getAvansPrimitCard() {
		return avansPrimitCard;
	}
	public void setAvansPrimitCard(BigDecimal avansPrimitCard) {
		this.avansPrimitCard = avansPrimitCard;
	}
	public BigDecimal getAvansPrimitNumerar() {
		return avansPrimitNumerar;
	}
	public void setAvansPrimitNumerar(BigDecimal avansPrimitNumerar) {
		this.avansPrimitNumerar = avansPrimitNumerar;
	}
	public BigDecimal getDiferentaDeIncasat() {
		return diferentaDeIncasat;
	}
	public void setDiferentaDeIncasat(BigDecimal diferentaDeIncasat) {
		this.diferentaDeIncasat = diferentaDeIncasat;
	}
	public BigDecimal getDiferentaDeRestituit() {
		return diferentaDeRestituit;
	}
	public void setDiferentaDeRestituit(BigDecimal diferentaDeRestituit) {
		this.diferentaDeRestituit = diferentaDeRestituit;
	}
	public List<CheltuieliReprezentantArbReportRowModel> getRows() {
		return rows;
	}
	public void setRows(List<CheltuieliReprezentantArbReportRowModel> rows) {
		this.rows = rows;
	}
	public BigDecimal getTotalCheltuieli() {
		return totalCheltuieli;
	}
	public void setTotalCheltuieli(BigDecimal totalCheltuieli) {
		this.totalCheltuieli = totalCheltuieli;
	}
}
