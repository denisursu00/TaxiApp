package ro.cloudSoft.cloudDoc.domain.registruIntrariIesiri;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "REGISTRU_INTRARI_IESIRI_LEG")
public class RegistruIntrariIesiriLegatura {

	public Long id;
	public Long registruIntrariId;
	public Long registruIesiriId;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "registru_intrari_id")
	public Long getRegistruIntrariId() {
		return registruIntrariId;
	}

	public void setRegistruIntrariId(Long registruIntrariId) {
		this.registruIntrariId = registruIntrariId;
	}

	@Column(name = "registru_iesiri_id")
	public Long getRegistruIesiriId() {
		return registruIesiriId;
	}

	public void setRegistruIesiriId(Long registruIesiriId) {
		this.registruIesiriId = registruIesiriId;
	}

}
