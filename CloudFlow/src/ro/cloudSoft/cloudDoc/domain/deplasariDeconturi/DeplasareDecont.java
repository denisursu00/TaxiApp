package ro.cloudSoft.cloudDoc.domain.deplasariDeconturi;

import java.math.BigDecimal;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont.ModalitatePlataForDecontEnum;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;

@Entity
@Table(name = "DEPLASARI_DECONTURI", uniqueConstraints = @UniqueConstraint(columnNames = { "NUMAR_INREGISTRARE" }))
public class DeplasareDecont {

	private Long id;
	private String numarInregistrare;
	private ApelativReprezentantArbEnum apelativ;
	private NomenclatorValue reprezentantArb;
	private String numarDecizie;
	private String documentLocationRealName;
	private String documentId;
	private String denumireInstitutie;
	private Date dataDecizie;
	private Long organismId;
	private String denumireComitet;
	private Integer numarDeplasariEfectuate;
	private Integer numarDeplasariBugetateRamase;
	private String eveniment;
	private String tara;
	private String oras;
	private Date dataPlecare;
	private Date dataSosire;
	private Date dataConferintaInceput;
	private Date dataConferintaSfarsit;
	private Integer numarNopti;
	private boolean minutaIntalnireTransmisa;
	private String observatii;
	private Long detaliiNumarDeplasariBugetateNomenclatorValueId;
	
	private String cheltuieliArbTitularDecont;
	private String cheltuieliArbTipDecont;
	private Date cheltuieliArbDataDecont;
	private List<CheltuialaArb> cheltuieliArb;

	private BigDecimal cheltuieliReprezentantArbDiurnaZilnica;
	private String cheltuieliReprezentantArbDiurnaZilnicaValuta;
	private BigDecimal cheltuieliReprezentantArbDiurnaZilnicaCursValutar;
	private ModalitatePlataForDecontEnum cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata;
	private Integer cheltuieliReprezentantArbNumarZile;
	private BigDecimal cheltuieliReprezentantArbTotalDiurna;
	private BigDecimal cheltuieliReprezentantArbAvansPrimitSuma;
	private String cheltuieliReprezentantArbAvansPrimitSumaValuta;
	private BigDecimal cheltuieliReprezentantArbAvansPrimitSumaCursValutar;
	private ModalitatePlataForDecontEnum cheltuieliReprezentantArbAvansPrimitCardSauNumerar;
	private List<CheltuialaReprezentantArb> cheltuieliReprezentantArb;
	
	private boolean anulat;
	private String motivAnulare;
	private boolean finalizat;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NUMAR_INREGISTRARE")
	public String getNumarInregistrare() {
		return numarInregistrare;
	}

	public void setNumarInregistrare(String numarInregistrare) {
		this.numarInregistrare = numarInregistrare;
	}

	@Column(name = "APELATIV")
	@Enumerated(value = EnumType.STRING)
	public ApelativReprezentantArbEnum getApelativ() {
		return apelativ;
	}

	public void setApelativ(ApelativReprezentantArbEnum apelativ) {
		this.apelativ = apelativ;
	}

	@ManyToOne()
	@JoinColumn(name = "REPREZENTANT_ARB_ID", nullable = false)
	public NomenclatorValue getReprezentantArb() {
		return reprezentantArb;
	}

	public void setReprezentantArb(NomenclatorValue reprezentantArb) {
		this.reprezentantArb = reprezentantArb;
	}

	@Column(name = "NUMAR_DECIZIE")
	public String getNumarDecizie() {
		return numarDecizie;
	}

	public void setNumarDecizie(String numarDecizie) {
		this.numarDecizie = numarDecizie;
	}

	@Column(name = "DOCUMENT_LOCATION_REAL_NAME")
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}

	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}

	@Column(name = "DOCUMENT_ID")
	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	@Column(name = "DENUMIRE_INSTITUTIE")
	public String getDenumireInstitutie() {
		return denumireInstitutie;
	}
	
	public void setDenumireInstitutie(String denumireInstitutie) {
		this.denumireInstitutie = denumireInstitutie;
	}

	@Column(name = "DATA_DECIZIE")
	public Date getDataDecizie() {
		return dataDecizie;
	}

	public void setDataDecizie(Date dataDecizie) {
		this.dataDecizie = dataDecizie;
	}
	
	@Column(name = "ORGANISM_ID")
	public Long getOrganismId() {
		return organismId;
	}
	
	public void setOrganismId(Long organismId) {
		this.organismId = organismId;
	}

	@Column(name = "DENUMIRE_COMITET")
	public String getDenumireComitet() {
		return denumireComitet;
	}
	
	public void setDenumireComitet(String denumireComitet) {
		this.denumireComitet = denumireComitet;
	}

	@Column(name = "NUMAR_DEPLASARI_EFECTUATE")
	public Integer getNumarDeplasariEfectuate() {
		return numarDeplasariEfectuate;
	}

	public void setNumarDeplasariEfectuate(Integer numarDeplasariEfectuate) {
		this.numarDeplasariEfectuate = numarDeplasariEfectuate;
	}

	@Column(name = "NUMAR_DEPLASARI_BUG_RAMASE")
	public Integer getNumarDeplasariBugetateRamase() {
		return numarDeplasariBugetateRamase;
	}

	public void setNumarDeplasariBugetateRamase(Integer numarDeplasariBugetateRamase) {
		this.numarDeplasariBugetateRamase = numarDeplasariBugetateRamase;
	}

	@Column(name = "EVENIMENT")
	public String getEveniment() {
		return eveniment;
	}

	public void setEveniment(String eveniment) {
		this.eveniment = eveniment;
	}

	@Column(name = "TARA")
	public String getTara() {
		return tara;
	}

	public void setTara(String tara) {
		this.tara = tara;
	}

	@Column(name = "ORAS")
	public String getOras() {
		return oras;
	}

	public void setOras(String oras) {
		this.oras = oras;
	}

	@Column(name = "DATA_PLECARE")
	public Date getDataPlecare() {
		return dataPlecare;
	}

	public void setDataPlecare(Date dataPlecare) {
		this.dataPlecare = dataPlecare;
	}

	@Column(name = "DATA_SOSIRE")
	public Date getDataSosire() {
		return dataSosire;
	}

	public void setDataSosire(Date dataSosire) {
		this.dataSosire = dataSosire;
	}

	@Column(name = "DATA_CONFERINTA_INCEPUT")
	public Date getDataConferintaInceput() {
		return dataConferintaInceput;
	}

	public void setDataConferintaInceput(Date dataConferintaInceput) {
		this.dataConferintaInceput = dataConferintaInceput;
	}

	@Column(name = "DATA_CONFERINTA_SFARSIT")
	public Date getDataConferintaSfarsit() {
		return dataConferintaSfarsit;
	}

	public void setDataConferintaSfarsit(Date dataConferintaSfarsit) {
		this.dataConferintaSfarsit = dataConferintaSfarsit;
	}
	
	@Column(name = "NUMAR_NOPTI")
	public Integer getNumarNopti() {
		return numarNopti;
	}

	public void setNumarNopti(Integer numarNopti) {
		this.numarNopti = numarNopti;
	}

	@Column(name = "MINUTA_INTALNIRE_TRANSMISA")
	public boolean isMinutaIntalnireTransmisa() {
		return minutaIntalnireTransmisa;
	}

	public void setMinutaIntalnireTransmisa(boolean minutaIntalnireTransmisa) {
		this.minutaIntalnireTransmisa = minutaIntalnireTransmisa;
	}

	@Column(name = "OBSERVATII")
	public String getObservatii() {
		return observatii;
	}

	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}

	@Column(name = "detalii_numar_deplasari_bugetate_nomenclator_value_id")
	public Long getDetaliiNumarDeplasariBugetateNomenclatorValueId() {
		return detaliiNumarDeplasariBugetateNomenclatorValueId;
	}

	public void setDetaliiNumarDeplasariBugetateNomenclatorValueId(Long detaliiNumarDeplasariBugetateNomenclatorValueId) {
		this.detaliiNumarDeplasariBugetateNomenclatorValueId = detaliiNumarDeplasariBugetateNomenclatorValueId;
	}

	@Column(name = "CHELTUIELI_ARB_TITULAR_DECONT")
	public String getCheltuieliArbTitularDecont() {
		return cheltuieliArbTitularDecont;
	}

	public void setCheltuieliArbTitularDecont(String cheltuieliArbTitularDecont) {
		this.cheltuieliArbTitularDecont = cheltuieliArbTitularDecont;
	}

	@Column(name = "CHELTUIELI_ARB_TIP_DECONT")
	public String getCheltuieliArbTipDecont() {
		return cheltuieliArbTipDecont;
	}

	public void setCheltuieliArbTipDecont(String cheltuieliArbTipDecont) {
		this.cheltuieliArbTipDecont = cheltuieliArbTipDecont;
	}

	@Column(name = "CHELTUIELI_ARB_DATA_DECONT")
	public Date getCheltuieliArbDataDecont() {
		return cheltuieliArbDataDecont;
	}

	public void setCheltuieliArbDataDecont(Date cheltuieliArbDataDecont) {
		this.cheltuieliArbDataDecont = cheltuieliArbDataDecont;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "deplasareDecont")
	public List<CheltuialaArb> getCheltuieliArb() {
		return cheltuieliArb;
	}

	public void setCheltuieliArb(List<CheltuialaArb> cheltuieliArb) {
		this.cheltuieliArb = cheltuieliArb;
	}

	@Column(name = "CHEL_REP_ARB_DIURNA_ZILNICA")
	public BigDecimal getCheltuieliReprezentantArbDiurnaZilnica() {
		return cheltuieliReprezentantArbDiurnaZilnica;
	}

	public void setCheltuieliReprezentantArbDiurnaZilnica(BigDecimal cheltuieliReprezentantArbDiurnaZilnica) {
		this.cheltuieliReprezentantArbDiurnaZilnica = cheltuieliReprezentantArbDiurnaZilnica;
	}

	@Column(name = "CHEL_REP_ARB_D_Z_VALUTA")
	public String getCheltuieliReprezentantArbDiurnaZilnicaValuta() {
		return cheltuieliReprezentantArbDiurnaZilnicaValuta;
	}

	public void setCheltuieliReprezentantArbDiurnaZilnicaValuta(String cheltuieliReprezentantArbDiurnaZilnicaValuta) {
		this.cheltuieliReprezentantArbDiurnaZilnicaValuta = cheltuieliReprezentantArbDiurnaZilnicaValuta;
	}

	@Column(name = "CHEL_REP_ARB_D_Z_CURS_VALUTAR")
	public BigDecimal getCheltuieliReprezentantArbDiurnaZilnicaCursValutar() {
		return cheltuieliReprezentantArbDiurnaZilnicaCursValutar;
	}

	public void setCheltuieliReprezentantArbDiurnaZilnicaCursValutar(
			BigDecimal cheltuieliReprezentantArbDiurnaZilnicaCursValutar) {
		this.cheltuieliReprezentantArbDiurnaZilnicaCursValutar = cheltuieliReprezentantArbDiurnaZilnicaCursValutar;
	}
	
	@Column(name = "CHEL_REP_ARB_D_Z_MODALITATE_PLATA")
	@Enumerated(EnumType.STRING)
	public ModalitatePlataForDecontEnum getCheltuieliReprezentantArbDiurnaZilnicaModalitatePlata() {
		return cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata;
	}

	public void setCheltuieliReprezentantArbDiurnaZilnicaModalitatePlata(
			ModalitatePlataForDecontEnum cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata) {
		this.cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata = cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata;
	}

	@Column(name = "CHEL_REP_ARB_NUMAR_ZILE")
	public Integer getCheltuieliReprezentantArbNumarZile() {
		return cheltuieliReprezentantArbNumarZile;
	}

	public void setCheltuieliReprezentantArbNumarZile(Integer cheltuieliReprezentantArbNumarZile) {
		this.cheltuieliReprezentantArbNumarZile = cheltuieliReprezentantArbNumarZile;
	}

	@Column(name = "CHEL_REP_ARB_TOTAL_DIURNA")
	public BigDecimal getCheltuieliReprezentantArbTotalDiurna() {
		return cheltuieliReprezentantArbTotalDiurna;
	}

	public void setCheltuieliReprezentantArbTotalDiurna(BigDecimal cheltuieliReprezentantArbTotalDiurna) {
		this.cheltuieliReprezentantArbTotalDiurna = cheltuieliReprezentantArbTotalDiurna;
	}

	@Column(name = "CHEL_REP_ARB_AVANS_PR_SUMA")
	public BigDecimal getCheltuieliReprezentantArbAvansPrimitSuma() {
		return cheltuieliReprezentantArbAvansPrimitSuma;
	}

	public void setCheltuieliReprezentantArbAvansPrimitSuma(BigDecimal cheltuieliReprezentantArbAvansPrimitSuma) {
		this.cheltuieliReprezentantArbAvansPrimitSuma = cheltuieliReprezentantArbAvansPrimitSuma;
	}

	@Column(name = "CHEL_REP_ARB_AVANS_P_S_VALUTA")
	public String getCheltuieliReprezentantArbAvansPrimitSumaValuta() {
		return cheltuieliReprezentantArbAvansPrimitSumaValuta;
	}

	public void setCheltuieliReprezentantArbAvansPrimitSumaValuta(String cheltuieliReprezentantArbAvansPrimitSumaValuta) {
		this.cheltuieliReprezentantArbAvansPrimitSumaValuta = cheltuieliReprezentantArbAvansPrimitSumaValuta;
	}

	@Column(name = "CHEL_REP_ARB_A_P_S_CURS_VALUT")
	public BigDecimal getCheltuieliReprezentantArbAvansPrimitSumaCursValutar() {
		return cheltuieliReprezentantArbAvansPrimitSumaCursValutar;
	}

	public void setCheltuieliReprezentantArbAvansPrimitSumaCursValutar(
			BigDecimal cheltuieliReprezentantArbAvansPrimitSumaCursValutar) {
		this.cheltuieliReprezentantArbAvansPrimitSumaCursValutar = cheltuieliReprezentantArbAvansPrimitSumaCursValutar;
	}

	@Column(name = "CHEL_REP_ARB_A_P_CARD_SAU_NUM")
	public ModalitatePlataForDecontEnum getCheltuieliReprezentantArbAvansPrimitCardSauNumerar() {
		return cheltuieliReprezentantArbAvansPrimitCardSauNumerar;
	}

	public void setCheltuieliReprezentantArbAvansPrimitCardSauNumerar(
			ModalitatePlataForDecontEnum cheltuieliReprezentantArbAvansPrimitCardSauNumerar) {
		this.cheltuieliReprezentantArbAvansPrimitCardSauNumerar = cheltuieliReprezentantArbAvansPrimitCardSauNumerar;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch= FetchType.LAZY, mappedBy = "deplasareDecont")
	public List<CheltuialaReprezentantArb> getCheltuieliReprezentantArb() {
		return cheltuieliReprezentantArb;
	}

	public void setCheltuieliReprezentantArb(List<CheltuialaReprezentantArb> cheltuieliReprezentantArb) {
		this.cheltuieliReprezentantArb = cheltuieliReprezentantArb;
	}

	@Column(name = "ANULAT")
	public boolean isAnulat() {
		return anulat;
	}

	public void setAnulat(boolean anulat) {
		this.anulat = anulat;
	}

	@Column(name = "MOTIV_ANULARE")
	public String getMotivAnulare() {
		return motivAnulare;
	}

	public void setMotivAnulare(String motivAnulare) {
		this.motivAnulare = motivAnulare;
	}

	@Column(name = "FINALIZAT")
	public boolean isFinalizat() {
		return finalizat;
	}

	public void setFinalizat(boolean finalizat) {
		this.finalizat = finalizat;
	}

	public static enum ApelativReprezentantArbEnum {
		DOMNISOARA,
		DOAMNA,
		DOMNUL
	}

	public static enum ModalitatePlataForDecontEnum {
		CARD,
		NUMERAR
	}

}
