package ro.cloudSoft.cloudDoc.domain.directory.organization;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import ro.cloudSoft.cloudDoc.domain.directory.AbstractDirectoryEntity;

/**
 * 
 */
public class DirectoryOrganizationUnit extends AbstractDirectoryEntity {

	private String name;
	private String description;
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append(dn)
			.toString();
	}
}