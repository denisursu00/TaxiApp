package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb;

import java.math.BigDecimal;

import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont.ModalitatePlataForDecontEnum;

public class CheltuialaReprezentantArbDiurna {

	private BigDecimal diurna;
	private Integer numarZile;
	private String modalitatePlata;

	public CheltuialaReprezentantArbDiurna() {
	}

	public CheltuialaReprezentantArbDiurna(BigDecimal diurna, Integer numarZile, ModalitatePlataForDecontEnum modalitatePlata) {
		this.diurna = diurna;
		this.numarZile = numarZile;
		this.modalitatePlata = modalitatePlata.toString();
	}

	public BigDecimal getDiurna() {
		return diurna;
	}

	public void setDiurna(BigDecimal diurna) {
		this.diurna = diurna;
	}

	public Integer getNumarZile() {
		return numarZile;
	}

	public void setNumarZile(Integer numarZile) {
		this.numarZile = numarZile;
	}

	public String getModalitatePlata() {
		return modalitatePlata;
	}

	public void setModalitatePlata(String modalitatePlata) {
		this.modalitatePlata = modalitatePlata;
	}

}
