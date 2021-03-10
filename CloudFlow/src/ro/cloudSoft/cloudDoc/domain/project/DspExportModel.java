package ro.cloudSoft.cloudDoc.domain.project;

import java.util.Date;
import java.util.List;

public class DspExportModel {
	
	private String numeProiect;
	private String descriere;
	private String initiatorProiect;
	private String incadrareProiect;
	private String arieDeCuprindere;
	private String domeniuBancar;
	private Date dataInceputProiect;
	private Date dataSfarsitProiect;
	private Date dataImplementarii;
	private String gradImportanta;
	private Integer gradRealizareEstimat;
	private String responsabil;
	private String cadrulLegal;
	private String specificitateProiect;
	private String obiectiveProiect;
	private String evaluareaImpactului;
	private String abreviereProiect;
	private String autoritatiImplicate;
	
	private List<DspExportComisieGLModel> comisiiGLImplicate;
	private List<DspExportGrupActivitateModel> actiuniIntreprinse;
	private List<DspExportGrupActivitateModel> actiuniViitoare;
	
	public String getNumeProiect() {
		return numeProiect;
	}
	public void setNumeProiect(String numeProiect) {
		this.numeProiect = numeProiect;
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
	public String getDomeniuBancar() {
		return domeniuBancar;
	}
	public void setDomeniuBancar(String domeniuBancar) {
		this.domeniuBancar = domeniuBancar;
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
	public String getGradImportanta() {
		return gradImportanta;
	}
	public void setGradImportanta(String gradImportanta) {
		this.gradImportanta = gradImportanta;
	}
	public Integer getGradRealizareEstimat() {
		return gradRealizareEstimat;
	}
	public void setGradRealizareEstimat(Integer gradRealizareEstimat) {
		this.gradRealizareEstimat = gradRealizareEstimat;
	}
	public String getResponsabil() {
		return responsabil;
	}
	public void setResponsabil(String responsabil) {
		this.responsabil = responsabil;
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
	public String getObiectiveProiect() {
		return obiectiveProiect;
	}
	public void setObiectiveProiect(String obiectiveProiect) {
		this.obiectiveProiect = obiectiveProiect;
	}
	public String getEvaluareaImpactului() {
		return evaluareaImpactului;
	}
	public void setEvaluareaImpactului(String evaluareaImpactului) {
		this.evaluareaImpactului = evaluareaImpactului;
	}
	public String getAbreviereProiect() {
		return abreviereProiect;
	}
	public void setAbreviereProiect(String abreviereProiect) {
		this.abreviereProiect = abreviereProiect;
	}
	public String getAutoritatiImplicate() {
		return autoritatiImplicate;
	}
	public void setAutoritatiImplicate(String autoritatiImplicate) {
		this.autoritatiImplicate = autoritatiImplicate;
	}
	public List<DspExportComisieGLModel> getComisiiGLImplicate() {
		return comisiiGLImplicate;
	}
	public void setComisiiGLImplicate(List<DspExportComisieGLModel> comisiiGLImplicate) {
		this.comisiiGLImplicate = comisiiGLImplicate;
	}
	public List<DspExportGrupActivitateModel> getActiuniIntreprinse() {
		return actiuniIntreprinse;
	}
	public void setActiuniIntreprinse(List<DspExportGrupActivitateModel> actiuniIntreprinse) {
		this.actiuniIntreprinse = actiuniIntreprinse;
	}
	public List<DspExportGrupActivitateModel> getActiuniViitoare() {
		return actiuniViitoare;
	}
	public void setActiuniViitoare(List<DspExportGrupActivitateModel> actiuniViitoare) {
		this.actiuniViitoare = actiuniViitoare;
	}
}
