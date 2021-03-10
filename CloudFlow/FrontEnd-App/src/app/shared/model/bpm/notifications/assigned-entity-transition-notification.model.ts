import { TransitionNotificationModelType } from "./transition-notification-model-type";
import { TransitionNotificationModel } from "./transition-notification.model";

export class AssignedEntityTransitionNotificationModel extends TransitionNotificationModel {

	public constructor() {
		super(TransitionNotificationModelType.ASSIGNED_ENTITY);
	}
}