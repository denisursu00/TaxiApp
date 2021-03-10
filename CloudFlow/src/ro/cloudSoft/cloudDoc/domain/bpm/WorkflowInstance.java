package ro.cloudSoft.cloudDoc.domain.bpm;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class WorkflowInstance implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7993080135534752115L;
	
	public static final String STATUS_RUNNING = "RUNNING";
	public static final String STATUS_FINNISHED = "FINNISHED";
	public static final String STATUS_IMPORTED = "IMPORTED";
	public static final String STATUS_NONE = "NONE";
	
	private String processInstanceId;
	
	private String documentId;
	
	private String workspaceName;

	private Workflow workflow;
	
	private Long initiatorId;
	
	private String status;
	
	private Long id;
	
	private Date finishedDate;
	
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
	 * @param processInstanceId the processInstanceId to set
	 */
	public void setProcessInstanceId(String processInstanceId)
	{
		this.processInstanceId = processInstanceId;
	}

	/**
	 * @return the processInstanceId
	 */
	public String getProcessInstanceId()
	{
		return processInstanceId;
	}

	
	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(String documentId)
	{
		this.documentId = documentId;
	}

	/**
	 * @return the documentId
	 */
	public String getDocumentId()
	{
		return documentId;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	/**
	 * @param workflow the workflow to set
	 */
	public void setWorkflow(Workflow workflow)
	{
		this.workflow = workflow;
	}

	/**
	 * @return the workflow
	 */
	@OneToOne(cascade = { CascadeType.MERGE }, fetch=FetchType.EAGER)
	public Workflow getWorkflow()
	{
		return workflow;
	}
	
	public String getWorkspaceName()
	{
		return workspaceName;
	}

	public void setWorkspaceName(String workspaceName)
	{
		this.workspaceName = workspaceName;
	}
	/**
	 * @param initiatorId the initiatorId to set
	 */
	public void setInitiatorId(Long initiatorId)
	{
		this.initiatorId = initiatorId;
	}

	/**
	 * @return the initiatorId
	 */
	public Long getInitiatorId()
	{
		return initiatorId;
	}
	
	@Column(name="finished_date")
	public Date getFinishedDate() {
		return finishedDate;
	}
	
	public void setFinishedDate(Date finishedDate) {
		this.finishedDate = finishedDate;
	}
}
