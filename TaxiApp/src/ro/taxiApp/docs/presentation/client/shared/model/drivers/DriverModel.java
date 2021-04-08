package ro.taxiApp.docs.presentation.client.shared.model.drivers;

import java.util.Date;

import ro.taxiApp.docs.presentation.client.shared.model.organization.UserModel;

public class DriverModel {

	private Long id;
	private Date birthDate;
	private String licenceNumber;
	private Date expiryDate;
	private Boolean available;
	private Date lastMedExam;
	
	private UserModel user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getLicenceNumber() {
		return licenceNumber;
	}

	public void setLicenceNumber(String licenceNumber) {
		this.licenceNumber = licenceNumber;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public Date getLastMedExam() {
		return lastMedExam;
	}

	public void setLastMedExam(Date lastMedExam) {
		this.lastMedExam = lastMedExam;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}
	
	
	
}
