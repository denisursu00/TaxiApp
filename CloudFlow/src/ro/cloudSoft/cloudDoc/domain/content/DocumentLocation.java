package ro.cloudSoft.cloudDoc.domain.content;

public class DocumentLocation {
	
	public static final int MAX_LENGTH_REAL_NAME = 20;

    private ACL acl;
    
    private String name;
    private String realName;
    
    private String description;

    public ACL getAcl() {
        return this.acl;
    }
    public void setAcl(ACL val) {
        this.acl = val;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRealName() {
        return realName;
    }
    public void setRealName(String realName) {
        this.realName = realName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}