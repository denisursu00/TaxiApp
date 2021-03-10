package ro.cloudSoft.cloudDoc.presentation.client.client.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.NavigationConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSearchCriteriaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.DocumentLocationComboBox;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.DualListField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SimpleSearchTab extends TabItem {

	public static final String SIMPLE_SEARCH_TYPE = "simple";
	private static final String MAIN_SEPARATOR = "<;>";
	
	private static final String PARAMETER_DOCUMENT_SEARCH_CRITERIA = "documentSearchCriteria";
	private static final String PARAMETER_DOCUMENT_SEARCH_TYPE = "documentSearchType";
	
	private static final String PARAM_DOCUMENT_LOCATION = "documentLocation=";
	private static final String PARAM_START_DATE = "startDate=";
	private static final String PARAM_END_DATE = "endDate=";
	private static final String PARAM_DOCUMENT_TYPES = "documentTypes=";
	private static final String PARAM_DOCUMENT_STATES = "documentStates=";
	
	private FormPanel simpleSearchForm;
	private DocumentLocationComboBox documentLocation;
	private FieldSet documentCreatedPeriodFS;
	private DateField startDate;
	private DateField endDate;
	private FieldSet documentTypesFS;
	private DualListField<DocumentTypeModel> documentTypes;
	private FieldSet documentStatesFS;
	private DualListField<WorkflowStateModel> documentStates;
	private HiddenField<String> documentSearchCriteriaParamHidden;
	private HiddenField<String> documentSearchTypeHidden;
	
	public SimpleSearchTab() {
		
		setText(GwtLocaleProvider.getConstants().SIMPLE_SEARCH());
		setScrollMode(Scroll.AUTO);
		
		initSimpleSearchForm();
		addListeners();
		
		add(simpleSearchForm);
		addDoubleClickActions();
	}
	
	private void initSimpleSearchForm(){
		simpleSearchForm = new FormPanel();
		simpleSearchForm.setHeaderVisible(false);
		simpleSearchForm.setBorders(false);
		simpleSearchForm.setBodyBorder(false);
		simpleSearchForm.setAction(NavigationConstants.getDocumentSearchReportLink());
		simpleSearchForm.setMethod(Method.POST);
		
		documentLocation = new DocumentLocationComboBox();
		documentLocation.setFieldLabel(GwtLocaleProvider.getConstants().WORKSPACE());
		documentLocation.setValueField(DocumentLocationModel.PROPERTY_REAL_NAME);
		documentLocation.setAllowBlank(false);
		
		documentCreatedPeriodFS = new FieldSet();
		documentCreatedPeriodFS.setHeading(GwtLocaleProvider.getConstants().DOCUMENT_CREATION_DATE());
		documentCreatedPeriodFS.setLayout(new FormLayout());
		
		startDate = new DateField();
		startDate.getPropertyEditor().setFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		startDate.setFieldLabel(GwtLocaleProvider.getConstants().DOCUMENT_CREATION_DATE_FROM());
		
		endDate = new DateField();
		endDate.getPropertyEditor().setFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
		endDate.setFieldLabel(GwtLocaleProvider.getConstants().DOCUMENT_CREATION_DATE_TO());
		
		documentCreatedPeriodFS.add(startDate);
		documentCreatedPeriodFS.add(endDate);
		
		documentTypesFS = new FieldSet();
		documentTypesFS.setHeading(GwtLocaleProvider.getConstants().DOCUMENT_TYPES());
		
		documentTypes = new DualListField<DocumentTypeModel>();
		documentTypes.getFromList().setStore(new ListStore<DocumentTypeModel>());
		documentTypes.getFromList().setDisplayField(DocumentTypeModel.PROPERTY_NAME);
		documentTypes.getToList().setStore(new ListStore<DocumentTypeModel>());
		documentTypes.getToList().setDisplayField(DocumentTypeModel.PROPERTY_NAME);
		documentTypesFS.add(documentTypes);
		documentTypes.getFromList().setData("initialCount", new Integer(0));
		
		documentStatesFS = new FieldSet();
		documentStatesFS.setHeading(GwtLocaleProvider.getConstants().DOCUMENT_STATES());
		
		documentStates = new DualListField<WorkflowStateModel>();
		documentStates.getFromList().setStore(new ListStore<WorkflowStateModel>());
		documentStates.getFromList().setDisplayField(WorkflowStateModel.PROPERTY_NAME);
		documentStates.getToList().setStore(new ListStore<WorkflowStateModel>());
		documentStates.getToList().setDisplayField(WorkflowStateModel.PROPERTY_NAME);
		documentStatesFS.add(documentStates);
		
		documentSearchCriteriaParamHidden = new HiddenField<String>();
		documentSearchCriteriaParamHidden.setName(PARAMETER_DOCUMENT_SEARCH_CRITERIA);
		documentSearchCriteriaParamHidden.setValue(null);
		simpleSearchForm.add(documentSearchCriteriaParamHidden);
		
		documentSearchTypeHidden = new HiddenField<String>();
		documentSearchTypeHidden.setName(PARAMETER_DOCUMENT_SEARCH_TYPE);
		documentSearchTypeHidden.setValue(SIMPLE_SEARCH_TYPE);
		simpleSearchForm.add(documentSearchTypeHidden);
		
		// cu chestia asta am incercat sa pun datile orizontal, dar nu a mers...
		/*HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlign(HorizontalAlignment.LEFT);
		hp.setLayout(new FormLayout());
		hp.setBorders(true);
		hp.add(documentCreationDate);
		hp.addText("de la ");
		hp.add(startDate);
		hp.addText(" pana la ");
		hp.add(endDate);
		AdapterField dateAdapter = new AdapterField(hp);
		dateAdapter.setFieldLabel("Data creari:");
		dateAdapter.setBorders(true);
		*/
		
		simpleSearchForm.add(documentLocation);
		simpleSearchForm.add(documentCreatedPeriodFS);
		//simpleSearchForm.add(dateAdapter, new FormData("100%"));
		simpleSearchForm.add(documentTypesFS);
		simpleSearchForm.add(documentStatesFS);
	}
	
	private void addListeners(){
		
		documentTypes.getToList().getStore().addListener(Store.Add, new StoreListener<DocumentTypeModel>(){
			@Override
			public void handleEvent(StoreEvent<DocumentTypeModel> e) {
				List<Long> ids = new ArrayList<Long>();
				if (e.getModels() != null){
					for(DocumentTypeModel dtm : e.getModels()){
						ids.add(dtm.getId());
					}
				}
				
				LoadingManager.get().loading();
				GwtServiceProvider.getWorkflowService().getStatesByDocumentTypeIds(ids, new AsyncCallback<List<WorkflowStateModel>>(){
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					@Override
					public void onSuccess(List<WorkflowStateModel> result) {
						// verific sa nu mai fie deja adagate alte stari la fel (cu acelasi id)
						for(WorkflowStateModel wsm : result){
							WorkflowStateModel state = documentStates.getFromList().getStore().findModel(WorkflowStateModel.PROPERTY_ID, wsm.getId());
							if (state == null)
								state = documentStates.getToList().getStore().findModel(WorkflowStateModel.PROPERTY_ID, wsm.getId());
							if (state == null)
								documentStates.getFromList().getStore().add(wsm);
						}
						LoadingManager.get().loadingComplete();
					}
				});
				super.handleEvent(e);
			}
		});
		
		// cand se sterge un document atunci veridic in lista stanga ce se adauga 
		documentTypes.getFromList().getStore().addListener(Store.Add, new StoreListener<DocumentTypeModel>(){
			@Override
			public void handleEvent(StoreEvent<DocumentTypeModel> e) {
				// ca sa nu mai fac interogari aiurea in baza de date, mai intai
				// verific daca nu cumva lista documentelor din dreapta este goala,
				// pentru ca in acest caz sterg tot de jos, am facut cu count-ul de la
				// lista din stanga deoarece exista un bug la GXT si nu stie
				// intotdeauna dimeniusiunea listei din dreapta
				int initialCount = Integer.valueOf(documentTypes.getFromList().getData("initialCount").toString()).intValue();
				if (documentTypes.getFromList().getStore().getCount() == initialCount || initialCount == 0){
					documentStates.getFromList().getStore().removeAll();
					documentStates.getToList().getStore().removeAll();
				} else {
					
					/*
					 * Deoarece adaugarea in lista din stanga se poate face inainte / dupa stergerea
					 * din lista din dreapta (in functie de eveniment), trebuie sa exclud tipurile
					 * de documente care au fost adaugate in lista din stanga.
					 */
					Set<Long> documentTypeIdsToBeExcluded = new HashSet<Long>();
					if (e.getModel() != null) {
						documentTypeIdsToBeExcluded.add(e.getModel().getId());
					}
					if (e.getModels() != null) {
						for (DocumentTypeModel documentType : e.getModels()) {
							documentTypeIdsToBeExcluded.add(documentType.getId());
						}
					}
					
					List<Long> ids = new ArrayList<Long>();
					for (DocumentTypeModel d : documentTypes.getToList().getStore().getModels()) {
						if (!documentTypeIdsToBeExcluded.contains(d.getId())) {
							ids.add(d.getId());
						}
					}
					
					LoadingManager.get().loading();
					GwtServiceProvider.getWorkflowService().getStatesByDocumentTypeIds(ids, new AsyncCallback<List<WorkflowStateModel>>(){
						@Override
						public void onFailure(Throwable exception) {
							MessageUtils.displayError(exception);
							LoadingManager.get().loadingComplete();
						}
						@Override
						public void onSuccess(List<WorkflowStateModel> result) {
							// starile care nu le gasesc in lista venita din baza de date
							// le voi sterge							
							for (WorkflowStateModel sFrom : documentStates.getFromList().getStore().getModels()){
								boolean found = false;
								for(WorkflowStateModel wsm : result){
									if (sFrom.getId().longValue() == wsm.getId().longValue()){
										found = true;
										break;
									}
								}
								if (!found)
									documentStates.getFromList().getStore().remove(sFrom);						
							}							
							for (WorkflowStateModel sTo : documentStates.getToList().getStore().getModels()){
								boolean found = false;
								for(WorkflowStateModel wsm : result){
									if (sTo.getId().longValue() == wsm.getId().longValue()){
										found = true;
										break;
									}
								}
								if (!found)
									documentStates.getToList().getStore().remove(sTo);									
							}
							
							LoadingManager.get().loadingComplete();
						}
					});
				}
				super.handleEvent(e);
			}
		});
		
		simpleSearchForm.addListener(Events.Submit, new Listener<FormEvent>(){
			@Override
			public void handleEvent(FormEvent be) {
				String respone = be.getResultHtml();
				if (respone.length() > 0){
					MessageUtils.displayError(GwtLocaleProvider.getExceptionMessages().ERROR_GENERATING_REPORT());
				}
			}
		});
	}
	
	public void prepareSimpleSearchForm(){
		
		reset();
		
		LoadingManager.get().loading();
		GwtServiceProvider.getDocumentTypeService().getAvailableDocumentTypesForSearch(new AsyncCallback<List<DocumentTypeModel>>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(List<DocumentTypeModel> result) {
				
				documentTypes.getFromList().getStore().add(result);
				documentTypes.getFromList().setData("initialCount", new Integer(result.size()));
				
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private void reset(){
		simpleSearchForm.clear();
		documentTypes.getFromList().getStore().removeAll();
		documentTypes.getToList().getStore().removeAll();
		documentStates.getFromList().getStore().removeAll();
		documentStates.getToList().getStore().removeAll();
	}
	
	public boolean isSimpleSearchFromValid(){
		return simpleSearchForm.isValid();
	}
	
	public void populateForSearch(DocumentSearchCriteriaModel documentSearchCriteriaModel){
		documentSearchCriteriaModel.setDocumentLocationRealName(
				documentLocation.getValue().getRealName());
		documentSearchCriteriaModel.setCreatedStart(startDate.getValue());
		documentSearchCriteriaModel.setCreatedEnd(endDate.getValue());
		documentSearchCriteriaModel.setSearchInVersions(false);
		
		if (documentTypes.getToList().getStore().getCount() > 0){
			for(DocumentTypeModel dtm : documentTypes.getToList().getStore().getModels()){
				documentSearchCriteriaModel.addDocumentTypeId(dtm.getId());
			}
		}		
		
		if (documentStates.getToList().getStore().getCount() > 0){
			for(WorkflowStateModel wsm : documentStates.getToList().getStore().getModels()){
				documentSearchCriteriaModel.addWorkflowStateId(wsm.getId());
			}
		}		
	}
	
	/**
	 * Aceasta metoda construeste un string de forma:<br><br>
	 * startDate=1299299000<;>endDate=923383300<;>documentTypes=3223,3454<;>
	 * documentStates=2334,33,56<;><br><br>
	 * apoi face submit la form.
	 */
	public void submitSimpleSearchForm(){	
		StringBuilder param = new StringBuilder();
		
		documentSearchTypeHidden.setValue(SIMPLE_SEARCH_TYPE);
		
		// document location
		if (documentLocation.getValue() != null){
			param.append(PARAM_DOCUMENT_LOCATION + documentLocation.getValue().getRealName() + MAIN_SEPARATOR);
		}else{
			simpleSearchForm.isValid();
			return;
		}
		
		// start date
		if (startDate.getValue() != null){
			param.append(PARAM_START_DATE + startDate.getValue().getTime() + MAIN_SEPARATOR);
		}
		
		// end date
		if (endDate.getValue() != null){
			param.append(PARAM_END_DATE + endDate.getValue().getTime() + MAIN_SEPARATOR);
		}
		
		// document types		
		List<DocumentTypeModel> listDocumentTypes = documentTypes.getToList().getStore().getModels();
		if (listDocumentTypes != null && listDocumentTypes.size() > 0){
			StringBuilder ids = new StringBuilder();
			for(int i = 0; i < listDocumentTypes.size(); i++){
				ids.append(listDocumentTypes.get(i).getId());
				ids.append(i == (listDocumentTypes.size() -1) ? "":",");
			}
			param.append(PARAM_DOCUMENT_TYPES + ids.toString() + MAIN_SEPARATOR);
		}
		
		// document states
		List<WorkflowStateModel> listDocumentStates = documentStates.getToList().getStore().getModels();
		if (listDocumentStates != null && listDocumentStates.size() > 0){
			StringBuilder ids = new StringBuilder();
			for(int i = 0; i < listDocumentStates.size(); i++){
				ids.append(listDocumentStates.get(i).getId());
				ids.append(i == (listDocumentStates.size() -1) ? "":",");
			}
			param.append(PARAM_DOCUMENT_STATES + ids.toString() + MAIN_SEPARATOR);
		}
		
		documentSearchCriteriaParamHidden.setValue(param.toString());
		
		simpleSearchForm.submit();
	}
	
	private void addDoubleClickActions(){
		documentTypes.getFromList().getListView().addListener(Events.DoubleClick, new Listener<ListViewEvent<DocumentTypeModel>>(){
			public void handleEvent(ListViewEvent<DocumentTypeModel> be) {
				documentTypes.getToList().getStore().add(be.getModel());
				documentTypes.getFromList().getStore().remove(be.getModel());
			}			
		});
		documentTypes.getToList().getListView().addListener(Events.DoubleClick, new Listener<ListViewEvent<DocumentTypeModel>>(){
			public void handleEvent(ListViewEvent<DocumentTypeModel> be) {
				documentTypes.getFromList().getStore().add(be.getModel());
				documentTypes.getToList().getStore().remove(be.getModel());
			}			
		});
		documentStates.getFromList().getListView().addListener(Events.DoubleClick, new Listener<ListViewEvent<WorkflowStateModel>>(){
			public void handleEvent(ListViewEvent<WorkflowStateModel> be) {
				documentStates.getToList().getStore().add(be.getModel());
				documentStates.getFromList().getStore().remove(be.getModel());
			}			
		});
		documentStates.getToList().getListView().addListener(Events.DoubleClick, new Listener<ListViewEvent<WorkflowStateModel>>(){
			public void handleEvent(ListViewEvent<WorkflowStateModel> be) {
				documentStates.getFromList().getStore().add(be.getModel());
				documentStates.getToList().getStore().remove(be.getModel());
			}			
		});
	}	
	
}
