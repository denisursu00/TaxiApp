package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class PrezentaAgaFilterModel {

	private String documentId;
	private Long bancaId;
	private String functie;
	private Date deLaDataInceput;
	private Date panaLaDataInceput;

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public Long getBancaId() {
		return bancaId;
	}

	public void setBancaId(Long bancaId) {
		this.bancaId = bancaId;
	}

	public String getFunctie() {
		return functie;
	}

	public void setFunctie(String functie) {
		this.functie = functie;
	}

	public Date getDeLaDataInceput() {
		return deLaDataInceput;
	}

	public void setDeLaDataInceput(Date deLaDataInceput) {
		this.deLaDataInceput = deLaDataInceput;
	}

	public Date getPanaLaDataInceput() {
		return panaLaDataInceput;
	}

	public void setPanaLaDataInceput(Date panaLaDataInceput) {
		this.panaLaDataInceput = panaLaDataInceput;
	}

}
