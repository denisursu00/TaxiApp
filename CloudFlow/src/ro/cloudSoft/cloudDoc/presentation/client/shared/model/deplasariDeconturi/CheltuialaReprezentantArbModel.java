package ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi;

import java.math.BigDecimal;
import java.util.Date;

public class CheltuialaReprezentantArbModel {

	private Long id;
	private String valuta;
	private BigDecimal cursValutar;
	private String modalitatePlata;
	private String numarDocumentJustificativ;
	private Date dataDocumentJustificativ;
	private String tipDocumentJustificativ;
	private BigDecimal valoareCheltuiala;
	private String explicatie;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public BigDecimal getCursValutar() {
		return cursValutar;
	}

	public void setCursValutar(BigDecimal cursValutar) {
		this.cursValutar = cursValutar;
	}

	public String getModalitatePlata() {
		return modalitatePlata;
	}

	public void setModalitatePlata(String modalitatePlata) {
		this.modalitatePlata = modalitatePlata;
	}

	public String getNumarDocumentJustificativ() {
		return numarDocumentJustificativ;
	}

	public void setNumarDocumentJustificativ(String numarDocumentJustificativ) {
		this.numarDocumentJustificativ = numarDocumentJustificativ;
	}

	public Date getDataDocumentJustificativ() {
		return dataDocumentJustificativ;
	}

	public void setDataDocumentJustificativ(Date dataDocumentJustificativ) {
		this.dataDocumentJustificativ = dataDocumentJustificativ;
	}

	public String getTipDocumentJustificativ() {
		return tipDocumentJustificativ;
	}

	public void setTipDocumentJustificativ(String tipDocumentJustificativ) {
		this.tipDocumentJustificativ = tipDocumentJustificativ;
	}

	public BigDecimal getValoareCheltuiala() {
		return valoareCheltuiala;
	}

	public void setValoareCheltuiala(BigDecimal valoareCheltuiala) {
		this.valoareCheltuiala = valoareCheltuiala;
	}

	public String getExplicatie() {
		return explicatie;
	}

	public void setExplicatie(String explicatie) {
		this.explicatie = explicatie;
	}
}
