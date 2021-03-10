package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ListItemModel;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class DocumentTypeTemplateModel extends BaseModel implements IsSerializable, ListItemModel {

    private static final long serialVersionUID = -3655210218081758390L;
	
    public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_DESCRIPTION = "description";
	private String exportAvailabilityExpression;
	
	public DocumentTypeTemplateModel() {}
	
	public DocumentTypeTemplateModel(String name, String description) {
		this.setName(name);
		this.setDescription(description);
	}
	
	@Override
	public String toString() {
		return this.getName() + " - " + this.getDescription();
	}
	
	public String getName() {
		return this.get(PROPERTY_NAME);
	}
	public void setName(String name) {
		this.set(PROPERTY_NAME, name);
	}
    public String getDescription() {
        return get(PROPERTY_DESCRIPTION);
    }
    public void setDescription(String description) {
        set(PROPERTY_DESCRIPTION, description);
    }
    
    @Override
    public String getItemValue() {
    	return getName();
    }
    
    @Override
    public String getItemLabel() {
    	return getDescription();
    }
    
    public String getExportAvailabilityExpression() {
		return exportAvailabilityExpression;
	}
    
    public void setExportAvailabilityExpression(String exportAvailabilityExpression) {
		this.exportAvailabilityExpression = exportAvailabilityExpression;
	}
}