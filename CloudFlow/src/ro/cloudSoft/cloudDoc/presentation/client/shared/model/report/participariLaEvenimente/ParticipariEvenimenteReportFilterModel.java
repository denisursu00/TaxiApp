package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.participariLaEvenimente;

import java.util.Date;
import java.util.List;


public class ParticipariEvenimenteReportFilterModel {

	private Date dataInceput;
	private Date dataSfarsit;
	private Long idUserResponsabilTask;
	private Long idProiect;
	private List<Long> subprojectIds;
	private String statusTask;
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

	public Long getIdUserResponsabilTask() {
		return idUserResponsabilTask;
	}

	public void setIdUserResponsabilTask(Long idUserResponsabilTask) {
		this.idUserResponsabilTask = idUserResponsabilTask;
	}

	public Long getIdProiect() {
		return idProiect;
	}

	public void setIdProiect(Long idProiect) {
		this.idProiect = idProiect;
	}

	public String getStatusTask() {
		return statusTask;
	}

	public void setStatusTask(String statusTask) {
		this.statusTask = statusTask;
	}

	public List<Long> getSubprojectIds() {
		return subprojectIds;
	}

	public void setSubprojectIds(List<Long> subprojectIds) {
		this.subprojectIds = subprojectIds;
	}

}
