package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class TaskuriCumulateReportFilterModel {

	private String zonaTask;
	private Date deLaData;
	private Date panaLaData;
	private Long userAsignat;
	private String status;

	public String getZonaTask() {
		return zonaTask;
	}

	public void setZonaTask(String zonaTask) {
		this.zonaTask = zonaTask;
	}

	public Date getDeLaData() {
		return deLaData;
	}

	public void setDeLaData(Date deLaData) {
		this.deLaData = deLaData;
	}

	public Date getPanaLaData() {
		return panaLaData;
	}

	public void setPanaLaData(Date panaLaData) {
		this.panaLaData = panaLaData;
	}

	public Long getUserAsignat() {
		return userAsignat;
	}

	public void setUserAsignat(Long userAsignat) {
		this.userAsignat = userAsignat;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
