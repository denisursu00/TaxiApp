package ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri;

import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.AtasamentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariModel.RaspunsuriBanciCuPropuneriEnum;

public class RegistruIntrariViewModel {

	private Long id;
	private String numarInregistrare;
	private Date dataInregistrare;
	private String numeEmitent;
	private String departamentEmitent;
	private String numarDocumentEmitent;
	private Date dataDocumentEmitent;
	private String tipDocument;
	private String codTipDocument;
	private boolean trimisPeMail;
	private String continut;
	private Integer numarPagini;
	private Integer numarAnexe;
	private String repartizatCatre;
	private String comisieSauGL;
	private String proiect;
	private boolean necesitaRaspuns;
	private Date termenRaspuns;
	private String numarInregistrareOfRegistruIesiri;
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

	public String getRepartizatCatre() {
		return repartizatCatre;
	}

	public void setRepartizatCatre(String repartizatCatre) {
		this.repartizatCatre = repartizatCatre;
	}

	public String getComisieSauGL() {
		return comisieSauGL;
	}

	public void setComisieSauGL(String comisieSauGL) {
		this.comisieSauGL = comisieSauGL;
	}

	public String getProiect() {
		return proiect;
	}

	public void setProiect(String proiect) {
		this.proiect = proiect;
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

	public String getNumarInregistrareOfRegistruIesiri() {
		return numarInregistrareOfRegistruIesiri;
	}

	public void setNumarInregistrareOfRegistruIesiri(String numarInregistrareOfRegistruIesiri) {
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
