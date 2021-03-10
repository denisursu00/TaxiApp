package ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DeplasareDecontModel {

	private Long id;
	private String numarInregistrare;
	private String apelativ;
	private Long reprezentantArbId;
	private String numarDecizie;
	private String documentLocationRealName;
	private String documentId;
	private String denumireInstitutie;
	
	private String denumireOrganism;
	private String abreviereOrganism;
	
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
	private List<CheltuialaArbModel> cheltuieliArb;
	
	private BigDecimal cheltuieliReprezentantArbDiurnaZilnica;
	private String cheltuieliReprezentantArbDiurnaZilnicaValuta;
	private BigDecimal cheltuieliReprezentantArbDiurnaZilnicaCursValutar;
	private String cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata;
	private Integer cheltuieliReprezentantArbNumarZile;
	private BigDecimal cheltuieliReprezentantArbTotalDiurna;
	private BigDecimal cheltuieliReprezentantArbAvansPrimitSuma;
	private String cheltuieliReprezentantArbAvansPrimitSumaValuta;
	private BigDecimal cheltuieliReprezentantArbAvansPrimitSumaCursValutar;
	private String cheltuieliReprezentantArbAvansPrimitCardSauNumerar;
	private List<CheltuialaReprezentantArbModel> cheltuieliReprezentantArb;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNumarInregistrare() {
		return numarInregistrare;
	}
	public void setNumarInregistrare(String numarInregistrare) {
		this.numarInregistrare = numarInregistrare;
	}
	public String getApelativ() {
		return apelativ;
	}
	public void setApelativ(String apelativ) {
		this.apelativ = apelativ;
	}
	public Long getReprezentantArbId() {
		return reprezentantArbId;
	}
	public void setReprezentantArbId(Long reprezentantArbId) {
		this.reprezentantArbId = reprezentantArbId;
	}
	public String getNumarDecizie() {
		return numarDecizie;
	}
	public void setNumarDecizie(String numarDecizie) {
		this.numarDecizie = numarDecizie;
	}
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getDenumireInstitutie() {
		return denumireInstitutie;
	}
	public void setDenumireInstitutie(String denumireInstitutie) {
		this.denumireInstitutie = denumireInstitutie;
	}
	public String getDenumireOrganism() {
		return denumireOrganism;
	}
	public void setDenumireOrganism(String denumireOrganism) {
		this.denumireOrganism = denumireOrganism;
	}
	public String getAbreviereOrganism() {
		return abreviereOrganism;
	}
	public void setAbreviereOrganism(String abreviereOrganism) {
		this.abreviereOrganism = abreviereOrganism;
	}
	public Date getDataDecizie() {
		return dataDecizie;
	}
	public void setDataDecizie(Date dataDecizie) {
		this.dataDecizie = dataDecizie;
	}
	public Long getOrganismId() {
		return organismId;
	}
	public void setOrganismId(Long organismId) {
		this.organismId = organismId;
	}
	public String getDenumireComitet() {
		return denumireComitet;
	}
	public void setDenumireComitet(String denumireComitet) {
		this.denumireComitet = denumireComitet;
	}
	public Integer getNumarDeplasariEfectuate() {
		return numarDeplasariEfectuate;
	}
	public void setNumarDeplasariEfectuate(Integer numarDeplasariEfectuate) {
		this.numarDeplasariEfectuate = numarDeplasariEfectuate;
	}
	public Integer getNumarDeplasariBugetateRamase() {
		return numarDeplasariBugetateRamase;
	}
	public void setNumarDeplasariBugetateRamase(Integer numarDeplasariBugetateRamase) {
		this.numarDeplasariBugetateRamase = numarDeplasariBugetateRamase;
	}
	public String getEveniment() {
		return eveniment;
	}
	public void setEveniment(String eveniment) {
		this.eveniment = eveniment;
	}
	public String getTara() {
		return tara;
	}
	public void setTara(String tara) {
		this.tara = tara;
	}
	public String getOras() {
		return oras;
	}
	public void setOras(String oras) {
		this.oras = oras;
	}
	public Date getDataPlecare() {
		return dataPlecare;
	}
	public void setDataPlecare(Date dataPlecare) {
		this.dataPlecare = dataPlecare;
	}
	public Date getDataSosire() {
		return dataSosire;
	}
	public void setDataSosire(Date dataSosire) {
		this.dataSosire = dataSosire;
	}
	public Date getDataConferintaInceput() {
		return dataConferintaInceput;
	}
	public void setDataConferintaInceput(Date dataConferintaInceput) {
		this.dataConferintaInceput = dataConferintaInceput;
	}
	public Date getDataConferintaSfarsit() {
		return dataConferintaSfarsit;
	}
	public void setDataConferintaSfarsit(Date dataConferintaSfarsit) {
		this.dataConferintaSfarsit = dataConferintaSfarsit;
	}
	public Integer getNumarNopti() {
		return numarNopti;
	}
	public void setNumarNopti(Integer numarNopti) {
		this.numarNopti = numarNopti;
	}
	public boolean isMinutaIntalnireTransmisa() {
		return minutaIntalnireTransmisa;
	}
	public void setMinutaIntalnireTransmisa(boolean minutaIntalnireTransmisa) {
		this.minutaIntalnireTransmisa = minutaIntalnireTransmisa;
	}
	public String getObservatii() {
		return observatii;
	}
	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}
	public Long getDetaliiNumarDeplasariBugetateNomenclatorValueId() {
		return detaliiNumarDeplasariBugetateNomenclatorValueId;
	}
	public void setDetaliiNumarDeplasariBugetateNomenclatorValueId(Long detaliiNumarDeplasariBugetateNomenclatorValueId) {
		this.detaliiNumarDeplasariBugetateNomenclatorValueId = detaliiNumarDeplasariBugetateNomenclatorValueId;
	}
	public String getCheltuieliArbTitularDecont() {
		return cheltuieliArbTitularDecont;
	}
	public void setCheltuieliArbTitularDecont(String cheltuieliArbTitularDecont) {
		this.cheltuieliArbTitularDecont = cheltuieliArbTitularDecont;
	}
	public String getCheltuieliArbTipDecont() {
		return cheltuieliArbTipDecont;
	}
	public void setCheltuieliArbTipDecont(String cheltuieliArbTipDecont) {
		this.cheltuieliArbTipDecont = cheltuieliArbTipDecont;
	}
	public Date getCheltuieliArbDataDecont() {
		return cheltuieliArbDataDecont;
	}
	public void setCheltuieliArbDataDecont(Date cheltuieliArbDataDecont) {
		this.cheltuieliArbDataDecont = cheltuieliArbDataDecont;
	}
	public List<CheltuialaArbModel> getCheltuieliArb() {
		return cheltuieliArb;
	}
	public void setCheltuieliArb(List<CheltuialaArbModel> cheltuieliArb) {
		this.cheltuieliArb = cheltuieliArb;
	}
	public BigDecimal getCheltuieliReprezentantArbDiurnaZilnica() {
		return cheltuieliReprezentantArbDiurnaZilnica;
	}
	public void setCheltuieliReprezentantArbDiurnaZilnica(BigDecimal cheltuieliReprezentantArbDiurnaZilnica) {
		this.cheltuieliReprezentantArbDiurnaZilnica = cheltuieliReprezentantArbDiurnaZilnica;
	}
	public String getCheltuieliReprezentantArbDiurnaZilnicaValuta() {
		return cheltuieliReprezentantArbDiurnaZilnicaValuta;
	}
	public void setCheltuieliReprezentantArbDiurnaZilnicaValuta(String cheltuieliReprezentantArbDiurnaZilnicaValuta) {
		this.cheltuieliReprezentantArbDiurnaZilnicaValuta = cheltuieliReprezentantArbDiurnaZilnicaValuta;
	}
	public BigDecimal getCheltuieliReprezentantArbDiurnaZilnicaCursValutar() {
		return cheltuieliReprezentantArbDiurnaZilnicaCursValutar;
	}
	public void setCheltuieliReprezentantArbDiurnaZilnicaCursValutar(
			BigDecimal cheltuieliReprezentantArbDiurnaZilnicaCursValutar) {
		this.cheltuieliReprezentantArbDiurnaZilnicaCursValutar = cheltuieliReprezentantArbDiurnaZilnicaCursValutar;
	}
	public String getCheltuieliReprezentantArbDiurnaZilnicaModalitatePlata() {
		return cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata;
	}
	public void setCheltuieliReprezentantArbDiurnaZilnicaModalitatePlata(
			String cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata) {
		this.cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata = cheltuieliReprezentantArbDiurnaZilnicaModalitatePlata;
	}
	public Integer getCheltuieliReprezentantArbNumarZile() {
		return cheltuieliReprezentantArbNumarZile;
	}
	public void setCheltuieliReprezentantArbNumarZile(Integer cheltuieliReprezentantArbNumarZile) {
		this.cheltuieliReprezentantArbNumarZile = cheltuieliReprezentantArbNumarZile;
	}
	public BigDecimal getCheltuieliReprezentantArbTotalDiurna() {
		return cheltuieliReprezentantArbTotalDiurna;
	}
	public void setCheltuieliReprezentantArbTotalDiurna(BigDecimal cheltuieliReprezentantArbTotalDiurna) {
		this.cheltuieliReprezentantArbTotalDiurna = cheltuieliReprezentantArbTotalDiurna;
	}
	public BigDecimal getCheltuieliReprezentantArbAvansPrimitSuma() {
		return cheltuieliReprezentantArbAvansPrimitSuma;
	}
	public void setCheltuieliReprezentantArbAvansPrimitSuma(BigDecimal cheltuieliReprezentantArbAvansPrimitSuma) {
		this.cheltuieliReprezentantArbAvansPrimitSuma = cheltuieliReprezentantArbAvansPrimitSuma;
	}
	public String getCheltuieliReprezentantArbAvansPrimitSumaValuta() {
		return cheltuieliReprezentantArbAvansPrimitSumaValuta;
	}
	public void setCheltuieliReprezentantArbAvansPrimitSumaValuta(String cheltuieliReprezentantArbAvansPrimitSumaValuta) {
		this.cheltuieliReprezentantArbAvansPrimitSumaValuta = cheltuieliReprezentantArbAvansPrimitSumaValuta;
	}
	public BigDecimal getCheltuieliReprezentantArbAvansPrimitSumaCursValutar() {
		return cheltuieliReprezentantArbAvansPrimitSumaCursValutar;
	}
	public void setCheltuieliReprezentantArbAvansPrimitSumaCursValutar(
			BigDecimal cheltuieliReprezentantArbAvansPrimitSumaCursValutar) {
		this.cheltuieliReprezentantArbAvansPrimitSumaCursValutar = cheltuieliReprezentantArbAvansPrimitSumaCursValutar;
	}
	public String getCheltuieliReprezentantArbAvansPrimitCardSauNumerar() {
		return cheltuieliReprezentantArbAvansPrimitCardSauNumerar;
	}
	public void setCheltuieliReprezentantArbAvansPrimitCardSauNumerar(
			String cheltuieliReprezentantArbAvansPrimitCardSauNumerar) {
		this.cheltuieliReprezentantArbAvansPrimitCardSauNumerar = cheltuieliReprezentantArbAvansPrimitCardSauNumerar;
	}
	public List<CheltuialaReprezentantArbModel> getCheltuieliReprezentantArb() {
		return cheltuieliReprezentantArb;
	}
	public void setCheltuieliReprezentantArb(List<CheltuialaReprezentantArbModel> cheltuieliReprezentantArb) {
		this.cheltuieliReprezentantArb = cheltuieliReprezentantArb;
	}
}
