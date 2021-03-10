package ro.cloudSoft.cloudDoc.domain.alteDeconturi;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ALTE_DECONTURI_CHELTUIALA")
public class AlteDeconturiCheltuiala {

	private Long id;
	private AlteDeconturi alteDeconturi;
	private TipDocumentJustificativ tipDocumentJustificativ;
	private String explicatie;
	private String numarDocumentJustificativ;
	private Date dataDocumentJustificativ;
	private BigDecimal valoareCheltuiala;
	private List<AlteDeconturiCheltuialaAtasament> atasamente;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ALTE_DECONTURI_ID")
	public AlteDeconturi getAlteDeconturi() {
		return alteDeconturi;
	}

	public void setAlteDeconturi(AlteDeconturi alteDeconturi) {
		this.alteDeconturi = alteDeconturi;
	}

	@Column(name = "TIP_DOCUMENT_JUSTIFICATIV")
	@Enumerated(value = EnumType.STRING)
	public TipDocumentJustificativ getTipDocumentJustificativ() {
		return tipDocumentJustificativ;
	}

	public void setTipDocumentJustificativ(TipDocumentJustificativ tipDocumentJustificativ) {
		this.tipDocumentJustificativ = tipDocumentJustificativ;
	}

	@Column(name = "EXPLICATIE")
	public String getExplicatie() {
		return explicatie;
	}

	public void setExplicatie(String explicatie) {
		this.explicatie = explicatie;
	}

	@Column(name = "NUMAR_DOCUMENT_JUSTIFICATIV")
	public String getNumarDocumentJustificativ() {
		return numarDocumentJustificativ;
	}

	public void setNumarDocumentJustificativ(String numarDocumentJustificativ) {
		this.numarDocumentJustificativ = numarDocumentJustificativ;
	}

	@Column(name = "DATA_DOCUMENT_JUSTIFICATIV")
	public Date getDataDocumentJustificativ() {
		return dataDocumentJustificativ;
	}

	public void setDataDocumentJustificativ(Date dataDocumentJustificativ) {
		this.dataDocumentJustificativ = dataDocumentJustificativ;
	}

	@Column(name = "VALOARE_CHELTUIALA")
	public BigDecimal getValoareCheltuiala() {
		return valoareCheltuiala;
	}

	public void setValoareCheltuiala(BigDecimal valoareCheltuiala) {
		this.valoareCheltuiala = valoareCheltuiala;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch= FetchType.LAZY, mappedBy = "alteDeconturiCheltuiala")
	public List<AlteDeconturiCheltuialaAtasament> getAtasamente() {
		return atasamente;
	}

	public void setAtasamente(List<AlteDeconturiCheltuialaAtasament> atasamente) {
		this.atasamente = atasamente;
	}

	
	public static enum TipDocumentJustificativ {
		BON_FISCAL,
		FACTURA,
		CHITANTA,
		ALTELE
	}
}
