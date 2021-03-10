package ro.cloudSoft.cloudDoc.web.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import au.com.bytecode.opencsv.CSVWriter;
import ro.cloudSoft.cloudDoc.core.constants.FormatConstants;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentAdvancedSearchResultsView;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentAdvancedSearchResultsViewsWrapper;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSimpleSearchResultsView;
import ro.cloudSoft.cloudDoc.domain.security.SecurityManager;
import ro.cloudSoft.cloudDoc.security.SecurityManagerHolder;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowExecutionService;
import ro.cloudSoft.cloudDoc.services.bpm.WorkflowService;
import ro.cloudSoft.cloudDoc.services.content.DocumentTypeService;
import ro.cloudSoft.cloudDoc.services.content.SearchService;
import ro.cloudSoft.cloudDoc.services.organization.UserService;
import ro.cloudSoft.common.utils.DependencyInjectionUtils;
import ro.cloudSoft.common.utils.web.HttpServletResponseUtils;

public class DocumentSearchReportController implements Controller, InitializingBean {

	private static final String PARAMETER_VALUE_SEARCH_TYPE_SIMPLE = "simple";
	private static final String PARAMETER_VALUE_SEARCH_TYPE_ADVANCED = "advanced";
	
	private static final String PARAMETER_DOCUMENT_SEARCH_CRITERIA = "documentSearchCriteria";
	private static final String PARAMETER_DOCUMENT_SEARCH_TYPE = "documentSearchType";

	private static final String CSV_CONTENT_TYPE = "application/CSV";
	private static final String CSV_FILE_NAME = "document_search_report.csv";
	
	private static final char CSV_SEPARATOR = ',';
	private static final char CSV_DELIMITER = '"';
	private static final String CSV_NEW_LINE_SEPARATOR = "\r\n";
	
	private static final String COLUMN_NAME_DOCUMENT_NAME = "NUME DOCUMENT";
	private static final String COLUMN_NAME_WORKFLOW_SENDER = "EXPEDITOR";
	private static final String COLUMN_NAME_WORKFLOW_CURRENT_STATE = "STAREA CURENTA";
	private static final String COLUMN_NAME_WORKFLOW_NAME = "FLUX";
	private static final String COLUMN_NAME_DOCUMENT_TYPE = "TIP DOCUMENT";
	private static final String COLUMN_NAME_DOCUMENT_CREATED_DATE = "DATA CREARII";
	private static final String COLUMN_NAME_DOCUMENT_AUTHOR = "AUTOR";
	
	private SearchService searchService;
	private DocumentTypeService documentTypeService;
	private UserService userService;
	private WorkflowService workflowService;
	private WorkflowExecutionService workflowExecutionService;

	@Override
	public void afterPropertiesSet() throws Exception {
		DependencyInjectionUtils.checkRequiredDependencies(
			searchService,
			documentTypeService,
			userService,
			workflowService,
			workflowExecutionService
		);
	}
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String joinedParameters = request.getParameter(PARAMETER_DOCUMENT_SEARCH_CRITERIA);
		DocumentSearchCriteria searchCriteria = new DocumentSearchCriteriaFromParametersBuilder(joinedParameters).build();
		
		SecurityManager userSecurity = SecurityManagerHolder.getSecurityManager();

		String documentSearchType = request.getParameter(PARAMETER_DOCUMENT_SEARCH_TYPE);
		if (documentSearchType.equals(PARAMETER_VALUE_SEARCH_TYPE_SIMPLE)) {			
			List<DocumentSimpleSearchResultsView> simpleSearchResultsViews = searchService.findDocumentsUsingSimpleSearch(searchCriteria, userSecurity);
			writeSimpleSearchReportAsCsv(simpleSearchResultsViews, response);
		} else if (documentSearchType.equals(PARAMETER_VALUE_SEARCH_TYPE_ADVANCED)) {
			DocumentAdvancedSearchResultsViewsWrapper advancedSearchResultsViewsWrapper = searchService.findDocumentsUsingAdvancedSearch(searchCriteria, userSecurity);
			writeAdvancedSearchReportAsCsv(advancedSearchResultsViewsWrapper, response);
		}
		
		return null;
	}
	
	private void writeSimpleSearchReportAsCsv(List<DocumentSimpleSearchResultsView> simpleSearchResultsViews, HttpServletResponse response) throws IOException {

		prepareResponseForCsv(response);

		CSVWriter csvWriter = createCsvWriter(response);
		
		String[] columnHeaders = new String[] {
			COLUMN_NAME_DOCUMENT_NAME,
			COLUMN_NAME_WORKFLOW_SENDER,
			COLUMN_NAME_WORKFLOW_CURRENT_STATE,
			COLUMN_NAME_WORKFLOW_NAME,
			COLUMN_NAME_DOCUMENT_TYPE,
			COLUMN_NAME_DOCUMENT_CREATED_DATE,
			COLUMN_NAME_DOCUMENT_AUTHOR
		};
		csvWriter.writeNext(columnHeaders);
		
		DateFormat dateFormatter = getDateFormatterForReport();
		
		for (DocumentSimpleSearchResultsView simpleSearchResultsView : simpleSearchResultsViews) {
			String[] columnValues = new String[] {
				normalizeColumnValue(simpleSearchResultsView.getDocumentName()),
				normalizeColumnValue(simpleSearchResultsView.getWorkflowSenderDisplayName()),
				normalizeColumnValue(simpleSearchResultsView.getWorkflowCurrentStateName()),
				normalizeColumnValue(simpleSearchResultsView.getWorkflowName()),
				normalizeColumnValue(simpleSearchResultsView.getDocumentTypeName()),
				normalizeColumnValue(dateFormatter.format(simpleSearchResultsView.getDocumentCreatedDate())),
				normalizeColumnValue(simpleSearchResultsView.getDocumentAuthorDisplayName())
			};
			csvWriter.writeNext(columnValues);
		}

		closeCsvWriter(csvWriter);
	}
	
	private void writeAdvancedSearchReportAsCsv(DocumentAdvancedSearchResultsViewsWrapper advancedSearchResultsViewsWrapper, HttpServletResponse response) throws IOException {

		prepareResponseForCsv(response);
		
		CSVWriter csvWriter = createCsvWriter(response);
		
		List<String> columnHeadersAsList = Lists.newArrayList(
			COLUMN_NAME_DOCUMENT_CREATED_DATE,
			COLUMN_NAME_DOCUMENT_AUTHOR,
			COLUMN_NAME_WORKFLOW_CURRENT_STATE
		);
		
		Collection<String> representativemetadataDefinitionLabels = advancedSearchResultsViewsWrapper.getRepresentativeMetadataDefinitionLabelById().values();
		columnHeadersAsList.addAll(representativemetadataDefinitionLabels);
		
		String[] columnHeaders = columnHeadersAsList.toArray(new String[columnHeadersAsList.size()]);
		csvWriter.writeNext(columnHeaders);
		
		DateFormat dateFormatter = getDateFormatterForReport();
		
		for (DocumentAdvancedSearchResultsView advancedSearchResultsView : advancedSearchResultsViewsWrapper.getSearchResultsViews()) {
			
			List<String> columnValuesAsList = Lists.newArrayList(
				normalizeColumnValue(dateFormatter.format(advancedSearchResultsView.getDocumentCreatedDate())),
				normalizeColumnValue(advancedSearchResultsView.getDocumentAuthorDisplayName()),
				normalizeColumnValue(advancedSearchResultsView.getWorkflowCurrentStateName())
			);
			
			for (Long representativeMetadataDefinitionId : advancedSearchResultsViewsWrapper.getRepresentativeMetadataDefinitionLabelById().keySet()) {
				String metadataInstanceDisplayValue = advancedSearchResultsView.getDocumentMetadataInstanceDisplayValueByDefinitionId().get(representativeMetadataDefinitionId);
				String normalizedMetadataInstanceDisplayValue = normalizeColumnValue(metadataInstanceDisplayValue);
				columnValuesAsList.add(normalizedMetadataInstanceDisplayValue);
			}
			
			String[] columnValues = columnValuesAsList.toArray(new String[columnValuesAsList.size()]);
			csvWriter.writeNext(columnValues);
		}
		
		closeCsvWriter(csvWriter);
	}
	
	private void prepareResponseForCsv(HttpServletResponse response) throws IOException {
		response.setContentType(CSV_CONTENT_TYPE);
		HttpServletResponseUtils.setHeaderForAttachmentWithName(response, CSV_FILE_NAME);
	}
	
	private CSVWriter createCsvWriter(HttpServletResponse response) throws IOException {
		return new CSVWriter(response.getWriter(), CSV_SEPARATOR, CSV_DELIMITER, CSV_NEW_LINE_SEPARATOR);
	}
	
	private DateFormat getDateFormatterForReport() {
		return new SimpleDateFormat(FormatConstants.DATE_FOR_DISPLAY);
	}
	
	private String normalizeColumnValue(String columnValue) {
		return Strings.nullToEmpty(columnValue);
	}
	
	private void closeCsvWriter(CSVWriter csvWriter) throws IOException {
		csvWriter.flush();
		csvWriter.close();
	}
	
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}
	public void setDocumentTypeService(DocumentTypeService documentTypeService) {
		this.documentTypeService = documentTypeService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}
	public void setWorkflowExecutionService(WorkflowExecutionService workflowExecutionService) {
		this.workflowExecutionService = workflowExecutionService;
	}
}