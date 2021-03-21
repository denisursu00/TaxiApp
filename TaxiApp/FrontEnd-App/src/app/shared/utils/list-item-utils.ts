import { SelectItem } from "primeng/api";
import { StringUtils } from "./string-utils";
import { ObjectUtils } from "./object-utils";

export class ListItemUtils {

    public static sort(items: any[], sortProperty: string, orderDesc: boolean = false, numericSort: boolean = false): void {
        if (ObjectUtils.isNullOrUndefined(sortProperty)) {
            throw new Error("'sortProperty' nu poate fi null sau undifined.");
        }

        items.sort(function(a,b) {
            let valueA: string = a[sortProperty];
            let valueB: string = b[sortProperty];
            
            if (ObjectUtils.isNullOrUndefined(valueA)) {
                return -1;
            }
            if (ObjectUtils.isNullOrUndefined(valueB)) {
                return 1;
            }

            if (numericSort) {
                return Number(valueA) > Number(valueB) ? 1 : -1;
            }
            
            return (valueA.toString()).localeCompare(valueB.toString());
        });

        if (orderDesc) {
            items.reverse();
        }
    }
 
    public static sortByLabel(items: SelectItem[], orderDesc: boolean = false, numericSort: boolean = false): void {
        this.sort(items, "label", orderDesc, numericSort);      
    }

    public static sortByValue(items: SelectItem[], orderDesc: boolean = false, numericSort: boolean = false): void {
        this.sort(items, "value", orderDesc, numericSort); 
    }
}