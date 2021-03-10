package ro.cloudSoft.cloudDoc.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 */
//@Entity
//@Table(name = "CONCEDII")
public class Concediu {
	
	private String documentId;
	
	private Long solicitantId;
	
	private Date dataInceput;
	private Date dataSfarsit;
	
	private boolean cerereAprobata;
	
	@Id
	@Column(name = "DOCUMENT_ID", length = 32, nullable = false)
	public String getDocumentId() {
		return documentId;
	}
	
	@Column(name = "SOLICITANT_ID", nullable = false)
	public Long getSolicitantId() {
		return solicitantId;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_INCEPUT", nullable = false)
	public Date getDataInceput() {
		return dataInceput;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_SFARSIT", nullable = false)
	public Date getDataSfarsit() {
		return dataSfarsit;
	}
	
	@Column(name = "IS_CERERE_APROBATA", nullable = false)
	public boolean isCerereAprobata() {
		return cerereAprobata;
	}
	
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
		
	public void setSolicitantId(Long solicitantId) {
		this.solicitantId = solicitantId;
	}
	
	public void setDataInceput(Date dataInceput) {
		this.dataInceput = dataInceput;
	}
	
	public void setDataSfarsit(Date dataSfarsit) {
		this.dataSfarsit = dataSfarsit;
	}
	
	public void setCerereAprobata(boolean cerereAprobata) {
		this.cerereAprobata = cerereAprobata;
	}
}