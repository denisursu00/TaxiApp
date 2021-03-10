package ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri;

import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.AtasamentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.SubactivityModel;

public class RegistruIesiriModel {

	private Long id;
	private String numarInregistrare;
	private Date dataInregistrare;
	private Long tipDocumentId;
	private Integer numarPagini;
	private Integer numarAnexe;
	private Long intocmitDeUserId;
	private boolean trimisPeMail;
	private String continut;
	private boolean asteptamRaspuns;
	private Date termenRaspuns;
	private List<RegistruIesiriDestinatarModel> destinatari;
	private List<Long> proiectIds;
	private boolean anulat;
	private String motivAnulare;
	private boolean inchis;
	private boolean trebuieAnulat;
	private List<AtasamentModel> atasamente;
	private SubactivityModel subactivity;

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
	public Long getTipDocumentId() {
		return tipDocumentId;
	}
	public void setTipDocumentId(Long tipDocumentId) {
		this.tipDocumentId = tipDocumentId;
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
	public Long getIntocmitDeUserId() {
		return intocmitDeUserId;
	}
	public void setIntocmitDeUserId(Long intocmitDeUserId) {
		this.intocmitDeUserId = intocmitDeUserId;
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
	public List<RegistruIesiriDestinatarModel> getDestinatari() {
		return destinatari;
	}
	public void setDestinatari(List<RegistruIesiriDestinatarModel> destinatari) {
		this.destinatari = destinatari;
	}
	public List<Long> getProiectIds() {
		return proiectIds;
	}
	public void setProiectIds(List<Long> proiectIds) {
		this.proiectIds = proiectIds;
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
	public SubactivityModel getSubactivity() {
		return subactivity;
	}
	public void setSubactivity(SubactivityModel subactivity) {
		this.subactivity = subactivity;
	}
	
}
