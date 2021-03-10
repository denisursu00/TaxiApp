package ro.cloudSoft.cloudDoc.domain;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 */
//@Entity
//@Table(name = "pending_pontaje_for_concedii")
public class PendingPontajForConcediu {

	private PendingPontajForConcediuPk id = new PendingPontajForConcediuPk();
	
	public PendingPontajForConcediu() {}
	
	public PendingPontajForConcediu(String solicitantEmail, String solicitantOrganizationUnitName, Date day) {
		setSolicitantEmail(solicitantEmail);
		setSolicitantOrganizationUnitName(solicitantOrganizationUnitName);
		setDay(day);
	}
	
	@EmbeddedId
	public PendingPontajForConcediuPk getId() {
		return id;
	}
	
	public void setId(PendingPontajForConcediuPk id) {
		this.id = id;
	}
	
	@Transient
	public String getSolicitantEmail() {
		return getId().getSolicitantEmail();
	}
	
	@Transient
	public String getSolicitantOrganizationUnitName() {
		return getId().getSolicitantOrganizationUnitName();
	}
	
	@Transient
	public Date getDay() {
		return getId().getDay();
	}

	public void setSolicitantEmail(String solicitantEmail) {
		getId().setSolicitantEmail(solicitantEmail);
	}

	public void setSolicitantOrganizationUnitName(String solicitantOrganizationUnitName) {
		getId().setSolicitantOrganizationUnitName(solicitantOrganizationUnitName);
	}
	
	public void setDay(Date day) {
		getId().setDay(day);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof PendingPontajForConcediu)) {
			return false;
		}
		
		PendingPontajForConcediu other = (PendingPontajForConcediu) obj;
		
		return getId().equals(other.getId());
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}