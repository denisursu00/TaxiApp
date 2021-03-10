package ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator.NomenclatorValueModel;

public class ReprezentantiComisieSauGLEditBundle {

	private ReprezentantiComisieSauGLModel reprezentanti;
	
	private NomenclatorValueModel comisieSauGL;
	
	public Long persoaneNomenclatorId;
	public Long membriCDNomenclatorId;	
	public Long institutiiNomenclatorId;
	
	public Integer nrAniValabilitateMandatPresedinteVicepresedinte;
	public Integer nrAniValabilitateMandatMembruCdCoordonator;
	public boolean isCategorieComisie;
	
	public ReprezentantiComisieSauGLModel getReprezentanti() {
		return reprezentanti;
	}
	public void setReprezentanti(ReprezentantiComisieSauGLModel reprezentanti) {
		this.reprezentanti = reprezentanti;
	}	
	public void setComisieSauGL(NomenclatorValueModel comisieSauGL) {
		this.comisieSauGL = comisieSauGL;
	}
	public NomenclatorValueModel getComisieSauGL() {
		return comisieSauGL;
	}
	public Long getPersoaneNomenclatorId() {
		return persoaneNomenclatorId;
	}
	public void setPersoaneNomenclatorId(Long persoaneNomenclatorId) {
		this.persoaneNomenclatorId = persoaneNomenclatorId;
	}
	public Long getMembriCDNomenclatorId() {
		return membriCDNomenclatorId;
	}
	public void setMembriCDNomenclatorId(Long membriCDNomenclatorId) {
		this.membriCDNomenclatorId = membriCDNomenclatorId;
	}
	public Long getInstitutiiNomenclatorId() {
		return institutiiNomenclatorId;
	}
	public void setInstitutiiNomenclatorId(Long institutiiNomenclatorId) {
		this.institutiiNomenclatorId = institutiiNomenclatorId;
	}
	public Integer getNrAniValabilitateMandatPresedinteVicepresedinte() {
		return nrAniValabilitateMandatPresedinteVicepresedinte;
	}
	public void setNrAniValabilitateMandatPresedinteVicepresedinte(
			Integer nrAniValabilitateMandatPresedinteVicepresedinte) {
		this.nrAniValabilitateMandatPresedinteVicepresedinte = nrAniValabilitateMandatPresedinteVicepresedinte;
	}
	public Integer getNrAniValabilitateMandatMembruCdCoordonator() {
		return nrAniValabilitateMandatMembruCdCoordonator;
	}
	public void setNrAniValabilitateMandatMembruCdCoordonator(Integer nrAniValabilitateMandatMembruCdCoordonator) {
		this.nrAniValabilitateMandatMembruCdCoordonator = nrAniValabilitateMandatMembruCdCoordonator;
	}
	public boolean isCategorieComisie() {
		return isCategorieComisie;
	}
	public void setCategorieComisie(boolean isCategorieComisie) {
		this.isCategorieComisie = isCategorieComisie;
	}
	
	
}
