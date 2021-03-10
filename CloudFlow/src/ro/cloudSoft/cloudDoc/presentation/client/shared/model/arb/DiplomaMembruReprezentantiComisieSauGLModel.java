package ro.cloudSoft.cloudDoc.presentation.client.shared.model.arb;

public class DiplomaMembruReprezentantiComisieSauGLModel {
	
	private Long id;
	private String denumire;
	private String an;
	private String observatii;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDenumire() {
		return denumire;
	}
	public void setDenumire(String denumire) {
		this.denumire = denumire;
	}
	public String getAn() {
		return an;
	}
	public void setAn(String an) {
		this.an = an;
	}
	public String getObservatii() {
		return observatii;
	}
	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}	
}
