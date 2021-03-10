package ro.cloudSoft.cloudDoc.domain;

import java.util.Date;

/**
 * 
 */
public class ConcediuDetailsForPontaj {
	
	private String documentId;
	
	private String solicitantEmail;
	private String solicitantOrganizationUnitName;
	
	private Date dataInceput;
	private Date dataSfarsit;
	
	public String getDocumentId() {
		return documentId;
	}
	public String getSolicitantEmail() {
		return solicitantEmail;
	}
	public String getSolicitantOrganizationUnitName() {
		return solicitantOrganizationUnitName;
	}
	public Date getDataInceput() {
		return dataInceput;
	}
	public Date getDataSfarsit() {
		return dataSfarsit;
	}
	
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public void setSolicitantEmail(String solicitantEmail) {
		this.solicitantEmail = solicitantEmail;
	}
	public void setSolicitantOrganizationUnitName(String solicitantOrganizationUnitName) {
		this.solicitantOrganizationUnitName = solicitantOrganizationUnitName;
	}
	public void setDataInceput(Date dataInceput) {
		this.dataInceput = dataInceput;
	}
	public void setDataSfarsit(Date dataSfarsit) {
		this.dataSfarsit = dataSfarsit;
	}	
}