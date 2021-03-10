package ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ListItemModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

public class WorkflowStateModel extends BaseModel implements ListItemModel, IsSerializable {

	private static final long serialVersionUID = 1L;
	
	public static final int STATETYPE_START = 1;
	public static final int STATETYPE_INTERMEDIATE = 2;
	public static final int STATETYPE_STOP = 3;
	
	public final static int ATTACH_PERM_ADD = 1;
	public final static int ATTACH_PERM_DELETE = 2;
	public final static int ATTACH_PERM_ALL = 3;
	
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_TEMP_ID = "tempId";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_CODE = "code";
	public static final String PROPERTY_STATE_TYPE = "stateType";
	public static final String PROPERTY_DISPLAY_STATE_TYPE = "displayStateType";
	public static final String PROPERTY_ATTACHMENTS_PERMISSION = "attachmentsPermission";
	public static final String PROPERTY_DISPLAY_STATE = "displayState";
	
	public static final String PROPERTY_AUTOMATIC_RUNNING = "automaticRunning";
	public static final String PROPERTY_CLASS_PATH = "classPath";
	
	public WorkflowStateModel() {
	}
	
	public void setId(Long id){
		set(PROPERTY_ID, id);
	}
	
	public Long getId(){
		return (Long)get(PROPERTY_ID);
	}
	
	public void setName(String name){
		set(PROPERTY_NAME, name);
		setDisplayState();
	}
	
	public String getName(){
		return (String)get(PROPERTY_NAME);
	}
	
	public void setCode(String code){
		set(PROPERTY_CODE, code);
		setDisplayState();
	}
	
	public String getCode(){
		return (String)get(PROPERTY_CODE);
	}
	
	public void setStateType(Integer stateType){
		set(PROPERTY_STATE_TYPE, stateType);
	}
	
	public Integer getStateType(){
		return (Integer)get(PROPERTY_STATE_TYPE);
	}
	
	public void setAttachmentsPermission(Integer attachmentsPermission){
		set(PROPERTY_ATTACHMENTS_PERMISSION, attachmentsPermission);
	}
	
	public Integer getAttachmentsPermission(){
		return (Integer)get(PROPERTY_ATTACHMENTS_PERMISSION);
	}
	
	public void setTempId(Integer tempId){
		set(PROPERTY_TEMP_ID, tempId);
	}
	
	public Integer getTempId(){
		return (Integer)get(PROPERTY_TEMP_ID);
	}
	
	/**
	 * Folosit pentru interfata.
	 * @param stateType
	 */
	public void setDisplayStateType(int stateType){
		switch (stateType) {
		case STATETYPE_START:
			set(PROPERTY_DISPLAY_STATE_TYPE, GwtLocaleProvider.getConstants().STATE_START_TYPE());
			break;
		case STATETYPE_INTERMEDIATE:
			set(PROPERTY_DISPLAY_STATE_TYPE, GwtLocaleProvider.getConstants().STATE_INTERMEDIATE_TYPE());
			break;
		case STATETYPE_STOP:
			set(PROPERTY_DISPLAY_STATE_TYPE, GwtLocaleProvider.getConstants().STATE_STOP_TYPE());
			break;
		default:
			break;
		}
	}
	
	/**
	 * Folosit pentru interfata.
	 * @return
	 */
	public String getDisplayStateType(){
		return (String)get(PROPERTY_DISPLAY_STATE_TYPE);
	}
	
	public void setDisplayState() {
		if (getCode() != null && getName() != null)
			set(PROPERTY_DISPLAY_STATE, getName() + "(" + getCode() + ")");
	}
	
	public String getDisplayState() {
		return get(PROPERTY_DISPLAY_STATE);
	}
	
	@Override
	public String getItemValue() {
		return getCode();
	}
	
	@Override
	public String getItemLabel() {
		return getName();
	}

	public boolean isAutomaticRunning() {
		return get(PROPERTY_AUTOMATIC_RUNNING);
	}

	public void setAutomaticRunning(boolean automaticRunning) {
		set(PROPERTY_AUTOMATIC_RUNNING, automaticRunning);
	}

	public String getClassPath() {
		return get(PROPERTY_CLASS_PATH);
	}

	public void setClassPath(String classPath) {
		set(PROPERTY_CLASS_PATH, classPath);
	}
}
