import { Component } from "@angular/core";
import { AppError, MessageDisplayer, ObjectUtils, ConfirmationUtils, DateUtils, OrganizationService, AclService, SecurityManagerModel, RidesService, ClientsService } from "@app/shared";
import { ClientModel } from "@app/shared/model/clients/client.model";
import { UserModel } from "@app/shared/model/organization/user.model";
import { RideModel } from "@app/shared/model/rides/ride.model";
import { ListItemUtils } from "@app/shared/utils/list-item-utils";

@Component({
  selector: 'app-ride',
  templateUrl: './ride.component.html',
  styleUrls: ['./ride.component.css']
})
export class RideComponent {

  private aclService: AclService;
  private ridesService: RidesService;
  private clientsService: ClientsService;
  private organizationService: OrganizationService;
	private messageDisplayer: MessageDisplayer;

  private securityManager: SecurityManagerModel;

	public rides: RideModel[];
	public selectedRide: RideModel;
  public clients: Map<Number,String>;

	public rideWindowVisible: boolean;
	public rideWindowMode: "add" | "edit";
	public rideWindowRideId: number;

	public editButtonDisabled: boolean;
  public loading: boolean;
  public tableVisible: boolean;

	public scrollHeight: string;

	public constructor(aclService: AclService, ridesService: RidesService, clientsService: ClientsService, organizationService: OrganizationService, messageDisplayer: MessageDisplayer) {
		this.aclService = aclService;
    this.ridesService = ridesService;
    this.clientsService = clientsService;
    this.organizationService = organizationService;
		this.messageDisplayer = messageDisplayer;
		this.init();
	}

	public async init(): Promise<void> {
		this.rideWindowVisible = false;
    this.tableVisible = false;
    this.loading = true;
		this.editButtonDisabled = true;
    this.rides = [];
    this.clients = new Map();
		this.scrollHeight = (window.innerHeight - 180) + "px";
    await this.getSecurityManager();
		await this.loadRides();
    this.loading = false;
    this.tableVisible = true;
	}

  private getSecurityManager(): Promise<void> {
    return new Promise<void>((resolve,reject) => {
      this.aclService.getSecurityManager({
        onSuccess: (securityManager: SecurityManagerModel): void => {
          this.securityManager = securityManager;
          resolve();
        },
        onFailure: (appError: AppError): void => {
          this.messageDisplayer.displayAppError(appError);
          reject();
        }
      });
      resolve();
		});
  }

	private loadRides(): Promise<void> {
    return new Promise<void>((resolve,reject) => {
      this.ridesService.getAllRides({
        onSuccess: async (rides: RideModel[]): Promise<void> => {
          ListItemUtils.sort(rides,"id");
          for (const ride of rides) {
            if (ObjectUtils.isNotNullOrUndefined(ride.dispatcherId)) {
              if (this.securityManager.userIdAsString === ride.dispatcherId.toString() && ride.finished !== true && ObjectUtils.isNullOrUndefined(ride.startTime)) {
                this.rides.push(ride);
                this.clients.set(ride.clientId, await this.getClientNameById(ride.clientId));
              }
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

  private getClientNameById(id: Number): Promise<String> {
    return new Promise<String>((resolve,reject) => {
      this.clientsService.getClientById(id, {
        onSuccess: (client: ClientModel): void => {
          this.organizationService.getUserById(client.userId, {
            onSuccess: (user: UserModel): void => {
              resolve(user.firstName + " " + user.lastName);
            },
            onFailure: (appError: AppError): void => {
              this.messageDisplayer.displayAppError(appError);
              reject("Error");
            }
          });
        },
        onFailure: (appError: AppError): void => {
          this.messageDisplayer.displayAppError(appError);
          reject("Error");
        }
      });
    });
  }

  public getClientNameByIdForDisplay(id: number): String {
    return this.clients.get(id);
  }

	public onAddRide(): void {
		this.rideWindowMode = "add";
		this.rideWindowVisible = true;
	}

	public onEditRide(): void {
		this.rideWindowMode = "edit";
		this.rideWindowRideId = this.selectedRide.id;
		this.rideWindowVisible = true;
	}

	public getDateForDisplay(date: Date): String {
		return DateUtils.formatForDisplay(date);
	}

	public onRideSelected(event: any): void {
		this.changePerspective();
	}

	public onRideUnselected(event: any): void {
		this.changePerspective();
	}

	private changePerspective(): void {
		this.editButtonDisabled = ObjectUtils.isNullOrUndefined(this.selectedRide);
	}

	public onRideWindowClosed(): void {
		this.rideWindowVisible = false;
    this.editButtonDisabled = true;
	}

	public async onRideWindowDataSaved(): Promise<void> {
    this.tableVisible = false;
    this.loading = true;
    this.rides = [];
		await this.loadRides();
    this.loading = false;
    this.tableVisible = true;
	}

}
