package ro.cloudSoft.cloudDoc.domain.directory;

/**
 * Clasa abstracta pentru o entitate din director (LDAP)
 * 
 * 
 */
public abstract class AbstractDirectoryEntity {

	protected String dn;
	
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	
	@Override
	public String toString() {
		return getDn();
	}
}