package ro.cloudSoft.cloudDoc.presentation.client.client;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;

/**
 * Contine parametri pentru configurarea afisarii paginate a listei de documente.
 * 
 * 
 */
public class PagedDocumentsLoadConfig extends BasePagingLoadConfig {

	private static final long serialVersionUID = 1L;

	private String documentLocationRealName;
	private String parentFolderId;
	private boolean documentsOfSameType;
	
	@SuppressWarnings("unused")
	private PagedDocumentsLoadConfig() {}
	
	public PagedDocumentsLoadConfig(String documentLocationRealName, String parentFolderId, boolean areDocumentsOfSameType, int offset, int limit) {
		this.documentLocationRealName = documentLocationRealName;
		this.parentFolderId = parentFolderId;
		this.documentsOfSameType = areDocumentsOfSameType;
		setOffset(offset);
		setLimit(limit);
	}
	
	public String getDocumentLocationRealName() {
		return documentLocationRealName;
	}
	public String getParentFolderId() {
		return parentFolderId;
	}
	public boolean isDocumentsOfSameType() {
		return documentsOfSameType;
	}
}