package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb;

import java.math.BigDecimal;
import java.util.List;

public class CheltuieliReprezentantArbReportModel {

	private BigDecimal total;
	private BigDecimal avans;
	private String avansModalitatePlata;
	private BigDecimal diferenta;
	private List<CheltuieliArbSiRePrezentantArbRowModel> rows;

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getAvans() {
		return avans;
	}

	public void setAvans(BigDecimal avans) {
		this.avans = avans;
	}

	public String getAvansModalitatePlata() {
		return avansModalitatePlata;
	}

	public void setAvansModalitatePlata(String avansModalitatePlata) {
		this.avansModalitatePlata = avansModalitatePlata;
	}

	public BigDecimal getDiferenta() {
		return diferenta;
	}

	public void setDiferenta(BigDecimal diferenta) {
		this.diferenta = diferenta;
	}

	public List<CheltuieliArbSiRePrezentantArbRowModel> getRows() {
		return rows;
	}

	public void setRows(List<CheltuieliArbSiRePrezentantArbRowModel> rows) {
		this.rows = rows;
	}

}
