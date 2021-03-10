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
@Table(name="NOMENCLATOR_VALIDATION")
public class NomenclatorValidation {
	
	private Long id;
	private Nomenclator nomenclator;
	private String name;
	private String conditionExpression;
	private String message;
	private Integer validationOrder;	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NOMENCLATOR_ID")
	public Nomenclator getNomenclator() {
		return nomenclator;
	}
	public void setNomenclator(Nomenclator nomenclator) {
		this.nomenclator = nomenclator;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "CONDITION_EXPRESSION")
	public String getConditionExpression() {
		return conditionExpression;
	}
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}
	
	@Column(name = "MESSAGE")
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Column(name = "VALIDATION_ORDER")
	public Integer getValidationOrder() {
		return validationOrder;
	}
	public void setValidationOrder(Integer validationOrder) {
		this.validationOrder = validationOrder;
	}
}
