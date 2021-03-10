package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;
import java.util.List;

public class ActiuniPeProiectReportFilterModel {
	
	private Date dataInceput;
	private Date dataSfarsit;
	private Long proiectId;
	private List<Long> subprojectIds;
	
	public Date getDataInceput() {
		return dataInceput;
	}
	
	public void setDataInceput(Date dataInceput) {
		this.dataInceput = dataInceput;
	}
	
	public Date getDataSfarsit() {
		return dataSfarsit;
	}
	
	public void setDataSfarsit(Date dataSfarsit) {
		this.dataSfarsit = dataSfarsit;
	}
	
	public Long getProiectId() {
		return proiectId;
	}
	
	public void setProiectId(Long proiectId) {
		this.proiectId = proiectId;
	}

	public List<Long> getSubprojectIds() {
		return subprojectIds;
	}

	public void setSubprojectIds(List<Long> subprojectIds) {
		this.subprojectIds = subprojectIds;
	}

}
