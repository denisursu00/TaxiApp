package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

public class AderareOioroReportRowModel implements Cloneable{
	
	private String organism;
	private int an;
	private String abreviere;
	private String comitet;
	private String institutie;
	private String reprezentant;
	private String functie;
	private String coordonatorArb;
	private int nrDeplasariBugetate;
	
	public int getAn() {
		return an;
	}
	public void setAn(int an) {
		this.an = an;
	}
	public String getOrganism() {
		return organism;
	}
	public void setOrganism(String organism) {
		this.organism = organism;
	}
	public String getAbreviere() {
		return abreviere;
	}
	public void setAbreviere(String abreviere) {
		this.abreviere = abreviere;
	}
	public String getComitet() {
		return comitet;
	}
	public void setComitet(String comitet) {
		this.comitet = comitet;
	}
	public String getInstitutie() {
		return institutie;
	}
	public void setInstitutie(String institutie) {
		this.institutie = institutie;
	}
	public String getReprezentant() {
		return reprezentant;
	}
	public void setReprezentant(String reprezentant) {
		this.reprezentant = reprezentant;
	}
	public String getFunctie() {
		return functie;
	}
	public void setFunctie(String functie) {
		this.functie = functie;
	}
	public String getCoordonatorArb() {
		return coordonatorArb;
	}
	public void setCoordonatorArb(String coordonatorArb) {
		this.coordonatorArb = coordonatorArb;
	}
	public int getNrDeplasariBugetate() {
		return nrDeplasariBugetate;
	}
	public void setNrDeplasariBugetate(int nrDeplasariBugetate) {
		this.nrDeplasariBugetate = nrDeplasariBugetate;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	
}
