package ro.taxiApp.docs.presentation.client.shared.model.rides;

import java.math.BigDecimal;
import java.util.Date;

public class RideModel {

	private Long id;
	private Date startTime;
	private Date endTime;
	private String startLocation;
	private String endLocation;
	private String startAdress;
	private String endAdress;
	private BigDecimal price;
	private Boolean canceled;
	private Boolean finished;
	
	private Long clientId;
	private Long driverId;
	private Long dispatcherId;
	private String paymentType;
	private String observations;
	
	private Long carCategoryId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getStartLocation() {
		return startLocation;
	}
	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}
	public String getEndLocation() {
		return endLocation;
	}
	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}
	public String getStartAdress() {
		return startAdress;
	}
	public void setStartAdress(String startAdress) {
		this.startAdress = startAdress;
	}
	public String getEndAdress() {
		return endAdress;
	}
	public void setEndAdress(String endAdress) {
		this.endAdress = endAdress;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Boolean getCanceled() {
		return canceled;
	}
	public void setCanceled(Boolean canceled) {
		this.canceled = canceled;
	}
	public Boolean getFinished() {
		return finished;
	}
	public void setFinished(Boolean finished) {
		this.finished = finished;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public Long getClientId() {
		return clientId;
	}
	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}
	public Long getDriverId() {
		return driverId;
	}
	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}
	public Long getDispatcherId() {
		return dispatcherId;
	}
	public void setDispatcherId(Long dispatcherId) {
		this.dispatcherId = dispatcherId;
	}
	public String getObservations() {
		return observations;
	}
	public void setObservations(String observations) {
		this.observations = observations;
	}
	public Long getCarCategoryId() {
		return carCategoryId;
	}
	public void setCarCategoryId(Long carCategoryId) {
		this.carCategoryId = carCategoryId;
	}
	
}
