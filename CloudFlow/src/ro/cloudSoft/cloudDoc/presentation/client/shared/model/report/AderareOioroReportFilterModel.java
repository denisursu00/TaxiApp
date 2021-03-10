package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

public class AderareOioroReportFilterModel {

	private List<Integer> aniList;
	private List<Long> organismIdList;
	private String abreviere;
	private List<Long> comitetIdList;
	private List<Long> institutieIdList;
	private List<Long> reprezentantIdList;
	private String functie;
	private List<Long> coordonatorArbIdList;
	public List<Integer> getAniList() {
		return aniList;
	}
	public void setAniList(List<Integer> aniList) {
		this.aniList = aniList;
	}
	public List<Long> getOrganismIdList() {
		return organismIdList;
	}
	public void setOrganismIdList(List<Long> organismIdList) {
		this.organismIdList = organismIdList;
	}
	public String getAbreviere() {
		return abreviere;
	}
	public void setAbreviere(String abreviere) {
		this.abreviere = abreviere;
	}
	public List<Long> getComitetIdList() {
		return comitetIdList;
	}
	public void setComitetIdList(List<Long> comitetIdList) {
		this.comitetIdList = comitetIdList;
	}
	public List<Long> getInstitutieIdList() {
		return institutieIdList;
	}
	public void setInstitutieIdList(List<Long> institutieIdList) {
		this.institutieIdList = institutieIdList;
	}
	public List<Long> getReprezentantIdList() {
		return reprezentantIdList;
	}
	public void setReprezentantIdList(List<Long> reprezentantIdList) {
		this.reprezentantIdList = reprezentantIdList;
	}
	public String getFunctie() {
		return functie;
	}
	public void setFunctie(String functie) {
		this.functie = functie;
	}
	public List<Long> getCoordonatorArbIdList() {
		return coordonatorArbIdList;
	}
	public void setCoordonatorArbIdList(List<Long> coordonatorArbIdList) {
		this.coordonatorArbIdList = coordonatorArbIdList;
	}
		
}
