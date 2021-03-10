package ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri;

import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.AtasamentModel;

public class RegistruIesiriViewModel {

	private Long id;
	private String numarInregistrare;
	private Date dataInregistrare;
	private String tipDocument;
	private String codTipDocument;
	private Integer numarPagini;
	private Integer numarAnexe;
	private String intocmitDeUser;
	private boolean trimisPeMail;
	private String continut;
	private boolean asteptamRaspuns;
	private Date termenRaspuns;
	private List<RegistruIesiriDestinatarViewModel> destinatari;
	private String numeProiecteConcatenate;
	private boolean anulat;
	private String motivAnulare;
	private boolean inchis;
	private boolean trebuieAnulat;
	private List<AtasamentModel> atasamente;
	private String subactivityName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNumarInregistrare() {
		return numarInregistrare;
	}
	public void setNumarInregistrare(String numarInregistrare) {
		this.numarInregistrare = numarInregistrare;
	}
	public Date getDataInregistrare() {
		return dataInregistrare;
	}
	public void setDataInregistrare(Date dataInregistrare) {
		this.dataInregistrare = dataInregistrare;
	}
	public String getTipDocument() {
		return tipDocument;
	}
	public void setTipDocument(String tipDocument) {
		this.tipDocument = tipDocument;
	}
	public String getCodTipDocument() {
		return codTipDocument;
	}
	public void setCodTipDocument(String codTipDocument) {
		this.codTipDocument = codTipDocument;
	}
	public Integer getNumarPagini() {
		return numarPagini;
	}
	public void setNumarPagini(Integer numarPagini) {
		this.numarPagini = numarPagini;
	}
	public Integer getNumarAnexe() {
		return numarAnexe;
	}
	public void setNumarAnexe(Integer numarAnexe) {
		this.numarAnexe = numarAnexe;
	}
	public String getIntocmitDeUser() {
		return intocmitDeUser;
	}
	public void setIntocmitDeUser(String intocmitDeUser) {
		this.intocmitDeUser = intocmitDeUser;
	}
	public boolean isTrimisPeMail() {
		return trimisPeMail;
	}
	public void setTrimisPeMail(boolean trimisPeMail) {
		this.trimisPeMail = trimisPeMail;
	}
	public String getContinut() {
		return continut;
	}
	public void setContinut(String continut) {
		this.continut = continut;
	}
	public boolean isAsteptamRaspuns() {
		return asteptamRaspuns;
	}
	public void setAsteptamRaspuns(boolean asteptamRaspuns) {
		this.asteptamRaspuns = asteptamRaspuns;
	}
	public Date getTermenRaspuns() {
		return termenRaspuns;
	}
	public void setTermenRaspuns(Date termenRaspuns) {
		this.termenRaspuns = termenRaspuns;
	}
	public List<RegistruIesiriDestinatarViewModel> getDestinatari() {
		return destinatari;
	}
	public void setDestinatari(List<RegistruIesiriDestinatarViewModel> destinatari) {
		this.destinatari = destinatari;
	}
	public String getNumeProiecteConcatenate() {
		return numeProiecteConcatenate;
	}
	public void setNumeProiecteConcatenate(String numeProiecteConcatenate) {
		this.numeProiecteConcatenate = numeProiecteConcatenate;
	}
	public boolean isAnulat() {
		return anulat;
	}
	public void setAnulat(boolean anulat) {
		this.anulat = anulat;
	}
	public String getMotivAnulare() {
		return motivAnulare;
	}
	public void setMotivAnulare(String motivAnulare) {
		this.motivAnulare = motivAnulare;
	}
	public boolean isInchis() {
		return inchis;
	}
	public void setInchis(boolean inchis) {
		this.inchis = inchis;
	}
	public List<AtasamentModel> getAtasamente() {
		return atasamente;
	}
	public void setAtasamente(List<AtasamentModel> atasamente) {
		this.atasamente = atasamente;
	}
	public boolean isTrebuieAnulat() {
		return trebuieAnulat;
	}
	public void setTrebuieAnulat(boolean trebuieAnulat) {
		this.trebuieAnulat = trebuieAnulat;
	}
	public String getSubactivityName() {
		return subactivityName;
	}
	public void setSubactivityName(String subactivityName) {
		this.subactivityName = subactivityName;
	}
	
}
