package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

public class CentralizatorPrezentaPerioadaReportModel {

	private List<CentralizatorPrezentaPerioadaReportRowModel> rows;
	private Integer totalParticipariLevel0;
	private Integer totalParticipariLevel1;
	private Integer totalParticipariLevel2;
	private Integer totalParticipariLevel3;
	private Integer totalParticipariLevel3Plus;
	private Integer totalParticipariInAfaraNom;

	public List<CentralizatorPrezentaPerioadaReportRowModel> getRows() {
		return rows;
	}

	public void setRows(List<CentralizatorPrezentaPerioadaReportRowModel> rows) {
		this.rows = rows;
	}

	public Integer getTotalParticipariLevel0() {
		return totalParticipariLevel0;
	}

	public void setTotalParticipariLevel0(Integer totalParticipariLevel0) {
		this.totalParticipariLevel0 = totalParticipariLevel0;
	}

	public Integer getTotalParticipariLevel1() {
		return totalParticipariLevel1;
	}

	public void setTotalParticipariLevel1(Integer totalParticipariLevel1) {
		this.totalParticipariLevel1 = totalParticipariLevel1;
	}

	public Integer getTotalParticipariLevel2() {
		return totalParticipariLevel2;
	}

	public void setTotalParticipariLevel2(Integer totalParticipariLevel2) {
		this.totalParticipariLevel2 = totalParticipariLevel2;
	}

	public Integer getTotalParticipariLevel3() {
		return totalParticipariLevel3;
	}

	public void setTotalParticipariLevel3(Integer totalParticipariLevel3) {
		this.totalParticipariLevel3 = totalParticipariLevel3;
	}

	public Integer getTotalParticipariLevel3Plus() {
		return totalParticipariLevel3Plus;
	}

	public void setTotalParticipariLevel3Plus(Integer totalParticipariLevel3Plus) {
		this.totalParticipariLevel3Plus = totalParticipariLevel3Plus;
	}

	public Integer getTotalParticipariInAfaraNom() {
		return totalParticipariInAfaraNom;
	}

	public void setTotalParticipariInAfaraNom(Integer totalParticipariInAfaraNom) {
		this.totalParticipariInAfaraNom = totalParticipariInAfaraNom;
	}

}
