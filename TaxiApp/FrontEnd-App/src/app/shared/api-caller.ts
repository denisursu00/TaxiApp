import { JsonMapper } from "./json-mapper";
import { AsyncCallback } from "./async-callback";
import { Injectable } from "@angular/core";
import { Headers, Http, Response, URLSearchParams, ResponseContentType, RequestOptions } from "@angular/http";
import { Observable } from "rxjs/";
import { AppError } from "./model";
import { ObjectUtils } from "./utils/object-utils";
import { AuthManager } from "./auth/auth-manager";
import { AuthDataStorage } from "./auth/auth-data-storage";
import { StringUtils } from "./utils/string-utils";
import { Router } from "@angular/router";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { HttpErrorResponse, HttpResponse } from "@angular/common/http";
import { HttpParams } from "@angular/common/http";
import { ApiPathUtils } from "./utils/api-path-utils";

@Injectable({providedIn: "root"})
export class ApiCaller {

	private baseUrl: string = "rest";
	private jsonMapper: JsonMapper;
	private httpClient: HttpClient;
	
	public constructor(jsonMapper: JsonMapper, httpClient: HttpClient) {
		this.httpClient = httpClient;
		this.jsonMapper = jsonMapper;
	}

	private getHttpHeaders(): HttpHeaders {
		let headersConfig = {
			"Content-Type": "application/json",
			"Accept": "application/json"
		};		
		return new HttpHeaders(headersConfig);
	}

	private getHeadersForUpload(): HttpHeaders {
		let headersConfig = {
			"Accept": "application/json"
		};		
		return new HttpHeaders(headersConfig);
	}

	private getHeadersForDownload(): HttpHeaders {
		let headersConfig = {};
		return new HttpHeaders(headersConfig);
	}

	private buildPath(relativePath: string): string {
		let normalizedRelativePath: string = ApiPathUtils.removeSlashFromStart(relativePath);
		return this.baseUrl + "/" + normalizedRelativePath;
	}
	
	private serializeRequestBody(body: any): any {
		if (body === null || body === "undefined") {
			return {};
		}
		if (ObjectUtils.isString(body) || ObjectUtils.isNumber(body) || ObjectUtils.isBoolean(body)) {
			return body;
		} else if (ObjectUtils.isArray(body)) {
			let bodyAsArray: any = [];
			body.forEach((element: any) => {
				bodyAsArray.push(this.serializeRequestBody(element));
			});
			return bodyAsArray;
		} else if (typeof(body) === "object") {
			return this.jsonMapper.serialize(body);
		} else {
			throw new Error("Tipul obiectului nu este cunoscut [" + typeof(body) + "]");
		}
	}

	private deserializeResponseBody(response: any, outputType: { new(): any }): any {
		if (typeof response !== "object") {
			return response;
		}
		if (response === null) {
			return null;
		}
		return this.jsonMapper.deserialize(response, outputType);
	}

	public call<O>(relativePath: string, body: any, outputType: { new(): any }, callback: AsyncCallback<O, AppError>): void {
		let responseType: any = "json";
		if (ObjectUtils.isAJavaScriptObject(outputType)) {
			responseType = "text";
		}
		this.httpClient
			.post(
				this.buildPath(relativePath),
				this.serializeRequestBody(body),
				{ 
					headers: this.getHttpHeaders(), 
					responseType: responseType
				}
			).subscribe(
				(responseObject: any) => {
					if (ObjectUtils.isAJavaScriptObject(outputType)) {
						try {
							let parsedObject: any = JSON.parse(responseObject);
							callback.onSuccess(parsedObject);
						} catch(error) {
							callback.onSuccess(responseObject);
						} 
					} else {
						callback.onSuccess(this.deserializeResponseBody(responseObject, outputType));
					}
				},
				(responseWhenFail: HttpErrorResponse) => {
					let errorMessage: AppError = new AppError();
					errorMessage.errorCode = "UNRECOVERABLE_EXCEPTION";
					if (responseWhenFail.status === 0) {
						errorMessage.errorCode = "INVOCATION_EXCEPTION";
						errorMessage.errorDetails = "Server error or the server is inaccessible.";
					} else {
						if (ObjectUtils.isNotNullOrUndefined(responseWhenFail.error)) {
							let errorObject: any = responseWhenFail.error;
							if (ObjectUtils.isString(responseWhenFail.error)) {
								try {
									errorObject = JSON.parse(responseWhenFail.error);
								} catch(error) {
									errorObject = null;
								} 
							}
							if (errorObject !== null) {
								errorMessage = this.deserializeResponseBody(errorObject, AppError);
							}							
						}
					}
					callback.onFailure(errorMessage);
				}
			);
	}

	public upload<O>(relativePath: string, body: any, outputType: { new(): any }, callback: AsyncCallback<O, AppError>): void {
		this.httpClient.post(
				this.buildPath(relativePath),
				body,
				{
					headers: this.getHeadersForUpload(),
					responseType: "json"
				}
			).subscribe(
				(responseObject: any) => {
					callback.onSuccess(this.deserializeResponseBody(responseObject, outputType));
				},
				(responseWhenFail: HttpErrorResponse) => {
					let errorMessage: AppError = new AppError();
					if (responseWhenFail.status === 0) {
						errorMessage.errorCode = "INVOCATION_EXCEPTION";
						errorMessage.errorDetails = "Server error or the server is inaccessible.";
					} else {
						errorMessage = this.deserializeResponseBody(responseWhenFail.error, AppError);
					}
					callback.onFailure(errorMessage);
				}
			);
	}

	public download(relativePath: string, params?: any): Observable<HttpResponse<Blob>> {		
		let url: string = this.buildPath(relativePath);
		let httpParams: HttpParams = new HttpParams();
		if (ObjectUtils.isNotNullOrUndefined(params)) {
			Object.keys(params).forEach((key: string) => {
				let paramValue: any = params[key];
				if (ObjectUtils.isNotNullOrUndefined(paramValue)) {
					httpParams = httpParams.append(key, paramValue);
				}
			});
		}		
		return this.httpClient.get(url, {
			observe: "response",
			responseType: "blob",
			params: httpParams,
			headers: this.getHeadersForDownload()
		});
	}
}
