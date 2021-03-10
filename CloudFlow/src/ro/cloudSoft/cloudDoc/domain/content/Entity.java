package ro.cloudSoft.cloudDoc.domain.content;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

/**
 * Clasa de baza pentru definirea continutului.
 * Are numai proprietatile de baza pentru entitati
 * 
 */
public abstract class Entity {
	
	public static final int MAX_LENGTH_ID = 100;

    /**
     * Numele asociat entitatii
     */
    private String name;
    /**
     * Descrierea asociata entitatii
     */
    private String description;
    /**
     * Identificatorul unic asociat entitatii
     */
    private String id;
    /**
     * data ultimei actualizari
     */
    private Calendar lastUpdate;
    /**
     * Data crearii entitatii
     */
    private Calendar created;
    /**
     * Utilizatorul care a creat continutul
     */
    private String author;
    /**
     * Calea unde se afla entitatea
     */
    private String RealPath;
    /**
     * Utilizatorul care a facut lock pe entitate
     */
    private String lockedBy;
    private ACL acl;
    private String parentId;
    /**
     * Calea (afisata in interfata) unde se afla entitatea
     */
    private String path;
    /**
     * Id-ul tipului de document asociat documentelor si/sau folderelor
     */
	private Long documentTypeId;

    public Entity() {
    }
	
    /**
     * Verifica daca entitatea este noua (nu a fost inca persistata).
     */
	public boolean isNew() {
		return (getId() == null);
	}

    public String getAuthor() {
        return author;
    }
    
    public Long getAuthorUserId() {
    	if (StringUtils.isBlank(getAuthor())) {
    		return null;
    	}
    	return Long.valueOf(getAuthor());
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Calendar getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Calendar lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealPath() {
        return RealPath;
    }

    public void setRealPath(String path) {
        this.RealPath = path;
    }  
    
    /**
     * Verifica daca documentul este blocat.
     */
    public boolean isLocked() {
    	return StringUtils.isNotBlank(getLockedBy());
    }
    
    public Long getLockedByUserId() {
    	String lockedByUserIdAsString = getLockedBy();
    	if (StringUtils.isBlank(lockedByUserIdAsString)) {
    		return null;
    	}
    	return Long.valueOf(lockedByUserIdAsString);
    }

    /**
     * @return the lockedBy
     */
    public String getLockedBy() {
        return lockedBy;
    }

    /**
     * @param lockedBy the lockedBy to set
     */
    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public ACL getAcl() {
        return acl;
    }

    public void setAcl(ACL val) {
        this.acl = val;
    }

    /**
     * @return the parentId
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }
    public Long getDocumentTypeId()
    {
    	return documentTypeId;
    }
    public void setDocumentTypeId (Long documentTypeId)
    {
    	this.documentTypeId = documentTypeId;
    }
}