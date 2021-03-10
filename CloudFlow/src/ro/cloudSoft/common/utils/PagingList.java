package ro.cloudSoft.common.utils;

import java.util.List;

/**
 * O lista folosita pentru paginare
 * 
 * 
 */
public class PagingList<T> {

	private final int totalCount;
	private final int offset;
	private final List<T> elements;
	
	public PagingList(int totalCount, int offset, List<T> elements) {
		this.totalCount = totalCount;
		this.offset = offset;
		this.elements = elements;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	public int getOffset() {
		return offset;
	}
	public List<T> getElements() {
		return elements;
	}
}