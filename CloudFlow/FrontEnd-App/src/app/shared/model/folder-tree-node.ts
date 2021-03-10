import { DocumentLocationModel } from "../model/document-location.model";
import { FolderModel } from "../model/folder.model";

export class FolderTreeNode<T> {
	
	public data: T;

	public constructor(data: T) {
		this.data = data;
	}

	public isDocumentLocation(): boolean {
		return this.data instanceof DocumentLocationModel;
	}

	public isFolder(): boolean {
		return this.data instanceof FolderModel;
	}
}