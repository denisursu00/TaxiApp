package ro.cloudSoft.cloudDoc.presentation.client.admin.documenttypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.constants.NavigationConstants;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentLocationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.DocumentTypeTemplateModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.content.FolderModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtFileUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.PropertyEditors;
import ro.cloudSoft.cloudDoc.presentation.client.shared.validators.FileFieldValidator;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.FormPanelWithIEFix;
import ro.cloudSoft.cloudDoc.presentation.client.shared.widgets.content.FolderSelectionField;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;

public class GeneralTab extends TabItem {
	
	private static final List<String> RESPONSE_MESSAGES_OK = Arrays.asList(new String[] {
		"<pre>OK</pre>",
		"<PRE>OK</PRE>",
		"OK"
	});
	
	private List<String> namesForTemplatesToDelete;

	// elementele principale
	private FormPanel generalForm;
	private FormPanel templatesFormPanel;
	private FieldSet templatesFieldSet;

	// elementele din "generalForm"
	private HiddenField<Long> idHiddenField;
	private TextField<String> nameTextField;
	private TextArea descriptionTextArea;
	private CheckBox keepAllVersionsCheckBox;
	private FolderSelectionField defaultLocationField;
	
	// elementele din "templatesFieldSet"
	private FormPanel addTemplateFormPanel;
	private LayoutContainer existingTemplatesContainer;

	// elementele din "addTemplateFormPanel"
	private Text selectTemplateText;
	private FileUploadField templateFileUploadField;
	private Button addTemplateButton;
	private Text templateDescriptionText;
	private TextField<String> templateDescriptionTextField;

	public GeneralTab() {
		this.namesForTemplatesToDelete = new ArrayList<String>();
		
		setText(GwtLocaleProvider.getConstants().GENERAL());
		setScrollMode(Scroll.AUTO);
		initForm();
		configureGeneralFormValidation();
		this.initTemplateRelatedElements();
	}

	private void initForm() {
		generalForm = new FormPanel();
		generalForm.setHeaderVisible(false);
		this.generalForm.setBodyBorder(false);
		this.generalForm.setBorders(false);
		generalForm.setLabelWidth(100);
		generalForm.setFieldWidth(420);

		idHiddenField = new HiddenField<Long>();
		idHiddenField.setPropertyEditor(PropertyEditors.LONG);

		nameTextField = new TextField<String>();
		nameTextField.setFieldLabel(GwtLocaleProvider.getConstants().NAME());
		this.nameTextField.setMaxLength(DocumentTypeModel.LENGTH_NAME);

		descriptionTextArea = new TextArea();
		descriptionTextArea.setFieldLabel(GwtLocaleProvider.getConstants().DESCRIPTION());
		this.descriptionTextArea.setMaxLength(DocumentTypeModel.LENGTH_DESCRIPTION);
		
		keepAllVersionsCheckBox = new CheckBox();
		keepAllVersionsCheckBox.setBoxLabel("");
		keepAllVersionsCheckBox.setFieldLabel(GwtLocaleProvider.getConstants().KEEP_ALL_VERSIONS());
		
		defaultLocationField = new FolderSelectionField();
		defaultLocationField.setFieldLabel("Default location");

		generalForm.add(idHiddenField);
		generalForm.add(nameTextField);
		generalForm.add(descriptionTextArea);
		generalForm.add(keepAllVersionsCheckBox);
		generalForm.add(defaultLocationField, new FormData("90%"));

		add(generalForm);
	}

	private void configureGeneralFormValidation() {
		nameTextField.setAllowBlank(false);
	}
	
	private void initTemplateRelatedElements() {
		this.templatesFormPanel = new FormPanel();
		this.templatesFormPanel.setBodyBorder(false);
		this.templatesFormPanel.setBorders(false);
		this.templatesFormPanel.setHeaderVisible(false);
		
		this.templatesFieldSet = new FieldSet();
		this.templatesFieldSet.setHeading(GwtLocaleProvider.getConstants().TEMPLATES_FOR_EXPORT());		
		
		this.addTemplateFormPanel = new FormPanelWithIEFix();
		this.addTemplateFormPanel.addListener(Events.Submit, new Listener<FormEvent>() {
			@Override
			public void handleEvent(FormEvent fe) {
				String respone = fe.getResultHtml();
				
				if (GeneralTab.RESPONSE_MESSAGES_OK.contains(respone)) {
					
					String name = GwtFileUtils.getFileNameFromPath(GeneralTab.this.templateFileUploadField.getValue());
					String description = GeneralTab.this.templateDescriptionTextField.getValue();
					
					DocumentTypeTemplateModel template = new DocumentTypeTemplateModel(name, description);
					
					GeneralTab.this.addExistingTemplate(null, template);					
				} else {
					MessageUtils.displayError(GwtLocaleProvider.getMessages().UPLOAD_ATTACHMENT_ERROR());
				}
				
				GeneralTab.this.addTemplateFormPanel.clear();
				GeneralTab.this.addTemplateFormPanel.reset();
				
				LoadingManager.get().loadingComplete();
			}
		});
		this.addTemplateFormPanel.setAction(NavigationConstants.getUploadTemplateLink());
		this.addTemplateFormPanel.setBodyBorder(false);
		this.addTemplateFormPanel.setBorders(false);
		this.addTemplateFormPanel.setEncoding(Encoding.MULTIPART);
		this.addTemplateFormPanel.setHeaderVisible(false);
		this.addTemplateFormPanel.setMethod(Method.POST);

		TableLayout addTemplateFormPanelTableLayout = new TableLayout();
		addTemplateFormPanelTableLayout.setCellPadding(5);
		addTemplateFormPanelTableLayout.setColumns(3);
		this.addTemplateFormPanel.setLayout(addTemplateFormPanelTableLayout);
		
		TableData simpleTableData = new TableData();
		
		TableData doubleTableData = new TableData();
		doubleTableData.setColspan(2);
		
		this.selectTemplateText = new Text();
		this.selectTemplateText.setStyleName("textNormal");
		this.selectTemplateText.setText(GwtLocaleProvider.getConstants().SELECT_TEMPLATE());		
		this.addTemplateFormPanel.add(this.selectTemplateText, simpleTableData);
		
		this.templateFileUploadField = new FileUploadField() {
			@Override
			protected void createFileInput() {
				super.createFileInput();
				/*
				 * Trebuie sa fac asta pentru ca altfel nu se seteaza numele
				 * campului in formular.
				 */
				this.getFileInput().setAttribute("name", "template");
			}
		};
		this.templateFileUploadField.setName("template");
		this.templateFileUploadField.setAllowBlank(false);
		this.templateFileUploadField.setValidator(new FileFieldValidator("jrxml"));
		this.templateFileUploadField.setWidth(320);
		this.addTemplateFormPanel.add(this.templateFileUploadField, simpleTableData);
		
		
		this.addTemplateButton = new Button();
		this.addTemplateButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				if (GeneralTab.this.addTemplateFormPanel.isValid()) {
					LoadingManager.get().loading();
					GeneralTab.this.addTemplateFormPanel.submit();
				}
			}
		});
		this.addTemplateButton.setText(GwtLocaleProvider.getConstants().ADD());
		this.addTemplateFormPanel.add(this.addTemplateButton, simpleTableData);
		
		this.templateDescriptionText = new Text();
		this.templateDescriptionText.setStyleName("textNormal");
		this.templateDescriptionText.setText(GwtLocaleProvider.getConstants().DESCRIPTION());
		this.addTemplateFormPanel.add(this.templateDescriptionText, simpleTableData);
		
		this.templateDescriptionTextField = new TextField<String>();
		this.templateDescriptionTextField.setAllowBlank(false);
		this.templateDescriptionTextField.setName("description");
		this.templateDescriptionTextField.setWidth(320);
		this.addTemplateFormPanel.add(this.templateDescriptionTextField, doubleTableData);
		
		this.templatesFieldSet.add(this.addTemplateFormPanel);
		
		this.existingTemplatesContainer = new LayoutContainer();
		
		TableLayout existingTemplatesContainerTableLayout = new TableLayout();
		existingTemplatesContainerTableLayout.setCellPadding(10);
		existingTemplatesContainerTableLayout.setColumns(2);
		this.existingTemplatesContainer.setLayout(existingTemplatesContainerTableLayout);
		
		this.templatesFieldSet.add(this.existingTemplatesContainer);

		this.templatesFormPanel.add(this.templatesFieldSet);
		
		this.add(this.templatesFormPanel);
	}
	
	private void addExistingTemplate(Long documentTypeId, final DocumentTypeTemplateModel template) {
		final Anchor downloadTemplateLink = new Anchor();
		String parameters = (documentTypeId == null) ? "?templateName=" + template.getName() : "?documentTypeId=" + documentTypeId.toString() + "&templateName=" + template.getName();
		downloadTemplateLink.setHref(NavigationConstants.getDownloadTemplateLink() + parameters);
		downloadTemplateLink.setText(template.toString());
		this.existingTemplatesContainer.add(downloadTemplateLink);
		
		final Button deleteTemplateButton = new Button();
		deleteTemplateButton.setIconStyle("icon-delete");
		deleteTemplateButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				GeneralTab.this.namesForTemplatesToDelete.add(template.getName());
				GeneralTab.this.existingTemplatesContainer.remove(downloadTemplateLink);
				GeneralTab.this.existingTemplatesContainer.remove(deleteTemplateButton);
				GeneralTab.this.existingTemplatesContainer.layout();
			}
		});
		this.existingTemplatesContainer.add(deleteTemplateButton);
		
		this.existingTemplatesContainer.layout();
	}

	private void reset() {
		this.namesForTemplatesToDelete.clear();
		// Resetam formularul.
		generalForm.clear();
		defaultLocationField.resetSelectedFolder();
		this.addTemplateFormPanel.clear();
		//this.addTemplateFormPanel.reset();
		this.existingTemplatesContainer.removeAll();
		this.existingTemplatesContainer.layout();
	}

	public boolean isValid() {
		boolean generalFormIsValid = generalForm.isValid();
		if (!generalFormIsValid) {
			ErrorHelper.addError(GwtLocaleProvider.getMessages().REQUIRED_FIELDS_NOT_COMPLETED());
		}
		return generalFormIsValid;
	}

	public void prepareForAdd() {
		reset();
	}

	public void prepareForEdit(DocumentTypeModel documentType) {
		
		reset();
		
		idHiddenField.setValue(documentType.getId());
		nameTextField.setValue(documentType.getName());
		descriptionTextArea.setValue(documentType.getDescription());
		keepAllVersionsCheckBox.setValue(documentType.isKeepAllVersions());
		
		populateDefaultLocationField(documentType);
		
		for (DocumentTypeTemplateModel template : documentType.getTemplates()) {
			this.addExistingTemplate(documentType.getId(), template);
		}
	}
	
	private void populateDefaultLocationField(DocumentTypeModel documentType) {
		if (documentType.isDefaultLocationSet()) {
			
			final String parentDocumentLocationRealNameForDefaultLocation = documentType.getParentDocumentLocationRealNameForDefaultLocation();
			final String folderIdForDefaultLocation = documentType.getFolderIdForDefaultLocation();
			
			LoadingManager.get().loading();
			GwtServiceProvider.getDocumentLocationService().getDocumentLocationByRealName(parentDocumentLocationRealNameForDefaultLocation, new AsyncCallback<DocumentLocationModel>() {
				
				@Override
				public void onFailure(Throwable exception) {
					MessageUtils.displayError(exception);
					LoadingManager.get().loadingComplete();
				}
				
				@Override
				public void onSuccess(final DocumentLocationModel documentLocation) {
	
					LoadingManager.get().loading();
					GwtServiceProvider.getFolderService().getFolderById(folderIdForDefaultLocation, parentDocumentLocationRealNameForDefaultLocation, new AsyncCallback<FolderModel>() {
						
						@Override
						public void onFailure(Throwable exception) {
							MessageUtils.displayError(exception);
							LoadingManager.get().loadingComplete();
						}
						
						@Override
						public void onSuccess(FolderModel folder) {
							defaultLocationField.setSelectedFolder(documentLocation, folder);
							LoadingManager.get().loadingComplete();
						}
					});
					
					LoadingManager.get().loadingComplete();
				}
			});
		}
	}

	public void populate(DocumentTypeModel documentType) {
		
		documentType.setId(idHiddenField.getValue());
		documentType.setName(nameTextField.getValue());
		documentType.setDescription(descriptionTextArea.getValue());
		documentType.setKeepAllVersions(ComponentUtils.getCheckBoxValue(keepAllVersionsCheckBox));
		documentType.setNamesForTemplatesToDelete(this.namesForTemplatesToDelete);
		
		documentType.setParentDocumentLocationRealNameForDefaultLocation(defaultLocationField.getParentDocumentLocationRealNameOfSelectedFolder());
		documentType.setFolderIdForDefaultLocation(defaultLocationField.getIdOfSelectedFolder());
	}
}