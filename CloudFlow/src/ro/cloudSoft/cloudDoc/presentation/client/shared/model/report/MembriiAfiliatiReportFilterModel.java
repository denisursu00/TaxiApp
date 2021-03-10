package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class MembriiAfiliatiReportFilterModel {

	private DocumentIdentifierModel document;
	private Long institutieId;
	private String numeReprezentantAcreditat;
	private String prenumeReprezentantAcreditat;
	private String numeReprezentantInlocuitor;
	private String prenumeReprezentantInlocuitor;
	private Date dataSedintaDeLa;
	private Date dataSedintaPanaLa;
	private Integer comisieId;

	public DocumentIdentifierModel getDocument() {
		return document;
	}

	public void setDocument(DocumentIdentifierModel document) {
		this.document = document;
	}

	public Long getInstitutieId() {
		return institutieId;
	}

	public void setInstitutieId(Long institutieId) {
		this.institutieId = institutieId;
	}

	public String getNumeReprezentantAcreditat() {
		return numeReprezentantAcreditat;
	}

	public void setNumeReprezentantAcreditat(String numeReprezentantAcreditat) {
		this.numeReprezentantAcreditat = numeReprezentantAcreditat;
	}

	public String getPrenumeReprezentantAcreditat() {
		return prenumeReprezentantAcreditat;
	}

	public void setPrenumeReprezentantAcreditat(String prenumeReprezentantAcreditat) {
		this.prenumeReprezentantAcreditat = prenumeReprezentantAcreditat;
	}

	public String getNumeReprezentantInlocuitor() {
		return numeReprezentantInlocuitor;
	}

	public void setNumeReprezentantInlocuitor(String numeReprezentantInlocuitor) {
		this.numeReprezentantInlocuitor = numeReprezentantInlocuitor;
	}

	public String getPrenumeReprezentantInlocuitor() {
		return prenumeReprezentantInlocuitor;
	}

	public void setPrenumeReprezentantInlocuitor(String prenumeReprezentantInlocuitor) {
		this.prenumeReprezentantInlocuitor = prenumeReprezentantInlocuitor;
	}

	public Date getDataSedintaDeLa() {
		return dataSedintaDeLa;
	}

	public void setDataSedintaDeLa(Date dataSedintaDeLa) {
		this.dataSedintaDeLa = dataSedintaDeLa;
	}

	public Date getDataSedintaPanaLa() {
		return dataSedintaPanaLa;
	}

	public void setDataSedintaPanaLa(Date dataSedintaPanaLa) {
		this.dataSedintaPanaLa = dataSedintaPanaLa;
	}

	public Integer getComisieId() {
		return comisieId;
	}

	public void setComisieId(Integer comisieId) {
		this.comisieId = comisieId;
	}

}
