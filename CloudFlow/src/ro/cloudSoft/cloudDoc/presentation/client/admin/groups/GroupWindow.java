package ro.cloudSoft.cloudDoc.presentation.client.admin.groups;

import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.admin.GroupsPanel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.GroupModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtServiceProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.SorterProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ErrorHelper;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.MessageUtils;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GroupWindow extends Window {
	
	private static final int WINDOW_ORIGINAL_WIDTH = 1024;
	private static final int WINDOW_ORIGINAL_HEIGHT = 500;
	private static final int LEFT_PANEL_ORIGINAL_WIDTH =  450;
	private static final int LEFT_PANEL_ORIGINAL_HEIGHT =  470;
	private static final int CENTER_PANEL_ORIGINAL_WIDTH = 176;
	private static final int CENTER_PANEL_ORIGINAL_HEIGHT =  470;
	private static final int RIGHT_PANEL_ORIGINAL_WIDTH =  400;
	private static final int RIGHT_PANEL_ORIGINAL_HEIGHT =  470;
	private static final int FORM_PANEL_ORIGINAL_WIDTH = 450;
	private static final int FORM_PANEL_ORIGINAL_HEIGHT = 200;
	private static final int AVAILABLE_PANEL_ORIGINAL_WIDTH =  450;
	private static final int AVAILABLE_PANEL_ORIGINAL_HEIGHT =  265;
	
	
	private GroupsPanel mainWindow;
	private VerticalPanel leftPanel;
	private VerticalPanel centerPanel;
	private ContentPanel availableUsersPanel;
	private Button addButton;
	private Button removeButton;
	private Button addAllButton;
	private Button removeAllButton;
	private ContentPanel rightPanel;
	private HorizontalPanel mainPanel;
	private FormPanel form;
	private HiddenField<String> hiddenIdField;
	private TextField<String> nameTextField;
	private TextArea descriptionArea;
	private StoreFilterField<UserModel> usersFilterField;
	private ListView<UserModel> availableUsers;
	private ListView<UserModel> members;
	private Button saveButton;
	private Button cancelButton;
	

	public GroupWindow(GroupsPanel parentWindow) {	
		mainWindow = parentWindow;		
		initWindow();
		initMainPanel();
		initInnerPanels();
		initToolBar();	
		initDoubleClickActions();
		addListener(Events.Resize, new Listener<WindowEvent>(){
			public void handleEvent(WindowEvent be) {
				calculateResizePercentage(be.getWidth(), be.getHeight());				
			}			
		});
	}
	
	private void initWindow() {
		//setHeading(GwtLocaleProvider.getConstants().GROUP());
		setSize(WINDOW_ORIGINAL_WIDTH, WINDOW_ORIGINAL_HEIGHT);
		setMinWidth(640);
		setMinHeight(390);
		setMaximizable(true);
		setModal(true);
		setLayout(new FitLayout());		
	}
	
	private void initMainPanel(){
		mainPanel = new HorizontalPanel();
		add(mainPanel);
	}
	
	private void initInnerPanels(){		
		leftPanel = new VerticalPanel();
		leftPanel.setSize(LEFT_PANEL_ORIGINAL_WIDTH, LEFT_PANEL_ORIGINAL_HEIGHT);
		
		centerPanel = new VerticalPanel();
		centerPanel.setSpacing(20);
		centerPanel.setSize(CENTER_PANEL_ORIGINAL_WIDTH, CENTER_PANEL_ORIGINAL_HEIGHT);
		//centerPanel.setVerticalAlign(VerticalAlignment.MIDDLE);		
		//centerPanel.setPagePosition(401, 300);		
		//centerPanel.setHorizontalAlign(HorizontalAlignment.CENTER);
				
		rightPanel = new ContentPanel();
		//rightPanel.setHeading(GwtLocaleProvider.getConstants().GROUP_MEMBERS());
		rightPanel.setHeaderVisible(false);
		rightPanel.setLayout(new FitLayout());
		rightPanel.setBorders(true);
		rightPanel.setSize(RIGHT_PANEL_ORIGINAL_WIDTH, RIGHT_PANEL_ORIGINAL_HEIGHT);
		
		initForm();
		initUsersFilterField();
		initAvailableUsers();	
		usersFilterField.bind(availableUsers.getStore());
		initButtons();
		initMembers();		
		
		mainPanel.add(leftPanel);	
		mainPanel.add(centerPanel, new TableData(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE));
		mainPanel.add(rightPanel);
	}
	
	private void  initForm(){
		form = new FormPanel();
		form.setSize(FORM_PANEL_ORIGINAL_WIDTH, FORM_PANEL_ORIGINAL_HEIGHT);
		form.setHeaderVisible(false);
		hiddenIdField = new HiddenField<String>();
		
		nameTextField = new TextField<String>();
		nameTextField.setFieldLabel(GwtLocaleProvider.getConstants().NAME());
		nameTextField.setAllowBlank(false);
		this.nameTextField.setMaxLength(GroupModel.LENGTH_NAME);
		
		descriptionArea = new TextArea();
		descriptionArea.setFieldLabel(GwtLocaleProvider.getConstants().DESCRIPTION());
		this.descriptionArea.setMaxLength(GroupModel.LENGTH_DESCRIPTION);
		
		form.add(hiddenIdField);
		form.add(nameTextField);
		form.add(descriptionArea);
		leftPanel.add(form);
	}
	
	private void initUsersFilterField() {		
		usersFilterField = new StoreFilterField<UserModel>(){
			protected boolean doSelect(Store<UserModel> store,
					UserModel parent, UserModel record,
					String property, String filter) {				
				if ( record instanceof UserModel ){
					UserModel candidateUser = (UserModel) record;
					String candidateUserName = candidateUser.getName().toLowerCase();
					String textToMatch = filter.toLowerCase();
					if (candidateUserName.contains(textToMatch)) {
						return true;
					}
				}				
				return false;
			}			
		};
		usersFilterField.setFieldLabel(GwtLocaleProvider.getConstants().SEARCH_USER());
		//usersFilterField.setHideLabel(true);
		usersFilterField.setWidth(380);
		form.add(usersFilterField);
	}
	
	private void initAvailableUsers(){
		availableUsersPanel = new ContentPanel(new FitLayout());
		//availableUsersPanel.setHeading(GwtLocaleProvider.getConstants().USERS_AVAILABLE());
		availableUsersPanel.setHeaderVisible(false);
		availableUsersPanel.setSize(AVAILABLE_PANEL_ORIGINAL_WIDTH, AVAILABLE_PANEL_ORIGINAL_HEIGHT);		
		availableUsers = new ListView<UserModel>(new ListStore<UserModel>());
		availableUsers.getStore().setStoreSorter(SorterProvider.getUserListSorter());
		availableUsers.setDisplayProperty(UserModel.USER_PROPERTY_DISPLAY_NAME);
		availableUsers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		availableUsersPanel.add(availableUsers);
		leftPanel.add(availableUsersPanel);		
	}
	
	private void initButtons(){
		addButton = new Button(GwtLocaleProvider.getConstants().ADD());
		addButton.setSize(72, 36);
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				if ( availableUsers.getSelectionModel().getSelectedItem() != null )
					//if ( members.getStore().findModel( availableUsers.getSelectionModel().getSelectedItem() ) != null )
					if ( members.getStore().findModel( UserModel.USER_PROPERTY_USERID, 
							availableUsers.getSelectionModel().getSelectedItem().getUserId() ) != null )
						 MessageUtils.displayError(GwtLocaleProvider.getMessages().USER_ALREADY_IN_GROUP());
					else members.getStore().add( (UserModel) availableUsers.getSelectionModel().getSelectedItem() );
			}			
		});
		removeButton = new Button(GwtLocaleProvider.getConstants().REMOVE());
		removeButton.setSize(72, 36);
		removeButton.addSelectionListener( new SelectionListener<ButtonEvent>(){
			@Override
			public void componentSelected(ButtonEvent ce) {
				if ( members.getSelectionModel().getSelectedItem() != null )
					members.getStore().remove( (UserModel) members.getSelectionModel().getSelectedItem() );				
			}			
		});		
		
		addAllButton = new Button(GwtLocaleProvider.getConstants().ADD_ALL());
		addAllButton.setSize(72, 36);
		addAllButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				for (UserModel userModel : availableUsers.getStore().getModels()){
					if (members.getStore().findModel(UserModel.USER_PROPERTY_USERID, userModel.getUserId()) == null)
						members.getStore().add(userModel);
				}				
			}			
		});
		
		removeAllButton = new Button(GwtLocaleProvider.getConstants().REMOVE_ALL());
		removeAllButton.setSize(72, 36);
		removeAllButton.addSelectionListener(new SelectionListener<ButtonEvent>(){
			public void componentSelected(ButtonEvent ce) {
				members.getStore().removeAll();				
			}			
		});
		centerPanel.add(addButton, new TableData(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE));
		centerPanel.add(addAllButton, new TableData(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE));
		centerPanel.add(removeButton, new TableData(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE));
		centerPanel.add(removeAllButton, new TableData(HorizontalAlignment.CENTER, VerticalAlignment.MIDDLE));
	}
	
	private void initMembers(){
		members = new ListView<UserModel>(new ListStore<UserModel>());
		members.getStore().setStoreSorter(SorterProvider.getUserListSorter());
		members.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		members.setDisplayProperty(UserModel.USER_PROPERTY_DISPLAY_NAME);		
		members.setBorders(false);
		rightPanel.add(members);
	}
	
	private void initToolBar(){
		//toolBar = new ToolBar();		
		ButtonBar buttonBar = getButtonBar();
		saveButton = new Button(GwtLocaleProvider.getConstants().SAVE());
		cancelButton = new Button( GwtLocaleProvider.getConstants().CANCEL());
		saveButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {								
				if (isValid()){
					LoadingManager.get().loading();
					GwtServiceProvider.getOrgService().getGroups(new AsyncCallback<List<GroupModel>>(){
						@Override
						public void onFailure(Throwable exception) {
							MessageUtils.displayError(exception);
							LoadingManager.get().loadingComplete();
						}
						@Override
						public void onSuccess(List<GroupModel> groups) {
							boolean valid = true;
							for (GroupModel groupModel : groups){ // TODO de modificat verificarea numelui daca o sa am tagsOut si pentru el
								if (groupModel.getName().equalsIgnoreCase(nameTextField.getValue()) &&  !groupModel.getId().equalsIgnoreCase(hiddenIdField.getValue())){
									valid = false;
									ErrorHelper.addError(GwtLocaleProvider.getMessages().GROUP_ALREADY_EXISTS());
									break;
								}	
							}
							if (valid) {
								GwtServiceProvider.getOrgService().setGroup(getGroupModelFromForm(),
										members.getStore().getModels(), new AsyncCallback<String>() {
									@Override
									public void onFailure(Throwable exception) {
										MessageUtils.displayError(exception);
										LoadingManager.get().loadingComplete();
									}
									@Override
									public void onSuccess(String arg0) {										
										MessageUtils.display(GwtLocaleProvider.getConstants().SUCCESS(), GwtLocaleProvider.getMessages().GROUP_SAVED());
										mainWindow.refresh();
										LoadingManager.get().loadingComplete();
										hide();
									}					
								});								
							} else {								
								ErrorHelper.displayErrors();
								LoadingManager.get().loadingComplete();
							}
						}			
					});					
				}				
				else ErrorHelper.displayErrors();
				
			}			
		});
		cancelButton.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {				
				/*if (id == null)
					prepareForAdd();
				else prepareForEdit(id);*/
					
				//reset();
				hide();
			}
			
		});
		
		buttonBar.add(saveButton);
		buttonBar.add(cancelButton);
		buttonBar.setAlignment(HorizontalAlignment.RIGHT);
		//this.setBottomComponent(toolBar);
		
	}

	public void prepareForAdd() {
		LoadingManager.get().loading();
		reset();
		populateAvailableUsers();
		setHeading(GwtLocaleProvider.getConstants().GROUP()+" : ");
		LoadingManager.get().loadingComplete();	
	}

	public void prepareForEdit(final String id) {

		LoadingManager.get().loading();
		GwtServiceProvider.getOrgService().getGroupById(id, new AsyncCallback<GroupModel>() {
			
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			
			@Override
			public void onSuccess(GroupModel group) {
				
				reset();
				populateForm(group);
				setHeading(GwtLocaleProvider.getConstants().GROUP() + " : " + group.getName());
				
				LoadingManager.get().loading();
				GwtServiceProvider.getOrgService().getUsersFromGroup(id, new AsyncCallback<List<UserModel>>() {
					
					@Override
					public void onFailure(Throwable exception) {
						MessageUtils.displayError(exception);
						LoadingManager.get().loadingComplete();
					}
					
					@Override
					public void onSuccess(List<UserModel> userModels) {				
						members.getStore().add(userModels);
						LoadingManager.get().loadingComplete();
					}			
				});
				populateAvailableUsers();	
				
				LoadingManager.get().loadingComplete();
			}
		});
	}
	
	private void populateForm(GroupModel groupModel){
		hiddenIdField.setValue(groupModel.getId());
		nameTextField.setValue(groupModel.getName());
		//nameTextField.setValue(tagsIn(groupModel.getName()));
		descriptionArea.setValue(groupModel.getDescription() != null ? tagsIn(groupModel.getDescription()) : groupModel.getDescription());
	}
	
	private void reset(){
		form.clear();
		members.getStore().removeAll();
		availableUsers.getStore().removeAll();
	}
	
	private void populateAvailableUsers() {
		LoadingManager.get().loading();
		GwtServiceProvider.getOrgService().getUsers(new AsyncCallback<List<UserModel>>() {
			@Override
			public void onFailure(Throwable exception) {
				MessageUtils.displayError(exception);
				LoadingManager.get().loadingComplete();
			}
			@Override
			public void onSuccess(List<UserModel> arg0) {
				show();
				availableUsers.getStore().add(arg0);
				LoadingManager.get().loadingComplete();
			}			
		});
	}
	
	private GroupModel getGroupModelFromForm(){
		GroupModel model = new GroupModel();
		model.setId(hiddenIdField.getValue());
		model.setName(nameTextField.getValue());
		//model.setName(tagsOut(nameTextField.getValue()));		
		model.setDescription( descriptionArea.getValue() != null ? tagsOut(descriptionArea.getValue()) : descriptionArea.getValue());
		return model;
	}
	 
	private boolean isValid(){
		boolean formIsValid = form.isValid();
		if ( nameTextField.getValue().trim().length()<=0)
			formIsValid = false;
		if (!formIsValid) {
			ErrorHelper.addError(GwtLocaleProvider.getMessages().REQUIRED_FIELDS_NOT_COMPLETED());
		}
		return formIsValid;
	} 
	
	private String tagsOut(String input){		
		String result = input.replace("<", "&lt;");			
		return result.replace(">", "&gt;");
	}
	
	private String tagsIn(String input){		
		String result = input.replace("&lt;", "<");
		return result.replace("&gt;", ">");
	}
	
	private void initDoubleClickActions(){
		availableUsers.addListener(Events.DoubleClick, new Listener<ListViewEvent<UserModel>>(){
			public void handleEvent(ListViewEvent<UserModel> listViewEvent) {
				if ( availableUsers.getSelectionModel().getSelectedItem() != null )
					//if ( members.getStore().findModel( availableUsers.getSelectionModel().getSelectedItem() ) != null )
					if ( members.getStore().findModel( UserModel.USER_PROPERTY_USERID, 
							availableUsers.getSelectionModel().getSelectedItem().getUserId() ) != null )
						 MessageUtils.displayError(GwtLocaleProvider.getMessages().USER_ALREADY_IN_GROUP());
					else members.getStore().add( (UserModel) availableUsers.getSelectionModel().getSelectedItem() );				
			}			
		});
		members.addListener(Events.DoubleClick, new Listener<ListViewEvent<UserModel>>(){
			public void handleEvent(ListViewEvent<UserModel> listViewEvent) {
				if ( members.getSelectionModel().getSelectedItem() != null )
					members.getStore().remove( (UserModel) members.getSelectionModel().getSelectedItem() );					
			}			
		});
	}
	
	private void calculateResizePercentage(int w, int h){
		float widthP = (float)w / (float)WINDOW_ORIGINAL_WIDTH;
		float heightP = (float)h / (float)WINDOW_ORIGINAL_HEIGHT;		
		changeSize(widthP, heightP);
	}
	
	public void changeSize(float wIndex, float hIndex){		
		if (wIndex < 0.70){
			form.setWidth(Math.round(FORM_PANEL_ORIGINAL_WIDTH * 0.70f));
			availableUsersPanel.setWidth(Math.round(AVAILABLE_PANEL_ORIGINAL_WIDTH * 0.70f));
			leftPanel.setWidth(Math.round(LEFT_PANEL_ORIGINAL_WIDTH * 0.70f));
			
			centerPanel.setWidth(Math.round(CENTER_PANEL_ORIGINAL_WIDTH * (wIndex)));
			rightPanel.setWidth(Math.round(RIGHT_PANEL_ORIGINAL_WIDTH * (wIndex))-50);
			
		} else{
			form.setWidth(Math.round(FORM_PANEL_ORIGINAL_WIDTH * wIndex));
			availableUsersPanel.setWidth(Math.round(AVAILABLE_PANEL_ORIGINAL_WIDTH * wIndex));
			leftPanel.setWidth(Math.round(LEFT_PANEL_ORIGINAL_WIDTH * wIndex));
			centerPanel.setWidth(Math.round(CENTER_PANEL_ORIGINAL_WIDTH * wIndex));
			rightPanel.setWidth(Math.round(RIGHT_PANEL_ORIGINAL_WIDTH * wIndex)-19);
		}
		
		if (hIndex < 1){
			form.setHeight(Math.round(FORM_PANEL_ORIGINAL_HEIGHT * hIndex)-22);
			availableUsersPanel.setHeight(Math.round(AVAILABLE_PANEL_ORIGINAL_HEIGHT * hIndex)-22);
			leftPanel.setHeight(Math.round(LEFT_PANEL_ORIGINAL_HEIGHT * hIndex)-43);			
			centerPanel.setHeight(Math.round(CENTER_PANEL_ORIGINAL_HEIGHT * hIndex)-3);
			rightPanel.setHeight(Math.round(RIGHT_PANEL_ORIGINAL_HEIGHT * hIndex)-42);			
		}			
		else {
			form.setHeight(Math.round(FORM_PANEL_ORIGINAL_HEIGHT * hIndex)-20);
			availableUsersPanel.setHeight(Math.round(AVAILABLE_PANEL_ORIGINAL_HEIGHT*hIndex)-22);
			leftPanel.setHeight(Math.round(LEFT_PANEL_ORIGINAL_HEIGHT * hIndex)-41);			
			centerPanel.setHeight(Math.round(CENTER_PANEL_ORIGINAL_HEIGHT * hIndex)-35);
			rightPanel.setHeight(Math.round(RIGHT_PANEL_ORIGINAL_HEIGHT * hIndex)-42);
		}
		layout();
	}
	
}
