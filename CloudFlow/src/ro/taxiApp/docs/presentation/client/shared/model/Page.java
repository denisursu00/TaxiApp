package ro.taxiApp.docs.presentation.client.shared.model;

import java.util.ArrayList;
import java.util.List;

public class Page<I> {
	
	private List<I> items;
	private int totalItems;
	private int pageSize;
	
	public List<I> getItems() {
		return items;
	}
	public void setItems(List<I> items) {
		this.items = items;
	}
	public int getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
