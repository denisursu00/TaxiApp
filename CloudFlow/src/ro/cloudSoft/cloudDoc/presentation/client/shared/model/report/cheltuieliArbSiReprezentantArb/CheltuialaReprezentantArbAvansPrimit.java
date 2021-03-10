package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report.cheltuieliArbSiReprezentantArb;

import java.math.BigDecimal;

import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont.ModalitatePlataForDecontEnum;

public class CheltuialaReprezentantArbAvansPrimit {

	private BigDecimal avansPrimit;
	private String modalitatePlata;

	
	public CheltuialaReprezentantArbAvansPrimit() {
	}

	public CheltuialaReprezentantArbAvansPrimit(BigDecimal avansPrimit, ModalitatePlataForDecontEnum modalitatePlata) {
		this.avansPrimit = avansPrimit;
		this.modalitatePlata = modalitatePlata.toString();
	}

	public BigDecimal getAvansPrimit() {
		return avansPrimit;
	}

	public void setAvansPrimit(BigDecimal avansPrimit) {
		this.avansPrimit = avansPrimit;
	}

	public String getModalitatePlata() {
		return modalitatePlata;
	}

	public void setModalitatePlata(String modalitatePlata) {
		this.modalitatePlata = modalitatePlata;
	}

}
