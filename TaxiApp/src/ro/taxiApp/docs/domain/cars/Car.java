package ro.taxiApp.docs.domain.cars;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ro.taxiApp.docs.domain.drivers.Driver;

@Entity
@Table(
	name = "CAR"
)
public class Car {
	
	private Long id;
	private String model;
	private String registrationNumber;
	private Date lastTechControl;
	
	private Driver driver;
	
	private CarCategory carCategory;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "model")
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Column(name = "reg_number")
	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	@Column(name = "last_tech_control")
	public Date getLastTechControl() {
		return lastTechControl;
	}

	public void setLastTechControl(Date lastTechControl) {
		this.lastTechControl = lastTechControl;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = false)
	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "car_category_id", referencedColumnName = "id", nullable = false)
	public CarCategory getCarCategory() {
		return carCategory;
	}

	public void setCarCategory(CarCategory carCategory) {
		this.carCategory = carCategory;
	}
	
}
