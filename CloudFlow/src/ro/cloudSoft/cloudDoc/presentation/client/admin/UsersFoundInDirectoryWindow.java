package ro.cloudSoft.cloudDoc.presentation.client.admin;

import java.util.LinkedList;
import java.util.List;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.directory.organization.DirectoryUserModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.SorterProvider;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentUtils;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.ComponentWithRenderingProblems;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.LoadingManager;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * 
 */
public class UsersFoundInDirectoryWindow extends Window implements ComponentWithRenderingProblems {
	
	private String organizationIdAsString;
	private String organizationUnitIdAsString;
	
	private Grid<DirectoryUserModel> usersFoundInDirectoryGrid;
	private UserDetailsWindow userDetailsWindow = new UserDetailsWindow();

	public UsersFoundInDirectoryWindow() {
		
		setHeading(GwtLocaleProvider.getConstants().SEARCH_RESULTS());
		setMaximizable(false);
		setModal(true);
		setSize(840, 600);
		setLayout(new FitLayout());
		
		ColumnConfig usernameColumnConfig = new ColumnConfig();
		usernameColumnConfig.setId(DirectoryUserModel.PROPERTY_USERNAME);
		usernameColumnConfig.setHeader(GwtLocaleProvider.getConstants().USERNAME());
		usernameColumnConfig.setWidth(220);
		
		ColumnConfig firstNameColumnConfig = new ColumnConfig();
		firstNameColumnConfig.setId(DirectoryUserModel.PROPERTY_FIRST_NAME);
		firstNameColumnConfig.setHeader(GwtLocaleProvider.getConstants().FIRST_NAME());
		firstNameColumnConfig.setWidth(120);
		
		ColumnConfig lastNameColumnConfig = new ColumnConfig();
		lastNameColumnConfig.setId(DirectoryUserModel.PROPERTY_LAST_NAME);
		lastNameColumnConfig.setHeader(GwtLocaleProvider.getConstants().LAST_NAME());
		lastNameColumnConfig.setWidth(120);
		
		ColumnConfig emailColumnConfig = new ColumnConfig();
		emailColumnConfig.setId(DirectoryUserModel.PROPERTY_EMAIL);
		emailColumnConfig.setHeader(GwtLocaleProvider.getConstants().EMAIL());
		emailColumnConfig.setWidth(120);
		
		ColumnConfig titleColumnConfig = new ColumnConfig();
		titleColumnConfig.setId(DirectoryUserModel.PROPERTY_TITLE);
		titleColumnConfig.setHeader(GwtLocaleProvider.getConstants().TITLE());
		titleColumnConfig.setWidth(120);

		List<ColumnConfig> columnConfiguration = new LinkedList<ColumnConfig>();
		
		columnConfiguration.add(usernameColumnConfig);
		columnConfiguration.add(firstNameColumnConfig);
		columnConfiguration.add(lastNameColumnConfig);
		columnConfiguration.add(emailColumnConfig);
		columnConfiguration.add(titleColumnConfig);
		
		ColumnModel columnModel = new ColumnModel(columnConfiguration);
	
		usersFoundInDirectoryGrid = new Grid<DirectoryUserModel>( new ListStore<DirectoryUserModel>(), columnModel);
		usersFoundInDirectoryGrid.getStore().setStoreSorter(SorterProvider.getDirectoryUserStoreSorter());
		usersFoundInDirectoryGrid.getSelectionModel().setSelectionMode( SelectionMode.SINGLE );
		
		usersFoundInDirectoryGrid.setAutoExpandColumn(usernameColumnConfig.getId());
		usersFoundInDirectoryGrid.setAutoExpandMax(2000);
		
		usersFoundInDirectoryGrid.setStripeRows(true);
		
		usersFoundInDirectoryGrid.addListener(Events.RowDoubleClick, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				DirectoryUserModel selectedUser = usersFoundInDirectoryGrid.getSelectionModel().getSelectedItem();
				if (selectedUser != null) {
					userDetailsWindow.prepareForImport(selectedUser, organizationIdAsString, organizationUnitIdAsString);
					hide();
				}
			}
		});
		
		add(usersFoundInDirectoryGrid);
		
		LoadingManager.get().registerComponentWithRenderingProblems(this);
	}
	
	public void showFoundUsers(List<DirectoryUserModel> foundUsers, 
			String organizationIdAsString, String organizationUnitIdAsString) {
		
		this.organizationIdAsString = organizationIdAsString;
		this.organizationUnitIdAsString = organizationUnitIdAsString;
		
		usersFoundInDirectoryGrid.getStore().removeAll();
		usersFoundInDirectoryGrid.getStore().add(foundUsers);
		
		show();
	}
	
	@Override
	public boolean needsEnsuringIsProperlyRenderedNow() {
		return isVisible();
	}
	
	@Override
	public void ensureIsProperlyRendered() {
		ComponentUtils.ensureGridIsProperlyRendered(usersFoundInDirectoryGrid);
	}
}