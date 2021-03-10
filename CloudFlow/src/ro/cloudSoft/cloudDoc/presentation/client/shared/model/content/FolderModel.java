package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FolderModel extends ContentEntityModel implements IsSerializable {

	private static final long serialVersionUID = -391676071139567614L;
	
    public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_AUTHOR = "author";
    public static final String PROPERTY_DESCRIPTION = "description";
    public static final String PROPERTY_PATH = "path";
    public static final String PROPERTY_PARENT_ID = "parentId";
    public static final String PROPERTY_REAL_PATH = "realPath";
    public static final String PROPERTY_DOCUMENT_LOCATION_REAL_NAME = "documentLocationRealName";
    public static final String PROPERTY_DOCUMENT_TYPE_ID = "documentTypeId";

    public FolderModel(){
        super();
    }

    public FolderModel(String name, String id) {
        this();
        setName(name);
        setId(id);
    }

    public String getName(){
        return (String)get(PROPERTY_NAME);
    }

    public void setName(String name){
        set(PROPERTY_NAME, name);
    }

    public String getId(){
        return (String)get(PROPERTY_ID);
    }

    public void setId(String id){
        set(PROPERTY_ID, id);
    }

    public String getAuthor(){
        return (String)get(PROPERTY_AUTHOR);
    }

    public void setAuthor(String author){
        set(PROPERTY_AUTHOR, author);
    }

    public String getDescription() {
        return get(PROPERTY_DESCRIPTION);
    }

    public void setDescription(String description) {
        set(PROPERTY_DESCRIPTION, description);
    }

    public String getPath(){
        return (String)get(PROPERTY_PATH);
    }

    public void setPath(String path){
        set(PROPERTY_PATH, path);
    }

    @Override
    public String getRealPath(){
        return (String)get(PROPERTY_REAL_PATH);
    }

    public void setRealPath(String realPath){
        set(PROPERTY_REAL_PATH, realPath);
    }

    public String getParentId(){
        return (String)get(PROPERTY_PARENT_ID);
    }

    public void setParentId(String parentid){
        set(PROPERTY_PARENT_ID, parentid);
    }

    public String getDocumentLocationRealName() {
        return get(PROPERTY_DOCUMENT_LOCATION_REAL_NAME);
    }

    public void setDocumentLocationRealName(String documentLocationRealName) {
        set(PROPERTY_DOCUMENT_LOCATION_REAL_NAME, documentLocationRealName);
    }
    
    
    @Override
    public String getFolderId() {
        return getParentId();
    }
    
    public String getEntityType(){
        return CONTENT_ENTITY_TYPE_FOLDER;
    }
    
    public Long getDocumentTypeId() {
    	return get(PROPERTY_DOCUMENT_TYPE_ID);
    }
    
    public void setDocumentTypeId(Long documentTypeId) {
    	set(PROPERTY_DOCUMENT_TYPE_ID, documentTypeId);
    }
}