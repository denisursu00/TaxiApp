package ro.cloudSoft.cloudDoc.domain.project;

import java.util.Date;

public class DspExportActivitateModel {

	private String nume;
	private String descriere;
	private Date dataSfarsit;
	private String userAsignat;
	private String subactivity;
	
	public String getNume() {
		return nume;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	public String getDescriere() {
		return descriere;
	}
	public void setDescriere(String descriere) {
		this.descriere = descriere;
	}
	public Date getDataSfarsit() {
		return dataSfarsit;
	}
	public void setDataSfarsit(Date dataSfarsit) {
		this.dataSfarsit = dataSfarsit;
	}
	public String getUserAsignat() {
		return userAsignat;
	}
	public void setUserAsignat(String userAsignat) {
		this.userAsignat = userAsignat;
	}
	public String getSubactivity() {
		return subactivity;
	}
	public void setSubactivity(String subactivity) {
		this.subactivity = subactivity;
	}
}
