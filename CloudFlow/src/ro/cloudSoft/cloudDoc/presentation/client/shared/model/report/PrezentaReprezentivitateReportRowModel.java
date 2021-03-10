package ro.cloudSoft.cloudDoc.presentation.client.shared.model.report;

public class PrezentaReprezentivitateReportRowModel {
	private String banca;
	private String cod;
	private int level0;
	private int level1;
	private int level2;
	private int level3;
	private int level3Plus;
	private int inAfaraNom;
	private int totalPrezenta;
	private double coeficientStructural;
	private double notaFinalaPrezenta;	
	
	private RaspunsuriBanciReportRowModel raspunsuriBanci;
	private double notaFinalaRaspunsuriBanci;
	
	private double notaFinalaBanca;
	private double rankNotaBanca;
	
	public String getBanca() {
		return banca;
	}
	public void setBanca(String banca) {
		this.banca = banca;
	}
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	public int getLevel0() {
		return level0;
	}
	public void setLevel0(int level0) {
		this.level0 = level0;
	}
	public int getLevel1() {
		return level1;
	}
	public void setLevel1(int level1) {
		this.level1 = level1;
	}
	public int getLevel2() {
		return level2;
	}
	public void setLevel2(int level2) {
		this.level2 = level2;
	}
	public int getLevel3() {
		return level3;
	}
	public void setLevel3(int level3) {
		this.level3 = level3;
	}
	public int getLevel3Plus() {
		return level3Plus;
	}
	public void setLevel3Plus(int level3Plus) {
		this.level3Plus = level3Plus;
	}
	public int getInAfaraNom() {
		return inAfaraNom;
	}
	public void setInAfaraNom(int inAfaraNom) {
		this.inAfaraNom = inAfaraNom;
	}
	public int getTotalPrezenta() {
		return totalPrezenta;
	}
	public void setTotalPrezenta(int totalPrezenta) {
		this.totalPrezenta = totalPrezenta;
	}
	public double getCoeficientStructural() {
		return coeficientStructural;
	}
	public void setCoeficientStructural(double coeficientStructural) {
		this.coeficientStructural = coeficientStructural;
	}
	public double getNotaFinalaPrezenta() {
		return notaFinalaPrezenta;
	}
	public void setNotaFinalaPrezenta(double notaFinalaPrezenta) {
		this.notaFinalaPrezenta = notaFinalaPrezenta;
	}
	public RaspunsuriBanciReportRowModel getRaspunsuriBanci() {
		return raspunsuriBanci;
	}
	public void setRaspunsuriBanci(RaspunsuriBanciReportRowModel raspunsuriBanci) {
		this.raspunsuriBanci = raspunsuriBanci;
	}
	public double getNotaFinalaBanca() {
		return notaFinalaBanca;
	}
	public void setNotaFinalaBanca(double notaFinalaBanca) {
		this.notaFinalaBanca = notaFinalaBanca;
	}
	public double getRankNotaBanca() {
		return rankNotaBanca;
	}
	public void setRankNotaBanca(double rankNotaBanca) {
		this.rankNotaBanca = rankNotaBanca;
	}
	public double getNotaFinalaRaspunsuriBanci() {
		return notaFinalaRaspunsuriBanci;
	}
	public void setNotaFinalaRaspunsuriBanci(double notaFinalaRaspunsuriBanci) {
		this.notaFinalaRaspunsuriBanci = notaFinalaRaspunsuriBanci;
	}

}
