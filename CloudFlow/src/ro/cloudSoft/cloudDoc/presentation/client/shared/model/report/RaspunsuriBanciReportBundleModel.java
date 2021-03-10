package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SimpleListItemModel;

public class RaspunsuriBanciReportBundleModel {

	private List<SimpleListItemModel> denumiriBanci;
	private List<SimpleListItemModel> proiecte;
	
	public List<SimpleListItemModel> getDenumiriBanci() {
		return denumiriBanci;
	}
	public void setDenumiriBanci(List<SimpleListItemModel> denumiriBanci) {
		this.denumiriBanci = denumiriBanci;
	}
	public List<SimpleListItemModel> getProiecte() {
		return proiecte;
	}
	public void setProiecte(List<SimpleListItemModel> proiecte) {
		this.proiecte = proiecte;
	}
	
}
