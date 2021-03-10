import { Component, OnInit, ViewChild, ElementRef, AfterViewInit, Input, OnChanges } from "@angular/core";
import { ObjectUtils } from "@app/shared";

// TODO: Trebuie implementat si pentru cazul in care valoarea curenta este mai mare decat valoarea care vine
// EX: valoarea curenta este 51 si valoarea care vine este 24. Diagrama trebuie sa scada progresiv valoarea de la 51 la 24
// 	   fara ca aceasta sa se reseteze

@Component({
	selector: "app-gauge-chart",
	templateUrl: "./gauge-chart.component.html",
	styleUrls: ["./gauge-chart.component.css"]
})
export class GaugeChartComponent implements OnInit, OnChanges {

	public static readonly ONE_SECOND_IN_MILISECONDS: number = 1000;
	
	public static readonly DEFAULT_UNIT_LABEL: string = "";

	public static readonly DEFAULT_DEVICE_PIXEL_RATIO: number = 1;

	@Input()
	public type: "full" | "half" = "half";

	@Input()
	public size: number = 200;

	@Input()
	public fontSize: number = 30;

	@Input()
	public fontColor: string = "#44ce42";
	
	@Input()
	public backgroundColor: string = "#bbf4ba";

	@Input()
	public foregroundColor: string = "#44ce42";

	@Input()
	public thickness: number = 30;

	@Input()
	public min: number = 0;
	
	@Input()
	public max: number = 100;
	
	@Input()
	public value: number;
	
	@Input()
	public unitLabel;

	@Input()
	public label: string = "";

	@ViewChild("canvas")
	public canvas: ElementRef;

	public canvasContext: CanvasRenderingContext2D;

	private currentValue = 0;
	private animationLoop: any;

	private devicePixelRatio: number;

	public constructor() {
		this.init();
	}

	private init(): void {
		this.devicePixelRatio = window.devicePixelRatio || GaugeChartComponent.DEFAULT_DEVICE_PIXEL_RATIO;
		if (window.devicePixelRatio > 1) {
			this.devicePixelRatio = this.devicePixelRatio * 0.5
		} 
		if (window.devicePixelRatio < 1) {
			this.devicePixelRatio = this.devicePixelRatio / 0.5
		}
	}

	public ngOnInit(): void {
		if (!this.isFullType() && !this.isHalfType()) {
			throw new Error("Property [type] cannot be [" + this.type + "]. Possible values [full] or [half]");
		}
	}

	public ngOnChanges(): void {
		this.configureCanvas();
		this.reset();
		this.setAnimationInterval();
	}
	
	private configureCanvas(): void {

		this.canvasContext = (<HTMLCanvasElement>this.canvas.nativeElement).getContext("2d");
		this.canvas.nativeElement.width = this.size * this.devicePixelRatio;
		this.canvas.nativeElement.height = this.size * this.devicePixelRatio;

		if (this.isHalfType()) {
			this.canvas.nativeElement.width = this.size * this.devicePixelRatio;
			this.canvas.nativeElement.height = this.size * this.devicePixelRatio / 2 + this.fontSize;
		}

		this.canvasContext.scale(this.devicePixelRatio, this.devicePixelRatio);
	}

	public draw(): void {
		this.clearCanvas();
		this.drawGauge();
		this.drawCurrentValue();
		this.drawLabel();
	}

	private drawGauge(): void {

		if (this.isFullType()) {
			this.drawBackgroundCircleForFullGauge();
			this.drawForegroundCircleForFullGauge();
		} else if (this.isHalfType()) {
			this.drawBackgroundCircleForHalfGauge();
			this.drawForegroundCircleForHalfGauge();
		}
	}

	private drawBackgroundCircleForFullGauge(): void {
		this.drawArc(this.backgroundColor, 0, Math.PI * 2);
	}
	
	private drawForegroundCircleForFullGauge(): void {
		let ratio: number = this.currentValue / this.max;
		let ratioInRadians: number = ratio * Math.PI * 2;

		let startAngle: number = ratioInRadians - Math.PI / 2;
		let endAngle: number = 0 -  Math.PI / 2;
		
		this.drawArc(this.foregroundColor, startAngle, endAngle);
	}

	private drawBackgroundCircleForHalfGauge(): void {
		this.drawArc(this.backgroundColor, 0, Math.PI);
	}

	private drawForegroundCircleForHalfGauge(): void {
		let ratio: number = this.currentValue / this.max;
		let ratioInRadians: number = ratio * Math.PI;
		
		let startAngle: number = ratioInRadians -  Math.PI;
		let endAngle: number = Math.PI;
		
		this.drawArc(this.foregroundColor, startAngle, endAngle);
		this.canvasContext.stroke();
	}

	private drawArc(color: string, startAngle: number, endAngle: number): void {
		
		this.canvasContext.beginPath();

		let xPosition = this.size / 2;
		let yPosition = this.size / 2;

		this.canvasContext.arc(xPosition, yPosition, this.calculateRadius(), startAngle, endAngle, true);

		this.canvasContext.strokeStyle = color;
		this.canvasContext.lineWidth = this.thickness;

		this.canvasContext.stroke();
	}

	private drawCurrentValue(): void {

		this.canvasContext.fillStyle = this.fontColor;
		this.canvasContext.font = this.fontSize  + "px arial";
		
		let textForDisplay: string = this.prepareCurrentValueForDisplay();
		let textWidth = this.canvasContext.measureText(textForDisplay).width;

		let xPosition = (this.size / 2) - (textWidth / 2);
		let yPosition = (this.size / 2) + (this.fontSize / 2);

		this.canvasContext.fillText(textForDisplay, xPosition, yPosition);
	}

	private drawLabel(): void {
		// TODO: Trebuie implementat
	}

	private setAnimationInterval(): void {

		let differenceBetweenCurrentValueAndGivenValue = this.value - this.currentValue;
		
		this.animationLoop = setInterval(
			() => this.animate(),
			GaugeChartComponent.ONE_SECOND_IN_MILISECONDS / differenceBetweenCurrentValueAndGivenValue
		);
	}
	
	private animate(): void {
		
		if (ObjectUtils.isNullOrUndefined(this.value)) {
			this.value = 0;
		}

		if (this.currentValue < this.value) {
			this.currentValue++;
		} else {
			this.currentValue--;
		}
		
		this.draw();

		if(this.currentValue >= this.value || this.currentValue >= this.max) {
			clearInterval(this.animationLoop);
		}
	}

	private isFullType(): boolean {
		return this.type === "full";
	}

	private isHalfType(): boolean {
		return this.type === "half";
	}

	private reset(): void {
		if (ObjectUtils.isNotNullOrUndefined(this.animationLoop)) {
			clearInterval(this.animationLoop);
		}
		this.currentValue = 0;
	}

	private clearCanvas(): void {
		this.canvasContext.clearRect(0, 0, this.size, this.size);
	}

	private calculateRadius(): number {
		return (this.size / 2) - this.thickness;
	}

	private prepareUnitLabel(): string {
		return ObjectUtils.isNotNullOrUndefined(this.unitLabel) ? " " + this.unitLabel : GaugeChartComponent.DEFAULT_UNIT_LABEL;
	}

	private prepareCurrentValueForDisplay(): string {
		return this.currentValue + this.prepareUnitLabel();
	}
}