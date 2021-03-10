package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;
import java.util.List;

public class ReprezentantiBancaPerFunctieSiComisieReportFilterModel {

	private Date dataSedintaDeLa;
	private Date dataSedintaPanaLa;
	private List<Long> comisieId;
	private List<Long> institutieId;
	private List<DocumentIdentifierModel> documents;

	public Date getDataSedintaDeLa() {
		return dataSedintaDeLa;
	}

	public void setDataSedintaDeLa(Date dataSedintaDeLa) {
		this.dataSedintaDeLa = dataSedintaDeLa;
	}

	public Date getDataSedintaPanaLa() {
		return dataSedintaPanaLa;
	}

	public void setDataSedintaPanaLa(Date dataSedintaPanaLa) {
		this.dataSedintaPanaLa = dataSedintaPanaLa;
	}

	public List<Long> getComisieId() {
		return comisieId;
	}

	public void setComisieId(List<Long> comisieId) {
		this.comisieId = comisieId;
	}

	public List<Long> getInstitutieId() {
		return institutieId;
	}

	public void setInstitutieId(List<Long> institutieId) {
		this.institutieId = institutieId;
	}

	public List<DocumentIdentifierModel> getDocuments() {
		return documents;
	}

	public void setDocuments(List<DocumentIdentifierModel> documents) {
		this.documents = documents;
	}

}
