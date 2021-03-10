package ro.cloudSoft.cloudDoc.domain.nomenclator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "NOMENCLATOR_UI_ATTRIBUTE")
public class NomenclatorUiAttribute {
	
	private Long id;
	private int uiOrder;
	private Nomenclator nomenclator;
	private NomenclatorAttribute attribute;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ui_order")
	public int getUiOrder() {
		return uiOrder;
	}

	public void setUiOrder(int uiOrder) {
		this.uiOrder = uiOrder;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nomenclator_id", updatable = false)
	public Nomenclator getNomenclator() {
		return nomenclator;
	}

	public void setNomenclator(Nomenclator nomenclator) {
		this.nomenclator = nomenclator;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attribute_id", updatable = false)
	public NomenclatorAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(NomenclatorAttribute attribute) {
		this.attribute = attribute;
	}
}
