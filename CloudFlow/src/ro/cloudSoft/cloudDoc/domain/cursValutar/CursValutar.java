package ro.cloudSoft.cloudDoc.domain.cursValutar;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CURS_VALUTAR")
public class CursValutar {
	
	private Long id;
	private BigDecimal eur;
	private BigDecimal usd;
	private Date data;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="eur")
	public BigDecimal getEur() {
		return eur;
	}

	public void setEur(BigDecimal eur) {
		this.eur = eur;
	}

	@Column(name="usd")
	public BigDecimal getUsd() {
		return usd;
	}

	public void setUsd(BigDecimal usd) {
		this.usd = usd;
	}

	@Column(name="data")
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
}
