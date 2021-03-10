package ro.cloudSoft.cloudDoc.presentation.client.shared.model.cursValutar;

import java.math.BigDecimal;
import java.util.Date;

public class CursValutarModel {

	private Long id;
	private BigDecimal eur;
	private BigDecimal usd;
	private Date data;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getEur() {
		return eur;
	}
	public void setEur(BigDecimal eur) {
		this.eur = eur;
	}
	public BigDecimal getUsd() {
		return usd;
	}
	public void setUsd(BigDecimal usd) {
		this.usd = usd;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
}
