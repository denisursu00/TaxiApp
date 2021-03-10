package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

public class NumarSedinteComisieGlReportModel {

	private List<NumarSedinteComisieGlReportRowModel> rows;
	private Integer totalComisie;
	private Integer totalGl;
	private Integer totalGeneral;

	public List<NumarSedinteComisieGlReportRowModel> getRows() {
		return rows;
	}

	public void setRows(List<NumarSedinteComisieGlReportRowModel> rows) {
		this.rows = rows;
	}

	public Integer getTotalComisie() {
		return totalComisie;
	}

	public void setTotalComisie(Integer totalComisie) {
		this.totalComisie = totalComisie;
	}

	public Integer getTotalGl() {
		return totalGl;
	}

	public void setTotalGl(Integer totalGl) {
		this.totalGl = totalGl;
	}

	public Integer getTotalGeneral() {
		return totalGeneral;
	}

	public void setTotalGeneral(Integer totalGeneral) {
		this.totalGeneral = totalGeneral;
	}

}
