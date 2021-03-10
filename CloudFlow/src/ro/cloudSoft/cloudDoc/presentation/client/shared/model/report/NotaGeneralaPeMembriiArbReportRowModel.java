package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.math.BigDecimal;

public class NotaGeneralaPeMembriiArbReportRowModel {

	private String banca;
	private String comisie;
	private BigDecimal notaFinalaComisie;
	private Integer rankNotaComisie;

	public String getBanca() {
		return banca;
	}

	public void setBanca(String banca) {
		this.banca = banca;
	}

	public String getComisie() {
		return comisie;
	}

	public void setComisie(String comisie) {
		this.comisie = comisie;
	}

	public BigDecimal getNotaFinalaComisie() {
		return notaFinalaComisie;
	}

	public void setNotaFinalaComisie(BigDecimal notaFinalaComisie) {
		this.notaFinalaComisie = notaFinalaComisie;
	}

	public Integer getRankNotaComisie() {
		return rankNotaComisie;
	}

	public void setRankNotaComisie(Integer rankNotaComisie) {
		this.rankNotaComisie = rankNotaComisie;
	}

}
