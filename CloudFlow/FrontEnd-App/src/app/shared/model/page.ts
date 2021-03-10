import { PageConstants } from "./../constants";
import { JsonProperty, JsonObject } from "json2typescript";
import { ArrayUtils } from "../utils/array-utils";

@JsonObject
export class Page<I> {

	@JsonProperty("items")
	public items: Array<I> = [];

	@JsonProperty("totalItems", Number)
	public totalItems: number = null;

	@JsonProperty("pageSize", Number)
	public pageSize: number = null;

	public constructor() {
		this.items = new Array<I>();
		this.pageSize = PageConstants.DEFAULT_PAGE_SIZE;
	}

	public hasItems(): boolean {
		return ArrayUtils.isNotEmpty(this.items);
	}
}