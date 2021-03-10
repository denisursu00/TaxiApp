package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class NotaGeneralaPeMembriiArbReportFilterModel {

	private Date dataSedintaDeLa;
	private Date dataSedintaPanaLa;
	private Long bancaId;

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

	public Long getBancaId() {
		return bancaId;
	}

	public void setBancaId(Long bancaId) {
		this.bancaId = bancaId;
	}

}
