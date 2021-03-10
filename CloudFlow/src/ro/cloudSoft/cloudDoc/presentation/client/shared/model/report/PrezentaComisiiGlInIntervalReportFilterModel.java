package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class PrezentaComisiiGlInIntervalReportFilterModel {

	private DocumentIdentifierModel document;
	private Long institutieId;
	private String participantAcreditat;
	private String numeParticipantInlocuitor;
	private String prenumeParticipantInlocuitor;
	private String functie;

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

	public String getParticipantAcreditat() {
		return participantAcreditat;
	}

	public void setParticipantAcreditat(String participantAcreditat) {
		this.participantAcreditat = participantAcreditat;
	}

	public String getNumeParticipantInlocuitor() {
		return numeParticipantInlocuitor;
	}

	public void setNumeParticipantInlocuitor(String numeParticipantInlocuitor) {
		this.numeParticipantInlocuitor = numeParticipantInlocuitor;
	}

	public String getPrenumeParticipantInlocuitor() {
		return prenumeParticipantInlocuitor;
	}

	public void setPrenumeParticipantInlocuitor(String prenumeParticipantInlocuitor) {
		this.prenumeParticipantInlocuitor = prenumeParticipantInlocuitor;
	}

	public String getFunctie() {
		return functie;
	}

	public void setFunctie(String functie) {
		this.functie = functie;
	}


}
