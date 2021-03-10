import { TransitionNotificationModelType } from "./transition-notification-model-type";
import { TransitionNotificationModel } from "./transition-notification.model";

export class HierarchicalSuperiorOfInitiatorTransitionNotificationModel extends TransitionNotificationModel {

	public constructor() {
		super(TransitionNotificationModelType.HIERARCHICAL_SUPERIOR_OF_INITIATOR);
	}
}