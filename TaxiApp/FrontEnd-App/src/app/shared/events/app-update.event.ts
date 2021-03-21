import { AppEvent } from "./../app-event-mediator";

export class AppUpdateEvent extends AppEvent {

	private _newAppVersion: string;
	
	constructor(newAppVersion: string) {
		super();
		this._newAppVersion = newAppVersion;
	}

	public get newAppVersion(): string {
		return this._newAppVersion;
	}

}