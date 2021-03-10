import { Component, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, Validators, AbstractControl } from "@angular/forms";
import { StringValidators, CalendarModel, FormUtils, ArrayUtils, CalendarUserRightsModel } from "@app/shared";
import { CalendarTabContent } from "../calendar-tab-content";
import { OrganizationEntityModel } from "@app/shared/model/organization-entity.model";

@Component({
	selector: "app-calendar-users-rights-tab-content",
	templateUrl: "./calendar-users-rights-tab-content.component.html"
})
export class CalendarUsersRightsTabContentComponent extends CalendarTabContent implements OnInit {

	public usersRights: CalendarUserRightsModel[];

	public permitAll: boolean = false;

	public calendarId: number;

	public constructor() {
		super();
	}

	public doWhenNgOnInit(): void {
		if (this.isEdit()) {
			this.calendarId = this.calendarModel.id;
		}
	}

	public onUsersRightsSelectionChanged(usersRights: CalendarUserRightsModel[]): void {
		this.usersRights = usersRights;
	}

	public reset(): void {
		this.usersRights = [];
		this.permitAll = false;
	}
	
	public prepareForAdd(): void {
		this.reset();
	}
	
	public prepareForEdit(): void {
		this.usersRights = this.calendarModel.usersRights;
		this.permitAll = this.calendarModel.permitAll;
	}
	
	public populateForSave(calendarModel: CalendarModel): void {
		calendarModel.permitAll = this.permitAll;
		if (!calendarModel.permitAll) {
			calendarModel.usersRights = this.usersRights;
		}
	}
	
	public isValid(): boolean {
		if (!this.permitAll) {
			return ArrayUtils.isNotEmpty(this.usersRights);
		}
		return true;
	}
}
