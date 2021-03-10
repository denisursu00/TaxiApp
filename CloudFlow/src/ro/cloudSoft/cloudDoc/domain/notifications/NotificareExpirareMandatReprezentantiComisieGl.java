package ro.cloudSoft.cloudDoc.domain.notifications;

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
@Table( name = "NOTIFICARE_EXPIRARE_MANDAT_REPRZENTANTI_COMISIE_GL")
public class NotificareExpirareMandatReprezentantiComisieGl {
	
	private Long id;
	private ReprezentantiComisieSauGL reprezentantComisieSauGL;
	private NomenclatorValue persoana;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reprezentant_comisie_gl_id")
	public ReprezentantiComisieSauGL getReprezentantComisieSauGL() {
		return reprezentantComisieSauGL;
	}

	public void setReprezentantComisieSauGL(ReprezentantiComisieSauGL reprezentantComisieSauGL) {
		this.reprezentantComisieSauGL = reprezentantComisieSauGL;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "persoana_id")
	public NomenclatorValue getPersoana() {
		return persoana;
	}

	public void setPersoana(NomenclatorValue persoana) {
		this.persoana = persoana;
	}

}
