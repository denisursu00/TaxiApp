package ro.cloudSoft.cloudDoc.presentation.client.shared.model.bpm.notifications;

public enum TransitionNotificationModelType {

	ASSIGNED_ENTITY() {
		
		@Override
		public String getLabel() {
			//return GwtLocaleProvider.getConstants().ASSIGNED_ENTITY();
			return "ASSIGNED_ENTITY";
		}
	},
	INITIATOR() {
		
		@Override
		public String getLabel() {
			//return GwtLocaleProvider.getConstants().INITIATOR();
			return "INITIATOR";
		}
	},
	HIERARCHICAL_SUPERIOR_OF_INITIATOR() {
		
		@Override
		public String getLabel() {
			//return GwtLocaleProvider.getConstants().HIERARCHICAL_SUPERIOR_OF_INITIATOR();
			return "HIERARCHICAL_SUPERIOR_OF_INITIATOR";
		}
	},
	MANUALLY_CHOSEN_ENTITIES() {
		
		@Override
		public String getLabel() {
			//return GwtLocaleProvider.getConstants().MANUALLY_CHOSEN_ENTITIES();
			return "MANUALLY_CHOSEN_ENTITIES";
		}
	},
	METADATA() {
		
		@Override
		public String getLabel() {
			//return GwtLocaleProvider.getConstants().METADATA();
			return "METADATA";
		}
	},
	HIERARCHICAL_SUPERIOR_OF_USER_METADATA() {
		
		@Override
		public String getLabel() {
			return "HIERARCHICAL_SUPERIOR_OF_USER_METADATA";
		}
	};
	
	public abstract String getLabel();
}