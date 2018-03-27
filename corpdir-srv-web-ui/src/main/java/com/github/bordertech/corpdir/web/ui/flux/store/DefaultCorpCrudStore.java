package com.github.bordertech.corpdir.web.ui.flux.store;

import com.github.bordertech.corpdir.api.common.ApiIdObject;
import com.github.bordertech.corpdir.web.ui.CorpEntityType;
import com.github.bordertech.flux.crud.store.impl.DefaultDataApiCrudStore;
import com.github.bordertech.corpdir.web.ui.flux.dataapi.CorpCrudDataApi;

/**
 * Default Corp Store with backing API.
 *
 * @param <T> the CorpDir API Object
 * @param <D> the CorpDir data API type
 * 
 * @author Jonathan Austin
 * @author Aswin Kandula
 */
public class DefaultCorpCrudStore<T extends ApiIdObject, D extends CorpCrudDataApi<T, ?, ?>> extends DefaultDataApiCrudStore<String, String, T, D> implements CorpCrudStore<T, D> {


	/**
	 * @param type the corp entity type
	 * @param api the backing API
	 */
	public DefaultCorpCrudStore(final CorpEntityType type, final D api) {
		super(type.getStoreKey(), CorpEntityType.getLinkedCreators(type), api);
	}
}
