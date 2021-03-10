package ro.cloudSoft.cloudDoc.domain.notifications;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ro.cloudSoft.cloudDoc.domain.arb.ReprezentantiComisieSauGL;
import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;

@Entity
@Table( name = "NOTIFICARE_NR_ORGANISME_BUGETATE")
public class NotificareNrOrganismeBugetate {
	
	private Long id;
	private Integer an;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "an")
	public Integer getAn() {
		return an;
	}

	public void setAn(Integer an) {
		this.an = an;
	}

}
