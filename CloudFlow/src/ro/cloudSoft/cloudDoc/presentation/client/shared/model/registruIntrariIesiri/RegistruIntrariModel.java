package ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri;

import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.AtasamentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.project.SubactivityModel;

public class RegistruIntrariModel {

	private Long id;
	private String numarInregistrare;
	private Date dataInregistrare;
	private Long emitentId;
	private String numeEmitent;
	private String departamentEmitent;
	private String numarDocumentEmitent;
	private Date dataDocumentEmitent;
	private Long tipDocumentId;
	private String codTipDocument;
	private boolean trimisPeMail;
	private String continut;
	private Integer numarPagini;
	private Integer numarAnexe;
	private Long repartizatCatreUserId;
	private List<Long> comisieSauGLIds;
	private List<Long> proiectIds;
	private boolean necesitaRaspuns;
	private Date termenRaspuns;
	private Long numarInregistrareOfRegistruIesiri;
	private String observatii;
	private RaspunsuriBanciCuPropuneriEnum raspunsuriBanciCuPropuneri;
	private Integer nrZileIntrareEmitent;
	private Integer nrZileRaspunsIntrare;
	private Integer nrZileRaspunsEmitent;
	private Integer nrZileTermenDataRaspuns;
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

	public Long getEmitentId() {
		return emitentId;
	}

	public void setEmitentId(Long emitentId) {
		this.emitentId = emitentId;
	}

	public String getNumeEmitent() {
		return numeEmitent;
	}

	public void setNumeEmitent(String numeEmitent) {
		this.numeEmitent = numeEmitent;
	}

	public String getDepartamentEmitent() {
		return departamentEmitent;
	}

	public void setDepartamentEmitent(String departamentEmitent) {
		this.departamentEmitent = departamentEmitent;
	}

	public String getNumarDocumentEmitent() {
		return numarDocumentEmitent;
	}

	public void setNumarDocumentEmitent(String numarDocumentEmitent) {
		this.numarDocumentEmitent = numarDocumentEmitent;
	}

	public Date getDataDocumentEmitent() {
		return dataDocumentEmitent;
	}

	public void setDataDocumentEmitent(Date dataDocumentEmitent) {
		this.dataDocumentEmitent = dataDocumentEmitent;
	}

	public Long getTipDocumentId() {
		return tipDocumentId;
	}

	public void setTipDocumentId(Long tipDocumentId) {
		this.tipDocumentId = tipDocumentId;
	}

	public String getCodTipDocument() {
		return codTipDocument;
	}

	public void setCodTipDocument(String codTipDocument) {
		this.codTipDocument = codTipDocument;
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

	public Long getRepartizatCatreUserId() {
		return repartizatCatreUserId;
	}
	
	public void setRepartizatCatreUserId(Long repartizatCatreUserId) {
		this.repartizatCatreUserId = repartizatCatreUserId;
	}

	public List<Long> getComisieSauGLIds() {
		return comisieSauGLIds;
	}

	public void setComisieSauGLIds(List<Long> comisieSauGLIds) {
		this.comisieSauGLIds = comisieSauGLIds;
	}

	public List<Long> getProiectIds() {
		return proiectIds;
	}

	public void setProiectIds(List<Long> proiectIds) {
		this.proiectIds = proiectIds;
	}

	public boolean isNecesitaRaspuns() {
		return necesitaRaspuns;
	}

	public void setNecesitaRaspuns(boolean necesitaRaspuns) {
		this.necesitaRaspuns = necesitaRaspuns;
	}

	public Date getTermenRaspuns() {
		return termenRaspuns;
	}

	public void setTermenRaspuns(Date termenRaspuns) {
		this.termenRaspuns = termenRaspuns;
	}

	public Long getNumarInregistrareOfRegistruIesiri() {
		return numarInregistrareOfRegistruIesiri;
	}

	public void setNumarInregistrareOfRegistruIesiri(Long numarInregistrareOfRegistruIesiri) {
		this.numarInregistrareOfRegistruIesiri = numarInregistrareOfRegistruIesiri;
	}

	public String getObservatii() {
		return observatii;
	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}

	public RaspunsuriBanciCuPropuneriEnum getRaspunsuriBanciCuPropuneri() {
		return raspunsuriBanciCuPropuneri;
	}

	public void setRaspunsuriBanciCuPropuneri(RaspunsuriBanciCuPropuneriEnum raspunsuriBanciCuPropuneri) {
		this.raspunsuriBanciCuPropuneri = raspunsuriBanciCuPropuneri;
	}
	
	public Integer getNrZileIntrareEmitent() {
		return nrZileIntrareEmitent;
	}

	public void setNrZileIntrareEmitent(Integer nrZileIntrareEmitent) {
		this.nrZileIntrareEmitent = nrZileIntrareEmitent;
	}

	public Integer getNrZileRaspunsIntrare() {
		return nrZileRaspunsIntrare;
	}

	public void setNrZileRaspunsIntrare(Integer nrZileRaspunsIntrare) {
		this.nrZileRaspunsIntrare = nrZileRaspunsIntrare;
	}

	public Integer getNrZileRaspunsEmitent() {
		return nrZileRaspunsEmitent;
	}

	public void setNrZileRaspunsEmitent(Integer nrZileRaspunsEmitent) {
		this.nrZileRaspunsEmitent = nrZileRaspunsEmitent;
	}

	public Integer getNrZileTermenDataRaspuns() {
		return nrZileTermenDataRaspuns;
	}

	public void setNrZileTermenDataRaspuns(Integer nrZileTermenDataRaspuns) {
		this.nrZileTermenDataRaspuns = nrZileTermenDataRaspuns;
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

	public static enum RaspunsuriBanciCuPropuneriEnum {
		DA,
		NU,
		NA
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
