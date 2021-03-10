package ro.cloudSoft.cloudDoc.domain.content;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PROJECT")
public class ProjectMetadataDefinition extends MetadataDefinition {
	
	private boolean multipleProjectsSelection;
	
	@Column(name = "MULTIPLE_PROJECTS_SELECTION", nullable = true)
	public boolean isMultipleProjectsSelection() {
		return multipleProjectsSelection;
	}
	
	public void setMultipleProjectsSelection(boolean multipleProjectsSelection) {
		this.multipleProjectsSelection = multipleProjectsSelection;
	}
}