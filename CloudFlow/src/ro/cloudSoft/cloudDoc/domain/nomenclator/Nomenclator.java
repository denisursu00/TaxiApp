package ro.cloudSoft.cloudDoc.domain.nomenclator;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.google.common.collect.Lists;

@Entity
@Table(
	name="NOMENCLATOR",
	uniqueConstraints = @UniqueConstraint(columnNames= { "name" })
)
public class Nomenclator {
	
	private Long id;
	private String name;
	private String code;
	private boolean hidden;
	private boolean allowProcessingValuesFromUI;
	private boolean allowProcessingStructureFromUI;
	private List<NomenclatorAttribute> attributes;
	private List<NomenclatorUiAttribute> uiAttributes;
	private List<NomenclatorValidation> validations = Lists.newLinkedList();
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "code", updatable = false)
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name = "hidden", updatable = false)
	public boolean isHidden() {
		return hidden;
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	@Column(name = "allow_processing_values_from_ui", updatable = false)
	public boolean isAllowProcessingValuesFromUI() {
		return allowProcessingValuesFromUI;
	}
	
	public void setAllowProcessingStructureFromUI(boolean allowProcessingStructureFromUI) {
		this.allowProcessingStructureFromUI = allowProcessingStructureFromUI;
	}
	
	@Column(name = "allow_processing_structure_from_ui", updatable = false)
	public boolean isAllowProcessingStructureFromUI() {
		return allowProcessingStructureFromUI;
	}
	
	public void setAllowProcessingValuesFromUI(boolean allowProcessingValuesFromUI) {
		this.allowProcessingValuesFromUI = allowProcessingValuesFromUI;
	}
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "nomenclator")
	@OrderBy("uiOrder")
	public List<NomenclatorAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<NomenclatorAttribute> attributes) {
		this.attributes = attributes;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "nomenclator")
	@OrderBy("uiOrder")
	public List<NomenclatorUiAttribute> getUiAttributes() {
		return uiAttributes;
	}

	public void setUiAttributes(List<NomenclatorUiAttribute> uiAttributes) {
		this.uiAttributes = uiAttributes;
	}
	
	@OrderBy("validationOrder")
	@OneToMany(cascade = CascadeType.ALL, mappedBy="nomenclator")
	public List<NomenclatorValidation> getValidations() {
		return validations;
	}
	public void setValidations(List<NomenclatorValidation> validations) {
		this.validations = validations;
	}
}
