import { TaskModel } from "@app/shared";

export abstract class TaskTabContent {

	protected abstract reset(): void;

	protected abstract populateForSave(task: TaskModel): void;

	protected abstract isValid(): boolean;
}