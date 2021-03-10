export class TransitionRoutingTypeModel {
	
	public static readonly ROUTING_HIERARCHICAL_SUB: string = "HIERARCHICAL_SUB";
	public static readonly ROUTING_HIERARCHICAL_INF: string = "HIERARCHICAL_INF";
	public static readonly ROUTING_HIERARCHICAL_SUP: string = "HIERARCHICAL_SUP";
	public static readonly ROUTING_INITIATOR: string = "INITIATOR";
	public static readonly ROUTING_GROUP: string = "GROUP";
	public static readonly ROUTING_PREVIOUS: string = "PREVIOUS";
	public static readonly ROUTING_MANUAL: string = "MANUAL";
	public static readonly ROUTING_PARAMETER: string = "PARAMETER";
	public static readonly ROUTING_OU: string = "OU";
	public static readonly ROUTING_PARAMETER_HIERARCHICAL_SUP: string = "PARAMETER_HIERARCHICAL_SUP";


	public value: string;

	public constructor(value: string) {
		this.value = value;
	}

	public get labelCode(): string {
		return "ROUTING_" + this.value;
	}

	public static getRoutingTypes(): TransitionRoutingTypeModel[] {
		let routingTypes: TransitionRoutingTypeModel[] = [];
		routingTypes.push(new TransitionRoutingTypeModel(TransitionRoutingTypeModel.ROUTING_HIERARCHICAL_SUB));
		routingTypes.push(new TransitionRoutingTypeModel(TransitionRoutingTypeModel.ROUTING_HIERARCHICAL_INF));
		routingTypes.push(new TransitionRoutingTypeModel(TransitionRoutingTypeModel.ROUTING_HIERARCHICAL_SUP));
		routingTypes.push(new TransitionRoutingTypeModel(TransitionRoutingTypeModel.ROUTING_INITIATOR));
		routingTypes.push(new TransitionRoutingTypeModel(TransitionRoutingTypeModel.ROUTING_GROUP));
		routingTypes.push(new TransitionRoutingTypeModel(TransitionRoutingTypeModel.ROUTING_PREVIOUS));
		routingTypes.push(new TransitionRoutingTypeModel(TransitionRoutingTypeModel.ROUTING_MANUAL));
		routingTypes.push(new TransitionRoutingTypeModel(TransitionRoutingTypeModel.ROUTING_PARAMETER));
		routingTypes.push(new TransitionRoutingTypeModel(TransitionRoutingTypeModel.ROUTING_OU));
		routingTypes.push(new TransitionRoutingTypeModel(TransitionRoutingTypeModel.ROUTING_PARAMETER_HIERARCHICAL_SUP));
		return routingTypes;
	}
}