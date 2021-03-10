package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;

public class DeplasariDeconturiReportFilterModel {
	String denumireInstitutie;
	Long reprezentantId;
	Long organismId; 
	String denumireComitet;
	String oras; 
	Date dataPlecareDeLa;
	Date dataPlecarePanaLa; 
	Date dataSosireDeLa; 
	Date dataSosirePanaLa;
	String titularDecont;
	
	public Long getReprezentantId() {
		return reprezentantId;
	}
	public void setReprezentantId(Long reprezentantId) {
		this.reprezentantId = reprezentantId;
	}
	public Long getOrganismId() {
		return organismId;
	}
	public void setOrganismId(Long organismId) {
		this.organismId = organismId;
	}
	public String getOras() {
		return oras;
	}
	public void setOras(String oras) {
		this.oras = oras;
	}
	public Date getDataPlecareDeLa() {
		return dataPlecareDeLa;
	}
	public void setDataPlecareDeLa(Date dataPlecareDeLa) {
		this.dataPlecareDeLa = dataPlecareDeLa;
	}
	public Date getDataPlecarePanaLa() {
		return dataPlecarePanaLa;
	}
	public void setDataPlecarePanaLa(Date dataPlecarePanaLa) {
		this.dataPlecarePanaLa = dataPlecarePanaLa;
	}
	public Date getDataSosireDeLa() {
		return dataSosireDeLa;
	}
	public void setDataSosireDeLa(Date dataSosireDeLa) {
		this.dataSosireDeLa = dataSosireDeLa;
	}
	public Date getDataSosirePanaLa() {
		return dataSosirePanaLa;
	}
	public void setDataSosirePanaLa(Date dataSosirePanaLa) {
		this.dataSosirePanaLa = dataSosirePanaLa;
	}
	public String getDenumireInstitutie() {
		return denumireInstitutie;
	}
	public void setDenumireInstitutie(String denumireInstitutie) {
		this.denumireInstitutie = denumireInstitutie;
	}
	public String getDenumireComitet() {
		return denumireComitet;
	}
	public void setDenumireComitet(String denumireComitet) {
		this.denumireComitet = denumireComitet;
	}
	public String getTitularDecont() {
		return titularDecont;
	}
	public void setTitularDecont(String titularDecont) {
		this.titularDecont = titularDecont;
	}
	
}
