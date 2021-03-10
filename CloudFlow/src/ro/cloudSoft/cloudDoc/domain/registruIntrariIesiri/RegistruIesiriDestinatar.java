package ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;

@Entity
@Table(name = "REGISTRU_IESIRI_DESTINATAR")
public class RegistruIesiriDestinatar {

	private Long id;
	private String nume;
	private NomenclatorValue institutie;
	private String departament;
	private String numarInregistrare;
	private Date dataInregistrare;
	private RegistruIntrari registruIntrari;
	private String observatii;
	private NomenclatorValue comisieGl;
	private RegistruIesiri registruIesiri;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "nume")
	public String getNume() {
		return nume;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "institutie_id")
	public NomenclatorValue getInstitutie() {
		return institutie;
	}

	public void setInstitutie(NomenclatorValue institutie) {
		this.institutie = institutie;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	@Column(name = "departament")
	public String getDepartament() {
		return departament;
	}

	public void setDepartament(String departament) {
		this.departament = departament;
	}

	@Column(name = "numar_inregistrare")
	public String getNumarInregistrare() {
		return numarInregistrare;
	}

	public void setNumarInregistrare(String numarInregistrare) {
		this.numarInregistrare = numarInregistrare;
	}

	@Column(name = "data_inregistrare")
	public Date getDataInregistrare() {
		return dataInregistrare;
	}

	public void setDataInregistrare(Date dataInregistrare) {
		this.dataInregistrare = dataInregistrare;
	}

	@OneToOne(fetch = FetchType.LAZY, targetEntity = RegistruIntrari.class)
	@JoinColumn(name = "registru_intrari_id")
	public RegistruIntrari getRegistruIntrari() {
		return registruIntrari;
	}

	public void setRegistruIntrari(RegistruIntrari registruIntrari) {
		this.registruIntrari = registruIntrari;
	}

	@Column(name = "observatii")
	public String getObservatii() {
		return observatii;
	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "comisii_gl_id")
	public NomenclatorValue getComisieGl() {
		return comisieGl;
	}

	public void setComisieGl(NomenclatorValue comisieGl) {
		this.comisieGl = comisieGl;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "registru_iesiri_id")
	public RegistruIesiri getRegistruIesiri() {
		return registruIesiri;
	}

	public void setRegistruIesiri(RegistruIesiri registruIesiri) {
		this.registruIesiri = registruIesiri;
	}
}
