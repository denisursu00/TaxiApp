package ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri;

import java.util.Date;

public class RegistruIesiriDestinatarModel {
	
	private Long id;
	private String nume;
	private Long institutieId;
	private String departament;
	private String numarInregistrare;
	private Date dataInregistrare;
	private Long registruIntrariId;
	private String observatii;
	private Long comisieGlId;
	private Long registruIesiriId;
	
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
	public Long getInstitutieId() {
		return institutieId;
	}
	public void setInstitutieId(Long institutieId) {
		this.institutieId = institutieId;
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
	public Long getComisieGlId() {
		return comisieGlId;
	}
	public void setComisieGlId(Long comisieGlId) {
		this.comisieGlId = comisieGlId;
	}
	public Long getRegistruIesiriId() {
		return registruIesiriId;
	}
	public void setRegistruIesiriId(Long registruIesiriId) {
		this.registruIesiriId = registruIesiriId;
	}
}
