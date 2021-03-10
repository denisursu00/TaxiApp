package ro.cloudSoft.cloudDoc.domain.project;

import java.util.Date;
import java.util.List;

public class DspViewModel {
	
	private Long projectId;
	
	private String numeProiect;
	private String abreviereProiect;
	private String descriere;
	private String initiatorProiect;
	private String domeniuBancar;
	private String proiectInitiatDeArb;
	private String incadrareProiect;
	private String arieDeCuprindere;
	private String proiectInitiatDeAltaEntitate;
	private Date dataInceputProiect;
	private Date dataSfarsitProiect;
	private Date dataImplementarii;
	private String evaluareaImpactului;
	private String gradDeImportanta;
	private Integer gradDeRealizareEstimatDeResponsabil;
	private String responsabilProiect;
	private String autoritatiImplicate;
	private String obiectiveProiect;
	private String cadrulLegal;
	private String specificitateProiect;
	
	private List<DspParticipantViewModel> participanti;
	private List<DspComisieGlViewModel> comisiiGlImplicate;
	private List<DspActivitateViewModel> activitati;
	
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public String getNumeProiect() {
		return numeProiect;
	}
	public void setNumeProiect(String numeProiect) {
		this.numeProiect = numeProiect;
	}
	public String getAbreviereProiect() {
		return abreviereProiect;
	}
	public void setAbreviereProiect(String abreviereProiect) {
		this.abreviereProiect = abreviereProiect;
	}
	public String getDescriere() {
		return descriere;
	}
	public void setDescriere(String descriere) {
		this.descriere = descriere;
	}
	public String getInitiatorProiect() {
		return initiatorProiect;
	}
	public void setInitiatorProiect(String initiatorProiect) {
		this.initiatorProiect = initiatorProiect;
	}
	public String getDomeniuBancar() {
		return domeniuBancar;
	}
	public void setDomeniuBancar(String domeniuBancar) {
		this.domeniuBancar = domeniuBancar;
	}
	public String getProiectInitiatDeArb() {
		return proiectInitiatDeArb;
	}
	public void setProiectInitiatDeArb(String proiectInitiatDeArb) {
		this.proiectInitiatDeArb = proiectInitiatDeArb;
	}
	public String getIncadrareProiect() {
		return incadrareProiect;
	}
	public void setIncadrareProiect(String incadrareProiect) {
		this.incadrareProiect = incadrareProiect;
	}
	public String getArieDeCuprindere() {
		return arieDeCuprindere;
	}
	public void setArieDeCuprindere(String arieDeCuprindere) {
		this.arieDeCuprindere = arieDeCuprindere;
	}
	public String getProiectInitiatDeAltaEntitate() {
		return proiectInitiatDeAltaEntitate;
	}
	public void setProiectInitiatDeAltaEntitate(String proiectInitiatDeAltaEntitate) {
		this.proiectInitiatDeAltaEntitate = proiectInitiatDeAltaEntitate;
	}
	public Date getDataInceputProiect() {
		return dataInceputProiect;
	}
	public void setDataInceputProiect(Date dataInceputProiect) {
		this.dataInceputProiect = dataInceputProiect;
	}
	public Date getDataSfarsitProiect() {
		return dataSfarsitProiect;
	}
	public void setDataSfarsitProiect(Date dataSfarsitProiect) {
		this.dataSfarsitProiect = dataSfarsitProiect;
	}
	public Date getDataImplementarii() {
		return dataImplementarii;
	}
	public void setDataImplementarii(Date dataImplementarii) {
		this.dataImplementarii = dataImplementarii;
	}
	public String getEvaluareaImpactului() {
		return evaluareaImpactului;
	}
	public void setEvaluareaImpactului(String evaluareaImpactului) {
		this.evaluareaImpactului = evaluareaImpactului;
	}
	public String getGradDeImportanta() {
		return gradDeImportanta;
	}
	public void setGradDeImportanta(String gradDeImportanta) {
		this.gradDeImportanta = gradDeImportanta;
	}
	public Integer getGradDeRealizareEstimatDeResponsabil() {
		return gradDeRealizareEstimatDeResponsabil;
	}
	public void setGradDeRealizareEstimatDeResponsabil(Integer gradDeRealizareEstimatDeResponsabil) {
		this.gradDeRealizareEstimatDeResponsabil = gradDeRealizareEstimatDeResponsabil;
	}
	public String getResponsabilProiect() {
		return responsabilProiect;
	}
	public void setResponsabilProiect(String responsabilProiect) {
		this.responsabilProiect = responsabilProiect;
	}
	public String getAutoritatiImplicate() {
		return autoritatiImplicate;
	}
	public void setAutoritatiImplicate(String autoritatiImplicate) {
		this.autoritatiImplicate = autoritatiImplicate;
	}
	public String getObiectiveProiect() {
		return obiectiveProiect;
	}
	public void setObiectiveProiect(String obiectiveProiect) {
		this.obiectiveProiect = obiectiveProiect;
	}
	public String getCadrulLegal() {
		return cadrulLegal;
	}
	public void setCadrulLegal(String cadrulLegal) {
		this.cadrulLegal = cadrulLegal;
	}
	public String getSpecificitateProiect() {
		return specificitateProiect;
	}
	public void setSpecificitateProiect(String specificitateProiect) {
		this.specificitateProiect = specificitateProiect;
	}
	public List<DspParticipantViewModel> getParticipanti() {
		return participanti;
	}
	public void setParticipanti(List<DspParticipantViewModel> participanti) {
		this.participanti = participanti;
	}
	public List<DspComisieGlViewModel> getComisiiGlImplicate() {
		return comisiiGlImplicate;
	}
	public void setComisiiGlImplicate(List<DspComisieGlViewModel> comisiiGlImplicate) {
		this.comisiiGlImplicate = comisiiGlImplicate;
	}
	public List<DspActivitateViewModel> getActivitati() {
		return activitati;
	}
	public void setActivitati(List<DspActivitateViewModel> activitati) {
		this.activitati = activitati;
	}	
}
