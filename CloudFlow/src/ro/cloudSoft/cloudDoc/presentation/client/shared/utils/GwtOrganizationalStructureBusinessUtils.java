package ro.cloudSoft.cloudDoc.presentation.client.shared.utils;

import java.util.Collection;

import ro.cloudSoft.cloudDoc.presentation.client.shared.model.ModelWithId;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.OrganizationUnitModel;
import ro.cloudSoft.cloudDoc.presentation.client.shared.model.organization.UserModel;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * Contine metode utilitare pentru reguli de business legate de structura organizatorica.
 */
public class GwtOrganizationalStructureBusinessUtils {

	/**
	 * Verifica daca o entitate organizatorica poate fi mutata intr-o alta entitate.
	 */
	public static boolean canMoveTo(ModelWithId entityToMove, ModelWithId destinationEntity,
			ModelWithId parentOfEntityToMove, Collection<ModelData> ancestorsOfDestinationEntity) {
		
		if ((entityToMove == null) || (destinationEntity == null)) {
			throw new IllegalArgumentException("Nici sursa, nici destinatia nu pot fi nule.");
		}
		
		if (!isEntityToMoveTheRightType(entityToMove)) {
			return false;
		}
		if (!isDestinationEntityForMoveTheRightType(destinationEntity)) {
			return false;
		}
		
		boolean isEntityToMoveTheDestination = GwtCompareUtils.areEqual(entityToMove.getId(), destinationEntity.getId());
		if (isEntityToMoveTheDestination) {
			return false;
		}
		
		if (parentOfEntityToMove == null) {
			throw new IllegalArgumentException("Parintele sursei nu poate fi null.");
		}
		boolean isDestinationTheCurrentParentOfEntityToMove = GwtCompareUtils.areEqual(destinationEntity.getId(), parentOfEntityToMove.getId());
		if (isDestinationTheCurrentParentOfEntityToMove) {
			return false;
		}
		
		boolean isEntityToMoveAnAncestorOfDestination = false;
		for (ModelData ancestorOfDestinationEntity : ancestorsOfDestinationEntity) {
			
			if (!(ancestorOfDestinationEntity instanceof ModelWithId)) {
				throw new IllegalArgumentException("Stramosii entitatii destinatie trebuie sa aiba ID pentru a putea fi verificati.");
			}
			ModelWithId ancestorOfDestinationEntityWithId = (ModelWithId) ancestorOfDestinationEntity;
			
			if (GwtCompareUtils.areEqual(entityToMove.getId(), ancestorOfDestinationEntityWithId.getId())) {
				isEntityToMoveAnAncestorOfDestination = true;
				break;
			}
		}
		if (isEntityToMoveAnAncestorOfDestination) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Verifica daca entitatea data poate fi mutata.
	 */
	public static boolean isEntityToMoveTheRightType(ModelData entityToMove) {
		return ((entityToMove instanceof OrganizationUnitModel) || (entityToMove instanceof UserModel));
	}
	
	/**
	 * Verifica daca entitatea data poate fi aleasa ca destinatie in mutarea unei alte entitati.
	 */
	public static boolean isDestinationEntityForMoveTheRightType(ModelData destinationEntity) {
		return ((destinationEntity instanceof OrganizationModel) || (destinationEntity instanceof OrganizationUnitModel));
	}
}