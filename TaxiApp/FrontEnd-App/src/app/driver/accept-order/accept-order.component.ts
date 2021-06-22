import { Component, OnInit } from '@angular/core';
import { AppError, CarsService, ClientsService, DriversService, LoggedInUserModel, MessageDisplayer, RidesService } from '@app/shared';
import { AuthManager } from '@app/shared/auth';
import { DriverModel } from '@app/shared/model/drivers';
import { RideModel } from '@app/shared/model/rides/ride.model';

@Component({
  selector: 'app-accept-order',
  templateUrl: './accept-order.component.html',
  styleUrls: ['./accept-order.component.css']
})
export class AcceptOrderComponent implements OnInit {

  private clientService: ClientsService;
	private driversService: DriversService;
	private rideService: RidesService;
	private authManager: AuthManager;
	private messageDisplayer: MessageDisplayer;

  private loggedInUser: LoggedInUserModel;
  private loggedInDriver: DriverModel;

  public rides: RideModel[];

  public orderWindowVisible: boolean = false;
  public acceptedOrder: RideModel;

  constructor(clientsService: ClientsService, driversService: DriversService, ridesService: RidesService, authManager: AuthManager, messageDisplayer: MessageDisplayer) {
    this.clientService = clientsService;
    this.driversService = driversService;
    this.rideService = ridesService;
    this.authManager = authManager;
    this.messageDisplayer = messageDisplayer;
    this.init();
  }

  ngOnInit() {

  }

  private async init(): Promise<void> {
    this.rides = [];
    this.loggedInUser = this.authManager.getLoggedInUser();
    await this.getLoggedInDriver();
    await this.getAllOrders();
    this.getOrdersWithInterval();
  }

  private getLoggedInDriver(): Promise<void> {
    return new Promise<void>((resolve,reject) => {
			this.driversService.getDriverByUserId(this.loggedInUser.id, {
				onSuccess: (driver: DriverModel): void => {
					this.loggedInDriver = driver;
					resolve();
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
					reject();
				}
			});
		});
  }

  private getOrdersWithInterval(): void {
    setInterval(() => {
      this.getAllOrders();
		}, 10000);
  }

  private getAllOrders(): Promise<void> {
    return new Promise<void>((resolve,reject) => {
			this.rideService.getRidesForDriver(this.loggedInDriver.id, {
        onSuccess: (rides: RideModel[]): void => {
					this.rides = rides;
					resolve();
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
					reject();
				}
      });
		});
  }

  public onAcceptOrder(ride: RideModel): void {
    this.acceptedOrder = ride;
    this.acceptedOrder.driverId = this.loggedInDriver.id;
    this.rideService.saveRide(this.acceptedOrder, {
      onSuccess: (): void => {
        this.orderWindowVisible = true;
      },
      onFailure: (appError: AppError): void => {
        this.messageDisplayer.displayAppError(appError);
      }
    });
  }

  public onOrderWindowClosed(): void {
    this.orderWindowVisible = false;
    this.acceptedOrder = null;
  }

}
