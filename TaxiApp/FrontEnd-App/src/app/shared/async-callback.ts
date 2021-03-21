export interface AsyncCallback<R, E> {
	
	onSuccess(result: R): void; 

	onFailure(error: E): void;
}