package ro.taxiApp.docs.domain.rides;

public enum PaymentType {

	CASH("CASH"),
	CARD("CARD");
	
	private String label;
	
	private PaymentType(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
}
