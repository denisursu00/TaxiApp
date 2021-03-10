package ro.cloudSoft.cloudDoc.domain.prezentaOnline;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;

@Entity
@Table(name = "prezenta_online_participanti")
public class PrezentaOnlineParticipanti {
	
	private Long id;
	private String documentId;
	private String documentLocationRealName;
	private NomenclatorValue institutie;
	private NomenclatorValue membruInstitutie;
	private String nume;
	private String prenume;
	private String functie;
	private String departament;
	private String telefon;
	private String email;
	private String calitate;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="document_id")
	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	@Column(name="document_location_real_name")
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}

	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "institutie_id")
	public NomenclatorValue getInstitutie() {
		return institutie;
	}

	public void setInstitutie(NomenclatorValue institutie) {
		this.institutie = institutie;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "membru_institutie_id")
	public NomenclatorValue getMembruInstitutie() {
		return membruInstitutie;
	}

	public void setMembruInstitutie(NomenclatorValue membruInstitutie) {
		this.membruInstitutie = membruInstitutie;
	}

	@Column(name="nume")
	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	@Column(name="prenume")
	public String getPrenume() {
		return prenume;
	}

	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}

	@Column(name="functie")
	public String getFunctie() {
		return functie;
	}

	public void setFunctie(String functie) {
		this.functie = functie;
	}

	@Column(name="departament")
	public String getDepartament() {
		return departament;
	}

	public void setDepartament(String departament) {
		this.departament = departament;
	}

	@Column(name="telefon")
	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	@Column(name="email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="calitate")
	public String getCalitate() {
		return calitate;
	}

	public void setCalitate(String calitate) {
		this.calitate = calitate;
	}

}
