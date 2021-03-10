package ro.cloudSoft.cloudDoc.domain.bpm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Entity implementation class for Entity: WorkflowState
 * 
 * 
 */
@Entity
public class WorkflowState implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public final static int STATETYPE_START = 1;
	public final static int STATETYPE_INTERMEDIATE = 2;
	public final static int STATETYPE_STOP = 3;

	public final static int ATTACH_PERM_ADD = 1;
	public final static int ATTACH_PERM_DELETE = 2;
	public final static int ATTACH_PERM_ALL = 3;

	private Long id;
	private String name;
	private String code;

	private Integer stateType;
	
	private boolean automaticRunning;
	private String classPath;

	/**
	 * Drepturi pentru manipularea atasamentelor: de adaugare si stergere.
	 * Administratorul aplicatiei va putea defini daca actorul din starea
	 * destinanie a tranzitiei curente are dreptul de adaugare de noi atasamente
	 * sau daca are dreptul de a le sterge pe cele deja existente.
	 * 
	 * Va fi o constanta dintr-un enum cu valorile (ADD, DELETE, ALL)
	 */
	private Integer attachmentsPermission;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getAttachmentsPermission() {
		return attachmentsPermission;
	}

	public void setAttachmentsPermission(Integer attachmentsPermission) {
		this.attachmentsPermission = attachmentsPermission;
	}

	public Integer getStateType() {
		return stateType;
	}

	public void setStateType(Integer stateType) {
		this.stateType = stateType;
	}
	
	@Column(name = "automaticrunning")
	public boolean isAutomaticRunning() {
		return automaticRunning;
	}

	public void setAutomaticRunning(boolean automaticRunning) {
		this.automaticRunning = automaticRunning;
	}

	@Column(name = "classpath")
	public String getClassPath() {
		return classPath;
	}

	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof WorkflowState)) {
			return false;
		}
		
		WorkflowState other = (WorkflowState) obj;
		
		return new EqualsBuilder()
			.append(id, other.id)
			.append(name, other.name)
			.append(code, other.code)
			.append(stateType, other.stateType)
			.append(attachmentsPermission, other.attachmentsPermission)
			.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(id)
			.append(name)
			.append(code)
			.append(stateType)
			.append(attachmentsPermission)
			.toHashCode();
	}
}