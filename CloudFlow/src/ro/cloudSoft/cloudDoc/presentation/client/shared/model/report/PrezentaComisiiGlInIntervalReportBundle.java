package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.SimpleListItemModel;

public class PrezentaComisiiGlInIntervalReportBundle {

	private List<SimpleListItemModel> institutieItems;
	private List<SimpleListItemModel> participantAcreditatItems;

	public List<SimpleListItemModel> getInstitutieItems() {
		return institutieItems;
	}

	public void setInstitutieItems(List<SimpleListItemModel> institutieItems) {
		this.institutieItems = institutieItems;
	}

	public List<SimpleListItemModel> getParticipantAcreditatItems() {
		return participantAcreditatItems;
	}

	public void setParticipantAcreditatItems(List<SimpleListItemModel> participantAcreditatItems) {
		this.participantAcreditatItems = participantAcreditatItems;
	}

}
