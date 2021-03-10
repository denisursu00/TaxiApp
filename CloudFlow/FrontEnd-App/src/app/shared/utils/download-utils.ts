import { Response } from "@angular/http";
import { StringUtils } from "./string-utils";
import { ObjectUtils } from "./object-utils";
import { ArrayUtils } from "./array-utils";
import { HttpResponse } from "@angular/common/http";

export class DownloadUtils {

	// TODO - de facut constnate pentru headere, mime type etc,

	public static saveFileFromResponse(response: HttpResponse<Blob>, fileName?: string): void {

		let contentType: string = this.extractContentTypeFromResponse(response);
		if (StringUtils.isBlank(contentType)) {
			contentType = "application/octet-stream";
		}
		
		let blob = response.body;

		let finalFileName: string = fileName;
		if (StringUtils.isBlank(finalFileName)) {
			finalFileName = this.extractFileNameFromResponse(response);
		}
		if (StringUtils.isBlank(finalFileName)) {
			throw new Error("no file name");
		}
		if (ObjectUtils.isNotNullOrUndefined(window.navigator) && window.navigator.msSaveOrOpenBlob) {
			navigator.msSaveOrOpenBlob(blob, finalFileName);
		} else {
			let link = document.createElement("a");
			link.style.display = "none";
			document.body.appendChild(link);
			if (ObjectUtils.isNotNullOrUndefined(link.download)) {
				link.setAttribute("href", URL.createObjectURL(blob));
				link.setAttribute("download", finalFileName);
				link.click();
			} else {
				let url = window.URL.createObjectURL(blob);
				window.open(url);
			}
			document.body.removeChild(link);
		}
	}

	private static extractFileNameFromResponse(response: HttpResponse<Blob>): string {
		let contenDisposition: string = this.getHeaderValue(response, "content-disposition");
		if (StringUtils.isBlank(contenDisposition)) {
			return null;
		}
		// TODO - De aici in jos ar trebui refacut. Trebuie sa fie gata!!!
		let contentDispositionValues: string[] = contenDisposition.split(";");
		let fileName: string = null;
		if (contentDispositionValues.length >= 1) {
			let filenamePropAndValue: string[] = contentDispositionValues[1].split("=");
			if (filenamePropAndValue.length >= 1) {
				fileName = filenamePropAndValue[1];
				if (StringUtils.isNotBlank(fileName)) {
					fileName = fileName.replace(/['"]/g, "");
					fileName = fileName.trim();
				}				
			}
		}
		return fileName;
	}

	private static extractContentTypeFromResponse(response: HttpResponse<Blob>): string {
		return this.getHeaderValue(response, "content-type");
	}

	private static getHeaderValue(response: HttpResponse<Blob>, key: string): string {
		let realHeaderKey: string = null;
		let headerKeys: string[] = response.headers.keys();
		if (ArrayUtils.isNotEmpty(headerKeys)) {
			headerKeys.forEach((realKey: string) => {
			if (realKey.toLowerCase().trim() === key.toLowerCase().trim()) {
					realHeaderKey = realKey;
				}
			});
		}
		return response.headers.get(realHeaderKey);
	}
}