package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

public class NivelReprezentareComisiiReportModel {

	private List<NivelReprezentareComisiiReportRowModel> rows;
	private int totalPresedintiComisii;
	private int totalVicepresedintiComisii;
	private int totalReprezentareOrganismeInterne;
	private int totalReprezentareOrganismeInternationale;
	private int sumaTotalReprezentare;
	private double totalProcentReprezentare;
	
	public List<NivelReprezentareComisiiReportRowModel> getRows() {
		return rows;
	}
	public void setRows(List<NivelReprezentareComisiiReportRowModel> rows) {
		this.rows = rows;
	}
	public int getTotalPresedintiComisii() {
		return totalPresedintiComisii;
	}
	public void setTotalPresedintiComisii(int totalPresedintiComisii) {
		this.totalPresedintiComisii = totalPresedintiComisii;
	}
	public int getTotalVicepresedintiComisii() {
		return totalVicepresedintiComisii;
	}
	public void setTotalVicepresedintiComisii(int totalVicepresedintiComisii) {
		this.totalVicepresedintiComisii = totalVicepresedintiComisii;
	}
	public int getTotalReprezentareOrganismeInterne() {
		return totalReprezentareOrganismeInterne;
	}
	public void setTotalReprezentareOrganismeInterne(int totalReprezentareOrganismeInterne) {
		this.totalReprezentareOrganismeInterne = totalReprezentareOrganismeInterne;
	}
	public int getTotalReprezentareOrganismeInternationale() {
		return totalReprezentareOrganismeInternationale;
	}
	public void setTotalReprezentareOrganismeInternationale(int totalReprezentareOrganismeInternationale) {
		this.totalReprezentareOrganismeInternationale = totalReprezentareOrganismeInternationale;
	}
	public int getSumaTotalReprezentare() {
		return sumaTotalReprezentare;
	}
	public void setSumaTotalReprezentare(int sumaTotalReprezentare) {
		this.sumaTotalReprezentare = sumaTotalReprezentare;
	}
	public double getTotalProcentReprezentare() {
		return totalProcentReprezentare;
	}
	public void setTotalProcentReprezentare(double totalProcentReprezentare) {
		this.totalProcentReprezentare = totalProcentReprezentare;
	}
	
}
