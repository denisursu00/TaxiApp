package ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb;

import java.util.List;

public class MembruReprezentantiComisieSauGLModel {

	private Long id;
	private Long reprezentantiComisieSauGLId;
	private Long institutieId;
	private Long membruInstitutieId;
	private String nume;
	private String prenume;
	private String functie;
	private String departament;
	private String email;
	private String telefon;
	private String calitate;
	private String stare;
	private List<DiplomaMembruReprezentantiComisieSauGLModel> diplome;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getReprezentantiComisieSauGLId() {
		return reprezentantiComisieSauGLId;
	}

	public void setReprezentantiComisieSauGLId(Long reprezentantiComisieSauGLId) {
		this.reprezentantiComisieSauGLId = reprezentantiComisieSauGLId;
	}

	public Long getInstitutieId() {
		return institutieId;
	}

	public void setInstitutieId(Long institutieId) {
		this.institutieId = institutieId;
	}

	public Long getMembruInstitutieId() {
		return membruInstitutieId;
	}
	
	public void setMembruInstitutieId(Long membruInstitutieId) {
		this.membruInstitutieId = membruInstitutieId;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getPrenume() {
		return prenume;
	}

	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}

	public String getFunctie() {
		return functie;
	}

	public void setFunctie(String functie) {
		this.functie = functie;
	}

	public String getDepartament() {
		return departament;
	}

	public void setDepartament(String departament) {
		this.departament = departament;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public String getCalitate() {
		return calitate;
	}

	public void setCalitate(String calitate) {
		this.calitate = calitate;
	}

	public String getStare() {
		return stare;
	}

	public void setStare(String stare) {
		this.stare = stare;
	}
	
	public List<DiplomaMembruReprezentantiComisieSauGLModel> getDiplome() {
		return diplome;
	}
	
	public void setDiplome(List<DiplomaMembruReprezentantiComisieSauGLModel> diplome) {
		this.diplome = diplome;
	}
}
