package ro.cloudSoft.cloudDoc.domain.replacementProfiles;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import ro.cloudSoft.cloudDoc.domain.organization.User;
import ro.cloudSoft.common.utils.StringUtils2;

import com.google.common.collect.Sets;

/**
 * 
 */
@Entity
@Table(name = "replacement_profiles")
public class ReplacementProfile {

	private Long id;
	
	private String requesterUsername;
	private User replacement;
	private Set<User> selectedRequesterUserProfiles = Sets.newHashSet();
	
	private Set<ReplacementProfile> selectedReplacementProfilesInWhichRequesterIsReplacement = Sets.newHashSet();
	
	private String comments;
	
	private Date startDate;
	private Date endDate;
	
	private boolean outOfOffice;
	private String outOfOfficeEmailSubjectTemplate;
	private String outOfOfficeEmailBodyTemplate;
	
	private ReplacementProfileStatus status;
	
	@Transient
	public boolean isNew() {
		return (getId() == null);
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	public Long getId() {
		return id;
	}
	
	@Column(name = "requester_username", nullable = false, length = 200)
	public String getRequesterUsername() {
		return requesterUsername;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "replacement_user_id", referencedColumnName = "org_entity_id", nullable = false)
	public User getReplacement() {
		return replacement;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "sel_user_prof_4_repl_profiles",
		joinColumns = @JoinColumn(name = "repl_profile_id", referencedColumnName = "id", nullable = false),
		inverseJoinColumns = @JoinColumn(name = "user_profile_id", referencedColumnName = "org_entity_id", nullable = false)
	)
	public Set<User> getSelectedRequesterUserProfiles() {
		return selectedRequesterUserProfiles;
	}
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "sel_repl_prof_req_is_repl",
		joinColumns = @JoinColumn(name = "repl_profile_id_4_requester", referencedColumnName = "id", nullable = false),
		inverseJoinColumns = @JoinColumn(name = "repl_profile_id_4_replacement", referencedColumnName = "id", nullable = false)
	)
	public Set<ReplacementProfile> getSelectedReplacementProfilesInWhichRequesterIsReplacement() {
		return selectedReplacementProfilesInWhichRequesterIsReplacement;
	}
	
	@Column(name = "comments", length = 2000)
	public String getComments() {
		return comments;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "start_date", nullable = false)
	public Date getStartDate() {
		return startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "end_date", nullable = false)
	public Date getEndDate() {
		return endDate;
	}
	
	@Column(name = "out_of_office", nullable = false)
	public boolean isOutOfOffice() {
		return outOfOffice;
	}
	
	@Column(name = "out_of_office_email_subj_tpl", length = 200)
	public String getOutOfOfficeEmailSubjectTemplate() {
		return outOfOfficeEmailSubjectTemplate;
	}

	@Column(name = "out_of_office_email_body_tpl", length = 2000)
	public String getOutOfOfficeEmailBodyTemplate() {
		return outOfOfficeEmailBodyTemplate;
	}
	
	@OneToOne(mappedBy = "profile")
	public ReplacementProfileStatus getStatus() {
		return status;
	}
	
	/**
	 * Returneaza profilul utilizatorului cu ID-ul dat dintre cele selectate ale titularului.
	 * 
	 * @throws IllegalArgumentException daca nu se gaseste un profil de utilizator cu ID-ul dat
	 */
	@Transient
	public User getSelectedUserProfileWithId(Long userId) {
		
		for (User userProfile : getSelectedRequesterUserProfiles()) {
			if (userProfile.getId().equals(userId)) {
				return userProfile;
			}
		}
		
		String exceptionMessage = null;
		if (getId() != null) {
			exceptionMessage = "Nu s-a gasit profilul de utilizator cu ID-ul [" + userId + "] " +
				"printre cele selectate ale profilului de inlocuire cu ID-ul [" + getId() + "].";
		} else {
			exceptionMessage = "Nu s-a gasit profilul de utilizator cu ID-ul [" + userId + "] " +
				"printre cele selectate ale profilului de inlocuire";
		}
		throw new IllegalArgumentException(exceptionMessage);
	}

	/**
	 * Returneaza adresele de e-mail distincte pentru profilele de utilizator selectate ale titularului.
	 * Comparatia intre adresele de e-mail NU este case-sensitive.
	 */
	@Transient
	public Set<String> getRequesterEmailAddresses() {
		Set<String> requesterEmailAddresses = Sets.newHashSet();
		for (User requesterUserProfile : getSelectedRequesterUserProfiles()) {
			requesterEmailAddresses.add(requesterUserProfile.getEmail());
		}
		return StringUtils2.withoutDuplicatesWithDifferentCase(requesterEmailAddresses);
	}

	/**
	 * Verifica daca profilul de inlocuire dat este printre cele selectate in care titularul este inlocuitor.
	 */
	@Transient
	public boolean selectedReplacementProfileInWhichRequesterIsReplacementExists(ReplacementProfile replacementProfileToFind) {
		for (ReplacementProfile replacementProfile : getSelectedReplacementProfilesInWhichRequesterIsReplacement()) {
			if (replacementProfile.getId().equals(replacementProfileToFind.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setRequesterUsername(String requesterUsername) {
		this.requesterUsername = requesterUsername;
	}
	public void setReplacement(User replacement) {
		this.replacement = replacement;
	}
	public void setSelectedRequesterUserProfiles(Set<User> requesterUserProfiles) {
		this.selectedRequesterUserProfiles = requesterUserProfiles;
	}
	public void setSelectedReplacementProfilesInWhichRequesterIsReplacement(Set<ReplacementProfile> selectedReplacementProfilesInWhichRequesterIsReplacement) {
		this.selectedReplacementProfilesInWhichRequesterIsReplacement = selectedReplacementProfilesInWhichRequesterIsReplacement;
	}
	public void setStatus(ReplacementProfileStatus status) {
		this.status = status;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public void setOutOfOffice(boolean outOfOffice) {
		this.outOfOffice = outOfOffice;
	}
	public void setOutOfOfficeEmailSubjectTemplate(String outOfOfficeEmailSubjectTemplate) {
		this.outOfOfficeEmailSubjectTemplate = outOfOfficeEmailSubjectTemplate;
	}
	public void setOutOfOfficeEmailBodyTemplate(String outOfOfficeEmailBodyTemplate) {
		this.outOfOfficeEmailBodyTemplate = outOfOfficeEmailBodyTemplate;
	}
}