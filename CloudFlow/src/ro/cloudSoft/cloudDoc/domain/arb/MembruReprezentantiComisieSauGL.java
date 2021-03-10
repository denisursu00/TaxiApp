package ro.cloudSoft.cloudDoc.domain.arb;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;

@Entity
@Table(	name="MEMBRI_REPREZENTANT_COMISIE_GL")
public class MembruReprezentantiComisieSauGL {
	
	private Long id;
	private ReprezentantiComisieSauGL reprezentantiComisieSauGL;
	private NomenclatorValue institutie;	
	private NomenclatorValue membruInstitutie;
	private String nume;
	private String prenume;
	private String functie;
	private String departament;
	private String email;
	private String telefon;
	private Calitate calitate;
	private Stare stare;
	private List<DiplomaMembruReprezentantiComisieSauGL> diplome;
	
	public static enum Calitate {
		TITULAR,
		SUPLEANT,
		INLOCUITOR
	}
	
	public static enum Stare {		
		ACTIV, 
		INACTIV
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "REPREZENTANTI_COMISIE_GL_ID", nullable = false)
	public ReprezentantiComisieSauGL getReprezentantiComisieSauGL() {
		return reprezentantiComisieSauGL;
	}
	
	public void setReprezentantiComisieSauGL(ReprezentantiComisieSauGL reprezentantiComisieSauGL) {
		this.reprezentantiComisieSauGL = reprezentantiComisieSauGL;
	}
	
	@ManyToOne
	@JoinColumn(name = "INSTITUTIE_ID", nullable = false)
	public NomenclatorValue getInstitutie() {
		return institutie;
	}

	public void setInstitutie(NomenclatorValue institutie) {
		this.institutie = institutie;
	}
	
	@ManyToOne
	@JoinColumn(name = "MEMBRU_INSTITUTIE_ID")
	public NomenclatorValue getMembruInstitutie() {
		return membruInstitutie;
	}

	public void setMembruInstitutie(NomenclatorValue membruInstitutie) {
		this.membruInstitutie = membruInstitutie;
	}
	
	@Column(name = "NUME")
	public String getNume() {
		return nume;
	}
	
	public void setNume(String nume) {
		this.nume = nume;
	}
	
	@Column(name = "PRENUME")
	public String getPrenume() {
		return prenume;
	}
	
	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}
	
	@Column(name = "FUNCTIE")
	public String getFunctie() {
		return functie;
	}
	
	public void setFunctie(String functie) {
		this.functie = functie;
	}
	
	@Column(name = "DEPARTAMENT")
	public String getDepartament() {
		return departament;
	}
	
	public void setDepartament(String departament) {
		this.departament = departament;
	}
	
	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "TELEFON")
	public String getTelefon() {
		return telefon;
	}
	
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	
	@Column(name = "CALITATE", nullable = false)
	@Enumerated(EnumType.STRING)
	public Calitate getCalitate() {
		return calitate;
	}

	public void setCalitate(Calitate calitate) {
		this.calitate = calitate;
	}
	
	@Column(name = "STARE", nullable = false)
	@Enumerated(EnumType.STRING)
	public Stare getStare() {
		return stare;
	}

	public void setStare(Stare stare) {
		this.stare = stare;
	}
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="membru")
	public List<DiplomaMembruReprezentantiComisieSauGL> getDiplome() {
		return diplome;
	}
	
	public void setDiplome(List<DiplomaMembruReprezentantiComisieSauGL> diplome) {
		this.diplome = diplome;
	}
} 
