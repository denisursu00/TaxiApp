package ro.cloudSoft.cloudDoc.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Objects;

/**
 * 
 */
//@Embeddable
public class PendingPontajForConcediuPk implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String solicitantEmail;
	private String solicitantOrganizationUnitName;
	private Date day;
	
	@Column(name = "solicitant_email", nullable = false, length = 200)
	public String getSolicitantEmail() {
		return solicitantEmail;
	}
	
	@Column(name = "solicitant_org_unit_name", nullable = false, length = 200)
	public String getSolicitantOrganizationUnitName() {
		return solicitantOrganizationUnitName;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "day", nullable = false)
	public Date getDay() {
		return day;
	}
	
	public void setSolicitantEmail(String solicitantEmail) {
		this.solicitantEmail = solicitantEmail;
	}
	public void setSolicitantOrganizationUnitName(String solicitantOrganizationUnitName) {
		this.solicitantOrganizationUnitName = solicitantOrganizationUnitName;
	}
	public void setDay(Date day) {
		this.day = day;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof PendingPontajForConcediuPk)) {
			return false;
		}
		
		PendingPontajForConcediuPk other = (PendingPontajForConcediuPk) obj;
		
		String solicitantEmail = getSolicitantEmail();
		String solicitantEmailInLowerCase = StringUtils.lowerCase(solicitantEmail);
		
		String otherSolicitantEmail = other.getSolicitantEmail();
		String otherSolicitantEmailInLowerCase = StringUtils.lowerCase(otherSolicitantEmail);
		
		return (
			Objects.equal(solicitantEmailInLowerCase, otherSolicitantEmailInLowerCase) &&
			Objects.equal(getSolicitantOrganizationUnitName(), other.getSolicitantOrganizationUnitName()) &&
			Objects.equal(getDay(), other.getDay())
		);
	}
	
	@Override
	public int hashCode() {
		
		String solicitantEmail = getSolicitantEmail();
		String solicitantEmailInLowerCase = StringUtils.lowerCase(solicitantEmail);
		
		return Objects.hashCode(solicitantEmailInLowerCase, getSolicitantOrganizationUnitName(), getDay());
	}
}