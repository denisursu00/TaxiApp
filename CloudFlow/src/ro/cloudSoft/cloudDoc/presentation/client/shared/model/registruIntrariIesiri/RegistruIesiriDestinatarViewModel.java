package ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri;

import java.util.Date;

public class RegistruIesiriDestinatarViewModel {
	
	private Long id;
	private String nume;
	private String departament;
	private String numarInregistrare;
	private Date dataInregistrare;
	private Long registruIntrariId;
	private String observatii;
	private String comisieGl;
	private Long registruIesiriId;
	private String nrInregistrareIntrare;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNume() {
		return nume;
	}
	public void setNume(String nume) {
		this.nume = nume;
	}
	public String getDepartament() {
		return departament;
	}
	public void setDepartament(String departament) {
		this.departament = departament;
	}
	public String getNumarInregistrare() {
		return numarInregistrare;
	}
	public void setNumarInregistrare(String numarInregistrare) {
		this.numarInregistrare = numarInregistrare;
	}
	public Date getDataInregistrare() {
		return dataInregistrare;
	}
	public void setDataInregistrare(Date dataInregistrare) {
		this.dataInregistrare = dataInregistrare;
	}
	public Long getRegistruIntrariId() {
		return registruIntrariId;
	}
	public void setRegistruIntrariId(Long registruIntrariId) {
		this.registruIntrariId = registruIntrariId;
	}
	public String getObservatii() {
		return observatii;
	}
	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}
	public String getComisieGl() {
		return comisieGl;
	}
	public void setComisieGl(String comisieGl) {
		this.comisieGl = comisieGl;
	}
	public Long getRegistruIesiriId() {
		return registruIesiriId;
	}
	public void setRegistruIesiriId(Long registruIesiriId) {
		this.registruIesiriId = registruIesiriId;
	}
	public String getNrInregistrareIntrare() {
		return nrInregistrareIntrare;
	}
	public void setNrInregistrareIntrare(String nrInregistrareIntrare) {
		this.nrInregistrareIntrare = nrInregistrareIntrare;
	}
}
