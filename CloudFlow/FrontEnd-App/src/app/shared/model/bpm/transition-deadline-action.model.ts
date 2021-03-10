export class TransitionDeadlineActionModel {
	
	public static readonly DEADLINE_ACTION_NOTIFY: number = 1;
	public static readonly DEADLINE_ACTION_ROUTE: number = 2;

	public value: number;

	public get label(): string {
		if (this.value === 1) {
			return "DEADLINE_ACTION_NOTIFY";
		} else if (this.value === 2) {
			return "DEADLINE_ACTION_ROUTE";
		}
		throw new Error("Deadline action value cannot be [" + this.value + "]");
	}

	public constructor(value: number) {
		this.value = value;
	}

	public static getDeadlineActions(): TransitionDeadlineActionModel[] {
		let deadlineActions: TransitionDeadlineActionModel[] = [];
		deadlineActions.push(new TransitionDeadlineActionModel(TransitionDeadlineActionModel.DEADLINE_ACTION_ROUTE));
		deadlineActions.push(new TransitionDeadlineActionModel(TransitionDeadlineActionModel.DEADLINE_ACTION_NOTIFY));
		return deadlineActions;
	}
}