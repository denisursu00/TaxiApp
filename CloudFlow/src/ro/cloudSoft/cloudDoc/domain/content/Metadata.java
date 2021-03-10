package ro.cloudSoft.cloudDoc.domain.content;

/**
 *
 * 
 */
@Deprecated
//DANASA: needs refactoring
//TO DO: add id metadatadef+value  
public class Metadata {

    private long idMetadataDefinition;
    
	private String labelName;
    private String value;
	private String type;
    private boolean required;

    /**
     * @return the idMetadataDefinition
     */
    public long getIdMetadataDefinition() {
		return idMetadataDefinition;
	}
    /**
     * @param idMetadataDefinition the idMetadataDefinition to set
     */
	public void setIdMetadataDefinition(long idMetadataDefinition) {
		this.idMetadataDefinition = idMetadataDefinition;
	}
    
    /**
     * @return the labelName
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * @param labelName the labelName to set
     */
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the required
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * @param required the required to set
     */
    public void setRequired(boolean required) {
        this.required = required;
    }
}
