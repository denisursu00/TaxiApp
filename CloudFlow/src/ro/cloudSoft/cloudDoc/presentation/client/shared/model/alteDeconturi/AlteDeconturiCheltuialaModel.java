package ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class AlteDeconturiCheltuialaModel {

	private Long id;
	private String tipDocumentJustificativ;
	private String explicatie;
	private String numarDocumentJustificativ;
	private Date dataDocumentJustificativ;
	private BigDecimal valoareCheltuiala;
	private List<AlteDeconturiCheltuialaAtasamentModel> atasamente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipDocumentJustificativ() {
		return tipDocumentJustificativ;
	}

	public void setTipDocumentJustificativ(String tipDocumentJustificativ) {
		this.tipDocumentJustificativ = tipDocumentJustificativ;
	}

	public String getExplicatie() {
		return explicatie;
	}

	public void setExplicatie(String explicatie) {
		this.explicatie = explicatie;
	}

	public String getNumarDocumentJustificativ() {
		return numarDocumentJustificativ;
	}

	public void setNumarDocumentJustificativ(String numarDocumentJustificativ) {
		this.numarDocumentJustificativ = numarDocumentJustificativ;
	}

	public Date getDataDocumentJustificativ() {
		return dataDocumentJustificativ;
	}

	public void setDataDocumentJustificativ(Date dataDocumentJustificativ) {
		this.dataDocumentJustificativ = dataDocumentJustificativ;
	}

	public BigDecimal getValoareCheltuiala() {
		return valoareCheltuiala;
	}

	public void setValoareCheltuiala(BigDecimal valoareCheltuiala) {
		this.valoareCheltuiala = valoareCheltuiala;
	}

	public List<AlteDeconturiCheltuialaAtasamentModel> getAtasamente() {
		return atasamente;
	}

	public void setAtasamente(List<AlteDeconturiCheltuialaAtasamentModel> atasamenteModel) {
		this.atasamente = atasamenteModel;
	}


}
