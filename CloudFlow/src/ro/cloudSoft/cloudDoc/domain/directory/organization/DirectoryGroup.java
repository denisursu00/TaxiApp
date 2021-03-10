package ro.cloudSoft.cloudDoc.domain.directory.organization;

import java.util.Collection;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import ro.cloudSoft.cloudDoc.domain.directory.AbstractDirectoryEntity;

import com.google.common.collect.Lists;

/**
 * 
 */
public class DirectoryGroup extends AbstractDirectoryEntity {

	private String name;
	private String description;
	private Collection<String> memberDns = Lists.newLinkedList();
	
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public Collection<String> getMemberDns() {
		return memberDns;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setMemberDns(Collection<String> memberDns) {
		this.memberDns = memberDns;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append(dn)
			.toString();
	}
}