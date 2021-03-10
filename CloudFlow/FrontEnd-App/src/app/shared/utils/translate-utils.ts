import { Injectable } from "@angular/core";
import { TranslateService, LangChangeEvent } from "@ngx-translate/core";
import { StringUtils } from "./string-utils";

@Injectable()
export class TranslateUtils {
	
	private translateService: TranslateService;

	public constructor(translateService: TranslateService) {
		this.translateService = translateService;
	}

	public setDefaultLang(lang: string) {
		this.translateService.setDefaultLang(lang);
	}

	public changeLanguage(lang: string): void {
		this.translateService.use(lang);
	}

	public getDefaultLang(): string {
		return this.translateService.getDefaultLang();
	}

	public onLangChangedHandler(langChangedHandler: Function): void {
		this.translateService.onLangChange.subscribe(langChangedHandler);
	}

	public translateLabel(labelCode: string): string {
		return this.translateService.instant("LABELS." + labelCode);
	}

	public translateMessage(messageCode: string): string {
		return this.translateService.instant("MESSAGES." + messageCode);
	}

	public translateCode(code: string): string {
		if (StringUtils.isNotBlank(code)) {
			return this.translateService.instant(code);
		} else {
			return "";
		}
	}
}