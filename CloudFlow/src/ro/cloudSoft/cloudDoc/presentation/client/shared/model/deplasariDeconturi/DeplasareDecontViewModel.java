package ro.cloudSoft.cloudDoc.presentation.client.shared.model.deplasariDeconturi;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class DeplasareDecontViewModel {

	private Long id;
	private String month;
	private String numarInregistrare;
	private String apelativ;
	private String denumireReprezentantArb;
	private String functie;
	private String numarDecizie;
	private String documentLocationRealName;
	private String documentId;
	private String denumireInstitutie;
	private Date dataDecizie;
	private String denumireOrganism;
	private String abreviereOrganism;
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
	private BigDecimal totalCheltuieliArbValutaEur;
	private BigDecimal totalCheltuieliArbValutaUsd;
	private BigDecimal totalCheltuieliArbValutaRon;
	private BigDecimal totalCheltuieliArbRon;
	
	private List<CheltuialaReprezentantArbModel> cheltuieliReprezentantArb;
	private BigDecimal totalCheltuieliReprezentantArbValutaEur;
	private BigDecimal totalCheltuieliReprezentantArbValutaUsd;
	private BigDecimal totalCheltuieliReprezentantArbValutaRon;
	private BigDecimal totalCheltuieliReprezentantArbRon;

	private BigDecimal avansPrimitRon;
	private BigDecimal totalDiurnaRon;
	private BigDecimal totalDeIncasat;
	
	private boolean anulat;
	private String motivAnulare;
	private boolean finalizat;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
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
	public String getDenumireReprezentantArb() {
		return denumireReprezentantArb;
	}
	public void setDenumireReprezentantArb(String denumireReprezentantArb) {
		this.denumireReprezentantArb = denumireReprezentantArb;
	}
	public String getFunctie() {
		return functie;
	}
	public void setFunctie(String functie) {
		this.functie = functie;
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
	public BigDecimal getTotalCheltuieliArbValutaEur() {
		return totalCheltuieliArbValutaEur;
	}
	public void setTotalCheltuieliArbValutaEur(BigDecimal totalCheltuieliArbValutaEur) {
		this.totalCheltuieliArbValutaEur = totalCheltuieliArbValutaEur;
	}
	public BigDecimal getTotalCheltuieliArbValutaUsd() {
		return totalCheltuieliArbValutaUsd;
	}
	public void setTotalCheltuieliArbValutaUsd(BigDecimal totalCheltuieliArbValutaUsd) {
		this.totalCheltuieliArbValutaUsd = totalCheltuieliArbValutaUsd;
	}
	public BigDecimal getTotalCheltuieliArbValutaRon() {
		return totalCheltuieliArbValutaRon;
	}
	public void setTotalCheltuieliArbValutaRon(BigDecimal totalCheltuieliArbValutaRon) {
		this.totalCheltuieliArbValutaRon = totalCheltuieliArbValutaRon;
	}
	public BigDecimal getTotalCheltuieliArbRon() {
		return totalCheltuieliArbRon;
	}
	public void setTotalCheltuieliArbRon(BigDecimal totalCheltuieliArbRon) {
		this.totalCheltuieliArbRon = totalCheltuieliArbRon;
	}
	public List<CheltuialaReprezentantArbModel> getCheltuieliReprezentantArb() {
		return cheltuieliReprezentantArb;
	}
	public void setCheltuieliReprezentantArb(List<CheltuialaReprezentantArbModel> cheltuieliReprezentantArb) {
		this.cheltuieliReprezentantArb = cheltuieliReprezentantArb;
	}
	public BigDecimal getTotalCheltuieliReprezentantArbValutaEur() {
		return totalCheltuieliReprezentantArbValutaEur;
	}
	public void setTotalCheltuieliReprezentantArbValutaEur(BigDecimal totalCheltuieliReprezentantArbValutaEur) {
		this.totalCheltuieliReprezentantArbValutaEur = totalCheltuieliReprezentantArbValutaEur;
	}
	public BigDecimal getTotalCheltuieliReprezentantArbValutaUsd() {
		return totalCheltuieliReprezentantArbValutaUsd;
	}
	public void setTotalCheltuieliReprezentantArbValutaUsd(BigDecimal totalCheltuieliReprezentantArbValutaUsd) {
		this.totalCheltuieliReprezentantArbValutaUsd = totalCheltuieliReprezentantArbValutaUsd;
	}
	public BigDecimal getTotalCheltuieliReprezentantArbValutaRon() {
		return totalCheltuieliReprezentantArbValutaRon;
	}
	public void setTotalCheltuieliReprezentantArbValutaRon(BigDecimal totalCheltuieliReprezentantArbValutaRon) {
		this.totalCheltuieliReprezentantArbValutaRon = totalCheltuieliReprezentantArbValutaRon;
	}
	public BigDecimal getTotalCheltuieliReprezentantArbRon() {
		return totalCheltuieliReprezentantArbRon;
	}
	public void setTotalCheltuieliReprezentantArbRon(BigDecimal totalCheltuieliReprezentantArbRon) {
		this.totalCheltuieliReprezentantArbRon = totalCheltuieliReprezentantArbRon;
	}
	public BigDecimal getAvansPrimitRon() {
		return avansPrimitRon;
	}
	public void setAvansPrimitRon(BigDecimal avansPrimitRon) {
		this.avansPrimitRon = avansPrimitRon;
	}
	public BigDecimal getTotalDeIncasat() {
		return totalDeIncasat;
	}
	public void setTotalDeIncasat(BigDecimal totalDeIncasat) {
		this.totalDeIncasat = totalDeIncasat;
	}
	public boolean isAnulat() {
		return anulat;
	}
	public void setAnulat(boolean anulat) {
		this.anulat = anulat;
	}
	public String getMotivAnulare() {
		return motivAnulare;
	}
	public void setMotivAnulare(String motivAnulare) {
		this.motivAnulare = motivAnulare;
	}
	public boolean isFinalizat() {
		return finalizat;
	}
	public void setFinalizat(boolean finalizat) {
		this.finalizat = finalizat;
	}
	public BigDecimal getTotalDiurnaRon() {
		return totalDiurnaRon;
	}
	public void setTotalDiurnaRon(BigDecimal totalDiurnaRon) {
		this.totalDiurnaRon = totalDiurnaRon;
	}
	
}
