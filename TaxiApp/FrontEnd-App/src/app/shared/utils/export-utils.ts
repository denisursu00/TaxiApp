import { Table } from "primeng/table";

export class ExportUtils {
   
public static replaceQuotationMarkIfStringData(cellData: any): any {
		if (typeof cellData === "string") {
			let data:string = cellData;
			if (data.search("\"") > 0) {
				return data.split("\"").join("'");
			}
		}
		return cellData;
	}
}
