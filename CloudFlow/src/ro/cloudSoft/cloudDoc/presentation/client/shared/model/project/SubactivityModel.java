package ro.cloudSoft.cloudDoc.presentation.client.shared.model.project;

public class SubactivityModel {

	private Long id;
	private String name;
	
	public SubactivityModel() {};
	
	public SubactivityModel(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
