package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import ro.cloudSoft.cloudDoc.domain.deplasariDeconturi.DeplasareDecont.ApelativReprezentantArbEnum;

public class DeplasariDeconturiReportRowModel implements Serializable{
	private Integer luna;
	private String apelativ;
	private String reprezentantArbOioro;
	private String functia;
	private String institutie;
	private String numarDecizie;
	private Date dataDecizie;
	private String denumireOrganism;
	private String abreviereOrganism;
	private String comitet;
	private Integer numarDeplasariEfectuate;
	private Integer numarDeplasariBugetateRamase;
	private String eveniment;
	private String tara;
	private String oras;
	private Date dataPlecare;
	private Date dataSosire;
	private Date dataInceputConferinta;
	private Date dataSfarsitConferinta;
	private Integer numarNopti;
	private boolean saTransmisMinutaIntalnitii;
	private String observatii;
	private String titularDecont;
	private String tipDecont;
	private String numarDecont;
	private Date dataDecont;
	private String valuta;
	private BigDecimal cursValutar;
	private String numarDocumentJustificativ;
	private Date dataDocumentJustificativ;
	private BigDecimal cazare;
	private BigDecimal biletDeAvion;
	private BigDecimal asiguareMedicala;
	private BigDecimal transferAeroport;
	private BigDecimal alteCheltuieli;
	private BigDecimal totalCheltuieliPerValuta;
	private BigDecimal totalCheltuieliInRON;
	private String valuta2;
	private BigDecimal cursValutar2;
	private String numarDocumentJustificativ2;
	private Date dataDocumentJustificativ2;
	private BigDecimal cazare2;
	private BigDecimal biletDeAvion2;
	private BigDecimal taxiTrenMetrou;
	private BigDecimal diurnaZilnica;
	private Integer numarZile;
	private BigDecimal totalDiurnaInRON;
	private BigDecimal comisionUtilizareCard;
	private BigDecimal alteCheltuieli2;
	private BigDecimal avans;
	private BigDecimal totalCheltuieliPerValuta2;
	private BigDecimal totalCheltuieliInRON2;
	private BigDecimal totalDeIncasatDeRestituitInRON;
	
	public Integer getLuna() {
		return luna;
	}
	public void setLuna(Integer luna) {
		this.luna = luna;
	}
	public String getApelativ() {
		return apelativ;
	}
	public void setApelativ(ApelativReprezentantArbEnum apelativReprezentantArbEnum) {
		this.apelativ = apelativReprezentantArbEnum.toString();
	}
	public String getReprezentantArbOioro() {
		return reprezentantArbOioro;
	}
	public void setReprezentantArbOioro(String reprezentantArbOioro) {
		this.reprezentantArbOioro = reprezentantArbOioro;
	}
	public String getFunctia() {
		return functia;
	}
	public void setFunctia(String functia) {
		this.functia = functia;
	}
	public String getInstitutie() {
		return institutie;
	}
	public void setInstitutie(String institutie) {
		this.institutie = institutie;
	}
	public String getNumarDecizie() {
		return numarDecizie;
	}
	public void setNumarDecizie(String numarDecizie) {
		this.numarDecizie = numarDecizie;
	}
	public Date getDataDecizie() {
		return dataDecizie;
	}
	public void setDataDecizie(Date dataDecizie) {
		this.dataDecizie = dataDecizie;
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
	public String getComitet() {
		return comitet;
	}
	public void setComitet(String comitet) {
		this.comitet = comitet;
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
	public Date getDataInceputConferinta() {
		return dataInceputConferinta;
	}
	public void setDataInceputConferinta(Date dataInceputConferinta) {
		this.dataInceputConferinta = dataInceputConferinta;
	}
	public Date getDataSfarsitConferinta() {
		return dataSfarsitConferinta;
	}
	public void setDataSfarsitConferinta(Date dataSfarsitConferinta) {
		this.dataSfarsitConferinta = dataSfarsitConferinta;
	}
	public Integer getNumarNopti() {
		return numarNopti;
	}
	public void setNumarNopti(Integer numarNopti) {
		this.numarNopti = numarNopti;
	}
	public boolean isSaTransmisMinutaIntalnitii() {
		return saTransmisMinutaIntalnitii;
	}
	public void setSaTransmisMinutaIntalnitii(boolean saTransmisMinutaIntalnitii) {
		this.saTransmisMinutaIntalnitii = saTransmisMinutaIntalnitii;
	}
	public String getObservatii() {
		return observatii;
	}
	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}
	public String getTitularDecont() {
		return titularDecont;
	}
	public void setTitularDecont(String titularDecont) {
		this.titularDecont = titularDecont;
	}
	public String getTipDecont() {
		return tipDecont;
	}
	public void setTipDecont(String tipDecont) {
		this.tipDecont = tipDecont;
	}
	public String getNumarDecont() {
		return numarDecont;
	}
	public void setNumarDecont(String numarDecont) {
		this.numarDecont = numarDecont;
	}
	public Date getDataDecont() {
		return dataDecont;
	}
	public void setDataDecont(Date dataDecont) {
		this.dataDecont = dataDecont;
	}
	public String getValuta() {
		return valuta;
	}
	public void setValuta(String valuta) {
		this.valuta = valuta;
	}
	public BigDecimal getCursValutar() {
		return cursValutar;
	}
	public void setCursValutar(BigDecimal cursValutar) {
		this.cursValutar = cursValutar;
	}
	public String getNumarDocumentJustificativ() {
		return numarDocumentJustificativ;
	}
	public void setNumarDocumentJustificativ(String numarDocumentJustificativ) {
		this.numarDocumentJustificativ = numarDocumentJustificativ;
	}
	public Date getDataDocumentJustificativ() {
		return dataDocumentJustificativ;
	}
	public void setDataDocumentJustificativ(Date dataDocumentJustificativ) {
		this.dataDocumentJustificativ = dataDocumentJustificativ;
	}
	public BigDecimal getCazare() {
		return cazare;
	}
	public void setCazare(BigDecimal cazare) {
		this.cazare = cazare;
	}
	public BigDecimal getBiletDeAvion() {
		return biletDeAvion;
	}
	public void setBiletDeAvion(BigDecimal biletDeAvion) {
		this.biletDeAvion = biletDeAvion;
	}
	public BigDecimal getAsiguareMedicala() {
		return asiguareMedicala;
	}
	public void setAsiguareMedicala(BigDecimal asiguareMedicala) {
		this.asiguareMedicala = asiguareMedicala;
	}
	public BigDecimal getTransferAeroport() {
		return transferAeroport;
	}
	public void setTransferAeroport(BigDecimal transferAeroport) {
		this.transferAeroport = transferAeroport;
	}
	public BigDecimal getAlteCheltuieli() {
		return alteCheltuieli;
	}
	public void setAlteCheltuieli(BigDecimal alteCheltuieli) {
		this.alteCheltuieli = alteCheltuieli;
	}
	public BigDecimal getTotalCheltuieliPerValuta() {
		return totalCheltuieliPerValuta;
	}
	public void setTotalCheltuieliPerValuta(BigDecimal totalCheltuieliPerValuta) {
		this.totalCheltuieliPerValuta = totalCheltuieliPerValuta;
	}
	public BigDecimal getTotalCheltuieliInRON() {
		return totalCheltuieliInRON;
	}
	public void setTotalCheltuieliInRON(BigDecimal totalCheltuieliInRON) {
		this.totalCheltuieliInRON = totalCheltuieliInRON;
	}
	public String getValuta2() {
		return valuta2;
	}
	public void setValuta2(String valuta2) {
		this.valuta2 = valuta2;
	}
	public BigDecimal getCursValutar2() {
		return cursValutar2;
	}
	public void setCursValutar2(BigDecimal cursValutar2) {
		this.cursValutar2 = cursValutar2;
	}
	public String getNumarDocumentJustificativ2() {
		return numarDocumentJustificativ2;
	}
	public void setNumarDocumentJustificativ2(String numarDocumentJustificativ2) {
		this.numarDocumentJustificativ2 = numarDocumentJustificativ2;
	}
	public Date getDataDocumentJustificativ2() {
		return dataDocumentJustificativ2;
	}
	public void setDataDocumentJustificativ2(Date dataDocumentJustificativ2) {
		this.dataDocumentJustificativ2 = dataDocumentJustificativ2;
	}
	public BigDecimal getCazare2() {
		return cazare2;
	}
	public void setCazare2(BigDecimal cazare2) {
		this.cazare2 = cazare2;
	}
	public BigDecimal getBiletDeAvion2() {
		return biletDeAvion2;
	}
	public void setBiletDeAvion2(BigDecimal biletDeAvion2) {
		this.biletDeAvion2 = biletDeAvion2;
	}
	public BigDecimal getTaxiTrenMetrou() {
		return taxiTrenMetrou;
	}
	public void setTaxiTrenMetrou(BigDecimal taxiTrenMetrou) {
		this.taxiTrenMetrou = taxiTrenMetrou;
	}
	public BigDecimal getDiurnaZilnica() {
		return diurnaZilnica;
	}
	public void setDiurnaZilnica(BigDecimal diurnaZilnica) {
		this.diurnaZilnica = diurnaZilnica;
	}
	public Integer getNumarZile() {
		return numarZile;
	}
	public void setNumarZile(Integer numarZile) {
		this.numarZile = numarZile;
	}
	public BigDecimal getTotalDiurnaInRON() {
		return totalDiurnaInRON;
	}
	public void setTotalDiurnaInRON(BigDecimal totalDiurnaInRON) {
		this.totalDiurnaInRON = totalDiurnaInRON;
	}
	public BigDecimal getComisionUtilizareCard() {
		return comisionUtilizareCard;
	}
	public void setComisionUtilizareCard(BigDecimal comisionUtilizareCard) {
		this.comisionUtilizareCard = comisionUtilizareCard;
	}
	public BigDecimal getAlteCheltuieli2() {
		return alteCheltuieli2;
	}
	public void setAlteCheltuieli2(BigDecimal alteCheltuieli2) {
		this.alteCheltuieli2 = alteCheltuieli2;
	}
	public BigDecimal getAvans() {
		return avans;
	}
	public void setAvans(BigDecimal avans) {
		this.avans = avans;
	}
	public BigDecimal getTotalCheltuieliPerValuta2() {
		return totalCheltuieliPerValuta2;
	}
	public void setTotalCheltuieliPerValuta2(BigDecimal totalCheltuieliPerValuta2) {
		this.totalCheltuieliPerValuta2 = totalCheltuieliPerValuta2;
	}
	public BigDecimal getTotalCheltuieliInRON2() {
		return totalCheltuieliInRON2;
	}
	public void setTotalCheltuieliInRON2(BigDecimal totalCheltuieliInRON2) {
		this.totalCheltuieliInRON2 = totalCheltuieliInRON2;
	}
	public BigDecimal getTotalDeIncasatDeRestituitInRON() {
		return totalDeIncasatDeRestituitInRON;
	}
	public void setTotalDeIncasatDeRestituitInRON(BigDecimal totalDeIncasatDeRestituitInRON) {
		this.totalDeIncasatDeRestituitInRON = totalDeIncasatDeRestituitInRON;
	}

	
}
