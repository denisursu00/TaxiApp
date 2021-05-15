package ro.taxiApp.docs.domain.rides;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ro.taxiApp.docs.domain.clients.Client;
import ro.taxiApp.docs.domain.drivers.Driver;
import ro.taxiApp.docs.domain.organization.User;

@Entity
@Table(
	name = "RIDE"
)
public class Ride {

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
	
	private Client client;
	private Driver driver;
	private User dispatcher;
	private PaymentType paymentType;
	private String observations;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time")
	public Date getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time")
	public Date getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name = "start_location")
	public String getStartLocation() {
		return startLocation;
	}
	
	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}
	
	@Column(name = "end_location")
	public String getEndLocation() {
		return endLocation;
	}
	
	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}
	
	@Column(name = "start_adress")
	public String getStartAdress() {
		return startAdress;
	}
	
	public void setStartAdress(String startAdress) {
		this.startAdress = startAdress;
	}
	
	@Column(name = "end_adress")
	public String getEndAdress() {
		return endAdress;
	}
	
	public void setEndAdress(String endAdress) {
		this.endAdress = endAdress;
	}
	
	@Column(name = "price")
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	@Column(name = "canceled")
	public Boolean getCanceled() {
		return canceled;
	}
	
	public void setCanceled(Boolean canceled) {
		this.canceled = canceled;
	}
	
	@Column(name = "finished")
	public Boolean getFinished() {
		return finished;
	}
	
	public void setFinished(Boolean finished) {
		this.finished = finished;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "driver_id", referencedColumnName = "id", nullable = true)
	public Driver getDriver() {
		return driver;
	}
	
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dispatcher_id", referencedColumnName = "id", nullable = true)
	public User getDispatcher() {
		return dispatcher;
	}
	
	public void setDispatcher(User dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	@Column(name = "observations")
	public String getObservations() {
		return observations;
	}
	
	public void setObservations(String observations) {
		this.observations = observations;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_type", nullable = false)
	public PaymentType getPaymentType() {
		return paymentType;
	}
	
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	
	
	
}
