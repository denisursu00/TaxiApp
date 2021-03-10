package ro.cloudSoft.cloudDoc.presentation.client.shared.model.prezentaOnline;

import java.util.List;

public class ParticipantiModel {

	private int totalParticipanti;
	private int totalMembriiArb;
	private int totalMembriiAfiliati;
	private int totalMembriiAlteInstitutii;
	private List<PrezentaMembriiReprezentantiComisieGl> rows;

	public int getTotalParticipanti() {
		return totalParticipanti;
	}

	public void setTotalParticipanti(int totalParticipanti) {
		this.totalParticipanti = totalParticipanti;
	}

	public int getTotalMembriiArb() {
		return totalMembriiArb;
	}

	public void setTotalMembriiArb(int totalMembriiArb) {
		this.totalMembriiArb = totalMembriiArb;
	}

	public int getTotalMembriiAfiliati() {
		return totalMembriiAfiliati;
	}

	public void setTotalMembriiAfiliati(int totalMembriiAfiliati) {
		this.totalMembriiAfiliati = totalMembriiAfiliati;
	}

	public int getTotalMembriiAlteInstitutii() {
		return totalMembriiAlteInstitutii;
	}

	public void setTotalMembriiAlteInstitutii(int totalMembriiAlteInstitutii) {
		this.totalMembriiAlteInstitutii = totalMembriiAlteInstitutii;
	}

	public List<PrezentaMembriiReprezentantiComisieGl> getRows() {
		return rows;
	}

	public void setRows(List<PrezentaMembriiReprezentantiComisieGl> rows) {
		this.rows = rows;
	}

}
