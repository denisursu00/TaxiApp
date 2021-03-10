import { JsonObject, JsonProperty } from "json2typescript";
import { OrganizationTreeNodeModel } from "./organization-tree-node.model";

@JsonObject
export class OrganizationTreeModel {
	
	@JsonProperty("rootNode", OrganizationTreeNodeModel)
	public rootNode: OrganizationTreeNodeModel = null;
}