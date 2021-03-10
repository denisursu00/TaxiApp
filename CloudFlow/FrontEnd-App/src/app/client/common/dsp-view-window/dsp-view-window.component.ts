import { Component, Input, OnInit, Output, EventEmitter} from "@angular/core";
import { Response } from "@angular/http";
import { ProjectService, TranslateUtils, AppError, MessageDisplayer, DspViewModel, PageConstants, DateConstants, ExportType, BaseWindow } from "@app/shared";
import { DspActivitateViewModel, TaskPriority, TaskStatus, DspActivitateAttachmentViewModel, DownloadUtils } from "@app/shared";
import { SelectItem, Dialog } from "primeng/primeng";
import { HttpResponse } from "@angular/common/http";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
	selector: "app-dsp-view-window",
	templateUrl: "./dsp-view-window.component.html",
	styleUrls: ["./dsp-view-window.component.css"]
})
export class DspViewWindowComponent extends BaseWindow implements OnInit {

	@Input()
	public projectId: number;

	@Output()
	public windowClosed: EventEmitter<void>;

	private messageDisplayer: MessageDisplayer;
	private translateUtils: TranslateUtils;
	private projectService: ProjectService;

	public loadedData: boolean;
	public windowVisible: boolean;

	public title: string;
	public pageSize: number;
	public dateFormat: string;

	public prioritateActivitateFilterSelectItems: SelectItem[];
	public statusActivitateFilterSelectItems: SelectItem[];

	public dspView: DspViewModel;

	public constructor(translateUtils: TranslateUtils, messageDisplayer: MessageDisplayer, 
			projectService: ProjectService) {
		super();
		this.translateUtils = translateUtils;
		this.messageDisplayer = messageDisplayer;
		this.projectService = projectService;

		this.title = "DSP";
		this.pageSize = PageConstants.DEFAULT_PAGE_SIZE;
		this.dateFormat = DateConstants.DATE_FORMAT_FOR_TYPING;
		this.loadedData = false;
		this.windowVisible = false;
		this.windowClosed = new EventEmitter();
		this.prepareFilters();
	}

	public ngOnInit(): void {	
		this.projectService.getDspViewModelByProiectId(this.projectId, {
			onSuccess: (rDspViewModel: DspViewModel): void => {
				this.dspView = rDspViewModel;
				this.updateTitle();	
				this.unlock();
				this.windowVisible = true;
				this.loadedData = true;
			},
			onFailure: (error: AppError): void => {
				this.messageDisplayer.displayAppError(error);
			}
		});
	}

	private prepareFilters(): void {

		this.prioritateActivitateFilterSelectItems = [];
		Object.keys(TaskPriority).forEach((enumOption: string) => {
			this.prioritateActivitateFilterSelectItems.push({
				value: enumOption,
				label: this.translateUtils.translateLabel("PROJECT_TASK_PRIORITY_" + enumOption)
			});
		});

		this.statusActivitateFilterSelectItems = [];
		Object.keys(TaskStatus).forEach((enumOption: string) => {
			this.statusActivitateFilterSelectItems.push({
				value: enumOption,
				label: this.translateUtils.translateLabel("PROJECT_TASK_STATUS_" + enumOption)
			});
		});
		this.statusActivitateFilterSelectItems = this.statusActivitateFilterSelectItems.filter(item => {
			return item.value !== TaskStatus.CANCELLED;
		});

		ListItemUtils.sortByLabel(this.statusActivitateFilterSelectItems);
	}

	private updateTitle(): void {
		this.title = this.dspView.abreviereProiect;
	}

	public onHide(event: any): void {
		this.closeWindow();
	}

	private closeWindow(): void {
		this.windowVisible = false;
		this.windowClosed.emit();
	}

	public onViewAttachments(event: any): void {
	}

	public onDownloadTaskAttachment(activitateAttachment: DspActivitateAttachmentViewModel): void {
		this.lock();
		this.projectService.downloadTaskAttachment(activitateAttachment.id).subscribe(
			(response: HttpResponse<Blob>) => {
				this.unlock();
				DownloadUtils.saveFileFromResponse(response, activitateAttachment.name);
			}, 
			(error: any) => {
				this.unlock();
				this.messageDisplayer.displayError("DOWNLOAD_FILE_ERROR");
			}
		);
	}

	public onExportAsDocxAction(): void {
		this.lock();
		this.projectService.exportDsp(this.projectId, ExportType.DOCX).subscribe(
			(response: HttpResponse<Blob>) => {
				this.unlock();
				DownloadUtils.saveFileFromResponse(response);
			}, 
			(error: any) => {
				this.unlock();
				this.messageDisplayer.displayError("DOWNLOAD_FILE_ERROR");
			}
		);
	}

	public onCloseAction(): void {
		this.closeWindow();
	}
}