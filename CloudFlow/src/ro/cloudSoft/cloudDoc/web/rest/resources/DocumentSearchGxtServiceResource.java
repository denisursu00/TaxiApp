package ro.cloudSoft.cloudDoc.web.rest.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ro.cloudSoft.cloudDoc.core.AppException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.exceptions.PresentationException;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSearchCriteriaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSelectionSearchFilterModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentAdvancedSearchResultsViewsWrapperModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentSelectionSearchResultViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.search.DocumentSimpleSearchResultsViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.views.MyActivitiesViewModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.services.search.DocumentSearchGxtService;
import ro.cloudSoft.cloudDoc.presentation.server.utils.PresentationExceptionUtils;
import ro.cloudSoft.cloudDoc.services.content.SearchService;

@Component
@Path("/DocumentSearchGxtService")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class DocumentSearchGxtServiceResource extends BaseResource {
	
	@Autowired
	private DocumentSearchGxtService documentSearchGxtService;
	
	@Autowired
	private SearchService searchService;
	
	@POST
	@Path("/getMyActivities")
	public List<MyActivitiesViewModel> getMyActivities() throws PresentationException {
		return documentSearchGxtService.getMyActivites();
	}
	
	@POST
	@Path("/findDocumentsUsingSimpleSearch")
	public List<DocumentSimpleSearchResultsViewModel> findDocumentsUsingSimpleSearch(DocumentSearchCriteriaModel documentSearchCriteria) throws PresentationException {
		return documentSearchGxtService.findDocumentsUsingSimpleSearch(documentSearchCriteria);
	}
	
	@POST
	@Path("/findDocumentsUsingAdvancedSearch")
	public DocumentAdvancedSearchResultsViewsWrapperModel findDocumentsUsingAdvancedSearch(DocumentSearchCriteriaModel documentSearchCriteria) throws PresentationException {
		return documentSearchGxtService.findDocumentsUsingAdvancedSearch(documentSearchCriteria);
	}
	
	@POST
	@Path("/findDocumentsForSelection")
	public List<DocumentSelectionSearchResultViewModel> findDocumentsForSelection(DocumentSelectionSearchFilterModel filterModel) throws PresentationException {
		try {
			return searchService.findDocumentsForSelection(filterModel, getSecurity());
		} catch (AppException e) {
			throw PresentationExceptionUtils.getPresentationException(e);
		}
	}
}
