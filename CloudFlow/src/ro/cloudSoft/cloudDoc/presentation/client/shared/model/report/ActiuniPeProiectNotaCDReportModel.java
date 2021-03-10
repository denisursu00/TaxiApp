package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class ActiuniPeProiectNotaCDReportModel {

	private String numeProiect;
	private String tipNota;
	private String subiectNota;
	private Date dataCdArb;
	
	public String getNumeProiect() {
		return numeProiect;
	}
	public void setNumeProiect(String numeProiect) {
		this.numeProiect = numeProiect;
	}
	public String getTipNota() {
		return tipNota;
	}
	public void setTipNota(String tipNota) {
		this.tipNota = tipNota;
	}
	public String getSubiectNota() {
		return subiectNota;
	}
	public void setSubiectNota(String subiectNota) {
		this.subiectNota = subiectNota;
	}
	public Date getDataCdArb() {
		return dataCdArb;
	}
	public void setDataCdArb(Date dataCdArb) {
		this.dataCdArb = dataCdArb;
	}
}
