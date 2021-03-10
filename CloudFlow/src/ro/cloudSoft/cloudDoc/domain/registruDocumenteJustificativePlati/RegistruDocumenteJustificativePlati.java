package ro.cloudSoft.cloudDoc.domain.registruDocumenteJustificativePlati;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiModel.ModLivrareEnum;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruDocumenteJustificativePlati.RegistruDocumenteJustificativePlatiModel.ModalitatePlataEnum;

@Entity
@Table(name = "REGISTRU_DOCUMENTE_JUST_PLATI", uniqueConstraints = @UniqueConstraint(columnNames = {
		"numar_inregistrare" }))
public class RegistruDocumenteJustificativePlati {

	private Long id;
	private String numarInregistrare;
	private Date dataInregistrare;
	private String emitent;
	private NomenclatorValue tipDocument;
	private String numarDocument;
	private Date dataDocument;
	private ModLivrareEnum modLivrare;
	private String detalii;
	private BigDecimal valoare;
	private NomenclatorValue moneda;
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
	private List<RegistruDocumenteJustificativePlatiAtasament> atasamente;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "numar_inregistrare")
	public String getNumarInregistrare() {
		return numarInregistrare;
	}

	public void setNumarInregistrare(String numarInregistrare) {
		this.numarInregistrare = numarInregistrare;
	}

	@Column(name = "data_inregistrare")
	public Date getDataInregistrare() {
		return dataInregistrare;
	}

	public void setDataInregistrare(Date dataInregistrare) {
		this.dataInregistrare = dataInregistrare;
	}

	@Column(name = "emitent")
	public String getEmitent() {
		return emitent;
	}

	public void setEmitent(String emitent) {
		this.emitent = emitent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tip_document_id")
	public NomenclatorValue getTipDocument() {
		return tipDocument;
	}

	public void setTipDocument(NomenclatorValue tipDocument) {
		this.tipDocument = tipDocument;
	}

	@Column(name = "numar_document")
	public String getNumarDocument() {
		return numarDocument;
	}

	public void setNumarDocument(String numarDocument) {
		this.numarDocument = numarDocument;
	}

	@Column(name = "data_document")
	public Date getDataDocument() {
		return dataDocument;
	}

	public void setDataDocument(Date dataDocument) {
		this.dataDocument = dataDocument;
	}

	@Column(name = "mod_livrare")
	@Enumerated(value = EnumType.STRING)
	public ModLivrareEnum getModLivrare() {
		return modLivrare;
	}

	public void setModLivrare(ModLivrareEnum modLivrare) {
		this.modLivrare = modLivrare;
	}

	@Column(name = "detalii")
	public String getDetalii() {
		return detalii;
	}

	public void setDetalii(String detalii) {
		this.detalii = detalii;
	}

	@Column(name = "valoare")
	public BigDecimal getValoare() {
		return valoare;
	}

	public void setValoare(BigDecimal valoare) {
		this.valoare = valoare;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "moneda_id")
	public NomenclatorValue getMoneda() {
		return moneda;
	}

	public void setMoneda(NomenclatorValue moneda) {
		this.moneda = moneda;
	}

	@Column(name = "data_scadenta")
	public Date getDataScadenta() {
		return dataScadenta;
	}

	public void setDataScadenta(Date dataScadenta) {
		this.dataScadenta = dataScadenta;
	}

	@Column(name = "modalitate_plata")
	@Enumerated(value = EnumType.STRING)
	public ModalitatePlataEnum getModalitatePlata() {
		return modalitatePlata;
	}

	public void setModalitatePlata(ModalitatePlataEnum modalitatePlata) {
		this.modalitatePlata = modalitatePlata;
	}

	@Column(name = "reconciliere_cu_extras_banca")
	public boolean isReconciliereCuExtrasBanca() {
		return reconciliereCuExtrasBanca;
	}

	public void setReconciliereCuExtrasBanca(boolean reconciliereCuExtrasBanca) {
		this.reconciliereCuExtrasBanca = reconciliereCuExtrasBanca;
	}

	@Column(name = "platit")
	public boolean isPlatit() {
		return platit;
	}

	public void setPlatit(boolean platit) {
		this.platit = platit;
	}

	@Column(name = "data_platii")
	public Date getDataPlatii() {
		return dataPlatii;
	}

	public void setDataPlatii(Date dataPlatii) {
		this.dataPlatii = dataPlatii;
	}

	@Column(name = "incadrare_conform_bvc")
	public String getIncadrareConformBVC() {
		return incadrareConformBVC;
	}

	public void setIncadrareConformBVC(String incadrareConformBVC) {
		this.incadrareConformBVC = incadrareConformBVC;
	}

	@Column(name = "intrare_emitere")
	public Long getIntrareEmitere() {
		return intrareEmitere;
	}

	public void setIntrareEmitere(Long intrareEmitere) {
		this.intrareEmitere = intrareEmitere;
	}

	@Column(name = "plata_scadenta")
	public Long getPlataScadenta() {
		return plataScadenta;
	}

	public void setPlataScadenta(Long plataScadenta) {
		this.plataScadenta = plataScadenta;
	}

	@Column(name = "scadenta_emitere")
	public Long getScadentaEmitere() {
		return scadentaEmitere;
	}

	public void setScadentaEmitere(Long scadentaEmitere) {
		this.scadentaEmitere = scadentaEmitere;
	}

	@Column(name = "anulat")
	public boolean isAnulat() {
		return anulat;
	}

	public void setAnulat(boolean anulat) {
		this.anulat = anulat;
	}

	@Column(name = "motiv_anulare")
	public String getMotivAnulare() {
		return motivAnulare;
	}

	public void setMotivAnulare(String motivAnulare) {
		this.motivAnulare = motivAnulare;
	}

	@OneToMany(mappedBy = "registruDocumenteJustificativePlati", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<RegistruDocumenteJustificativePlatiAtasament> getAtasamente() {
		return atasamente;
	}

	public void setAtasamente(List<RegistruDocumenteJustificativePlatiAtasament> atasamente) {
		this.atasamente = atasamente;
	}

}
