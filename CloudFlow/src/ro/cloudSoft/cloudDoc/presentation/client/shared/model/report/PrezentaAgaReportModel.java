package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.math.BigDecimal;
import java.util.List;

public class PrezentaAgaReportModel {

	private List<PrezentaAgaReportRowModel> rows;
	private Integer prezentaActualaPresedinte;
	private Integer prezentaActualaVicepresedinte;
	private Integer prezentaActualaInlocuitor;
	private Integer prezentaActualaTotal;
	private Integer prezentaActualaMembriVotanti;
	private BigDecimal prezentaProcentPresedinte;
	private BigDecimal prezentaProcentVicepresedinte;
	private BigDecimal prezentaProcentInlocuitor;
	private BigDecimal prezentaProcentMembriVotanti;
	private Integer membriAsociatie;
	private BigDecimal prezentaNecesara;
	private Integer restNecesarPrezenta;
	private BigDecimal necesarAdoptare;
	private BigDecimal jumatePlusUnu;
	private BigDecimal necesarAdoptareHotarare;
	private Integer necesarPentruVotSecret;

	public List<PrezentaAgaReportRowModel> getRows() {
		return rows;
	}

	public void setRows(List<PrezentaAgaReportRowModel> rows) {
		this.rows = rows;
	}

	public Integer getPrezentaActualaPresedinte() {
		return prezentaActualaPresedinte;
	}

	public void setPrezentaActualaPresedinte(Integer prezentaActualaPresedinte) {
		this.prezentaActualaPresedinte = prezentaActualaPresedinte;
	}

	public Integer getPrezentaActualaVicepresedinte() {
		return prezentaActualaVicepresedinte;
	}

	public void setPrezentaActualaVicepresedinte(Integer prezentaActualaVicepresedinte) {
		this.prezentaActualaVicepresedinte = prezentaActualaVicepresedinte;
	}

	public Integer getPrezentaActualaInlocuitor() {
		return prezentaActualaInlocuitor;
	}

	public void setPrezentaActualaInlocuitor(Integer prezentaActualaInlocuitor) {
		this.prezentaActualaInlocuitor = prezentaActualaInlocuitor;
	}

	public Integer getPrezentaActualaTotal() {
		return prezentaActualaTotal;
	}

	public void setPrezentaActualaTotal(Integer prezentaActualaTotal) {
		this.prezentaActualaTotal = prezentaActualaTotal;
	}

	public Integer getPrezentaActualaMembriVotanti() {
		return prezentaActualaMembriVotanti;
	}

	public void setPrezentaActualaMembriVotanti(Integer prezentaActualaMembriVotanti) {
		this.prezentaActualaMembriVotanti = prezentaActualaMembriVotanti;
	}

	public BigDecimal getPrezentaProcentPresedinte() {
		return prezentaProcentPresedinte;
	}

	public void setPrezentaProcentPresedinte(BigDecimal prezentaProcentPresedinte) {
		this.prezentaProcentPresedinte = prezentaProcentPresedinte;
	}

	public BigDecimal getPrezentaProcentVicepresedinte() {
		return prezentaProcentVicepresedinte;
	}

	public void setPrezentaProcentVicepresedinte(BigDecimal prezentaProcentVicepresedinte) {
		this.prezentaProcentVicepresedinte = prezentaProcentVicepresedinte;
	}

	public BigDecimal getPrezentaProcentInlocuitor() {
		return prezentaProcentInlocuitor;
	}

	public void setPrezentaProcentInlocuitor(BigDecimal prezentaProcentInlocuitor) {
		this.prezentaProcentInlocuitor = prezentaProcentInlocuitor;
	}

	public BigDecimal getPrezentaProcentMembriVotanti() {
		return prezentaProcentMembriVotanti;
	}

	public void setPrezentaProcentMembriVotanti(BigDecimal prezentaProcentMembriVotanti) {
		this.prezentaProcentMembriVotanti = prezentaProcentMembriVotanti;
	}

	public Integer getMembriAsociatie() {
		return membriAsociatie;
	}

	public void setMembriAsociatie(Integer membriAsociatie) {
		this.membriAsociatie = membriAsociatie;
	}

	public BigDecimal getPrezentaNecesara() {
		return prezentaNecesara;
	}

	public void setPrezentaNecesara(BigDecimal prezentaNecesara) {
		this.prezentaNecesara = prezentaNecesara;
	}

	public Integer getRestNecesarPrezenta() {
		return restNecesarPrezenta;
	}

	public void setRestNecesarPrezenta(Integer restNecesarPrezenta) {
		this.restNecesarPrezenta = restNecesarPrezenta;
	}

	public BigDecimal getNecesarAdoptare() {
		return necesarAdoptare;
	}

	public void setNecesarAdoptare(BigDecimal necesarAdoptare) {
		this.necesarAdoptare = necesarAdoptare;
	}

	public BigDecimal getJumatePlusUnu() {
		return jumatePlusUnu;
	}

	public void setJumatePlusUnu(BigDecimal jumatePlusUnu) {
		this.jumatePlusUnu = jumatePlusUnu;
	}

	public BigDecimal getNecesarAdoptareHotarare() {
		return necesarAdoptareHotarare;
	}

	public void setNecesarAdoptareHotarare(BigDecimal necesarAdoptareHotarare) {
		this.necesarAdoptareHotarare = necesarAdoptareHotarare;
	}

	public Integer getNecesarPentruVotSecret() {
		return necesarPentruVotSecret;
	}

	public void setNecesarPentruVotSecret(Integer necesarPentruVotSecret) {
		this.necesarPentruVotSecret = necesarPentruVotSecret;
	}
	
}
