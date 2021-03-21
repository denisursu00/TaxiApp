export interface ConfirmationCallback {
	approve(): void;
	reject(): void;
}