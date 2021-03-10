import { MetadataDefinitionModel, WorkflowStateModel } from "@app/shared";

export class MetadataDefinitionInputData {

	public editMetadataDefinition: MetadataDefinitionModel;

	public metadataDefinitions: MetadataDefinitionModel[];

	public workflowStates: WorkflowStateModel[];
}