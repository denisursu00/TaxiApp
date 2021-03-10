package ro.cloudSoft.cloudDoc.domain.project;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;
import ro.cloudSoft.cloudDoc.domain.organization.User;

@Entity
@Table(
	name="PROJECT",
	uniqueConstraints = @UniqueConstraint(columnNames= { "name" })
)
public class Project {
	
	private Long id;
	private String name;
	private String description;
	private User initiator;
	private User responsibleUser;
	private String projectAbbreviation;
	private String documentId;
	private String documentLocationRealName;
	private List<OrganizationEntity> participants;
	private List<ProjectEstimation> estimations;
	private Date startDate;
	private Date endDate;
	private ProjectStatus status;
	private ProjectType type;
	private List<NomenclatorValue> comisiiSauGl;
	private Date implementationDate;
	private List<Task> tasks;
	private NomenclatorValue gradImportanta;
	//new fields from initiere dsp migration
	private String numarProiect;
	private NomenclatorValue domeniuBancar;
	private Boolean proiectInitiatArb; 
	private ArieDeCuprindereEnum arieDeCuprindere;	
	private String proiectInitiatDeAltaEntitate;
	private String evaluareaImpactului;
	private NomenclatorValue incadrareProiect;
	private String autoritatiImplicate;
	private String obiectiveProiect;
	private String cadruLegal;
	private String specificitateProiect;
	private List<Subactivity> subactivities;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "initiator_id", referencedColumnName = "org_entity_id", nullable = false)
	public User getInitiator() {
		return initiator;
	}
	
	public void setInitiator(User initiator) {
		this.initiator = initiator;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_responsible_id", referencedColumnName = "org_entity_id", nullable = false)
	public User getResponsibleUser() {
		return responsibleUser;
	}
	
	public void setResponsibleUser(User responsibleUser) {
		this.responsibleUser = responsibleUser;
	}

	@Column(name = "project_abbreviation")
	public String getProjectAbbreviation() {
		return projectAbbreviation;
	}
	
	public void setProjectAbbreviation(String projectAbbreviation) {
		this.projectAbbreviation = projectAbbreviation;
	}
	
	@Column(name = "jr_document_id")
	public String getDocumentId() {
		return documentId;
	}
	
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	@Column(name = "jr_document_location_real_name")
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	
	public void setDocumentLocationRealName(String documentLocationRealName) {
		this.documentLocationRealName = documentLocationRealName;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "project_participants_oe",
		joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false),
		inverseJoinColumns = @JoinColumn(name = "participant_org_entity_id", referencedColumnName = "org_entity_id", nullable = false)
	)
	public List<OrganizationEntity> getParticipants() {
		return participants;
	}

	public void setParticipants(List<OrganizationEntity> participants) {
		this.participants = participants;
	}
	
	@OneToMany(cascade= CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "project")
	public List<ProjectEstimation> getEstimations() {
		return estimations;
	}

	public void setEstimations(List<ProjectEstimation> estimations) {
		this.estimations = estimations;
	}

	@Column(name = "start_date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "end_date")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	public ProjectType getType() {
		return type;
	}

	public void setType(ProjectType type) {
		this.type = type;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "project_comisii_sau_gl",
		joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false),
		inverseJoinColumns = @JoinColumn(name = "comisie_sau_gl_id", referencedColumnName = "id", nullable = false)
	)
	public List<NomenclatorValue> getComisiiSauGl() {
		return comisiiSauGl;
	}

	public void setComisiiSauGl(List<NomenclatorValue> comisiiSauGl) {
		this.comisiiSauGl = comisiiSauGl;
	}
	
	@Column(name = "implementation_date")
	public Date getImplementationDate() {
		return implementationDate;
	}

	public void setImplementationDate(Date implementationDate) {
		this.implementationDate = implementationDate;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name ="grad_importanta_nom_val_id")
	public NomenclatorValue getGradImportanta() {
		return gradImportanta;
	}

	public void setGradImportanta(NomenclatorValue gradImportanta) {
		this.gradImportanta = gradImportanta;
	}

	@Column(name = "numar_proiect")
	public String getNumarProiect() {
		return numarProiect;
	}
	
	public void setNumarProiect(String numarProiect) {
		this.numarProiect = numarProiect;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "domeniu_bancar_nom_val_id")
	public NomenclatorValue getDomeniuBancar() {
		return domeniuBancar;
	}

	public void setDomeniuBancar(NomenclatorValue domeniuBancar) {
		this.domeniuBancar = domeniuBancar;
	}

	@Column(name = "proiect_initiat_arb")
	public Boolean getProiectInitiatArb() {
		return proiectInitiatArb;
	}

	public void setProiectInitiatArb(Boolean proiectInitiatArb) {
		this.proiectInitiatArb = proiectInitiatArb;
	}

	@Column(name = "arie_de_cuprindere")
	@Enumerated(value = EnumType.STRING)
	public ArieDeCuprindereEnum getArieDeCuprindere() {
		return arieDeCuprindere;
	}

	public void setArieDeCuprindere(ArieDeCuprindereEnum arieDeCuprindere) {
		this.arieDeCuprindere = arieDeCuprindere;
	}

	@Column(name = "proiect_initiat_alta_entitate")
	public String getProiectInitiatDeAltaEntitate() {
		return proiectInitiatDeAltaEntitate;
	}

	public void setProiectInitiatDeAltaEntitate(String proiectInitiatDeAltaEntitate) {
		this.proiectInitiatDeAltaEntitate = proiectInitiatDeAltaEntitate;
	}

	@Column(name = "evaluarea_impactului")
	public String getEvaluareaImpactului() {
		return evaluareaImpactului;
	}

	public void setEvaluareaImpactului(String evaluareImpactului) {
		this.evaluareaImpactului = evaluareImpactului;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "incadrare_proiect_nom_val_id")
	public NomenclatorValue getIncadrareProiect() {
		return incadrareProiect;
	}

	public void setIncadrareProiect(NomenclatorValue incadrareProiect) {
		this.incadrareProiect = incadrareProiect;
	}

	@Column(name = "autoritati_implicate")
	public String getAutoritatiImplicate() {
		return autoritatiImplicate;
	}

	public void setAutoritatiImplicate(String autoritatiImplicate) {
		this.autoritatiImplicate = autoritatiImplicate;
	}

	@Column(name = "obiective_proiect")
	public String getObiectiveProiect() {
		return obiectiveProiect;
	}

	public void setObiectiveProiect(String obiectiveProiect) {
		this.obiectiveProiect = obiectiveProiect;
	}

	@Column(name = "cadrul_legal")
	public String getCadruLegal() {
		return cadruLegal;
	}

	public void setCadruLegal(String cadruLegal) {
		this.cadruLegal = cadruLegal;
	}

	@Column(name = "specificitate_proiect")
	public String getSpecificitateProiect() {
		return specificitateProiect;
	}

	public void setSpecificitateProiect(String specificitateProiect) {
		this.specificitateProiect = specificitateProiect;
	}

	public static enum ArieDeCuprindereEnum {
		INTERN("intern"),
		INTERNATIONAL("international");
		
		private final String value;
		private ArieDeCuprindereEnum(String value) {
			this.value = value;
		}
		public String  getValue() {
			return this.value;
		}
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	@OneToMany(mappedBy = "project")
	@Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public List<Subactivity> getSubactivities() {
		return subactivities;
	}

	public void setSubactivities(List<Subactivity> subactivities) {
		this.subactivities = subactivities;
	}
	
}

