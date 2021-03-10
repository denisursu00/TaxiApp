package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

public class PrezentaSedinteCdPvgInvitatiExterniReportModel {

	private List<PrezentaSedintaCdPvgInvitatiReportRowModel> rows;
	private int totalInvitatiAcreditati;
	private int totalInvitatiInlocuitori;

	public List<PrezentaSedintaCdPvgInvitatiReportRowModel> getRows() {
		return rows;
	}

	public void setRows(List<PrezentaSedintaCdPvgInvitatiReportRowModel> rows) {
		this.rows = rows;
	}

	public int getTotalInvitatiAcreditati() {
		return totalInvitatiAcreditati;
	}

	public void setTotalInvitatiAcreditati(int totalInvitatiAcreditati) {
		this.totalInvitatiAcreditati = totalInvitatiAcreditati;
	}

	public int getTotalInvitatiInlocuitori() {
		return totalInvitatiInlocuitori;
	}

	public void setTotalInvitatiInlocuitori(int totalInvitatiInlocuitori) {
		this.totalInvitatiInlocuitori = totalInvitatiInlocuitori;
	}

}
