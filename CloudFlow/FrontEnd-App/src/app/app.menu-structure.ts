import { Type } from "@angular/core";
import { RoleEnum, RouteConstants, AdminPermissionEnum, ClientPermissionEnum, PrezentaPermissionEnum } from "@app/shared";
import { AUTH_ACCESS } from "@app/shared/auth";

export interface MenuItemDefinition {
	labelCode: string;
	icon?: string;
	path?: string;
	authPermissions?: ClientPermissionEnum[] | AdminPermissionEnum[] | PrezentaPermissionEnum[];
	items?: MenuItemDefinition[];
}

export const MENU_STRUCTURE: MenuItemDefinition[] = [
	{
		labelCode: "CLIENT",
		items: [
			{
				labelCode: "DASHBOARD",
				icon: "fa fa-tachometer",
				path: RouteConstants.CLIENT_DASHBOARD,
				authPermissions: AUTH_ACCESS.CLIENT.DASHBOARD.permissions
			},
			{
				labelCode: "DOCUMENTS",
				icon: "fa fa-files-o",
				items: [
					{
						labelCode: "MY_DOCUMENT_ACTIVITIES",
						icon: "fa fa-tasks",
						path: "client/my-document-activities",
						authPermissions: AUTH_ACCESS.CLIENT.MY_DOCUMENT_ACTIVITIES.permissions					
					},
					{
						labelCode: "WORKSPACE_PANEL",
						icon: "fa fa-tachometer",
						path: "client/workspace-panel",
						authPermissions: AUTH_ACCESS.CLIENT.WORKSPACE_PANEL.permissions			
					},
					{
						labelCode: "SEARCHES_AND_REPORTS",
						icon: "fa fa-search",
						path: "client/searches-and-reports",
						authPermissions: AUTH_ACCESS.CLIENT.SEARCHES_AND_REPORTS.permissions	
					}
				]
			},
			{
				labelCode: "PROJECTS",
				icon: "fa fa-bars",
				items: [
					{
						labelCode: "MY_PROJECT_ACTIVITIES",
						icon: "fa fa-list-alt",
						path: "client/projects/my-project-activities",
						authPermissions: AUTH_ACCESS.CLIENT.PROJECTS.MY_PROJECT_ACTIVITIES.permissions	
					},
					{
						labelCode: "INITIATION_UPDATING_PROJECTS",
						icon: "fa fa-folder-open",
						path: "client/projects/initiation-updating-projects",
						authPermissions: AUTH_ACCESS.CLIENT.PROJECTS.INITIATION_UPDATING_PROJECTS.permissions		
					}
				]
			}, 
			{
				labelCode: "CALENDAR",
				icon: "fa fa-calendar",
				path: "client/calendar",
				authPermissions: AUTH_ACCESS.CLIENT.CALENDAR.permissions		
			},
			{
				labelCode: "NOMENCLATORS",
				icon: "fa fa-bars",
				path: "client/nomenclators/data",
				authPermissions: AUTH_ACCESS.CLIENT.NOMENCLATORS.permissions		
			},
			{
				labelCode: "REPORTS",
				icon: "fa fa-bars",
				items: [
					{
						labelCode: "RAPOARTE_CD",
						icon: "fa fa-bars",
						items: [
							{
								labelCode: "REPORT_NUMAR_SEDINTE_SI_PARTICIPANTI",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-numar-sedinte-si-participanti",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions			
							},
							{
								labelCode: "REPORT_PREZENTA_SEDINTA_MEMBRII",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-prezenta-sedinte-membrii",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions				
							},
							{
								labelCode: "REPORT_PREZENTA_SEDINTE_INVITATI_ARB",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-prezenta-sedinte-invitati-arb",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions				
							},
							{
								labelCode: "REPORT_PREZENTA_SEDINTE_INVITATI_EXTERNI",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-prezenta-sedinte-invitati-externi",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							}
						]
					},
					{
						labelCode: "REPORT_PARTICIPARI_LA_EVENIMENTE",
						icon: "fa fa-bars",
						items: [
							{
								labelCode: "REPORT_PARTICIPARI_LA_EVENIMENTE",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-participari-la-evenimente",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							}
							
						]
					},
					{
						labelCode: "RAPOARTE_PROIECTE",
						icon: "fa fa-bars",
						items: [
							{
								labelCode: "REPORT_ACTIUNI_PE_PROIECT",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-actiuni-pe-proiect",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "REPORT_DASHBOARD_PROIECTE",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-dashboard-proiecte",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							}
						]
					},
					{
						labelCode: "RAPOARTE_INFORMARE_LUNARE_SI_SAPTAMANALE",
						icon: "fa fa-bars",
						items: [
							{
								labelCode: "REPORT_ACTIUNI_ORGANIZATE_DE_ARB",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-actiuni-organizate-de-arb",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "REPORT_DOCUMENTE_TRIMISE_DE_ARB",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-documente-trimise-de-arb",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "REPORT_LISTA_PROIECTELOR_CARE_AU_VIZAT_ACTIUNILE_LUNII",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-lista-proiectelor-care-au-vizat-actiunile-lunii",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "REPORT_PARTICIPARE_REPREZENTANTI_ARB_LA_ACTIUNI_IN_AFARA_ASOCIATIEI",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-participare-reprezentanti-arb-la-actiuni-in-afara-asociatiei",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							}
						]
					},
					{
						labelCode: "RAPOARTE_DEPLASARI",
						icon: "fa fa-bars",
						items: [
							{
								labelCode: "REPORT_DECONT_CHELTUIELI_ALTE_DECONTURI",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-decont-cheltuieli-alte-deconturi",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "REPORT_CHETUIELI_ARB_SI_REPREZENTANT_ARB",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-cheltuieli-arb-si-reprezentant-arb",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "RAPORT_DEPLASARI_DECONTURI_CHELTUIELI_REPREZENTANT_ARB",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-deplasari-deconturi-cheltuieli-reprezentanti-arb",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "REPORT_DEPLASARI_DECONTURI",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-deplasari-deconturi",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "REPORT_CERERI_CONCEDIU",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-cereri-concediu",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							}
						]
					},
					{
						labelCode: "RAPOARTE_COMSII_GL",
						icon: "fa fa-bars",
						items: [
							{
								labelCode: "REPORT_PREZENTA_COMISII_GL_IN_INTERVAL",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-comisii-gl-in-interval",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions
							},
							{
								labelCode: "REPORT_NUMAR_PARTICIPANTI_SEDINTE_COMISIE_GL",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-numar-participanti-sedinte-comisie-gl",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "RAPORT_SEDINTE_COMISIE_GL",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-numar-sedinte-comisie-gl",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							}
						]
					},
					{
						labelCode: "RAPORT_MEMBRII_AFILIATI",
						icon: "fa fa-bars",
						items: [
							{
								labelCode: "RAPORT_MEMBRII_AFILIATI",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-membrii-afiliati",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							}
						]
					},
					{
						labelCode: "REPORT_TASKURI_CUMULATE",
						icon: "fa fa-bars",
						items: [
							{
								labelCode: "REPORT_TASKURI_CUMULATE",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-taskuri-cumulate",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							}
						]
					},
					{
						labelCode: "REPORT_PREZENTA_AGA",
						icon: "fa fa-bars",
						items: [
							{
								labelCode: "REPORT_PREZENTA_AGA",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-prezenta-aga",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							}
						]
					},
					{
						labelCode: "REPORT_ADERARE_OIORO",
						icon: "fa fa-bars",
						items: [
							{
								labelCode: "REPORT_ADERARE_OIORO",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-aderare-oioro",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							}
						]
					},
					{
						labelCode: "RAPOARTE_BANCI",
						icon: "fa fa-bars",
						items: [
							{
								labelCode: "RAPORT_REPREZENTANTI_BANCA_PER_FUNCTIE_SI_COMISIE",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-reprezentanti-banca-per-functie-si-comisie",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "REPORT_RASPUNSURI_BANCI",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-raspunsuri-banci",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "RAPORT_CENTRALIZATOR_PREZENTA_PERIOADA",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-centralizator-prezenta-perioada",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "RAPORT_NOTA_GENERALA_PE_MEMBRII_ARB",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-nota-generala-pe-membrii-arb",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "REPORT_PREZENTA_COMISII_ARB_REPREZENTATIVITATE",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-prezenta-comisii-gl-reprezentativitate",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "REPORT_NOTA_GENERALA",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-nota-generala",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "REPORT_NIVEL_REPREZENTARE_COMISII",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-nivel-reprezentare-comisii",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							},
							{
								labelCode: "REPORT_NOTE_BANCI",
								icon: "fa fa-circle-o",
								path: "client/reports/raport-note-banci",
								authPermissions: AUTH_ACCESS.CLIENT.REPORTS.permissions		
							}
						]
					}
				]
			},
			{
				labelCode: "REGISTERS",
				icon: "fa fa-bars",
				items: [
					{
						labelCode: "REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI",
						icon: "fa fa-files-o",
						path: "client/registers/registru-documente-justificative-plati",
						authPermissions: AUTH_ACCESS.CLIENT.REGISTERS.REGISTRU_DOCUMENTE_JUSTIFICATIVE_PLATI.permissions		
					},
					{
						labelCode: "REGISTRU_INTRARI_IESIRI",
						icon: "fa fa-arrows-h",
						path: "client/registers/registru-intrari-iesiri",
						authPermissions: AUTH_ACCESS.CLIENT.REGISTERS.REGISTRU_INTRARI_IESIRI.permissions	
					},
					{
						labelCode: "COMISII_SAU_GL",
						icon: "fa fa-group",
						path: "client/registers/comisii-sau-gl",
						authPermissions: AUTH_ACCESS.CLIENT.REGISTERS.COMISII_SAU_GL.permissions	
					},
					{
						labelCode: "DEPLASARI_DECONTURI",
						icon: "fa fa-money",
						path: "client/registers/deplasari-deconturi",
						authPermissions: AUTH_ACCESS.CLIENT.REGISTERS.DEPLASARI_DECONTURI.permissions	
					},
					{
						labelCode: "ALTE_DECONTURI",
						icon: "fa fa-money",
						path: "client/registers/alte-deconturi",
						authPermissions: AUTH_ACCESS.CLIENT.REGISTERS.ALTE_DECONTURI.permissions	
					}
				]
			}
		]
	},
	{
		labelCode: "PREZENTA_ONLINE",
		icon: "fa fa-pencil-square-o",
		path: "prezenta-online",
		authPermissions: AUTH_ACCESS.PREZENTA.COMPLETARE.permissions
	},			
	{
		labelCode: "ADMIN",
		items: [
			{
				labelCode: "ORGANIZATIONAL_STRUCTURE",
				icon: "fa fa-user",
				path: "admin/organizational-structure",
				authPermissions: AUTH_ACCESS.ADMIN.ORGANIZATIONAL_STRUCTURE.permissions
			},
			{
				labelCode: "USER_GROUPS",
				icon: "fa fa-group",
				path: "admin/groups",
				authPermissions: AUTH_ACCESS.ADMIN.GROUPS.permissions
			},
			{
				labelCode: "ROLES_AND_PERMISSIONS",
				icon: "fa fa-bars",
				path: "admin/roles-and-permissions",
				authPermissions: AUTH_ACCESS.ADMIN.ROLES_AND_PERMISSIONS.permissions
			},
			{
				labelCode: "DOCUMENT_TYPES",
				icon: "fa fa-file",
				path: "admin/document-types",
				authPermissions: AUTH_ACCESS.ADMIN.DOCUMENT_TYPES.permissions
			},
			{
				labelCode: "MIME_TYPES",
				icon: "fa fa-paperclip",
				path: "admin/mime-types",
				authPermissions: AUTH_ACCESS.ADMIN.MIME_TYPES.permissions
			},
			{
				labelCode: "WORKFLOWS",
				icon: "fa fa-paper-plane-o",
				path: "admin/workflows",
				authPermissions:AUTH_ACCESS.ADMIN.WORKFLOWS.permissions
			},
			{
				labelCode: "CALENDARS",
				icon: "fa fa-calendar",
				path: "admin/calendars",
				authPermissions: AUTH_ACCESS.ADMIN.CALENDARS.permissions
			},
			{
				labelCode: "REPLACEMENT_PROFILES",
				icon: "fa fa-circle-o",
				path: "admin/replacement-profiles",
				authPermissions: AUTH_ACCESS.ADMIN.REPLACEMENT_PROFILES.permissions
			},
			{
				labelCode: "NOMENCLATORS",
				icon: "fa fa-bars",
				path: "admin/nomenclators",
				authPermissions: AUTH_ACCESS.ADMIN.NOMENCLATORS.permissions
			},
			{
				labelCode: "PARAMETERS",
				icon: "fa fa-list-ul",
				path: "admin/parameters",
				authPermissions: AUTH_ACCESS.ADMIN.PARAMETERS.permissions
			}/*,
			{
				labelCode: "AUDIT",
				icon: "fa fa-info-circle",
				path: "admin/audit",
				authPermissions: AUTH_ACCESS.ADMIN.AUDIT.permissions
			},
			{
				labelCode: "HISTORY_AND_ERRORS",
				icon: "fa fa-history",
				path: "admin/history-and-errors",
				authPermissions: AUTH_ACCESS.ADMIN.HISTORY_AND_ERRORS.permissions
			}*/
		]
	}
];