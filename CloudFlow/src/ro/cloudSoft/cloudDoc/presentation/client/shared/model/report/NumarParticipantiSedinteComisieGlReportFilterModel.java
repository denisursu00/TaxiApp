package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class NumarParticipantiSedinteComisieGlReportFilterModel {

	private Date dataSedintaDeLa;
	private Date dataSedintaPanaLa;
	private Long comisieId;
	private Long bancaId;
	private String functie;
	private String departament;
	private String calitateMembru;
	private Long responsabilId;

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

	public String getFunctie() {
		return functie;
	}

	public void setFunctie(String functie) {
		this.functie = functie;
	}

	public String getDepartament() {
		return departament;
	}

	public void setDepartament(String departament) {
		this.departament = departament;
	}

	public String getCalitateMembru() {
		return calitateMembru;
	}

	public void setCalitateMembru(String calitateMembru) {
		this.calitateMembru = calitateMembru;
	}

	public Long getResponsabilId() {
		return responsabilId;
	}

	public void setResponsabilId(Long responsabilId) {
		this.responsabilId = responsabilId;
	}

}
