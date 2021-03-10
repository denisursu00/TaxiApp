package ro.cloudSoft.cloudDoc.dto.organization;

import java.util.List;

public class OrganizationTreeNodeDTO {
	
	private Type type;
	
	// common
	private String id;
	private String parentId;
	private String name;
	
	// ou
	private String managerId;
	private List<OrganizationTreeNodeDTO> children;
	
	// user
	private String title;
	private String customTitleTemplate;
	
	public static enum Type {
		ORGANIZATION,
		ORGANIZATION_UNIT,
		USER
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public List<OrganizationTreeNodeDTO> getChildren() {
		return children;
	}

	public void setChildren(List<OrganizationTreeNodeDTO> children) {
		this.children = children;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCustomTitleTemplate() {
		return customTitleTemplate;
	}

	public void setCustomTitleTemplate(String customTitleTemplate) {
		this.customTitleTemplate = customTitleTemplate;
	}
}
