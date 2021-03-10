package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.io.Serializable;
import java.util.Date;

public class ParticipareReprezentantiArbLaActiuniInAfaraAsociatieiReportRowModel implements Serializable{

	private Date data;
	private String actiune;
	private String subiectAgenda;
	private String detaliuSubiectAgenda;
	private String participanti;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getActiune() {
		return actiune;
	}

	public void setActiune(String actiune) {
		this.actiune = actiune;
	}

	public String getParticipanti() {
		return participanti;
	}

	public void setParticipanti(String participanti) {
		this.participanti = participanti;
	}

	public String getSubiectAgenda() {
		return subiectAgenda;
	}

	public void setSubiectAgenda(String subiectAgenda) {
		this.subiectAgenda = subiectAgenda;
	}

	public String getDetaliuSubiectAgenda() {
		return detaliuSubiectAgenda;
	}

	public void setDetaliuSubiectAgenda(String detaliuSubiectAgenda) {
		this.detaliuSubiectAgenda = detaliuSubiectAgenda;
	}
}
