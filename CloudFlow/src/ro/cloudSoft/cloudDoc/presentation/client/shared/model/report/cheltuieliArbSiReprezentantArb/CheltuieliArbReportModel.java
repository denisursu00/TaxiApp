package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb;

import java.math.BigDecimal;
import java.util.List;

public class CheltuieliArbReportModel {

	private BigDecimal total;
	private List<CheltuieliArbSiRePrezentantArbRowModel> rows;

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<CheltuieliArbSiRePrezentantArbRowModel> getRows() {
		return rows;
	}

	public void setRows(List<CheltuieliArbSiRePrezentantArbRowModel> rows) {
		this.rows = rows;
	}

}
