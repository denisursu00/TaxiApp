package ro.cloudSoft.cloudDoc.presentation.client.shared.model.content;

@SuppressWarnings("serial")
public class ProjectMetadataDefinitionModel extends MetadataDefinitionModel {
	
	private boolean multipleProjectsSelection;
	
	public boolean isMultipleProjectsSelection() {
		return multipleProjectsSelection;
	}
	
	public void setMultipleProjectsSelection(boolean multipleProjectsSelection) {
		this.multipleProjectsSelection = multipleProjectsSelection;
	}
}