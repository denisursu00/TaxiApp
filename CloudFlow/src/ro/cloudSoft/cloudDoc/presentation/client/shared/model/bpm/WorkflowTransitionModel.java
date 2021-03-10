package ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm;

import java.util.Arrays;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.user.client.rpc.IsSerializable;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.OrganizationEntityModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications.TransitionNotificationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.utils.GwtBooleanUtils;

public class WorkflowTransitionModel extends BaseModel implements IsSerializable {

	private static final long serialVersionUID = 1L;

	// routing types
	public static final String ROUTING_HIERARCHICAL_SUB = "HIERARCHICAL_SUB";
	public static final String ROUTING_HIERARCHICAL_INF = "HIERARCHICAL_INF";
	public static final String ROUTING_HIERARCHICAL_SUP = "HIERARCHICAL_SUP";
	public static final String ROUTING_INITIATOR = "INITIATOR";
	public static final String ROUTING_GROUP = "GROUP";
	public static final String ROUTING_PREVIOUS = "PREVIOUS";
	public static final String ROUTING_MANUAL = "MANUAL";
	public static final String ROUTING_PARAMETER = "PARAMETER";
	public static final String ROUTING_OU = "OU";
	public static final String ROUTING_PARAMETER_HIERARCHICAL_SUP = "PARAMETER_HIERARCHICAL_SUP";
	
	public static final int LENGTH_EMAIL_SUBJECT = 500;
	public static final int LENGTH_EMAIL_CONTENT = 4000;
	
	public List<TransitionRoutingTypeModel> ROUTING_TYPES(){
		/*List<TransitionRoutingTypeModel> list = Arrays.asList(new TransitionRoutingTypeModel[] {
				new TransitionRoutingTypeModel(ROUTING_HIERARCHICAL_SUP, 
						GwtLocaleProvider.getConstants().ROUTING_HIERARCHICAL_SUP()),
				new TransitionRoutingTypeModel(ROUTING_HIERARCHICAL_INF,
						GwtLocaleProvider.getConstants().ROUTING_HIERARCHICAL_INF()),
				new TransitionRoutingTypeModel(ROUTING_HIERARCHICAL_SUB,
						GwtLocaleProvider.getConstants().ROUTING_HIERARCHICAL_SUB()),
				new TransitionRoutingTypeModel(ROUTING_INITIATOR,
						GwtLocaleProvider.getConstants().ROUTING_INITIATOR()),
				new TransitionRoutingTypeModel(ROUTING_GROUP,
						GwtLocaleProvider.getConstants().ROUTING_GROUP()),
				new TransitionRoutingTypeModel(ROUTING_PREVIOUS,
						GwtLocaleProvider.getConstants().ROUTING_PREVIOUS()),
				new TransitionRoutingTypeModel(ROUTING_MANUAL,
						GwtLocaleProvider.getConstants().ROUTING_MANUAL()),
				new TransitionRoutingTypeModel(ROUTING_PARAMETER,
						GwtLocaleProvider.getConstants().ROUTING_PARAMETER()),
				new TransitionRoutingTypeModel(ROUTING_OU,
						GwtLocaleProvider.getConstants().ROUTING_OU())
		});
		return list;*/
		List<TransitionRoutingTypeModel> list = Arrays.asList(new TransitionRoutingTypeModel[] {
				new TransitionRoutingTypeModel(ROUTING_HIERARCHICAL_SUP, ROUTING_HIERARCHICAL_SUP),
				new TransitionRoutingTypeModel(ROUTING_HIERARCHICAL_INF, ROUTING_HIERARCHICAL_INF),
				new TransitionRoutingTypeModel(ROUTING_HIERARCHICAL_SUB, ROUTING_HIERARCHICAL_SUB),
				new TransitionRoutingTypeModel(ROUTING_INITIATOR, ROUTING_INITIATOR),
				new TransitionRoutingTypeModel(ROUTING_GROUP, ROUTING_GROUP),
				new TransitionRoutingTypeModel(ROUTING_PREVIOUS, ROUTING_PREVIOUS),
				new TransitionRoutingTypeModel(ROUTING_MANUAL, ROUTING_MANUAL),
				new TransitionRoutingTypeModel(ROUTING_PARAMETER, ROUTING_PARAMETER),
				new TransitionRoutingTypeModel(ROUTING_OU, ROUTING_OU),
				new TransitionRoutingTypeModel(ROUTING_PARAMETER_HIERARCHICAL_SUP, ROUTING_PARAMETER_HIERARCHICAL_SUP)
		});
		return list;
	}
	
	// deadline
	public static final int DEADLINE_ACTION_NOTIFY = 1;
	public static final int DEADLINE_ACTION_ROUTE = 2;
	
	public List<TransitionDeadlineActionModel> DEADLINE_ACTIONS(){
		/*List<TransitionDeadlineActionModel> list = Arrays.asList(new TransitionDeadlineActionModel[]{
				new TransitionDeadlineActionModel(DEADLINE_ACTION_NOTIFY,
						GwtLocaleProvider.getConstants().DEADLINE_ACTION_NOTIFY()),
				new TransitionDeadlineActionModel(DEADLINE_ACTION_ROUTE,
						GwtLocaleProvider.getConstants().DEADLINE_ACTION_ROUTE())
		});
		return list;*/
		List<TransitionDeadlineActionModel> list = Arrays.asList(new TransitionDeadlineActionModel[]{
				new TransitionDeadlineActionModel(DEADLINE_ACTION_NOTIFY, "DEADLINE_ACTION_NOTIFY"),
				new TransitionDeadlineActionModel(DEADLINE_ACTION_ROUTE, "DEADLINE_ACTION_ROUTE")
		});
		return list;
	}
	
	// properties
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_ROUTING_CONDITION = "routingCondition";
	public static final String PROPERTY_ROUTING_TYPE = "routingType";
	public static final String PROPERTY_ROUTING_DESTINATION_ID = "routingDestinationId";
	public static final String PROPERTY_ROUTING_DESTINATION_PARAMETER = "routingDestinationParameter";
	public static final String PROPERTY_START_STATE = "startState";
	public static final String PROPERTY_FINAL_STATE = "finalState";
	public static final String PROPERTY_AVAILABLE_FOR_AUTOMATIC_ACTIONS_ONLY = "availableForAutomaticActionsOnly";
	public static final String PROPERTY_DEADLINE_ACTION = "deadlineAction";
	public static final String PROPERTY_DEADLINE_PERIOD = "deadlinePeriod";
	public static final String PROPERTY_DEADLINE_ACTION_TYPE = "deadlineActionType";
	public static final String PROPERTY_DEADLINE_NOTIFY_RESEND_INTERVAL = "deadlineNotifyResendInterval";
	public static final String PROPERTY_EXTRA_VIEWERS = "extraViewers";
	public static final String PROPERTY_START_STATE_NAME = "startStateName"; // interfata
	public static final String PROPERTY_START_STATE_CODE = "startStateCode"; // interfata
	public static final String PROPERTY_FINAL_STATE_NAME = "finalStateName"; // interfata
	public static final String PROPERTY_FINAL_STATE_CODE = "finalStateCode"; // interfata
	public static final String PROPERTY_NOTIFICATIONS = "notifications";
	private boolean uiSendConfirmation;
	
	public WorkflowTransitionModel() {
	}
	
	public void setId(Long id){
		set(PROPERTY_ID, id);
	}
	
	public Long getId(){
		return (Long)get(PROPERTY_ID);
	}
	
	public void setName(String name){
		set(PROPERTY_NAME, name);
	}
	
	public String getName(){
		return (String)get(PROPERTY_NAME);
	}
	
	public void setRoutingCondition(String routingCondition){
		set(PROPERTY_ROUTING_CONDITION, routingCondition);
	}
	
	public String getRoutingCondition(){
		return (String)get(PROPERTY_ROUTING_CONDITION);
	}
	
	public void setRoutingType(String routingType){
		set(PROPERTY_ROUTING_TYPE, routingType);
	}
	
	public String getRoutingType(){
		return (String)get(PROPERTY_ROUTING_TYPE);
	}
	
	public void setRoutingDestinationId(Long routingDestinationId){
		set(PROPERTY_ROUTING_DESTINATION_ID, routingDestinationId);
	}
	
	public Long getRoutingDestinationId(){
		return (Long)get(PROPERTY_ROUTING_DESTINATION_ID);
	}
	
	public void setRoutingDestinationParameter(String routingDestinationParameter){
		set(PROPERTY_ROUTING_DESTINATION_PARAMETER, routingDestinationParameter);
	}
	
	public String getRoutingDestinationParameter(){
		return (String)get(PROPERTY_ROUTING_DESTINATION_PARAMETER);
	}
	
	public void setStartState(WorkflowStateModel startState){
		set(PROPERTY_START_STATE, startState);
		setStartStateCode(startState.getCode());
		setStartStateName(startState.getName());
	}
	
	public WorkflowStateModel getStartState(){
		return (WorkflowStateModel)get(PROPERTY_START_STATE);
	}
	
	public void setFinalState(WorkflowStateModel finalState){
		set(PROPERTY_FINAL_STATE, finalState);
		setFinalStateCode(finalState.getCode());
		setFinalStateName(finalState.getName());
	}
	
	public WorkflowStateModel getFinalState(){
		return (WorkflowStateModel)get(PROPERTY_FINAL_STATE);
	}
	
	public void setAvailableForAutomaticActionsOnly(boolean availableForAutomaticActionsOnly) {
		set(PROPERTY_AVAILABLE_FOR_AUTOMATIC_ACTIONS_ONLY, availableForAutomaticActionsOnly);
	}
	
	public boolean isAvailableForAutomaticActionsOnly() {
		Boolean availableForAutomaticActionsOnlyAsObject = get(PROPERTY_AVAILABLE_FOR_AUTOMATIC_ACTIONS_ONLY);
		return GwtBooleanUtils.isTrue(availableForAutomaticActionsOnlyAsObject);
	}
	
	public void setDeadlineAction(Boolean deadlineAction){
		set(PROPERTY_DEADLINE_ACTION, deadlineAction);
	}
	
	public Boolean getDeadlineAction(){
		return (Boolean)get(PROPERTY_DEADLINE_ACTION);
	}
	
	public void setDeadlinePeriod(Integer deadlinePeriod){
		set(PROPERTY_DEADLINE_PERIOD, deadlinePeriod);
	}
	
	public Integer getDeadlinePeriod(){
		return (Integer)get(PROPERTY_DEADLINE_PERIOD);
	}
	
	public void setDeadlineActionType(Integer deadlineActionType){
		set(PROPERTY_DEADLINE_ACTION_TYPE, deadlineActionType);
	}
	
	public Integer getDeadlineActionType(){
		return (Integer)get(PROPERTY_DEADLINE_ACTION_TYPE);
	}
	
	/**
	 * Folosita in interfata.
	 * @param startStateName
	 */
	public void setStartStateName(String startStateName){
		set(PROPERTY_START_STATE_NAME, startStateName);
	}
	
	/**
	 * Folosita in interfata.
	 * @return
	 */
	public String getStartStateName(){
		return (String)get(PROPERTY_START_STATE_NAME);
	}
	
	/**
	 * Folosita in interfata.
	 * @param startStateCode
	 */
	public void setStartStateCode(String startStateCode){
		set(PROPERTY_START_STATE_CODE, startStateCode);
	}
	
	/**
	 * Folosita in interfata.
	 * @return
	 */
	public String getStartStateCode(){
		return (String)get(PROPERTY_START_STATE_CODE);
	}
	
	/**
	 * Folosita in interfata.
	 * @param finalStateName
	 */
	public void setFinalStateName(String finalStateName){
		set(PROPERTY_FINAL_STATE_NAME, finalStateName);
	}
	
	/**
	 * Folosita in interfata.
	 * @return
	 */
	public String getFinalStateName(){
		return (String)get(PROPERTY_FINAL_STATE_NAME);
	}
	
	/**
	 * Folosita in interfata.
	 * @param finalStateCode
	 */
	public void setFinalStateCode(String finalStateCode){
		set(PROPERTY_FINAL_STATE_CODE, finalStateCode);
	}
	
	/**
	 * Folosita in interfata.
	 * @return
	 */
	public String getFinalStateCode(){
		return (String)get(PROPERTY_FINAL_STATE_CODE);
	}
	
	public void setDeadlineNotifyResendInterval(Integer deadlineNotifyResendInterval){
		set(PROPERTY_DEADLINE_NOTIFY_RESEND_INTERVAL, deadlineNotifyResendInterval);
	}
	
	public Integer getDeadlineNotifyResendInterval(){
		return (Integer)get(PROPERTY_DEADLINE_NOTIFY_RESEND_INTERVAL);
	}
	
	public void setExtraViewers(List<OrganizationEntityModel> extraViewers){
		set(PROPERTY_EXTRA_VIEWERS, extraViewers);
	}
	
	public List<OrganizationEntityModel> getExtraViewers(){
		return get(PROPERTY_EXTRA_VIEWERS);
	}
	
	public List<TransitionNotificationModel> getNotifications() {
		return get(PROPERTY_NOTIFICATIONS);
	}
	
	public void setNotifications(List<TransitionNotificationModel> notifications) {
		set(PROPERTY_NOTIFICATIONS, notifications);
	}
	
	public boolean isUiSendConfirmation() {
		return uiSendConfirmation;
	}
	
	public void setUiSendConfirmation(boolean uiSendConfirmation) {
		this.uiSendConfirmation = uiSendConfirmation;
	}
	
	/**
	 * 
	 * Clasa folosita in interfata pentru afisarea actiuni de expirare.
	 *
	 */
	public static class TransitionDeadlineActionModel extends BaseModel implements IsSerializable {
		private static final long serialVersionUID = 1L;
		
		public static final String LABEL = "label";
		public static final String VALUE = "value";
		
		public TransitionDeadlineActionModel()
		{
		    
		}
		public TransitionDeadlineActionModel(Integer value, String label) {
			setValue(value);
			setLabel(label);
		}
		
		public void setLabel(String label){
			set(LABEL, label);
		}
		
		public String getLabel(){
			return (String)get(LABEL);
		}
		
		public void setValue(Integer value){
			set(VALUE, value);
		}
		
		public Integer getValue(){
			return (Integer)get(VALUE);
		}
		
	}
	
	/**
	 * 
	 * Clasa folosita pentru interfata la afisarea tipului de rutare a tranzitiei.
	 * 
	 */
	public static class TransitionRoutingTypeModel extends BaseModel implements IsSerializable {
		private static final long serialVersionUID = 1L;
		
		public static final String LABEL = "label";
		public static final String VALUE = "value";
		
		public TransitionRoutingTypeModel()
		{
		    
		}
		public TransitionRoutingTypeModel(String value, String label) {
			setValue(value);
			setLabel(label);			
		}
		
		public void setLabel(String label){
			set(LABEL, label);
		}
		
		public String getLabel(){
			return (String)get(LABEL);
		}
		
		public void setValue(String value){
			set(VALUE, value);
		}
		
		public String getValue(){
			return (String)get(VALUE);
		}
		
	}
	
}