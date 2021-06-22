import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AppError, BaseWindow, CarsService, ClientsService, ConfirmationUtils, DateUtils, MessageDisplayer, ObjectUtils, OrganizationService, ParameterConstants, ParameterModel, ParametersService, RidesService, StringUtils } from '@app/shared';
import { CarCategoryModel } from '@app/shared/model/cars';
import { ClientModel } from '@app/shared/model/clients/client.model';
import { UserModel } from '@app/shared/model/organization/user.model';
import { RideModel } from '@app/shared/model/rides/ride.model';

@Component({
  selector: 'app-accepted-order-window',
  templateUrl: './accepted-order-window.component.html',
  styleUrls: ['./accepted-order-window.component.css']
})
export class AcceptedOrderWindowComponent extends BaseWindow implements OnInit {

  @Input()
  public acceptedOrder: RideModel;

  @Output()
	public windowClosed: EventEmitter<void>;

  public positionTop: number;

  private organizationService: OrganizationService;
  private clientService: ClientsService;
  private carService: CarsService;
  private rideService: RidesService;
  private parameterService: ParametersService;
  private messageDisplayer: MessageDisplayer;
  private confirmationUtils: ConfirmationUtils;

  public windowVisible: boolean;

  public userClient: UserModel;

  public carCategory: CarCategoryModel;

  public rideStarted: boolean = false;
  public rideFinished: boolean = false;

  public ridePrice: number = 0;
  public ridePriceString: string = "0";

  private nodeJsTimer: NodeJS.Timer;

  private pricePerHourDay: number;
  private pricePerKmDay: number;
  private pricePerHourNight: number;
  private pricePerKmNight: number;

  constructor(organizationService: OrganizationService, clientService: ClientsService, carService: CarsService, rideService: RidesService, messageDisplayer: MessageDisplayer,
              parameterService: ParametersService, confirmationUtils: ConfirmationUtils) {
    super();
    this.organizationService = organizationService;
    this.clientService = clientService;
    this.carService = carService;
    this.rideService = rideService;
    this.parameterService = parameterService;
    this.messageDisplayer = messageDisplayer;
    this.confirmationUtils = confirmationUtils;
    this.windowClosed = new EventEmitter<void>();
    this.init();
  }

  ngOnInit() {
  }

  private init(): void {
    this.positionTop = (document.documentElement.clientHeight/100)*15;
    this.windowVisible = true;
    this.lock();
    setTimeout(async ()=>{
      await this.getParameters();
      await this.getUserClient();
      await this.getCarCategory();
      this.unlock();
    }, 1000);
	}

  private getUserClient(): Promise<void> {
    return new Promise<void>((resolve,reject)=>{
      this.clientService.getClientById(this.acceptedOrder.clientId, {
        onSuccess: (client: ClientModel): void => {
          this.organizationService.getUserById(client.userId, {
            onSuccess: (user: UserModel): void => {
              this.userClient = user;
              resolve();
            },
            onFailure: (appError: AppError): void => {
              this.messageDisplayer.displayAppError(appError);
              reject();
            }
          });
				},
				onFailure: (appError: AppError): void => {
					this.messageDisplayer.displayAppError(appError);
          reject();
				}
      });
    });
  }

  private getCarCategory(): Promise<void> {
    return new Promise<void>((resolve,reject)=>{
      this.carService.getCarCategoryById(this.acceptedOrder.carCategoryId, {
        onSuccess: (carCategory: CarCategoryModel): void => {
          this.carCategory = carCategory;
          resolve();
        },
        onFailure: (appError: AppError): void => {
          this.messageDisplayer.displayAppError(appError);
          reject();
        }
      });
    });
  }

  private getParameters(): Promise<void> {
    return new Promise<void>((resolve,reject)=>{
      this.parameterService.getAllParameters({
        onSuccess: (parameters: ParameterModel[]): void => {
          for (const parameter of parameters) {
            if (parameter.name === ParameterConstants.PRICE_HOUR_DAY) {
              this.pricePerHourDay = StringUtils.toNumber(parameter.value);
            }
            if (parameter.name === ParameterConstants.PRICE_KM_DAY) {
              this.pricePerKmDay = StringUtils.toNumber(parameter.value);
            }
            if (parameter.name === ParameterConstants.PRICE_HOUR_NIGHT) {
              this.pricePerHourNight = StringUtils.toNumber(parameter.value);
            }
            if (parameter.name === ParameterConstants.PRICE_KM_NIGHT) {
              this.pricePerKmNight = StringUtils.toNumber(parameter.value);
            }
          }
          resolve();
        },
        onFailure: (appError: AppError): void => {
          this.messageDisplayer.displayAppError(appError);
          reject();
        }
      });
    });
  }

  private isDay(): boolean {
    let date = new Date();
    let minimumDay = new Date();
    minimumDay.setHours(6,0,0,0);
    let maximumDay = new Date();
    maximumDay.setHours(20,59,59,999);
    let day: boolean = (date.getTime() < maximumDay.getTime()) && (date.getTime() > minimumDay.getTime());
    return day;
  }

  private startRide(): void {
    this.acceptedOrder.startTime = new Date();
    this.rideService.saveRide(this.acceptedOrder, {
      onSuccess: (): void => {
        this.rideStarted = true;
      },
      onFailure: (appError: AppError): void => {
        this.messageDisplayer.displayAppError(appError);
      }
    });
  }

  private finishRide(): void {
    this.acceptedOrder.endTime = new Date();
    this.acceptedOrder.price = StringUtils.toNumber(this.ridePriceString);
    this.acceptedOrder.finished = true;
    this.rideService.saveRide(this.acceptedOrder, {
      onSuccess: (): void => {
        this.rideFinished = true;
      },
      onFailure: (appError: AppError): void => {
        this.messageDisplayer.displayAppError(appError);
      }
    });
  }

  private cancelRide(): void {
    if (ObjectUtils.isNotNullOrUndefined(this.acceptedOrder.startTime)) {
      this.acceptedOrder.endTime = new Date();
    }
    this.acceptedOrder.canceled = true;
    this.rideService.saveRide(this.acceptedOrder, {
      onSuccess: (): void => {
        this.rideFinished = true;
        this.ridePrice = 0;
        this.ridePriceString = "0";
      },
      onFailure: (appError: AppError): void => {
        this.messageDisplayer.displayAppError(appError);
      }
    });
  }

  public onStartRide(): void {
    this.startRide();
    let isDay = this.isDay();
    this.nodeJsTimer = setInterval(()=>{
      if (isDay) {
        this.ridePrice = this.ridePrice + ((this.pricePerHourDay/3600)*10) + ((this.pricePerKmDay/1000)*80);
      } else {
        this.ridePrice = this.ridePrice + ((this.pricePerHourNight/3600)*10) + ((this.pricePerKmNight/1000)*80);
      }
      this.ridePriceString = this.ridePrice.toPrecision(3);
    },10000);
  }

  public onFinishRide(): void {
    this.finishRide();
    clearInterval(this.nodeJsTimer);
  }

  public onCancelRide(): void {
    this.confirmationUtils.confirm("CONFIRM_CANCEL_ORDER", {
			approve: (): void => {
				this.cancelRide();
        if (ObjectUtils.isNotNullOrUndefined(this.nodeJsTimer)) {
          clearInterval(this.nodeJsTimer);
        }
			},
			reject: (): void => {}
		});
  }

  public onCloseWindow(): void {
    this.windowVisible = false;
		this.windowClosed.emit();
  }

}
