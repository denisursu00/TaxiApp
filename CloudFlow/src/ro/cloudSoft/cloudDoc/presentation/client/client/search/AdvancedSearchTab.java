package ro.cloudSoft.cloudDoc.presentation.client.client.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.UserMetadataField;
import ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata.UserMetadataFieldFactory;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.NavigationConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSearchCriteriaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSearchCriteriaModel.CollectionSearchCriteriaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.DocumentSearchCriteriaModel.MetadataSearchCriteriaModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutoNumberMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.UserMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtFormatUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.DocumentLocationComboBox;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.DualListField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AdvancedSearchTab extends TabItem {
	
	/*
	 * metadatele indexate sunt salvate intr-un map (Map<String, List<Field<?>>>) astfel:
	 * daca este metadata: [PREFIX_METADATA + id, Lista de field-uri]
	 * daca este colectie: [PREFIX_COLLECTION_METADATA + idColectie + IDS_SEPARATOR + idMetadata, Lista de field-uri]
	 * 
	 */	
	private static final String PREFIX_METADATA = "m"; 
	private static final String PREFIX_COLLECTION_METADATA = "cm";
	private static final String IDS_SEPARATOR = "-";
	private static final String MAIN_SEPARATOR = "<;>";
	
	private static final String PARAMETER_DOCUMENT_SEARCH_CRITERIA = "documentSearchCriteria";
	private static final String PARAMETER_DOCUMENT_SEARCH_TYPE = "documentSearchType";
	
	private static final String PARAM_DOCUMENT_LOCATION = "documentLocation=";
	private static final String PARAM_START_DATE = "startDate=";
	private static final String PARAM_END_DATE = "endDate=";
	private static final String PARAM_DOCUMENT_TYPES = "documentTypes=";
	private static final String PARAM_SEARCH_IN_VERSIONS = "searchInVersions=";
	private static final String PARAM_DOCUMENT_STATES = "documentStates=";
	private static final String PARAM_M_INDEXED_METADATA = "mIndexedMetadata=";
	private static final String PARAM_CM_INDEXED_METADATA = "cmIndexedMetadata=";
	
	public static final String ADVANCED_SEARCH_TYPE = "advanced";
	
	private FormPanel advancedSearchForm;
	private DocumentLocationComboBox documentLocation;
	private FieldSet documentCreatedPeriodFS;
	private DateField startDate;
	private DateField endDate;
	private ComboBox<DocumentTypeModel> documentType;
	private CheckBox searchInVersions;
	private FieldSet indexedMetadatasFS;
	private FieldSet documentStatesFS;
	private DualListField<WorkflowStateModel> documentStates;
	private HiddenField<String> documentSearchCriteriaParamHidden;
	private HiddenField<String> documentSearchTypeHidden;
	private Map<String, List<Field<?>>> indexedMetadatas = new HashMap<String, List<Field<?>>>();
	
	public AdvancedSearchTab() {
		
		setText(GwtLocaleProvider.getConstants().ADVANCED_SEARCH());
		setScrollMode(Scroll.AUTO);
		
		initAdvancedSearchForm();
		addListeners();
		
		add(advancedSearchForm);
		addDoubleClickActions();
	}
	
	private void initAdvancedSearchForm(){
		advancedSearchForm = new FormPanel();
		advancedSearchForm.setHeaderVisible(false);
		advancedSearchForm.setBorders(false);
		advancedSearchForm.setBodyBorder(false);
		advancedSearchForm.setAction(NavigationConstants.getDocumentSearchReportLink());
		advancedSearchForm.setMethod(Method.POST);
		
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
		
		documentType = new ComboBox<DocumentTypeModel>();
		documentType.setStore(new ListStore<DocumentTypeModel>());
		documentType.setFieldLabel(GwtLocaleProvider.getConstants().DOC_TYPE());
		documentType.setDisplayField(DocumentTypeModel.PROPERTY_NAME);
		documentType.setValueField(DocumentTypeModel.PROPERTY_ID);
		documentType.setAllowBlank(false);
		
		searchInVersions = new CheckBox();
		searchInVersions.setFieldLabel(GwtLocaleProvider.getConstants().SEARCH_IN_VERSIONS());
		searchInVersions.setBoxLabel("");
		searchInVersions.disable();
		
		indexedMetadatasFS = new FieldSet();
		indexedMetadatasFS.setHeading(GwtLocaleProvider.getConstants().INDEXED_METADATAS());
		FormLayout fl = new FormLayout();
		fl.setLabelWidth(150);
		indexedMetadatasFS.setLayout(fl);
		indexedMetadatasFS.setCollapsible(true);
		indexedMetadatasFS.setExpanded(false);
		
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
		advancedSearchForm.add(documentSearchCriteriaParamHidden);
		
		documentSearchTypeHidden = new HiddenField<String>();
		documentSearchTypeHidden.setName(PARAMETER_DOCUMENT_SEARCH_TYPE);
		documentSearchTypeHidden.setValue(ADVANCED_SEARCH_TYPE);
		advancedSearchForm.add(documentSearchTypeHidden);
		
		advancedSearchForm.add(documentLocation);
		advancedSearchForm.add(documentCreatedPeriodFS);
		advancedSearchForm.add(documentType);
		advancedSearchForm.add(searchInVersions);
		advancedSearchForm.add(indexedMetadatasFS);
		advancedSearchForm.add(documentStatesFS);
	}
	
	private void addListeners(){
		documentType.addSelectionChangedListener(new SelectionChangedListener<DocumentTypeModel>(){
			@Override
			public void selectionChanged(SelectionChangedEvent<DocumentTypeModel> se) {
				if (se.getSelectedItem() != null) {
					
					// TODO cautare in versiuni - facut enable - daca pt. tipul de document 
					// se pastreaaza toate versiunile
					//if (se.getSelectedItem().get...)
					//	searchInVersions.enable();
					searchInVersions.enable(); // TODO de sters linia asta dupa ce se pune la document proprietate de cautare in versiuni
					
					// metadate indexate
					getDocumentType(se.getSelectedItem().getId());
					
					// starile
					List<Long> ids = new ArrayList<Long>();
					ids.add(se.getSelectedItem().getId());
					reloadDocumentStates(ids);
				}
			}
		});
		
		advancedSearchForm.addListener(Events.Submit, new Listener<FormEvent>(){
			@Override
			public void handleEvent(FormEvent be) {
				String respone = be.getResultHtml();
				if (respone.length() > 0) {
					MessageUtils.displayError(GwtLocaleProvider.getExceptionMessages().ERROR_GENERATING_REPORT());
				}
			}
		});
	}
	
	private void reloadDocumentStates(List<Long> ids){
		documentStates.getFromList().getStore().removeAll();
		documentStates.getToList().getStore().removeAll();
		if (ids != null && ids.size() > 0) {
			LoadingManager.get().loading();
			GwtServiceProvider.getWorkflowService().getStatesByDocumentTypeIds(ids, new AsyncCallback<List<WorkflowStateModel>>(){
				@Override
				public void onFailure(Throwable exception) {
					MessageUtils.displayError(exception);
					LoadingManager.get().loadingComplete();
				}
				@Override
				public void onSuccess(List<WorkflowStateModel> result) {
					documentStates.getFromList().getStore().add(result);
					LoadingManager.get().loadingComplete();
				}
			});
		}
	}
	
	private void getDocumentType(Long id) {
		LoadingManager.get().loading();
		GwtServiceProvider.getDocumentTypeService().getDocumentTypeById(id, new AsyncCallback<DocumentTypeModel>(){
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(DocumentTypeModel documentType) {
				populateIndexedMetadatasFS(documentType);
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private void populateIndexedMetadatasFS(DocumentTypeModel documentType){
		indexedMetadatasFS.removeAll();
		indexedMetadatasFS.setExpanded(false);
		indexedMetadatas.clear();
		if (documentType.getMetadataDefinitions() != null){
			for(MetadataDefinitionModel mdm : documentType.getMetadataDefinitions()){
				if (mdm instanceof ListMetadataDefinitionModel){
					ListMetadataDefinitionModel lmdm = (ListMetadataDefinitionModel)mdm;
					if (lmdm.isIndexed()){
						LayoutContainer container = new LayoutContainer();
						container.setBorders(false);
						FormLayout fl = new FormLayout();
						fl.setHideLabels(true);
						container.setLayout(fl);
						if (lmdm.getType().compareToIgnoreCase(MetadataDefinitionModel.TYPE_LIST) == 0){
							if (lmdm.isMultipleSelection()){
								ListField<ListMetadataItemModel> listField = new ListField<ListMetadataItemModel>();
								listField.setStore(new ListStore<ListMetadataItemModel>());
								listField.getStore().add(lmdm.getListItems());
								listField.setDisplayField(ListMetadataItemModel.PROPERTY_LABEL);
								container.add(listField);
								addValueInIndexedMetadatas(PREFIX_METADATA + lmdm.getId(), listField);
							}else{							
								ComboBox<ListMetadataItemModel> comboBox = new ComboBox<ListMetadataItemModel>();
								comboBox.setStore(new ListStore<ListMetadataItemModel>());
								comboBox.getStore().add(lmdm.getListItems());
								comboBox.setDisplayField(ListMetadataItemModel.PROPERTY_LABEL);
								container.add(comboBox);
								addValueInIndexedMetadatas(PREFIX_METADATA + lmdm.getId(), comboBox);
							}
							if (lmdm.isExtendable()){
								TextField<String> textField = new TextField<String>();
								textField.setEmptyText(GwtLocaleProvider.getConstants().ANOTHER_VALUE() + "...");
								container.add(textField);
								addValueInIndexedMetadatas(PREFIX_METADATA + lmdm.getId(), textField);
							}
						}
						AdapterField adapter = new AdapterField(container);
						adapter.setFieldLabel(lmdm.getLabel());
						indexedMetadatasFS.add(adapter, new FormData("90%"));
					}
				}else if (mdm instanceof MetadataCollectionDefinitionModel) {
					MetadataCollectionDefinitionModel cmdm = (MetadataCollectionDefinitionModel)mdm;
					FieldSet cmdmFieldSet = new FieldSet();
					FormLayout fl = new FormLayout();
					fl.setLabelWidth(140);
					cmdmFieldSet.setLayout(fl);
					cmdmFieldSet.setHeading(cmdm.getLabel());
					
					for (MetadataDefinitionModel mdm2 : cmdm.getMetadataDefinitions()){
						if (mdm2 instanceof ListMetadataDefinitionModel){
							ListMetadataDefinitionModel lmdm = (ListMetadataDefinitionModel)mdm2;
							if (lmdm.isIndexed()){
								final ComboBox<ListMetadataItemModel> comboBox = new ComboBox<ListMetadataItemModel>();
								comboBox.setStore(new ListStore<ListMetadataItemModel>());
								comboBox.getStore().add(lmdm.getListItems());
								comboBox.setDisplayField(ListMetadataItemModel.PROPERTY_LABEL);
								comboBox.setFieldLabel(lmdm.getLabel());
								cmdmFieldSet.add(comboBox);
								String key = PREFIX_COLLECTION_METADATA + cmdm.getId() + IDS_SEPARATOR + mdm2.getId();
								addValueInIndexedMetadatas(key, comboBox);								
							}
						}else if(mdm2 instanceof AutoNumberMetadataDefinitionModel){
							AutoNumberMetadataDefinitionModel anmdm = (AutoNumberMetadataDefinitionModel)mdm2;
							if (anmdm.isIndexed()){
								TextField<String> textField = new TextField<String>();
								textField.setFieldLabel(anmdm.getLabel());
								if (anmdm.getPrefix() != null)
									textField.setEmptyText(anmdm.getPrefix() + "####");
								else
									textField.setEmptyText("####");
								cmdmFieldSet.add(textField);
								String key = PREFIX_COLLECTION_METADATA + cmdm.getId() + IDS_SEPARATOR + mdm2.getId();
								addValueInIndexedMetadatas(key, textField);
							}
						}else{
							if (mdm2.isIndexed()){
								if (mdm2.getType().compareToIgnoreCase(MetadataDefinitionModel.TYPE_TEXT) == 0){
									TextField<String> textField = new TextField<String>();
									textField.setFieldLabel(mdm2.getLabel());
									cmdmFieldSet.add(textField);
									String key = PREFIX_COLLECTION_METADATA + cmdm.getId() + IDS_SEPARATOR + mdm2.getId();
									addValueInIndexedMetadatas(key, textField);
								}else if (mdm2.getType().compareToIgnoreCase(MetadataDefinitionModel.TYPE_NUMERIC) == 0){
									NumberField numberField = new NumberField();
									numberField.setFieldLabel(mdm2.getLabel());
									cmdmFieldSet.add(numberField);
									String key = PREFIX_COLLECTION_METADATA + cmdm.getId() + IDS_SEPARATOR + mdm2.getId();
									addValueInIndexedMetadatas(key, numberField);
								}else if (mdm2.getType().compareToIgnoreCase(MetadataDefinitionModel.TYPE_DATE) == 0){
									DateField dateField = new DateField();
									dateField.getPropertyEditor().setFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
									dateField.setFieldLabel(mdm2.getLabel());
									cmdmFieldSet.add(dateField);
									String key = PREFIX_COLLECTION_METADATA + cmdm.getId() + IDS_SEPARATOR + mdm2.getId();
									addValueInIndexedMetadatas(key, dateField);
								}
							}
						}
					}
					// daca exista field-uri in fieldset atunci sa il adauge
					if (cmdmFieldSet.getItems().size() > 0)
						indexedMetadatasFS.add(cmdmFieldSet);
				}else if(mdm instanceof AutoNumberMetadataDefinitionModel){
					AutoNumberMetadataDefinitionModel anmdm = (AutoNumberMetadataDefinitionModel)mdm;
					if (anmdm.isIndexed()){
						TextField<String> textField = new TextField<String>();
						textField.setFieldLabel(anmdm.getLabel());
						if (anmdm.getPrefix() != null)
							textField.setEmptyText(anmdm.getPrefix() + "####");
						else
							textField.setEmptyText("####");
						indexedMetadatasFS.add(textField);
						addValueInIndexedMetadatas(PREFIX_METADATA + anmdm.getId(), textField);
					}
				}else{
					if (mdm.isIndexed()){
						if (mdm.getType().compareToIgnoreCase(MetadataDefinitionModel.TYPE_TEXT) == 0){
							TextField<String> textField = new TextField<String>();
							textField.setFieldLabel(mdm.getLabel());
							indexedMetadatasFS.add(textField);
							addValueInIndexedMetadatas(PREFIX_METADATA + mdm.getId(), textField);
						}else if (mdm.getType().compareToIgnoreCase(MetadataDefinitionModel.TYPE_NUMERIC) == 0){
							NumberField numberField = new NumberField();
							numberField.setFieldLabel(mdm.getLabel());
							indexedMetadatasFS.add(numberField);
							addValueInIndexedMetadatas(PREFIX_METADATA + mdm.getId(), numberField);
						}else if (mdm.getType().compareToIgnoreCase(MetadataDefinitionModel.TYPE_USER) == 0){
							UserMetadataDefinitionModel umdm = (UserMetadataDefinitionModel) mdm;
							UserMetadataField userMetadataField = UserMetadataFieldFactory.forMetadataDefinition(umdm);
							userMetadataField.setFieldLabel(mdm.getLabel());
							indexedMetadatasFS.add(userMetadataField);
							addValueInIndexedMetadatas(PREFIX_METADATA + mdm.getId(), userMetadataField);
						}else if (mdm.getType().compareToIgnoreCase(MetadataDefinitionModel.TYPE_TEXT_AREA) == 0){
							TextArea textArea = new TextArea();
							textArea.setFieldLabel(mdm.getLabel());
							indexedMetadatasFS.add(textArea);
							addValueInIndexedMetadatas(PREFIX_METADATA + mdm.getId(), textArea);
						}else if (mdm.getType().compareToIgnoreCase(MetadataDefinitionModel.TYPE_DATE) == 0){
							DateField dateField = new DateField();
							dateField.getPropertyEditor().setFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
							dateField.setFieldLabel(mdm.getLabel());
							indexedMetadatasFS.add(dateField);
							addValueInIndexedMetadatas(PREFIX_METADATA + mdm.getId(), dateField);
						}
					}
				}
			}
			indexedMetadatasFS.setExpanded(indexedMetadatasFS.getItems().size() > 0);
		}
	}
	
	private void addValueInIndexedMetadatas(String key, Field<?> field){
		if (indexedMetadatas.get(key) != null){
			indexedMetadatas.get(key).add(field);
		}else{
			List<Field<?>> list = new ArrayList<Field<?>>();
			list.add(field);
			indexedMetadatas.put(key, list);
		}
	}
	
	public boolean isAdvancedSearchFormValid(){
		return advancedSearchForm.isValid();
	}
	
	public void prepareAdvancedSearchForm() {
		
		reset();		

		LoadingManager.get().loading();
		GwtServiceProvider.getDocumentTypeService().getAvailableDocumentTypesForSearch(
				new AsyncCallback<List<DocumentTypeModel>>() {
			
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			
			@Override
			public void onSuccess(List<DocumentTypeModel> result) {
				documentType.getStore().add(result);
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private void reset(){
		advancedSearchForm.clear();
		indexedMetadatas.clear();
		indexedMetadatasFS.removeAll();
		documentType.getStore().removeAll();
		documentStates.getFromList().getStore().removeAll();
		documentStates.getToList().getStore().removeAll();
	}
	
	@SuppressWarnings("unchecked")
	public void populateForSearch(DocumentSearchCriteriaModel documentSearchCriteriaModel){
		documentSearchCriteriaModel.setDocumentLocationRealName(
				documentLocation.getValue().getRealName());
		documentSearchCriteriaModel.setCreatedStart(startDate.getValue());
		documentSearchCriteriaModel.setCreatedEnd(endDate.getValue());
		if (searchInVersions.isEnabled())
			documentSearchCriteriaModel.setSearchInVersions(searchInVersions.getValue());
		
		if (documentType.getValue() != null){
			documentSearchCriteriaModel.addDocumentTypeId(documentType.getValue().getId());
			if (documentStates.getToList().getStore().getCount() > 0){
				for(WorkflowStateModel wsm : documentStates.getToList().getStore().getModels()){
					documentSearchCriteriaModel.addWorkflowStateId(wsm.getId());
				}
			}
		}
		
		for(String key : indexedMetadatas.keySet()){
						
			List<Field<?>> fields = (List<Field<?>>)indexedMetadatas.get(key);
			List<String> values = new ArrayList<String>();
			
			for (Field<?> field : fields){
				if (field.getValue() != null){
					values.clear();
					if (field instanceof UserMetadataField) {
						UserMetadataField userMetadataField = (UserMetadataField) field;
						values = userMetadataField.getMetadataValues();
					} else if (field instanceof ComboBox){
						ComboBox<ListMetadataItemModel> combo = (ComboBox<ListMetadataItemModel>)field;
						ListMetadataItemModel lmim = (ListMetadataItemModel)combo.getValue();
						values.add(lmim.getValue());
					}else if (field instanceof ListField){
						ListField<ListMetadataItemModel> listField = (ListField<ListMetadataItemModel>)field;
						if (listField.getSelection() != null && listField.getSelection().size() > 0){
							for (ListMetadataItemModel lmim : listField.getSelection()){
								values.add(lmim.getValue());
							}
						}
					}else if(field instanceof DateField){
						DateField dateField = (DateField)field;
						values.add(GwtFormatUtils.convertDateToString(GwtFormatConstants.DATE_FOR_SAVING, dateField.getValue()));
					}else
						values.add(field.getValue().toString());
					
					for (String value : values){
						if (value != null && value.trim().length() > 0){
							if (key.startsWith(PREFIX_METADATA)){
								Long metadataId = Long.valueOf(key.substring(PREFIX_METADATA.length()));
								MetadataSearchCriteriaModel mscm = new MetadataSearchCriteriaModel(metadataId, value); 
								documentSearchCriteriaModel.addMetadataSearchCriteria(mscm);
							}else{
								String[] ids =  key.substring(PREFIX_COLLECTION_METADATA.length()).split(IDS_SEPARATOR);
								Long collectionMetadataId = Long.valueOf(ids[0]);
								Long metadataId = Long.valueOf(ids[1]);
								CollectionSearchCriteriaModel cscm = new CollectionSearchCriteriaModel(collectionMetadataId, metadataId, value);
								documentSearchCriteriaModel.addCollectionSearchCriteria(cscm);
							}
						}
					}
				}
			}	
		}
	}
	
	/**
	 * Aceasta metoda construeste un string de forma:<br><br>
	 * startDate=1299299000<;>endDate=923383300<;>documentTypes=3223,3454<;>documentStates=2334,33,56<;>
	 * mIndexedMetadata=1009:sdc<;>mIndexedMetadata=1029:omilogic<;>cmIndexedMetadata=10222-103:vasile<br><br>
	 * apoi face submit la form.
	 */
	@SuppressWarnings("unchecked")
	public void submitAdvancedSearchForm(){	
		StringBuilder param = new StringBuilder();
		
		if (!advancedSearchForm.isValid())
			return;
		
		documentSearchTypeHidden.setValue(ADVANCED_SEARCH_TYPE);
		
		// document location
		if (documentLocation.getValue() != null){
			param.append(PARAM_DOCUMENT_LOCATION + documentLocation.getValue().getRealName() + MAIN_SEPARATOR);
		}else
			return;
		
		// start date
		if (startDate.getValue() != null){
			param.append(PARAM_START_DATE + startDate.getValue().getTime() + MAIN_SEPARATOR);
		}
		
		// end date
		if (endDate.getValue() != null){
			param.append(PARAM_END_DATE + endDate.getValue().getTime() + MAIN_SEPARATOR);
		}
		
		// document type
		if (documentType.getValue() != null){
			param.append(PARAM_DOCUMENT_TYPES + documentType.getValue().getId() + MAIN_SEPARATOR);
		}
		
		// cautare in versiuni
		if (searchInVersions.isEnabled()){
			param.append(PARAM_SEARCH_IN_VERSIONS + searchInVersions.getValue() + MAIN_SEPARATOR);
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
		
		// indexed metadatas
		for(String key : indexedMetadatas.keySet()){
			List<Field<?>> fields = (List<Field<?>>)indexedMetadatas.get(key);
			List<String> values = new ArrayList<String>();
			
			for (Field<?> field : fields){
				if (field.getValue() != null){
					values.clear();
					if (field instanceof UserMetadataField) {
						UserMetadataField userMetadataField = (UserMetadataField) field;
						values.addAll(userMetadataField.getMetadataValues());
					} else if (field instanceof ComboBox) {
						ComboBox<ListMetadataItemModel> combo = (ComboBox<ListMetadataItemModel>)field;
						ListMetadataItemModel lmim = (ListMetadataItemModel)combo.getValue();
						values.add(lmim.getValue());
					}else if (field instanceof ListField){
						ListField<ListMetadataItemModel> listField = (ListField<ListMetadataItemModel>)field;
						if (listField.getSelection() != null && listField.getSelection().size() > 0){
							for (ListMetadataItemModel lmim : listField.getSelection()){
								values.add(lmim.getValue());
							}
						}
					}else if(field instanceof DateField){
						DateField dateField = (DateField)field;
						values.add(GwtFormatUtils.convertDateToString(GwtFormatConstants.DATE_FOR_SAVING, dateField.getValue()));
					}else
						values.add(field.getValue().toString());
					
					for (int i=0; i < values.size(); i++){
						if (values.get(i) != null && values.get(i).trim().length() > 0){
							if (key.startsWith(PREFIX_METADATA)){
								param.append(PARAM_M_INDEXED_METADATA + key.substring(PREFIX_METADATA.length()) + ":" + values.get(i) + MAIN_SEPARATOR);
							}else{
								param.append(PARAM_CM_INDEXED_METADATA + key.substring(PREFIX_COLLECTION_METADATA.length()) + ":" + values.get(i) + MAIN_SEPARATOR);
							}
						}
					}
				}
			}
		}	
		
		documentSearchCriteriaParamHidden.setValue(param.toString());
		
		advancedSearchForm.submit();
	}
	
	private void addDoubleClickActions(){		
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
