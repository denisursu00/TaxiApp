package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

public class PrezentaSedintaCdPvgInvitatiARBReportModel {
	
	private int totalInvitatiARB;
	private List<PrezentaSedintaCdPvgInvitatiARBReportRowModel> rows;

	public int getTotalInvitatiARB() {
		return totalInvitatiARB;
	}
	public void setTotalInvitatiARB(int totalInvitatiARB) {
		this.totalInvitatiARB = totalInvitatiARB;
	}
	public List<PrezentaSedintaCdPvgInvitatiARBReportRowModel> getRows() {
		return rows;
	}
	public void setRows(List<PrezentaSedintaCdPvgInvitatiARBReportRowModel> rows) {
		this.rows = rows;
	}
		
}
