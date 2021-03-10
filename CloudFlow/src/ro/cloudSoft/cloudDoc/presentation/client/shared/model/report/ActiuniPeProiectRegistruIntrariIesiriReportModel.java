package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class ActiuniPeProiectRegistruIntrariIesiriReportModel {

	private String numeProiect;
	private String numeSubproiect;
	private String tipDocument;
	private String document;
	private Date dataInregistrare;
	private String numarDeInregistrare;
	
	public ActiuniPeProiectRegistruIntrariIesiriReportModel(String numeProiect, String numeSubproiect, String tipDocument, String document, Date dataInregistrare,
			String numarDeInregistrare) {
		this.numeProiect = numeProiect;
		this.numeSubproiect = numeSubproiect;
		this.tipDocument = tipDocument;
		this.document = document;
		this.dataInregistrare = dataInregistrare;
		this.numarDeInregistrare = numarDeInregistrare;
	}

	public String getNumeProiect() {
		return numeProiect;
	}
	
	public void setNumeProiect(String numeProiect) {
		this.numeProiect = numeProiect;
	}
	
	public String getDocument() {
		return document;
	}
	
	public void setDocument(String document) {
		this.document = document;
	}
	
	public Date getDataInregistrare() {
		return dataInregistrare;
	}
	
	public void setDataInregistrare(Date dataInregistrare) {
		this.dataInregistrare = dataInregistrare;
	}
	
	public String getNumarDeInregistrare() {
		return numarDeInregistrare;
	}
	
	public void setNumarDeInregistrare(String numarDeInregistrare) {
		this.numarDeInregistrare = numarDeInregistrare;
	}
	
	public String getTipDocument() {
		return tipDocument;
	}
	
	public void setTipDocument(String tipDocument) {
		this.tipDocument = tipDocument;
	}

	public String getNumeSubproiect() {
		return numeSubproiect;
	}

	public void setNumeSubproiect(String numeSubproiect) {
		this.numeSubproiect = numeSubproiect;
	}
}
