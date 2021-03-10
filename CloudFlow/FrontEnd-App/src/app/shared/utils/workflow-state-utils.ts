import { StringUtils } from "./string-utils";
import { WorkflowConstants } from "./../constants/workflow.constants";

export class WorkflowStateUtils {	

	public static isStateFound(stateCodes: string, stateCode: string): boolean {
		const normalizedStateCodes: string = WorkflowConstants.STEPS_SEPARATOR + stateCodes + WorkflowConstants.STEPS_SEPARATOR;
		const normalizedStateCode: string = WorkflowConstants.STEPS_SEPARATOR + stateCode + WorkflowConstants.STEPS_SEPARATOR;
		return StringUtils.contains(normalizedStateCodes, normalizedStateCode);
	}
}