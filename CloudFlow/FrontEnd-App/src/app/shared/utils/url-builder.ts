import { ObjectUtils } from "../utils/object-utils";

export class UrlBuilder {

	private address: string;
	private parameters: ParameterMap<string, string>[];

	public constructor(address: string, ...parameters: ParameterMap<string, any>[]) {
		this.address = address;
		this.parameters = parameters;
	}

	public build(): string {

		if (!this.address) {
			throw new Error("The address cannot be null or undefined.");
		}

		let url: string = this.address;
		let separator: string = "?";

		this.parameters.forEach(parameter => {
			if (ObjectUtils.isNotNullOrUndefined(parameter.value)) {
				url += separator + parameter.key + "=" + parameter.value;
				separator = "&";
			}
		});
		return url;
	}
}

export interface ParameterMap<K, V> {
	key: K;
	value: V;
}