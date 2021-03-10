import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, FormControl, Validators } from "@angular/forms";
import { DateConstants, DateUtils, AppError, NomenclatorConstants, NomenclatorValidators, ObjectUtils, ValueOfNomenclatorValueField, FormUtils, NomenclatorSortedAttribute, BaseWindow, AdminPermissionEnum, ConfirmationWindowFacade, RegistruIntrariModel } from "@app/shared";
import { RegistruIntrariIesiriService, NomenclatorService, ProjectService, MessageDisplayer } from "@app/shared";
import { TranslateUtils, ProjectModel, RegistruIesiriDestinatariModel, RegistruIesiriModel, NomenclatorValueModel, RegistruIesiriAtasamentModel, AttachmentService, AttachmentModel, ArrayUtils, DownloadUtils, NomenclatorUtils } from "@app/shared";
import { RegistruIesiriDestinatarManagerInputData } from "../registru-iesiri-destinatar-manager/registru-iesiri-destinatar-manager.component";
import { FileUpload, SelectItem, MultiSelect } from "primeng/primeng";
import { HttpResponse } from "@angular/common/http";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";
import { AuthManager } from "@app/shared/auth";
import { ProjectSubactivityModel } from "@app/shared/model/project/project-subactivity.model";

@Component({
	selector: "app-registru-iesiri-window",
	templateUrl: "./registru-iesiri-window.component.html"
})
export class RegistruIesiriWindowComponent extends BaseWindow implements OnInit {

	private static readonly COD_TIP_DOCUMENT_CONSULTARE_BANCI = "CB";
	private static readonly COD_TIP_DOCUMENT_SCRISOARE_EXTERNA_TRIMISA_LA_CARE_SE_ASTEAPTA_RASPUNS = "SR";
	private static readonly TIP_REGISTRU_IESIRI = "iesiri";

	@Input()
	public registruId: number;

	@Input()
	public nomenclatorIdByCode: Map<string,number>;

	@Input()
	public mode: "add" | "edit" | "view";

	@Output()
	private windowClosed: EventEmitter<void>;
	
	@ViewChild(FileUpload)
	private fileUpload: FileUpload;

	private formBuilder: FormBuilder;
	private registruIntrariIesiriService: RegistruIntrariIesiriService;
	private nomenclatorService: NomenclatorService;
	private projectService: ProjectService;
	private translateUtils: TranslateUtils;
	private messageDisplayer: MessageDisplayer;
	private authManager: AuthManager;

	public registruIesiri: RegistruIesiriModel;
	
	public formGroup: FormGroup;
	public windowVisible: boolean = false;
	public title: string;

	public dateFormat: string;
	public yearRange: string;

	public proiecteItems: SelectItem[];
	public subproiecteItems: SelectItem[];
	public confirmationWindow: ConfirmationWindowFacade;

	public dataInregistrareMaxDate: Date;
	public termenRaspunsMinDate: Date;

	public saveButtonDisabled: boolean = false;

	public destinatarWindowMode: string;

	public registruIesiriDestinatarManagerInputData: RegistruIesiriDestinatarManagerInputData;
	public uploadedAttachments: RegistruIesiriAtasamentModel[];

	private attachmentService: AttachmentService;

	public valoareMinima: number = 0;
	public validationMessageMinValueParameter = {value: this.valoareMinima};
	public customSortedAttributes: NomenclatorSortedAttribute[] = [];

	public widthDropdownProiect: string;

	public constructor(attachmentService: AttachmentService, formBuilder: FormBuilder, registruIntrariIesiriService: RegistruIntrariIesiriService,
			nomenclatorService: NomenclatorService, projectService: ProjectService, translateUtils: TranslateUtils, messageDisplayer: MessageDisplayer, authManager: AuthManager) {
		super();
		this.attachmentService = attachmentService;
		this.formBuilder = formBuilder;
		this.registruIntrariIesiriService = registruIntrariIesiriService;
		this.nomenclatorService = nomenclatorService;
		this.projectService = projectService;
		this.translateUtils = translateUtils;
		this.messageDisplayer = messageDisplayer;
		this.authManager = authManager;
		this.proiecteItems = [];
		this.confirmationWindow = new ConfirmationWindowFacade();
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		this.windowClosed = new EventEmitter<void>();
		this.registruIesiriDestinatarManagerInputData = new RegistruIesiriDestinatarManagerInputData();
		this.init();
		this.lock();		
	}

	private init(): void {
		this.prepareForm();
		this.prepareProiecteItems();
		this.changePerspectiveForTermenRaspunsFormControl();
		this.prepareCustomSortedAttributes();
		this.necesitaRaspunsFormControl.disable();
		this.uploadedAttachments = [];
	}

	public ngOnInit(): void {
		this.destinatarWindowMode = this.mode;
		this.prepareNomenclatorValuesForNomenclatorValueSelectors();
		this.prepareByMode();
	}

	

	private prepareForm(): void {
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("dataInregistrare", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("tipDocument", new FormControl(null, NomenclatorValidators.nomenclatorValueRequired()));
		this.formGroup.addControl("trimisPeMail", new FormControl(null, []));
		this.formGroup.addControl("continut", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("numarPagini", new FormControl(null));
		this.formGroup.addControl("numarAnexe", new FormControl(null));
		this.formGroup.addControl("intocmitDe", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("proiecte", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("subproiect", new FormControl(null, []));
		this.formGroup.addControl("necesitaRaspuns", new FormControl(null, []));
		this.formGroup.addControl("termenRaspuns", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("destinatari", new FormControl(null, [Validators.required]));
	}

	private prepareNomenclatorValuesForNomenclatorValueSelectors(): void {
		if (this.nomenclatorIdByCode){
			let fieldValue: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(this.nomenclatorIdByCode.get(NomenclatorConstants.NOMENCLATOR_CODE_REGISTRU_IESIRI_TIP_DOCUMENT));
			this.tipDocumentFormControl.setValue(fieldValue);
			this.openWindow();
		}
		this.unlock();
	}

	private prepareCustomSortedAttributes(): void {
		let sortedAttribute: NomenclatorSortedAttribute = new NomenclatorSortedAttribute();
		sortedAttribute.attributeKey = NomenclatorConstants.REGISTRU_IESIRI_TIP_DOCUMENT_ATTR_KEY_DENUMIRE;
		sortedAttribute.type = "ASC";
		this.customSortedAttributes.push(sortedAttribute);
	}

	private prepareByMode(): void {
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isEdit()) {
			this.prepareForEdit();
		} else if (this.isView() ) {
			this.prepareForView();
		} else {
			throw new Error("Property [mode] cannot have the value [" + this.mode + "]");
		}
	}

	private prepareForAdd(): void {
		this.updatePerspectiveForAdd();
	}

	private updatePerspectiveForAdd(): void {
		let currentDate: Date = new Date();
		this.dataInregistrareFormControl.setValue(currentDate);
		this.numarPaginiFormControl.setValue(0);
		this.numarAnexeFormControl.setValue(0);
		this.numarAnexeFormControl.updateValueAndValidity();
		this.numarPaginiFormControl.updateValueAndValidity();
		this.dataInregistrareMaxDate = this.dataInregistrareFormControl.value;
		this.termenRaspunsMinDate = this.dataInregistrareFormControl.value;
		this.setTitle();
	}

	public onTipDocumentChanged(event: any): void {
		
		this.lock();
		this.changePerspectiveByTipDocument();	
		
		this.registruIesiriDestinatarManagerInputData.tipDocumentId = this.tipDocumentFormControl.value.value;
		this.registruIesiriDestinatarManagerInputData.registruIesiriId = this.registruId;

		if (this.isAdd() && this.hasMappedRecipient()){
			this.destinatariFormControl.reset();
			this.messageDisplayer.displayWarn("IN_REGISTER_REMOVED_RECIPIENT");
		}
	}

	public hasMappedRecipient(): boolean {
		let recipients: RegistruIesiriDestinatariModel[] = this.destinatariFormControl.value;
		if (ArrayUtils.isEmpty(recipients)){
			return false;
		}
		return recipients.some(recipient => ObjectUtils.isNotNullOrUndefined(recipient.registruIntrariId));
	}

	private changePerspectiveByTipDocument(): void {
		if (!(<ValueOfNomenclatorValueField>this.tipDocumentFormControl.value).value) {
			this.unlock();
			return;
		}
		this.nomenclatorService.getNomenclatorValue((<ValueOfNomenclatorValueField>this.tipDocumentFormControl.value).value, {
			onSuccess: (nomenclatorValue: NomenclatorValueModel): void => {
				this.registruIesiriDestinatarManagerInputData.codDocumentEchivalent = nomenclatorValue[NomenclatorConstants.REGISTRU_IESIRI_TIP_DOCUMENT_ATTR_KEY_CODE_ECHIVALENT_CERERE_INTRARE];
				let isTipDocumentAsteaptaRaspuns: Boolean = NomenclatorUtils.getFieldValueAsBoolean(nomenclatorValue, NomenclatorConstants.REGISTRU_IESIRI_TIP_DOCUMENT_ATTR_KEY_ASTEAPTA_RASPUNS);
				if (isTipDocumentAsteaptaRaspuns) {					
					this.necesitaRaspunsFormControl.disable();
					this.necesitaRaspunsFormControl.setValue(true);
				} else {
					this.necesitaRaspunsFormControl.disable();
					this.necesitaRaspunsFormControl.setValue(false);					
				}
				if (!this.isView()) {
					this.changePerspectiveForTermenRaspunsFormControl();
				}
				this.unlock();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
				this.unlock();
			}
		});
	}

	private setTitle(): void {
		let action: string = null;
		if (this.isAdd()) {
			action = this.translateUtils.translateLabel("ADD");
			let currentYear: number = new Date().getFullYear();
			this.registruIntrariIesiriService.getLastNrInregistrareByTipRegistruAndYear(RegistruIesiriWindowComponent.TIP_REGISTRU_IESIRI, currentYear, {
				onSuccess: (nrInregistrare: number) => {
					this.title += " - "+this.translateUtils.translateLabel("REGISTRU_IESIRI_NR_INREGISTRARE")+": "+(nrInregistrare+1)+" ~ "+currentYear;
				},
				onFailure: (appError: AppError) => {
					this.messageDisplayer.displayAppError(appError);
					this.unlock();
				}
			});
		} else if (this.isEdit()) {
			action = this.translateUtils.translateLabel("EDIT");
	
		} else if (this.isView()) {
			action = this.translateUtils.translateLabel("VIEW");
		}
		this.title = action;
	}

	private prepareForEdit(): void {
		this.registruIesiriDestinatarManagerInputData.registruIesiriWindowPerspective = "edit";
		this.updatePerspectiveForEdit();
		this.loadRegistruIesiriAndPrepareForm();
		this.unlock();
		this.openWindow();
	}

	private loadRegistruIesiriAndPrepareForm(): void {
		this.registruIntrariIesiriService.getRegistruIesiri(this.registruId, {
			onSuccess: (registruIesiri: RegistruIesiriModel): void => {
				this.registruIesiri = registruIesiri;
				this.prepareFormFormRegistruIesiriModel(registruIesiri);				
			},
			onFailure: (appError: AppError): void => { 
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareFormFormRegistruIesiriModel(registruIesiri: RegistruIesiriModel): void {
		this.dataInregistrareFormControl.setValue(registruIesiri.dataInregistrare);
		this.destinatariFormControl.setValue(registruIesiri.destinatari);
		this.trimisPeMailFormControl.setValue(registruIesiri.trimisPeMail);
		this.continutFormControl.setValue(registruIesiri.continut);
		this.numarPaginiFormControl.setValue(registruIesiri.numarPagini);
		this.numarAnexeFormControl.setValue(registruIesiri.numarAnexe);
		this.intocmitDeFormControl.setValue(registruIesiri.intocmitDeUserId);
		this.necesitaRaspunsFormControl.setValue(registruIesiri.asteptamRaspuns);
		this.termenRaspunsFormControl.setValue(registruIesiri.termenRaspuns);
		if (this.isEdit() && this.authManager.hasPermission(AdminPermissionEnum.EDIT_REGISTRU_INTRARI_IESIRI_ELEVATED) && !registruIesiri.asteptamRaspuns){
			this.termenRaspunsFormControl.disable();
		}
		this.uploadedAttachments = registruIesiri.atasamente;
		
		setTimeout(() => {
			
			this.proiecteFormControl.setValue(registruIesiri.proiectIds);
			this.subproiectFormControl.setValue(registruIesiri.subactivity != null ? registruIesiri.subactivity.id : null);
			this.prepareSubactivitySelectItems(registruIesiri.proiectIds);
			this.nomenclatorService.getNomenclatorValue(registruIesiri.tipDocumentId, {
				onSuccess: (value: NomenclatorValueModel): void => {
					let valueOfNomenclatorValueField: ValueOfNomenclatorValueField = new ValueOfNomenclatorValueField(value.nomenclatorId);
					valueOfNomenclatorValueField.value = registruIesiri.tipDocumentId;
					this.tipDocumentFormControl.setValue(valueOfNomenclatorValueField);
					this.changePerspectiveByTipDocument();

					this.registruIesiriDestinatarManagerInputData.tipDocumentId = registruIesiri.tipDocumentId;
					this.registruIesiriDestinatarManagerInputData.registruIesiriId = this.registruId;
					this.registruIesiriDestinatarManagerInputData.registruIesiriRegistryNumber = registruIesiri.numarInregistrare;
					let hasMappedDestinatar: RegistruIesiriDestinatariModel = this.registruIesiri.destinatari.find(destinatar => {
						return ObjectUtils.isNotNullOrUndefined(destinatar.registruIntrariId);
					});
					if (hasMappedDestinatar !== undefined ){
						this.registruIesiriDestinatarManagerInputData.hasMappedDestinatar = true;
					}else{
						this.registruIesiriDestinatarManagerInputData.hasMappedDestinatar = false;
					}
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
				}
			});
		}, 500);
	}

	private prepareForView(): void {
		this.registruIesiriDestinatarManagerInputData.registruIesiriWindowPerspective = "view";
		this.updatePerspectiveForView();
		this.loadRegistruIesiriAndPrepareForm();
		this.unlock();
		this.openWindow();
	}

	private updatePerspectiveForEdit(): void {
		this.destinatarWindowMode = this.mode;
		
		this.setTitle();
		let currentDate: Date = new Date();
		this.dataInregistrareFormControl.setValue(currentDate);
		this.dataInregistrareMaxDate = this.dataInregistrareFormControl.value;
		this.termenRaspunsMinDate = this.dataInregistrareFormControl.value;
		if (!this.authManager.hasPermission(AdminPermissionEnum.EDIT_REGISTRU_INTRARI_IESIRI_ELEVATED)){
			FormUtils.enableOrDisableFormControl(this.trimisPeMailFormControl, false);
			FormUtils.enableOrDisableFormControl(this.continutFormControl, false);
			FormUtils.enableOrDisableFormControl(this.numarPaginiFormControl, false);
			FormUtils.enableOrDisableFormControl(this.numarAnexeFormControl, false);
			FormUtils.enableOrDisableFormControl(this.intocmitDeFormControl, false);
			FormUtils.enableOrDisableFormControl(this.proiecteFormControl, false);
			FormUtils.enableOrDisableFormControl(this.termenRaspunsFormControl, false);
		}
		FormUtils.enableOrDisableFormControl(this.necesitaRaspunsFormControl, false);
		FormUtils.enableOrDisableFormControl(this.tipDocumentFormControl, false);
	}

	private updatePerspectiveForView(): void {
		this.setTitle();
		FormUtils.disableAllFormFields(this.formGroup);
		this.saveButtonDisabled = true;
	}

	private prepareProiecteItems(): void {
		this.projectService.getAllProjects({
			onSuccess: (projectModels: ProjectModel[]): void => {
				projectModels.forEach(projectModel => {
					let proiectItem: SelectItem = {label: projectModel.name, value: projectModel.id};
					this.proiecteItems.push(proiectItem);
				});

				ListItemUtils.sortByLabel(this.proiecteItems);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private getProjectSubactivities(projectId: number): Promise<ProjectSubactivityModel[]> {
		return new Promise<ProjectSubactivityModel[]>((resolve, reject) => {
			this.projectService.getProjectSubactivities(projectId, {
				onSuccess: (subactivities: ProjectSubactivityModel[]): void => {
					resolve(subactivities);
				},onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
					reject();
				}
			});
		});
	}

	private getInRegisterEntryById(entryId: number): Promise<RegistruIntrariModel> {
		return new Promise<RegistruIntrariModel>((resolve, reject) => {
			this.registruIntrariIesiriService.getRegistruIntrariById(entryId, {
				onSuccess: (inRegisterEntry: RegistruIntrariModel): void => {
					resolve(inRegisterEntry);
				},onFailure: (error: AppError): void => {
					this.messageDisplayer.displayAppError(error);
					reject();
				}
			});
		});
	}
	
	public prepareSubactivitySelectItems(projectIds: Array<number>): void {
		if (projectIds.length === 1){
			this.getProjectSubactivities(projectIds[0])
				.then(subactivities => {
					this.subproiecteItems = [];
					subactivities.forEach(subactivity => {
						this.subproiecteItems.push({value: subactivity.id, label: subactivity.name});
					});
					if (this.isEdit() || this.isAdd()){
						this.subproiectFormControl.enable();
					}
				});
		}else{
			this.subproiectFormControl.patchValue(null);
			this.subproiecteItems = [];
			this.subproiectFormControl.disable();
		}
	}

	public onNecesitaRaspunsValueChanged(event: any): void {
		this.changePerspectiveForTermenRaspunsFormControl();
	}

	private changePerspectiveForTermenRaspunsFormControl(): void {
		if (this.isAdd()) {
			if (this.necesitaRaspunsFormControl.value) {
				this.termenRaspunsFormControl.enable();
			} else {
				this.termenRaspunsFormControl.disable();
				this.termenRaspunsFormControl.setValue(null);
			}
		}		
	}

	public onMappedDestinatarAdded(registruIntrariId: number): void {
		if (this.isAdd()){
			this.getInRegisterEntryById(registruIntrariId)
				.then((inRegisterEntrty: RegistruIntrariModel) => {
					this.continutFormControl.setValue(inRegisterEntrty.continut);
					this.intocmitDeFormControl.setValue(inRegisterEntrty.repartizatCatreUserId);
					this.proiecteFormControl.setValue(inRegisterEntrty.proiectIds);
					this.continutFormControl.updateValueAndValidity();
					this.intocmitDeFormControl.updateValueAndValidity();
					this.proiecteFormControl.updateValueAndValidity();
					this.prepareSubactivitySelectItems(inRegisterEntrty.proiectIds);
					this.subproiectFormControl.setValue(!!inRegisterEntrty.subactivity ? inRegisterEntrty.subactivity.id : null);
					this.subproiectFormControl.updateValueAndValidity();
					this.messageDisplayer.displayInfo("OUT_REGISTER_ENTRY_FIELDS_UPDATED_FROM_IN_REGISTER_ENTRY");
				});
		}
	}
	
	public onHide(event: any): void {
		this.closeWindow();
	}

	public onCloseAction(event: any): void {
		this.closeWindow();
	}

	private closeWindow(): void {
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	public onSaveAction(event: any): void {
		if (!this.isValid()) {
			return;
		}
		this.registruIntrariIesiriService.saveRegistruIesiri(this.prepareRegistruIesiriModelFromForm(), {
			onSuccess: (): void => {
				this.messageDisplayer.displaySuccess("REGISTRU_IESIRI_SALVAT");
				this.windowClosed.emit();
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});
	}

	private prepareRegistruIesiriModelFromForm(): RegistruIesiriModel {
		let model: RegistruIesiriModel = new RegistruIesiriModel();
		if (ObjectUtils.isNotNullOrUndefined(this.registruId) && !this.isAdd()) {
			model.id = this.registruId;
		}

		if (ObjectUtils.isNotNullOrUndefined(this.registruIesiri)) {
			model.numarInregistrare = this.registruIesiri.numarInregistrare;
			model.inchis = this.registruIesiri.inchis;
		}
		model.asteptamRaspuns = this.necesitaRaspunsFormControl.value;
		model.continut = this.continutFormControl.value;
		model.dataInregistrare = this.dataInregistrareFormControl.value;
		model.intocmitDeUserId = Number(this.intocmitDeFormControl.value);
		model.trimisPeMail = this.trimisPeMailFormControl.value;
		model.numarAnexe = !!this.numarAnexeFormControl.value ? this.numarAnexeFormControl.value : 0;
		model.numarPagini = !!this.numarPaginiFormControl.value ? this.numarAnexeFormControl.value : 0;
		model.termenRaspuns = this.termenRaspunsFormControl.value;
		model.tipDocumentId = (<ValueOfNomenclatorValueField>this.tipDocumentFormControl.value).value;
		model.destinatari = this.destinatariFormControl.value;
		model.proiectIds = this.proiecteFormControl.value;
		if (this.subproiectFormControl.value != null){
			let subactivityModel = new ProjectSubactivityModel();
			subactivityModel.id = this.subproiectFormControl.value;
			model.subactivity = subactivityModel;
		}
		model.atasamente = this.uploadedAttachments;
		return model;
	}

	private isValid(): boolean {
		return this.validate();
	}

	private openWindow(): void {
		this.windowVisible = true;
	}

	public isAdd(): boolean {
		return this.mode === "add";
	}

	public isEdit(): boolean {
		return this.mode === "edit";
	}

	public isView(): boolean {
		return this.mode === "view";
	}

	private validate(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}

	public onUpload(event): void {
		
		let nrOfUploads: number = event.files.length;
		if (nrOfUploads > 0) {
			this.lock();
		}

		for(let file of event.files) {

			if (!this.uploadedAttachmentsContainsAttachmentWithName(file.name)) {

				let formData:FormData = new FormData();
				formData.append("attachment", file, file.name);
				
				this.attachmentService.uploadFile(formData, {
					onSuccess: (attachment: AttachmentModel) => {
						let attachmentModel: RegistruIesiriAtasamentModel = new RegistruIesiriAtasamentModel();
						attachmentModel.fileName = attachment.name;
						this.uploadedAttachments.push(attachmentModel);
						nrOfUploads--;
						if (nrOfUploads === 0) {
							this.unlock();
						}
					},
					onFailure: (appError: AppError) => {
						nrOfUploads--;
						if (nrOfUploads === 0) {
							this.unlock();
						}
						this.messageDisplayer.displayAppError(appError);
					}
				});
			} else {
				nrOfUploads--;
				if (nrOfUploads === 0) {
					this.unlock();
				}
			}
		}
		
		this.resetSelectedAttachments();
	}

	private resetSelectedAttachments(): void {
		this.fileUpload.files = [];
	}

	private uploadedAttachmentsContainsAttachmentWithName(name: string): boolean {
		return ArrayUtils.isNotEmpty(
			this.uploadedAttachments.filter(
				attachment => attachment.fileName === name
			)
		);
	}

	public hasAttachments(): boolean {
		return ArrayUtils.isNotEmpty(this.uploadedAttachments);
	}

	public onDownloadAtasament(attachment: RegistruIesiriAtasamentModel): void {
		this.lock();
		if (ObjectUtils.isNullOrUndefined(attachment.id) ) {	
			this.attachmentService.downloadFile(attachment.fileName).subscribe(
				(response: HttpResponse<Blob>) => {
					this.unlock();
					DownloadUtils.saveFileFromResponse(response, attachment.fileName);
				}, 
				(error: any) => {
					this.unlock();
					this.messageDisplayer.displayError("DOWNLOAD_FILE_ERROR");
				}
			);
		} else {
			this.registruIntrariIesiriService.downloadAtasamentRegistruIesiriById(attachment.id).subscribe(
				(response: HttpResponse<Blob>) => {
					this.unlock();
					DownloadUtils.saveFileFromResponse(response, attachment.fileName);
				}, 
				(error: any) => {
					this.unlock();
					this.messageDisplayer.displayError("DOWNLOAD_FILE_ERROR");
				}
			);
		}
	}

	public onRemoveAtasament(attachment: RegistruIesiriAtasamentModel): void {
		this.confirmationWindow.confirm({
			approve: (): void => {
				this.confirmationWindow.hide();
				this.lock();
				this.attachmentService.deleteFile(attachment.fileName, {
					onSuccess: (): void => {
						ArrayUtils.removeElement(this.uploadedAttachments, attachment);
						this.unlock();
					}, 
					onFailure: (error: AppError): void => {
						this.unlock();
						this.messageDisplayer.displayAppError(error);
					}
				});
			},
			reject: (): void => {
				this.confirmationWindow.hide();
			}
		},"CONFIRM_DELETE_ATTACHMENT");
		
	}

	public onRemoveFile(file: File){
		this.confirmationWindow.confirm({
			approve: (): void => {
				this.confirmationWindow.hide();
			},
			reject: (): void => {
				this.fileUpload.files.push(file);
				this.confirmationWindow.hide();
			}
		},"CONFIRM_DELETE_ATTACHMENT");
	}

	public onClickProiect(): void {
		this.widthDropdownProiect = (document.getElementById('anexaId').clientWidth * 1) + "px";
	}

	public onProiectSelectionChanged(event: any, multiSelect: MultiSelect){
		this.prepareSubactivitySelectItems(event.value);
		multiSelect.hide();
	}

	public get fileUploadDisabled(): boolean {
		return this.isView();
	}

	private getControlByName(controlName: string): FormControl {
		return <FormControl> this.formGroup.controls[controlName];
	}
	
	public get dataInregistrareFormControl(): FormControl {
		return this.getControlByName("dataInregistrare");
	}

	public get tipDocumentFormControl(): FormControl {
		return this.getControlByName("tipDocument");
	}

	public get trimisPeMailFormControl(): FormControl {
		return this.getControlByName("trimisPeMail");
	}

	public get continutFormControl(): FormControl {
		return this.getControlByName("continut");
	}

	public get numarPaginiFormControl(): FormControl {
		return this.getControlByName("numarPagini");
	}

	public get numarAnexeFormControl(): FormControl {
		return this.getControlByName("numarAnexe");
	}

	public get intocmitDeFormControl(): FormControl {
		return this.getControlByName("intocmitDe");
	}

	public get proiecteFormControl(): FormControl {
		return this.getControlByName("proiecte");
	}

	public get necesitaRaspunsFormControl(): FormControl {
		return this.getControlByName("necesitaRaspuns");
	}

	public get termenRaspunsFormControl(): FormControl {
		return this.getControlByName("termenRaspuns");
	}

	public get destinatariFormControl(): FormControl {
		return this.getControlByName("destinatari");
	}

	public get subproiectFormControl(): FormControl {
		return this.getControlByName("subproiect");
	}
	public get isViewMode(): boolean {
		if (this.isView() ){
			return true;
		} else {
			return false;
		}
	}

}
