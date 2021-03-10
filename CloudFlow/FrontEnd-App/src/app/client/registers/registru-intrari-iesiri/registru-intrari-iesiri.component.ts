import { Component, ViewChild } from "@angular/core";
import { RegistruIntrariComponent } from "./registru-intrari/registru-intrari.component";
import { RegistruIesiriComponent } from "./registru-iesiri";

@Component({
	selector: "app-registru-intrari-iesiri",
	templateUrl: "./registru-intrari-iesiri.component.html"
})
export class RegistruIntrariIesiriComponent {

	@ViewChild(RegistruIntrariComponent)
	registruIntrari: RegistruIntrariComponent;

	@ViewChild(RegistruIesiriComponent)
	registruIesiri: RegistruIesiriComponent;
	
	public activeTabIndex: number;

	public constructor() {
		this.init();
	}

	private init(): void {
		
	}

	public onTabChanged(event: any): void {
		this.activeTabIndex = event.index;
	}

	public onOutRegisterRefreshed(): void {
		this.registruIntrari.reloadRegistruIntrariSelectorData();
	}

	public onInRegisterRefreshed(): void {
		this.registruIesiri.reloadRegistruIesiriSelectorData();
	}
}