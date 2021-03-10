import { ClientPermissionEnum, AdminPermissionEnum } from "@app/shared";
import { PrezentaPermissionEnum } from "../enums";

export const AUTH_ACCESS = {
	
	CLIENT: {
		DASHBOARD: {
			permissions: [ ClientPermissionEnum.ACCESS_DASHBOARD ]
		},
		MY_DOCUMENT_ACTIVITIES: {
			permissions: [ ClientPermissionEnum.MANAGE_DOCUMENTS ]
		},
		WORKSPACE_PANEL: {
			permissions: [ ClientPermissionEnum.MANAGE_DOCUMENTS ]
		},
		SEARCHES_AND_REPORTS: {
			permissions: [ ClientPermissionEnum.MANAGE_DOCUMENTS ]
		},
		PROJECTS: {
			MY_PROJECT_ACTIVITIES: {
				permissions: [ ClientPermissionEnum.MANAGE_PROJECTS ]
			},
			INITIATION_UPDATING_PROJECTS: {
				permissions: [ ClientPermissionEnum.MANAGE_PROJECTS ]
			}
		},
		CALENDAR: {
			permissions: [ ClientPermissionEnum.VIEW_CALEDAR ]
		},
		NOMENCLATORS: {
			permissions: [ ClientPermissionEnum.VIEW_NOMENCLATORS ]
		},
		REPORTS: {
			permissions: [ ClientPermissionEnum.ACCESS_REPORTS ]
		},
		REGISTERS: {
			REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI: {
				permissions: [ ClientPermissionEnum.VIEW_REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI ]
			},
			REGISTRU_INTRARI_IESIRI: {
				permissions: [ ClientPermissionEnum.VIEW_REGISTRU_INTRARI_IESIRI ]
			},
			COMISII_SAU_GL: {
				permissions: [ ClientPermissionEnum.VIEW_COMISII_GL ]
			},
			DEPLASARI_DECONTURI: {
				permissions: [ ClientPermissionEnum.VIEW_DEPLASARI_DECONTURI ]
			},
			ALTE_DECONTURI: {
				permissions: [ ClientPermissionEnum.VIEW_ALTE_DECONTURI ]
			}
		}
	},

	PREZENTA: {
		COMPLETARE: {
			permissions: [ PrezentaPermissionEnum.COMPLETARE_PREZENTA ]
		},
		IMPORTARE: {
			permissions: [ PrezentaPermissionEnum.IMPORTARE_PREZENTA ]
		}
	},

	ADMIN: {
		ORGANIZATIONAL_STRUCTURE: {
			permissions: [ AdminPermissionEnum.MANAGE_ORGANIZATIONAL_STRUCTURE ]
		},
		GROUPS: {
			permissions: [ AdminPermissionEnum.MANAGE_GROUPS ]
		},
		ROLES_AND_PERMISSIONS: {
			permissions: [ AdminPermissionEnum.MANAGE_ROLES_AND_PERMISSIONS ]
		},
		DOCUMENT_TYPES: {
			permissions: [ AdminPermissionEnum.MANAGE_DOCUMENT_TYPES ]
		},
		MIME_TYPES: {
			permissions: [ AdminPermissionEnum.MANAGE_MIME_TYPES ]
		},
		WORKFLOWS: {
			permissions: [ AdminPermissionEnum.MANAGE_WORKFLOWS ]
		},
		CALENDARS: {
			permissions: [ AdminPermissionEnum.MANAGE_CALENDARS ]
		},
		REPLACEMENT_PROFILES: {
			permissions: [ AdminPermissionEnum.MANAGE_REPLACEMENT_PROFILES ]
		},
		NOMENCLATORS: {
			permissions: [ AdminPermissionEnum.MANAGE_NOMENCLATORS ]
		},
		PARAMETERS: {
			permissions: [ AdminPermissionEnum.MANAGE_PARAMETERS ]
		}
	}
};
