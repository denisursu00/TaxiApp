package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class ActiuniPeProiectTaskReportModel {

	private String numeProiect;
	private String numeSubproiect;
	private String tipActiune;
	private String actiuni;
	private Date dataInceputTask;
	private String status;
	
	public String getNumeProiect() {
		return numeProiect;
	}
	
	public void setNumeProiect(String numeProiect) {
		this.numeProiect = numeProiect;
	}
	
	public String getTipActiune() {
		return tipActiune;
	}
	
	public void setTipActiune(String tipActiune) {
		this.tipActiune = tipActiune;
	}
	
	public String getActiuni() {
		return actiuni;
	}
	
	public void setActiuni(String actiuni) {
		this.actiuni = actiuni;
	}
	
	public Date getDataInceputTask() {
		return dataInceputTask;
	}
	
	public void setDataInceputTask(Date dataInceputTask) {
		this.dataInceputTask = dataInceputTask;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public String getNumeSubproiect() {
		return numeSubproiect;
	}

	public void setNumeSubproiect(String numeSubproiect) {
		this.numeSubproiect = numeSubproiect;
	}
	
}
