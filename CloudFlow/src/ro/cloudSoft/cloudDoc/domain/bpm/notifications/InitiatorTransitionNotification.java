package ro.cloudSoft.cloudDoc.domain.bpm.notifications;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "initiator_trans_notifs")
@PrimaryKeyJoinColumn(name = "notification_id", referencedColumnName = "id")
public class InitiatorTransitionNotification extends TransitionNotification {
}