package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DocumentLocationModel extends ContentEntityModel implements IsSerializable {

	private static final long serialVersionUID = 7591854895878741742L;
	
	public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_REAL_NAME = "realName";
    public static final String PROPERTY_DESCRIPTION = "description";

    public DocumentLocationModel() {}

    public DocumentLocationModel(String name, String realName, String descr) {
        setName(name);
        setRealName(realName);
        setDescription(descr);
    }

    public String getName(){
        return (String)get(PROPERTY_NAME);
    }

    public String getRealName(){
        return (String)get(PROPERTY_REAL_NAME);
    }

    public String getDescription() {
        return (String)get(PROPERTY_DESCRIPTION);
    }

    public void setName(String name){
        set(PROPERTY_NAME, name);
    }

    public void setDescription(String description) {
        set(PROPERTY_DESCRIPTION, description);
    }

    public void setRealName(String realName){
        set(PROPERTY_REAL_NAME, realName);
    }

    @Override
    public String getId() {
        //return getRealName();
        return null;
    }
    
    // Am adaugat doar ca sa se mapeze cu modelul ce vine din frontend
    public void setId(String id) {
    }
    
    @Override
    public String getDocumentLocationRealName() {
        return getRealName();
    }

    // Am adaugat doar ca sa se mapeze cu modelul ce vine din frontend
    public void setDocumentLocationRealName(String documentLocationRealName) {
    }
    
    @Override
    public String getPath() {
        return getName();
    }

    // Am adaugat doar ca sa se mapeze cu modelul ce vine din frontend
    public void setPath(String path) {
    }
    
    @Override
    public String getFolderId() {
        return null;
    }

    // Am adaugat doar ca sa se mapeze cu modelul ce vine din frontend
    public void setFolderId(String folderId) {
    }
    
    public String getEntityType(){
        return CONTENT_ENTITY_TYPE_WORKSPACE;
    }

    // Am adaugat doar ca sa se mapeze cu modelul ce vine din frontend
    public void setEntityType(String entityType) {
    }

    // Am adaugat doar ca sa se mapeze cu modelul ce vine din frontend
    public void setRealPath(String realPath) {
    }
}