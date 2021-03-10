package ro.cloudSoft.cloudDoc.domain.bpm;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import ro.cloudSoft.cloudDoc.domain.bpm.notifications.TransitionNotification;
import ro.cloudSoft.cloudDoc.domain.organization.OrganizationEntity;

import com.google.common.collect.Sets;

/**
 * Entity implementation class for Entity: WorkflowTransition
 * 
 * 
 */
@Entity
@Table( uniqueConstraints = {@UniqueConstraint(columnNames={"name", "workflow_id"})} )
public class WorkflowTransition implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
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
	
	public static final int DEADLINE_ACTION_NOTIFY = 1;
	public static final int DEADLINE_ACTION_ROUTE = 2;

	private Long id;

	private String name;

	/**
	 * Rutare conditionata (da/nu) – daca este bifata optiunea “da” atunci se
	 * vor completa alte campuri suplimentare ce descriu conditia de rutare.
	 * Administratorul va putea defini conditii complexe utilizand operatori
	 * relationali, aritmetici si logici. Ca operanzi vor fi utilizate campurile
	 * de pe metadocument, campurile putand fi scrise utilizand tag-uri
	 * speciale.
	 * 
	 * Bifa va exista doar in modelul din interfata, in baza doar salvam
	 * conditia, scrisa in JSP EL (pentru a putea fi interpretata de JBPM)
	 * 
	 */
	private String routingCondition;

	/**
	 * O sa fie o constanta dintr-un enum cu urmatoarele valori:<br>
	 * - HIERARCHICAL_SUB<br>
	 * - HIERARCHICAL_INF<br> 
	 * - HIERARCHICAL_SUP<br>
	 * - INITIATOR<br>
	 * - GROUP<br>
	 * - PREVIOUS<br>
	 * - MANUAL<br>
	 * - PARAMETER<br>
	 * - OU
	 * - PARAMETER_HIERARCHICAL_SUP
	 */
	private String routingType;

	/**
	 * Daca tipul de rutare este GROUP sau OU, va salva id-ul userului/unitatii
	 * organizationale
	 */
	private Long routingDestinationId;

	/**
	 * Daca tipul rutarii este PARAMETER sau USER_HIERARCHICAL_SUP, va salva valoarea parametrului
	 */
	private String routingDestinationParameter;

	private WorkflowState startState;
	
	private WorkflowState finalState;
	
	private Set<OrganizationEntity> extraViewers;
	
	private boolean availableForAutomaticActionsOnly;
	
	/**
	 * Actiune automata la expirare (da/nu)
	 */
	private Boolean deadlineAction;

	/**
	 * Numarul de zile dupa care se considera expirata
	 */
	private Integer deadlinePeriod;

	/**
	 * Constanta care va reprezenta una din urmatoarele: - Rutare automata catre
	 * urmatorul pas din flux; - Notificarea unui utilizator, grupuri de
	 * utilizatori, unitati organizatorice;
	 */
	private Integer deadlineActionType;
	
	private Integer deadlineNotifyResendInterval;
	
	private Set<TransitionNotification> notifications = Sets.newHashSet();
	
	private boolean uiSendConfirmation;
	
	public WorkflowTransition()
	{
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * 
	 * @return String - numele tranzitiei.
	 */
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getRoutingCondition()
	{
		return this.routingCondition;
	}

	public void setRoutingCondition(String routingCondition)
	{
		this.routingCondition = routingCondition;
	}

	public String getRoutingType()
	{
		return this.routingType;
	}

	public void setRoutingType(String routingType)
	{
		this.routingType = routingType;
	}

	public Long getRoutingDestinationId()
	{
		return this.routingDestinationId;
	}

	public void setRoutingDestinationId(Long routingDestinationId)
	{
		this.routingDestinationId = routingDestinationId;
	}

	public String getRoutingDestinationParameter()
	{
		return this.routingDestinationParameter;
	}

	public void setRoutingDestinationParameter(	String routingDestinationParameter)
	{
		this.routingDestinationParameter = routingDestinationParameter;
	}

	@ManyToOne
	@Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public WorkflowState getStartState()
	{
		return startState;
	}

	public void setStartState(WorkflowState startState)
	{
		this.startState = startState;
	}

	@ManyToOne
	@Cascade(CascadeType.SAVE_UPDATE)
	public WorkflowState getFinalState()
	{
		return finalState;
	}

	public void setFinalState(WorkflowState finalState)
	{
		this.finalState = finalState;
	}
	
	@Column(name = "available_4_auto_actions_only", nullable = false)
	public boolean isAvailableForAutomaticActionsOnly() {
		return availableForAutomaticActionsOnly;
	}
	
	public void setAvailableForAutomaticActionsOnly(boolean availableForAutomaticActionsOnly) {
		this.availableForAutomaticActionsOnly = availableForAutomaticActionsOnly;
	}
	
	public Boolean isDeadlineAction()
	{
		return deadlineAction;
	}

	public void setDeadlineAction(Boolean deadlineAction)
	{
		this.deadlineAction = deadlineAction;
	}

	public Integer getDeadlinePeriod()
	{
		return deadlinePeriod;
	}

	public void setDeadlinePeriod(Integer deadlinePeriod)
	{
		this.deadlinePeriod = deadlinePeriod;
	}

	public Integer getDeadlineActionType()
	{
		return deadlineActionType;
	}

	public void setDeadlineActionType(Integer deadlineActionType)
	{
		this.deadlineActionType = deadlineActionType;
	}

	public void setExtraViewers(Set<OrganizationEntity> extraViewers)
	{
		this.extraViewers = extraViewers;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="WorkflowViewer_OE")
	public Set<OrganizationEntity> getExtraViewers()
	{
		return extraViewers;
	}
	
	/**
	 * @param deadlineNotifyResendInterval the deadlineNotifyResendInterval to set
	 */
	public void setDeadlineNotifyResendInterval(Integer deadlineNotifyResendInterval)
	{
		this.deadlineNotifyResendInterval = deadlineNotifyResendInterval;
	}

	/**
	 * @return the deadlineNotifyResendInterval
	 */
	public Integer getDeadlineNotifyResendInterval()
	{
		return deadlineNotifyResendInterval;
	}
	
	@OneToMany(cascade = javax.persistence.CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "transition")
	public Set<TransitionNotification> getNotifications() {
		return notifications;
	}
	
	public void setNotifications(Set<TransitionNotification> notifications) {
		this.notifications = notifications;
	}
	
	@Column(name = "ui_send_confirmation")
	public boolean isUiSendConfirmation() {
		return uiSendConfirmation;
	}
	
	public void setUiSendConfirmation(boolean uiSendConfirmation) {
		this.uiSendConfirmation = uiSendConfirmation;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", id)
			.append("name", name)
			.toString();
	}
}