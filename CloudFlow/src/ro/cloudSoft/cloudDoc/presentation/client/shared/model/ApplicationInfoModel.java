package ro.cloudSoft.cloudDoc.presentation.client.shared.model;

public class ApplicationInfoModel {

	private String environmentName;
	private boolean production;
	
	public String getEnvironmentName() {
		return environmentName;
	}
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	public boolean isProduction() {
		return production;
	}
	public void setProduction(boolean production) {
		this.production = production;
	}
}
