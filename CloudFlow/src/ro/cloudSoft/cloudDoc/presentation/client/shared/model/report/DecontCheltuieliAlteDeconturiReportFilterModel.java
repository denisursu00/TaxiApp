package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

import java.util.Date;

public class DecontCheltuieliAlteDeconturiReportFilterModel {

	private String titular;
	private Date dataDecont;
	private Date dataDecontDeLa;
	private Date dataDecontPanaLa;

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public Date getDataDecont() {
		return dataDecont;
	}
	
	public void setDataDecont(Date dataDecont) {
		this.dataDecont = dataDecont;
	}
	
	public Date getDataDecontDeLa() {
		return dataDecontDeLa;
	}
	
	public void setDataDecontDeLa(Date dataDecontDeLa) {
		this.dataDecontDeLa = dataDecontDeLa;
	}
	
	public Date getDataDecontPanaLa() {
		return dataDecontPanaLa;
	}
	
	public void setDataDecontPanaLa(Date dataDecontPanaLa) {
		this.dataDecontPanaLa = dataDecontPanaLa;
	}
	
}
