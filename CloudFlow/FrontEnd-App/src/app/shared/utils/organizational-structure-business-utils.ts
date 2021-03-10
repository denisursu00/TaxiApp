import { OrganizationUnitModel } from "../model/organization/organization-unit.model";
import { OrganizationTreeNodeModel } from "../model/organization-tree-node.model";
import { OrganizationModel } from "../model/organization/organization.model";
import { UserModel } from "../model/organization/user.model";
import { StringUtils } from "./string-utils";
import { ObjectUtils } from "./object-utils";

export class OrganizationalStructureBusinessUtils {

	public static canMoveTo(entityToMove: OrganizationTreeNodeModel, destinationEntity: OrganizationTreeNodeModel, 
			childrenOfEntityToMove: OrganizationTreeNodeModel[], parentIdOfEntityToMove: string): boolean {
		
		if (ObjectUtils.isNullOrUndefined(entityToMove) || ObjectUtils.isNullOrUndefined(destinationEntity)) {
			throw new Error("Nici sursa, nici destinatia nu pot fi nule.");
		}
		
		if (!this.isEntityToMoveTheRightType(entityToMove)) {
			return false;
		}
		if (!this.isDestinationEntityForMoveTheRightType(destinationEntity)) {
			return false;
		}
		
		let isEntityToMoveTheDestination: boolean = entityToMove.id === destinationEntity.id;
		if (isEntityToMoveTheDestination) {
			return false;
		}
		
		if (StringUtils.isNotBlank(parentIdOfEntityToMove)) {
			let isDestinationTheCurrentParentOfEntityToMove: boolean = destinationEntity.id === parentIdOfEntityToMove;
			if (isDestinationTheCurrentParentOfEntityToMove) {
				return false;
			}
		}
		
		let isDestinationAChildOfEntityToMove: boolean = false;
		for (let childOfEntityToMove of childrenOfEntityToMove) {
			if (destinationEntity.id === childOfEntityToMove.id) {
				isDestinationAChildOfEntityToMove = true;
				break;
			}
		}
		
		if (isDestinationAChildOfEntityToMove) {
			return false;
		}
		
		return true;
	}

	public static isEntityToMoveTheRightType(entityToMove: OrganizationTreeNodeModel): boolean {
		return ((entityToMove.isOrganizationUnit()) || (entityToMove.isUser()));
	}
	
	public static isDestinationEntityForMoveTheRightType(destinationEntity: OrganizationTreeNodeModel): boolean {
		return ((destinationEntity.isOrganization()) || (destinationEntity.isOrganizationUnit()));
	}
}