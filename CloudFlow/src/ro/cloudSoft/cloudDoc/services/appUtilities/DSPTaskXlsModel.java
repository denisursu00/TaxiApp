package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.util.Date;
import java.util.List;

public class DSPTaskXlsModel {

	private String abreviereProiect;
	
	private String numeTask;
	private String descriere;
	private PrioritateTask prioritate;
	private Date dataInceput;
	private Date dataSfarsit;
	private String responsabilActivitate;
	private String participareLa;
	private String explicatii;
	private StatusTask status;
	private Date dataFinalizare;
	private List<String> numeAtasamente;
	
	public static enum PrioritateTask {
		LOW, NORMAL, HIGH
	}
	
	public static enum StatusTask {
		IN_PROGRESS, FINALIZED
	}

	public String getAbreviereProiect() {
		return abreviereProiect;
	}

	public void setAbreviereProiect(String abreviereProiect) {
		this.abreviereProiect = abreviereProiect;
	}

	public String getNumeTask() {
		return numeTask;
	}

	public void setNumeTask(String numeTask) {
		this.numeTask = numeTask;
	}

	public String getDescriere() {
		return descriere;
	}

	public void setDescriere(String descriere) {
		this.descriere = descriere;
	}

	public PrioritateTask getPrioritate() {
		return prioritate;
	}

	public void setPrioritate(PrioritateTask prioritate) {
		this.prioritate = prioritate;
	}

	public Date getDataInceput() {
		return dataInceput;
	}

	public void setDataInceput(Date dataInceput) {
		this.dataInceput = dataInceput;
	}

	public Date getDataSfarsit() {
		return dataSfarsit;
	}

	public void setDataSfarsit(Date dataSfarsit) {
		this.dataSfarsit = dataSfarsit;
	}

	public String getResponsabilActivitate() {
		return responsabilActivitate;
	}

	public void setResponsabilActivitate(String responsabilActivitate) {
		this.responsabilActivitate = responsabilActivitate;
	}

	public String getParticipareLa() {
		return participareLa;
	}

	public void setParticipareLa(String participareLa) {
		this.participareLa = participareLa;
	}

	public String getExplicatii() {
		return explicatii;
	}

	public void setExplicatii(String explicatii) {
		this.explicatii = explicatii;
	}

	public StatusTask getStatus() {
		return status;
	}

	public void setStatus(StatusTask status) {
		this.status = status;
	}

	public Date getDataFinalizare() {
		return dataFinalizare;
	}

	public void setDataFinalizare(Date dataFinalizare) {
		this.dataFinalizare = dataFinalizare;
	}

	public List<String> getNumeAtasamente() {
		return numeAtasamente;
	}
	
	public void setNumeAtasamente(List<String> numeAtasamente) {
		this.numeAtasamente = numeAtasamente;
	}
}
