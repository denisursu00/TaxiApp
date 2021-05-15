import { Injectable } from "@angular/core";
import { JsonConvert, JsonConverter, JsonCustomConvert, OperationMode, ValueCheckingMode } from "json2typescript";
import { ObjectUtils } from "./utils/object-utils";
import { ArrayUtils } from "./utils/array-utils";


@Injectable()
export class JsonMapper {

	private getJsonConverter(): JsonConvert {
		let jsonConvert: JsonConvert = new JsonConvert();
		jsonConvert.ignorePrimitiveChecks = false; 
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