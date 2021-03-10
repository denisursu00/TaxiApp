package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

public class NotaGeneralaPeMembriiArbReportMapModel {

	private Long bancaId;
	private Long comisieId;
	private String functia;
	private String calitatea;

	public Long getBancaId() {
		return bancaId;
	}

	public void setBancaId(Long bancaId) {
		this.bancaId = bancaId;
	}

	public Long getComisieId() {
		return comisieId;
	}

	public void setComisieId(Long comisieId) {
		this.comisieId = comisieId;
	}

	public String getFunctia() {
		return functia;
	}

	public void setFunctia(String functia) {
		this.functia = functia;
	}

	public String getCalitatea() {
		return calitatea;
	}

	public void setCalitatea(String calitatea) {
		this.calitatea = calitatea;
	}

	@Override
	public String toString() {
		return "NotaGeneralaPeMembriiArbReportMapModel [bancaId=" + bancaId + ", comisieId=" + comisieId + ", functia=" + functia + ", calitatea=" + calitatea + "]";
	}

}
