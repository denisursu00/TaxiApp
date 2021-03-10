package ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruDocumenteJustificativePlati;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.AtasamentModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;

public class RegistruDocumenteJustificativePlatiModel {

	private Long id;
	private String lunaInregistrare;
	private String numarInregistrare;
	private Date dataInregistrare;
	private String emitent;
	// TODO - De modificat sa fie doar tipDocumentId, nu tot modelul
	private NomenclatorValueModel tipDocument;
	private String numarDocument;
	private Date dataDocument;
	private ModLivrareEnum modLivrare;
	private String detalii;
	private BigDecimal valoare;
	// TODO - De modificat sa fie doar monedaId, nu tot modelul
	private NomenclatorValueModel moneda;
	private Date dataScadenta;
	private ModalitatePlataEnum modalitatePlata;
	private boolean reconciliereCuExtrasBanca;
	private boolean platit;
	private Date dataPlatii;
	private String incadrareConformBVC;
	private Long intrareEmitere;
	private Long plataScadenta;
	private Long scadentaEmitere;
	private boolean anulat;
	private String motivAnulare;
	private List<AtasamentModel> atasamente;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLunaInregistrare() {
		return lunaInregistrare;
	}

	public void setLunaInregistrare(String lunaInregistrare) {
		this.lunaInregistrare = lunaInregistrare;
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

	public String getEmitent() {
		return emitent;
	}

	public void setEmitent(String emitent) {
		this.emitent = emitent;
	}
	
	public NomenclatorValueModel getTipDocument() {
		return tipDocument;
	}
	
	public void setTipDocument(NomenclatorValueModel tipDocument) {
		this.tipDocument = tipDocument;
	}

	public String getNumarDocument() {
		return numarDocument;
	}

	public void setNumarDocument(String numarDocument) {
		this.numarDocument = numarDocument;
	}

	public Date getDataDocument() {
		return dataDocument;
	}

	public void setDataDocument(Date dataDocument) {
		this.dataDocument = dataDocument;
	}
	
	public ModLivrareEnum getModLivrare() {
		return modLivrare;
	}
	
	public void setModLivrare(ModLivrareEnum modLivrare) {
		this.modLivrare = modLivrare;
	}
	
	public String getDetalii() {
		return detalii;
	}
	
	public void setDetalii(String detalii) {
		this.detalii = detalii;
	}
	
	public BigDecimal getValoare() {
		return valoare;
	}
	
	public void setValoare(BigDecimal valoare) {
		this.valoare = valoare;
	}
	
	public NomenclatorValueModel getMoneda() {
		return moneda;
	}
	
	public void setMoneda(NomenclatorValueModel moneda) {
		this.moneda = moneda;
	}

	public Date getDataScadenta() {
		return dataScadenta;
	}

	public void setDataScadenta(Date dataScadenta) {
		this.dataScadenta = dataScadenta;
	}
	
	public ModalitatePlataEnum getModalitatePlata() {
		return modalitatePlata;
	}
	
	public void setModalitatePlata(ModalitatePlataEnum modalitatePlata) {
		this.modalitatePlata = modalitatePlata;
	}

	public boolean isReconciliereCuExtrasBanca() {
		return reconciliereCuExtrasBanca;
	}

	public void setReconciliereCuExtrasBanca(boolean reconciliereCuExtrasBanca) {
		this.reconciliereCuExtrasBanca = reconciliereCuExtrasBanca;
	}

	public boolean isPlatit() {
		return platit;
	}

	public void setPlatit(boolean platit) {
		this.platit = platit;
	}

	public Date getDataPlatii() {
		return dataPlatii;
	}

	public void setDataPlatii(Date dataPlatii) {
		this.dataPlatii = dataPlatii;
	}

	public String getIncadrareConformBVC() {
		return incadrareConformBVC;
	}

	public void setIncadrareConformBVC(String incadrareConformBVC) {
		this.incadrareConformBVC = incadrareConformBVC;
	}

	public Long getIntrareEmitere() {
		return intrareEmitere;
	}

	public void setIntrareEmitere(Long intrareEmitere) {
		this.intrareEmitere = intrareEmitere;
	}

	public Long getPlataScadenta() {
		return plataScadenta;
	}

	public void setPlataScadenta(Long plataScadenta) {
		this.plataScadenta = plataScadenta;
	}

	public Long getScadentaEmitere() {
		return scadentaEmitere;
	}

	public void setScadentaEmitere(Long scadentaEmitere) {
		this.scadentaEmitere = scadentaEmitere;
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
		
	public List<AtasamentModel> getAtasamente() {
		return atasamente;
	}

	public void setAtasamente(List<AtasamentModel> atasamente) {
		this.atasamente = atasamente;
	}

	public static enum ModLivrareEnum {
		POSTA,
		EMAIL,
		CURIER,
		LIVRARE_EMITENT
	}
	
	public static enum ModalitatePlataEnum {
		INTERNET_BANKING,
		ORDIN_DE_PLATA,
		NUMERAR,
		CARD,
		DIRECT_DEBIT
	}

}
