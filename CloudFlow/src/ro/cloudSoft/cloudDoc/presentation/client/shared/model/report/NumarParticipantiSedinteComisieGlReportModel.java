package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

public class NumarParticipantiSedinteComisieGlReportModel {

	private Integer totalGeneral;
	private List<NumarParticipantiSedinteComisieGlReportRowModel> rows;

	public Integer getTotalGeneral() {
		return totalGeneral;
	}

	public void setTotalGeneral(Integer totalGeneral) {
		this.totalGeneral = totalGeneral;
	}

	public List<NumarParticipantiSedinteComisieGlReportRowModel> getRows() {
		return rows;
	}

	public void setRows(List<NumarParticipantiSedinteComisieGlReportRowModel> rows) {
		this.rows = rows;
	}

}
