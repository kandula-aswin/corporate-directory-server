package com.github.bordertech.corpdir.web.ui.flux.actioncreator.impl;

import com.github.bordertech.corpdir.api.v1.model.UnitType;
import com.github.bordertech.corpdir.web.ui.CorpEntityType;
import com.github.bordertech.corpdir.web.ui.flux.actioncreator.temp.DefaultCorpCrudActionCreatorTemp;
import com.github.bordertech.corpdir.web.ui.flux.dataapi.impl.UnitTypeApi;
import javax.inject.Inject;

/**
 * Unit Type action creator implementation.
 *
 * @author jonathan
 */
public class UnitTypeActionCreator extends DefaultCorpCrudActionCreatorTemp<UnitType, UnitTypeApi> {

	/**
	 * @param api the backing API
	 */
	@Inject
	public UnitTypeActionCreator(final UnitTypeApi api) {
		super(CorpEntityType.UNIT_TYPE, api);
	}
}
