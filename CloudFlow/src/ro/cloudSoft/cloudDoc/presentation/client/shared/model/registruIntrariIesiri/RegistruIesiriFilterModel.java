package ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri;

import java.util.Date;
import java.util.List;

public class RegistruIesiriFilterModel {
	
	private Integer year;
	private String nrInregistrare;
	private List<Integer> selectedMonths;
	private Boolean isMailed;
	private String developingUser;
	private Boolean isAwaitingResponse;
	private Boolean isCanceled;
	private Boolean isFinished;
	private Date registrationDate;
	private String documentTypeCode;
	private String content;
	private Integer numberOfPages;
	private Integer numberOfAnnexes;
	private String projectName;
	private Date responseDeadline;
	private String cancellationReason;
	private String sortField;
	private Boolean isAscendingOrder;
	private List<Long> documentTypeIds;
	private String subactivityName;
	
	//specific filtrarii pe destinatari
	private String departamentDestinatar;
	private String numarInregistrareDestinatar;
	private Date dataInregistrareDestinatar;
	private Long comisieGlDestinatar;
	private String observatiiDestinatar;
	private List<Long> institutiiIdsDestinatar;
	private String numeDestinatarNou;
	
	// specific filtrarii din intrari
	private Long emitentId;
	private String numeEmitent;
	private Long tipDocumentIntrareId;
	
	// pagination
	private Integer offset;
	private Integer pageSize;
	
	public Integer getYear() {
		return year;
	}
	
	public void setYear(Integer year) {
		this.year = year;
	}
	
	public Long getEmitentId() {
		return emitentId;
	}

	public void setEmitentId(Long emitentId) {
		this.emitentId = emitentId;
	}

	public String getNumeEmitent() {
		return numeEmitent;
	}

	public void setNumeEmitent(String numeEmitent) {
		this.numeEmitent = numeEmitent;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getNrInregistrare() {
		return nrInregistrare;
	}

	public void setNrInregistrare(String nrInregistrare) {
		this.nrInregistrare = nrInregistrare;
	}

	public List<Integer> getSelectedMonths() {
		return selectedMonths;
	}

	public void setSelectedMonths(List<Integer> selectedMonths) {
		this.selectedMonths = selectedMonths;
	}

	public Boolean getIsMailed() {
		return isMailed;
	}

	public void setIsMailed(Boolean isMailed) {
		this.isMailed = isMailed;
	}

	public String getDevelopingUser() {
		return developingUser;
	}

	public void setDevelopingUser(String developingUser) {
		this.developingUser = developingUser;
	}

	public Boolean getIsAwaitingResponse() {
		return isAwaitingResponse;
	}

	public void setIsAwaitingResponse(Boolean isAwaitingResponse) {
		this.isAwaitingResponse = isAwaitingResponse;
	}

	public Boolean getIsCanceled() {
		return isCanceled;
	}

	public void setIsCanceled(Boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	public Boolean getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(Boolean isFinished) {
		this.isFinished = isFinished;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public List<Long> getDocumentTypeIds() {
		return documentTypeIds;
	}

	public void setDocumentTypeIds(List<Long> documentTypeIds) {
		this.documentTypeIds = documentTypeIds;
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public void setDocumentTypeCode(String documentTypeCode) {
		this.documentTypeCode = documentTypeCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public Integer getNumberOfAnnexes() {
		return numberOfAnnexes;
	}

	public void setNumberOfAnnexes(Integer numberOfAnnexes) {
		this.numberOfAnnexes = numberOfAnnexes;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Date getResponseDeadline() {
		return responseDeadline;
	}

	public void setResponseDeadline(Date responseDeadline) {
		this.responseDeadline = responseDeadline;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}
	
	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public Boolean getIsAscendingOrder() {
		return isAscendingOrder;
	}

	public void setIsAscendingOrder(Boolean isAscendingOrder) {
		this.isAscendingOrder = isAscendingOrder;
	}

	public Long getTipDocumentIntrareId() {
		return tipDocumentIntrareId;
	}

	public void setTipDocumentIntrareId(Long tipDocumentIntrareId) {
		this.tipDocumentIntrareId = tipDocumentIntrareId;
	}

	public String getDepartamentDestinatar() {
		return departamentDestinatar;
	}

	public void setDepartamentDestinatar(String departamentDestinatar) {
		this.departamentDestinatar = departamentDestinatar;
	}

	public String getNumarInregistrareDestinatar() {
		return numarInregistrareDestinatar;
	}

	public void setNumarInregistrareDestinatar(String numarInregistrareDestinatar) {
		this.numarInregistrareDestinatar = numarInregistrareDestinatar;
	}

	public Date getDataInregistrareDestinatar() {
		return dataInregistrareDestinatar;
	}

	public void setDataInregistrareDestinatar(Date dataInregistrareDestinatar) {
		this.dataInregistrareDestinatar = dataInregistrareDestinatar;
	}

	public Long getComisieGlDestinatar() {
		return comisieGlDestinatar;
	}

	public void setComisieGlDestinatar(Long comisieGlDestinatar) {
		this.comisieGlDestinatar = comisieGlDestinatar;
	}

	public String getObservatiiDestinatar() {
		return observatiiDestinatar;
	}

	public void setObservatiiDestinatar(String observatiiDestinatar) {
		this.observatiiDestinatar = observatiiDestinatar;
	}

	public List<Long> getInstitutiiIdsDestinatar() {
		return institutiiIdsDestinatar;
	}

	public void setInstitutiiIdsDestinatar(List<Long> institutiiIdsDestinatar) {
		this.institutiiIdsDestinatar = institutiiIdsDestinatar;
	}

	public String getNumeDestinatarNou() {
		return numeDestinatarNou;
	}

	public void setNumeDestinatarNou(String numeDestinatarNou) {
		this.numeDestinatarNou = numeDestinatarNou;
	}

	public String getSubactivityName() {
		return subactivityName;
	}

	public void setSubactivityName(String subactivityName) {
		this.subactivityName = subactivityName;
	}
}
