package ro.cloudSoft.cloudDoc.domain.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ro.cloudSoft.cloudDoc.domain.nomenclator.NomenclatorValue;

@Entity
@Table(name = "USER_NOMENCLATOR_PERSON")
public class UserAndNomenclatorPersonRelation {

	private Long id;
	private User user;
	private NomenclatorValue nomenclatorPerson;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "org_entity_id", nullable = false, unique = true)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "NOMENCLATOR_PERSON_ID", nullable = false, unique = true)
	public NomenclatorValue getNomenclatorPerson() {
		return nomenclatorPerson;
	}	
	public void setNomenclatorPerson(NomenclatorValue nomenclatorPerson) {
		this.nomenclatorPerson = nomenclatorPerson;
	}
}
