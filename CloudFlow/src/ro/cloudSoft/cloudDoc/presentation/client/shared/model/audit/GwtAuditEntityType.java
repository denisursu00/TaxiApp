package ro.cloudSoft.cloudDoc.presentation.client.shared.model.audit;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.EnumWithLocalizedLabel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.providers.GwtLocaleProvider;

public enum GwtAuditEntityType implements EnumWithLocalizedLabel {
	
	USER {

		@Override
		public String getLocalizedLabel() {
			return GwtLocaleProvider.getConstants().USER();
		}
	},
	ORGANIZATION_UNIT {

		@Override
		public String getLocalizedLabel() {
			return GwtLocaleProvider.getConstants().ORG_UNIT();
		}
	},
	GROUP {

		@Override
		public String getLocalizedLabel() {
			return GwtLocaleProvider.getConstants().GROUP();
		}
	},
	DOCUMENT_TYPE {

		@Override
		public String getLocalizedLabel() {
			return GwtLocaleProvider.getConstants().DOC_TYPE();
		}
	},
	MIME_TYPE {

		@Override
		public String getLocalizedLabel() {
			return GwtLocaleProvider.getConstants().MIME_TYPE();
		}
	},	
	WORKFLOW {

		@Override
		public String getLocalizedLabel() {
			return GwtLocaleProvider.getConstants().WORKFLOW();
		}
	},
	REPLACEMENT_PROFILE {

		@Override
		public String getLocalizedLabel() {
			return GwtLocaleProvider.getConstants().REPLACEMENT_PROFILE();
		}
	},
	ARCHIVING_PROFILE {

		@Override
		public String getLocalizedLabel() {
			return GwtLocaleProvider.getConstants().ARCHIVING_PROFILE();
		}
	},
	DOCUMENT {

		@Override
		public String getLocalizedLabel() {
			return GwtLocaleProvider.getConstants().DOCUMENT();
		}
	}
}