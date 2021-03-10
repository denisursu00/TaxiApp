package ro.cloudSoft.cloudDoc.domain.content;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.google.common.collect.Lists;

@Entity
public class MetadataCollection implements Serializable 
{
	
	private static final long serialVersionUID = 1L;
	
	private static final int LENGTH_STEPS = 500;
	
	private Long id;
	private String name;
	private String jrPropertyName;
	
	private boolean mandatory;
	private String mandatoryStates;
	
	private boolean restrictedOnEdit;
	private String restrictedOnEditStates;
	private int orderNumber;
	
	private boolean invisible;
	private String invisibleInStates;
	
	private String metadataNameForAutoCompleteWithMetadata;
	private TypeOfAutoCompleteWithMetadata typeOfAutoCompleteWithMetadata;
	private String nomenclatorAttributeKeyForAutoCompleteWithMetadata;
	private String classNameForAutoCompleteWithMetadata;
	
	private List<? extends MetadataDefinition> metadataDefinitions = Lists.newLinkedList();
	
	private List<DocumentValidationDefinition> validationDefinitions = Lists.newLinkedList();
	
	public MetadataCollection() 
	{
		super();
	}

	
	@Id    
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) 
	{	
		this.id = id;
	}   
	
	
	private String label;
	
	
	/**
	 * @param metadataDefinitions the metadataDefinitions to set
	 */
	public void setMetadataDefinitions(List<? extends MetadataDefinition> metadataDefinitions) 
	{
		this.metadataDefinitions = metadataDefinitions;
	}

	/**
	 * @return the metadataDefinitions
	 */
	@OrderBy("orderNumber")
	@OneToMany(targetEntity=ro.cloudSoft.cloudDoc.domain.content.MetadataDefinition.class, cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
    @JoinColumn(name="collection_id")
	public List<? extends MetadataDefinition> getMetadataDefinitions() 
	{
		return metadataDefinitions;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	
	@Column(name = "JR_PROPERTY_NAME")
	public String getJrPropertyName() {
		return jrPropertyName;
	}
	
	public void setJrPropertyName(String jrPropertyName) {
		this.jrPropertyName = jrPropertyName;
	}
	
	@Column(name = "mandatory", nullable = false)
	public boolean isMandatory() {
		return mandatory;
	}
	
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	
	@Column(name = "mandatory_steps", nullable = true, length = LENGTH_STEPS)
	public String getMandatoryStates() {
		return mandatoryStates;
	}
	
	public void setMandatoryStates(String mandatoryStates) {
		this.mandatoryStates = mandatoryStates;
	}
	
	@Column(name = "restricted_on_edit", nullable = false)
	public boolean isRestrictedOnEdit() {
		return this.restrictedOnEdit;
	}
	
	public void setRestrictedOnEdit(boolean restrictedOnEdit) {
		this.restrictedOnEdit = restrictedOnEdit;
	}
	
	@Column(name = "restricted_on_edit_steps", nullable = true, length = LENGTH_STEPS)
	public String getRestrictedOnEditStates() {
		return restrictedOnEditStates;
	}
	
	public void setRestrictedOnEditStates(String restrictedOnEditStates) {
		this.restrictedOnEditStates = restrictedOnEditStates;
	}
	
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}
	
	@Column(name = "INVISIBLE")
	public boolean isInvisible() {
		return invisible;
	}
	
	public void setInvisibleInStates(String invisibleInStates) {
		this.invisibleInStates = invisibleInStates;
	}
	
	@Column(name = "INVISIBLE_IN_STATES")
	public String getInvisibleInStates() {
		return invisibleInStates;
	}
	
	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getLabel()
	{
		return label;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	@OrderBy("validationOrder")
	@OneToMany(cascade = CascadeType.ALL, mappedBy="metadataCollection")
	public List<DocumentValidationDefinition> getValidationDefinitions() {
		return validationDefinitions;
	}
	
	public void setValidationDefinitions(List<DocumentValidationDefinition> validationDefinitions) {
		this.validationDefinitions = validationDefinitions;
	}
	
	@Column(name = "md_name_for_auto_cmpl_wth_md")
	public String getMetadataNameForAutoCompleteWithMetadata() {
		return metadataNameForAutoCompleteWithMetadata;
	}

	public void setMetadataNameForAutoCompleteWithMetadata(String metadataNameForAutoCompleteWithMetadata) {
		this.metadataNameForAutoCompleteWithMetadata = metadataNameForAutoCompleteWithMetadata;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type_of_auto_cmpl_wth_md")
	public TypeOfAutoCompleteWithMetadata getTypeOfAutoCompleteWithMetadata() {
		return typeOfAutoCompleteWithMetadata;
	}

	public void setTypeOfAutoCompleteWithMetadata(TypeOfAutoCompleteWithMetadata typeOfAutoCompleteWithMetadata) {
		this.typeOfAutoCompleteWithMetadata = typeOfAutoCompleteWithMetadata;
	}
	
	@Column(name = "nom_atr_ky_for_aut_cmpl_wth_md")
	public String getNomenclatorAttributeKeyForAutoCompleteWithMetadata() {
		return nomenclatorAttributeKeyForAutoCompleteWithMetadata;
	}

	public void setNomenclatorAttributeKeyForAutoCompleteWithMetadata(
			String nomenclatorAttributeKeyForAutoCompleteWithMetadata) {
		this.nomenclatorAttributeKeyForAutoCompleteWithMetadata = nomenclatorAttributeKeyForAutoCompleteWithMetadata;
	}
	
	@Column(name = "class_name_for_aut_cmpl_wth_md")
	public String getClassNameForAutoCompleteWithMetadata() {
		return classNameForAutoCompleteWithMetadata;
	}
	
	public void setClassNameForAutoCompleteWithMetadata(String classNameForAutoCompleteWithMetadata) {
		this.classNameForAutoCompleteWithMetadata = classNameForAutoCompleteWithMetadata;
	}
}