package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SimpleListItemModel;

public class DashboardProiecteFilterBundle {
	private List<SimpleListItemModel> abrevieriProiect;
	private List<SimpleListItemModel> tasks;
	private List<SimpleListItemModel> responsibleUsers;
	private List<SimpleListItemModel> arieDeCuprindereList;
	private Long nomenclatorDomeniiBancareId;
	private Long nomenclatorImportantaProiecteId;
	private Long nomenclatorIncadrariProiecteId;
	
	public List<SimpleListItemModel> getAbrevieriProiect() {
		return abrevieriProiect;
	}
	public void setAbrevieriProiect(List<SimpleListItemModel> abrevieriProiect) {
		this.abrevieriProiect = abrevieriProiect;
	}
	public List<SimpleListItemModel> getTasks() {
		return tasks;
	}
	public void setTasks(List<SimpleListItemModel> tasks) {
		this.tasks = tasks;
	}
	public List<SimpleListItemModel> getResponsibleUsers() {
		return responsibleUsers;
	}
	public void setResponsibleUsers(List<SimpleListItemModel> responsibleUsers) {
		this.responsibleUsers = responsibleUsers;
	}
	public List<SimpleListItemModel> getArieDeCuprindereList() {
		return arieDeCuprindereList;
	}
	public void setArieDeCuprindereList(List<SimpleListItemModel> arieDeCuprindereList) {
		this.arieDeCuprindereList = arieDeCuprindereList;
	}
	public Long getNomenclatorDomeniiBancareId() {
		return nomenclatorDomeniiBancareId;
	}
	public void setNomenclatorDomeniiBancareId(Long nomenclatorDomeniiBancareId) {
		this.nomenclatorDomeniiBancareId = nomenclatorDomeniiBancareId;
	}
	public Long getNomenclatorImportantaProiecteId() {
		return nomenclatorImportantaProiecteId;
	}
	public void setNomenclatorImportantaProiecteId(Long nomenclatorImportantaProiecteId) {
		this.nomenclatorImportantaProiecteId = nomenclatorImportantaProiecteId;
	}
	public Long getNomenclatorIncadrariProiecteId() {
		return nomenclatorIncadrariProiecteId;
	}
	public void setNomenclatorIncadrariProiecteId(Long nomenclatorIncadrariProiecteId) {
		this.nomenclatorIncadrariProiecteId = nomenclatorIncadrariProiecteId;
	}
	
}
