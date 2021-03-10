package ro.cloudSoft.cloudDoc.services;

public enum GeneralReport {
	
	DSP("DSP", "DSP");

	private final String reportIdentifier;
	private final String sourceDirectoryName;
	
	private GeneralReport(String reportIdentifier, String sourceDirectoryName) {
		this.reportIdentifier = reportIdentifier;
		this.sourceDirectoryName = sourceDirectoryName;
	}
	
	public String getReportIdentifier() {
		return reportIdentifier;
	}
	
	public String getSourceDirectoryName() {
		return sourceDirectoryName;
	}
}
