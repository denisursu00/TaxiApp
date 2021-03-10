package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class NumarSedinteCdPvgSiParticipantiReportRowModel {
	
	private Date dataSedinta;
	private int totalMembriiCD;
	private int totalInvitati;	
	private int totalInvitatiARB;	
	private int totalParticipanti;
	
	public Date getDataSedinta() {
		return dataSedinta;
	}
	public void setDataSedinta(Date dataSedinta) {
		this.dataSedinta = dataSedinta;
	}
	public int getTotalMembriiCD() {
		return totalMembriiCD;
	}
	public void setTotalMembriiCD(int totalMembriiCD) {
		this.totalMembriiCD = totalMembriiCD;
	}
	public int getTotalInvitati() {
		return totalInvitati;
	}
	public void setTotalInvitati(int totalInvitati) {
		this.totalInvitati = totalInvitati;
	}
	public int getTotalInvitatiARB() {
		return totalInvitatiARB;
	}
	public void setTotalInvitatiARB(int totalInvitatiARB) {
		this.totalInvitatiARB = totalInvitatiARB;
	}
	public int getTotalParticipanti() {
		return totalParticipanti;
	}
	public void setTotalParticipanti(int totalParticipanti) {
		this.totalParticipanti = totalParticipanti;
	}
}
