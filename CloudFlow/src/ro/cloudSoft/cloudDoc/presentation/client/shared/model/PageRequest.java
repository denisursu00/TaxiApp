package ro.cloudSoft.cloudDoc.presentation.client.shared.model;

public class PageRequest<P> {
	
	private P payload;
	private int offset;
	private int limit;
	
	public P getPayload() {
		return payload;
	}
	public void setPayload(P payload) {
		this.payload = payload;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
