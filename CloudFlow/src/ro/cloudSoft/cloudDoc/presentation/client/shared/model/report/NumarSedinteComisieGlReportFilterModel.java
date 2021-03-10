package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class NumarSedinteComisieGlReportFilterModel {

	private Date dataSedintaDeLa;
	private Date dataSedintaPanaLa;
	private Long comisieId;
	private DocumentIdentifierModel document;

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

	public Long getComisieId() {
		return comisieId;
	}

	public void setComisieId(Long comisieId) {
		this.comisieId = comisieId;
	}

	public DocumentIdentifierModel getDocument() {
		return document;
	}

	public void setDocument(DocumentIdentifierModel document) {
		this.document = document;
	}

}
