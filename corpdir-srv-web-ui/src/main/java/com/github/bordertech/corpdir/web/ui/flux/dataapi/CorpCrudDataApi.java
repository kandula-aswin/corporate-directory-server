package com.github.bordertech.corpdir.web.ui.flux.dataapi;

import com.github.bordertech.corpdir.api.common.ApiIdObject;
import com.github.bordertech.corpdir.api.service.BasicIdService;
import com.github.bordertech.flux.crud.dataapi.CrudApi;

/**
 * Corp CRUD API with defined types.
 *
 * @param <T> the Corp API Object
 * @param <S> the Corp backing read and write Service
 * 
 * @author Jonathan Austin
 */
public interface CorpCrudDataApi<T extends ApiIdObject, S extends BasicIdService<T>> extends CrudApi<String, String, T> {

	Class<T> getApiClass();

	S getService();
}
