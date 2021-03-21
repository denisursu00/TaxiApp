import { TranslateLoader } from "@ngx-translate/core";
import * as ro from "./locale/ro.json";
import * as en from "./locale/en.json";
import { Observable, of } from "rxjs";

export class AppTranslateLoader implements TranslateLoader {

	public getTranslation(lang: string): Observable<any> {
		if (lang === "en") {
			return of(en.default);
		}
		return of(ro.default);		
	}
}