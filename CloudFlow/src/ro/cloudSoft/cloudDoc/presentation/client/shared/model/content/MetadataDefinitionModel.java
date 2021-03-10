package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import java.util.Arrays;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ModelWithLabel;

public class MetadataDefinitionModel extends BaseModel implements ModelWithLabel, IsSerializable {
	
	public static final int LENGTH_NAME = 128;
	public static final int LENGTH_LABEL = 128;

	private static final long serialVersionUID = -1798047104878308777L;
	
	public static final String TYPE_TEXT = "TEXT";
	public static final String TYPE_NUMERIC = "NUMERIC";
	public static final String TYPE_AUTO_NUMBER = "AUTO_NUMBER";
	public static final String TYPE_DATE = "DATE";
	public static final String TYPE_DATE_TIME = "DATE_TIME";
	public static final String TYPE_MONTH = "MONTH";
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
		TYPE_GROUP,
		TYPE_DOCUMENT,
		TYPE_CALENDAR
	});
	
	public static final List<String> TYPES_METADATA_FROM_COLLECTION =
			Arrays.asList(new String[] {
		TYPE_TEXT,
		TYPE_NUMERIC,
		TYPE_AUTO_NUMBER,
		TYPE_DATE,
		TYPE_LIST
	});
	
	public static final List<String> SIMPLE_TYPES = Arrays.asList(new String[] {
			TYPE_TEXT,
			TYPE_NUMERIC,
			TYPE_TEXT_AREA,			
	});

	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_LABEL = "label";
	public static final String PROPERTY_MANDATORY = "mandatory";
	public static final String PROPERTY_RESTRICTED_ON_EDIT = "restrictedOnEdit";
	public static final String PROPERTY_MANDATORY_STATES = "mandatoryStates";
	public static final String PROPERTY_RESTRICTED_ON_EDIT_STATES = "restrictedOnEditStates";
	public static final String PROPERTY_INVISIBLE = "invisible";
	public static final String PROPERTY_INVISIBLE_IN_STATES = "invisibleInStates";
	public static final String PROPERTY_REPRESENTATIVE = "representative";
	public static final String PROPERTY_INDEXED = "indexed";
	public static final String PROPERTY_ORDER_NUMBER = "orderNumber";
	public static final String PROPERTY_TYPE = "type";
	public static final String PROPERTY_DEFAULT_VALUE = "defaultValue";
	
	private String metadataNameForAutoCompleteWithMetadata;
	private String typeOfAutoCompleteWithMetadata;
	private String nomenclatorAttributeKeyForAutoCompleteWithMetadata;
	private String classNameForAutoCompleteWithMetadata;
	
	public MetadataDefinitionModel() {}
	
	public void setId(Long id){
		set(PROPERTY_ID, id);
	}
	
	public Long getId(){
		return get(PROPERTY_ID); 
	}
	
	public void setName(String name){
		set(PROPERTY_NAME, name);
	}
	
	public String getName(){
		return get(PROPERTY_NAME); 
	}
	
	public void setLabel(String label){
		set(PROPERTY_LABEL, label);
	}
	
	public String getLabel(){
		return get(PROPERTY_LABEL); 
	}
	
	public void setMandatory(Boolean mandatory){
		set(PROPERTY_MANDATORY, mandatory);
	}
	
	public Boolean isMandatory(){
		return get(PROPERTY_MANDATORY); 
	}
	
	public void setMandatoryStates(String mandatoryStates ){
		set( PROPERTY_MANDATORY_STATES, mandatoryStates );
	}
	
	public String getMandatoryStates(){
		return get( PROPERTY_MANDATORY_STATES );
	}
	
	public void setRestrictedOnEdit(Boolean restrictedOnEdit){
		set(PROPERTY_RESTRICTED_ON_EDIT, restrictedOnEdit);
	}
	
	public Boolean isRestrictedOnEdit(){
		return get(PROPERTY_RESTRICTED_ON_EDIT); 
	}
	
	public void setRestrictedOnEditStates(String restrictedOnEditStates) {
		set(PROPERTY_RESTRICTED_ON_EDIT_STATES, restrictedOnEditStates);
	}
	public String getRestrictedOnEditStates() {
		return get(PROPERTY_RESTRICTED_ON_EDIT_STATES);
	}
	
	public void setInvisible(Boolean invisible){
		set(PROPERTY_INVISIBLE, invisible);
	}
	
	public Boolean isInvisible(){
		return get(PROPERTY_INVISIBLE); 
	}
	
	public void setInvisibleInStates(String invisibleInStates) {
		set(PROPERTY_INVISIBLE_IN_STATES, invisibleInStates);
	}
	public String getInvisibleInStates() {
		return get(PROPERTY_INVISIBLE_IN_STATES);
	}
	
	public void setRepresentative(Boolean representative){
		set(PROPERTY_REPRESENTATIVE, representative);
	}
	
	public Boolean isRepresentative(){
		return get(PROPERTY_REPRESENTATIVE); 
	}
	
	public void setIndexed(Boolean indexed){
		set(PROPERTY_INDEXED, indexed);
	}
	
	public Boolean isIndexed(){
		return get(PROPERTY_INDEXED); 
	}
	
	public void setOrderNumber(Integer orderNumber){
		set(PROPERTY_ORDER_NUMBER, orderNumber);
	}
	
	public Integer getOrderNumber(){
		return get(PROPERTY_ORDER_NUMBER);
	}
	
	public void setType(String type){
		set(PROPERTY_TYPE, type);
	}
	
	public String getType(){
		return get(PROPERTY_TYPE); 
	}
	
	public void setDefaultValue(String defaultValue) {
		set(PROPERTY_DEFAULT_VALUE, defaultValue);
	}
	
	public String getDefaultValue() {
		return get(PROPERTY_DEFAULT_VALUE);
	}

	public String getMetadataNameForAutoCompleteWithMetadata() {
		return metadataNameForAutoCompleteWithMetadata;
	}

	public void setMetadataNameForAutoCompleteWithMetadata(String metadataForAutoCompleteWithMetadata) {
		this.metadataNameForAutoCompleteWithMetadata = metadataForAutoCompleteWithMetadata;
	}

	public String getTypeOfAutoCompleteWithMetadata() {
		return typeOfAutoCompleteWithMetadata;
	}

	public void setTypeOfAutoCompleteWithMetadata(String typeOfAutoCompleteWithMetadata) {
		this.typeOfAutoCompleteWithMetadata = typeOfAutoCompleteWithMetadata;
	}

	public String getNomenclatorAttributeKeyForAutoCompleteWithMetadata() {
		return nomenclatorAttributeKeyForAutoCompleteWithMetadata;
	}
	
	public void setNomenclatorAttributeKeyForAutoCompleteWithMetadata(
			String nomenclatorAttributeKeyForAutoCompleteWithMetadata) {
		this.nomenclatorAttributeKeyForAutoCompleteWithMetadata = nomenclatorAttributeKeyForAutoCompleteWithMetadata;
	}
	
	public String getClassNameForAutoCompleteWithMetadata() {
		return classNameForAutoCompleteWithMetadata;
	}
	
	public void setClassNameForAutoCompleteWithMetadata(String classNameForAutoCompleteWithMetadata) {
		this.classNameForAutoCompleteWithMetadata = classNameForAutoCompleteWithMetadata;
	}
}