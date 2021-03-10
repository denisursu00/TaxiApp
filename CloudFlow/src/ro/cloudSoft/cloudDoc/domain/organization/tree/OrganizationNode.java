package ro.cloudSoft.cloudDoc.domain.organization.tree;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 
 */
public class OrganizationNode
{

    public String name;
    public int type;
    public String id;
    public Long managerId;
    public String title;
    public String customTitleTemplate;

    public List<OrganizationNode> children = Lists.newArrayList();
 
    public OrganizationNode() {}

    public OrganizationNode(String name, String id, int type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public int getNumberOfChildren() {
        if (children == null) {
            return 0;
        }
        return children.size();
    }

    public void addChild(OrganizationNode child) {
        if (children == null) {
            children = new ArrayList<OrganizationNode>();
        }
        children.add(child);
    }
     
    public void insertChildAt(int index, OrganizationNode child) throws IndexOutOfBoundsException {
        if (index == getNumberOfChildren()) {
            // this is really an append
            addChild(child);
            return;
        } else {
            children.get(index); //just to throw the exception, and stop here
            children.add(index, child);
        }
    }

    public void removeChildAt(int index) throws IndexOutOfBoundsException {
        children.remove(index);
    }

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
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
	public List<OrganizationNode> getChildren() {
		return children;
	}
	public void setChildren(List<OrganizationNode> children) {
		this.children = children;
	}
}