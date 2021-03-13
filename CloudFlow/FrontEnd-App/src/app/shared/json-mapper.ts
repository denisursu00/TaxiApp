import { Injectable } from "@angular/core";
import { environment } from "@app/../environments/environment";
import { JsonConvert, JsonConverter, JsonCustomConvert, OperationMode, ValueCheckingMode } from "json2typescript";
import { ObjectUtils } from "./utils/object-utils";
import { ArrayUtils } from "./utils/array-utils";


@Injectable()
export class JsonMapper {

	private getJsonConverter(): JsonConvert {
		let jsonConvert: JsonConvert = new JsonConvert();
		if (!environment.production) {
			// jsonConvert.operationMode = OperationMode.LOGGING; // print some debug data
		}
		jsonConvert.ignorePrimitiveChecks = false; // don't allow assigning number to string etc.
		jsonConvert.valueCheckingMode = ValueCheckingMode.ALLOW_NULL;
		return jsonConvert;
	}

	public deserialize(json: any, classReference: { new (): any;}): any {		
		return this.getJsonConverter().deserialize(json, classReference);
	}

	public serialize(data: any): any {
		return this.getJsonConverter().serialize(data);
	}
}

@JsonConverter
export class JsonDateConverter implements JsonCustomConvert<Date> {

	serialize(date: Date): any {
		if (date !== null && date !== undefined) {
			return date.toISOString();
		}
		return null;
	}

	deserialize(date: any): Date {
		if (date !== null && date !== undefined) {
			return new Date(date);
		}
		return null;
	}
}