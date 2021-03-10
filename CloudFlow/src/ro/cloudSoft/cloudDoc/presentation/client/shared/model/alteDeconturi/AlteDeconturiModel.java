package ro.cloudSoft.cloudDoc.presentation.client.shared.model.alteDeconturi;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class AlteDeconturiModel {

	private Long id;
	private String month;
	private String titularDecont;
	private String numarDecont;
	private Date dataDecont;
	private BigDecimal avansPrimit;
	private String tipAvansPrimit;
	private List<AlteDeconturiCheltuialaModel> cheltuieli;
	private BigDecimal totalCheltuieli;
	private BigDecimal totalDeIncasatRestituit;
	private boolean anulat;
	private String motivAnulare;
	private boolean finalizat;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getTitularDecont() {
		return titularDecont;
	}

	public void setTitularDecont(String titularDecont) {
		this.titularDecont = titularDecont;
	}

	public String getNumarDecont() {
		return numarDecont;
	}

	public void setNumarDecont(String numarDecont) {
		this.numarDecont = numarDecont;
	}

	public Date getDataDecont() {
		return dataDecont;
	}

	public void setDataDecont(Date dataDecont) {
		this.dataDecont = dataDecont;
	}

	public BigDecimal getAvansPrimit() {
		return avansPrimit;
	}

	public void setAvansPrimit(BigDecimal avansPrimit) {
		this.avansPrimit = avansPrimit;
	}

	public String getTipAvansPrimit() {
		return tipAvansPrimit;
	}

	public void setTipAvansPrimit(String tipAvansPrimit) {
		this.tipAvansPrimit = tipAvansPrimit;
	}

	public List<AlteDeconturiCheltuialaModel> getCheltuieli() {
		return cheltuieli;
	}

	public void setCheltuieli(List<AlteDeconturiCheltuialaModel> cheltuieli) {
		this.cheltuieli = cheltuieli;
	}

	public BigDecimal getTotalCheltuieli() {
		return totalCheltuieli;
	}

	public void setTotalCheltuieli(BigDecimal totalCheltuieli) {
		this.totalCheltuieli = totalCheltuieli;
	}

	public BigDecimal getTotalDeIncasatRestituit() {
		return totalDeIncasatRestituit;
	}

	public void setTotalDeIncasatRestituit(BigDecimal totalDeIncasatRestituit) {
		this.totalDeIncasatRestituit = totalDeIncasatRestituit;
	}

	public boolean isAnulat() {
		return anulat;
	}

	public void setAnulat(boolean anulat) {
		this.anulat = anulat;
	}

	public String getMotivAnulare() {
		return motivAnulare;
	}

	public void setMotivAnulare(String motivAnulare) {
		this.motivAnulare = motivAnulare;
	}
	
	public boolean isFinalizat() {
		return finalizat;
	}

	public void setFinalizat(boolean finalizat) {
		this.finalizat = finalizat;
	}
	
}
