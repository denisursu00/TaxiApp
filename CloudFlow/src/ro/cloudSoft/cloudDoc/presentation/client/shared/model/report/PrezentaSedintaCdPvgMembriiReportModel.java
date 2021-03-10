package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

public class PrezentaSedintaCdPvgMembriiReportModel {

	private int totalMemmbrii;
	private List<PrezentaSedintaCdPvgMembriiReportRowModel> rows;

	public int getTotalMemmbrii() {
		return totalMemmbrii;
	}

	public void setTotalMemmbrii(int totaMemmbrii) {
		this.totalMemmbrii = totaMemmbrii;
	}

	public List<PrezentaSedintaCdPvgMembriiReportRowModel> getRows() {
		return rows;
	}

	public void setRows(List<PrezentaSedintaCdPvgMembriiReportRowModel> rows) {
		this.rows = rows;
	}

}
