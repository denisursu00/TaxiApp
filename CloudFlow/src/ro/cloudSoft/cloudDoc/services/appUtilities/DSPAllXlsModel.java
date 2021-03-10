package ro.cloudSoft.cloudDoc.services.appUtilities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public class DSPAllXlsModel {

	private List<DSPProiectXlsModel> proiecte;
	private List<DSPParticipantXlsModel> participanti;
	private List<DSPTaskXlsModel> taskuri;
	private List<DSPComisieGLXlsModel> comisiiGLImplicate;
	
	public List<DSPParticipantXlsModel> getParticipantiOfProiect(String abreviereProiect) {
		List<DSPParticipantXlsModel> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(participanti)) {
			for (DSPParticipantXlsModel p : participanti) {
				if (p.getAbreviereProiect().equals(abreviereProiect)) {
					result.add(p);
				}
			}
		}
		return result;
	}
	
	public List<DSPComisieGLXlsModel> getComisiiGLImplicateOfProiect(String abreviereProiect) {
		List<DSPComisieGLXlsModel> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(comisiiGLImplicate)) {
			for (DSPComisieGLXlsModel p : comisiiGLImplicate) {
				if (p.getAbreviereProiect().equals(abreviereProiect)) {
					result.add(p);
				}
			}
		}
		return result;
	}
	
	public List<DSPTaskXlsModel> getTaskuriOfProiect(String abreviereProiect) {
		List<DSPTaskXlsModel> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(taskuri)) {
			for (DSPTaskXlsModel p : taskuri) {
				if (p.getAbreviereProiect().equals(abreviereProiect)) {
					result.add(p);
				}
			}
		}
		return result;
	}
	
	public List<DSPProiectXlsModel> getProiecte() {
		return proiecte;
	}
	public void setProiecte(List<DSPProiectXlsModel> proiecte) {
		this.proiecte = proiecte;
	}
	public List<DSPParticipantXlsModel> getParticipanti() {
		return participanti;
	}
	public void setParticipanti(List<DSPParticipantXlsModel> participanti) {
		this.participanti = participanti;
	}
	public List<DSPTaskXlsModel> getTaskuri() {
		return taskuri;
	}
	public void setTaskuri(List<DSPTaskXlsModel> taskuri) {
		this.taskuri = taskuri;
	}
	public List<DSPComisieGLXlsModel> getComisiiGLImplicate() {
		return comisiiGLImplicate;
	}
	public void setComisiiGLImplicate(List<DSPComisieGLXlsModel> comisiiGLImplicate) {
		this.comisiiGLImplicate = comisiiGLImplicate;
	}
	
	@Override
	public String toString() {
		StringBuilder ab = new StringBuilder();
		if (CollectionUtils.isNotEmpty(proiecte)) {
			for (DSPProiectXlsModel p : this.proiecte) {
				ab.append(p.toString());
				ab.append("\n");
			}
		}
		ab.append("\n");
		if (CollectionUtils.isNotEmpty(comisiiGLImplicate)) {
			for (DSPComisieGLXlsModel p : this.comisiiGLImplicate) {
				ab.append(p.toString());
				ab.append("\n");
			}
		}
		ab.append("\n");
		if (CollectionUtils.isNotEmpty(participanti)) {
			for (DSPParticipantXlsModel p : this.participanti) {
				ab.append(p.toString());
				ab.append("\n");
			}
		}
		ab.append("\n");
		if (CollectionUtils.isNotEmpty(taskuri)) {
			for (DSPTaskXlsModel p : this.taskuri) {
				ab.append(p.toString());
				ab.append("\n");
			}
		}
		return ab.toString();
	}
}
