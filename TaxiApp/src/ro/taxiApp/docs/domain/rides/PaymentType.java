package ro.taxiApp.docs.domain.rides;

public enum PaymentType {

	CASH("Cash"),
	CARD("Card");
	
	private String label;
	
	private PaymentType(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
}
