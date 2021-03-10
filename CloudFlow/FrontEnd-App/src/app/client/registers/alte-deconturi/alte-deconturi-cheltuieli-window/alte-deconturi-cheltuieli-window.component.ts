import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from "@angular/core";
import { AlteDeconturiCheltuieliModel, TipDocumentJustificativForCheltuieliEnum, AlteDeconturiCheltuialaAtasamentModel } from "@app/shared/model/alte-deconturi";
import { FormBuilder, FormGroup, Validators, AbstractControl, FormControl } from "@angular/forms";
import { MessageDisplayer, DateConstants, DateUtils, UiUtils, StringValidators, ObjectUtils, BaseWindow } from "@app/shared";
import { FormUtils, TranslateUtils, AttachmentService, AttachmentModel, AppError, ArrayUtils, DownloadUtils, AlteDeconturiService } from "@app/shared";
import { SelectItem, FileUpload, Dialog } from "primeng/primeng";
import { environment } from "@app/../environments/environment";
import { Response } from "@angular/http";
import { HttpResponse } from "@angular/common/http";


@Component({
	selector: "app-alte-deconturi-cheltuieli-window",
	templateUrl: "./alte-deconturi-cheltuieli-window.component.html"
})
export class AlteDeconturiCheltuieliWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public mode: "add" | "edit" | "view";

	@Input()
	public cheltuiala: AlteDeconturiCheltuieliModel;

	@Input()
	public decontId: number;

	@Input()
	public dataDecont: Date;

	@Output()
	public windowClosed: EventEmitter<void>;

	@Output()
	public dataSaved: EventEmitter<AlteDeconturiCheltuieliModel>;

	private formBuilder: FormBuilder;
	private translateUtils: TranslateUtils;

	public visible: boolean;

	public dateFormat: string;
	public yearRange: string;

	public formGroup: FormGroup;

	public tipDocumentJustificativItems: SelectItem[];

	public saveButtonVisible: boolean = true;

	public mandatoryVisible: boolean = false;

	public valoareMinimaCheltuiala: number = 1;
	public validationMessageMinLengthParameter = {value: this.valoareMinimaCheltuiala};

	public uploadedAttachments: AlteDeconturiCheltuialaAtasamentModel[];

	private attachmentService: AttachmentService;	
	private alteDeconturiService: AlteDeconturiService;
	private messageDisplayer: MessageDisplayer;

	@ViewChild(FileUpload)
	private fileUpload: FileUpload;

	public constructor(attachmentService: AttachmentService, formBuilder: FormBuilder, alteDeconturiService: AlteDeconturiService, translateUtils: TranslateUtils, messageDisplayer: MessageDisplayer) {
		super();
		this.attachmentService = attachmentService;
		this.formBuilder = formBuilder;
		this.alteDeconturiService = alteDeconturiService;
		this.translateUtils = translateUtils;
		this.messageDisplayer = messageDisplayer;
		this.windowClosed = new EventEmitter<void>();
		this.dataSaved = new EventEmitter<AlteDeconturiCheltuieliModel>();
		this.init();
	}
	
	private init(): void {
		this.visible = true;
		
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.yearRange = DateUtils.getDefaultYearRange();
		
		this.uploadedAttachments = [];

		this.prepareForm();
		this.prepareTipDocumentJustificativItems();
		this.unlock();		
	}

	private prepareForm(): void {
		this.formGroup = this.formBuilder.group([]);
		this.formGroup.addControl("tipDocumentJustificativ", new FormControl(null, [Validators.required]));
		this.formGroup.addControl("explicatie", new FormControl(null, []));
		this.formGroup.addControl("numarDocumentJustificativ", new FormControl(null, []));
		this.formGroup.addControl("dataDocumentJustificativ", new FormControl(null, []));
		this.formGroup.addControl("valoareCheltuiala", new FormControl(null, [Validators.required, Validators.min(this.valoareMinimaCheltuiala)]));
	}

	private prepareTipDocumentJustificativItems(): void {
		this.tipDocumentJustificativItems = [
			{label: this.translateUtils.translateLabel(TipDocumentJustificativForCheltuieliEnum.BON_FISCAL), value: TipDocumentJustificativForCheltuieliEnum.BON_FISCAL},
			{label: this.translateUtils.translateLabel(TipDocumentJustificativForCheltuieliEnum.FACTURA), value: TipDocumentJustificativForCheltuieliEnum.FACTURA},
			{label: this.translateUtils.translateLabel(TipDocumentJustificativForCheltuieliEnum.CHITANTA), value: TipDocumentJustificativForCheltuieliEnum.CHITANTA},
			{label: this.translateUtils.translateLabel(TipDocumentJustificativForCheltuieliEnum.ALTELE), value: TipDocumentJustificativForCheltuieliEnum.ALTELE}
		];
	}

	public ngOnInit(): void {
		if (this.isEditMode()) {
			this.prepareForEdit();
			this.mandatoryVisible = true;		
		} else if (this.isAddMode()) {
			this.mandatoryVisible = true;
		}else if (this.isViewMode()) {
			this.prepareForView();
			this.mandatoryVisible = false;
		}
		
	}

	private prepareForEdit(): void {
		if (ObjectUtils.isNullOrUndefined(this.cheltuiala)) {
			throw new Error("Input property [cheltuiala] cannot be null or undefined on edit mode.");
		}
		this.prepareFormFromCheltuiala();
	}

	private prepareForView(): void {
		if (ObjectUtils.isNullOrUndefined(this.cheltuiala)) {
			throw new Error("Input property [cheltuiala] cannot be null or undefined on edit mode.");
		}
		this.preparePerspectiveForViewMode();
		this.prepareFormFromCheltuiala();
	}

	private preparePerspectiveForViewMode(): void {
		FormUtils.disableAllFormFields(this.formGroup);
		this.saveButtonVisible = false;
	}

	private prepareFormFromCheltuiala(): void {
		this.tipDocumentJustificativFormControl.setValue(this.cheltuiala.tipDocumentJustificativ);
		this.explicatieFormControl.setValue(this.cheltuiala.explicatie);
		this.numarDocumentJustificativFormControl.setValue(this.cheltuiala.numarDocumentJustificativ);
		this.dataDocumentJustificativFormControl.setValue(this.cheltuiala.dataDocumentJustificativ);
		this.valoareCheltuialaFormControl.setValue(this.cheltuiala.valoareCheltuiala);
		this.uploadedAttachments = this.cheltuiala.atasamente;
	}

	public onSave(): void {
		if (!this.isFormValid()) {
			return;
		}

		let cheltuiala: AlteDeconturiCheltuieliModel = new AlteDeconturiCheltuieliModel();

		if (this.isEditMode()) {
			cheltuiala.id = this.cheltuiala.id;
		} else {
			cheltuiala.id = null;
		}
		
		cheltuiala.tipDocumentJustificativ = this.tipDocumentJustificativFormControl.value;
		cheltuiala.explicatie = this.explicatieFormControl.value;
		cheltuiala.numarDocumentJustificativ = this.numarDocumentJustificativFormControl.value;
		cheltuiala.dataDocumentJustificativ = this.dataDocumentJustificativFormControl.value;
		cheltuiala.valoareCheltuiala = this.valoareCheltuialaFormControl.value;		
		cheltuiala.atasamente = this.uploadedAttachments;

		this.dataSaved.emit(cheltuiala);
		this.windowClosed.emit();
	}
	
	public isFormValid(): boolean {
		FormUtils.validateAllFormFields(this.formGroup);
		return this.formGroup.valid;
	}

	private isEditMode(): boolean {
		return this.mode === "edit";
	}

	public isViewMode(): boolean {
		return this.mode === "view";
	}

	public isAddMode(): boolean {
		return this.mode === "add";
	}

	public onTipDocumentJustificativValueChanged(event: any): void {
		if (event.value === TipDocumentJustificativForCheltuieliEnum.ALTELE) {
			this.explicatieFormControl.setValidators([Validators.required, StringValidators.blank]);
			this.explicatieFormControl.updateValueAndValidity();
		} else {
			this.explicatieFormControl.clearValidators();
			this.explicatieFormControl.updateValueAndValidity();
		}
		
	}

	private getControlByName(name: string): AbstractControl {
		return this.formGroup.controls[name];
	}

	public get tipDocumentJustificativFormControl(): AbstractControl {
		return this.getControlByName("tipDocumentJustificativ");
	}

	public get explicatieFormControl(): AbstractControl {
		return this.getControlByName("explicatie");
	}

	public get numarDocumentJustificativFormControl(): AbstractControl {
		return this.getControlByName("numarDocumentJustificativ");
	}

	public get dataDocumentJustificativFormControl(): AbstractControl {
		return this.getControlByName("dataDocumentJustificativ");
	}

	public get valoareCheltuialaFormControl(): AbstractControl {
		return this.getControlByName("valoareCheltuiala");
	}

	public onHide($event): void {
		this.windowClosed.emit();
	}

	public onCancel($event): void {
		this.windowClosed.emit();
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
						let attachmentModel: AlteDeconturiCheltuialaAtasamentModel = new AlteDeconturiCheltuialaAtasamentModel();
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

	public onDownloadAtasament(attachment: AlteDeconturiCheltuialaAtasamentModel): void {
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
			this.alteDeconturiService.downloadAtasamentOfCheltuiala(attachment.id).subscribe(
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

	public onRemoveAtasament(attachment: AlteDeconturiCheltuialaAtasamentModel): void {	
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
	}

	public get fileUploadDisabled(): boolean {
		return this.isViewMode();
	}

	public get isExplicatieMandatory(): boolean {
		if (this.mandatoryVisible && (this.tipDocumentJustificativFormControl.value === TipDocumentJustificativForCheltuieliEnum.ALTELE)) {
			return true;
		} else {
			return false;
		}
	}
}
