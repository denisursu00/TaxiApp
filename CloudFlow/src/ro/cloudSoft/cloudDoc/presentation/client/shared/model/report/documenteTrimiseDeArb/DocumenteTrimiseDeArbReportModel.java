package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.documenteTrimiseDeArb;

import java.util.Date;

public class DocumenteTrimiseDeArbReportModel {
	
	private Date dataInregistrare;
	private String institutie; 
	private String document; 
	private String numarInregistrareIesire;
	
	public Date getDataInregistrare() {
		return dataInregistrare;
	}
	
	public void setDataInregistrare(Date dataInregistrare) {
		this.dataInregistrare = dataInregistrare;
	}
	
	public String getInstitutie() {
		return institutie;
	}
	
	public void setInstitutie(String institutie) {
		this.institutie = institutie;
	}
	
	public String getDocument() {
		return document;
	}
	
	public void setDocument(String document) {
		this.document = document;
	}
	
	public String getNumarInregistrareIesire() {
		return numarInregistrareIesire;
	}
	
	public void setNumarInregistrareIesire(String numarInregistrareIesire) {
		this.numarInregistrareIesire = numarInregistrareIesire;
	} 

}
