package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

public class NumarSedinteCdPvgSiParticipantiReportModel {
	
	private int totalOfTotalMembriiCD;
	private int totalOfTotalInvitati;	
	private int totalOfTotalInvitatiARB;	
	private int totalOfTotalParticipanti;
	private List<NumarSedinteCdPvgSiParticipantiReportRowModel> rows;
	
	public int getTotalOfTotalMembriiCD() {
		return totalOfTotalMembriiCD;
	}
	public void setTotalOfTotalMembriiCD(int totalOfTotalMembriiCD) {
		this.totalOfTotalMembriiCD = totalOfTotalMembriiCD;
	}
	public int getTotalOfTotalInvitati() {
		return totalOfTotalInvitati;
	}
	public void setTotalOfTotalInvitati(int totalOfTotalInvitati) {
		this.totalOfTotalInvitati = totalOfTotalInvitati;
	}
	public int getTotalOfTotalInvitatiARB() {
		return totalOfTotalInvitatiARB;
	}
	public void setTotalOfTotalInvitatiARB(int totalOfTotalInvitatiARB) {
		this.totalOfTotalInvitatiARB = totalOfTotalInvitatiARB;
	}
	public int getTotalOfTotalParticipanti() {
		return totalOfTotalParticipanti;
	}
	public void setTotalOfTotalParticipanti(int totalOfTotalParticipanti) {
		this.totalOfTotalParticipanti = totalOfTotalParticipanti;
	}
	public List<NumarSedinteCdPvgSiParticipantiReportRowModel> getRows() {
		return rows;
	}
	public void setRows(List<NumarSedinteCdPvgSiParticipantiReportRowModel> rows) {
		this.rows = rows;
	}
}
