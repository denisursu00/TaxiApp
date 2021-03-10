package ro.cloudSoft.cloudDoc.domain.deplasariDeconturi;

import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.Table;

import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont.ModalitatePlataForDecontEnum;

@Entity
@Table(name = "CHELTUIELI_REPREZENTANT_ARB")
public class CheltuialaReprezentantArb {

	private Long id;
	private DeplasareDecont deplasareDecont;
	private ValutaForCheltuieliReprezentantArbEnum valuta;
	private BigDecimal cursValutar;
	private ModalitatePlataForDecontEnum modalitatePlata;
	private String numarDocumentJustificativ;
	private Date dataDocumentJustificativ;
	private TipDocumentJustificativForCheltuieliReprezentantArbEnum tipDocumentJustificativ;
	private BigDecimal valoareCheltuiala;
	private String explicatie;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPLASARE_DECONT_ID")
	public DeplasareDecont getDeplasareDecont() {
		return deplasareDecont;
	}

	public void setDeplasareDecont(DeplasareDecont deplasareDecont) {
		this.deplasareDecont = deplasareDecont;
	}

	@Column(name = "VALUTA")
	@Enumerated(EnumType.STRING)
	public ValutaForCheltuieliReprezentantArbEnum getValuta() {
		return valuta;
	}

	public void setValuta(ValutaForCheltuieliReprezentantArbEnum valuta) {
		this.valuta = valuta;
	}

	@Column(name = "CURS_VALUTAR")
	public BigDecimal getCursValutar() {
		return cursValutar;
	}

	public void setCursValutar(BigDecimal cursValutar) {
		this.cursValutar = cursValutar;
	}

	@Column(name = "MODALITATE_PLATA")
	@Enumerated(EnumType.STRING)
	public ModalitatePlataForDecontEnum getModalitatePlata() {
		return modalitatePlata;
	}

	public void setModalitatePlata(ModalitatePlataForDecontEnum modalitatePlata) {
		this.modalitatePlata = modalitatePlata;
	}

	@Column(name = "NUMAR_DOCUMENT_JUSTIFICATIV")
	public String getNumarDocumentJustificativ() {
		return numarDocumentJustificativ;
	}

	public void setNumarDocumentJustificativ(String numarDocumentJustificativ) {
		this.numarDocumentJustificativ = numarDocumentJustificativ;
	}

	@Column(name = "DATA_DOCUMENT_JUSTIFICATIV")
	public Date getDataDocumentJustificativ() {
		return dataDocumentJustificativ;
	}

	public void setDataDocumentJustificativ(Date dataDocumentJustificativ) {
		this.dataDocumentJustificativ = dataDocumentJustificativ;
	}

	@Column(name = "TIP_DOCUMENT_JUSTIFICATIV")
	@Enumerated(EnumType.STRING)
	public TipDocumentJustificativForCheltuieliReprezentantArbEnum getTipDocumentJustificativ() {
		return tipDocumentJustificativ;
	}

	public void setTipDocumentJustificativ(TipDocumentJustificativForCheltuieliReprezentantArbEnum tipDocumentJustificativ) {
		this.tipDocumentJustificativ = tipDocumentJustificativ;
	}

	@Column(name = "VALOARE_CHELTUIALA")
	public BigDecimal getValoareCheltuiala() {
		return valoareCheltuiala;
	}

	public void setValoareCheltuiala(BigDecimal valoareCheltuiala) {
		this.valoareCheltuiala = valoareCheltuiala;
	}

	@Column(name = "EXPLICATIE")
	public String getExplicatie() {
		return explicatie;
	}

	public void setExplicatie(String explicatie) {
		this.explicatie = explicatie;
	}

	public static enum TipDocumentJustificativForCheltuieliReprezentantArbEnum {
		CAZARE,
		BILET_DE_AVION,
		TAXI_TREN_METROU,
		COMISION_UTILIZARE_CARD,
		ALTE_CHELTUIELI
	}

	public static enum ValutaForCheltuieliReprezentantArbEnum {
		RON,
		EUR,
		USD
	}
}
