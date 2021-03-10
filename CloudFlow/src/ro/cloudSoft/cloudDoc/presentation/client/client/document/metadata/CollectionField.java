package ro.cloudSoft.cloudDoc.presentation.client.client.document.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.cloudSoft.cloudDoc.presentation.client.shared.CustomGridCellRenderer;
import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.GwtFormatConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.WorkflowStateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.AutoNumberMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.CollectionInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.ListMetadataItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataCollectionDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataDefinitionModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.MetadataInstanceModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils.ConfirmCallback;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtFormatUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtValidateUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.business.content.GwtMetadataOrCollectionBusinessUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.CustomAdapterFieldWithAutoWidth;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.NumberFormat;

public class CollectionField extends CustomAdapterFieldWithAutoWidth {
	
	private MetadataCollectionDefinitionModel collectionDefinition;
	private WorkflowStateModel currentState;
	
	private Listener<GridEvent<CollectionInstanceEditorGridModel>> cancelEditListener;
	
	private AutoNumberMetadataValueHandler autoNumberMetadataValueHandler;
	private ListMetadataItemLabelResolver listMetadataItemLabelResolver;
	
	private ContentPanel innerContentPanel;
	
	private ToolBar collectionInstanceEditorToolBar;
	private EditorGrid<CollectionInstanceEditorGridModel> collectionInstanceEditorGrid;
	
	private Button addButton;
	private Button removeButton;
	
	private boolean readOnly;
	private boolean mandatory;
	private boolean restrictedOnEdit;
	
	public CollectionField(MetadataCollectionDefinitionModel collectionDefinition, WorkflowStateModel currentState) {
		
		super(240);
		
		this.setToolTip(GwtLocaleProvider.getConstants().REQUIRED_METADATAS_TOOLTIP());
		
		this.collectionDefinition = collectionDefinition;
		this.currentState = currentState;
		
		cancelEditListener = new Listener<GridEvent<CollectionInstanceEditorGridModel>>() {
			@Override
			public void handleEvent(GridEvent<CollectionInstanceEditorGridModel> ge) {
				ge.setCancelled(true);
			}
		};
		
		autoNumberMetadataValueHandler = new AutoNumberMetadataValueHandler();
		listMetadataItemLabelResolver = new ListMetadataItemLabelResolver();
		
		innerContentPanel = new ContentPanel();
		innerContentPanel.setHeaderVisible(false);
		innerContentPanel.setLayout(new FitLayout());

		collectionInstanceEditorToolBar = new ToolBar();
		
		addButton = new Button();
		addButton.setText(GwtLocaleProvider.getConstants().ADD());
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent be) {
				CollectionInstanceEditorGridModel collectionInstance = new CollectionInstanceEditorGridModel();
				CollectionField.this.autoNumberMetadataValueHandler.completeAllAutoNumberMetadataFields(collectionInstance);
				collectionInstanceEditorGrid.getStore().insert(collectionInstance, 0);
			}
		});
		collectionInstanceEditorToolBar.add(addButton);
		
		removeButton = new Button();
		removeButton.setText(GwtLocaleProvider.getConstants().REMOVE());
		removeButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent be) {
				final CollectionInstanceEditorGridModel selectedCollectionInstance  = collectionInstanceEditorGrid.getSelectionModel().getSelectedItem();
				if (selectedCollectionInstance != null) {
					ComponentUtils.confirm(GwtLocaleProvider.getConstants().DELETE(),
							GwtLocaleProvider.getMessages().CONFIRM_DELETE_COLLECTION(),
							new ConfirmCallback() {
						
						@Override
						public void onYes() {
							collectionInstanceEditorGrid.getStore().remove(selectedCollectionInstance);
						}
					});
				}
			}
		});		
		collectionInstanceEditorToolBar.add(removeButton);
		
		innerContentPanel.setTopComponent(collectionInstanceEditorToolBar);
		
		List<ColumnConfig> columnConfigList = new ArrayList<ColumnConfig>();
		for (final MetadataDefinitionModel metadataDefinition : collectionDefinition.getMetadataDefinitions()) {
			if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_AUTO_NUMBER)) {
				/*
				 * Daca definitia metadatei este de tip auto number,
				 * o adaug in registrul handler-ului de valori specifice.
				 */
				this.autoNumberMetadataValueHandler.addMetadataDefinition((AutoNumberMetadataDefinitionModel) metadataDefinition);
			} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_LIST)) {
				/*
				 * Daca definitia metadatei este de tip lista, atunci o adaug
				 * in registrul etichetelor valorilor. 
				 */
				this.listMetadataItemLabelResolver.addListMetadataDefinition((ListMetadataDefinitionModel) metadataDefinition);
			}
			// Fiecare definitie de metadata va avea o coloana in grid.
			ColumnConfig metadataColumnConfig = new ColumnConfig();
			metadataColumnConfig.setId(CollectionInstanceEditorGridModel.PREFIX_METADATA_PROPERTIES + metadataDefinition.getId().toString());
			metadataColumnConfig.setHeader((GwtMetadataOrCollectionBusinessUtils.isMetadataMandatory(metadataDefinition, this.currentState)) ? metadataDefinition.getLabel() + " *" : metadataDefinition.getLabel());
			metadataColumnConfig.setWidth(120);
			
			if (!GwtMetadataOrCollectionBusinessUtils.isMetadataRestrictedOnEdit(metadataDefinition, this.currentState)) {
				metadataColumnConfig.setEditor(getEditor(metadataDefinition));
			}
			
			metadataColumnConfig.setRenderer(new CustomGridCellRenderer<CollectionInstanceEditorGridModel>() {
				
				@Override
				public Object doRender(CollectionInstanceEditorGridModel model, String property, ColumnData config, int rowIndex, 
						int colIndex, ListStore<CollectionInstanceEditorGridModel> store, Grid<CollectionInstanceEditorGridModel> grid) {
					
					String value = model.get(property);
					if (value != null) {
						if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_TEXT)) {
							return value;
						} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_NUMERIC)) {
							return value;
						} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_AUTO_NUMBER)) {
							return value;
						} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_DATE)) {
							Date date = GwtFormatUtils.getDateFromString(GwtFormatConstants.DATE_FOR_SAVING, value);
							return GwtFormatUtils.convertDateToString(GwtFormatConstants.DATE_FOR_DISPLAY, date);
						} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_LIST)) {
							return CollectionField.this.listMetadataItemLabelResolver.getLabelForValue(metadataDefinition.getId(), value);
						} else {
							return null;
						}
					} else {
						if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_AUTO_NUMBER)) {
							return "(" + GwtLocaleProvider.getConstants().AUTO_GENERATED() + ")";
						} else {
							return "(" + GwtLocaleProvider.getConstants().EMPTY() + ")";
						}
					}
				}
			});
			columnConfigList.add(metadataColumnConfig);
		}
		ColumnModel columnModel = new ColumnModel(columnConfigList);
		collectionInstanceEditorGrid = new EditorGrid<CollectionInstanceEditorGridModel>(new ListStore<CollectionInstanceEditorGridModel>(), columnModel);
		collectionInstanceEditorGrid.setSelectionModel(new GridSelectionModel<CollectionInstanceEditorGridModel>());
		
		innerContentPanel.add(collectionInstanceEditorGrid);
		
		setWidgetOfField(innerContentPanel);
	}
	
	/**
	 * Obtine editorul de celula corespunzator metadatei alese.
	 * @param metadataDefinition definitia metadatei
	 * @return editorul de celula corespunzator metadatei alese
	 */
	private CellEditor getEditor(MetadataDefinitionModel metadataDefinition) {
		if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_TEXT)) {
			TextField<String> textField = new TextField<String>();
			return new CellEditor(textField);
		} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_NUMERIC)) {
			NumberField numberField = new NumberField();
			return new CellEditor(numberField) {
				@Override
				public Object preProcessValue(Object value) {
					return (value != null) ? Double.valueOf((String) value) : null;
				}
				@Override
				public Object postProcessValue(Object value) {
					return (value != null) ? ((Double) value).toString() : null;
				}
			};
		} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_AUTO_NUMBER)) {
			TextField<String> textField = new TextField<String>();
			textField.setReadOnly(true);
			return new CellEditor(textField);
		} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_DATE)) {
			DateField dateField = new DateField();
			dateField.getPropertyEditor().setFormat(GwtFormatConstants.GWT_DATE_TIME_FORMATTER_FOR_DISPLAY);
			dateField.setEditable(false);
			return new CellEditor(dateField) {
				@Override
				public Object preProcessValue(Object value) {
					return (value != null) ? GwtFormatUtils.getDateFromString(GwtFormatConstants.DATE_FOR_SAVING, (String) value) : null;
				}
				@Override
				public Object postProcessValue(Object value) {
					return (value != null) ? GwtFormatUtils.convertDateToString(GwtFormatConstants.DATE_FOR_SAVING, (Date) value) : null;
				}
			};
		} else if (metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_LIST)) {
			final ComboBox<ListMetadataItemModel> comboBox = new ComboBox<ListMetadataItemModel>();
			comboBox.setDisplayField(ListMetadataItemModel.PROPERTY_LABEL);
			comboBox.setEditable(false);
			comboBox.setForceSelection(true);
			comboBox.setStore(new ListStore<ListMetadataItemModel>());
			comboBox.setTriggerAction(TriggerAction.ALL);
			comboBox.getStore().add(((ListMetadataDefinitionModel) metadataDefinition).getListItems());
			return new CellEditor(comboBox) {
				@Override
				public Object preProcessValue(Object value) {
					if (value == null) {
						return null;
					}
					return comboBox.getStore().findModel(ListMetadataItemModel.PROPERTY_VALUE, value);
				}
				@Override
				public Object postProcessValue(Object value) {
					if (value == null) {
						return null;
					}
					ListMetadataItemModel item = (ListMetadataItemModel) value;
					return item.getValue();
				}
			};
		} else {
			LabelField labelField = new LabelField();
			return new CellEditor(labelField);
		}
	}

	public List<CollectionInstanceModel> getCollectionInstanceList() {
		List<CollectionInstanceModel> collectionInstanceList = new ArrayList<CollectionInstanceModel>();
		
		List<CollectionInstanceEditorGridModel> collectionInstanceListFromEditorGrid = collectionInstanceEditorGrid.getStore().getModels();
		for (CollectionInstanceEditorGridModel collectionInstanceFromEditorGrid : collectionInstanceListFromEditorGrid) {
			CollectionInstanceModel collectionInstance = new CollectionInstanceModel();
			collectionInstance.setMetadataInstanceList(collectionInstanceFromEditorGrid.getMetadataInstanceList());
			collectionInstanceList.add(collectionInstance);
		}
		
		return collectionInstanceList;
	}
	
	public void addCollectionInstance(CollectionInstanceModel collectionInstance) {
		CollectionInstanceEditorGridModel collectionInstanceFromEditorGrid = new CollectionInstanceEditorGridModel();
		for (MetadataInstanceModel metadataInstance : collectionInstance.getMetadataInstanceList()) {
			collectionInstanceFromEditorGrid.addMetadataInstance(metadataInstance);
		}
		this.autoNumberMetadataValueHandler.updateSequenceValues(collectionInstanceFromEditorGrid);
		this.collectionInstanceEditorGrid.getStore().add(collectionInstanceFromEditorGrid);
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		
		this.readOnly = readOnly;
		
		this.collectionInstanceEditorToolBar.setEnabled(!readOnly && !restrictedOnEdit);
		if (readOnly) {
			this.collectionInstanceEditorGrid.addListener(Events.BeforeEdit, this.cancelEditListener);
		} else {
			this.collectionInstanceEditorGrid.removeListener(Events.BeforeEdit, this.cancelEditListener);
		}
	}
	
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	
	public void setRestrictedOnEdit(boolean restrictedOnEdit) {
		
		this.restrictedOnEdit = restrictedOnEdit;
		
		this.collectionInstanceEditorToolBar.setEnabled(!readOnly && !restrictedOnEdit);
	}
	
	@Override
	public boolean isValid(boolean param) {
		/*
		 * Metoda a trebuit suprascrisa intrucat AdapterField returneaza
		 * intotdeauna true.
		 */
		return validateValue(null);
	}
	
	@Override
	protected boolean validateCustomAdapterField() {

		List<CollectionInstanceEditorGridModel> collectionInstancesFromGrid = this.collectionInstanceEditorGrid.getStore().getModels();
		
		if (mandatory && collectionInstancesFromGrid.isEmpty()) {
			markInvalid(GwtLocaleProvider.getMessages().AT_LEAST_ONE_COLLECTION_INSTANCE_IS_REQUIRED());
			return false;
		}
		
		for (CollectionInstanceEditorGridModel collectionInstanceFromGrid : collectionInstancesFromGrid) {
			for (MetadataDefinitionModel metadataDefinition : this.collectionDefinition.getMetadataDefinitions()) {
				String metadataDefinitionId = metadataDefinition.getId().toString();
				
				boolean isMandatory = GwtMetadataOrCollectionBusinessUtils.isMetadataMandatory(metadataDefinition, this.currentState);
				boolean isNotAutoNumberMetadata = !metadataDefinition.getType().equals(MetadataDefinitionModel.TYPE_AUTO_NUMBER);
				boolean isNotCompleted = !GwtValidateUtils.isCompleted(collectionInstanceFromGrid.getMetadataValue(metadataDefinitionId));
				
				if (isMandatory && isNotAutoNumberMetadata && isNotCompleted) {
					markInvalid(GwtLocaleProvider.getMessages().SOME_REQUIRED_METADATA_VALUES_NOT_COMPLETED());
					return false;
				}
			}
		}
		
		clearInvalid();
		return true;
	}

	private class CollectionInstanceEditorGridModel extends BaseModel {

		private static final long serialVersionUID = -3676655411451175867L;
		
		public static final String PREFIX_METADATA_PROPERTIES = "metadata_";
		
		/**
		 * Obtine ID-urile tuturor definitiilor de metadate ale caror valoare
		 * este completata.
		 * @return o lista cu ID-urile tuturor definitiilor de metadate
		 * ale caror valoare este completata
		 */
		public List<String> getMetadataDefinitionIds() {
			List<String> metadataDefinitionIds = new ArrayList<String>();
			
			for (String propertyName : this.getPropertyNames()) {
				if (propertyName.startsWith(PREFIX_METADATA_PROPERTIES)) {
					String metadataDefinitionId = propertyName.substring(PREFIX_METADATA_PROPERTIES.length());
					metadataDefinitionIds.add(metadataDefinitionId);
				}
			}
			
			return metadataDefinitionIds;
		}
		
		/**
		 * Obtine valoarea metadatei dupa ID-ul definitiei de metadata.
		 * @param metadataDefinitionId ID-ul definitiei de metadata
		 * @return valoarea metadatei
		 */
		public String getMetadataValue(String metadataDefinitionId) {
			return get(PREFIX_METADATA_PROPERTIES + metadataDefinitionId);
		}
		
		/**
		 * Obtine valorile tuturor metadatelor din colectie
		 * @return o lista cu valorile tuturor metadatelor din colectie
		 */
		public List<MetadataInstanceModel> getMetadataInstanceList() {
			List<MetadataInstanceModel> metadataInstanceList = new ArrayList<MetadataInstanceModel>();
			
			for (String propertyName : this.getPropertyNames()) {
				if (propertyName.startsWith(PREFIX_METADATA_PROPERTIES)) {
					String value = (String) get(propertyName);
					// Daca metadata nu are valoare, atunci nu trebuie salvata.
					if (value == null) {
						continue;
					}					
					Long metadataDefinitionId = Long.valueOf(propertyName.substring(PREFIX_METADATA_PROPERTIES.length()));
					MetadataInstanceModel metadataInstance = new MetadataInstanceModel();
					metadataInstance.setMetadataDefinitionId(metadataDefinitionId);
					List<String> values = Arrays.asList(new String[] {value});
					metadataInstance.setValues(values);
					metadataInstanceList.add(metadataInstance);
				}
			}
			
			return metadataInstanceList;
		}
		
		/**
		 * Adauga valoarea metadatei in colectie.
		 * @param metadataInstance metadata
		 */
		public void addMetadataInstance(MetadataInstanceModel metadataInstance) {
			set(PREFIX_METADATA_PROPERTIES + metadataInstance.getMetadataDefinitionId().toString(), metadataInstance.getValues().get(0));
		}
	}
	
	/**
	 * Clasa reprezinta o structura ce se ocupa cu managementul valorilor
	 * metadatelor de tip auto number din colectii.
	 * 
	 * 
	 */
	private class AutoNumberMetadataValueHandler {

		private Map<String, AutoNumberMetadataDefinitionModel> autoNumberMetadataMap;
		private Map<String, Integer> autoNumberMetadataSequenceMap;
		
		public AutoNumberMetadataValueHandler() {
			autoNumberMetadataMap = new HashMap<String, AutoNumberMetadataDefinitionModel>();
			autoNumberMetadataSequenceMap = new HashMap<String, Integer>();
		}
		
		/**
		 * Adauga definitia metadatei de tip auto number specificata in registrul intern.
		 * @param metadataDefinition definitia metadatei de tip auto number
		 */
		public void addMetadataDefinition(AutoNumberMetadataDefinitionModel metadataDefinition) {
			String id = metadataDefinition.getId().toString();
			Integer sequenceValue = new Integer(0);
			this.autoNumberMetadataMap.put(id, metadataDefinition);
			this.autoNumberMetadataSequenceMap.put(id, sequenceValue);
		}
		
		/**
		 * Actualizeaza secventele metadatelor de tip auto number in functie de
		 * valorile din colectia data.
		 * @param collectionInstance colectia
		 */
		public void updateSequenceValues(CollectionInstanceEditorGridModel collectionInstance) {
			List<String> metadataDefinitionIds = collectionInstance.getMetadataDefinitionIds();
			for (String metadataDefinitionId : metadataDefinitionIds) {
				if (this.isAutoNumberMetadata(metadataDefinitionId)) {
					// Ia definitia metadatei.
					AutoNumberMetadataDefinitionModel metadataDefinition = this.autoNumberMetadataMap.get(metadataDefinitionId);
					// Ia elementele utile din definitie.
					String prefix = metadataDefinition.getPrefix();
					int numberLength = metadataDefinition.getNumberLength().intValue();
					// Obtine formatul in care este salvata partea numerica a valorii metadatei.
					String numberFormat = this.getNumberFormat(numberLength);
					// Obtine valoarea metadatei din colectie.
					String metadataValue = collectionInstance.getMetadataValue(metadataDefinitionId);
					// Obtine doar partea numerica a valorii, formatata.
					String formattedNumber = metadataValue.substring(prefix.length());
					// valoarea numerica a metadatei
					int metadataSequenceValue = (int) NumberFormat.getFormat(numberFormat).parse(formattedNumber);
					// ultima valoare din secventa
					int currentSequenceValue = this.autoNumberMetadataSequenceMap.get(metadataDefinitionId).intValue();
					// Daca metadata are o valoare mai mare decat ultima din secventa...
					if (metadataSequenceValue > currentSequenceValue) {
						Integer newSequenceValue = new Integer(metadataSequenceValue);
						// Actualizeaza ultima valoare.
						this.autoNumberMetadataSequenceMap.put(metadataDefinitionId, newSequenceValue);
					}					
				}
			}
		}
		
		/**
		 * Completeaza valorile tuturor metadatelor de tip auto number din
		 * colectie.
		 * @param collectionInstance colectia
		 */
		public void completeAllAutoNumberMetadataFields(CollectionInstanceEditorGridModel collectionInstance) {
			for (String autoNumberMetadataDefinitionId : this.autoNumberMetadataMap.keySet()) {
				String value = this.getNewAutoNumberMetadataValue(autoNumberMetadataDefinitionId);				
				String propertyName = CollectionInstanceEditorGridModel.PREFIX_METADATA_PROPERTIES + autoNumberMetadataDefinitionId;
				collectionInstance.set(propertyName, value);
			}
		}
		
		/**
		 * Verifica daca definitia de metadata este de tip auto number.
		 * @param metadataDefinitionId ID-ul definitiei de metadata
		 * @return <code>true</code> daca definitia este de tip auto number,
		 * <code>false</code> altfel
		 */
		public boolean isAutoNumberMetadata(String metadataDefinitionId) {
			return (this.autoNumberMetadataMap.get(metadataDefinitionId) != null);
		}
		
		/**
		 * Obtine o valoare noua pentru o metadata de tip auto number.
		 * @param metadataDefinitionId ID-ul definitiei metadatei de tip auto
		 * numer
		 * @return valoarea noua pentru o metadata de tip auto numer
		 */
		private String getNewAutoNumberMetadataValue(String metadataDefinitionId) {
			AutoNumberMetadataDefinitionModel metadataDefinition = this.autoNumberMetadataMap.get(metadataDefinitionId);
			
			String prefix = metadataDefinition.getPrefix();
			int numberLength = metadataDefinition.getNumberLength().intValue();
			
			String numberFormat = this.getNumberFormat(numberLength);
			int sequenceValue = this.getNextAutoNumberMetadataSequenceValue(metadataDefinitionId);
			String formattedSequenceValue = NumberFormat.getFormat(numberFormat).format(sequenceValue);
			
			String metadataValue = prefix + formattedSequenceValue;
			
			return metadataValue;
		}
		
		/**
		 * Obtine formatul numarului din valoarea metadatei in functie de
		 * numarul de cifre.
		 * @param numberLength numarul de cifre
		 * @return formatul numarului din valoarea metadatei in functie de
		 * numarul de cifre
		 */
		private String getNumberFormat(int numberLength) {
			StringBuilder numberFormat = new StringBuilder();
			for (int i = 1; i <= numberLength; i++) {
				numberFormat.append("0");
			}
			return numberFormat.toString();
		}
		
		/**
		 * Obtine urmatoarea valoare a secventei definitiei metadatei de tip
		 * auto number, specificata prin ID-ul acesteia.
		 * @param metadataDefinitionId ID-ul definitiei metadatei de tip
		 * auto number
		 * @return urmatoarea valoare a secventei definitiei metadatei de tip
		 * auto number
		 */
		private int getNextAutoNumberMetadataSequenceValue(String metadataDefinitionId) {
			Integer oldSequenceValue = this.autoNumberMetadataSequenceMap.get(metadataDefinitionId);
			Integer newSequenceValue = new Integer(oldSequenceValue.intValue() + 1);
			this.autoNumberMetadataSequenceMap.put(metadataDefinitionId, newSequenceValue);
			return newSequenceValue.intValue();
		}		
	}
	/**
	 * Se ocupa cu obtinerea etichetelor valorilor metadatelor de tip lista.
	 * 
	 * 
	 */
	private class ListMetadataItemLabelResolver {
		
		private Map<Long, Map<String, String>> itemLabelMap;
		
		public ListMetadataItemLabelResolver() {
			this.itemLabelMap = new HashMap<Long, Map<String,String>>();
		}
		
		/**
		 * Adauga informatiile (valoare, eticheta) legate de elementele
		 * din definitia metadatei de tip lista in registrul intern.
		 * @param listMetadataDefinition definitia metadatei de tip lista
		 */
		public void addListMetadataDefinition(ListMetadataDefinitionModel listMetadataDefinition) {
			Map<String, String> valueAndLabelMap = new HashMap<String, String>();
			
			for (ListMetadataItemModel listMetadataItem : listMetadataDefinition.getListItems()) {
				valueAndLabelMap.put(listMetadataItem.getValue(), listMetadataItem.getLabel());
			}
			
			this.itemLabelMap.put(listMetadataDefinition.getId(), valueAndLabelMap);
		}
		
		/**
		 * Obtine eticheta corespunzatoare valorii metadatei.
		 * @param metadataDefinitionId ID-ul definitiei metadatei de tip lista
		 * @param listItemValue valoarea metadatei
		 * @return eticheta corespunzatoare valorii metadatei
		 */
		public String getLabelForValue(Long metadataDefinitionId, String listItemValue) {
			return this.itemLabelMap.get(metadataDefinitionId).get(listItemValue);
		}
	}
}