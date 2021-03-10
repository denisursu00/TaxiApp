import { Component } from "@angular/core";

@Component({
	selector: "app-loading",
	template: `
		<p-blockUI [blocked]="true">
			<div class="ui-grid ui-grid-responsive ui-grid-pad ui-fluid" style="position: absolute; top: 30%; left: 47%;">
				<div class="ui-grid-row">
					<div class="ui-grid-col-12" style="text-align: center;">
						<p-progressSpinner strokeWidth="4" fill="#777" animationDuration=".5s"></p-progressSpinner>			
					</div>
				</div>
				<div class="ui-grid-row">
					<div class="ui-grid-col-12" style="text-align: center;">
						<span style="color: #FFFFFF;">{{'MESSAGES.PLEASE_WAIT' | translate}}</span>
					</div>
				</div>
			</div>
		</p-blockUI>
	`
})
export class LoadingComponent {
}