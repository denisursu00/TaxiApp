package ro.taxiApp.docs.presentation.client.shared.model.cars;

import java.util.Date;

public class CarModel {

	private Long id;
	private String model;
	private String registrationNumber;
	private Date lastTechControl;
	
	private Long driverId;
	private Long carCategoryId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Date getLastTechControl() {
		return lastTechControl;
	}

	public void setLastTechControl(Date lastTechControl) {
		this.lastTechControl = lastTechControl;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public Long getCarCategoryId() {
		return carCategoryId;
	}

	public void setCarCategoryId(Long carCategoryId) {
		this.carCategoryId = carCategoryId;
	}
	
}
