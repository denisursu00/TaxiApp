package ro.cloudSoft.cloudDoc.domain.bpm.notifications;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "sup_of_initiator_trans_notifs")
@PrimaryKeyJoinColumn(name = "notification_id", referencedColumnName = "id")
public class HierarchicalSuperiorOfInitiatorTransitionNotification extends TransitionNotification {
}