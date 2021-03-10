package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SimpleListItemModel;

public class DeplasariDeconturiReportBundleModel {
	
	private List<SimpleListItemModel> denumiriInstitutii; 
	Long nomenclatorReprezentatOrgId; 
	Long nomenclatorOrganismeId; 
	private List<SimpleListItemModel> denumiriComiteteList;
	private List<SimpleListItemModel> oraseList; 
	private List<SimpleListItemModel> titulariDeconturi;
	
	public List<SimpleListItemModel> getDenumiriInstitutii() {
		return denumiriInstitutii;
	}
	public void setDenumiriInstitutii(List<SimpleListItemModel> denumiriInstitutii) {
		this.denumiriInstitutii = denumiriInstitutii;
	}
	public Long getNomenclatorReprezentatOrgId() {
		return nomenclatorReprezentatOrgId;
	}
	public void setNomenclatorReprezentatOrgId(Long nomenclatorReprezentatOrgId) {
		this.nomenclatorReprezentatOrgId = nomenclatorReprezentatOrgId;
	}
	public Long getNomenclatorOrganismeId() {
		return nomenclatorOrganismeId;
	}
	public void setNomenclatorOrganismeId(Long nomenclatorOrganismeId) {
		this.nomenclatorOrganismeId = nomenclatorOrganismeId;
	}
	
	public List<SimpleListItemModel> getDenumiriComiteteList() {
		return denumiriComiteteList;
	}
	public void setDenumiriComiteteList(List<SimpleListItemModel> denumiriComiteteList) {
		this.denumiriComiteteList = denumiriComiteteList;
	}
	public List<SimpleListItemModel> getOraseList() {
		return oraseList;
	}
	public void setOraseList(List<SimpleListItemModel> oraseList) {
		this.oraseList = oraseList;
	}
	public List<SimpleListItemModel> getTitulariDeconturi() {
		return titulariDeconturi;
	}
	public void setTitulariDeconturi(List<SimpleListItemModel> titulariDeconturi) {
		this.titulariDeconturi = titulariDeconturi;
	}
}
