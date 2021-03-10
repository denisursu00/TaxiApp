import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { GaugeChartComponent } from "./gauge-chart/gauge-chart.component";
import { SharedModule } from "@app/shared";

@NgModule({
	imports: [
		CommonModule,
		SharedModule
	],
	declarations: [
		GaugeChartComponent
	],
	exports: [
		GaugeChartComponent
	]
})
export class ChartsModule { }
