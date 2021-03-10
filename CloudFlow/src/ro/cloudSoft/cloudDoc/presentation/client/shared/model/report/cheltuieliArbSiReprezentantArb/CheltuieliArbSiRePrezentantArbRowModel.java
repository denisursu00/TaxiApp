package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb;

import java.math.BigDecimal;

import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaArb.TipDocumentJustificativForCheltuieliArbEnum;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.CheltuialaReprezentantArb.TipDocumentJustificativForCheltuieliReprezentantArbEnum;
import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont.ModalitatePlataForDecontEnum;

public class CheltuieliArbSiRePrezentantArbRowModel {

	private String explicatie;
	private BigDecimal suma;
	private String valuta;
	private String numerarSauCard;
	
	public CheltuieliArbSiRePrezentantArbRowModel() {
	}

	public CheltuieliArbSiRePrezentantArbRowModel(TipDocumentJustificativForCheltuieliArbEnum explicatie, BigDecimal suma, ModalitatePlataForDecontEnum modalitatePlata) {
		this.explicatie = explicatie.name();
		this.suma = suma;
		this.numerarSauCard = modalitatePlata.toString();
	}
	

	public CheltuieliArbSiRePrezentantArbRowModel(TipDocumentJustificativForCheltuieliReprezentantArbEnum explicatie, BigDecimal suma, ModalitatePlataForDecontEnum modalitatePlata) {
		this.explicatie = explicatie.name();
		this.suma = suma;
		this.numerarSauCard = modalitatePlata.toString();
	}

	public String getExplicatie() {
		return explicatie;
	}

	public void setExplicatie(String explicatie) {
		this.explicatie = explicatie;
	}

	public BigDecimal getSuma() {
		return suma;
	}

	public void setSuma(BigDecimal suma) {
		this.suma = suma;
	}

	public String getValuta() {
		return valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public String getNumerarSauCard() {
		return numerarSauCard;
	}

	public void setNumerarSauCard(String numerarSauCard) {
		this.numerarSauCard = numerarSauCard;
	}

}
