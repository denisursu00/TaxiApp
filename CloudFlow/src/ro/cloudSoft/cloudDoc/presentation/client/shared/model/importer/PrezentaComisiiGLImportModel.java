package ro.cloudSoft.cloudDoc.presentation.client.shared.model.importer;

import java.util.List;

public class PrezentaComisiiGLImportModel {
	private int rowIndex;
	private String intiatorPrezenta;
	private String sedinta;
	private String denumireComisieGl;
	private String dataInceput;
	private String dataSfarsit;
	private String responsabil;
	private List<InformatiiPrezentaComisiiGLImportModel> informatiiParticipanti;
	public String getIntiatorPrezenta() {
		return intiatorPrezenta;
	}
	public void setIntiatorPrezenta(String intiatorPrezenta) {
		this.intiatorPrezenta = intiatorPrezenta;
	}
	public String getSedinta() {
		return sedinta;
	}
	public void setSedinta(String sedinta) {
		this.sedinta = sedinta;
	}
	public String getDenumireComisieGl() {
		return denumireComisieGl;
	}
	public void setDenumireComisieGl(String denumireComisieGl) {
		this.denumireComisieGl = denumireComisieGl;
	}
	public String getDataInceput() {
		return dataInceput;
	}
	public void setDataInceput(String dataInceput) {
		this.dataInceput = dataInceput;
	}
	public String getDataSfarsit() {
		return dataSfarsit;
	}
	public void setDataSfarsit(String dataSfarsit) {
		this.dataSfarsit = dataSfarsit;
	}
	public String getResponsabil() {
		return responsabil;
	}
	public void setResponsabil(String responsabil) {
		this.responsabil = responsabil;
	}
	public List<InformatiiPrezentaComisiiGLImportModel> getInformatiiParticipanti() {
		return informatiiParticipanti;
	}
	public void setInformatiiParticipanti(List<InformatiiPrezentaComisiiGLImportModel> informatiiParticipanti) {
		this.informatiiParticipanti = informatiiParticipanti;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataInceput == null) ? 0 : dataInceput.trim().hashCode());
		result = prime * result + ((dataSfarsit == null) ? 0 : dataSfarsit.trim().hashCode());
		result = prime * result + ((denumireComisieGl == null) ? 0 : denumireComisieGl.trim().hashCode());
		result = prime * result + ((intiatorPrezenta == null) ? 0 : intiatorPrezenta.trim().hashCode());
		result = prime * result + ((responsabil == null) ? 0 : responsabil.trim().hashCode());
		result = prime * result + ((sedinta == null) ? 0 : sedinta.trim().hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrezentaComisiiGLImportModel other = (PrezentaComisiiGLImportModel) obj;
		if (dataInceput == null) {
			if (other.dataInceput != null)
				return false;
		} else if (!dataInceput.trim().equals(other.dataInceput.trim()))
			return false;
		if (dataSfarsit == null) {
			if (other.dataSfarsit != null)
				return false;
		} else if (!dataSfarsit.trim().equals(other.dataSfarsit.trim()))
			return false;
		if (denumireComisieGl == null) {
			if (other.denumireComisieGl != null)
				return false;
		} else if (!denumireComisieGl.trim().equals(other.denumireComisieGl.trim()))
			return false;
//		if (informatiiParticipanti == null) {
//			if (other.informatiiParticipanti != null)
//				return false;
//		} else if (!informatiiParticipanti.equals(other.informatiiParticipanti))
//			return false;
		if (intiatorPrezenta == null) {
			if (other.intiatorPrezenta != null)
				return false;
		} else if (!intiatorPrezenta.trim().equals(other.intiatorPrezenta.trim()))
			return false;
		if (responsabil == null) {
			if (other.responsabil != null)
				return false;
		} else if (!responsabil.trim().equals(other.responsabil.trim()))
			return false;
		if (sedinta == null) {
			if (other.sedinta != null)
				return false;
		} else if (!sedinta.trim().equals(other.sedinta.trim()))
			return false;
		return true;
	}
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	
}
