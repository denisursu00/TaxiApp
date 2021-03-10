import { Input, OnInit } from "@angular/core";
import { CalendarModel } from "@app/shared";

export abstract class CalendarTabContent implements OnInit {

	@Input()
	public mode: "add" | "edit";

	@Input()
	public calendarModel: CalendarModel;

	public ngOnInit(): void {
		this.doWhenNgOnInit();
		if (this.isAdd()) {
			this.prepareForAdd();
		} else if (this.isEdit()) {
			this.prepareForEdit();
		}
	}

	protected isAdd(): boolean {
		return (this.mode === "add");
	}

	protected isEdit(): boolean {
		return (this.mode === "edit");
	}

	protected abstract doWhenNgOnInit(): void;

	protected abstract reset(): void;
	
	protected abstract prepareForAdd(): void;

	protected abstract prepareForEdit(): void;

	protected abstract populateForSave(calendarModel: CalendarModel): void;

	protected abstract isValid(): boolean;
}