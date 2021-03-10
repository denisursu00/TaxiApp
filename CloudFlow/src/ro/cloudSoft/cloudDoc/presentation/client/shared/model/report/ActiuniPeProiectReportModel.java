package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

public class ActiuniPeProiectReportModel {

	private List<ActiuniPeProiectTaskReportModel> actiuniPeProiectTask;
	private List<ActiuniPeProiectRegistruIntrariIesiriReportModel> actiuniPeProiectRegistruIntrariIesiri;
	private List<ActiuniPeProiectNotaCDReportModel> actiuniPeProiectNotaCD;
	
	public List<ActiuniPeProiectTaskReportModel> getActiuniPeProiectTask() {
		return actiuniPeProiectTask;
	}
	
	public void setActiuniPeProiectTask(List<ActiuniPeProiectTaskReportModel> actiuniPeProiectTask) {
		this.actiuniPeProiectTask = actiuniPeProiectTask;
	}
	
	public List<ActiuniPeProiectRegistruIntrariIesiriReportModel> getActiuniPeProiectRegistruIntrariIesiri() {
		return actiuniPeProiectRegistruIntrariIesiri;
	}
	
	public void setActiuniPeProiectRegistruIntrariIesiri(
			List<ActiuniPeProiectRegistruIntrariIesiriReportModel> actiuniPeProiectRegistruIntrariIesiri) {
		this.actiuniPeProiectRegistruIntrariIesiri = actiuniPeProiectRegistruIntrariIesiri;
	}
	
	public List<ActiuniPeProiectNotaCDReportModel> getActiuniPeProiectNotaCD() {
		return actiuniPeProiectNotaCD;
	}
	
	public void setActiuniPeProiectNotaCD(List<ActiuniPeProiectNotaCDReportModel> actiuniPeProiectNotaCD) {
		this.actiuniPeProiectNotaCD = actiuniPeProiectNotaCD;
	}
}
