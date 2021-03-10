package ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri;

import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.registruIntrariIesiri.RegistruIntrariModel.RaspunsuriBanciCuPropuneriEnum;

public class RegistruIntrariFilter {
	
	private Integer year;
	private List<Integer> selectedMonths;
	private String registrationNumber;
	private Date registrationDate;
	private List<Long> senderIds;
	private String senderDepartment;
	private String senderDocumentNr;
	private Date senderDocumentDate;
	private List<Long> documentTypeIds;
	private String documentTypeCode;
	private Boolean isMailed;
	private String content;
	private Integer numberOfPages;
	private Integer numberOfAnnexes;
	private String assignedUser;
	private String committeeWgName;
	private String projectName;
	private String subactivityName;
	private Boolean isAwaitingResponse;
	private Date responseDeadline;
	private String iesireRegistrationNumber;
	private String remarks;
	private List<RaspunsuriBanciCuPropuneriEnum> bankResponseProposal;

	private Integer nrZileIntrareEmitent;
	private Integer nrZileRaspunsIntrare;
	private Integer nrZileRaspunsEmitent;
	private Integer nrZileTermenDataRaspuns;
	
	private Boolean isCanceled;
	private String cancellationReason;
	private Boolean isFinished;
	
	private String sortField;
	private Boolean isAscendingOrder;
	
	// specific filtrarii din iesiri
	private Long destinatarId;
	private String numeDestinatar;
	private Long tipDocumentIdDestinatar;
	
	// pagination
	private Integer offset;
	private Integer pageSize;
	
	public Integer getYear() {
		return year;
	}
	
	public void setYear(Integer year) {
		this.year = year;
	}
	
	public Long getDestinatarId() {
		return destinatarId;
	}
	
	public void setDestinatarId(Long destinatarId) {
		this.destinatarId = destinatarId;
	}
	
	public String getNumeDestinatar() {
		return numeDestinatar;
	}
	
	public void setNumeDestinatar(String numeDestinatar) {
		this.numeDestinatar = numeDestinatar;
	}

	public List<Integer> getSelectedMonths() {
		return selectedMonths;
	}

	public void setSelectedMonths(List<Integer> selectedMonths) {
		this.selectedMonths = selectedMonths;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	

	public List<Long> getSenderIds() {
		return senderIds;
	}

	public void setSenderIds(List<Long> senderIds) {
		this.senderIds = senderIds;
	}

	public String getSenderDepartment() {
		return senderDepartment;
	}

	public void setSenderDepartment(String senderDepartment) {
		this.senderDepartment = senderDepartment;
	}

	public String getSenderDocumentNr() {
		return senderDocumentNr;
	}

	public void setSenderDocumentNr(String senderDocumentNr) {
		this.senderDocumentNr = senderDocumentNr;
	}

	public Date getSenderDocumentDate() {
		return senderDocumentDate;
	}

	public void setSenderDocumentDate(Date senderDocumentDate) {
		this.senderDocumentDate = senderDocumentDate;
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

	public Boolean getIsMailed() {
		return isMailed;
	}

	public void setIsMailed(Boolean isMailed) {
		this.isMailed = isMailed;
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

	public String getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}

	public String getCommitteeWgName() {
		return committeeWgName;
	}

	public void setCommitteeWgName(String committeeWgName) {
		this.committeeWgName = committeeWgName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Boolean getIsAwaitingResponse() {
		return isAwaitingResponse;
	}

	public void setIsAwaitingResponse(Boolean isAwaitingResponse) {
		this.isAwaitingResponse = isAwaitingResponse;
	}

	public Date getResponseDeadline() {
		return responseDeadline;
	}

	public void setResponseDeadline(Date responseDeadline) {
		this.responseDeadline = responseDeadline;
	}

	public String getIesireRegistrationNumber() {
		return iesireRegistrationNumber;
	}

	public void setIesireRegistrationNumber(String iesireRegistrationNumber) {
		this.iesireRegistrationNumber = iesireRegistrationNumber;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public Integer getNrZileIntrareEmitent() {
		return nrZileIntrareEmitent;
	}

	public void setNrZileIntrareEmitent(Integer nrZileIntrareEmitent) {
		this.nrZileIntrareEmitent = nrZileIntrareEmitent;
	}

	public Integer getNrZileRaspunsIntrare() {
		return nrZileRaspunsIntrare;
	}

	public void setNrZileRaspunsIntrare(Integer nrZileRaspunsIntrare) {
		this.nrZileRaspunsIntrare = nrZileRaspunsIntrare;
	}

	public Integer getNrZileRaspunsEmitent() {
		return nrZileRaspunsEmitent;
	}

	public void setNrZileRaspunsEmitent(Integer nrZileRaspunsEmitent) {
		this.nrZileRaspunsEmitent = nrZileRaspunsEmitent;
	}

	public Integer getNrZileTermenDataRaspuns() {
		return nrZileTermenDataRaspuns;
	}

	public void setNrZileTermenDataRaspuns(Integer nrZileTermenDataRaspuns) {
		this.nrZileTermenDataRaspuns = nrZileTermenDataRaspuns;
	}

	public Boolean getIsCanceled() {
		return isCanceled;
	}

	public void setIsCanceled(Boolean isCanceled) {
		this.isCanceled = isCanceled;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public Boolean getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(Boolean isFinished) {
		this.isFinished = isFinished;
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

	public Long getTipDocumentIdDestinatar() {
		return tipDocumentIdDestinatar;
	}

	public void setTipDocumentIdDestinatar(Long tipDocumentIdDestinatar) {
		this.tipDocumentIdDestinatar = tipDocumentIdDestinatar;
	}

	public List<RaspunsuriBanciCuPropuneriEnum> getBankResponseProposal() {
		return bankResponseProposal;
	}

	public void setBankResponseProposal(List<RaspunsuriBanciCuPropuneriEnum> bankResponseProposal) {
		this.bankResponseProposal = bankResponseProposal;
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

	public String getSubactivityName() {
		return subactivityName;
	}

	public void setSubactivityName(String subactivityName) {
		this.subactivityName = subactivityName;
	}
	
}
