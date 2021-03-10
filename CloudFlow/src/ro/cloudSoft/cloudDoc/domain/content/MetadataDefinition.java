package ro.cloudSoft.cloudDoc.domain.content;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * Entity implementation class for Entity: MetadataDefinition
 *
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
name = "basemetadatatype",
discriminatorType = DiscriminatorType.STRING
)
public abstract class MetadataDefinition {
	
	public static final String DISCRIMINATOR_COLUMN_NAME = MetadataDefinition.class.getAnnotation(DiscriminatorColumn.class).name();
	
	public static final int LENGTH_NAME = 128;
	public static final int LENGTH_LABEL = 128;
	
	public static final String TYPE_TEXT = "TEXT";
	public static final String TYPE_NUMERIC = "NUMERIC";
	public static final String TYPE_AUTO_NUMBER = "AUTO_NUMBER";
	public static final String TYPE_DATE = "DATE";
	public static final String TYPE_DATE_TIME = "DATE_TIME";
	public static final String TYPE_MONTH= "MONTH";
	public static final String TYPE_LIST = "LIST";
	public static final String TYPE_USER = "USER";
	public static final String TYPE_TEXT_AREA = "TEXT_AREA";
	public static final String TYPE_METADATA_COLLECTION = "METADATA_COLLECTION";
	public static final String TYPE_NOMENCLATOR = "NOMENCLATOR";
	public static final String TYPE_GROUP = "GROUP";
	public static final String TYPE_DOCUMENT = "DOCUMENT";
	public static final String TYPE_CALENDAR = "CALENDAR";
	public static final String TYPE_PROJECT = "PROJECT";
	
	public static final List<String> TYPES = Arrays.asList(new String[] {
		TYPE_TEXT,
		TYPE_NUMERIC,
		TYPE_AUTO_NUMBER,
		TYPE_DATE,
		TYPE_DATE_TIME,
		TYPE_MONTH,
		TYPE_LIST,
		TYPE_USER,
		TYPE_TEXT_AREA,
		TYPE_METADATA_COLLECTION,
		TYPE_NOMENCLATOR,
		TYPE_DOCUMENT,
		TYPE_CALENDAR,
		TYPE_PROJECT
	});
	
	private Long id;
	private String name;
	private String jrPropertyName;
	private String label;
	private boolean mandatory;
	private boolean restrictedOnEdit;
	private boolean indexed;
	private boolean representative;
	private int orderNumber;
	
	private String mandatoryStates;
	private String restrictedOnEditStates;
	

	private boolean invisible;
	private String invisibleInStates;
	
	/**
	 * Tipul de metadata: numeric, data, samd
	 */
	private String metadataType;
	private MetadataCollection collection;
	
	private String defaultValue;

	private String metadataNameForAutoCompleteWithMetadata;
	private TypeOfAutoCompleteWithMetadata typeOfAutoCompleteWithMetadata;
	private String nomenclatorAttributeKeyForAutoCompleteWithMetadata;
	private String classNameForAutoCompleteWithMetadata;
	
	public MetadataDefinition() 
	{
		super();
	}  
	
	@Transient
	public String getDiscriminatorColumnValue() {
		Class<?> concreteMetadataDefinitionClass = getClass();
		try {
			return concreteMetadataDefinitionClass.getAnnotation(DiscriminatorValue.class).value();
		} catch (NullPointerException npe) {
			String exceptionMessage = "Clasa pentru definitie de metadata [" + concreteMetadataDefinitionClass.getName() + "] " +
				"NU are valoarea coloanei pentru discriminare setata.";
			throw new IllegalStateException(exceptionMessage, npe);
		}
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
	
	@Column(length = LENGTH_NAME)
	public String getName() 
	{
		return this.name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}   
	
	@Column(name = "JR_PROPERTY_NAME")
	public String getJrPropertyName() {
		return jrPropertyName;
	}
	
	public void setJrPropertyName(String jrPropertyName) {
		this.jrPropertyName = jrPropertyName;
	}
	
	@Column(length = LENGTH_LABEL)
	public String getLabel() 
	{
		return this.label;
	}

	public void setLabel(String label) 
	{
		this.label = label;
	}   
	
	public boolean getMandatory() 
	{
		return this.mandatory;
	}

	public void setMandatory(boolean mandatory) 
	{
		this.mandatory = mandatory;
	}
	public boolean isRestrictedOnEdit() {
		return this.restrictedOnEdit;
	}
	public void setRestrictedOnEdit(boolean restrictedOnEdit) {
		this.restrictedOnEdit = restrictedOnEdit;
	}
	
	public boolean getIndexed() 
	{
		return this.indexed;
	}

	public void setIndexed(boolean indexed) 
	{
		this.indexed = indexed;
	}   
	
	public boolean getRepresentative() 
	{
		return this.representative;
	}

	public void setRepresentative(boolean representative) 
	{
		this.representative = representative;
	}   
	
	public int getOrderNumber() 
	{
		return this.orderNumber;
	}

	public void setOrderNumber(int orderNumber) 
	{
		this.orderNumber = orderNumber;
	}
	/**
	 * @param metadataType the metadataType to set
	 */
	public void setMetadataType(String metadataType) 
	{
		this.metadataType = metadataType;
	}
	/**
	 * @return the metadataType
	 */
	
	
	public String getMetadataType() 
	{
		return metadataType;
	}

	/**
	 * @param mandatoryStates the mandatoryStates to set
	 */
	public void setMandatoryStates(String mandatoryStates)
	{
		this.mandatoryStates = mandatoryStates;
	}

	/**
	 * @return the mandatoryStates
	 */
	public String getMandatoryStates()
	{
		return mandatoryStates;
	}
	public void setRestrictedOnEditStates(String restrictedOnEditStates)
	{
		this.restrictedOnEditStates = restrictedOnEditStates;
	}
	public String getRestrictedOnEditStates()
	{
		return restrictedOnEditStates;
	}
	
	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}
	
	@Column(name = "INVISIBLE")
	public boolean isInvisible() {
		return invisible;
	}
	
	@Column(name = "INVISIBLE_IN_STATES")
	public String getInvisibleInStates() {
		return invisibleInStates;
	}
	
	public void setInvisibleInStates(String invisibleInStates) {
		this.invisibleInStates = invisibleInStates;
	}
	
	/**
	 * @param collection the collection to set
	 */
	public void setCollection(MetadataCollection collection)
	{
		this.collection = collection;
	}

	/**
	 * @return the collection
	 */
	@ManyToOne(optional=true)
	public MetadataCollection getCollection()
	{
		return collection;
	}	
	
	@Column(name = "default_value", length = 4000, nullable = true)
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
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