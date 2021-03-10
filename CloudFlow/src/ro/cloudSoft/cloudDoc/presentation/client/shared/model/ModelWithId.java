package ro.cloudSoft.cloudDoc.presentation.client.shared.model;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * Reprezinta un model care dispune de un identificator.
 */
public interface ModelWithId extends ModelData {

	Object getId();
}