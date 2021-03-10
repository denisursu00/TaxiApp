package ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariModel.RaspunsuriBanciCuPropuneriEnum;

@Entity
@Table(name = "REGISTRU_INTRARI", uniqueConstraints = @UniqueConstraint(columnNames = { "numar_inregistrare" }))
public class RegistruIntrari {

	public Long id;
	public String numarInregistrare;
	public Date dataInregistrare;
	public String numeEmitent;
	public NomenclatorValue emitent;
	public String departamentEmitent;
	public String numarDocumentEmitent;
	public Date dataDocumentEmitent;
	public NomenclatorValue tipDocument;
	public boolean trimisPeMail;
	public String continut;
	public Integer numarPagini;
	public Integer numarAnexe;
	public User repartizatCatre;
	public List<NomenclatorValue> comisiiSauGL;
	public List<Project> proiecte;
	public boolean necesitaRaspuns;
	public Date termenRaspuns;
	public Long registruIesiriId;
	public String observatii;
	public RaspunsuriBanciCuPropuneriEnum raspunsuriBanciCuPropuneri;
	public Integer nrZileIntrareEmitent;
	public Integer nrZileRaspunsIntrare;
	public Integer nrZileRaspunsEmitent;
	public Integer nrZileTermenDataRaspuns;
	private boolean anulat;
	private String motivAnulare;
	private boolean inchis;
	private boolean trebuieAnulat;
	private List<RegistruIntrariAtasament> atasamente;
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

	@Column(name = "nume_emitent")
	public String getNumeEmitent() {
		return numeEmitent;
	}

	public void setNumeEmitent(String numeEmitent) {
		this.numeEmitent = numeEmitent;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "emitent_id")
	public NomenclatorValue getEmitent() {
		return emitent;
	}

	public void setEmitent(NomenclatorValue emitent) {
		this.emitent = emitent;
	}

	@Column(name = "departament_emitent")
	public String getDepartamentEmitent() {
		return departamentEmitent;
	}

	public void setDepartamentEmitent(String departamentEmitent) {
		this.departamentEmitent = departamentEmitent;
	}

	@Column(name = "numar_document_emitent")
	public String getNumarDocumentEmitent() {
		return numarDocumentEmitent;
	}

	public void setNumarDocumentEmitent(String numarDocumentEmitent) {
		this.numarDocumentEmitent = numarDocumentEmitent;
	}

	@Column(name = "data_document_emitent")
	public Date getDataDocumentEmitent() {
		return dataDocumentEmitent;
	}

	public void setDataDocumentEmitent(Date dataDocumentEmitent) {
		this.dataDocumentEmitent = dataDocumentEmitent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tip_document_id")
	public NomenclatorValue getTipDocument() {
		return tipDocument;
	}

	public void setTipDocument(NomenclatorValue tipDocument) {
		this.tipDocument = tipDocument;
	}

	@Column(name = "trimis_pe_mail")
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
	@JoinColumn(name = "repartizat_catre_user_id")
	public User getRepartizatCatre() {
		return repartizatCatre;
	}

	public void setRepartizatCatre(User repartizatCatre) {
		this.repartizatCatre = repartizatCatre;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "registru_intrari_comisie_gl",
		joinColumns = @JoinColumn(name = "registru_intrari_id", referencedColumnName = "id", nullable = false),
		inverseJoinColumns = @JoinColumn(name = "comisii_gl_id", referencedColumnName = "id", nullable = false)
	)
	public List<NomenclatorValue> getComisiiSauGL() {
		return comisiiSauGL;
	}
	
	public void setComisiiSauGL(List<NomenclatorValue> comisiiSauGL) {
		this.comisiiSauGL = comisiiSauGL;
	}
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "registru_intrari_proiect",
		joinColumns = @JoinColumn(name = "registru_intrari_id", referencedColumnName = "id", nullable = false),
		inverseJoinColumns = @JoinColumn(name = "proiect_id", referencedColumnName = "id", nullable = false)
	)
	public List<Project> getProiecte() {
		return proiecte;
	}

	public void setProiecte(List<Project> proiecte) {
		this.proiecte = proiecte;
	}
	
	@Column(name = "necesita_raspuns")
	public boolean isNecesitaRaspuns() {
		return necesitaRaspuns;
	}

	public void setNecesitaRaspuns(boolean necesitaRaspuns) {
		this.necesitaRaspuns = necesitaRaspuns;
	}

	@Column(name = "termen_raspuns")
	public Date getTermenRaspuns() {
		return termenRaspuns;
	}

	public void setTermenRaspuns(Date termenRaspuns) {
		this.termenRaspuns = termenRaspuns;
	}

	@Column(name = "registru_iesiri_id")
	public Long getRegistruIesiriId() {
		return registruIesiriId;
	}

	public void setRegistruIesiriId(Long registruIesiriId) {
		this.registruIesiriId = registruIesiriId;
	}

	@Column(name = "observatii")
	public String getObservatii() {
		return observatii;
	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}

	@Column(name = "raspunsuri_banci_cu_propuneri")
	@Enumerated(value = EnumType.STRING)
	public RaspunsuriBanciCuPropuneriEnum getRaspunsuriBanciCuPropuneri() {
		return raspunsuriBanciCuPropuneri;
	}

	public void setRaspunsuriBanciCuPropuneri(RaspunsuriBanciCuPropuneriEnum raspunsuriBanciCuPropuneri) {
		this.raspunsuriBanciCuPropuneri = raspunsuriBanciCuPropuneri;
	}

	@Column(name = "nr_zile_intrare_emitent")
	public Integer getNrZileIntrareEmitent() {
		return nrZileIntrareEmitent;
	}
	
	public void setNrZileIntrareEmitent(Integer nrZileIntrareEmitent) {
		this.nrZileIntrareEmitent = nrZileIntrareEmitent;
	}

	@Column(name = "nr_zile_raspuns_intrare")
	public Integer getNrZileRaspunsIntrare() {
		return nrZileRaspunsIntrare;
	}
	
	public void setNrZileRaspunsIntrare(Integer nrZileRaspunsIntrare) {
		this.nrZileRaspunsIntrare = nrZileRaspunsIntrare;
	}

	@Column(name = "nr_zile_raspuns_emitent")
	public Integer getNrZileRaspunsEmitent() {
		return nrZileRaspunsEmitent;
	}
	
	public void setNrZileRaspunsEmitent(Integer nrZileRaspunsEmitent) {
		this.nrZileRaspunsEmitent = nrZileRaspunsEmitent;
	}
	
	@Column(name = "nr_zile_termen_data_raspuns")
	public Integer getNrZileTermenDataRaspuns() {
		return nrZileTermenDataRaspuns;
	}
	
	public void setNrZileTermenDataRaspuns(Integer nrZileTermenDataRaspuns) {
		this.nrZileTermenDataRaspuns = nrZileTermenDataRaspuns;
	}

	@Column(name = "anulat")
	public boolean isAnulat() {
		return anulat;
	}

	public void setAnulat(boolean anulat) {
		this.anulat = anulat;
	}

	@Column(name = "motiv_anulare")
	public String getMotivAnulare() {
		return motivAnulare;
	}
	
	public void setMotivAnulare(String motivAnulare) {
		this.motivAnulare = motivAnulare;
	}

	public boolean isInchis() {
		return inchis;
	}

	public void setInchis(boolean inchis) {
		this.inchis = inchis;
	}
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "registruIntrari")
	public List<RegistruIntrariAtasament> getAtasamente() {
		return atasamente;
	}

	public void setAtasamente(List<RegistruIntrariAtasament> atasamente) {
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
