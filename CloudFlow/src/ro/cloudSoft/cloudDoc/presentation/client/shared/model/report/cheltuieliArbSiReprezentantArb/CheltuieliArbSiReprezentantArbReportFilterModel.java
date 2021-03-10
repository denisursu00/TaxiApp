package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb;

import java.util.Date;


public class CheltuieliArbSiReprezentantArbReportFilterModel {

	private String titular;
	private Long organismId;
	private String comitet;
	private Date dataDecontDeLa;
	private Date dataDecontPanaLa;
	private String numarDecizie;
	private String valuta;

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public Long getOrganismId() {
		return organismId;
	}

	public void setOrganismId(Long organismId) {
		this.organismId = organismId;
	}

	public String getComitet() {
		return comitet;
	}

	public void setComitet(String comitet) {
		this.comitet = comitet;
	}

	public Date getDataDecontDeLa() {
		return dataDecontDeLa;
	}

	public void setDataDecontDeLa(Date dataDecontDeLa) {
		this.dataDecontDeLa = dataDecontDeLa;
	}

	public Date getDataDecontPanaLa() {
		return dataDecontPanaLa;
	}

	public void setDataDecontPanaLa(Date dataDecontPanaLa) {
		this.dataDecontPanaLa = dataDecontPanaLa;
	}

	public String getNumarDecizie() {
		return numarDecizie;
	}

	public void setNumarDecizie(String numarDecizie) {
		this.numarDecizie = numarDecizie;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

}
