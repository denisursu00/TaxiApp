package ro.cloudSoft.cloudDoc.domain.arb;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(	name="DIPLOME_MEMBRU_RPRZ_COMISIE_GL")
public class DiplomaMembruReprezentantiComisieSauGL {
	
	private Long id;
	private MembruReprezentantiComisieSauGL membru;
	private String denumire;
	private String an;
	private String observatii;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "MEMBRU_ID", nullable = false)
	public MembruReprezentantiComisieSauGL getMembru() {
		return membru;
	}
	
	public void setMembru(MembruReprezentantiComisieSauGL membru) {
		this.membru = membru;
	}
	
	@Column(name = "DENUMIRE", nullable = false)
	public String getDenumire() {
		return denumire;
	}
	
	public void setDenumire(String denumire) {
		this.denumire = denumire;
	}
	
	@Column(name = "AN", nullable = false)
	public String getAn() {
		return an;
	}
	
	public void setAn(String an) {
		this.an = an;
	}
	
	@Column(name = "OBSERVATII")
	public String getObservatii() {
		return observatii;
	}
	
	public void setObservatii(String observatii) {
		this.observatii = observatii;
	}
} 
