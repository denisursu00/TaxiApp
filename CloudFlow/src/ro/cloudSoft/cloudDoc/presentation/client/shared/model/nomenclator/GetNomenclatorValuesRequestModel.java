package ro.cloudSoft.cloudDoc.presentation.client.shared.model.nomenclator;

import java.util.List;

public class GetNomenclatorValuesRequestModel {

	private String nomenclatorCode;
	private Long nomenclatorId;

	private List<NomenclatorFilter> filters;

	public GetNomenclatorValuesRequestModel() {
		super();
	}

	public GetNomenclatorValuesRequestModel(String nomenclatorCode, List<NomenclatorFilter> filters) {
		super();
		this.nomenclatorCode = nomenclatorCode;
		this.filters = filters;
	}

	public GetNomenclatorValuesRequestModel(Long nomenclatorId, List<NomenclatorFilter> filters) {
		super();
		this.nomenclatorId = nomenclatorId;
		this.filters = filters;
	}


	public String getNomenclatorCode() {
		return nomenclatorCode;
	}

	public void setNomenclatorCode(String nomenclatorCode) {
		this.nomenclatorCode = nomenclatorCode;
	}

	public Long getNomenclatorId() {
		return nomenclatorId;
	}

	public void setNomenclatorId(Long nomenclatorId) {
		this.nomenclatorId = nomenclatorId;
	}

	public List<NomenclatorFilter> getFilters() {
		return filters;
	}

	public void setFilters(List<NomenclatorFilter> filters) {
		this.filters = filters;
	}
}
