package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.util.Date;

public class DSPProiectXlsModel {

	private String abreviereProiect;	
	private String numeProiect;
	private String descriere;
	private String domeniuBancar;
	private YesNoValue proiectInitiatARB;
	private DSPArieDeCuprindere arieDeCuprindere;
	private String proiectInitiatDeAltaEntitate;
	private Date dataInceput;
	private Date dataSfarsit;
	private Date dataImplementarii;
	private String evaluareaImpactului;
	private String incadrareProiect;
	private String responsabilProiect;
	private DSPGradDeImportanta gradImportanta;
	private String autoritatiImplicate;
	private String obiectiveProiect;
	private String cadruLegal;
	private String specificitateProiect;
	private DSPStatusProiect statusProiect;
	private Integer estimareRealizareProiect;
	
	public enum DSPStatusProiect {
		
		INITIATED,
		CLOSED
	}
	
	public static enum DSPGradDeImportanta {
		
		STRATEGIC_0("strategic - 0", "strategic", "0"),
		CRITIC_1("critic - 1", "critic", "1"),
		IMPORTANT_2("important - 2", "important", "2"),
		IMPORTANT_3("important - 3", "important", "3"),
		IN_ASTEPTARE_4("in asteptare - 4", "in asteptare", "4"),
		IN_ASTEPTARE_5("in asteptare - 5", "in asteptare", "5");
		
		private final String code;
		
		private final String grad;
		private final String valoare;
		
		private DSPGradDeImportanta(String code, String grad, String valoare) {
			this.code = code;
			this.grad = grad;
			this.valoare = valoare;
		}
		
		public static DSPGradDeImportanta getByCode(String code) {
			StringBuilder posibleCodes = new StringBuilder();
			for (DSPGradDeImportanta s : values()) {
				posibleCodes.append(" [" + s.getCode() + "]");
				if (s.getCode().equals(code)) {
					return s;
				}
			}
			throw new IllegalArgumentException("Codul [" + code + "] este necunoscut. Valorile cunoscute sunt: " + posibleCodes.toString());
		}
		
		public String getCode() {
			return code;
		}
		
		public String getGrad() {
			return grad;
		}
		
		public String getValoare() {
			return valoare;
		}
	}

	public static enum DSPArieDeCuprindere {
		
		INTERN("intern"),
		INTERNATIONAL("international");
		
		private final String code;
		
		private DSPArieDeCuprindere(String code) {
			this.code = code;
		}
		
		public static DSPArieDeCuprindere getByCode(String code) {
			StringBuilder posibleCodes = new StringBuilder();
			for (DSPArieDeCuprindere s : values()) {
				posibleCodes.append(" [" + s.getCode() + "]");
				if (s.getCode().equals(code)) {
					return s;
				}
			}
			throw new IllegalArgumentException("Codul [" + code + "] este necunoscut. Valorile cunoscute sunt: " + posibleCodes.toString());
		}
		
		public String getCode() {
			return code;
		}
	}

	public static enum YesNoValue {
		
		YES("da"),
		NO("nu");
		
		private final String code;
		
		private YesNoValue(String code) {
			this.code = code;
		}
		
		public static YesNoValue getByCode(String code) {
			StringBuilder posibleCodes = new StringBuilder();
			for (YesNoValue s : values()) {
				posibleCodes.append(" [" + s.getCode() + "]");
				if (s.getCode().equals(code)) {
					return s;
				}
			}
			throw new IllegalArgumentException("Codul [" + code + "] este necunoscut. Valorile cunoscute sunt: " + posibleCodes.toString());
		}
		
		public String getCode() {
			return code;
		}
		
		public Boolean getAsBoolean() {
			return YES.equals(this);
		}
	}

	public String getAbreviereProiect() {
		return abreviereProiect;
	}

	public void setAbreviereProiect(String abreviereProiect) {
		this.abreviereProiect = abreviereProiect;
	}

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

	public String getDomeniuBancar() {
		return domeniuBancar;
	}

	public void setDomeniuBancar(String domeniuBancar) {
		this.domeniuBancar = domeniuBancar;
	}

	public YesNoValue getProiectInitiatARB() {
		return proiectInitiatARB;
	}

	public void setProiectInitiatARB(YesNoValue proiectInitiatARB) {
		this.proiectInitiatARB = proiectInitiatARB;
	}

	public DSPArieDeCuprindere getArieDeCuprindere() {
		return arieDeCuprindere;
	}

	public void setArieDeCuprindere(DSPArieDeCuprindere arieDeCuprindere) {
		this.arieDeCuprindere = arieDeCuprindere;
	}

	public String getProiectInitiatDeAltaEntitate() {
		return proiectInitiatDeAltaEntitate;
	}

	public void setProiectInitiatDeAltaEntitate(String proiectInitiatDeAltaEntitate) {
		this.proiectInitiatDeAltaEntitate = proiectInitiatDeAltaEntitate;
	}

	public Date getDataInceput() {
		return dataInceput;
	}

	public void setDataInceput(Date dataInceput) {
		this.dataInceput = dataInceput;
	}

	public Date getDataSfarsit() {
		return dataSfarsit;
	}

	public void setDataSfarsit(Date dataSfarsit) {
		this.dataSfarsit = dataSfarsit;
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

	public String getIncadrareProiect() {
		return incadrareProiect;
	}

	public void setIncadrareProiect(String incadrareProiect) {
		this.incadrareProiect = incadrareProiect;
	}

	public String getResponsabilProiect() {
		return responsabilProiect;
	}

	public void setResponsabilProiect(String responsabilProiect) {
		this.responsabilProiect = responsabilProiect;
	}

	public DSPGradDeImportanta getGradImportanta() {
		return gradImportanta;
	}

	public void setGradImportanta(DSPGradDeImportanta gradImportanta) {
		this.gradImportanta = gradImportanta;
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

	public String getCadruLegal() {
		return cadruLegal;
	}

	public void setCadruLegal(String cadruLegal) {
		this.cadruLegal = cadruLegal;
	}

	public String getSpecificitateProiect() {
		return specificitateProiect;
	}

	public void setSpecificitateProiect(String specificitateProiect) {
		this.specificitateProiect = specificitateProiect;
	}

	public DSPStatusProiect getStatusProiect() {
		return statusProiect;
	}

	public void setStatusProiect(DSPStatusProiect statusProiect) {
		this.statusProiect = statusProiect;
	}

	public Integer getEstimareRealizareProiect() {
		return estimareRealizareProiect;
	}

	public void setEstimareRealizareProiect(Integer estimareRealizareProiect) {
		this.estimareRealizareProiect = estimareRealizareProiect;
	}
}
