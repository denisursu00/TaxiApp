package ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline;

import java.util.Date;

public class DocumentPrezentaOnlineModel {

	private String comisieGl;
	private Long comisieGlId;
	private String numeDocument;
	private Date dataInceput;
	private Date dataSfarsit;
	private String documentLocationRealName;
	private String documentId;
	private boolean finalizata;

	public String getComisieGl() {
		return comisieGl;
	}

	public void setComisieGl(String comisieGl) {
		this.comisieGl = comisieGl;
	}

	public Long getComisieGlId() {
		return comisieGlId;
	}

	public void setComisieGlId(Long comisieGlId) {
		this.comisieGlId = comisieGlId;
	}

	public String getNumeDocument() {
		return numeDocument;
	}

	public void setNumeDocument(String numeDocument) {
		this.numeDocument = numeDocument;
	}

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

	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}

	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public boolean isFinalizata() {
		return finalizata;
	}

	public void setFinalizata(boolean finalizata) {
		this.finalizata = finalizata;
	}
}
