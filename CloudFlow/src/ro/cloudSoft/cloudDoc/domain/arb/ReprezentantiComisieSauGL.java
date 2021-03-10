package ro.cloudSoft.cloudDoc.domain.arb;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.User;

@Entity
@Table(name="REPREZENTANTI_COMISIE_GL")
public class ReprezentantiComisieSauGL {
	
	private Long id;
	
	private NomenclatorValue comisieSauGL;
	
	private NomenclatorValue presedinte;
	private Date dataInceputMandatPresedinte;
	private Date dataExpirareMandatPresedinte;
	
	private NomenclatorValue vicepresedinte1;
	private Date dataInceputMandatVicepresedinte1;
	private Date dataExpirareMandatVicepresedinte1;
	
	private NomenclatorValue vicepresedinte2;
	private Date dataInceputMandatVicepresedinte2;
	private Date dataExpirareMandatVicepresedinte2;
	
	private NomenclatorValue vicepresedinte3;
	private Date dataInceputMandatVicepresedinte3;
	private Date dataExpirareMandatVicepresedinte3;
	
	private User responsabilARB;
	
	private NomenclatorValue membruCDCoordonator;
	private Date dataInceputMandatMembruCDCoordonator;
	private Date dataExpirareMandatMembruCDCoordonator;
	
	private List<MembruReprezentantiComisieSauGL> membri;
	
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
	@JoinColumn(name = "COMISIE_GL_ID", nullable = false)
	public NomenclatorValue getComisieSauGL() {
		return comisieSauGL;
	}
	
	public void setComisieSauGL(NomenclatorValue comisieSauGL) {
		this.comisieSauGL = comisieSauGL;
	}
	
	@ManyToOne
	@JoinColumn(name = "PRESEDINTE_ID")
	public NomenclatorValue getPresedinte() {
		return presedinte;
	}
	
	@Column(name = "DATA_INC_MANDAT_PRESEDINTE")
	public Date getDataInceputMandatPresedinte() {
		return dataInceputMandatPresedinte;
	}
	
	public void setDataInceputMandatPresedinte(Date dataInceputMandatPresedinte) {
		this.dataInceputMandatPresedinte = dataInceputMandatPresedinte;
	}
	
	public void setPresedinte(NomenclatorValue presedinte) {
		this.presedinte = presedinte;
	}
	
	@Column(name = "DATA_EXP_MANDAT_PRESEDINTE")
	public Date getDataExpirareMandatPresedinte() {
		return dataExpirareMandatPresedinte;
	}
	
	public void setDataExpirareMandatPresedinte(Date dataExpirareMandatPresedinte) {
		this.dataExpirareMandatPresedinte = dataExpirareMandatPresedinte;
	}
	
	@ManyToOne
	@JoinColumn(name = "VICEPRESEDINTE1_ID")
	public NomenclatorValue getVicepresedinte1() {
		return vicepresedinte1;
	}
	
	public void setVicepresedinte1(NomenclatorValue vicepresedinte1) {
		this.vicepresedinte1 = vicepresedinte1;
	}
	
	@Column(name = "DATA_INC_MANDAT_VICEPRESEDINT1")
	@Temporal(TemporalType.DATE)
	public Date getDataInceputMandatVicepresedinte1() {
		return dataInceputMandatVicepresedinte1;
	}
	
	public void setDataInceputMandatVicepresedinte1(Date dataInceputMandatVicepresedinte1) {
		this.dataInceputMandatVicepresedinte1 = dataInceputMandatVicepresedinte1;
	}
		
	@Column(name = "DATA_EXP_MANDAT_VICEPRESEDINT1")
	@Temporal(TemporalType.DATE)
	public Date getDataExpirareMandatVicepresedinte1() {
		return dataExpirareMandatVicepresedinte1;
	}
	
	public void setDataExpirareMandatVicepresedinte1(Date dataExpirareMandatVicepresedinte1) {
		this.dataExpirareMandatVicepresedinte1 = dataExpirareMandatVicepresedinte1;
	}
	
	@ManyToOne
	@JoinColumn(name = "VICEPRESEDINTE2_ID")
	public NomenclatorValue getVicepresedinte2() {
		return vicepresedinte2;
	}
	
	public void setVicepresedinte2(NomenclatorValue vicepresedinte2) {
		this.vicepresedinte2 = vicepresedinte2;
	}
	
	@Column(name = "DATA_INC_MANDAT_VICEPRESEDINT2")
	@Temporal(TemporalType.DATE)
	public Date getDataInceputMandatVicepresedinte2() {
		return dataInceputMandatVicepresedinte2;
	}
	
	public void setDataInceputMandatVicepresedinte2(Date dataInceputMandatVicepresedinte2) {
		this.dataInceputMandatVicepresedinte2 = dataInceputMandatVicepresedinte2;
	}
	
	@Column(name = "DATA_EXP_MANDAT_VICEPRESEDINT2")
	@Temporal(TemporalType.DATE)
	public Date getDataExpirareMandatVicepresedinte2() {
		return dataExpirareMandatVicepresedinte2;
	}
	
	public void setDataExpirareMandatVicepresedinte2(Date dataExpirareMandatVicepresedinte2) {
		this.dataExpirareMandatVicepresedinte2 = dataExpirareMandatVicepresedinte2;
	}
	
	@ManyToOne
	@JoinColumn(name = "VICEPRESEDINTE3_ID")
	public NomenclatorValue getVicepresedinte3() {
		return vicepresedinte3;
	}
	
	public void setVicepresedinte3(NomenclatorValue vicepresedinte3) {
		this.vicepresedinte3 = vicepresedinte3;
	}
	
	@Column(name = "DATA_INC_MANDAT_VICEPRESEDINT3")
	@Temporal(TemporalType.DATE)
	public Date getDataInceputMandatVicepresedinte3() {
		return dataInceputMandatVicepresedinte3;
	}
	
	public void setDataInceputMandatVicepresedinte3(Date dataInceputMandatVicepresedinte3) {
		this.dataInceputMandatVicepresedinte3 = dataInceputMandatVicepresedinte3;
	}
	
	@Column(name = "DATA_EXP_MANDAT_VICEPRESEDINT3")
	@Temporal(TemporalType.DATE)
	public Date getDataExpirareMandatVicepresedinte3() {
		return dataExpirareMandatVicepresedinte3;
	}
	
	public void setDataExpirareMandatVicepresedinte3(Date dataExpirareMandatVicepresedinte3) {
		this.dataExpirareMandatVicepresedinte3 = dataExpirareMandatVicepresedinte3;
	}
	
	@ManyToOne
	@JoinColumn(name = "RESPONSABIL_ARB_ID")
	public User getResponsabilARB() {
		return responsabilARB;
	}
	
	public void setResponsabilARB(User responsabilARB) {
		this.responsabilARB = responsabilARB;
	}
	
	@ManyToOne
	@JoinColumn(name = "MEMBRU_CD_COORDONATOR_ID")
	public NomenclatorValue getMembruCDCoordonator() {
		return membruCDCoordonator;
	}
	
	public void setMembruCDCoordonator(NomenclatorValue membruCDCoordonator) {
		this.membruCDCoordonator = membruCDCoordonator;
	}
	
	@Column(name = "DATA_INC_MANDAT_MEMBRU_CD_CRDT")
	@Temporal(TemporalType.DATE)
	public Date getDataInceputMandatMembruCDCoordonator() {
		return dataInceputMandatMembruCDCoordonator;
	}
	
	public void setDataInceputMandatMembruCDCoordonator(Date dataInceputMandatMembruCDCoordonator) {
		this.dataInceputMandatMembruCDCoordonator = dataInceputMandatMembruCDCoordonator;
	}
	
	@Column(name = "DATA_EXP_MANDAT_MEMBRU_CD_CRDT")
	@Temporal(TemporalType.DATE)
	public Date getDataExpirareMandatMembruCDCoordonator() {
		return dataExpirareMandatMembruCDCoordonator;
	}
	
	public void setDataExpirareMandatMembruCDCoordonator(Date dataExpirareMandatMembruCDCoordonator) {
		this.dataExpirareMandatMembruCDCoordonator = dataExpirareMandatMembruCDCoordonator;
	}
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="reprezentantiComisieSauGL")
	public List<MembruReprezentantiComisieSauGL> getMembri() {
		return membri;
	}	
	
	public void setMembri(List<MembruReprezentantiComisieSauGL> membri) {
		this.membri = membri;
	}	
}
