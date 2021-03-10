package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class MembriiAfiliatiReportRowModel {

	private String institutie;
	private String reprezentant;
	private String comisie;
	private Date dataSedinta;

	public String getInstitutie() {
		return institutie;
	}

	public void setInstitutie(String institutie) {
		this.institutie = institutie;
	}

	public String getReprezentant() {
		return reprezentant;
	}

	public void setReprezentant(String reprezentant) {
		this.reprezentant = reprezentant;
	}

	public String getComisie() {
		return comisie;
	}

	public void setComisie(String comisie) {
		this.comisie = comisie;
	}

	public Date getDataSedinta() {
		return dataSedinta;
	}

	public void setDataSedinta(Date dataSedinta) {
		this.dataSedinta = dataSedinta;
	}

}
