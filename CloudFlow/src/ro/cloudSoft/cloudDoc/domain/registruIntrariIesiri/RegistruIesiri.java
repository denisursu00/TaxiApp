package ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.cloudDoc.domain.project.Project;
import ro.cloudSoft.cloudDoc.domain.project.Subactivity;

@Entity
@Table(name = "REGISTRU_IESIRI", uniqueConstraints = @UniqueConstraint(columnNames = { "numar_inregistrare" }))
public class RegistruIesiri {

	private Long id;
	private String numarInregistrare;
	private Date dataInregistrare;
	private NomenclatorValue tipDocument;
	private Integer numarPagini;
	private Integer numarAnexe;
	private User intocmitDe;
	private boolean trimisPeMail;
	private String continut;
	private boolean asteptamRaspuns;
	private Date termenRaspuns;
	private List<RegistruIesiriDestinatar> destinatari;
	private List<Project> proiecte;
	private boolean anulat;
	private String motivAnulare;
	private boolean inchis;
	private boolean trebuieAnulat;
	private List<RegistruIesiriAtasament> atasamente;
	private Subactivity subactivity;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tip_document_id")
	public NomenclatorValue getTipDocument() {
		return tipDocument;
	}

	public void setTipDocument(NomenclatorValue tipDocument) {
		this.tipDocument = tipDocument;
	}
	
	@Column(name = "numar_pagini")
	public Integer getNumarPagini() {
		return numarPagini;
	}

	public void setNumarPagini(Integer numarPagini) {
		this.numarPagini = numarPagini;
	}

	@Column(name = "numar_anexe")
	public Integer getNumarAnexe() {
		return numarAnexe;
	}

	public void setNumarAnexe(Integer numarAnexe) {
		this.numarAnexe = numarAnexe;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "intocmit_de_user_id")
	public User getIntocmitDe() {
		return intocmitDe;
	}

	public void setIntocmitDe(User intocmitDe) {
		this.intocmitDe = intocmitDe;
	}
	
	@Column(name = "mail")
	public boolean isTrimisPeMail() {
		return trimisPeMail;
	}

	public void setTrimisPeMail(boolean trimisPeMail) {
		this.trimisPeMail = trimisPeMail;
	}
	
	@Column(name = "continut")
	public String getContinut() {
		return continut;
	}

	public void setContinut(String continut) {
		this.continut = continut;
	}

	@Column(name = "asteptam_raspuns")
	public boolean isAsteptamRaspuns() {
		return asteptamRaspuns;
	}

	public void setAsteptamRaspuns(boolean asteptamRaspuns) {
		this.asteptamRaspuns = asteptamRaspuns;
	}

	@Column(name = "termen_raspuns")
	public Date getTermenRaspuns() {
		return termenRaspuns;
	}

	public void setTermenRaspuns(Date termenRaspuns) {
		this.termenRaspuns = termenRaspuns;
	}
	
	@OneToMany(mappedBy = "registruIesiri", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<RegistruIesiriDestinatar> getDestinatari() {
		return destinatari;
	}

	public void setDestinatari(List<RegistruIesiriDestinatar> destinatari) {
		this.destinatari = destinatari;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "registru_iesiri_proiect",
		joinColumns = @JoinColumn(name = "registru_iesiri_id", referencedColumnName = "id", nullable = false),
		inverseJoinColumns = @JoinColumn(name = "proiect_id", referencedColumnName = "id", nullable = false)
	)
	public List<Project> getProiecte() {
		return proiecte;
	}

	public void setProiecte(List<Project> proiecte) {
		this.proiecte = proiecte;
	}
	
	@Column(name = "anulat")
	public boolean isAnulat() {
		return anulat;
	}
	public void setAnulat(boolean anulat) {
		this.anulat = anulat;
	}

	@Column(name = "motiv_anumare")
	public String getMotivAnulare() {
		return motivAnulare;
	}
	
	public void setMotivAnulare(String motivAnulare) {
		this.motivAnulare = motivAnulare;
	}

	@Column(name = "inchis")
	public boolean isInchis() {
		return inchis;
	}

	public void setInchis(boolean inchis) {
		this.inchis = inchis;
	}
	
	@OneToMany(mappedBy = "registruIesiri", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<RegistruIesiriAtasament> getAtasamente() {
		return atasamente;
	}

	public void setAtasamente(List<RegistruIesiriAtasament> atasamente) {
		this.atasamente = atasamente;
	}

	@Column(name = "trebuie_anulat")
	public boolean isTrebuieAnulat() {
		return trebuieAnulat;
	}

	public void setTrebuieAnulat(boolean trebuieAnulat) {
		this.trebuieAnulat = trebuieAnulat;
	}

	@OneToOne
	@JoinColumn(name="subactivity_id", nullable = true)
	public Subactivity getSubactivity() {
		return subactivity;
	}

	public void setSubactivity(Subactivity subactivity) {
		this.subactivity = subactivity;
	}
	
}
