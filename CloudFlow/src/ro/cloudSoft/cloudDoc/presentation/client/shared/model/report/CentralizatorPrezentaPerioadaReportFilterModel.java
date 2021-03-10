package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class CentralizatorPrezentaPerioadaReportFilterModel {

	private Date dataSedintaDeLa;
	private Date dataSedintaPanaLa;
	private Long comisieId;
	private Long bancaId;
	private Long levelId;

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

	public Long getComisieId() {
		return comisieId;
	}

	public void setComisieId(Long comisieId) {
		this.comisieId = comisieId;
	}

	public Long getBancaId() {
		return bancaId;
	}

	public void setBancaId(Long bancaId) {
		this.bancaId = bancaId;
	}

	public Long getLevelId() {
		return levelId;
	}

	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}

}
