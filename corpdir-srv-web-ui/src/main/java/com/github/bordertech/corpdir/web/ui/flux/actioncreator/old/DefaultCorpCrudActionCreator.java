package com.github.bordertech.corpdir.web.ui.flux.actioncreator.old;

import com.github.bordertech.corpdir.api.common.ApiIdObject;
import com.github.bordertech.corpdir.web.ui.CorpEntityType;
import com.github.bordertech.corpdir.web.ui.flux.dataapi.old.CorpCrudDataApi;
import com.github.bordertech.flux.crud.actioncreator.impl.DefaultDataApiCrudActionCreator;

/**
 * Corp CRUD Action Creator with defined types.
 *
 * @param <T> the Corp API object type
 * @param <D> the backing Corp API
 *
 * @author jonathan
 * @deprecated use {@link com.github.bordertech.corpdir.web.ui.flux.actioncreator.DefaultCorpCrudActionCreator}
 */
@Deprecated
public class DefaultCorpCrudActionCreator<T extends ApiIdObject, D extends CorpCrudDataApi<T, ?>> extends DefaultDataApiCrudActionCreator<String, T, D> implements CorpCrudActionCreator<T, D> {

	/**
	 * @param type the entity type
	 * @param api the backing data API
	 */
	public DefaultCorpCrudActionCreator(final CorpEntityType type, D api) {
		super(type.getActionCreatorKey(), api);
	}
}