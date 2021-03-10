package ro.cloudSoft.cloudDoc.domain.alteDeconturi;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "ALTE_DECONTURI", uniqueConstraints = @UniqueConstraint(columnNames = { "NUMAR_DECONT" }))
public class AlteDeconturi {

	private Long id;
	private String titularDecont;
	private String numarDecont;
	private Date dataDecont;
	private BigDecimal avansPrimit;
	private TipAvansPrimitEnum tipAvansPrimit;
	private List<AlteDeconturiCheltuiala> cheltuieli;
	private BigDecimal totalCheltuieli;
	private BigDecimal totalDeIncasatRestituit;
	private boolean anulat;
	private String motivAnulare;
	private boolean finalizat;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TITULAR_DECONT")
	public String getTitularDecont() {
		return titularDecont;
	}

	public void setTitularDecont(String titularDecont) {
		this.titularDecont = titularDecont;
	}

	@Column(name = "NUMAR_DECONT")
	public String getNumarDecont() {
		return numarDecont;
	}

	public void setNumarDecont(String numarDecont) {
		this.numarDecont = numarDecont;
	}

	@Column(name = "DATA_DECONT")
	public Date getDataDecont() {
		return dataDecont;
	}

	public void setDataDecont(Date dataDecont) {
		this.dataDecont = dataDecont;
	}

	@Column(name = "AVANS_PRIMIT")
	public BigDecimal getAvansPrimit() {
		return avansPrimit;
	}

	public void setAvansPrimit(BigDecimal avansPrimit) {
		this.avansPrimit = avansPrimit;
	}
	
	@Column(name = "TIP_AVANS_PRIMIT")
	@Enumerated(value = EnumType.STRING)
	public TipAvansPrimitEnum getTipAvansPrimit() {
		return tipAvansPrimit;
	}

	public void setTipAvansPrimit(TipAvansPrimitEnum tipAvansPrimit) {
		this.tipAvansPrimit = tipAvansPrimit;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "alteDeconturi")
	public List<AlteDeconturiCheltuiala> getCheltuieli() {
		return cheltuieli;
	}

	public void setCheltuieli(List<AlteDeconturiCheltuiala> cheltuieli) {
		this.cheltuieli = cheltuieli;
	}

	@Column(name = "TOTAL_CHELTUIELI")
	public BigDecimal getTotalCheltuieli() {
		return totalCheltuieli;
	}

	public void setTotalCheltuieli(BigDecimal totalCheltuieli) {
		this.totalCheltuieli = totalCheltuieli;
	}

	@Column(name = "TOTAL_DE_INCASAT_RESTITUIT")
	public BigDecimal getTotalDeIncasatRestituit() {
		return totalDeIncasatRestituit;
	}

	public void setTotalDeIncasatRestituit(BigDecimal totalDeIncasatRestituit) {
		this.totalDeIncasatRestituit = totalDeIncasatRestituit;
	}

	@Column(name = "ANULAT")
	public boolean isAnulat() {
		return anulat;
	}

	public void setAnulat(boolean anulat) {
		this.anulat = anulat;
	}

	@Column(name = "MOTIV_ANULARE")
	public String getMotivAnulare() {
		return motivAnulare;
	}

	public void setMotivAnulare(String motivAnulare) {
		this.motivAnulare = motivAnulare;
	}

	@Column(name = "FINALIZAT")
	public boolean isFinalizat() {
		return finalizat;
	}

	public void setFinalizat(boolean finalizat) {
		this.finalizat = finalizat;
	}
	
	public static enum TipAvansPrimitEnum {
		CARD,
		NUMERAR
	}
}
