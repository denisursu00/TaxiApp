package ro.cloudSoft.cloudDoc.web.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria.CollectionSearchCriteria;
import ro.cloudSoft.cloudDoc.domain.content.search.DocumentSearchCriteria.MetadataSearchCriteria;

public class DocumentSearchCriteriaFromParametersBuilder {
	
	private static final String PARAMETERS_MAIN_SEPARATOR = "<;>";
	
	private static final String PARAM_DOCUMENT_LOCATION = "documentLocation=";
	private static final String PARAM_START_DATE = "startDate=";
	private static final String PARAM_END_DATE = "endDate=";
	private static final String PARAM_DOCUMENT_TYPES = "documentTypes=";
	private static final String PARAM_SEARCH_IN_VERSIONS = "searchInVersions=";
	private static final String PARAM_DOCUMENT_STATES = "documentStates=";
	private static final String PARAM_M_INDEXED_METADATA = "mIndexedMetadata=";
	private static final String PARAM_CM_INDEXED_METADATA = "cmIndexedMetadata=";

	private final String joinedParameters;
	
	public DocumentSearchCriteriaFromParametersBuilder(String joinedParameters) {
		this.joinedParameters = joinedParameters;
	}
	
	/**
	 * Construeste un obiect de cautare in repository DocumentSearchCriteria in functie 
	 * de String-ul parameter care este intr-o anumita forma. Returneaza null daca
	 * parameter este null sau de lungime 0;
	 * <br><br>
	 * forma parametru:
	 * <br>
	 * startDate=1299299000<;>endDate=923383300<;>documentTypes=3223,3454<;>documentStates=2334,33,56;
	 * mIndexedMetadata=1009:sdc<;>mIndexedMetadata=1029:omilogic<;>cmIndexedMetadata=10222-103:vasile
	 *	 
	 * @param parameter
	 * @return DocumentSearchCriteria sau null
	 */
	public DocumentSearchCriteria build() {
		
		// TODO De simplificat, codul este un pic prea low-level
		
		/*
		 * forma String-ului care va fi parsat:
		 * 
		 * startDate=1299299000<;>endDate=923383300<;>documentTypes=3223,3454<;>documentStates=2334,33,56<;>
		 * mIndexedMetadata=1009:sdc<;>mIndexedMetadata=1029:omilogic<;>cmIndexedMetadata=10222-103:vasile<;>
		 * 
		 */			
		DocumentSearchCriteria criteria = null;
		if (joinedParameters != null && joinedParameters.length() > 0){
			criteria = new DocumentSearchCriteria();
			criteria.setSearchInVersions(false);
			String[] params = joinedParameters.split(PARAMETERS_MAIN_SEPARATOR);
			for (String param : params){				
				if (param.startsWith(PARAM_DOCUMENT_LOCATION)){
					// form: documentLocation=value
					// example: documentLocation=wks1
					
					int index = param.indexOf("=");
					String documentLocationRealName = param.substring(++index);
					criteria.setDocumentLocationRealName(documentLocationRealName);
				}else if (param.startsWith(PARAM_START_DATE)){
					// form: startDate=time
					// example: startDate=129220000
					
					String[] values = param.split("=");
					if (values[1].length() > 0)
						criteria.setCreatedStart(new Date(Long.valueOf(values[1])));
				}else if (param.startsWith(PARAM_END_DATE)){
					// form: startDate=time
					// example: startDate=129220000
					
					String[] values = param.split("=");
					if (values[1].length() > 0)
						criteria.setCreatedEnd(new Date(Long.valueOf(values[1])));
				}else if (param.startsWith(PARAM_SEARCH_IN_VERSIONS)){
					// form: searchInVersions=boolean
					// example: searchInVersions=true
					
					String[] values = param.split("=");
					criteria.setSearchInVersions(Boolean.parseBoolean(values[1]));
				}else if (param.startsWith(PARAM_DOCUMENT_TYPES)){
					// form: documentTypes=id sau documentTypes=id,id
					// example: documentTypes=129 sau documentTypes=232,23
					
					List<Long> idsList = new ArrayList<Long>(); 
					String[] values = param.split("=");
					if (values[1].length() > 0){
						String[] ids = values[1].split(",");
						if (ids != null && ids.length > 0){
							for(String id : ids){
								idsList.add(new Long(id));
							}						
						}
					}
					criteria.setDocumentTypeIdList(idsList);						
				}else if (param.startsWith(PARAM_DOCUMENT_STATES)){
					// form: documentStates=id sau documentStates=id,id
					// example: documentStates=129 sau documentStates=232,23
					
					List<Long> statesList = new ArrayList<Long>(); 
					String[] values = param.split("=");
					if (values[1].length() > 0){
						String[] states = values[1].split(",");
						if (states != null && states.length > 0){ 
							for(String state : states){
								statesList.add(new Long(state));
							}
						}
					}
					criteria.setWorkflowStateIdList(statesList);						
				}else if (param.startsWith(PARAM_M_INDEXED_METADATA)){
					// forma: mIndexedMetadata=id:value
					// example: mIndexedMetadata=1009:sdc
					
					// obtin id:value
					String idColonValue = param.substring(PARAM_M_INDEXED_METADATA.length());
					
					// first index for colon
					int index = idColonValue.indexOf(":");
					
					Long idM = Long.valueOf(idColonValue.substring(0, index));
					String value = idColonValue.substring(++index);
					
					MetadataSearchCriteria metadataSearchCriteria = new MetadataSearchCriteria(
							Long.valueOf(idM), value);
					criteria.addMetadataSearchCriteria(metadataSearchCriteria);
					
				}else if (param.startsWith(PARAM_CM_INDEXED_METADATA)){
					// form: cmIndexedMetadata=idCM-idM:value
					// example: cmIndexedMetadata=1020-1039:sdc
					
					// obtin idCM-idM:value
					String idsColonValue = param.substring(PARAM_CM_INDEXED_METADATA.length());
					
					// first index for colon
					int index = idsColonValue.indexOf(":");
					
					String ids = idsColonValue.substring(0, index);
					String[] idsArray = ids.split("-");
					
					Long idCM = Long.valueOf(idsArray[0]);
					Long idM = Long.valueOf(idsArray[1]);
					String value = idsColonValue.substring(++index);
					
					CollectionSearchCriteria collectionSearchCriteria =  new CollectionSearchCriteria(idCM, idM, value);
					criteria.addCollectionSearchCriteria(collectionSearchCriteria);
				}
			}
		}
		
		return criteria;
	}
}