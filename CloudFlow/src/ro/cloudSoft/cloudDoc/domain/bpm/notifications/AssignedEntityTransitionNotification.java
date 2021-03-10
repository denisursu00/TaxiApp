package ro.cloudSoft.cloudDoc.domain.bpm.notifications;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "assigned_trans_notifs")
@PrimaryKeyJoinColumn(name = "notification_id", referencedColumnName = "id")
public class AssignedEntityTransitionNotification extends TransitionNotification {
}