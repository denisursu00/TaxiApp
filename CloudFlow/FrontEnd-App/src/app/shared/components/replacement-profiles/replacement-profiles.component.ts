import { Component } from "@angular/core";
import { Column } from "primeng/primeng";
import { ReplacementProfileModel } from "../../model/replacement-profile/replacement-profile.model";
import { ReplacementProfilesService } from "../../service/replacement-profiles.service";
import { ConfirmationUtils } from "../../utils/confirmation-utils";
import { TranslateUtils } from "../../utils/translate-utils";
import { MessageDisplayer } from "../../message-displayer";
import { AppError } from "../../model/app-error";
import { ObjectUtils } from "../../utils/object-utils";

@Component({
	selector: "app-replacement-profiles",
	templateUrl: "replacement-profiles.component.html",
	styleUrls: ["replacement-profiles.component.css"]
})
export class ReplacementProfilesComponent {

	private static readonly COLUMN_REQUESTER_USERNAME: string = "requesterUsername";
	private static readonly COLUMN_REPLACEMENT_NAME: string = "replacementName";
	private static readonly COLUMN_START_DATE: string = "startDate";
	private static readonly COLUMN_END_DATE: string = "endDate";
	private static readonly COLUMN_STATUS: string = "status";
	
	private static readonly COLUMN_START_DATE_FOR_DISPLAY: string = "startDateForDisplay";
	private static readonly COLUMN_END_DATE_FOR_DISPLAY: string = "endDateForDisplay";

	private static readonly STATUS_PREFIX: string = "REPLACEMENT_PROFILE_STATUS_";

	public selectedReplacement: ReplacementProfileModel;
	public replacements: ReplacementProfileModel[];
	public columns: Column[];

	private confirmationUtils: ConfirmationUtils;
	private translateUtils: TranslateUtils;
	
	private replacementProfilesService: ReplacementProfilesService;
	private messageDisplayer: MessageDisplayer;
	
	public replacementProfilesWindowVisible: boolean;
	public replacementProfilesWindowMode: String;
	public replacementProfileId: number;

	public scrollHeight: string;

	public constructor(replacementProfilesService: ReplacementProfilesService, 
			messageDisplayer: MessageDisplayer, 
			confirmationUtils: ConfirmationUtils, 
			translateUtils: TranslateUtils) {
		this.replacementProfilesService = replacementProfilesService;
		this.messageDisplayer = messageDisplayer;
		this.confirmationUtils = confirmationUtils;
		this.translateUtils = translateUtils;
		this.init();
	}

	private init(): void {
		this.replacementProfilesWindowVisible = false;
		this.scrollHeight = (window.innerHeight - 180) + "px";
		this.reset();
		this.prepareColumns();
		this.populateReplacements();
	}

	public onAdd(event: any): void {
		this.replacementProfilesWindowMode = "new";
		this.replacementProfilesWindowVisible = true;
	}

	public onEdit(event: any): void {
		this.replacementProfileId = this.selectedReplacement.id;
		this.replacementProfilesWindowMode = "edit";
		this.replacementProfilesWindowVisible = true;
	}

	public onRemove(event: any): void {
		if (ObjectUtils.isNullOrUndefined(this.selectedReplacement)) {
			return;
		}
		this.confirmationUtils.confirm("CONFIRM_DELETE_REPLACEMENT_PROFILE", {
			approve: () => {
				this.replacementProfilesService.deleteReplacementProfileById(this.selectedReplacement.id, {
					onSuccess: () => {
						this.refresh();
					},
					onFailure: (appError: AppError) => {
						this.messageDisplayer.displayAppError(appError);
					}
				});
			},
			reject: () => {
			}
		}, "DELETE_REPLACEMENT_PROFILE");
	}

	public onRefresh(event: any): void {
		this.refresh();
	}

	private refresh(): void {
		this.reset();
		this.prepareColumns();
		this.populateReplacements();
	}

	private reset(): void {
		this.replacements = [];
		this.columns = [];
		this.selectedReplacement = null;
		this.replacementProfilesWindowVisible = false;
	}

	private prepareColumns(): void {
		this.columns = [];
		this.columns.push(
			this.buildColumn("LABELS.REQUESTER", ReplacementProfilesComponent.COLUMN_REQUESTER_USERNAME, ReplacementProfilesComponent.COLUMN_REQUESTER_USERNAME),
			this.buildColumn("LABELS.REPLACEMENT", ReplacementProfilesComponent.COLUMN_REPLACEMENT_NAME, ReplacementProfilesComponent.COLUMN_REPLACEMENT_NAME),
			this.buildColumn("LABELS.START_DATE",ReplacementProfilesComponent.COLUMN_START_DATE_FOR_DISPLAY, ReplacementProfilesComponent.COLUMN_START_DATE),
			this.buildColumn("LABELS.END_DATE", ReplacementProfilesComponent.COLUMN_END_DATE_FOR_DISPLAY, ReplacementProfilesComponent.COLUMN_END_DATE),
			this.buildColumn("LABELS.STATUS", ReplacementProfilesComponent.COLUMN_STATUS, ReplacementProfilesComponent.COLUMN_STATUS)
		);
	}

	private buildColumn(header: string, field: string, filterField: string): Column {
		let column: Column = new Column();
		column.header = header;
		column.field = field;
		column.filterField = filterField;
		return column;
	}

	private populateReplacements(): void {
		this.replacementProfilesService.getVisibleReplacementProfiles({
			onSuccess: (replacementProfiles: ReplacementProfileModel[]) => {
				this.buildReplacements(replacementProfiles);
			},
			onFailure: (appError: AppError): void => {
				this.messageDisplayer.displayAppError(appError);
			}
		});		
	}

	private buildReplacements(replacements: ReplacementProfileModel[]): void {
		replacements.forEach(replacement => {
			replacement.status = this.translateUtils.translateLabel(ReplacementProfilesComponent.STATUS_PREFIX + replacement.status);
			this.replacements.push(replacement);
		});
	}

	private onReplacementProfilesWindowClosed(): void {
		this.replacementProfilesWindowVisible = false;
	}

	private onReplacementProfilesWindowActionPerformed(): void {
		this.refresh();
	}
}